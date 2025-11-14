import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import javax.xml.xpath.*
import javax.xml.parsers.DocumentBuilderFactory


// set telephone number and cellphone number as properties and clean BP_PAYLOAD property
def Message processData(Message message) {
    	
	def messageLog = messageLogFactory.getMessageLog(message);
	def body = message.getBody(java.lang.String) as String;
	def properties = message.getProperties() as Map<String, Object>;
    def root = new XmlSlurper().parseText(body);
    
    def phoneNumList = root.BusinessPartnerSUITEReplicateRequestMessage.BusinessPartner.AddressInformation.Address.Telephone.collect{ it.MobilePhoneNumberIndicator.text()  + "|" + it.Number.SubscriberID.text() };

    message.setProperty("PhoneNumList", phoneNumList.join(","));
    
    message.setProperty("BP_PAYLOAD", '');

   return message;
}