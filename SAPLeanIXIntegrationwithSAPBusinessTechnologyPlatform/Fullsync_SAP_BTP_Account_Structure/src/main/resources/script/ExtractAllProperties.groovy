import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

Message processData(Message message) {
    def body = message.getBody(String)
    def jsonSlurper = new JsonSlurper()
    def payload = jsonSlurper.parseText(body)
    
    payload.each { key, value ->
        message.setProperty("GlobalAccount_Level1_"+key, value)
    }
    
    return message
}
