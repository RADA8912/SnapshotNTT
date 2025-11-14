import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.util.XmlParser
import groovy.util.XmlSlurper
import groovy.xml.XmlUtil

def Message processData(Message message) {

     try
       {
          
            def body = message.getBody(java.lang.String) as String;
            def IdocRoot = new XmlParser().parseText(body);
            def receiverPrn = IdocRoot.IDOC.EDI_DC40.RCVPRN.text();


            if(receiverPrn.equals("P40CLNT200")){
                IdocRoot.IDOC.EDI_DC40.RCVPRN[0].value = "PRODCPI";
                message.setBody(XmlUtil.serialize(IdocRoot))
            
            }else if(receiverPrn.contains("Q40")){
                IdocRoot.IDOC.EDI_DC40.RCVPRN[0].value = "DEVCPI";
                message.setBody(XmlUtil.serialize(IdocRoot))
                
            }else{
                throw new Exception("Error changing RCVPRN");
            }


       }
       catch(Exception e)
       {
           throw new RuntimeException(e);
             
       }
    
    return message;
}