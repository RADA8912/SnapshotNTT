import com.sap.gateway.ip.core.customdev.util.Message;
def Message processData(Message message) {
    if(message.getBody(java.lang.String) == '')
        message.setBody("<NOTFOUNT/>")
	return message;
}
 