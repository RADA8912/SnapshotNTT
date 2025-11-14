import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import java.time.LocalDate
import java.time.format.DateTimeFormatter

def Message processData(Message message) {
    Reader reader = message.getBody(Reader)
    def input = new JsonSlurper().parse(reader)
    def builder = new JsonBuilder()

    builder.ShippingDetails {
        'Header' {
            'ID' input.Orders.Header.OrderID
            def orderDate = input.Orders.Header.OrderDate

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy", Locale.ENGLISH)
            LocalDate date = LocalDate.parse(orderDate, inputFormatter)
            String formattedDate = outputFormatter.format(date)

            'OrderDate' formattedDate
        }
        def items = input.Orders.Items.findAll {item -> item.Shippable}
        'Items' items.collect { item ->
            [
                    'EmployeeID' : item.EmployeeID.padLeft(3, '0'),
                    'ShipAddress':  item.ShipAddress,
                    'ShipCountry': item.ShipCountry,
                    'Freight': item.Freight,
                    'Shippable' : item.Shippable
            ]
        }
    }
    message.setBody(builder.toPrettyString())
    return message
}

