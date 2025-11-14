
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.MarkupBuilder
def Message processData(Message message) {

    // // Accessing message body that contains XML data
    Reader reader = message.getBody(Reader)
    def CommissionDetail = new XmlSlurper().parse(reader)

    // Filter line items that cointain integrationCommissionDetail_id
    def integrationCommissionDetail_id = CommissionDetail.'**'.findAll { it.name() == 'integrationCommissionDetail_id'}*.text()

    // Remove square brackets from array
    def formattedComissionDetail_id = integrationCommissionDetail_id.toString().replaceAll(/[\[\]']+/, '')

    // integrationCommissionDetail_id is passed to the property of the message
    message.setProperty("integrationCommissionDetail_id", formattedComissionDetail_id)
    return message;
}

