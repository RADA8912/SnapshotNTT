import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.xml.*
import groovy.util.XmlSlurper

def Message processData(Message message) {

    def messageLog = messageLogFactory.getMessageLog(message);
      // XML-Daten aus dem Body der Nachricht abrufen
    def body = message.getBody(String)  // Body als String
   
    Reader reader = message.getBody(Reader)
    def rootNode = new XmlSlurper().parse(reader)
   
    //def xml = new XmlSlurper().parseText(body) // XML parsen
    def mestyp = rootNode.message.ORDERS05.IDOC.EDI_DC40.MESTYP.text()
    def belnr = rootNode.message.ORDERS05.IDOC.E1EDK01.BELNR.text()
    messageLog.addCustomHeaderProperty("BELNR", belnr);
    messageLog.addCustomHeaderProperty("MESTYP", mestyp);
    
    
    switch(mestyp){
        case "ORDRSP":
                message.setHeader("LinkedSAPObjectKey", belnr)
                message.setHeader("BusinessObjectTypeName", "BUS2032")
                message.setHeader("Slug", "TestCPIGroovy1.txt")
            break;
        default:
        ""
    }
    
    // Ergebnis in den Body zurückschreiben
    //message.setBody(output.toString())
    
  
    return message
/*
    def messageLog = messageLogFactory.getMessageLog(message)
   
    Reader reader = message.getBody(Reader)
    def Idoc = new XmlSlurper().parse(reader)
    def mestyp = Idoc.EDI_DC40.MESTYP.text()
   
    
    // Nachricht als XML-Dokument einlesen
    //def xml = new XmlSlurper().parseText(body)
    
    //def xml = new XmlSlurper().parse(body)
    // Namespace für das XML-Dokument
    //def ns = new Namespace("urn:sap-com:document:sap:idoc:soap:messages", "")
    
    // Zugriff auf den Wert des MESTYP-Feldes
    //def mestyp = xml.IDOC.EDI_DC40.MESTYP.text()
    //def idoc = xml.text()
    // Debugging-Information (optional)
   
    messageLog.addCustomHeaderProperty("MESTYP", mestyp);
    messageLog.addCustomHeaderProperty("Test", Idoc.text());
			
  
    
    // Den Wert des Feldes `MESTYP` für die weitere Verarbeitung setzen
    // message.setBody(mestyp)
    
    
    message.setHeader("LinkedSAPObjectKey", "0000000675")
    message.setHeader("BusinessObjectTypeName", "BUS2032")
    message.setHeader("Slug", "TestCPIGroovy.txt")
    
    
    
    def printNodes(node, indent = "") {
    println "${indent}Knoten: ${node.name()}"
    if (node.text()?.trim()) {
        println "${indent}  Wert: ${node.text().trim()}"
    }
    node.children().each { child ->
        printNodes(child, indent + "  ")
    }
}
    
    
    return message;
    */
}

//def ArchiveOrderResponse(String xml)
//{
  // messageLog.addCustomHeaderProperty("Test", xml.text());
   // def belnr = xml.message.ORDERS05.IDOC.E1EDK01.BELNR.text()
  
//}
