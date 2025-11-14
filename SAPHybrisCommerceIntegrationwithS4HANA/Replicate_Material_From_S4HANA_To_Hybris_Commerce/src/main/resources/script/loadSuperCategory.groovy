
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.it.api.mapping.*;
import groovy.xml.StreamingMarkupBuilder;
import groovy.xml.XmlUtil;
def Message processData(Message message) {
    def clfmas_full_xml = message.getBody(java.lang.String) as String;
    def matmas_full_xml = message.getProperty("MATMAS_XML") as String;
    message.setBody(matmas_full_xml);
    if(clfmas_full_xml == null || clfmas_full_xml == '' || !clfmas_full_xml.contains('CLFMAS02')){
       return message;
    }
	message.setProperty("CLFMAS_XML",clfmas_full_xml);
	return message;
}