import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {

    def body = message.getBody(java.lang.String)
    body = body.replaceAll("<multimap:Messages xmlns:multimap=", "")
    body = body.replaceAll("\"http://sap.com/xi/XI/SplitAndMerge\">", "")
    body = body.replaceAll("<multimap:Message1>","")
    body = body.replaceAll("</multimap:Message1>", "")
    body = body.replaceAll("</multimap:Messages>", "")
    body = body.replaceAll("<?xml version='1.0' encoding='UTF-8'?>", "")
    message.setBody(body)
    return message;
}