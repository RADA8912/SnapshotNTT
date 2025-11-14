/*
 * Copyright (c) 2020 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.dom.DOMCategory
import src.main.resources.script.LanguageConverter

Message processData(Message message) {
    byte[] content = message.getBody(byte[])
    def root = new XmlParser().parse(new InputStreamReader(new ByteArrayInputStream(content), 'UTF-8'))

    Map catalogVersions = new JsonSlurper().parseText(message.getHeader('XCatalogVersions', String))
    def catalogLanguages = message.getHeader('XCatalogLanguages', String)
    def languageMappingEntries = message.getHeader('XLanguageMapping', String)
    LanguageConverter converter = LanguageConverter.newConverter(catalogLanguages, languageMappingEntries)

    def attrMappingEntries = message.getHeader('XAttributeTypeMapping', String)
    Map attrTypes = new JsonSlurper().parseText(attrMappingEntries)

    // Define target payload
    Writable writable = new StreamingMarkupBuilder().bind { builder ->
        builder.batchParts {
            builder.batchChangeSet1 {
                root.AttributeList.Attribute.each { attr ->
                    builder.batchChangeSetPart1 {
                        builder.method('POST')
                        builder.uri('Attribute')
                        builder.body {
                            builder.Attribute {
                                builder.Attribute {
                                    builder.code(attr.@ID)
                                    builder.catalogVersion_ID(catalogVersions.collect { it.value }.join(','))
                                    builder.attributeType(determineAttributeType(attr, attrTypes))
                                    builder.multiValued(attr.@MultiValued)
                                    builder.unitCode(attr.@DefaultUnitID)
                                    builder.texts {
                                        List<Map> convertedNames = converter.convert(attr.Name.collect().toArray())
                                        convertedNames.each { entry ->
                                            builder.AttributeTexts {
                                                builder.locale(entry.lang)
                                                builder.name(entry.node)
                                                builder.originQualifier(entry.qualifiers)
                                            }
                                        }
                                    }
                                    builder.attributeValueAssignmentCode(getListOfValueID(attr))
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

String determineAttributeType(attribute, Map attrTypes) {
    if (attribute.'*'.find { it.name() == 'ListOfValueLink' }) {
        return 'ENUM'
    } else if (attribute.'*'.find { it.name() == 'Validation' }) {
        def value = attrTypes?.get(attribute.Validation.@BaseType[0])
        return value ?: 'STRING'
    } else {
        return 'STRING'
    }
}

String getListOfValueID(attribute) {
    return attribute.'*'.find { it.name() == 'ListOfValueLink' }?.@ListOfValueID
}