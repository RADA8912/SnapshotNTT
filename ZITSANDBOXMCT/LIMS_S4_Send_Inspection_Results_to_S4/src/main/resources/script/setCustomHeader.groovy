import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import java.io.*

/**
 * Set IDoc header properties in case multiple Idocs are send via the generic S4 iflow. In this case all created Idocs are listed in the Custom Header. 
 */
def Message processData(Message message) {
        //Create Message Factory
       def messageLog = messageLogFactory.getMessageLog(message)
       
       def InspectionLot = message.getProperty("InspectionLot")
       def InspPlanOperationInternalID = message.getProperty("InspPlanOperationInternalID")
       def InspectionCharacteristic = message.getProperty("InspectionCharacteristic")
       
       //Add Custom Header with the desired Name and the contents of the Headerfield
       messageLog.addCustomHeaderProperty("Inspection Lot", InspectionLot)
       messageLog.addCustomHeaderProperty("InspPlanOperationInternalID", InspPlanOperationInternalID)
       messageLog.addCustomHeaderProperty("InspectionCharacteristic", InspectionCharacteristic)
       
       return message
}