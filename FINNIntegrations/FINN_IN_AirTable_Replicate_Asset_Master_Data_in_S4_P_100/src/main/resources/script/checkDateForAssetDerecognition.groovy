import com.sap.gateway.ip.core.customdev.util.Message;
import java.text.SimpleDateFormat

/**
 * This method checks whether the compund date is before the actual date.
 * If so, a asset derecognition needs to be processed.
 * @param message
 * @return
 */

def Message processData(Message message) {

    def compoundDate = message.getProperties().get("compoundDate")
	def nextUse = message.getProperties().get("nextUse")

    // set calendar to current date
    Date now = new Date();
    sdf = new SimpleDateFormat("YYYY-MM-dd")
    def actualDate = sdf.format(now)

    //When the opsArrivalDate is earlier than the actual date, set boolean to true
	
	def dateBoolean = compoundDate < actualDate
	def nextUseBoolean = nextUse == 'defleeting'
	
	
    message.setProperty("Z_AssetTransfer", dateBoolean && nextUseBoolean)

    return message;
}




