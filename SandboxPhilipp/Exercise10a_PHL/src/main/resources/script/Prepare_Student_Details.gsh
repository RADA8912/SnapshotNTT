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
    xmlMarkup.Student_Details {
        if(id == 4){
            xmlMarkup.Id(id)
            xmlMarkup.Name("Peter")
            xmlMarkup.StudentClass(studentClass)
        }else{
            xmlMarkup.Id(id)
            xmlMarkup.Name(name)
            xmlMarkup.StudentClass(studentClass)
        }
    }

    def xmlString = xmlWriter.toString();
    
    message.setBody(xmlString);
    return message;
}