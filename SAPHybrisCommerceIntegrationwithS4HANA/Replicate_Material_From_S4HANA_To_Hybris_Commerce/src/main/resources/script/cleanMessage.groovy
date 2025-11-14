import com.sap.gateway.ip.core.customdev.util.Message;
def Message processData(Message message) {
    message.setBody("");
	return message;
}