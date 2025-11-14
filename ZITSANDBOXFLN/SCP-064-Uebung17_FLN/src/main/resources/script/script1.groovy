import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import java.time.LocalDate
import java.time.format.DateTimeFormatter

def Message processData(Message message) {

    // Accessing message body that contains JSON data
    Reader reader = message.getBody(Reader)
    def input = new JsonSlurper().parse(reader)

    // Instantiation of JsonBuilder for generation of the output
    def builder = new JsonBuilder()


    builder.ShippingDetails {
        // Header field is generated using direct assignment of the source field value
        'Header' {
            'ID' input.Orders.Header.OrderID

            def orderDate = input.Orders.Header.OrderDate

            // Converting data format
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy", Locale.ENGLISH)
            LocalDate date = LocalDate.parse(orderDate, inputFormatter)
            String formattedDate = outputFormatter.format(date)

            'OrderDate' formattedDate
        }

        // Filter line items where value equals to true
        def items = input.Orders.Items.findAll {item -> item.Shippable}

        // Iterate through the collection of filtered line items
        // Square brackets are used to represent the Items elements as an array
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

    // Output JSON data structure is passed to the body of the message
    message.setBody(builder.toPrettyString())
    return message
}

