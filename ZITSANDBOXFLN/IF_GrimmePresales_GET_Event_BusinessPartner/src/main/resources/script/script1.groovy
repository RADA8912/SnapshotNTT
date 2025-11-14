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

    // Extrahiere das BusinessPartner-Element
    def BusinessPartner = object.data.BusinessPartner

    // Setze das extrahierte Element in der Property "businessPartnerProperty"
    message.setProperty("BusinessPartner", BusinessPartner)
    println(BusinessPartner)
    // Ausgabe zur Überprüfung
    
    return message;
}