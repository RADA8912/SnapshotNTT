import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.util.*;
import java.util.Map.Entry;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.mapping.ValueMappingApi;
import com.sap.it.spi.ITApiHandler;
import com.sap.xi.mapping.camel.valmap.VMStore;
import com.sap.xi.mapping.camel.valmap.VMValidationException;
import com.sap.xi.mapping.camel.valmap.ValueMappingApiHandler;
import com.sap.it.api.exception.InvalidContextException;

def Message processData(Message message) 
{
	
	
	def map = message.getHeaders();
	def messageLog = messageLogFactory.getMessageLog(message);
	
	def docType = map.get("docType");
	def systemID = map.get("systemId");
	def realmID = map.get("realmID");
	def documentStandard = map.get("documentStandard");
	def xclientid = map.get("x-client-id");
	
	message.setHeader("Test1",xclientid);
	addCustomHeader(message,"Test1",xclientid);

	
	message.setHeader("DocumentType",docType);
	addCustomHeader(message,"Documenttype",docType);
	
	message.setHeader("SystemID",systemID);
	addCustomHeader(message,"SystemID",systemID);
	
	message.setHeader("SystemID",systemID);
	addCustomHeader(message,"SystemID",systemID);
	
	message.setHeader("DocumentStandard",documentStandard);
	addCustomHeader(message,"documentStandard",documentStandard);
	
	addCustomHeader(message,"RealmID",realmID);
	
	String eccURL=null;
	def address;
	def clientID;
	def ccLocationID;
	def credentialName;
	
			 try
			 {  
	
                def service = ITApiFactory.getApi(ValueMappingApi.class, null);
                def anid = service.getMappedValue("Ariba","ANID", 'AribaNetworkID', "ECC", "ANIdentifier");
            
                message.setHeader("anid", anid)
                addCustomHeader(message,"anid",anid);
                
                
                String xcheck = false;
                
                if(xclientid == null){
                    message.setHeader('itsNull', anid);
                    addCustomHeader(message,"ItsNull",anid);
                    
                }
            
                
                if(xclientid && anid)
                { 
                    xcheck = (anid.contains(xclientid));
                    message.setHeader('xcheck', xcheck);
                    addCustomHeader(message,"xcheck",xcheck);
                }
                if( xcheck == 'true' || anid == "ANID")
                {
                    if( service != null)
                    {
	      	   
				        address = service.getMappedValue("Ariba", "SystemID", systemID, "S4HANA", "Address");
				        clientID = service.getMappedValue("Ariba", "SystemID", systemID, "S4HANA", "ClientID");
				        ccLocationID = service.getMappedValue("Ariba", "SystemID", systemID, "S4HANA", "CCLocationID");
				        credentialName = service.getMappedValue("Ariba", "SystemID", systemID, "S4HANA", "Credentials");
      		        }
      		
      		        eccURL = address+"sap"+"/bc"+"/srt"+"/xip"+"/sap"+"/"+docType+"_in"+"/"+clientID+"/"+docType+"_in"+"/"+docType+"_in";
      		        
      		        if (docType == 'BusinessPartnerSUITEReplicateRequest')
      		        {
      		            eccURL = "https://6d9ac3d7-c05e-1ede-86c5-9327e372bff3.abap.canaryaws.hanavlab.ondemand.com/sap/bc/srt/scs_ext/sap/businesspartnersuitebulkreplic"
      		            credentialName = "S4HANA Credential Name"
      		        }
			    }
			    else
                {
                    throw new  Exception("Please maintain ANID in the Value Mapping correctly")
                }
			 }    
            catch(Exception e)
            {
                messageLog.setStringProperty("Exception",e.toString())
                return null;
            }
            message.setHeader("eccURL",eccURL);
	        addCustomHeader(message,"eccURL",eccURL);
     
            message.setHeader("ccLocationID",ccLocationID);
            message.setHeader("credentialName",credentialName);
      	
      //addCustomHeader(message,"ccLocationID",ccLocationID);
      //addCustomHeader(message,"credentialName",credentialName);
      
	        return message;
}


def addCustomHeader(Message message, String key, String val) {
	def messageLog = messageLogFactory.getMessageLog(message)	
	if (val != null) {
		messageLog.addCustomHeaderProperty(key, val);			
	}
}