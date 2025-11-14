import com.sap.gateway.ip.core.customdev.util.Message;

import groovy.xml.MarkupBuilder
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

def Message processData(Message message) {

    def chrmas_full_xml = message.getBody(java.lang.String) as String;
    def chrmas_full_text = new XmlSlurper().parseText(chrmas_full_xml);
    
    chrmas_full_text.CHRMAS05.findAll { it.IDOC.E1CABNM.E1CAWNM.text() == '' }*.replaceNode{};
    message.setBody(XmlUtil.serialize(new StreamingMarkupBuilder().bind {mkp.yield chrmas_full_text} ));
    return message;
}