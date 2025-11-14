import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

def Message processData(Message message) {
       def messageLog = messageLogFactory.getMessageLog(message)
       
       Reader reader = message.getBody(Reader)
       def properties = message.getProperties() 
       def input = new JsonSlurper().parse(reader)
       def inspectionLot = input.InspectionLot
       
       messageLog.addCustomHeaderProperty("Inspection Lot", inspectionLot)
       messageLog.addCustomHeaderProperty("Type", properties.get('routingCondition'))
       
       return message
}