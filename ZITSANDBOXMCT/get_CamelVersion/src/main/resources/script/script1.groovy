import com.sap.gateway.ip.core.customdev.util.Message
def Message processData(Message message) {
StringBuilder sb = new StringBuilder()
sb << "Groovy: ${GroovySystem.getVersion()}\r\n"
sb << "Java: ${System.getProperty('java.version')}\r\n"
message.setBody(sb.toString())
return message
}