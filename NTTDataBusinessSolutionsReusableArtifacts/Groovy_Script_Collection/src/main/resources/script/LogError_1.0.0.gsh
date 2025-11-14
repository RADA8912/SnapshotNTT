import com.sap.gateway.ip.core.customdev.util.Message
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Logs error message as attachment
 */
def Message processData(Message message) {
	Logger log = LoggerFactory.getLogger(this.getClass())

	try {
		def map = message.getProperties()
		def ex = map.get("CamelExceptionCaught")

		if (ex!=null) {
			if (ex.getClass().getCanonicalName().equals("org.apache.camel.component.ahc.AhcOperationFailedException")) {
				def responseBody = ex.getResponseBody()

				def statusText = ex.getStatusText()
				def statusCode = ex.getStatusCode()

				message.setBody(responseBody)
			}
		}

	} catch (Exception ex) {
		def messageLog = messageLogFactory.getMessageLog(message)
		if(messageLog != null)
		{
			messageLog.addAttachmentAsString("http_handler exception", ex.getMessage(), "plain/text")
		}
	}

	return message
}