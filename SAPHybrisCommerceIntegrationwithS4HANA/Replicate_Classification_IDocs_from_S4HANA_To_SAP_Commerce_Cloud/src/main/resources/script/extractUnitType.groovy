import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    def CHRMAS_XML = message.getBody(java.lang.String) as String;
    def unitType_XML = message.getProperty("UNIT_TYPE_XML") as String; 

    def full_unit_text = new XmlSlurper().parseText(unitType_XML);
    def charmas_text = new XmlSlurper().parseText(CHRMAS_XML);
    def unitCode = charmas_text.IDOC.E1CABNM.MSEHI;
 
    if(unitCode== null||unitCode =='')
        return message;
    def findMatchedUnitType = full_unit_text.message.Units.Unit.find{it.code.text()  == unitCode.text()};
    if(findMatchedUnitType != '')
        message.setProperty("UNIT_TYPE",findMatchedUnitType.unitType.text());

	return message;
}