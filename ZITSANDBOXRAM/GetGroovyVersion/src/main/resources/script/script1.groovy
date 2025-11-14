import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message)

{
    def groovyVersion = GroovySystem.getVersion()
    def javaVersion = System.getProperty('java.version')
   
    message.setBody("Groovy Version: " +  groovyVersion +"\n Java Version: " + javaVersion)
    
        return message;
}
