import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.*

Message processData(Message message) {
    def body = message.getBody(String)
    
    // Nur weitermachen, wenn Body nicht leer ist
    if (body == null || body.trim().isEmpty()) {
        message.setBody("Fehler: Response-Body war leer.")
        return message
    }

    def xml
    try {
        xml = new XmlSlurper().parseText(body)
    } catch (Exception e) {
        message.setBody("Fehler beim Parsen der SOAP-Antwort: ${e.message}")
        return message
    }

    def errorContextNode = xml.'**'.find { it.name() == 'ERROR_CONTEXT' }

    if (errorContextNode != null) {
        def errorXml = XmlUtil.serialize(errorContextNode)
        message.setBody(errorXml)
    } else {
        message.setBody("Kein <ERROR_CONTEXT> in der Antwort gefunden.")
    }

    return message
}