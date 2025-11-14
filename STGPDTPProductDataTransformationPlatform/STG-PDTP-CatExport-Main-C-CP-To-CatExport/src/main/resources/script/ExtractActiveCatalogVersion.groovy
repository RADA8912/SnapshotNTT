import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

Message processData(Message message) {
    Map body = new JsonSlurper().parseText(message.getBody(String))
    message.setHeader('XActiveVersion', body.id)
    message.setHeader('XCatalogVersionCode', body.code)
    return message
}
