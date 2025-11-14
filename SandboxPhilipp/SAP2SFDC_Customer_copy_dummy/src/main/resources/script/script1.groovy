package ST_SAP_Contact.src.main.resources.script

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import java.text.SimpleDateFormat


def Message processData(Message message) {


    //Access message body and properties
    Reader reader = message.getBody(Reader)
    Map properties = message.getProperties()

    //Define XML parser and builder
    def Contact = new XmlSlurper().parse(reader)
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)

    //build xml
    builder.toSFDC {
        'SAP_Gesch_ftspartner__c'(Contact.partnerNo.toString())
        'SAP_Gesch_ftspartnerrolle__c'(Contact.bpRole.toString())
        'LastName'(Contact.name1.toString())
    }

    //set body
    message.setBody(writer.toString())
    return message
}
