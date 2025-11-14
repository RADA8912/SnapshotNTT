/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */


import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import src.main.resources.script.MappingLogger
import src.main.resources.script.MaterialBuilder

def Message processData(Message message) {
	Map root = message.getBody()
	MappingLogger logger = new MappingLogger(message)
	def sourceLanguage = message.getHeader('XSourceCurrentLanguage', String)

	// Construct Map for prices and release memory reference to message property
	Map uniquePrices = MaterialBuilder.constructPricesReference(message.getProperty('PricesPayload'), sourceLanguage, message.getHeader('XSourcePriceCurrency', String))
	message.setProperty('PricesPayload', '')

	// Get all articles that have references to other articles
	def products = root.value.findAll { it.productReferences.size() }

	def outputStream = new ByteArrayOutputStream()
	def indentPrinter = new IndentPrinter(new BufferedWriter(new OutputStreamWriter(outputStream, 'UTF-8')), '', false)
	MarkupBuilder builder = new MarkupBuilder(indentPrinter)

	builder.Match {
		products.each { product ->
			logger.info("--- Begin processing product ${product.code} ---")
			MaterialBuilder materialBuilder = new MaterialBuilder(builder, message, logger, uniquePrices)
			builder.matchingMaterialDetail(matNo: product.formattedCode) {
				materialBuilder.buildByType(product, 'additionalAccessories')
				materialBuilder.buildByType(product, 'includedAccessories')
				materialBuilder.buildByType(product, 'sellMandatoryAccessoriesCare')
				materialBuilder.buildByType(product, 'sellRecommendedAccessoriesCare')
				materialBuilder.buildByType(product, 'sellRecommendedMSC')
				materialBuilder.buildByType(product, 'matchingProducts')
				materialBuilder.buildByType(product, 'sellAdditionalService')
			}
		}
	}

	message.setBody(outputStream)
	// Free up references to payloads
	message.setProperty('AvailabilityPayload', '')

	if (logger.getEntries().size()) {
		message.setProperty('LogEntries', logger.getEntries())
	}
	return message
}