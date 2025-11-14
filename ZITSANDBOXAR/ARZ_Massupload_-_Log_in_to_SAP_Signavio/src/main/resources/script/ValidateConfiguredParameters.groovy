import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    def properties = message.getProperties()

    if (!properties.get("signavioCredential")) {
        throw new Exception("'Credential Name' is not configured.")
    }

    if (!properties.get("signavioTenantId")) {
        throw new Exception("'Signavio Workspace ID' is not configured.")
    }

    return message
}