import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import java.util.UUID

def Message processData(Message message) {
    def docnum = message.getProperties().get("idocDocnum")
    def uuid = UUID.randomUUID().toString()
    def entryId = "${docnum}_${uuid}"
    message.setProperty("entryId", entryId)
    return message;
}