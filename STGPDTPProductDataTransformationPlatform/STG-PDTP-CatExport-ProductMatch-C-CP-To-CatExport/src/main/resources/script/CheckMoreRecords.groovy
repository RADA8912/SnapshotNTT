/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonException
import groovy.json.JsonParserType
import groovy.json.JsonSlurper

def Message processData(Message message) {
	Map inputRoot
	try {
		inputRoot = new JsonSlurper(type: JsonParserType.CHARACTER_SOURCE).parse(message.getBody(Reader))
	} catch (JsonException ignored) {
		// If there is a parsing error due to incomplete JSON response,
		// set hasMoreRecords so that the pagination call is executed again
		message.setProperty('hasMoreRecords', 'true')
		// Log warnings into the MPL attachment
		messageLogFactory?.getMessageLog(message)?.addAttachmentAsString('Invalid JSON response', message.getBody(String), 'application/json')
		return message
	}

	// Check if body already stored from earlier call, and combine it
	List accumulatedBody = message.getProperty('accumulatedBody')
	if (accumulatedBody?.size()) {
		accumulatedBody.addAll(inputRoot.value)
	}

	// If value is not empty, then store payload in property, and prepare offset for next call
	if (inputRoot.value.size()) {
		message.setProperty('hasMoreRecords', 'true')
		message.setProperty('accumulatedBody', accumulatedBody ?: inputRoot.value)
		message.setBody('Dummy')
		int pageSize = message.getProperty('pageSize') as int
		int pageOffset = message.getProperty('pageOffset') as int
		message.setProperty('pageOffset', pageOffset + pageSize) // Increase the offset to the next page
	} else {
		message.setProperty('hasMoreRecords', 'false')
		message.setProperty('accumulatedBody', [])
		message.setBody(accumulatedBody ? ['value': accumulatedBody] : inputRoot)
	}
	return message
}