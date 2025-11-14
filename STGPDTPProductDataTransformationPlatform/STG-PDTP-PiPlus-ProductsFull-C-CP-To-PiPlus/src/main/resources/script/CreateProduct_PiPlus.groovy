/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */
import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder
import src.main.resources.script.MappingLogger
import src.main.resources.script.ProductBuilder

def Message processData(Message message) {

	MappingLogger logger = new MappingLogger(message)

	// Payloads
	Map root = message.getBody()
	Map classTree = message.getProperty('ClassTreePayload')

	// Headers
	def targetCountry = message.getHeader('XTargetCountry', String) ?: 'XTargetCountry missing'
	def targetLanguage = message.getHeader('XTargetLanguage', String) ?: 'XTargetLanguage missing'
	def targetPublicationID = message.getHeader('XTargetPublicationID', String) ?: 'XTargetPublicationID missing'
	def sourcePiDataOrganization = message.getHeader('XSourcePiDataOrganization', String) ?: 'XSourcePiDataOrganization missing'
	def sourcePiDataVersion = message.getHeader('XSourcePiDataVersion', String) ?: 'XSourcePiDataVersion missing'

	Map unitMapping = new JsonSlurper().parseText(message.getProperty('XUnitMapping'))

	def catalogVersion = '3.1' // ensured: should be statically '3.1', see MIELEC2P-518
	def catalogActualDate = (new Date()).format("yyyy-MM-dd")

	List articles = root.value.findAll { it.externalObjectTypeCode == 'obj_article' }
	List products = root.value.findAll { it.externalObjectTypeCode == 'obj_product' }

	def outputStream = new ByteArrayOutputStream()
	def indentPrinter = new IndentPrinter(new BufferedWriter(new OutputStreamWriter(outputStream, 'UTF-8')), '', false)
	MarkupBuilder builder = new MarkupBuilder(indentPrinter)

	// Instantiate ProductBuilder only once as it executes time consuming operation to build the map for Product to Class references
	ProductBuilder pb = new ProductBuilder(products, message, logger, classTree)

	if(!articles.size()){
		throw new Exception('Missing articles in products payload in ProductsFull flow');
	}

	// Build target XML payload
	builder.MIELE_PI_PLUS(country: targetCountry, language: targetLanguage, date: catalogActualDate, version: catalogVersion, catalog: targetPublicationID) {
		articles.each { article ->
			logger.info("--- Begin processing article ${article.code} ---")
			pb.setArticleDetails(builder, article)
			def matNum = article.formattedCode
			def sparte = pb.getFeatureByCodeAndPosition('atr_SPART', 1)
			def prodGrpName = pb.getFeatureByCodeAndPosition('atr_productgroup', 1)
			def ean = pb.getFeatureByCodeAndPosition('atr_EAN11', 1)
			Map commStructureClassForArticle = pb.productToClassReferences.get('COMMUNICATION_STRUCTURE').get(article.code)
			logger.debug("Article ${article.code} belongs to communication structure class ${commStructureClassForArticle?.code}")
			if (!commStructureClassForArticle) {
				logger.log("[WARNING] No Area ID for article ${article.code} as it does not belong to any communication structure class")
			}
			String formattedAid = pb.getAreaID(commStructureClassForArticle)

			Map<String, List> piDataGroups = pb.processPiDataGroups()
			int counter = 1

			MATERIAL(materialNumber: matNum, sparte: sparte?.formattedValue, productGroup: prodGrpName?.formattedValue, ean: ean?.formattedValue, areaID: formattedAid) {
				Map piDataAttributes = [version: "${sourcePiDataOrganization}_${sourcePiDataVersion}", catalog: targetPublicationID]
				addProductGroupDetails(pb, article.code, piDataAttributes, logger)
				PI_DATA(piDataAttributes) {
					BASICS() {
						pb.buildPiAttributes(piDataGroups.get('Basics'))
					}
					ENERGY_LABEL_INFORMATION() {
						pb.buildPiAttributes(piDataGroups.get('Energy Label Information'))
					}
					SMART_INFORMATION() {
						pb.buildPiAttributes(piDataGroups.get('Smart Information'))
					}
					FEATURES() {
						pb.buildPiAttributes(piDataGroups.get('Features'))
					}
					MC() {
						counter = pb.buildPiAttributes(piDataGroups.get('MC'))
					}
				}

				PLUS_DATA {
					BENEFITS {
						counter = pb.buildBenefitsSorted(counter)
					}

					ADDITIONAL_PRODUCTDATA {
						counter = pb.buildAdditionalProductData_Media(counter)
						counter = pb.buildAdditionalProductData_References(counter)
						counter = pb.buildAdditionalProductData_Legal_Datasheet(counter)
					}

					VG_SPECIFIC_DATA {
						counter = pb.buildSpecificData(counter)
					}

					PRODUCTDETAILS {
						pb.buildProductDetails(unitMapping)
					}

				}

			}
		}
	}

	message.setBody(outputStream)

	if (logger.getEntries().size()) {
		storeWarningsInAttachment(logger.getEntries(), messageLogFactory?.getMessageLog(message))
	}

	return message
}

void storeWarningsInAttachment(List entries, messageLog) {
	if (messageLog) {
		StringBuilder sb = new StringBuilder()
		entries.each { sb << "${it}\r\n" }
		messageLog.addAttachmentAsString('Mapping Warnings - Product Full', sb.toString(), 'text/plain')
	}
}

void addProductGroupDetails(ProductBuilder productBuilder, String articleCode, Map attributes, MappingLogger logger) {
	Map productGroupClassForArticle = productBuilder.productToClassReferences.get('PI_STRUCTURE').get(articleCode)
	if (productGroupClassForArticle) {
		attributes.put('productGroup', productGroupClassForArticle.name)
		if (productGroupClassForArticle.code.contains('#')) {
			List productGroupDetails = productGroupClassForArticle.code.split('#')
			if (productGroupDetails.size() == 2) {
				attributes.put('productGroupID', productGroupDetails[0])
				attributes.put('productGroupVersion', productGroupDetails[1])
			}
		}
	} else {
		logger.warn("PI_STRUCTURE class for article ${articleCode} not found")
	}
}