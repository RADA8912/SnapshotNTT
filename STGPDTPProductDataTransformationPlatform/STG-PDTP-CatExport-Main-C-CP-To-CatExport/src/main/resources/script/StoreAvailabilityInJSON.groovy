/*
 * Copyright (c) 2022 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput

def Message processData(Message message) {
	List allProductAvailability = message.getProperty('accumulatedBody')

	Map output = allProductAvailability.collectEntries {
		[(it.productCode): it.statusValue]
	}

	// Store availability detail as a JSON string containing map of productCode and statusValue fields
	message.setBody(JsonOutput.toJson(output))
	return message
}