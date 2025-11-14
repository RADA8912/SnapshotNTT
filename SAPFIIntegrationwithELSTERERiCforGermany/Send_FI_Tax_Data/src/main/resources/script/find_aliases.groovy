import com.sap.it.api.ITApiFactory;
import com.sap.it.api.keystore.KeystoreService;
import com.sap.it.api.keystore.exception.KeystoreException;
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    def map = message.getProperties();
    def value = map.get("ag_prefix");
    def encryptionalias;
    def signaturealias;
    if (value == null || value.isEmpty()) {
        throw new java.lang.IllegalArgumentException("Cannot find key alias in the message");
    } else {
        value = value.toLowerCase();
        encryptionalias = value + "_enc";
        signaturealias = value + "_sig";
    }
    message.setHeader("encryptionkey", encryptionalias);
    message.setHeader("signaturekey", signaturealias);
    // check if the aliases exist
    def service = ITApiFactory.getApi(KeystoreService.class, null);
    if( service != null) {
        if (service.getKey(encryptionalias) == null) {
            throw new java.lang.IllegalArgumentException("Cannot find a key \"" + encryptionalias + "\" in the keystore");
        }
        if (service.getKey(signaturealias) == null) {
            throw new java.lang.IllegalArgumentException("Cannot find a key \"" + signaturealias + "\" in the keystore");
        }
    } else {
        throw new java.lang.IllegalStateException("KeystoreService not found");
    }
    return message;
}