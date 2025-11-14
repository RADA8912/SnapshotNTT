import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import javax.xml.xpath.*
import javax.xml.parsers.DocumentBuilderFactory

// set streetName, streetNumber, postalCode, town, country, region, fax, phone1, cellPhone, district, pobox, company for extra shipping address mapping
def Message processData(Message message) {
    	
	def messageLog = messageLogFactory.getMessageLog(message);
	def body = message.getBody(java.lang.String) as String;
	def properties = message.getProperties() as Map<String, Object>;
	
	String streetName = processXml(body, "//BusinessPartnerSUITEReplicateRequestMessage/BusinessPartner/AddressInformation/Address/PostalAddress/StreetName");
	message.setProperty("StreetName", streetName);
	
	String streetNumber = processXml(body, "//BusinessPartnerSUITEReplicateRequestMessage/BusinessPartner/AddressInformation/Address/PostalAddress/HouseID");
	message.setProperty("StreetNumber", streetNumber);
	
	String postalCode = processXml(body, "//BusinessPartnerSUITEReplicateRequestMessage/BusinessPartner/AddressInformation/Address/PostalAddress/StreetPostalCode");
	message.setProperty("PostalCode", postalCode);
	
	String town = processXml(body, "//BusinessPartnerSUITEReplicateRequestMessage/BusinessPartner/AddressInformation/Address/PostalAddress/CityName");
	message.setProperty("Town", town);
	
	String country = processXml(body, "//BusinessPartnerSUITEReplicateRequestMessage/BusinessPartner/AddressInformation/Address/PostalAddress/CountryCode");
	message.setProperty("Country", country);
	
	String region = processXml(body, "//BusinessPartnerSUITEReplicateRequestMessage/BusinessPartner/AddressInformation/Address/PostalAddress/RegionCode");
	message.setProperty("Region", region);
	
	String fax = processXml(body, "//BusinessPartnerSUITEReplicateRequestMessage/BusinessPartner/AddressInformation/Address/Facsimile/Number/SubscriberID");
	message.setProperty("Fax", fax);
	
	def root = new XmlSlurper().parseText(body);
    def phoneNumList = root.BusinessPartnerSUITEReplicateRequestMessage.BusinessPartner.AddressInformation.Address.Telephone.collect{ it.MobilePhoneNumberIndicator.text()  + "|" + it.Number.SubscriberID.text() };
    message.setProperty("PhoneNumList", phoneNumList.join(","));
	
	String district = processXml(body, "//BusinessPartnerSUITEReplicateRequestMessage/BusinessPartner/AddressInformation/Address/PostalAddress/DistrictName");
	message.setProperty("District", district);
	
	String pobox = processXml(body, "//BusinessPartnerSUITEReplicateRequestMessage/BusinessPartner/AddressInformation/Address/PostalAddress/POBoxID");
	message.setProperty("Pobox", pobox);
	
	String company = processXml(body, "//BusinessPartnerSUITEReplicateRequestMessage/BusinessPartner/Common/Organisation/Name/FirstLineName");
	message.setProperty("Company", company);
    
    
   return message;
}    

def processXml( String xml, String xpathQuery ) {
    
   def xpath = XPathFactory.newInstance().newXPath()
   def builder     = DocumentBuilderFactory.newInstance().newDocumentBuilder()
   def inputStream = new ByteArrayInputStream(xml.getBytes('UTF-8'))
   def records     = builder.parse(inputStream).documentElement
  
   xpath.evaluate( xpathQuery, records )
}