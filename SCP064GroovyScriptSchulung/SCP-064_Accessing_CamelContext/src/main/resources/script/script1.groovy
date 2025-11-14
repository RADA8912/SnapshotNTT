import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.camel.CamelContext
import org.apache.camel.Exchange

def Message processData(Message message) {
    Exchange ex = message.exchange
    CamelContext ctx = ex.getContext()
    
    StringBuilder sb = new StringBuilder()
    sb << "CamelContext Name/ID: ${ctx.getName()}\r\n"
    sb << "CamelContext Version: ${ctx.getVersion()}\r\n"
    
    message.setBody(sb.toString())
    
return message
}