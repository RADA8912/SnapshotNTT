import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.xml.*;
import java.io.*;

/**
 * This script checks if the leasing rates are similar. If yes, no change is necessary.
 * Therefore a property is set that is used in the subsequent router
 * @param message
 * @return message
 */

def Message processData(Message message)
{
    def props = message.getProperties()
    def oldRate = props.get("REUnitPrice").toDouble()
    def newRate = props.get("ConditionValue").toDouble()



    if(oldRate == newRate){
       message.setProperty("RateSimilar", "true")
    } else {
       message.setProperty("RateSimilar", "false")

    }

    return message



}

