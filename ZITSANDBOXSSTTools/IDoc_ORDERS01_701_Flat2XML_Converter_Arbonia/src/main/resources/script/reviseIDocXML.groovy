import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message)
{
	def result = "<?xml version=\'1.0\' encoding=\'UTF-8\' standalone=\'no\'?>\n"
	result += message.getBody(String).replaceAll("BEGIN=\"1\"", "BEGIN=\'1\'").replaceAll("SEGMENT=\"1\"", "SEGMENT=\'1\'")
	message.setBody(result)
    return message
}