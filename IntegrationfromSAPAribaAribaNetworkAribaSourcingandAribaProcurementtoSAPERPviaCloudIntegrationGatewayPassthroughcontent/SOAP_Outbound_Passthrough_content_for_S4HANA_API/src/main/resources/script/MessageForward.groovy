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

	String eccURL=null;
	def cigAddress;
	def credentialName;

	 try{

            def service = ITApiFactory.getApi(ValueMappingApi.class, null);

            if( service != null)
            {
			 cigAddress = service.getMappedValue("S4HANA", "Solution", "IESOP", "Ariba", "Address");
			 cigCredential = service.getMappedValue("S4HANA","CIGAUTH","CIGAUTH","Ariba","Credentials");
	   		}

      		eccURL = cigAddress;
      		credentialName = cigCredential;

      }
      catch(Exception e)
      {
        messageLog.setStringProperty("Exception",e.toString())
        return null;
      }

      message.setHeader("eccURL",eccURL);
      message.setHeader("credentialName",credentialName);

	 //addCustomHeader(message,"eccURL",eccURL);


	return message;
}


def addCustomHeader(Message message, String key, String val) {
	def messageLog = messageLogFactory.getMessageLog(message)
	if (val != null) {
		messageLog.addCustomHeaderProperty(key, val);
	}
}
