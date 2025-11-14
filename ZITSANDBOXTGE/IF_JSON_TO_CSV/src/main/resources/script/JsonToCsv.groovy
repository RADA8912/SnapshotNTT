import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter

def Message processData(Message message) {
    Reader reader = message.getBody(Reader)
    def input = new JsonSlurper().parse(reader)
    def items = input.Order.Items.findAll { it.Valid }
    Writer writer = new StringWriter()
    String[] headers = ['ItemNumber', 'ProductCode', 'Quantity']
    CSVFormat format = CSVFormat.DEFAULT.withHeader(headers)
    CSVPrinter csv = new CSVPrinter(writer, format)
    items.each { item ->
        csv.printRecord(
                item.ItemNumber.padLeft(3, '0'),
                item.MaterialNumber,
                item.Quantity
        )
    }
    message.setBody(writer.toString())
    return message
}