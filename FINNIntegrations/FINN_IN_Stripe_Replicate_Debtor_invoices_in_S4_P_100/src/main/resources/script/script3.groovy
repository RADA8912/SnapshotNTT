import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.text.DecimalFormat;
import groovy.xml.*
import groovy.xml.XmlUtil

def Message processData(Message message) {
    
    //Get the body for the XML Parsing
    def body = message.getBody(java.lang.String)
    
    // Message log
    def messageLog = messageLogFactory.getMessageLog(message);
     
    // get the split parameters 
    def splitIndex = message.getProperties().get('CamelSplitIndex');
    def splitComplete = message.getProperties().get('CamelSplitComplete');
    
    //Parste the text as XML
   def list = new XmlSlurper().parseText(body);
    
    
    def Double docTotal = 0;
    def Double itemtotal = 0;
    def Double offset_total = 0;
    def String addOffsetSegment;
    def String firstSplit;
    
    if (splitIndex == 0 && splitComplete == true ){
        // No split happend
        
        addOffsetSegment = 'false'
        message.setProperty("addOffsetSegment", addOffsetSegment);
        firstSplit = 'true';
        message.setProperty("firstSplit", firstSplit);
        
        docTotal = Double.parseDouble(list.debtor_transactions.amtDoccur.toString());
        message.setProperty("docTotal", docTotal);
        
    }else{
        
        
        for(item in list.debtor_transactions.item){
            
            // Count the Item Total + Tax
            def amtDoccurNet = Double.parseDouble(item.amtDoccurNet.toString());
            def amtDoccurTax = Double.parseDouble(item.amtDoccurTax.toString());
       
          itemtotal += amtDoccurNet + amtDoccurTax;
    
        }    
        
        // Rounding when the Zeros are exponential
        itemtotal = Math.round(itemtotal * 100);
        itemtotal = itemtotal/100;
        
        if (splitIndex == 0){
            // Split happened and this is the first split
            firstSplit = 'true';
            message.setProperty("firstSplit", firstSplit);
            addOffsetSegment = 'true'
            message.setProperty("addOffsetSegment", addOffsetSegment);
            docTotal = Double.parseDouble(list.debtor_transactions.amtDoccur.toString());
            
            
                // Calculate the offset
            offset_total = docTotal - itemtotal;      
            message.setProperty("offsetTotal", offset_total.toString());
            message.setProperty("docTotal", docTotal);
            
        }else{
            // Split happened and this is the subsequent splits
            firstSplit = 'false';
            message.setProperty("firstSplit", firstSplit);
            addOffsetSegment = 'false'
            message.setProperty("addOffsetSegment", addOffsetSegment);
            docTotal = itemtotal;
            message.setProperty("docTotal", docTotal);
        }
        
    }
    
    if (messageLog != null) {
        
     def log = 'firstSplit: ' + firstSplit + '\naddOffsetSegment: ' + addOffsetSegment + '\nSplitComplete: ' + splitComplete.toString() +  '\nSplit Index: ' + splitIndex.toString() +  '\nItem Total: ' + itemtotal + "\nDocument Total: " + docTotal + "\nOffest:" + offset_total;
     
     messageLog.addAttachmentAsString('Split Calculations', log, 'text/plain');
     
    }
    


    


 
        
    // if(offset_total != 0.00){
            
    //     // Only add the offset segment when required    
    //     message.setProperty("addOffsetSegment", 'true');
        
    //     if(splitIndex == 0){
    //         message.setProperty("firstSplit", 'true')
    //     } 

    //     if (messageLog != null) {
    //         def log = 'Item Total:' + itemtotal + "\n" + "Document Total:" + docTotal + "\n" + "Offest:" + offset_total;
    //         messageLog.addAttachmentAsString('Calcs', log, 'text/plain');
    //      }
 
    // }else{
    
    //     message.setProperty("addOffsetSegment", 'false');
            

    // }   
        
    


    return message;
}