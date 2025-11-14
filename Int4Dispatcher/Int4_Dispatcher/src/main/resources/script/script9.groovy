import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {

    def headers = message.getHeaders();
    def host = headers.get("int4emhost");
    def xqos = headers.get("int4xqos");
    def contenttype = headers.get("int4ctype");
    def queue = headers.get("int4ampqq")
    message.setHeader("ifttdest", "https://" + host + "/messagingrest/v1/queues/" + queue + "/messages" );
    message.setHeader("x-qos", xqos);
    message.setHeader("content-type", contenttype);

    return message;
}