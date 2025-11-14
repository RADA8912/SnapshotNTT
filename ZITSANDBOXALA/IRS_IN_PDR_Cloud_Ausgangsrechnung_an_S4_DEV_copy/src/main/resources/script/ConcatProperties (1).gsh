import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
  
    def mapProperties = message.getProperties();
    def Z_prefix = mapProperties.get("z_prefix");
    def Z_konto = mapProperties.get("z_konto");
    
    message.setHeader("z_externalID",  Z_prefix + Z_konto);
    return message;
    }
