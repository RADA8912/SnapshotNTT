import com.sap.gateway.ip.core.customdev.util.Message
import com.nttdatasolutions.edi.ShpmntToShipmentByItemToPack
import com.nttdatasolutions.edi.DelvryToShipmentByItemToPack

def Message processData(Message message) {
	//Property JavaMappingMode must be either "ShpmntToShipmentByItemToPack" or "DelvryToShipmentByItemToPack"
	def javaMappingMode = message.getProperty("JavaMappingMode")
	
	InputStream inputStream = new ByteArrayInputStream( message.getBody(String).getBytes( 'UTF-8' ) )
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream()

	switch(javaMappingMode) {
	case "ShpmntToShipmentByItemToPack":
		new ShpmntToShipmentByItemToPack().execute(inputStream, outputStream)
		break
	case "DelvryToShipmentByItemToPack":
		new DelvryToShipmentByItemToPack().execute(inputStream, outputStream)
		break
	default:
		throw new Exception("Property JavaMappingMode must be either \"ShpmntToShipmentByItemToPack\" or \"DelvryToShipmentByItemToPack\"")
		break
	}
	
	String resultString = new String(outputStream.toByteArray())
    message.setBody(resultString)
    return message
}
