import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.mapping.ValueMappingApi;
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message){

//Get Headers
def map = message.getHeaders();

def SAP_Sender= map.get("SAP_Sender");
def SAP_Receiver= map.get("SAP_Receiver");
def SAP_IDocType= map.get("SAP_IDocType");
def SAP_MessageType= map.get("SAP_MessageType");
       
//Set Value Mapping Parameters
def sAgency = "IDOC";
def sIdentifier = "SENDER";
def tAgency = "PROCESSDIRECT";
def tIdentifier = "RECEIVER";
def key = SAP_Sender+"|"+SAP_Receiver+"|"+SAP_MessageType+"|"+SAP_IDocType;

def service = ITApiFactory.getApi(ValueMappingApi.class, null);

if( service != null) {


def ReceiverFlowID=service.getMappedValue(sAgency, sIdentifier, key, tAgency, tIdentifier);
message.setHeader("ReceiverFlowID",ReceiverFlowID);


return message;
}

return null;

}