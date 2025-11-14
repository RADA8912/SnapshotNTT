import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

Message processData(Message message) {

    Reader reader = message.getBody(Reader)
    def Order = new XmlSlurper().parse(reader)
    Writer writer = new StringWriter()
    def indentPrinter = new IndentPrinter(writer, ' ')
    def builder = new MarkupBuilder(indentPrinter)

    builder.PurchaseOrder {
        'Header' {
            'OrderID' Order.OrderNumber
            'Datum' LocalDate.parse(Order.Date.text(), DateTimeFormatter.ofPattern('yyyy/MM/dd')).format(DateTimeFormatter.ofPattern('yyyy-MM-dd'))
            'KundenID' Order.CustomerId
        }

        'Items' {


            Order.each { item ->
                'Item' {
                item.children().each { tag ->
                    'ID' tag.ItemId
                    'Menge' tag.Quantity
                    }

                }
            }
        }
    }

    message.setBody(writer.toString())

return message
}