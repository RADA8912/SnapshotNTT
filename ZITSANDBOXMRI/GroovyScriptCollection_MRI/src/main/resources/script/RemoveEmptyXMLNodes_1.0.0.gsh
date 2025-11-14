import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder

/**
* removeEmptyXMLNodes
* This Groovy script removes empty XML nodes.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {

        // Get the message body
        def body =  message.getBody(java.lang.String)
        
        // Define XMLParser
        def xml = new XmlParser().parseText(body)
        // Find the empty nodes from the xml payload
        def emptyNodes = xml.'**'.findAll{it.name() && !it.text()}
        
        // Iterate throught the nodes and remove the empty nodes
        emptyNodes.each{ node ->
            def parent = node.parent()
            parent.remove(node)
        }

        // Set the message body 
        message.setBody(groovy.xml.XmlUtil.serialize(xml))

    return message

}
