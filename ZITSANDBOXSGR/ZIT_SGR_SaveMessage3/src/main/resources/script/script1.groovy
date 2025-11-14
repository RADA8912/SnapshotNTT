import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.*
import java.io.*

/**
* CopySessionCookies
* This Groovy script copies session cookies in message header and message log.
*
* @author itelligence.de
* @version 1.0.0
*/

def Message processData(Message message) 
{
	final String ITEM_SEPARATOR = "; "

	def headers = message.getHeaders()

	// Get cookies
	def cookie = headers.get("Set-Cookie")
	StringBuffer buffer = new StringBuffer()
	for (Object item : cookie) {
		buffer.append(item + ITEM_SEPARATOR)
	}

	// Set cookies to header
	message.setHeader("Cookie", buffer.toString())

	// Set cookies to log
	def messageLog = messageLogFactory.getMessageLog(message)
	if(messageLog != null)
	{
		messageLog.setStringProperty("Session_Cookies", buffer.toString())
	}

	return message
}