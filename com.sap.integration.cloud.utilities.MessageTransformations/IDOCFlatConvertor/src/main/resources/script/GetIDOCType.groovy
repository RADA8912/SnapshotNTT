import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.util.slurpersupport.GPathResult;
import groovy.xml.XmlUtil;

def Message processData(Message message){
    
    String body = message.getBody(String);
    Map<String, Object> props = message.getProperties();
    String conversionType = props.get("idoc.conversionType") as String;
    String sapRelease = props.get("idoc.SAPRelease") as String;
    String APPREL = props.get("idoc.APPREL") as String;
    String CIMTYP = "";
    
    String idocType;
    switch(conversionType) {
        case "XML2Flat":
            GPathResult rootNode = new XmlSlurper().parseText(body);
            idocType = rootNode.IDOC[0].EDI_DC40.IDOCTYP.text().trim();
            if (rootNode.IDOC[0].EDI_DC40.CIMTYP != null)
                CIMTYP = rootNode.IDOC[0].EDI_DC40.CIMTYP.text().trim();
            break;
        case "Flat2XML":
            String[] lines = body.tokenize("\n");
            //idoc type position and length in the header line
            idocType = lines[0].substring(39, 69).trim();
            CIMTYP = lines[0].substring(69, 99).trim();
            break;
        default:
            throw new Exception("Error! idoc.conversionType \'" + conversionType + "\' is not supported.");
    }
    
    message.setProperty("idocType", idocType);
    message.setProperty("idoc.CIMTYP", CIMTYP);
    
    String idocKey = idocType;
    idocKey = sapRelease == "" || sapRelease == null ? idocKey : idocKey + "_" + sapRelease;
    idocKey = CIMTYP == "" || CIMTYP == null ? idocKey : idocKey + "_" + CIMTYP;
    idocKey = APPREL == "" || APPREL == null ? idocKey : idocKey + "_" + APPREL;
    
    message.setProperty("idocKey", idocKey);

    message.setBody("<root/>"); 
    return message;
}
