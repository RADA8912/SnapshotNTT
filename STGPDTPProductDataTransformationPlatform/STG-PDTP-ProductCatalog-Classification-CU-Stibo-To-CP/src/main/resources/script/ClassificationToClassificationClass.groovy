/*
* Copyright (c) 2020 Miele & Cie. KG - All rights reserved.
*/

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.xml.StreamingMarkupBuilder
import org.apache.commons.lang3.time.DateUtils
import src.main.resources.script.LanguageConverter
import src.main.resources.script.MappingLogger
import src.main.resources.script.MetaDataBuilder
import src.main.resources.script.PublicationHandler
import java.text.SimpleDateFormat

Message processData(Message message) {
    byte[] content = message.getBody(byte[])
    def rootElement = new XmlParser().parse(new InputStreamReader(new ByteArrayInputStream(content), 'UTF-8'))
    def classifications = rootElement.'**'.findAll { it.name() == 'Classification' }
    def products = rootElement.'**'.findAll { it.name() == 'Product' }

    def catalogLanguages = message.getHeader('XCatalogLanguages', String)
    def languageMapping = message.getHeader('XLanguageMapping', String)
    def contentObjectPrefix = message.getHeader('XContentObjectPrefix', String)
    def publicationMapping = new JsonSlurper().parseText(message.getHeader('XCatalogVersions', String))
    Map classSystemTypeRootIdMapping = new JsonSlurper().parseText(message.getHeader('XClassSystemTypeRootIdMapping', String))
    Map catalogToClassSystemTypeMapping = new JsonSlurper().parseText(message.getHeader('XCatalogToClassSystemTypeMapping', String))
    def publicationSplit = message.getHeader('XPublicationSplit', Boolean)

    Map lifecycleMapping = new JsonSlurper().parseText(message.getHeader('XLifecycleMapping', String))
    Boolean lifecycleValidFromDisabled = message.getProperty('lifecycleValidFromDisabled') ? message.getProperty('lifecycleValidFromDisabled').toBoolean() : false
    Boolean lifecycleValidToDisabled = message.getProperty('lifecycleValidToDisabled') ? message.getProperty('lifecycleValidToDisabled').toBoolean() : false

    MappingLogger logger = new MappingLogger(message)

    LanguageConverter languageConverter = LanguageConverter.newConverter(catalogLanguages, languageMapping)
    PublicationHandler publicationHandler = new PublicationHandler(publicationMapping, publicationSplit, languageConverter)
    publicationHandler.constructClassSystemTypeToCatalogVersions(classSystemTypeRootIdMapping, catalogToClassSystemTypeMapping)

    // Process through the classification recursively to identify the system type
    // Start from /ClassificationRoot/Classifications/Classification
    Map systemTypeReferences = [:]
    rootElement.Classifications.Classification.each { Node classNode ->
        constructTypeReference(classNode, systemTypeReferences, classSystemTypeRootIdMapping, '')
    }
    ClassificationClassBatchRequest batchRequest
    Writable writable = new StreamingMarkupBuilder().bind { builder ->
        builder.batchParts {
            builder.batchChangeSet1 {
                batchRequest = new ClassificationClassBatchRequest(
                        languageConverter,
                        builder,
                        contentObjectPrefix,
                        classifications,
                        products,
                        rootElement.LegendClassification,
                        publicationHandler,
                        lifecycleMapping,
                        lifecycleValidFromDisabled,
                        lifecycleValidToDisabled,
                        systemTypeReferences,
                        classSystemTypeRootIdMapping,
                        logger
                )
                batchRequest.build()
                message.setProperty('ClassToProductReferences', batchRequest.classToProductReferences)
                message.setProperty('ClassificationClassExpectedCount', batchRequest.classificationClassCount)
            }
        }
    } as Writable

    message.setProperty('BatchEntity', 'ClassificationClass')
    def outputStream = new ByteArrayOutputStream()
    writable.writeTo(new BufferedWriter(new OutputStreamWriter(outputStream, 'UTF-8')))
    message.setBody(outputStream)

    def messageLog = messageLogFactory?.getMessageLog(message)
    if (messageLog) {
        messageLog.addCustomHeaderProperty('ClassificationClassExpectedCount', batchRequest.classificationClassCount as String)
        List logEntries = logger.getEntries()
        if (logEntries.size()) {
            StringBuilder sb = new StringBuilder()
            logEntries.each { sb << "${it}\r\n" }
            messageLog.addAttachmentAsString('MappingLogs', sb.toString(), 'text/plain')
        }
    }
    return message
}

void constructTypeReference(Node classNode, Map systemTypeReferences, Map classSystemTypeRootIdMapping, String parentSystemType) {
    String currentClassSystemType = (parentSystemType) ?: classSystemTypeRootIdMapping.get(classNode?.@ID)
    systemTypeReferences.put(classNode?.@ID, currentClassSystemType)
    classNode?.Classification?.each { Node childClass ->
        constructTypeReference(childClass, systemTypeReferences, classSystemTypeRootIdMapping, currentClassSystemType)
    }
}

class ClassificationClassBatchRequest {
    final LanguageConverter languageConverter
    final Object builder
    final List classifications
    final List products
    final Map<String, List> backReferences
    final String contentObjectPrefix
    final MetaDataBuilder metaDataBuilder
    final PublicationHandler publicationHandler
    final lifecycleMapping
    final lifecycleValidFromDisabled
    final lifecycleValidToDisabled
    final Map systemTypeReferences
    final Map classSystemTypeRootIdMapping
    final Map classToProductReferences
    final MappingLogger logger
    int classificationClassCount

    ClassificationClassBatchRequest(LanguageConverter languageConverter, builder, contentObjectPrefix, classifications, products, legendClassifications, PublicationHandler publicationHandler, lifecycleMapping, lifecycleValidFromDisabled, lifecycleValidToDisabled, Map systemTypeReferences, Map classSystemTypeRootIdMapping, MappingLogger logger) {
        this.languageConverter = languageConverter
        this.builder = builder
        this.classifications = classifications
        this.products = products
        this.backReferences = constructClassToProductBackReferences(legendClassifications)
        this.contentObjectPrefix = contentObjectPrefix
        this.metaDataBuilder = new MetaDataBuilder()
        this.publicationHandler = publicationHandler
        this.lifecycleMapping = lifecycleMapping
        this.lifecycleValidFromDisabled = lifecycleValidFromDisabled
        this.lifecycleValidToDisabled = lifecycleValidToDisabled
        this.systemTypeReferences = systemTypeReferences
        this.classSystemTypeRootIdMapping = classSystemTypeRootIdMapping
        this.classToProductReferences = [:]
        this.logger = logger
        this.classificationClassCount = 0
    }

    Map<String, List> constructClassToProductBackReferences(NodeList legendClassifications) {
        // Builds a class to product references (for backward references) for fast lookup later
        Map output = [:]
        legendClassifications.ClassProductReference.each { productRef ->
            List refsInClass = output.get(productRef.@ID)
            if (refsInClass) {
                refsInClass.add(productRef)
            } else {
                output.put(productRef.@ID, [productRef])
            }
        }
        return output
    }

    def build() {
        classifications.each { classification ->
            if (this.systemTypeReferences.get(classification.@ID) // skip those that do not have system type, indicating they are above the root IDs defined in CVM
                    && checkValidForLifecycle(classification, lifecycleMapping, languageConverter, lifecycleValidFromDisabled, lifecycleValidToDisabled)) {
                createBatchChangeSetPart(classification)
            }
        }
    }

    def createBatchChangeSetPart(element) {
        // Get the catalog versions relevant for the current classificationSystemType
        String currentClassSystemType = this.systemTypeReferences.get(element.@ID)
        String relevantCatalogVersions = this.publicationHandler.getRelevantCatalogVersions(currentClassSystemType)
        if (relevantCatalogVersions) {
            saveProductReference(element, relevantCatalogVersions)
            // Count the number of records
            this.classificationClassCount += relevantCatalogVersions.split(',').size()
            builder.batchChangeSetPart1 {
                method('POST')
                uri('ClassificationClass')
                builder.body {
                    builder.ClassificationClass {
                        builder.ClassificationClass {
                            builder.catalogVersion_ID(relevantCatalogVersions)
                            code(element.@ID)
                            addContentObjectReferences(element)
                            addAttributeAssignments(element)
                            addClassificationClassMetadata(element)
                            addClassificationClassMediaReferences(element)
                            externalObjectTypeCode(element.@UserTypeID)
                            displayBooleanAttributeAsBulletPoint(findAttributeByAttributeIDAndID(element, 'atr_display_yes_as_bullet', '01') ? true : false)
                            classificationSystemType(currentClassSystemType)
                            parentCode((this.classSystemTypeRootIdMapping.get(element.@ID)) ? '' : element.parent()?.@ID)
                            publishedFromDate(getDateFromValue(element, 'atr_publication_valid_from_country', languageConverter)?.format('yyyy-MM-dd') ?: '1900-01-01')
                            publishedUntilDate(getDateFromValue(element, 'atr_publication_expiry_date_country', languageConverter)?.format('yyyy-MM-dd') ?: '9999-12-31')
                            buildSortOrder(element)
                            addTexts(element)
                            addClassificationClassReference(element)
                            builder.classificationClassTypeCode(getClassificationClassTypeCode(element))
                        }
                    }
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
        builder.sortOrder(sortOrderNumber.text())
    }

    def addTexts(element) {
        List<Map> convertedNames = languageConverter.convert(element.Name.collect().toArray())
        builder.texts {
            convertedNames.each { convertedName ->
                ClassificationClassTexts {
                    String language = convertedName.get('lang')
                    name(convertedName.get('node'))
                    locale(language)
                    originQualifier(convertedName.get('qualifiers'))
                }
            }
        }
    }

    def addAttributeAssignments(element) {
        builder.classificationClassAttributeAssignments {
            element.'*'.findAll { it.name() == 'AttributeLink' }.each { attributeLink ->
                ClassificationClassAttributeAssignment {
                    attributeCode(attributeLink.@AttributeID)
                    classificationClassAttributeAssignmentMetaData {
                        if (attributeLink.children()) {
                            metaDataBuilder.build(builder, "ClassificationClassAttributeAssignment", attributeLink.MetaData, languageConverter)
                        }
                    }
                }
            }
        }
    }

    def addClassificationClassMetadata(element) {
        builder.classificationClassMetaData {
            metaDataBuilder.build(builder, "ClassificationClass", element.MetaData, languageConverter)
        }
    }

    def addClassificationClassMediaReferences(element) {
        Set<String> uniqueAssets = []
        builder.classificationClassMediaReferences {
            element.AssetCrossReference.each { asset ->
                List languages = (asset.@QualifierID) ? this.languageConverter.languageMapping[asset.@QualifierID]?.languages : ['']
                languages.each { assetLanguageCode ->
                    String key = "${asset.@AssetID}_${asset.@Type}_${assetLanguageCode}"
                    if (!uniqueAssets.contains(key)) {
                        uniqueAssets.add(key)
                        builder.ClassificationClassMediaReference {
                            builder.targetCode(asset.@AssetID)
                            builder.referenceTypeCode(asset.@Type)
                            builder.languageCode(assetLanguageCode)
                        }
                    }
                }
            }
        }
    }

    def addContentObjectReferences(element) {
        // Remove duplicates based on key combination of ProductID and Type
        Map uniqueCORefs = [:]
        element.'*'.findAll { it.name() == 'ProductReference' && isContentObject(it.@ProductID.toString()) }.each { productRef ->
            // We want to keep the last found unique reference, so if it occurs again, we remove the previous entry
            String objectKey = "${productRef.@ProductID}_${productRef.@Type}"
            if (uniqueCORefs.containsKey(objectKey)) {
                uniqueCORefs.remove(objectKey)
            }
            uniqueCORefs.put(objectKey, productRef)
        }
        builder.classificationClassContentObjectReferences {
            uniqueCORefs.each { objectKey, productRef ->
                ClassificationClassContentObjectReference {
                    targetCode(productRef.@ProductID)
                    referenceTypeCode(productRef.@Type)
                    buildSortOrder(productRef)
                    classificationClassContentObjectReferenceMetaData {
                        metaDataBuilder.build(builder, "ClassificationClassContentObjectReferenceMetaData", productRef.MetaData, languageConverter)
                    }
                }
            }
            // Extract inline references from MetaData with atr_filter_answer
            Map<String, Node> filterAnswers = getAttributeEntries(element, 'atr_filter_answer', this.languageConverter)
            if (filterAnswers) {
                List answers = filterAnswers.collect { it.value }
                // Assumption is that all languages will have the same inline references, so we just take the first entry to create the reference
                getInlineRefs(answers[0].text()).eachWithIndex { refCode, index ->
                    builder.ClassificationClassContentObjectReference {
                        builder.targetCode(refCode)
                        builder.referenceTypeCode('inline_reference_filter_answer')
                        builder.sortOrder(index)
                    }
                }
            }
        }
    }

    Set getInlineRefs(String inputText) {
        def inlineReferences = []
        String regexPattern = /#([^#]+)#<inRef\.objid>([^<]+)<\/inRef\.objid>/
        def matcher = (inputText =~ regexPattern)
        matcher.size().times {
            inlineReferences << matcher[it][2]
        }
        return inlineReferences.toSet()
    }

    Map<String, Node> getAttributeEntries(Node classification, String attributeName, LanguageConverter converter) {
        def entries = classification.MetaData.ValueGroup.find { it.@AttributeID == attributeName }?.Value?.collect()?.toArray()
        if (!entries?.size()) {
            def value = classification.MetaData.Value.find { it.@AttributeID == attributeName }
            if (value) {
                entries = [value].toArray()
            } else {
                entries = [].toArray()
            }
        }
        List<Map> output = converter.convert(entries)
        return output.collectEntries { [(it.lang): it.node] }
    }

    def saveProductReference(Node element, String catalogVersionIDsForClass) {
        // Forward references (from class to product)
        List productReferences = element.'*'.findAll { it.name() == 'ProductReference' && isProduct(it) }
        // Back references (from product to class)
        List backReferences = this.backReferences.get(element.@ID)
        if (backReferences) {
            productReferences.addAll(backReferences)
        }
        Set<String> uniqueProducts = []
        List references = []
        if (productReferences.size()) {
            logger.debug("Class ${element.@ID} with catalog version IDs ${catalogVersionIDsForClass} has product references")
            this.classToProductReferences.put(element.@ID, references)
        }
        // Go through all the class to product references and store the unique references in a Map
        productReferences.each { productRef ->
            String key = "${productRef.@ProductID}_${productRef.@Type}"
            def product = products.find { product -> product.@ID == productRef.@ProductID }
            def catalogVersionIDsForProduct = publicationHandler.getProductPublicationCatalogVersion(product)
            if (catalogVersionIDsForProduct) {
                // Filter IDs in catalogVersionIDsForProduct to only those found in catalogVersionIDsForClass
                catalogVersionIDsForProduct = filterRelevantCatalogVersionIDs(catalogVersionIDsForProduct, catalogVersionIDsForClass)
                if (!uniqueProducts.contains(key) && catalogVersionIDsForProduct) {
                    logger.debug("Storing reference ${key} for ${catalogVersionIDsForProduct}")
                    uniqueProducts.add(key)
                    // Get sort order
                    def sortOrderNumber = productRef.MetaData.Value.find { it.@AttributeID == 'atr_sort_order' }
                    def sortOrderValue = (sortOrderNumber) ? sortOrderNumber.text() : 0
                    // Create an entry for the product reference
                    Map productRefEntry = ['catalogVersion_ID': catalogVersionIDsForProduct,
                                           'targetCode'       : productRef.@ProductID,
                                           'sortOrder'        : sortOrderValue,
                                           'referenceTypeCode': productRef.@Type]
                    // Then add it to a list that is linked to the classification class
                    references.add(productRefEntry)
                } else {
                    logger.debug("Skipped reference ${key} for ${catalogVersionIDsForProduct}")
                }
            } else {
                logger.debug("Skipped reference ${key} with empty catalog version IDs")
            }
        }
    }

    String filterRelevantCatalogVersionIDs(String inputIDs, String referenceIDs) {
        List outputIDs = []
        inputIDs.split(',').collect {
            if (referenceIDs.contains(it)) {
                outputIDs.add(it)
            }
        }
        return outputIDs.join(',')
    }

    def addClassificationClassReference(element) {
        builder.classificationClassReferences {
            def classificationCrossReferences = element.'*'.findAll { it.name() == 'ClassificationCrossReference' } as List<?>
            classificationCrossReferences.each { crossReference ->
                ClassificationClassReference {
                    targetCode(crossReference.@ClassificationID)
                    referenceTypeCode(crossReference.@Type)
                }
            }
        }
    }

    String getClassificationClassTypeCode(element) {
        def value = element.MetaData.Value.find { it -> it.name() == 'Value' && it.@AttributeID == 'atr_legend_type' }
        if (!value) {
            value = element.MetaData.ValueGroup.find { it.@AttributeID == 'atr_legend_type' }?.Value?.get(0)
        }
        switch (value?.@ID) {
            case 'eco_information':
                return 'ECO_INFORMATION';
            case 'includedAssessories':
                return 'INCLUDED_ACCESSORIES';
            default:
                return '';
        }
        return '';
    }

    boolean isContentObject(id) {
        final prefixes = contentObjectPrefix.split(",").collect()
        String inputPrefix = id.replaceFirst(/(\w+)_0*(\d+)_\d+/, '$1')
        return (inputPrefix in prefixes)
    }

    boolean isProduct(reference) {
        return (reference.@ProductID.isNumber() && reference.@ProductID.length() == 18)
    }

    boolean checkValidForLifecycle(Node classification, Map lifecycleMapping, LanguageConverter converter, boolean validFromDisabled, boolean validToDisabled) {
        String mappingValue = lifecycleMapping[classification.@UserTypeID]
        def dateNow = new Date()
        boolean validToIsOk
        if (mappingValue) {
            //Lifecycle publication structure
            def publicationStructure = this.findAttributeByAttributeIDAndID(classification, 'atr_lifecycle_publication_structure', mappingValue)

            //Lifecycle legend
            def lifecycleLegend = this.findAttributeByAttributeIDAndID(classification, 'atr_lifecycle_legend', mappingValue)

            //Lifecycle filter
            def lifecycleFilter = this.findAttributeByAttributeIDAndID(classification, 'atr_lifecycle_filter', mappingValue)

            if (!publicationStructure && !lifecycleLegend && !lifecycleFilter) {
                return false
            }

            //Valid to
            if (!validToDisabled) {
                def validTo = getDateFromValue(classification, 'atr_publication_expiry_date_country', converter)
                if (validTo) {
                    if (dateNow.before(validTo) || DateUtils.isSameDay(validTo, dateNow)) {
                        validToIsOk = true
                    }
                } else {
                    validToIsOk = true
                }
            } else {
                validToIsOk = true
            }

            if (validToIsOk) {
                return true
            }

        } else {
            return true;
        }
    }

    Date getDateFromValue(Node classification, String attributeID, LanguageConverter converter) {
        def validDates = classification.MetaData.Value.findAll { it.@AttributeID == attributeID }
        Map<Integer, Node> priorityMap = [:]
        if (!validDates) {
            validDates = classification.MetaData.MultiValue.find { it.@AttributeID == attributeID }?.Value.findAll { it.name() == 'Value' }
        }
        if (!validDates) {
            return
        } else {
            validDates.each { validDate ->
                priorityMap.put(converter.languageMapping[validDate.@QualifierID].priority, validDate)
            }
            def sortedKeys = priorityMap.sort()*.key
            def nodeValue = priorityMap[sortedKeys[0]].text()
            def date = new SimpleDateFormat('yyyy-MM-dd').parse(nodeValue)
            return date
        }
    }

    Node findAttributeByAttributeIDAndID(Node classification, String attributeIDValue, String iDValue) {
        def attribute = classification.MetaData.Value.find { it.@AttributeID == attributeIDValue && it.@ID == iDValue }
        if (!attribute) {
            attribute = classification.MetaData.MultiValue.find { it.@AttributeID == attributeIDValue }?.Value.find { it.@ID == iDValue }
        }
        if (!attribute) {
            attribute = classification.MetaData.ValueGroup.find { it.@AttributeID == attributeIDValue }?.Value.find { it.@ID == iDValue }
        }
        return attribute
    }
}