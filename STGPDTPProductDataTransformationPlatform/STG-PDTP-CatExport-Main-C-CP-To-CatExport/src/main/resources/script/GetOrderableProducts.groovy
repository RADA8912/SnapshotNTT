/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput

def Message processData(Message message) {
	Map root = message.getBody()
	List productsFull = root.value

	List products = productsFull.collect { it.code }

	message.setProperty('OrderableProductsList', products)
	message.setProperty('OrderableProductsFound', (products.size()) ? 'true' : 'false')
	return message
}