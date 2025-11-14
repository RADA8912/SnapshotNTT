import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.xml.MarkupBuilder
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    
import java.lang.*;

def Message processData(Message message) {
    
    Reader reader = message.getBody(Reader)
    def input = new XmlSlurper().parse(reader)
    message.setHeader('SAP_ApplicationID', input.correlationId)
    
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'")
    LocalDateTime now = LocalDateTime.now()
    String time = dtf.format(now)  

    Writer writer = new StringWriter()
    def indentPrinter = new IndentPrinter(writer, '    ')
    def builder = new MarkupBuilder(indentPrinter)    

    builder.'ns0:SalesOrderRequest' ('xmlns:ns0':"http://test") {
        'MessageHeader' {
            'ReferenceID' input.MessageHeader.ReferenceID
            'CreationDateTime' time
            'SenderBusinessSystemID' input.MessageHeader.SenderBusinessSystemID
            'StandardID' input.MessageHeader.StandardID
        }
        def SalesOrderInp = input.SalesOrder
        'SalesOrder' {
            'SalesOrderID' input.SalesOrder.SalesOrderID                                                                 
            'InvoiceNumber' input.SalesOrder.InvoiceNumber             
            'ExternalDocumentID' input.SalesOrder.ExternalDocumentID
            'DistributionChannel' input.SalesOrder.DistributionChannel 
            'SalesOrderDate' input.SalesOrder.SalesOrderDate
            
        }
    }    
    message.setBody(writer.toString())
    return message
}