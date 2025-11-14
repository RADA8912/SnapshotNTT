import com.sap.gateway.ip.core.customdev.util.Message;
import org.apache.camel.impl.*

def Message processData(Message message) {

        def camelContext = new DefaultCamelContext()
        StringBuilder versions = new StringBuilder()
        versions << "Groovy: ${GroovySystem.getVersion()}\n"
        versions << "Java: ${System.getProperty('java.version')}\n"
        versions << "Camel: ${camelContext.getVersion()}"
        message.setBody(versions.toString())
        return message

}