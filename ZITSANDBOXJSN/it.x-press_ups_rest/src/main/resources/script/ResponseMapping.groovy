package com.nttdata.ndbs.btp.soap

import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.util.*
import groovy.xml.QName;

def Message processData(Message message) {
    def nodeName = new QName(message.getProperty('ITX_Namespace'), 'MT_Response', 'n0')
	def response = new Node(null, nodeName)
	
	new Node(response,'content',message.getBody(String.class))
	
	def contentTypeHeader = new Node(response,'header')
	new Node(contentTypeHeader,'header_key','ContentType')
	new Node(contentTypeHeader,'header_value',message.getHeader('Content-Type', String.class))
	
	def statusHeader = new Node(response,'header')
	new Node(statusHeader,'header_key','HTTP_STATUS')
	new Node(statusHeader,'header_value',message.getHeader('CamelHttpResponseCode', String.class))
	
	message.setBody(groovy.xml.XmlUtil.serialize(response));

	return message;
}
