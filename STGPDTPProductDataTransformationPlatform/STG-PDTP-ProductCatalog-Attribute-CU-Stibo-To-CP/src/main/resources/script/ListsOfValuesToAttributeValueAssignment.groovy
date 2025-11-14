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

    int attributeValuesIterator = 1

    // Define target payload
    Writable writable = new StreamingMarkupBuilder().bind { builder ->
        builder.batchParts {
            builder.batchChangeSet1 {
                root.ListsOfValues.ListOfValue.each { listOfValue ->
                    Map values = getValues(listOfValue, attributeValuesIterator)
                    //if (values && !values.isEmpty()) {
                        builder.batchChangeSetPart1 {
                            builder.method('POST')
                            builder.uri('AttributeValueAssignment')
                            builder.body {
                                builder.AttributeValueAssignment {
                                    builder.AttributeValueAssignment {
                                        builder.code(listOfValue.@ID)
                                        builder.catalogVersion_ID(catalogVersions.collect { it.value }.join(','))
                                        buildAttributeValues(listOfValue, values, builder, converter)
                                    }
                                }
                            }
                        }
                    //}
                }
            }
        }
    }

    def outputStream = new ByteArrayOutputStream()
    writable.writeTo(new BufferedWriter(new OutputStreamWriter(outputStream, 'UTF-8')))
    message.setBody(outputStream)
    return message
}

void buildAttributeValues(Object listOfValue, Map values, Object builder, LanguageConverter converter) {
        builder.attributeValues {
            values.each { k, v ->
                builder.AttributeValue {
                    builder.code(k)
                    builder.attributeCode(listOfValue.@ID)
                    List<Map> convertedValues = converter.convert(v)
                    builder.texts {
                        convertedValues.each { convertedValue ->
                            builder.AttributeValueTexts {
                                builder.locale(convertedValue.lang)
                                builder.value(convertedValue.node.text())
                            }
                        }
                    }
                }
            }
        }
}

Map getValues(Object listOfValue, int attributeValuesIterator) {
    Map values = listOfValue.ValueGroup?.collectEntries { [(it.@ID ?: getAttributeValueID(attributeValuesIterator)): it.Value.collect().toArray()] }
    if (!values) {
        values = listOfValue.Value?.collectEntries { [(it.@ID ?: getAttributeValueID(attributeValuesIterator)): it] }
    }
    return values
}

String getAttributeValueID(int attributeValueIterator) {
    String result = "X-${attributeValueIterator}"
    attributeValueIterator++
    return result
}
