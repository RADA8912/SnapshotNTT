import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.camel.CamelException

Message processData(Message message) {
  def adapterType = message.getHeader('IFTTType', String)
  throw new CamelException("Adapter type ${adapterType} not supported. Please correct additional parameter in IFTT Automation Object.")
  return message
}