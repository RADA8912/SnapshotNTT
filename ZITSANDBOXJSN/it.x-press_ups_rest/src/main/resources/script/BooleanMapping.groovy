package com.nttdata.ndbs.btp.soap

import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.util.*
import groovy.xml.QName;

def Message processData(Message message) {
	
	def newBody = message.getBody(String.class)
	newBody = newBody.replace("\"X\",", "true,")
	
	message.setBody(newBody);

	return message;
}
