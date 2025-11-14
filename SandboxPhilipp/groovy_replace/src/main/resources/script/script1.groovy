import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	def body = message.getBody(java.lang.String) as String
	body = body.replace("<storeOrdersResponse xmlns=\"http://dpd.com/common/service/types/ShipmentService/3.2\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">","<n0:storeOrdersResponse xmlns:n0=\"http://dpd.com/common/service/types/ShipmentService/3.2\">")
	
	body = body.replace("</storeOrdersResponse>","</n0:storeOrdersResponse>")
	
	message.setBody(body)
	return message
}