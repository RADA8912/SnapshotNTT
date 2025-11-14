import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter
def Message processData(Message message) {
   /*Reader reader = message.getBody(Reader)
    def Order = new XmlSlurper().parse(reader)
    Writer writer = new StringWriter()
    def builder = new MarkupBuilder(writer)


    builder.Orders{
        Orders.Order.each {
            myIteration -> 
            builder.ShippingDetail {
                'Header' {
                    'ID' myIteration.OrderID.text()
                }
                'Body' {
                    'ShipAddress' myIteration.ShipAddress.text()
                }
            }
        }
    }
    
    message.setBody(writer.toString()) */
    return message
}