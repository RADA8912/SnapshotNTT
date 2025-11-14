import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import src.main.resources.script.ContentObjectBuilder
import src.main.resources.script.MappingLogger

/*
* Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
*/

Message processData(Message message) {
    List contentObjects = message.getBody().value
    MappingLogger logger = new MappingLogger(message)

    def outputStream = new ByteArrayOutputStream()
    def indentPrinter = new IndentPrinter(new BufferedWriter(new OutputStreamWriter(outputStream, 'UTF-8')), '', false)
    MarkupBuilder builder = new MarkupBuilder(indentPrinter)
    Map infoDetailTypes = ['product_benefit': 'PRODBEN', 'special_topic': 'SPECIAL', 'sector_theme': 'BRANCHTHEME', 'reference': 'REFTHEME']
    ContentObjectBuilder coBuilder = new ContentObjectBuilder(builder, contentObjects*.contentObjectReferences, message, logger)

    builder.root {
        contentObjects.findAll { it.contentObjectTypeCode in ['product_benefit', 'special_topic', 'sector_theme', 'reference'] }
                .sort { it.formattedCode }.each { contentObject ->
            logger.info("--- Begin processing content object ${contentObject.code} ---")
            if (hasDetails(contentObject, contentObjects)) {
                builder.infoDetail(type: infoDetailTypes.get(contentObject.contentObjectTypeCode)) {
                    switch (contentObject.contentObjectTypeCode) {
                        case 'product_benefit':
                            coBuilder.buildSingleBenefitNode([target: contentObject], 'InfoDetail-Benefit')
                            break
                        case 'special_topic':
                            List modules = contentObjects.findAll { it.contentObjectTypeCode == 'info_module' && it.parentCode == contentObject.code }
                            coBuilder.buildSingleNodeWithModules('io_SPECIAL', [target: contentObject], 'InfoDetail-Special', modules, 'InfoDetail-Longtext')
                            break
                        case 'sector_theme':
                            List modules = contentObjects.findAll { it.contentObjectTypeCode == 'info_module' && it.parentCode == contentObject.code }
                            coBuilder.buildSingleNodeWithModules('io_BRANCHTHEME', [target: contentObject], 'InfoDetail-Branchtheme', modules, 'InfoDetail-Longtext')
                            break
                        case 'reference':
                            List modules = contentObjects.findAll { it.contentObjectTypeCode == 'info_module' && it.parentCode == contentObject.code }
                            coBuilder.buildSingleNodeWithModules('io_REFTHEME', [target: contentObject], 'InfoDetail-Reftheme', modules, 'InfoDetail-Longtext')
                            break
                    }
                    coBuilder.buildReference()
                    coBuilder.resetInlineReferences()
                }
            } else {
                logger.info("--- Content object ${contentObject.code} skipped as it has no relevant details ---")
            }
        }
    }

    message.setBody(outputStream)
    if (logger.getEntries().size()) {
        message.setProperty('LogEntries', logger.getEntries())
    }
    return message
}

private boolean hasDetails(Map contentObject, List contentObjects) {
    if (contentObject.contentObjectMediaReferences.any { it.referenceTypeCode == 'ref_content_object_video' }) {
        return true
    } else if (contentObjects.any { it.contentObjectTypeCode == 'info_module' && it.parentCode == contentObject.code }) {
        return true
    } else if (contentObject.contentObjectTypeCode == 'product_benefit' && contentObject.longText) {
        return true
    } else {
        return false
    }
}