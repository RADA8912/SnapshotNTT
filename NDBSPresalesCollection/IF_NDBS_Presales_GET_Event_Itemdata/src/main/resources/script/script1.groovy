import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder

def Message processData(Message message) {
    //Body
    def body = message.getBody(java.lang.String) as String
    

    // Parse JSON
    def jsonSlurper = new JsonSlurper()
	def object = jsonSlurper.parseText(body)

    // Extrahiere das Product-Element
    def ProductID = object.data.Product

    // Setze das extrahierte Element in die Property ProductID
    message.setProperty("ProductID", ProductID)

    return message;
}