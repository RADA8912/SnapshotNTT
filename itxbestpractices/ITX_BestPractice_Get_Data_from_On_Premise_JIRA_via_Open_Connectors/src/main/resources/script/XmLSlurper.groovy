package de.itelli.tnn.groovy
import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import groovy.xml.XmlUtil
import java.util.HashMap

def Message processData(Message message){
    
    //Body
    def body = message.getBody(java.lang.String);
    def root = new XmlSlurper().parseText(body);

   def result = root.'**'.findAll {node -> node.name() == 'description'}*.text()
    message.setProperty("result", result);
    return message;
}

