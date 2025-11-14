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
                myXML.ShippingDetail {

                    'Header'{
                        'ID' myIteration.OrderID.text()
                        'OrderDate' toFormatDate("yyyy-MM-dd'T'HH:mm:ss.SSS","dd.MM.yyy", myIteration.OrderDate.text())
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
String toFormatDate(String input, String output, String dateToFormat ){
    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(input, Locale.ENGLISH);
    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(output, Locale.ENGLISH);
    LocalDate date = LocalDate.parse(dateToFormat, inputFormatter);
    String formattedDate = outputFormatter.format(date);
    return formattedDate
}