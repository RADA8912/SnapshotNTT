/*
 */
import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.mapping.ValueMappingApi;
import groovy.util.XmlSlurper;
import groovy.util.XmlParser;

def Message processData(Message msg) {

def helperValMap = ITApiFactory.getApi(ValueMappingApi.class, null);
def keySender = [];
def map = msg.getHeaders();

//Get Key
keySender.add(map.get('EDI_Sender_ID'.toString()))
keySender.add(map.get('EDI_Receiver_ID'.toString()))
keySender.add(map.get('EDI_Message_Type'.toString()))
keySender.add(map.get('EDI_Message_Version'.toString()))
keySender.add(map.get('EDI_Message_Release'.toString()))
keySender = keySender.join('|');

//Get Receiver Address from Value Mapping
def keyReceiver = helperValMap.getMappedValue('EDI', 'UNB_UNH_INFO', keySender , 'CPI', 'ProcessDirectAddr');

//Set Property - Receiver Address
msg.setProperty("receiverAddr", keyReceiver);

return msg;
}