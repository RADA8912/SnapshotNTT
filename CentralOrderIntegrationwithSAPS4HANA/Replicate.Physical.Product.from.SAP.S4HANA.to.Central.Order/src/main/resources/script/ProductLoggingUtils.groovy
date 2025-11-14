package src.main.resources.script

import com.sap.gateway.ip.core.customdev.util.Message
import com.sap.it.api.msglog.MessageLogFactory
import groovy.json.JsonOutput
import groovy.xml.XmlUtil

public class ProductLoggingUtils {
	
	String FORMAT_TEXT	= 'text/plain'
	MessageLogFactory msgLogFactory

	public ProductLoggingUtils (MessageLogFactory messageLogFactory) {
		this.msgLogFactory = messageLogFactory
	}

	// Output a log page if the log level is DEBUG
	def Message outputDebugLog(Message message, String logTitle, String log, String format = FORMAT_TEXT) { 
		if (isLogLevelDebug(message)) { 
			this.outputToLog(message, logTitle, log, format);
		}
		return message;
	}

	// Creates a log page in CPI with the provided log title
	def Message outputToLog(Message message, String logTitle, String log, String format = FORMAT_TEXT) { 
		def messageLog = this.msgLogFactory.getMessageLog(message);
		messageLog.addAttachmentAsString(logTitle, log, format);
		
		return message;
	}
	
	def Boolean isLogLevelDebug(Message message) { 
		def isLogLevelDebug = false;
		def processingLogConfiguration = message.getProperty("SAP_MessageProcessingLogConfiguration") as String;
		if (processingLogConfiguration.contains("logLevel=DEBUG") || processingLogConfiguration.contains("logLevel=TRACE")) { 
			isLogLevelDebug = true;
		}
		return isLogLevelDebug; 
	}

	def String prettyPrintJson(Message message) {
		try {                
			def body_string = message.getBody(java.lang.String) as String;
			body_string = body_string==null ? "[]": body_string;
			return JsonOutput.prettyPrint(body_string);
		}
		catch(Exception ex) {
			return "Error parsing message body as JSON: " + ex.getMessage();
		}
	}

	def String prettyPrintXML(Message message) { 
		try {
			def body = message.getBody(java.io.Reader);
			if (body) {
				def xml = new XmlParser().parse(body);
				return XmlUtil.serialize(xml);
			}
			else {
				body = message.getBody(java.lang.String);
				return XmlUtil.serialize(body);
			}
		}
		catch(Exception ex) {
			return "Error parsing message body as XML: " + ex.getMessage();
		}
	}
}
