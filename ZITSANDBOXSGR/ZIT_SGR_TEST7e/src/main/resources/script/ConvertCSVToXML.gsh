import com.sap.gateway.ip.core.customdev.util.Message
import com.sap.it.api.ITApiFactory
import groovy.xml.MarkupBuilder
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord

def Message processData(Message message) {
    def body = message.getBody(String)
    
    // Define the CSV parser configuration
    def csvParser = CSVParser.parse(body, CSVFormat.DEFAULT.withHeader())

    // Parse CSV
    List<Map<String, String>> parsedData = []
    for (CSVRecord record : csvParser) {
        Map<String, String> data = [:]
        for (String header : csvParser.getHeaderMap().keySet()) {
            data[header] = record.get(header)
        }
        parsedData.add(data)
    }
    
    // Create the XML document using Markup Builder
    
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)

    // Create XML using MarkupBuilder
    xml.records {
        parsedData.each { record ->
            recordNode {
                record.each { key, value ->
                    "${key}"(value)
                }
            }
        }
    }

    def xmlString = writer.toString()

    // Set the XML document to the message body
    message.setBody(xmlString)

    return message
}