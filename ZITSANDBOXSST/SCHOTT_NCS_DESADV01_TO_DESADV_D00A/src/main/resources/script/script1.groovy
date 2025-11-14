/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/en-GB/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/en-GB/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.it.api.ITApiFactory
import com.sap.it.api.mapping.ValueMappingApi
import groovy.xml.*

String getValueMap (String senderQualifier, String targetQualifier, String senderValue) {
    
    def valueMapApi = ITApiFactory.getService(ValueMappingApi.class, null)
    try {
        
        String targetValue = valueMapApi.getMappedValue('SCHOTT', senderQualifier, senderValue, 'PARTNER', targetQualifier )

    } catch (Exception e){
        targetValue = 
    }
    return targetValue
}
def Message processData(Message message) {
    
    def body = message.getBody(String)
    def xml = new XMLSlurper.parseText(body)
    //RCVPRN:VSTEL
    def partnerPlant = xml.EDI_DC40.RCVPRN.text() + ":" + xml.E1EDK08.VSTEL.text()
    
    
    def receiverMailbox = getValueMap("PARTNER_PLANT", "RECEIVER_MAILBOX", partnerPlant)
    def senderMailbox = getValueMap("PARTNER_PLANT", "SENDER_MAILBOX", partnerPlant)
}