import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import org.apache.camel.CamelContext
import org.apache.camel.Exchange
import java.lang.management.*;
import groovy.xml.MarkupBuilder
def Message processData(Message message) {

    Exchange ex = message.exchange
	CamelContext ctx = ex.getContext()
   
   def variables = ["HC_HOST", "HC_HOST_SVC", "HC_HOST_CERT", "HC_REGION", "HC_ACCOUNT", "HC_APPLICATION", "HC_APPLICATION_URL", "HC_LOCAL_HTTP_PORT", "HC_LANDSCAPE", "HC_PROCESS_ID", "HC_AVAILABILITY_ZONE", "HC_OP_HTTP_PROXY_HOST", "HC_OP_HTTP_PROXY_PORT"]


  StringBuilder builder1 = new StringBuilder()
    builder1  << "Groovy: ${GroovySystem.getVersion()}\r\n"
	builder1 << "Java: ${System.getProperty('java.version')}\r\n"
    message.setBody(builder1.toString())

   def sw = new StringWriter()
   def builder = new MarkupBuilder(sw)
   builder.hcpvariables {
      variables.each { v -> 
         variable {
            name(v)
            value(System.getenv(v))
         }
      }
   }
message.setBody(sw.toString())
	
   return message

}