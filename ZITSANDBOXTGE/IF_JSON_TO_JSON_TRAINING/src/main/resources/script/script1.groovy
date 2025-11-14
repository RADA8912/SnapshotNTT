import com.sap.gateway.ip.core.customdev.util.Message
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder

def Message processData(Message message) {
   
   Reader reader = message.getBody(Reader)
    def input = new JsonSlurper().parse(reader)
    def builder = new JsonBuilder()
    
    builder.ShippingDetails {
        'Header' {
            'ID' input.Orders.Header.OrderID
            'OrderDate' LocalDate.parse(input.Orders.Header.OrderDate,
                DateTimeFormatter.ofPattern(("yyyy-MM-dd'T'HH:mm:ss.SSS"))).format(DateTimeFormatter.ofPattern('yyyy.MM.dd'))
        }
        def items = input.Orders.Items.findAll { item -> item.Shippable}
        'Items' items.collect { item ->
            [
                'EmployeeID' : item.EmployeeID.padLeft(3, '0'),
                'ShipAddress' : item.ShipAddress,
                'ShipCountry' : item.ShipCountry,
                'Freight' : item.Freight,
                'Shippable' : item.Shippable
            ]
        }
    }
    
    message.setBody(builder.toPrettyString()) 
    
    return message
}