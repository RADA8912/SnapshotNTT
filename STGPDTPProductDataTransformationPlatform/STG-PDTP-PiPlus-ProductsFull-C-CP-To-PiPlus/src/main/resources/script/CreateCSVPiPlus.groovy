/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import src.main.resources.script.MappingLogger
import src.main.resources.script.ProductBuilder
import org.apache.commons.text.StringEscapeUtils

def Message processData(Message message) {

	Map classRoot = message.getProperty('ClassTreePayload')
	Map root = message.getBody()
	List articles = root.value.findAll { it.externalObjectTypeCode == 'obj_article' }
	List products = root.value.findAll { it.externalObjectTypeCode == 'obj_product' }

	def targetLanguage = message.getHeader('XTargetLanguage', String)
	def targetCountry = message.getHeader('XTargetCountry', String)
	def sourcePiDataOrganization = message.getHeader('XSourcePiDataOrganization', String)
	def sourcePiDataVersion = message.getHeader('XSourcePiDataVersion', String)

	List classificationClasses = classRoot.value.findAll { it.externalObjectTypeCode == 'obj_PI_class_PRODUCT_GROUP' }

	MappingLogger logger = new MappingLogger(message)
	ProductBuilder productBuilder = new ProductBuilder(products, message, logger, classRoot)

	def outputStream = new ByteArrayOutputStream()
	def indentPrinter = new IndentPrinter(new BufferedWriter(new OutputStreamWriter(outputStream, 'UTF-8')), '', false)
	MarkupBuilder builder = new MarkupBuilder(indentPrinter)

	builder.FILES {
		classificationClasses.each { classificationClass ->
			def productGroup = classificationClass.name
			def csvBuilder = new CSVBuilder(classificationClass, message, articles, logger)
			csvBuilder.buildHeader()
			csvBuilder.buildProductLines(productBuilder)
			builder.FILE(fileName: "Miele_PI_PLUS_${productGroup}_${sourcePiDataOrganization}_${sourcePiDataVersion}_${targetCountry}_${targetLanguage.take(2).toUpperCase()}_3.1", csvBuilder.getResult())
		}
	}

	message.setBody(outputStream)
	// Free up references to payloads
	message.setProperty('ProgramLinesPayload', '')
	message.setProperty('ClassTreePayload', '')
	message.setProperty('PricesPayload', '')
	if (logger.getEntries().size()) {
		storeLogsInAttachment(logger.getEntries(), messageLogFactory?.getMessageLog(message))
	}
	return message
}

void storeLogsInAttachment(List entries, messageLog) {
	if (messageLog) {
		StringBuilder sb = new StringBuilder()
		entries.each { sb << "${it}\r\n" }
		messageLog.addAttachmentAsString('Mapping Warnings - Product Group CSV', sb.toString(), 'text/plain')
	}
}

class CSVBuilder {

	def header = []
	def rows = []
	Map attributeCodesOrder = [:]
	def classificationClass
	def separator = ';'
	def file = new File('MIELE_PI')
	Message message
	def products
	MappingLogger logger


	def CSVBuilder(classificationClass, message, products, logger) {
		this.classificationClass = classificationClass
		this.message = message
		this.products = products
		this.logger = logger
	}

	def buildHeader() {

		this.header << "MATNR"

		this.buildHeaderFromAttributes()

		for (int i = 7; i < 21; i++) {
			header << "USP_${i}_Picture"
			header << "USP_${i}_Icon"
		}
		header << "Price"
		header << "Miele_Direct_Selling_Price"
		header << "Energylabel_Icon"
		header << "Award_1"
		header << "Award_2"
		header << "Award_3"
		header << "Manual"
		header << "EU-Datasheet"
		header << "EU-Energylabel_PDF"
		header << "EU-Energylabel_PNG"
		header << "EU-Onlinelabel"
		for (int i = 1; i < 41; i++) {
			header << "Productbenefit${i}_ID"
			header << "Productbenefit${i}_picture"
			header << "Productbenefit${i}_video"
			header << "Productbenefit${i}_label"
			header << "Productbenefit${i}_headline"
			header << "Productbenefit${i}_text"
			header << "Productbenefit${i}_longtext"
			header << "Productbenefit${i}_footnote"
			header << "Productbenefit${i}_category"
			header << "Productbenefit${i}_highlight"
			header << "Productbenefit${i}_corefeature"
			header << "Productbenefit${i}_sequence_no"
			header << "Productbenefit${i}_onlyMiele"
		}
		for (int i = 1; i < 11; i++) {
			header << "Added_Info${i}_ID"
			header << "Added_Info${i}_label"
			header << "Added_Info${i}_text"
		}
		header << "Hierarchie_label"
		header << "Hierarchie_id"
		for (int i = 1; i < 11; i++) {
			header << "Productvideo${i}"
		}
		for (int i = 1; i < 41; i++) {
			header << "Installation_drawing${i}"
		}
		header << "Guarantee_booklet1"
		header << "Water_protection_guarantee_1"
		for (int i = 1; i < 7; i++) {
			header << "ProdFeat${i}_text"
			header << "ProdFeat${i}_reference"
		}
		header << "Designtype"
		header << "Shortpos"
		for (int i = 1; i < 8; i++) {
			header << "Legal_Datasheet${i}_Type"
			header << "Legal_Datasheet${i}"
		}
		header << "Sales_Status"
		header << "Valid_From"
		header << "Valid_To"
		header << "Web_Chooser_Attribute"
		header << "Price_per_Unit"
		for (int i = 1; i < 6; i++) {
			header << "PROGRAMMREIHE_${i}"
		}
		header << "PRODTYPE_FOOTNOTE"
	}

	def buildProductLines(ProductBuilder productBuilder) {

		this.classificationClass.classificationClassProductReferences.each { productReference ->

			def counter
			def row = []

			if (productReference.target) {

				Map product = this.products.find { it.code == productReference.targetCode }

				if (product) {

					this.buildProduct(product, row, productBuilder)
					def stringWriter = new StringWriter()
					def indentPrinter = new IndentPrinter(stringWriter, '', false)
					MarkupBuilder builder = new MarkupBuilder(indentPrinter)

					productBuilder.setArticleDetails(builder, product)
					builder.LINE {
						counter = productBuilder.buildBenefits_USP_Pictures(0)
						counter = productBuilder.buildBenefits_Price(0)
						counter = productBuilder.buildBenefits_Energylabel_Icon(0)
						counter = productBuilder.buildBenefits_Award(0)
						counter = productBuilder.buildBenefits_Manual(0)
						counter = productBuilder.buildBenefits_EU_Datasheet(0)
						counter = productBuilder.buildBenefits_EU_Label(0)
						counter = productBuilder.buildBenefits_AttributeSets(0)
						counter = productBuilder.buildBenefits_AddedInfo(0)
						counter = productBuilder.buildBenefits_Hierarchie(0)
						counter = productBuilder.buildAdditionalProductData_Media(0)
						counter = productBuilder.buildAdditionalProductData_References(0)
						counter = productBuilder.buildAdditionalProductData_Legal_Datasheet(0)
						counter = productBuilder.buildSpecificData(0)
					}

					def root = new XmlSlurper().parseText(stringWriter.toString())

					root.children().each { node ->
						row << "'${node.text()}'"
					}

					this.rows << row
					row.removeAll()
				}
			}
		}
	}

	private def buildProduct(productReference, List row, ProductBuilder productBuilder) {
		row << "${productReference.formattedCode}"
		this.logger.debug("Attribute codes for article - ${productReference.formattedCode}")
		productReference.productFeatures.addAll(productReference?.productFeaturesPiStructure)
		productReference.productFeatures.addAll(productReference?.productFeaturesLegendsStructure)
		
		this.attributeCodesOrder.each { codesOrder ->
			String value
			if (productBuilder.getMultiValuedAttributes().contains(codesOrder.value.code)) {
				List filteredPiFeatures = productReference.productFeaturesPiStructure?.findAll { it.attributeCode == codesOrder.value.code }
				value = filteredPiFeatures*.formattedValue.sort().join(', ')
			} else {
				value = productReference.productFeatures?.find { productFeature ->
					productFeature.attributeCode == codesOrder.value.code
				}?.formattedValue				
			}
			if (value) {
				// Add URL parameters for PRODUCT_IMAGE and ADD_IMAGE
				value = productBuilder.addURLParameters(codesOrder.value.propertyKey, value)
			}
			this.logger.debug("${codesOrder.key} - ${codesOrder.value.propertyKey} - ${codesOrder.value.code} - ${value}")
			row << "'${value}'"
		}
	}

	def getResult() {

		if (this.logger.getEntries().size()) {
			message.setProperty('LogEntries', logger.getEntries())
		}

		file.withWriter { writer ->
			writer.writeLine(header.join(this.separator).toString())
			rows.each { row ->
				writer.writeLine(row.join(this.separator).toString().replaceAll('null', ''))
			}
		}
		def fileText = file.getText('UTF-8')
		def unescaped = StringEscapeUtils.unescapeHtml3(fileText)
		unescaped = StringEscapeUtils.unescapeXml(unescaped)
		file.delete()
		return unescaped
	}

	private def buildHeaderFromAttributes() {
		Map piOrder = [:]
		this.classificationClass.classificationClassAttributeAssignments?.find { attributeAssignment ->
			def key = attributeAssignment.classificationClassAttributeAssignmentMetaData.find { metaData ->
				metaData.attributeCode == 'atr_PI_PROPERTY_KEY'
			}?.value
			def order = attributeAssignment.classificationClassAttributeAssignmentMetaData.find { metaData ->
				metaData.attributeCode == 'atr_PIM_PI_ORDER'
			}?.value
			if (key && order && !piOrder.containsKey(order.toInteger())) {
				piOrder.put(order.toInteger(), [propertyKey: key, code: attributeAssignment.attributeCode])
			}
		}
		this.attributeCodesOrder = piOrder.sort { it.key }
		this.attributeCodesOrder.each { it ->
			this.header << it.value.propertyKey
		}
	}
}