/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/en-US/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/en-US/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.*



def Message processData(Message message) {
    /*
    Parse the start date of the next leasing rate and set the end date 
    of the old one to the previous day
    */
    def xml = new XmlSlurper().parseText(message.getBody(String));
    def pattern = "yyyy-MM-dd"
  
    def endDate = xml.valid_from.text()

    def dateInUnix = Date.parse(pattern, endDate).getTime()

    def newDateInUnix = dateInUnix - (24*60*60*1000)
    def date = new Date(newDateInUnix)
    def final_end_date = date.format("yyyy-MM-dd")

    message.setProperty("EndDate", final_end_date)
    
    return message
}