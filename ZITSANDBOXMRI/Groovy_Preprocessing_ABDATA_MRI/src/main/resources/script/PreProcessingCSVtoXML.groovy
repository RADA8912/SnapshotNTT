import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
    
    def Message processData(Message message) {
        // Get message body 
        def body = message.getBody(String);

        // Build XMl string from CSV input body. Open XML by adding <root><field> nodes. Replace CLRF (Carriage Return Line Feed)of the body with </field><field>. Close the XML string with </field></root> .
        def bodyCsvToXml = "<root><field>" + body.replaceAll(/\r\n/,"</field><field>") + "</field></root>"

        // Set message body 
        message.setBody(bodyCsvToXml);

        return message;
    }
    