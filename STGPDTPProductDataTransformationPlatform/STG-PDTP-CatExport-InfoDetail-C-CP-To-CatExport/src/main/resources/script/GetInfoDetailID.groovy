import com.sap.gateway.ip.core.customdev.util.Message

/*

* Copyright (c) 2021 Miele & Cie. KG - All rights reserved.

*/

Message processData(Message message) {
	final reader = message.getBody(Reader)
	final root = new XmlSlurper().parse(reader)
	message.setProperty('infoDetailID', root.children()[0].@ioID.text())
	return message
}
