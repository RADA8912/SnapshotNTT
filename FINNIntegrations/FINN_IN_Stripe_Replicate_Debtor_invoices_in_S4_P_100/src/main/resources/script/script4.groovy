import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.*
import groovy.xml.XmlUtil

def Message processData(Message message) {
    //Retrieve the body and parse the message to an XMLParser
    def body = message.getBody(Reader)
    def rootNode = new XmlParser().parse(body)
    
    def messageLog = messageLogFactory.getMessageLog(message)
    
    
    for(item in rootNode.IDOC.E1BPACTX09){
        
               if(messageLog != null){
            messageLog.addAttachmentAsString("stuff", item.ITEMNO_ACC.text(), "text/plain");
       
       
       } 
        
    }
    
        
            

       
     
       

            
            
            
 


    return message;
}