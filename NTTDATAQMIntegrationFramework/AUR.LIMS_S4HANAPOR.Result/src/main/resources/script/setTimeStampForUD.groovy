import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

Message processData(Message message) {
    
    Reader reader = message.getBody(Reader)    
    def json = new JsonSlurper().parse(reader)
    def changedDateTime = json?.d?.ChangedDateTime
    message.setProperty("timestamp", changedDateTime)

return message
}