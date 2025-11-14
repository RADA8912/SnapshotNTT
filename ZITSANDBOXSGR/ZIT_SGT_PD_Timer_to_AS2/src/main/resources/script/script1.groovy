import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.it.api.pd.PartnerDirectoryService;
import com.sap.it.api.ITApiFactory;

def Message processData(Message message) {
       def service = ITApiFactory.getApi(PartnerDirectoryService.class, null); 
       if (service == null){
          throw new IllegalStateException("Partner Directory Service not found");
       }
       def map = message.getProperties();
       def partnerId = map.get("PartnerId");
       if (partnerId == null){
          throw new IllegalStateException("Partner ID is not set in the property 'PartnerId'")      
       }
     def parameterValue = service.getParameter("ReceiverAddress", partnerId , String.class);
     if (parameterValue == null){
        throw new IllegalStateException("URL parameter not found in the Partner Directory for the partner ID "+partrnerId);      
     }
    
      message.setProperty("ReceiverAddress", parameterValue );
      
      //reading the Binary parameter
      message.setHeader("xsltmappingname","pd:"+partnerId+":xsltmapping:Binary");

       return message;
}