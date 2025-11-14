/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.StreamingMarkupBuilder

import java.text.SimpleDateFormat

Message processData(Message message) {
	String exportTime = message.getProperty('ExportTime')
	String fileName = message.getHeader('CamelFileNameOnly', String)
	def timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
			.format(new SimpleDateFormat('yyyy-MM-dd HH:mm:ss').parse(exportTime))
	def timestampForCode = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date())
	def code = "${fileName}_${timestampForCode}"

	Map catalogCodes = message.getProperty('CatalogCodes')

	Map catalogCodeToId = message.getProperty('CatalogCodeToId')

	Writable writable = new StreamingMarkupBuilder().bind { builder ->
		builder.batchParts {
			builder.batchChangeSet1 {
				catalogCodes.each { catalogCode, catalogName ->
					builder.batchChangeSetPart1 {
						builder.method('POST')
						builder.uri('CatalogVersion')
						builder.body {
							builder.CatalogVersion {
								builder.CatalogVersion {
									builder.baseVersionTS(timestamp)
									builder.catalog_code(catalogCode)
									builder.code(code)
									builder.catalog_ID(catalogCodeToId.get(catalogCode))
								}
							}
						}
					}
				}
			}
		}
	}
	def outputStream = new ByteArrayOutputStream()
	writable.writeTo(new BufferedWriter(new OutputStreamWriter(outputStream, 'UTF-8')))
	message.setBody(outputStream)

	// Log CatalogVersion.code to Custom Header
	def messageLog = messageLogFactory?.getMessageLog(message)
	if (messageLog) {
		messageLog.addCustomHeaderProperty('CatalogVersionCode', code)
	}
	message.setHeader('XCatalogVersionCode', code)
	
	return message
}