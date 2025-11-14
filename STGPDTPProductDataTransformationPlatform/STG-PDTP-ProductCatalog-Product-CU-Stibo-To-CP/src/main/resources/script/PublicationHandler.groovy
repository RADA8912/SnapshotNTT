package src.main.resources.script

import groovy.json.JsonOutput
import org.apache.commons.lang3.time.DateUtils

import java.text.SimpleDateFormat

class PublicationHandler {

    final Map publicationMapping
    final boolean publicationSplit
    final LanguageConverter languageConverter
    Map classSystemTypeToCatalogVersions = [:]
    Map orderablePerPublication = [:]
    Map<String,Map<String,String>> datesPerPublication = [:]

    PublicationHandler(publicationMapping, publicationSplit, LanguageConverter languageConverter) {
        this.publicationSplit = publicationSplit
        this.publicationMapping = publicationMapping
        this.languageConverter = languageConverter
    }

    String getProductPublicationCatalogVersion(product) {
        this.orderablePerPublication = [:] // Reset for each product
        this.datesPerPublication = [:] // Reset for each product
        def publicationCatalogs = []
        def publicationCatalog

        //if master exists always publish it
        if(publicationMapping.get('objEnt_pub_master')) publicationCatalogs << publicationMapping.get('objEnt_pub_master')

        if (!this.publicationSplit) {
            publicationCatalogs << this.publicationMapping.values().toArray()[0]
            return publicationCatalogs.join(',')
        }

        List fulfilledPublications = product?.EntityCrossReference.findAll { entityRef ->
            // Check both single Value or in ValueGroup
            def entries = entityRef.MetaData.ValueGroup.find { it.@AttributeID == 'atr_publication_conditions_fulfilled' }?.Value?.collect()?.toArray()
            if (!entries?.size()) {
                def value = entityRef.MetaData.Value.find { it.@AttributeID == 'atr_publication_conditions_fulfilled' }
                if (value) {
                    entries = [value].toArray()
                } else {
                    entries = [].toArray()
                }
            }
            List<Map> output = this.languageConverter.convert(entries)
            if (output.size() && output[0].node.@ID == '1') {
                return true
            }
        }

        //has duplicates?
        if (fulfilledPublications.collect({ it.@EntityID }).countBy { it }.findResult {
            if (it.value > 1) {
                return true
            }
        }) {
            return null
        }

        fulfilledPublications.each { Node reference ->
            def currentPublicationCatalog = this.publicationMapping.get(reference.@EntityID)
            if (currentPublicationCatalog) {
                // Store the orderable flag and dates per publication
                Map publicationDates = getDates(reference)

                if (publicationDates) {
                    if(!this.isProductExpired(publicationDates)) {
                        publicationCatalogs << currentPublicationCatalog
                        this.datesPerPublication.put(currentPublicationCatalog, publicationDates)
                        this.orderablePerPublication.put(currentPublicationCatalog, getOrderable(reference))
                    }
                }
            }
        }

        publicationCatalog = publicationCatalogs.join(',')

        switch (product?.@UserTypeID) {
            case 'obj_product':
                if (product.'**'.find { node -> node.@UserTypeID == 'obj_article' }) {
                    return this.publicationMapping.values().toArray().join(',')
                }
                break;
            default:
                return publicationCatalog;
        }

    }

    String getOrderableListString() {
        List output = this.orderablePerPublication.collect { ['key': it.key, 'value': it.value] }
        return JsonOutput.toJson(output)
    }

    String getPublishedDatesString(String userTypeId, String catalogVersion) {
        List output = []
        List catalogVersionSplit = catalogVersion.split(',')
        if (userTypeId == 'obj_article') {
            output = this.datesPerPublication.collect {
                ['key': it.key, 'publishedFromDate': it.value.publishedFromDate, 'publishedUntilDate': it.value.publishedUntilDate]
            }
            //if master exists set constant dates
            if (catalogVersionSplit.contains(publicationMapping.get('objEnt_pub_master'))) {
                output << ['key': publicationMapping.get('objEnt_pub_master'), 'publishedFromDate': '1900-01-01', 'publishedUntilDate': '9999-12-31']
            }
        } else {
            output = catalogVersionSplit.collect {
                ['key': it, 'publishedFromDate': '1900-01-01', 'publishedUntilDate': '9999-12-31']
            }
        }
        return JsonOutput.toJson(output)
    }

    boolean getOrderable(Node entityReference) {
        Node orderableNode = entityReference.MetaData.Value.find { it.@AttributeID == 'atr_ZZ2CAT_ORDER' } ?: entityReference.MetaData.ValueGroup.find { it.@AttributeID == 'atr_ZZ2CAT_ORDER' }?.Value
        return orderableNode?.text() == 'X'
    }

    Map getDates(Node entityReference) {
        Node dateFromNode = entityReference.MetaData.Value.find { it.@AttributeID == 'atr_publication_valid_from' } ?: entityReference.MetaData.ValueGroup.find { it.@AttributeID == 'atr_publication_valid_from' }?.Value
        Node dateUntilNode = entityReference.MetaData.Value.find { it.@AttributeID == 'atr_publication_expiry_date' } ?: entityReference.MetaData.ValueGroup.find { it.@AttributeID == 'atr_publication_expiry_date' }?.Value
        return ['publishedFromDate': dateFromNode?.text() ?: '1900-01-01', 'publishedUntilDate': dateUntilNode?.text() ?: '9999-12-31']
    }

    private Boolean isProductExpired(Map dates) {
        String publishedUntilDateString = dates.get('publishedUntilDate')
        Date publishedUntilDate = new SimpleDateFormat('yyyy-MM-dd').parse(publishedUntilDateString)
        Date now = new Date()
        if(now.before(publishedUntilDate) || DateUtils.isSameDay(publishedUntilDate,now)) {
            return false;
        }
        return true;
    }

    void constructClassSystemTypeToCatalogVersions(Map classSystemTypeRootIdMapping, Map catalogToClassSystemTypeMapping) {
        // Map the catalog suffix to its version ID
        Map catalogToVersionIDs = this.publicationMapping.collectEntries {
            String catalogSuffix = it.key.replace('objEnt_pub_', '')
            [(catalogSuffix): it.value]
        }
        // If the catalog from STEP is not maintained in CVM, then import it for the classification version
        List defaultImportedVersions = []
        catalogToVersionIDs.each {
            if (!catalogToClassSystemTypeMapping.containsKey(it.key)) {
                defaultImportedVersions.add(it.value)
            }
        }

        // Go through each catalog system type, and find which catalog versions that it should have
        classSystemTypeRootIdMapping.each {
            List relevantCatalogVersions = defaultImportedVersions.collect()
            String systemType = it.value
            catalogToClassSystemTypeMapping.each { String catalogSuffix, String systemTypes ->
                if (catalogToVersionIDs.containsKey(catalogSuffix)) {
                    if (systemTypes.contains(systemType)
                            || systemTypes == 'ALL'
                            || systemTypes == '') {
                        relevantCatalogVersions.add(catalogToVersionIDs.get(catalogSuffix))
                    }
                }
            }
            this.classSystemTypeToCatalogVersions.put(systemType, relevantCatalogVersions.join(','))
        }
    }

    String getRelevantCatalogVersions(String classificationSystemType) {
        return this.classSystemTypeToCatalogVersions.get(classificationSystemType)
    }
}