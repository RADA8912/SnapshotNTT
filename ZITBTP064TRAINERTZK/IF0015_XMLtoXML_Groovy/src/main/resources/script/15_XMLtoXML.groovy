package btp064_Groovy_fuer_CPI

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder

import java.time.LocalDate
import java.time.format.DateTimeFormatter

def Message processData(Message message) {
    Reader reader = message.getBody(Reader)
    def Orders = new XmlSlurper().parse(reader)
    def writer = new StringWriter()
    def myXML = new MarkupBuilder(writer)

    myXML.Orders{

        Orders.Order.each {
            myIteration ->
                myXML.ShippingDetail {
                    'Header'{
                        'ID' myIteration.OrderID.text()

                        // format the date from yyyy-MM-dd'T'HH:mm:ss.SSS to dd.MM.yyy
                        def orderDate = myIteration.OrderDate.text()
                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
                        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy", Locale.ENGLISH)
                        LocalDate date = LocalDate.parse(orderDate, inputFormatter)
                        String formattedDate = outputFormatter.format(date)

                        // assign formatted date to OrderDate
                        'OrderDate' formattedDate
                    }
                    'Body'{
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
    return message
}

