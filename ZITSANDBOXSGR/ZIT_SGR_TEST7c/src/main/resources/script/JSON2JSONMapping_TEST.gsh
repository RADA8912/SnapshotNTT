import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

def Message processData(Message message) {
	String bodyNew = ''

	if (message.getBodySize() > 0) {
		// Get body
		Reader reader = message.getBody(java.io.Reader)
		def json = new JsonSlurper().parse(reader)

		List<Map<String,Object>> items = json.HL1.HL1Type.collect { item ->
		   [
			  Item1: item.Item1,
			  Item2: item.Item2,
			  ActualDeliveryQuantity: item.to_HL2.HL2Type.ActualDeliveryQuantity,
			  DeliveryQuantityUnit: item.to_HL2.HL2Type.DeliveryQuantityUnit,
			  HandlingUnitHeight: item.to_HL2.HL2Type.to_HL3.HL3Type.HandlingUnitHeight,
			  HandlingUnitLength: item.to_HL2.HL2Type.to_HL3.HL3Type.HandlingUnitLength,
			  Material: item.to_HL2.HL2Type.to_HL3.HL3Type.to_HL4.HL4Type.MATERIAL
		   ]      
		}

        bodyNew = JsonOutput.toJson(items)

		// Set new body
		message.setBody(bodyNew)

	}
	return message
}
