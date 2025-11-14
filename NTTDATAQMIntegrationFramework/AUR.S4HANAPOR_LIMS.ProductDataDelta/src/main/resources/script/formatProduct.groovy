import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

Message processData(Message message) {
    
    Reader reader = message.getBody(Reader)    
    def json = new JsonSlurper().parse(reader)
    
    // Entfernen der unerwünschten Felder
    json.remove("@odata.context")
    json.remove("@odata.metadataEtag")
    json.remove("@odata.nextLink")
    
    // Umbenennen von "value" zu "product"
    if (json.containsKey("value")) {
        json["ProductList"] = json.remove("value")
        
        // Entfernen nicht benötigter Felder innerhalb des Arrays
        json["ProductList"].each { it.remove("SAP__Messages") }
        json["ProductList"].each { it.remove("ProductConfiguration") }
    }
    
    // JSON zurück in String konvertieren und setzen
    message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(json)))
    return message
}
