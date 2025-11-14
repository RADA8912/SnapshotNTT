import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.mapping.ValueMappingApi;
import com.sap.it.api.mapping.ValueMappingApi;
import com.sap.it.spi.ITApiHandler;
import com.sap.xi.mapping.camel.valmap.VMStore;
import com.sap.xi.mapping.camel.valmap.VMValidationException;
import com.sap.xi.mapping.camel.valmap.ValueMappingApiHandler;
import com.sap.it.api.exception.InvalidContextException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.sap.it.api.*;
import com.sap.it.api.pd.*;
import java.lang.StringBuilder;
import com.sap.it.api.pd.BinaryData;
import java.lang.Exception;
import org.w3c.dom.Element
import org.w3c.dom.Node;
import java.io.Reader;
import com.sap.jdsr.passport.DSRPassport;
import com.sap.jdsr.passport.EncodeDecode;
import com.sap.jdsr.util.ConvertHelper;
import org.apache.commons.codec.DecoderException;
import java.security.MessageDigest;
import org.apache.commons.codec.binary.Hex;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;

def Message processData(Message message) {

def messageLog = messageLogFactory.getMessageLog(message);
def map = message.getHeaders();

//Read the exchange Headers
  // def anProductName = map.get("product");
   def anRealmId = map.get("anRealmId");
   def anLanguage = map.get("sap-language")
   def anAPIType = map.get("anAPIType");
   def anDocId = map.get("anDocId");
   def anDocType = map.get("anDocType");
   def anContentType = map.get("Content-Type");
   String anSenderID = map.get("anid");
   def anOperation = map.get("anOperation");
   def BusinessSystemID = map.get("anSystemId");
   def anIsAttachment = "false";
   message.setHeader("anOperation", anOperation)
   def xclientid = map.get("x-client-id");
   def service = ITApiFactory.getApi(ValueMappingApi.class, null);
   def anContinue;
   def anMetadataCall
   String address = null
String clientID = null
String ccLocationID = null
String credentialName = null

   
   
   message.setHeader("X-client-ID", xclientid);
   addCustomHeader(message,"X-client-ID", xclientid);
   
  def RepeatabilityRequestID = map.get("Repeatability-Request-ID");
  message.setHeader("Repeatability-Request-ID", RepeatabilityRequestID);
  addCustomHeader(message,"Repeatability-Request-ID", RepeatabilityRequestID);
  
  def RepeatabilityFirstSent = map.get("Repeatability-First-Sent");
  message.setHeader("Repeatability-First-Sent", RepeatabilityFirstSent);
  addCustomHeader(message,"Repeatability-First-Sent", RepeatabilityFirstSent);
  
  def IfMatch = map.get("If-Match");
  message.setHeader("If-Match", IfMatch);
  addCustomHeader(message,"If-Match", IfMatch);
  
  def RequestURI = map.get("Request-URI");
  message.setHeader("Request-URI", RequestURI);
  addCustomHeader(message,"Request-URI", RequestURI);
   
   def  anid = service.getMappedValue("Ariba","ANID", 'AribaNetworkID', "ECC", "ANIdentifier");
   
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
       anContinue = 'TRUE'; 
   }
   else
   {
       throw new  Exception("Please maintain ANID in the Value Mapping correctly")
   }
   // HTTP POST Operation is only supported in this call. Rest of the methods are not allowed and if received should fail the request and retrun proper HTTP Error Response code
   
   if (anContinue == 'TRUE')
    {
        def anCamelHTTPMethod = map.get("CamelHttpMethod")
        //addCustomHeader(message,"Inbound HTTPMethod", anCamelHTTPMethod);

        if (anCamelHTTPMethod != "POST") {
        message.setHeader("SAP_MessageType",anDocType);
       message.setHeader("SAP_Sender", anSenderID);
       message.setHeader("SAP_Receiver", anRealmId);
       message.setHeader("CamelHttpResponseCode","405")
       throw new IllegalStateException("Incoming HTTP Method"+anCamelHTTPMethod+"is not allowed");
   }

   // Check for any unwatned HTML Tags under PR Doc ID.
   if (anDocId)
   {
        Pattern ansearchHTMLPattern = Pattern.compile("\\<.*?\\>");
        Matcher anMatchHTMLTags = ansearchHTMLPattern.matcher(anDocId);
        boolean anInvalidDocID = anMatchHTMLTags.find();
        if (anInvalidDocID) {
           message.setHeader("SAP_MessageType",anDocType);
           message.setHeader("SAP_Sender", anSenderID);
           message.setHeader("SAP_Receiver", anRealmId);
           message.setHeader("anExceptionCode","CIG-PLT-00675")
           throw new IllegalStateException("Incoming Request might be missing mandatory HTTP Headers");
        }
   }

    message.setHeader("anTokenReady", "false")
    message.setHeader("anRequestReady", "false")
    message.setHeader("anBatchReady", "false")

       if (!anSenderID || !anRealmId)  {
       message.setHeader("anExceptionCode","CIG-PLT-00674")
       throw new IllegalStateException("Incoming Request might be missing mandatory HTTP Headers");
   }

    String[] anS4ResourceList = anAPIType.split('/')
   def anS4ResourceAPI = anS4ResourceList[0]
   def anS4ResourceType = anS4ResourceList[1]
    if (anAPIType.contains('batch'))  {
       message.setHeader("anPayloadContentType","multipart/mixed; boundary=batch")
       message.setProperty("payload", message.getBody(java.lang.String));
    }
    else if (anAPIType.contains('ATTACHMENT'))
    {
       anIsAttachment = "true"
       message.setHeader("anIsAttachment", anIsAttachment)
       addCustomHeader(message,"anIsAttachment", anIsAttachment);
       if (!anOperation) {
            message.setHeader("anExceptionCode", "CIG-PLT-00674");
		    throw new IllegalStateException("Incoming Request might be missing mandatory HTTP Headers");
       }
       message.setProperty("anOperation", anOperation)
       if (anOperation.equalsIgnoreCase('POST'))
        {
            message.setProperty("slug",map.get("Pslug"));
    	    message.setProperty("LinkedSAPObjectKey", map.get("PLinkedSAPObjectKey"));
    	    message.setProperty("BusinessObjectTypeName", map.get("PBusinessObjectTypeName"));
    	    message.setHeader("anPostAttachmentReady", "false")
        }
       else if (anOperation.equalsIgnoreCase('DELETE'))
       {
            message.setHeader("anDeleteAttachmentReady", "false")
	   }
    }
    else if (anAPIType.contains('metadata'))
    {
        anMetadataCall = 'True'
        message.setHeader("anMetadataCall", anMetadataCall)
        anDocId = anDocId+"_metadata"
    }
    else {
       message.setHeader("anPayloadContentType","application/json")
       message.setProperty("payload", message.getBody(java.lang.String));
    }


   //Set transactional headers for exchange
   message.setHeader("SAP_ApplicationID",anDocId);
   message.setHeader("SAP_MessageType",anDocType);
   message.setHeader("SAP_Sender", anSenderID);
   message.setHeader("SAP_Receiver", anRealmId);
   message.setHeader("anLanguage", anLanguage);
   //message.setHeader("anS4ResourceType",anS4ResourceType);

   try{

          // def service = ITApiFactory.getApi(ValueMappingApi.class, null);


            if( service != null)
            {
			 	  address = service.getMappedValue("Ariba", "SystemID", BusinessSystemID, "S4HANA", "Address");
      		 	  clientID = service.getMappedValue("Ariba", "SystemID", BusinessSystemID, "S4HANA", "ClientID");
      		 	  ccLocationID = service.getMappedValue("Ariba", "SystemID", BusinessSystemID, "S4HANA", "CCLocationID");
      		 	  credentialName = service.getMappedValue("Ariba", "SystemID", BusinessSystemID, "S4HANA", "Credentials");
      		}
   }
      		catch(Exception e)
      {
        messageLog.setStringProperty("Exception",e.toString())
        return null;
      }
   	    String anDestinationURL
   	    String anTokenURL
    	//String anTokenURL = address+"sap/opu/odata/sap/"+anS4ResourceAPI+"/?sap-language="+anLanguage+"&sap-client="+clientID
    	//String anTokenURL = address+"sap/opu/odata/sap/"+anS4ResourceAPI+"/?sap-client="+clientID+"&sap-language="+anLanguage
    	if (!RequestURI) {
    	    anTokenURL = address+"sap/opu/odata/sap/"+anS4ResourceAPI+"/?sap-client="+clientID
    	    anDestinationURL = address+"sap/opu/odata/sap/"+anAPIType+"?sap-client="+clientID+"&sap-language="+anLanguage;
    	}
    	else{
    	    String[] anS4ResourceList1 = RequestURI.split('/')
    	    String anS4ODATAPath = anS4ResourceList1[1] + "/" + anS4ResourceList1[2] + "/" + anS4ResourceList1[3] + "/" + anS4ResourceList1[4] + "/" + anS4ResourceList1[5] + "/" + anS4ResourceList1[6] +  "/" + anS4ResourceList1[7] + "/" + anS4ResourceList1[8] + "/" + anS4ResourceList1[9]
    	    anS4ResourceType = anS4ResourceList1[10];
    	    anTokenURL = address+ anS4ODATAPath +anS4ResourceAPI +"/?sap-client="+ clientID
    	    if (!anAPIType){
	            anAPIType = RequestURI;
	    }
	        anDestinationURL = address +anS4ODATAPath+"/"+ anS4ResourceList1[10]+"?sap-client="+clientID+"&sap-language="+anLanguage;
    	}
    	
    	if (anMetadataCall && anMetadataCall.equalsIgnoreCase("True"))
        {
            //anTokenURL = anTokenURL+"\$metadata"
         anTokenURL = anDestinationURL;
        }
	    anTokenURL = anTokenURL.trim();

	    //anDestinationURL = address+"sap/opu/odata/sap/"+anAPIType+"/?sap-language="+anLanguage+"&sap-client="+clientID;

	    //anDestinationURL = address+"sap/opu/odata/sap/"+anAPIType+"?sap-client="+clientID+"&sap-language="+anLanguage;
	    message.setHeader("anDestinationURL",anDestinationURL);
	    message.setHeader("ccLocationID",ccLocationID);
	    message.setHeader("credentialName",credentialName);
	    message.setHeader("anTokenURL",anTokenURL);
        message.setHeader("anS4ResourceType",anS4ResourceType); 
	        return message;

}
}
def addCustomHeader(Message message, String key, String val) {
	def messageLog = messageLogFactory.getMessageLog(message)
	if (val != null) {
		messageLog.addCustomHeaderProperty(key, val);
	}
}
   
