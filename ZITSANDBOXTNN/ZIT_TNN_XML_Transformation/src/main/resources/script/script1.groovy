/**
 * Created by Timo Nguyen on 26/01/21.
 *
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

def Message processData(Message message) {

    Reader reader = message.getBody(Reader)
    // Parse Input XML into a document tree that may be traversed similar to XPath expressions
    def Orders = new XmlSlurper().parse(reader)
    def writer = new StringWriter()
    def myXML = new MarkupBuilder(writer)

    // generate xml tree with Orders as the root node
    myXML.Orders{

        // iterate through each order
        Orders.Order.each {
            myIteration ->
                myXML.ShippingDetails {
                    'Header'{
                        'ID' myIteration.OrderID.text()

                        // format the date from yyyy-MM-dd'T'HH:mm:ss.SSS to dd.MM.yyy
                        def orderDate =myIteration.OrderDate.text()
                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
                        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy", Locale.ENGLISH);
                        LocalDate date = LocalDate.parse(orderDate, inputFormatter);
                        String formattedDate = outputFormatter.format(date);

                        // assign formatted date to OrderDate
                        'OrderDate' formattedDate
                    }
                    'Body' {
                        'ShipAddress' myIteration.ShipAddress.text()
                        'ShipCity' myIteration.ShipCity.text().toUpperCase()
                        'ShipCountry' myIteration.ShipCountry.text().toLowerCase()
                        'ShipVia' myIteration.ShipVia.text().padLeft(3, '0')
                        'ShippingFreight' myIteration.Freight.text()
                    }
                }
        }
    }
    message.setBody(writer.toString())
    return message;
}

