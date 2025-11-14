import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.mapping.ValueMappingApi;
import groovy.util.XmlSlurper;

def Message process(Message msg) {
    //Runtime
    def helperValMap = ITApiFactory.getApi(ValueMappingApi.class, null)
    def map = msg.getProperties()
    
    def keyIflow = map.get('Name')
    
    //Get Receiver Mail
    def keyReceiver = helperValMap.getMappedValue('IntegrationFlowName', 'Receiver', keyIflow , 'Receiver', 'Mail')

    //Set Propertie - Receiver Address 
    msg.setProperty("receiverMail", keyReceiver)
    return msg
}

