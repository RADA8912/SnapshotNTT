import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

def Message processData(Message message) {
    
    def body = message.getBody(String)
    
    // JSON-Daten parsen
    def parsedData = new JsonSlurper().parseText(body)
   
    // Neues Array für die serviceParameterLists
    def grouping = []
    def serviceList = []
    

    // Iteriere über jedes Array in den parsedData
    parsedData.each { array ->
    array.each { item ->
        // Überprüfen, ob serviceParameterList vorhanden ist
        if (item.serviceParameterList) {
            // Hinzufügen der serviceParameterList zum serviceList
            serviceList.add(serviceParameterList:item.serviceParameterList)
        }
    }
}

grouping.add(serviceList: serviceList)

def outPutJSon = JsonOutput.toJson(grouping)
message.setBody(outPutJSon)
   
    return message
}
