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

def Message processData(Message message) {
	
	
	def map = message.getHeaders();
	def messageLog = messageLogFactory.getMessageLog(message);
	
	def docType = map.get("docType");
	def realmID = map.get("realmId");
	
	
	message.setHeader("DocumentType",docType);
	addCustomHeader(message,"Documenttype",docType);
	
	message.setHeader("realmID",realmID);
	addCustomHeader(message,"realmID",realmID);
	def xclientid = map.get("x-client-id");
		
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
                if(xclientid)
                    {
                        xcheck = (anid.contains(xclientid));
                        message.setHeader('xcheck', xcheck);
                        addCustomHeader(message,"xcheck",xcheck);
                    }
      
                if( xcheck == 'true' || anid == "ANID")
                {
                    if(service !=null)
                    {
			            address = service.getMappedValue("Ariba", "RealmID", realmID, "ECC", "Address");
      		            clientID = service.getMappedValue("Ariba", "RealmID", realmID, "ECC", "ClientID");  
      		            ccLocationID = service.getMappedValue("Ariba", "RealmID", realmID, "ECC", "CCLocationID");
      		            credentialName = service.getMappedValue("Ariba", "RealmID", realmID, "ECC", "Credentials");
                    }
                    
      		    }
      		    else
      		    {
      		        message.setHeader('xcheck', xcheck);
      		        throw new  Exception("Please maintain ANID in the Value Mapping correctly")
      		    }
      	        eccURL = address+"sap"+"/bc"+"/srt"+"/xip"+"/arba"+"/"+docType+"/"+clientID;
			   
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