import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.StreamingMarkupBuilder

def Message processData(Message message) {
	def body = message.getBody(java.lang.String);
	def root = new XmlSlurper().parseText(body);
	def outputBuilder = new StreamingMarkupBuilder()
	outputBuilder.encoding = "UTF-8"

	def outxml = {
		Target{
			Result(root.arg1.toInteger() + root.arg2.toInteger())
		}
	}

	String result = outputBuilder.bind(outxml)
	message.setBody(result)
	return message;
}