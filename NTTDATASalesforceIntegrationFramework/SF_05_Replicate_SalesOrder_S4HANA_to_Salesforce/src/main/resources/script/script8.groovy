import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import java.text.SimpleDateFormat
import java.util.HashMap;

Message processData(Message message) {

    Reader reader = message.getBody(Reader)
    Map properties = message.getProperties()

    def A_SalesOrder = new XmlSlurper().parse(reader)
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)

        
    builder.sObjectsComposite {
        
        A_SalesOrder.records.each {
            record ->    
                'compositeRequest' {
                    'method'("PATCH")
                    'url'("/services/data/v52.0/sobjects/Order/" + record.Id.toString())
                    'referenceId'("RefOrder_Dectivated_"+record.Id.toString())
                    'body' {
                        'Status'("Draft")
                    }
                }
        }
    }
    message.setBody(writer.toString())
    return message
}