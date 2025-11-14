/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/de/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/de/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    //Properties
    def properties = message.getProperties();
    value = properties.get("all_href");
    
    //set the first element of all_href as a seperate property
    message.setProperty("one_href", value[0]);
    
    //delete first element of all_href
    message.setProperty("all_href", value.drop(1));
    
    /*
    //reduce counter
    int counter = message.getProperty("all_href_counter")
    message.setProperty("all_href_counter", counter-1)
    */
    
    
    
    
    return message;
}