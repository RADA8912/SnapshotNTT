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
		EO_RECEIVER_MESSAGE = map.get("EO_RECEIVER_MESSAGE");
		EO_RECEIVER_MESSAGEFORMAT = map.get("EO_RECEIVER_MESSAGEFORMAT");
		EO_PD_RECEIVER = map.get("EO_PD_RECEIVER");	
		// Calculate Key for sender agreement
		key_receiverAgreement = EO_RECEIVER + '#' + EO_RECEIVER_MESSAGE + '#' + EO_RECEIVER_MESSAGEFORMAT + '#' + EO_PD_RECEIVER
	
		// execute VM to determine iflow to transfer message to receiver
		def EO_PD_RECEIVER = valueMapApi.getMappedValue(eo_agency, 'ReceiverAgreement', key_receiverAgreement, eo_agency, 'EO_PD_RECEIVER')

		// Write Results to headed
        message.setHeader("EO_PD_RECEIVER", EO_PD_RECEIVER);
       
       return message;
}
