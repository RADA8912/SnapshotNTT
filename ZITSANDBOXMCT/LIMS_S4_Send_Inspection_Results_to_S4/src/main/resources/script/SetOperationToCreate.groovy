import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.*

def Message processData(Message message) {
    def body = message.getBody(String)
    def xml = new XmlSlurper().parseText(body)
    xml.InspectionData.operation.replaceBody('CREATE')
    def newBody = XmlUtil.serialize(xml)
    message.setBody(newBody)
    return message
}