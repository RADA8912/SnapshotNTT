import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import java.io.*

/**
 * Set IDoc header properties
 */
def Message processData(Message message) {

   def prop = message.getProperties()
    def messageLog = messageLogFactory.getMessageLog(message)
 

    String PurchaseOrderID = prop.get("PurchaseOrder")


	//Set custom header properties for message log API
	messageLog.addCustomHeaderProperty("PurchaseOrderID", PurchaseOrderID)

	return message
}