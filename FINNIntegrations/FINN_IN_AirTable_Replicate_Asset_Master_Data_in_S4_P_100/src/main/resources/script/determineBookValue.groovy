import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.*

/*
*
* This method stores all cumulated acquisiton values as well as the 
* depreciation values. 
* These are then used to determine the book value of the assets for
* depreciation area 01, 10 and 30.
*/


def Message processData(Message message) {
    //Get the body for the XML Parsing and the message log
    def body = message.getBody(java.lang.String)
    def messageLog = messageLogFactory.getMessageLog(message);

    //Parse the text as XML
    def list = new XmlSlurper().parseText(body) 
    
    //Extract Cumuluated acuqisiton values for that business year
    def bookValue = list.T_ANLCV.item.BCHWRT_LFD
    def depArea = list.T_ANLCV.item.AFABE
    def errorCode = list.E_RETCD

    if(errorCode == '8'){        
        //Set as Custom Header for error tracing
        messageLog.addCustomHeaderProperty("Book Value Zero", message.getProperties().get("Z_CAR_ID_DOWN"));    
        //Used to fill EVAL_GROUP1 with 1 as error indication in SAP
        message.setProperty("ZErrorAsset", "true")
        message.setProperty("ZErrorCode", errorCode)

        //Set Book values to 0
        message.setProperty("ZBookValue01", 0)
        message.setProperty("ZBookValue10", 0)
        message.setProperty("ZBookValue30", 0)

    }
    else {
        //Define the map
        def emptyMap = [:]
        //Fill the mal with the values for the depreciation area
        for (int i = 0; i < bookValue.size(); i++){
            emptyMap[depArea[i].toString()] = bookValue[i].toFloat()
        }


        message.setProperty("ZBookValue01", emptyMap['01'])
        message.setProperty("ZBookValue10", emptyMap['10'])
        message.setProperty("ZBookValue30", emptyMap['30'])
        //No errors detected
        message.setProperty("ZErrorAsset", "false")
    }
   
    message.setBody(message.getProperty("message"))

    return message;
}




