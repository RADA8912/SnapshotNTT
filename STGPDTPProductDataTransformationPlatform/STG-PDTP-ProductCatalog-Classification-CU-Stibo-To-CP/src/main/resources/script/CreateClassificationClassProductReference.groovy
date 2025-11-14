/*
 * Copyright (c) 2022 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.StreamingMarkupBuilder

Message processData(Message message) {
    Map classToProductReferences = message.getProperty('ClassToProductReferences')

    int classificationClassProductReferenceCount = 0

    Writable writable = new StreamingMarkupBuilder().bind { builder ->
        batchParts {
            batchChangeSet1 {
                classToProductReferences.each { classificationClass ->
                    classificationClass.value.each { productRef ->
                        classificationClassProductReferenceCount += productRef.catalogVersion_ID.split(',').size()
                        batchChangeSetPart1 {
                            method('POST')
                            uri('ClassificationClassProductReference')
                            body {
                                ClassificationClassProductReference {
                                    ClassificationClassProductReference {
                                        catalogVersion_ID(productRef.catalogVersion_ID)
                                        targetCode(productRef.targetCode)
                                        sortOrder(productRef.sortOrder)
                                        referenceTypeCode(productRef.referenceTypeCode)
                                        source {
                                            ClassificationClass {
                                                code(classificationClass.key)
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

    message.setProperty('BatchEntity', 'ClassificationClassProductReference')
    message.setProperty('ClassificationClassProductRefExpectedCount', classificationClassProductReferenceCount)
    def messageLog = messageLogFactory?.getMessageLog(message)
    if (messageLog) {
        messageLog.addCustomHeaderProperty('ClassificationClassProductRefExpectedCount', classificationClassProductReferenceCount as String)
    }

    message.setBody(outputStream)
    return message
}