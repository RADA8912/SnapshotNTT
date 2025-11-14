/*
* Copyright (c) 2020 Miele & Cie. KG - All rights reserved.
*/

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.xml.StreamingMarkupBuilder
import src.main.resources.script.FeaturesBuilder
import src.main.resources.script.LanguageConverter
import src.main.resources.script.PublicationHandler

Message processData(Message message) {
	Reader reader = message.getBody(Reader)

	def root = new XmlParser().parse(reader)
	def products = root.'**'.findAll {
		it.@UserTypeID == 'obj_product'
	}
	def articles = root.'**'.findAll {
		it.@UserTypeID == 'obj_article'
	}

	if (products.size() || articles.size()) {
		Writable writable = new StreamingMarkupBuilder().bind { XMLBuilder ->
			XMLBuilder.batchParts {
				XMLBuilder.batchChangeSet1 {
					new BatchChangeSetPartBuilder(products, articles, XMLBuilder, message).build()
				}
			}
		} as Writable

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
		writable.writeTo(new BufferedWriter(new OutputStreamWriter(outputStream, 'UTF-8')))
		message.setBody(new ByteArrayInputStream(outputStream.toByteArray()))
	}
	return message
}

class BatchChangeSetPartBuilder {
	def products
	def articles
	def XMLBuilder
	LanguageConverter languageConverter
	FeaturesBuilder featuresBuilder
	def contentObjectPrefix
	def publicationHandler
	def importCountry

	BatchChangeSetPartBuilder(products, articles, XMLBuilder, Message message) {
		this.products = products
		this.articles = articles
		this.XMLBuilder = XMLBuilder
		def catalogLanguages = message.getHeader('XCatalogLanguages', String)
		def languageMapping = message.getHeader('XLanguageMapping', String)
		this.importCountry = message.getHeader('XImportCountry', String)
		this.languageConverter = LanguageConverter.newConverter(catalogLanguages, languageMapping)
		this.featuresBuilder = new FeaturesBuilder()
		this.contentObjectPrefix = message.getHeader('XContentObjectPrefix', String)
		this.publicationHandler = new PublicationHandler(new JsonSlurper().parseText(message.getHeader('XCatalogVersions', String)), message.getHeader('XPublicationSplit', Boolean), this.languageConverter)
	}

	def build() {
		products.each { parent ->
			parent.'*'.findAll { p ->
				p.name() == "Product" && p.@UserTypeID.equals('obj_article')
			}.each {
				article -> buildProductVersion(article, parent)
			}
			buildProductVersion(parent, null)
		}
		// Import also articles that are not directly under an obj_product
		List orphanedArticles = articles.findAll { Node article -> article.parent().@UserTypeID != 'obj_product' }
		orphanedArticles.each { article ->
			buildProductVersion(article, null)
		}
	}

	def buildProductVersion(aProduct, aParent) {
		String catalogVersion = this.publicationHandler.getProductPublicationCatalogVersion(aProduct)
		if (catalogVersion) {
			XMLBuilder.batchChangeSetPart1 {
				method('POST')
				uri('Product')
				XMLBuilder.body {
					XMLBuilder.Product {
						XMLBuilder.Product {
							XMLBuilder.catalogVersion_ID(catalogVersion)
							code(aProduct.@ID)
							externalObjectTypeCode(aProduct.@UserTypeID)
							formattedCode(formattedProductCodeOf(aProduct))
							parentCode(aParent ? aParent.@ID : "")
							productTypeCode(productTypeOf(aProduct))
							orderableList((aProduct.@UserTypeID == 'obj_article') ? this.publicationHandler.getOrderableListString() : '[]')
							publishedDates(this.publicationHandler.getPublishedDatesString(aProduct.@UserTypeID, catalogVersion))
							XMLBuilder.productFeatures {
								featuresBuilder.build(XMLBuilder, "ProductFeature", aProduct.Values, languageConverter)
							}
							ean(aProduct.Values.Value.find { it.@AttributeID == 'atr_EAN11' })
							productDivisionCode((aProduct.@UserTypeID == 'obj_article') ? aProduct.Values.Value.find { it.@AttributeID == 'atr_SPART' } : '')
							productHierarchyCode((aProduct.@UserTypeID == 'obj_article') ? aProduct.Values.Value.find { it.@AttributeID == 'atr_PRDHA' } : '')
							objectLifeCycleStatusCode(getLifecycleStatus(aProduct))
							new ReferencesBuilder(XMLBuilder, aProduct, aParent, contentObjectPrefix, articles, this.publicationHandler, this.languageConverter).build()
							new TextsBuilder(XMLBuilder, aProduct, languageConverter).build()
						}
					}
				}
			}
		}
	}


	def formattedProductCodeOf(product) {
		def valueWithFormattedProductID = product.Values.Value.find { it.@AttributeID == 'atr_MATNR' }
		return valueWithFormattedProductID == null ? "" : valueWithFormattedProductID
	}

	def productTypeOf(product) {
		def valueElement = product.Values.Value.find {
			value -> value.@AttributeID.equals('atr_type_of_material')
		}
		return valueElement == null ? "" : valueElement.@ID
	}

	String getLifecycleStatus(product) {
		if(product.@UserTypeID != 'obj_article') {
			return ''
		}
		def attributeID = 'atr_lifecycle_article'
		def value = product.Values.Value.find { it -> it.name() == 'Value' && it.@AttributeID == attributeID && it.@QualifierID == this.importCountry }
		if (!value) {
			//fallback
			value = product.Values.Value.find { it -> it.name() == 'Value' && it.@AttributeID == attributeID && it.@QualifierID == 'AllCountries' }
		}
		if(!value) {
			value = product.Values.ValueGroup.find { it.@AttributeID == attributeID }?.Value?.find { it.@QualifierID == this.importCountry }
			//fallback
			if(!value){
				value = product.Values.ValueGroup.find { it.@AttributeID == attributeID }?.Value?.find { it.@QualifierID == 'AllCountries' }
			}
		}
		return value?.text()
	}
}

class ReferencesBuilder {
	final def XMLBuilder
	final def product
	final def parent
	final def assetRefSet = new HashSet<>()
	final def contentObjectPrefix
	final def articles
	final def publicationHandler
	final def languageConverter

	ReferencesBuilder(XMLBuilder, product, parent, contentObjectPrefix, articles, publicationHandler, languageConverter) {
		this.XMLBuilder = XMLBuilder
		this.product = product
		this.parent = parent
		this.contentObjectPrefix = contentObjectPrefix
		this.articles = articles
		this.publicationHandler = publicationHandler
		this.languageConverter = languageConverter
	}

	def build() {
		buildMediaReference()
		buildContentObjectReference()
		buildProductReference()
	}

	def buildProductReference() {
		XMLBuilder.productReferences {
			product.ProductCrossReference.findAll { productCrossReference ->
				isProduct(productCrossReference)
			}.each { productRef ->
				// We will create all references without checking if both source and target are in the same publication
				// A separate API call from main IFlow will perform the inconsistency clean up at the end of import
				ProductReference {
					targetCode(productRef.@ProductID)
					referenceTypeCode(productRef.@Type)
					buildSortOrder(productRef)
				}
			}
		}
	}

	def buildMediaReference() {
		XMLBuilder.productMediaReferences {
			product.AssetCrossReference.each { asset ->
				List languages = (asset.@QualifierID) ? this.languageConverter.languageMapping[asset.@QualifierID]?.languages : ['']
				languages.each {assetLanguageCode ->
					// avoid duplicates on AssetID, Type and language elements
					def assetRef = "${asset.@AssetID}_${asset.@Type}_${assetLanguageCode}"
					if (!assetRefSet.contains(assetRef)) {
						XMLBuilder.ProductMediaReference {
							targetCode(asset.@AssetID)
							referenceTypeCode(asset.@Type)
							buildSortOrder(asset)
							languageCode(assetLanguageCode)
						}
						assetRefSet.add(assetRef)
					}
				}
			}
		}
	}

	def buildContentObjectReference() {
		XMLBuilder.productContentObjectReferences {
			product.ProductCrossReference.findAll { productCrossReference ->
				isContentObject(productCrossReference.@ProductID.toString())
			}.each { contentObjectReference ->
				ProductContentObjectReference {
					targetCode(contentObjectReference.@ProductID)
					referenceTypeCode(contentObjectReference.@Type)
					buildSortOrder(contentObjectReference)
				}
			}
		}
	}

	def buildSortOrder(element) {
		def sortOrderNumber = element.MetaData.Value.find { it.@AttributeID == 'atr_sort_order' }
		if (!sortOrderNumber) // skipping if non existent
		{
			return
		}
		XMLBuilder.sortOrder(sortOrderNumber.text())
	}

	boolean isContentObject(id) {
		final prefixes = contentObjectPrefix.split(",").collect()
		String inputPrefix = id.replaceFirst(/(\w+)_0*(\d+)_\d+/, '$1')
		return (inputPrefix in prefixes)
	}

	boolean isProduct(reference) {
		return (reference.@ProductID.isNumber() && reference.@ProductID.length() == 18)
	}

}

class TextsBuilder {
	def builder
	LanguageConverter languageConverter
	def product
	def descriptionLocaleMap = [:]

	TextsBuilder(builder, product, LanguageConverter languageConverter) {
		this.builder = builder
		this.languageConverter = languageConverter
		this.product = product
		buildDescriptionLocalMap()
	}

	private void buildDescriptionLocalMap() {
		def descConvertedPart1 = languageConverter.convert(
				product.Values.ValueGroup.find { it.@AttributeID == 'atr_specific_content_des_type_text_calc' }?.Value?.collect()?.toArray()
		)
		def descConvertedPart2 = languageConverter.convert(
				product.Values.ValueGroup.find { it.@AttributeID == 'atr_specific_content_short_pos_text_calc' }?.Value?.collect()?.toArray()
		)
		descConvertedPart1.each { part1 ->
			def part2 = descConvertedPart2.find { it.get('lang') == part1.get('lang') }
			if (part2) {
				def localizedDescription = part1.get('node').text().concat(part2.get('node').text())
				def lang = part1.get('lang')
				descriptionLocaleMap.put(lang, localizedDescription)
			}
		}
	}

	def build() {
		List<Map> convertedNames = languageConverter.convert(product.Name.collect().toArray())
		builder.texts {
			convertedNames.each { convertedName ->
				ProductTexts {
					String language = convertedName.get('lang')
					name(convertedName.get('node'))
					locale(language)
					def localizedDescription = descriptionLocaleMap.get(convertedName.get('lang'))
					if (localizedDescription) {
						description(localizedDescription)
					}
					originQualifier(convertedName.get('qualifiers'))
				}
			}
		}
	}
}