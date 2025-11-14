import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import javax.xml.xpath.*
import javax.xml.parsers.DocumentBuilderFactory

// set customerId, uid, email, groups, language (BP_PAYLOAD) as properties for B2BCustomer mapping and phone1, fax, cellPhone for Contact Person Address mapping
def Message processData(Message message) {
    	
	def messageLog = messageLogFactory.getMessageLog(message);
	def body = message.getBody(java.lang.String) as String;
	def properties = message.getProperties() as Map<String, Object>;
	
	String BP = message.getProperty("BP_PAYLOAD");
	
	String correspondenceLanguageCode = processXml(BP, "//Language");
	message.setProperty("CorrespondenceLanguageCode", correspondenceLanguageCode);
	
	String BP_REL = message.getProperty("BP_REL_PAYLOAD");
	
	String defaultB2BUnit = processXml(BP_REL, "//BusinessPartnerInternalID");
	message.setProperty("DefaultB2BUnit", defaultB2BUnit);
    
    String internalId = processXml(BP_REL, "//BusinessPartnerRelationship/RelationshipBusinessPartnerInternalID");
    message.setProperty("InternalID", internalId);
    
    // B2BCustomer mapping
    String customerId = processXml(BP_REL, "//BusinessPartnerRelationship/ContactPerson/CustomerContactPerson/InternalID");
    message.setProperty(internalId + "_CustomerID", customerId);
    
    String uid = processXml(BP_REL, "//BusinessPartnerRelationship/ContactPerson/WorkplaceAddressInformation/Address/Email/URI");
    message.setProperty(internalId + "_uid", uid);
    
    String email = processXml(BP_REL, "//BusinessPartnerRelationship/ContactPerson/WorkplaceAddressInformation/Address/Email/URI");
    message.setProperty(internalId + "_email", email);
    
    String groups = processXml(BP_REL, "//BusinessPartnerRelationship/ContactPerson/BusinessPartnerFunctionTypeCode");
    message.setProperty(internalId + "_groups", groups);
    
    
    // Contact Person Address mapping
    // set telephone number and cellphone number as properties
    def root = new XmlSlurper().parseText(BP_REL);
    
    def phoneNumList = root.BusinessPartnerRelationshipSUITEReplicateRequestMessage.BusinessPartnerRelationship.ContactPerson.WorkplaceAddressInformation.Address.Telephone.collect{ it.MobilePhoneNumberIndicator.text()  + "|" + it.Number.SubscriberID.text() };
    message.setProperty(internalId + "_PhoneNumList", phoneNumList.join(","));
    
    String fax = processXml(BP_REL, "//BusinessPartnerRelationship/ContactPerson/WorkplaceAddressInformation/Address/Facsimile/Number/SubscriberID");
    message.setProperty(internalId + "_fax", fax);
    
    message.setProperty("BP_REL_PAYLOAD", '');
    
   return message;
}    

def processXml( String xml, String xpathQuery ) {
    
   def xpath = XPathFactory.newInstance().newXPath()
   def builder     = DocumentBuilderFactory.newInstance().newDocumentBuilder()
   def inputStream = new ByteArrayInputStream(xml.getBytes('UTF-8'))
   def records     = builder.parse(inputStream).documentElement
  
   xpath.evaluate( xpathQuery, records )
}