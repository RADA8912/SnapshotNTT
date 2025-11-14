/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/de/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/de/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.JsonSlurper;


def Message processData(Message message) {
    //Body
    def body = message.getBody(java.lang.String) as String
    
    def jsonSlurper = new JsonSlurper()
    def object = jsonSlurper.parseText(body)
    
    
    
    
    //Properties
    def properties = message.getProperties();
    message.setProperty("shared_documents_ID", object[0].href);
    
    return message;
}