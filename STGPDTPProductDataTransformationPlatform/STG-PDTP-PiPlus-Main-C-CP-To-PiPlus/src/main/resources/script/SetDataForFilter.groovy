/*
 * Copyright (c) 2022 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	// Set empty map/list when triggering Filter flow from PiPlus
    message.setHeader('XShopIdMapping', '{}')
    message.setHeader('XShopLinkMapping', '{}')
    message.setHeader('XShopLinkIsInternMapping', '{}')
    message.setHeader('XMediaShapeRatios', '{}')
    message.setHeader('XMediaShapeRatioMapping', '{}')
    message.setHeader('XPromotionsPayload', ['value': []])
	message.setBody('{}')
    return message
}