import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlParser
import groovy.json.JsonBuilder

def Message processData(Message message) 
{
	def body = message.getBody(String);
	def parsedXml = new XmlParser().parseText(body)
	def jsonBuilder = new JsonBuilder()

	jsonBuilder.Order {
    Header {
        OrderNumber parsedXml.Header.OrderNumber.text()
        Date parsedXml.Header.Date.text()
    }
    Items {
        parsedXml.Item.each { item ->
            Item {
                ItemNumber item.ItemNumber.text()
                MaterialNumber item.MaterialNumber.text()
                Quantity item.Quantity.text()
                Valid item.Valid.text()
            }
        }
    }
}
def jsonOutput = jsonBuilder.toString()
message.setBody(jsonOutput)
return message
}