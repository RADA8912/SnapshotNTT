import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonBuilder
import java.util.Base64
import groovy.util.XmlSlurper
import java.io.Reader
import java.text.SimpleDateFormat

def Message processData(Message message) {

    def  body = message.getBody()

    

    def jsonObject = []


        jsonObject.add(body)

    def output = [
            serviceParameterList: jsonObject
    ]

    def builder = new JsonBuilder(output)
   // message.setBody(builder)

    message.setBody(builder.toPrettyString())

    return message
}
