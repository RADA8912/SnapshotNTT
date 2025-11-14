/*
 * Copyright (c) 2022 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput

def Message processData(Message message) {

	List products = message.getProperty('OrderableProductsList')
	int loopIndex = message.getProperty('CamelLoopIndex') as int
	int pageSize = message.getProperty('pageSize') as int

	// Extract the subsection of the list based on current loop index
	int start = loopIndex * pageSize
	int end
	if ((loopIndex + 1) * pageSize >= products.size()) {
		end = products.size()
		message.setProperty('EndOfOrderableProductsList', 'true')
	} else {
		end = (loopIndex + 1) * pageSize
		message.setProperty('EndOfOrderableProductsList', 'false')
	}
	List output = products.subList(start, end)

	// Store JSON array in property to be used in query string of API call
	message.setProperty('OrderableProducts', JsonOutput.toJson(output))
	return message
}