import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.mapping.ValueMappingApi;
import groovy.util.XmlSlurper;

def Message processIDOC(Message msg) {
    //Runtime
	def runtimeAgency = 'ENVIRONMENT_IDOC';
    def srcAg = 'IDOC';
    def tgtAg = 'PROCESSDIRECT';
    def fieldSeparator = '#';
    def helperValMap = ITApiFactory.getApi(ValueMappingApi.class, null);
    
    //Get runtime parameters - IDOC
    //def controlSegmentFieldsStr = helperValMap.getMappedValue(runtimeAgency, 'PARAMETER', 'controlSegmentFields' , runtimeAgency, 'VALUE'); // Changed to Receivers perspective
    def controlSegmentFieldsStr = helperValMap.getMappedValue(runtimeAgency, 'PARAMETER', 'controlSegmentFields4Receiver' , runtimeAgency, 'VALUE');
    if (controlSegmentFieldsStr == null){ throw new Exception("NF: Control Segment Fields") }
    def controlSegmentFieldsArr = controlSegmentFieldsStr.tokenize(fieldSeparator);

    //Analyse Idoc Control Segment
    def body = msg.getBody();
    def xml  = new XmlSlurper().parseText(body);
    
    def keySender = [];
    controlSegmentFieldsArr.each{
        keySender.add(xml.IDOC.EDI_DC40."${it}".text());
    }
    keySender = keySender.join(fieldSeparator);
    
    //Get Receiver Address
    //def keyReceiver = helperValMap.getMappedValue(srcAg, 'SENDER', keySender , tgtAg, 'RECEIVER');// Changed to Receivers perspective
    def keyReceiver = helperValMap.getMappedValue(srcAg, 'SENDER', keySender , tgtAg, 'RECEIVER');
	if (keyReceiver == null){ throw new Exception("NF: keyReceiver Fields keysender ${keySender}") }
	
    //Set Propertie - Receiver Address 
    def map = msg.getProperties();
    msg.setProperty("receiverAddr", keyReceiver);
    return msg;
}
