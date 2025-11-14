import com.sap.gateway.ip.core.customdev.util.Message;
import javax.xml.xpath.*;
import javax.xml.parsers.DocumentBuilderFactory;

def Message processData(Message message) {
    
    // IDoc
    def body = message.getBody();
   // def xml = new XmlSlurper().parseText(body);

    //Properties
    def properties = message.getProperties();
    def companyCodeWhiteList = properties.get("CompanyCode_WhiteList");
    
    def xpathQuery = "count(/ZCREMAS_ECC/IDOC/E1LFA1M/E1LFB1M/BUKRS[contains('," + companyCodeWhiteList + ",',concat(',',text(),','))]) > 0";

    def xpath = XPathFactory.newInstance().newXPath();
    def builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    def records = builder.parse(body).documentElement;
    
    def result = xpath.evaluate(xpathQuery, records);
    
    message.setProperty("DeliverIdocFlag", result);
    
    return message;
}