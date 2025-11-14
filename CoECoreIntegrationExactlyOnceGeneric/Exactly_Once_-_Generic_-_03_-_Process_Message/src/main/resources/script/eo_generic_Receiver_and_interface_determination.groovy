import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.it.api.ITApiFactory
import com.sap.it.api.mapping.ValueMappingApi

def Message processData(Message message) {
        def map = message.getHeaders();
        def valueMapApi = ITApiFactory.getService(ValueMappingApi.class, null)
		
		def eo_agency = "EO_GENERIC"
		
		// Read Sender Agreement values from headers
		EO_RECEIVER = map.get("EO_SENDER");
		EO_MESSAGETYPE = map.get("EO_MESSAGETYPE");
		EO_MESSAGEFORMAT = map.get("EO_MESSAGEFORMAT");	
		// Calculate Key for sender agreement
		key_senderAgreement = EO_RECEIVER + '#' + EO_MESSAGETYPE + '#' + EO_MESSAGEFORMAT
	
		// execute VM to determine receiver
		def EO_RECEIVER = valueMapApi.getMappedValue(eo_agency, 'SenderAgreement', key_senderAgreement, eo_agency, 'EO_RECEIVER')
       
	   
		// Calculate Key for Receiver Determination
		key_receiverDetermination = key_senderAgreement + '#' + EO_RECEIVER
		// execute VM to determine Mapping (process Direct Path)
		def EO_PD_MAPPING = valueMapApi.getMappedValue(eo_agency, 'ReceiverDetermination', key_receiverDetermination, eo_agency, 'EO_PD_MAPPING')
		
       
		// Write Results to headed
        message.setHeader("EO_RECEIVER", EO_RECEIVER);
        message.setHeader("EO_PD_MAPPING", EO_PD_MAPPING);
       
       return message;
}
