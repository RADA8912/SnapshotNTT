/*
Since not every parameter is used for CREATE or UPDATE, they have to be removed.
*/

import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper;
import groovy.json.JsonBuilder;

def Message processData(Message message) {
    // Retrieve the message body as a string
    String body = message.getBody(String.class);

    // Parse the JSON body
    JsonSlurper jsonSlurper = new JsonSlurper();
    Map parsedJson = jsonSlurper.parseText(body);
    
    // Check if the InspectionData node exists and extract its content
    if (parsedJson.InspectionData) {
        Map inspectionData = parsedJson.InspectionData;
        
        // Safely convert InspResultValidValuesNumber and InspResultNmbrOfRecordedRslts to Integer
        int validValuesNumber = inspectionData.InspResultValidValuesNumber ? inspectionData.InspResultValidValuesNumber.toInteger() : 0;
        int numberOfRecordedResults = inspectionData.InspResultNmbrOfRecordedRslts ? inspectionData.InspResultNmbrOfRecordedRslts.toInteger() : 0;
        
        
        // Update the map with Integer values and remove the qualitative parameter
        inspectionData.InspResultValidValuesNumber = validValuesNumber;
        inspectionData.InspResultNmbrOfRecordedRslts = numberOfRecordedResults;
        
        if (inspectionData.operation == 'UPDATE')
        {
            inspectionData.remove('InspResultValidValuesNumber'); // not needed for update and leads to an error
            inspectionData.remove('InspResultNmbrOfRecordedRslts'); // not needed for update and leads to an error

        }
        
        if (inspectionData.operation == 'CREATE')
        {
            inspectionData.remove('InspectionValuationResult'); //reset valuation (reject or accept value)
            inspectionData.remove('InspectionResultOriginalValue'); //since there is no original value, this is only needed for update operation            
        }
        
       if (inspectionData.qualitative == 'true')
        {
            inspectionData.remove('InspResultFrmtdMeanValue');  // not needed for qualitative results and leads to an error
          //  inspectionData.remove('CharacteristicAttributeCodeGrp'); // not needed for qualitative results and leads to an error
        }
        

        inspectionData.remove('qualitative'); // Remove the qualitative parameter
        inspectionData.remove('operation'); //Remove operation
        inspectionData.remove('IsCharacteristicAddedLater');
        
        // Create new JSON without the InspectionData parent node
        parsedJson = inspectionData // Update this line if you intend to remove the InspectionData wrapper
        
        JsonBuilder jsonBuilder = new JsonBuilder(parsedJson);
        String newJson = jsonBuilder.toPrettyString(); // Converts back to JSON string
        
        //Create Attachment
         def messageLog = messageLogFactory.getMessageLog(message)
         if (messageLog != null) {
        messageLog.addAttachmentAsString("Outbound_Payload", newJson, "text/plain")
         }
    
        // Set the new JSON string as the message body 
        message.setBody(newJson);
    } else {
        // Log a warning if the InspectionData node does not exist
        messageLog.addWarning("The 'InspectionData' node does not exist in the payload.");
    }
    

    return message;
}
