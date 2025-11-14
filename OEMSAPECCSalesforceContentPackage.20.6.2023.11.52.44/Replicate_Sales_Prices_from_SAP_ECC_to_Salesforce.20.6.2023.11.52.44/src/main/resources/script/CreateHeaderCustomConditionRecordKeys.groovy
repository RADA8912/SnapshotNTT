import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    //Body 
    def body = message.getBody(java.lang.String) as String
    def sapkeys = '' as String

    def xml = new XmlParser().parseText( body )
    xml.A_SlsPrcgConditionRecordType
    .each {
        r -> sapkeys = sapkeys + "'" + r.CustomConditionRecordKey[0].text() + "'"
    }

    //Properties 
    message.setHeader("querySAPKeys", sapkeys.replace("''", "','"));
    return message;
}