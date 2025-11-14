/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

package src.main.resources.script

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

import java.text.NumberFormat

class MaterialBuilder {
    final Object builder
    final Map articleTypeMapping
    final Map articleTypeTranslation
    final Map shapeMapping
    final Map mediaDocTypeTranslation
    final MappingLogger logger
    final Map prices
    final Map shopIdMapping
    final Map shopLinkMapping
    final Map shopLinkIsInternMapping
    final Map availabilityMapping
    final Map displayCurrencyEntries
    final String targetGroup
    final String targetCountry
    final String sourceCurrentLanguage
    final List productsWithPromotions

    MaterialBuilder(Object builder, Message message, MappingLogger logger, Map prices) {
        this.builder = builder
        this.articleTypeMapping = new JsonSlurper().parseText(message.getProperty('XArticleRefTypeMapping'))
        this.articleTypeTranslation = new JsonSlurper().parseText(message.getProperty('XArticleRefTypeTranslation'))
        this.shapeMapping = new JsonSlurper().parseText(message.getProperty('XMediaShapeMapping'))
        this.mediaDocTypeTranslation = new JsonSlurper().parseText(message.getProperty('XMediaDocumentTypeTranslation'))
        this.shopIdMapping = new JsonSlurper().parseText(message.getProperty('XShopIdMapping'))
        this.shopLinkMapping = new JsonSlurper().parseText(message.getProperty('XShopLinkMapping'))
        this.shopLinkIsInternMapping = new JsonSlurper().parseText(message.getProperty('XShopLinkIsInternMapping'))
        this.availabilityMapping = new JsonSlurper().parseText(message.getProperty('AvailabilityPayload'))
        this.productsWithPromotions = message.getProperty('PromotionsPayload').value
        this.targetGroup = message.getHeader('XTargetGroup', String)
        this.targetCountry = message.getHeader('XTargetCountry', String)
        this.sourceCurrentLanguage = message.getHeader('XSourceCurrentLanguage', String)
        this.displayCurrencyEntries = constructDisplayCurrencyMapping(message, this.sourceCurrentLanguage)
        this.prices = prices
        this.logger = logger
    }
    
    static Map constructPricesReference(Map priceRoot, String sourceLanguage, String currencyCode) {
        if (priceRoot.value) {
            // Get currency formatter according to locale
            def lang = sourceLanguage.split('_')
            Locale locale = new Locale(lang[0], lang[1])
            Currency currentCurrency = Currency.getInstance(locale)
            NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale)
            numberFormatter.setMinimumFractionDigits(currentCurrency.getDefaultFractionDigits())

            return priceRoot.value.findAll { it.currencyCode == currencyCode }.collectEntries { [(it.productCode): [value: it.value.setScale(4, BigDecimal.ROUND_HALF_UP), formattedValue: numberFormatter.format(it.value), currencyCode: it.currencyCode, unitCode: it.unitCode]] }
        } else {
            return [:]
        }
    }

    void buildByType(Map product, String type) {
        buildByType(product, type, true)
    }

    Map constructDisplayCurrencyMapping(Message message, String sourceLanguage) {
        def displayCurrencyMappingString = message.getProperty('XDisplayCurrencyMapping')
        Map mappingEntries = (displayCurrencyMappingString) ? new JsonSlurper().parseText(displayCurrencyMappingString) : [:]

        String selectedEntry = mappingEntries.get(sourceLanguage)
        Map output = [:]
        if (selectedEntry) {
            List currencyPairs = selectedEntry.split(',').toList()*.trim()
            currencyPairs.each {
                List entry = it.split('=').toList()*.trim()
                if (entry.size() == 2) {
                    output.put(entry[0], entry[1])
                }
            }
        }
        return output
    }

    void buildByType(Map productSource, String type, boolean expandTeaser) {
        def references = productSource.productReferences.findAll { it.referenceTypeCode in getReferenceList(type) }
        def refCount = references.size()
        if (refCount) {
            builder."${type}"(label: articleTypeTranslation.get(type), count: refCount) {
                if (expandTeaser) {
                    references.each { prodRef ->
                        this.buildMaterialTeaser(prodRef, productSource)
                    }
                }
            }
        }
    }

    void buildMaterialTeaser(prodRef, Map productSource, String filter = '') {
        def productTarget = prodRef.target
        if (!productTarget) {
            logger.log("[WARNING] No target found for product code - ${prodRef.targetCode}")
        } else {
            if (!productTarget?.formattedCode) {
                logger.log("[WARNING] No value for formattedCode found for product code - ${prodRef.targetCode}")
            }
            def productName = getAttribute(productTarget, 'productFeatures', 'atr_Product_Name', 1)
            if (!productName || !productName?.formattedValue) {
                logger.log("[WARNING] No value for attribute atr_Product_Name found for product code - ${prodRef.targetCode}")
            }
            Map attributes = [matNo: productTarget.formattedCode, label: productName?.value, img: getPrimaryImageURL(productTarget), design: getDesignText(productTarget), shortpos: getShortposText(productTarget)]
            if (filter) {
                attributes << [filter: filter]
            }
            def sellMat = productSource.productReferences.find {
                it.referenceTypeCode in ['ref_mandatory_accessories_central',
                                         'ref_mandatory_accessories_local',
                                         'ref_recommended_accessory_central',
                                         'ref_recommended_accessory_local',
                                         'ref_miele_service_certificate_central',
                                         'ref_miele_service_certificate_local',
                                         'ref_central_services',
                                         'ref_local_services']
            }
            if (sellMat) {
                attributes << [sellMat: '1']
            }
            def taxes = productTarget.classificationClassProductReferences.findAll { it.referenceTypeCode == 'ref_legend_classification' }
            if (taxes?.size() > 1) {
                logger.log('[WARNING] More than one legend class found')
            }
            if (taxes[0]) {
                attributes << [tax: taxes[0].source?.code?.replace('cls_', '')]
            }
            builder.materialTeaser(attributes) {

                def price = buildPrice(builder, productTarget, logger)

                buildDisposibility(builder, productTarget)

                buildShop(builder, productTarget, price, logger)
                // eco/award
                buildIcon(builder, productTarget)

                buildPromotion(builder, productTarget)

                buildProductNotice(builder, productTarget)
            }
        }
    }

    private void buildProductNotice(Object builder, Map product) {
        def productNoticeNode = getAttribute(product, 'productFeatures', 'atr_product_notice', 1)
        if (productNoticeNode) {
            builder.productNotice { mkp.yieldUnescaped("<![CDATA[${productNoticeNode.formattedValue}]]>") }
        }
    }

    boolean matchingMaterialExists(Map product) {
        return product.productReferences.any { it.target?.externalObjectTypeCode == 'obj_article' }
    }

    private String getPrimaryImageURL(Map product) {
        def primageImageRef = product.productMediaReferences.find { it.referenceTypeCode == 'PrimaryProductImage' }
        if (primageImageRef) {
            def mediaUrl = primageImageRef.target?.url
            return "${mediaUrl}${shapeMapping.get('Z07')}"
        } else {
            return ''
        }
    }

    private String getDesignText(Map product) {
        def designCORef = product.productContentObjectReferences.find { it.referenceTypeCode == 'ref_design_type_reference' }
        if (designCORef) {
            return designCORef.target?.shortText
        } else {
            return ''
        }
    }

    private String getShortposText(Map product) {
        List designCORefs = product.productContentObjectReferences.findAll {
            it.referenceTypeCode == 'ref_short_positioning_reference'
        }.toSorted { a, b -> a.sortOrder <=> b.sortOrder ?: a.targetCode <=> b.targetCode }
        if (designCORefs) {
            if (designCORefs.size() > 1) {
                logger.log("[WARNING] More than one text found for shortpos attribute in product ${product.code}")
            }
            def inlineText = replaceInlineAttribute(designCORefs[0].target?.shortText, product)
            return inlineText
        } else {
            return ''
        }
    }

    private List getReferenceList(String type) {
        // Include non-breaking space (\p{Z}) or whitespace (\s) after comma
        return articleTypeMapping.get(type).split(/,[\p{Z}\s]*/).toList()
    }

    private Map getAttribute(Map entity, String attributeNodeName, String attributeCode, int multiValuePosition) {
        def attribute = entity."${attributeNodeName}".find {
            it.attributeCode == attributeCode && it.multiValuePosition == multiValuePosition
        }
        return attribute
    }

    Map buildPrice(Object builder, Map product, MappingLogger logger) {
        def atrPrice = getAttribute(product, 'productFeatures', 'atr_ZZ2CAT_NPRICE', 1)

        Map retrievedPrice = this.prices.get(product.code)
        if (retrievedPrice && (atrPrice?.formattedValue || retrievedPrice.value != 0)) {
            def displayCurrency = this.displayCurrencyEntries.get(retrievedPrice.currencyCode) ?: retrievedPrice.currencyCode
            Map attributes = [value: retrievedPrice.formattedValue, unit: retrievedPrice.unitCode, currency: displayCurrency, val: retrievedPrice.value, currency_iso: retrievedPrice.currencyCode]
            def vpreh = getAttribute(product, 'productFeatures', 'atr_VPREH', 1)
            def inhal = getAttribute(product, 'productFeatures', 'atr_INHAL', 1)
            def inhme = getAttribute(product, 'productFeatures', 'atr_INHME_shortText', 1)
            if ((vpreh?.value as BigDecimal) && (inhal?.value as BigDecimal) && inhme?.formattedValue) {
                BigDecimal pricePerUnit = (retrievedPrice.value as BigDecimal) * (vpreh?.value as BigDecimal) / (inhal?.value as BigDecimal)
                attributes.put('reference', "${vpreh.value} ${inhme.formattedValue} = ${pricePerUnit.setScale(2, BigDecimal.ROUND_HALF_UP)} ${retrievedPrice.currencyCode}")
            } else {
                logger.log("[WARNING] Price reference cannot be calculated for product code - ${product.code}. Price='${retrievedPrice.value}', atr_VPREH='${vpreh?.value}', atr_INHAL='${inhal?.value}', atr_INHME_shortText='${inhme?.formattedValue}' ")
            }
            builder.price(attributes)
            return retrievedPrice
        } else {
            logger.log("[WARNING] Price not found for product code - ${product.code}")
        }

    }

    public void buildShop(Object builder, Map product, Map price, MappingLogger logger) {
        Map attributes = [:]
        String type
        String isIntern
        String countryGroup = "${this.targetCountry}-${this.targetGroup}"
        String id = this.shopIdMapping.get(countryGroup)

        if (!id) {
            logger.log("[WARNING] Missing shop configuration for ${countryGroup}")
        }

        String link = this.shopLinkMapping.get(id)
        if (!link) {
            logger.log("[WARNING] Missing link configuration for the shop id - ${id}")
        }

        isIntern = this.shopLinkIsInternMapping.get(id)
        if (isIntern == '1') {
            type = 'intern'
        } else if (isIntern == '0') {
            type = 'extern'
        } else {
            logger.log("[WARNING] Missing 'is intern' configuration for the shop id - ${id}")
        }

        if (product.orderable && link && ( ( price && type == 'intern' ) || type == 'extern' ) ) {
            attributes << ['id': id.padLeft(5, '0')]

            link = link.replace('$MATNR$', product.formattedCode as String)
            attributes << ['link': link]

            if (type) {
                attributes << ['type': type]
            }

            def availability = this.availabilityMapping.get(product.code)
            if (!availability) {
                logger.log("[WARNING] Missing availability for product - ${product.code}")
            }
            if (availability) {
                attributes << ['avail': availability]
            }

            builder.shop(attributes) {}
        } else {
            logger.log("[WARNING] Shop not built for product code - ${product.code}")
        }
    }

    private void buildDisposibility(Object builder, Map product) {
        def deliveryAttribute = getAttribute(product, 'productFeatures', 'atr_CA_DELIVERY_TIME', 1)
        if (deliveryAttribute?.formattedValue) {
            builder.disposibility1(deliveryAttribute.formattedValue)
        }
    }

    void buildPromotion(Object builder, Map product) {
        this.productsWithPromotions.find { prd -> prd.code == product.code }?.promotions?.each { promotion ->
            builder.io_PROMO(ioID: promotion.code)
        }
    }

    private void buildIcon(Object builder, Map product) {
        def energyLabelRef = product.productMediaReferences.find { it.referenceTypeCode == 'ref_energylabel_to_product_png' }
        def onlineLabelRef = product.productMediaReferences.find { it.referenceTypeCode == 'ref_pim_onlinelabel' }
        int iconCount = 0
        if (energyLabelRef || onlineLabelRef) {
            Map attributes = [type:'eco']
            if(onlineLabelRef) attributes << [src: onlineLabelRef.target?.url]
            if(energyLabelRef) attributes << [detailSrc: energyLabelRef.target?.url]
            def pdfRef = product.productMediaReferences.find { it.referenceTypeCode == 'ref_eu_pi_data_sheet_pdf' }
            if (pdfRef) {
                attributes << [pdf: pdfRef.target?.url]
                attributes << [label: this.mediaDocTypeTranslation.get('ref_eu_pi_data_sheet_pdf')]
            }
            builder.icon(attributes)
            iconCount = 1
        }
        List awardRefs = product.productMediaReferences.findAll { it.referenceTypeCode == 'ref_pim_award' }
        awardRefs.eachWithIndex { awardRef, int i ->
            if (iconCount + i < 3) {
                def baseUrl = awardRef.target?.url
                builder.icon(type: 'award', src: "${baseUrl}${shapeMapping.get('Z02')}", detailSrc: "${baseUrl}${shapeMapping.get('Z13')}")
            }
        }
    }

    private String replaceInlineAttribute(String input, Map product) {
        String regexPattern = /<inRef\.attrid>([^<]+)<\/inRef\.attrid>/
        def matcher = (input =~ regexPattern)
        matcher.size().times {
            def matchedLine = matcher[it][0]
            def attribute = matcher[it][1]
            def prodFeature = product.productFeatures.find { it.attributeCode == attribute }
            if (prodFeature) {
                def replacement = (prodFeature.attributeUnit?.name) ? "${prodFeature.formattedValue}&nbsp;${prodFeature.attributeUnit.name}" : prodFeature.formattedValue
                input = input.replaceFirst(matchedLine, replacement)
            } else {
                logger.log("[WARNING] Could not resolve inline attribute ${attribute} of product code - ${product.code}")
                // Fallback replacement
                input = input.replaceAll('inRef.attrid', 'inRef_attrid')
            }
        }
        return input
    }
}