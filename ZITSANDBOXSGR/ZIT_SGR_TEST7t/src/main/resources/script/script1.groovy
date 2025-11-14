import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    String LINE_SEPARATOR = '\r\n'

    String body = ''
	body += "Operating System" + LINE_SEPARATOR
	body += "----------------" + LINE_SEPARATOR
	body += "OS name: " + System.properties['os.name'] + LINE_SEPARATOR
	body += "OS version: " + System.properties['os.version'] + LINE_SEPARATOR
	body += "OS architecture: " + System.properties['os.arch'] + LINE_SEPARATOR
	
	message.setBody(body)

    return message
}