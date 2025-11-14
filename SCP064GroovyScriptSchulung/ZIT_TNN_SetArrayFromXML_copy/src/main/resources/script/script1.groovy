/*
<?xml version="1.0" encoding="UTF-8"?>
<CommissionDetail>
<action>update</action>
<correlationid>029e0e33-74ba-4070-89c8-6r2443545f267</correlationid>
<data>
<item>
<parameters>
<integrationCommissionDetail_id>strg1234</integrationCommissionDetail_id>
</parameters>
<attributes>
<PCKG_NO_LIMIT>0001551930</PCKG_NO_LIMIT>
</attributes>
</item>
<item>
<parameters>
<integrationCommissionDetail_id>strg4567</integrationCommissionDetail_id>
</parameters>
<attributes>
<PCKG_NO_LIMIT>0001551931</PCKG_NO_LIMIT>
</attributes>
</item>
</data>
<deploymenttype>null</deploymenttype>
<label>COMMISSION_DETAIL_INTEGRATION-UPDATE</label>
<lastupdateutc>1900-01-01 00:00:00</lastupdateutc>
<objectsource>commissions</objectsource>
<objecttarget>commission_details</objecttarget>
<systemsource>Sapr3</systemsource>
<systemtarget>Dyn365</systemtarget>
</CommissionDetail>
 */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.MarkupBuilder
def Message processData(Message message) {
    
    Reader reader = message.getBody(Reader)
    
    def CommissionDetail = new XmlSlurper().parse(reader)
    def integrationCommissionDetail_id = CommissionDetail.'**'.findAll { it.name() == 'integrationCommissionDetail_id'}*.text()
    def formattedComissionDetail_id = integrationCommissionDetail_id.toString().replaceAll(/[\[\]']+/, '')
    
    message.setProperty("integrationCommissionDetail_id", formattedComissionDetail_id)
       return message;
}