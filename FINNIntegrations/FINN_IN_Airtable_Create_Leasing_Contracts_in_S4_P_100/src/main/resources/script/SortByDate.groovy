import com.sap.gateway.ip.core.customdev.util.Message

import groovy.xml.XmlUtil

def Message processData(Message message) {

    def body = message.getBody(Reader)

    def rootNode = new XmlParser().parse(body)

    rootNode.el_sap_export_general_data.each { it.children().sort { it.valid_from.text() } }

    message.setBody( XmlUtil.serialize(rootNode))
    
    return message

}