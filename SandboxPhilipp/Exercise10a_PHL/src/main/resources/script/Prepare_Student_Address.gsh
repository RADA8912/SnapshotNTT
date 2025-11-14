import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.util.XmlSlurper;
import groovy.util.XmlParser;
import groovy.xml.MarkupBuilder;

def Message processData(Message message) {
    //Body 
    def body = message.getBody(java.lang.String) as String;
    def student = new XmlSlurper().parseText(body)

    def id=student.Id;
    def studentClass=student.StudentClass;
    def name=student.Name;
        
    def xmlWriter = new StringWriter();

    def xmlMarkup = new MarkupBuilder(xmlWriter);
    xmlMarkup.Student_Address {
        xmlMarkup.Id(id)
        if(id == 4){
            xmlMarkup.Country("Germany")
            xmlMarkup.State("Karnataka")
            xmlMarkup.City("Bangalore")
        } else{
            xmlMarkup.Country("India")
            xmlMarkup.State("Karnataka")
            xmlMarkup.City("Bangalore")
        }
    }

    def xmlString = xmlWriter.toString();
        
    message.setBody(xmlString);
    return message;
}