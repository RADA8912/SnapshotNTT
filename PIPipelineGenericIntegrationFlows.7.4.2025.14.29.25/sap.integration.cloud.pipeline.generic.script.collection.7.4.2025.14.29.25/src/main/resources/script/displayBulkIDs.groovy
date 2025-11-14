import com.sap.gateway.ip.core.customdev.util.Message
import org.w3c.dom.NodeList

def Message processData(Message message) {
    def nodes = message.getProperty('nodelistToHeader') as NodeList
    def headerName = message.getProperty('nodelistName') as String
    def messageLog = messageLogFactory.getMessageLog(message)
    
    for (i = 0; i < nodes.getLength(); i++) {
        messageLog?.addCustomHeaderProperty(headerName, nodes.item(i).getTextContent())
    }

    return message
}