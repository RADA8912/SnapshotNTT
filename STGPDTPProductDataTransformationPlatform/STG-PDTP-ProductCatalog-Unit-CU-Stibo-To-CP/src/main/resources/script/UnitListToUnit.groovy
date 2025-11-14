/*
 * Copyright (c) 2020 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.xml.StreamingMarkupBuilder
import src.main.resources.script.LanguageConverter

Message processData(Message message) {
	byte[] content = message.getBody(byte[])
	def root = new XmlParser().parse(new InputStreamReader(new ByteArrayInputStream(content), 'UTF-8'))

    Map catalogVersions = new JsonSlurper().parseText(message.getHeader('XCatalogVersions', String))
    def catalogLanguages = message.getHeader('XCatalogLanguages', String)
    def languageMappingEntries = message.getHeader('XLanguageMapping', String)
    LanguageConverter converter = LanguageConverter.newConverter(catalogLanguages, languageMappingEntries)

    Writable writable = new StreamingMarkupBuilder().bind { builder ->
        batchParts {
            batchChangeSet1 {
                root.Unit.each { unit ->
                    batchChangeSetPart1 {
                        method('POST')
                        uri('AttributeUnit')
                        body {
                            AttributeUnit {
                                AttributeUnit {
                                    code(unit.@ID)
                                    catalogVersion_ID(catalogVersions.collect { it.value }.join(','))
                                    texts {
                                        List<Map> convertedNames = converter.convert(unit.Name.collect().toArray())
                                        convertedNames.each { entry ->
                                            AttributeUnitTexts {
                                                locale(entry.lang)
                                                name(entry.node)
                                                originQualifier(entry.qualifiers)
                                            }
                                        }
                                    }
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
    return message
}