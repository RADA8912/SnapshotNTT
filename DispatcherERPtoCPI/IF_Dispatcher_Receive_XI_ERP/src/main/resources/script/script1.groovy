import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.mapping.ValueMappingApi;
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;




def Message processData(Message message){

//Get Headers 
	def map = message.getHeaders();
	def value = map.get("SapSenderService");

def sAgency = "ERP_XI";
def sIdentifier = "SENDER";
def tAgency = "PROCESSDIRECT";
def tIdentifier = "RECEIVER";
def key = value; 

def service = ITApiFactory.getApi(ValueMappingApi.class, null);


if( service != null) {


def SenderService=service.getMappedValue(sAgency, sIdentifier, key, tAgency, tIdentifier);
message.setHeader("SenderService",SenderService);


return message;
}

return null;

}