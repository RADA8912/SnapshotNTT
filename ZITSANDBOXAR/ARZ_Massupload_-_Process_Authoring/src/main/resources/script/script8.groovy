/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/de/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/de/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.JsonSlurper;


def Message processData(Message message) {
    
    //get Body
    def body = message.getBody(java.lang.String) as String
    
    //Properties
    def properties = message.getProperties();
    
    //jsonSlurper for further usage
    def jsonSlurper = new JsonSlurper()
    def object = jsonSlurper.parseText(body)
   
    //create array for hrefs
    def all_href_new = []
    
    //search for all hrefs
    int i = 0
    while(i < object.size){
        //if the rel == directory, then the href will be saved in an array
        if(object[i].rel == "dir"){
            all_href_new.add(object[i].href)
        }
        i++
    }
    
    //get old href
    def all_href_old = message.getProperty("all_href")
    
    //combine old with new href
    all_href_new += all_href_old
    message.setProperty("all_href",all_href_new)
    
    //set counter to new href length
    message.setProperty("all_href_counter",all_href_new.size)
    
    
    //create array for hrefs
    def all_models_new = []
    
    //search for all mods
    i = 0
    while(i < object.size){
        //if the rel == directory, then the href will be saved in an array
        if(object[i].rel == "mod"){
            all_models_new.add(object[i].href)
        }
        i++
    }
    
    //get all_models
    def all_models_old = message.getProperty("all_models")
    
    //combine old with new href
    all_models_new += all_models_old
    message.setProperty("all_models",all_models_new)
    
    //set all_models property with found all_models
    message.setProperty("all_models",all_models_new)
    
    
    return message;
}