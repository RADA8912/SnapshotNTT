import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import javax.xml.xpath.*
import javax.xml.parsers.DocumentBuilderFactory

// set customerId, uid, email, groups, language (BP_PAYLOAD) as properties for B2BCustomer mapping and phone1, fax, cellPhone for Contact Person Address mapping
def Message processData(Message message) {
    	
	def messageLog = messageLogFactory.getMessageLog(message);
	def body = message.getBody(java.lang.String) as String;
	def properties = message.getProperties() as Map<String, Object>;
	
	
	String correspondenceLanguageCode = processXml(body, "//Language");
	message.setProperty("CorrespondenceLanguageCode", correspondenceLanguageCode);
		
	String defaultB2BUnit = processXml(body, "//BusinessPartnerInternalID");
	message.setProperty("DefaultB2BUnit", defaultB2BUnit);
    
    String internalId = processXml(body, "//BusinessPartnerRelationship/RelationshipBusinessPartnerInternalID");
    message.setProperty("InternalID", internalId);
    
    // B2BCustomer mapping
    String customerId = processXml(body, "//BusinessPartnerRelationship/ContactPerson/CustomerContactPerson/InternalID");
    message.setProperty(internalId + "_CustomerID", customerId);
    
    String uid = processXml(body, "//BusinessPartnerRelationship/ContactPerson/WorkplaceAddressInformation/Address/Email/URI");
    message.setProperty(internalId + "_uid", uid);
    
    String email = processXml(body, "//BusinessPartnerRelationship/ContactPerson/WorkplaceAddressInformation/Address/Email/URI");
    message.setProperty(internalId + "_email", email);
    
    String groups = processXml(body, "//BusinessPartnerRelationship/ContactPerson/BusinessPartnerFunctionTypeCode");
    message.setProperty(internalId + "_groups", groups);
    
    
    // Contact Person Address mapping
    // set telephone number and cellphone number as properties
    def root = new XmlSlurper().parseText(body);
    
    def phoneNumList = root.BusinessPartnerRelationshipSUITEBulkReplicateRequest.BusinessPartnerRelationshipSUITEReplicateRequestMessage.BusinessPartnerRelationship.ContactPerson.WorkplaceAddressInformation.Address.Telephone.collect{ it.MobilePhoneNumberIndicator.text()  + "|" + it.Number.SubscriberID.text() };
    message.setProperty(internalId + "_PhoneNumList", phoneNumList.join(","));
    
    String fax = processXml(body, "//BusinessPartnerRelationship/ContactPerson/WorkplaceAddressInformation/Address/Facsimile/Number/SubscriberID");
    message.setProperty(internalId + "_fax", fax);
    
    String BP_Person = message.getProperty("BP_PERSON");
    message.setBody(BP_Person);
    message.setProperty("BP_PERSON", '');
    
   return message;
}    

def processXml( String xml, String xpathQuery ) {
    
   def xpath = XPathFactory.newInstance().newXPath()
   def builder     = DocumentBuilderFactory.newInstance().newDocumentBuilder()
   def inputStream = new ByteArrayInputStream(xml.getBytes('UTF-8'))
   def records     = builder.parse(inputStream).documentElement
  
   xpath.evaluate( xpathQuery, records )
}