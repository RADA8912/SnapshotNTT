import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.net.URLDecoder;

def Message processData(Message message) {
    def qParm = message.getHeaders().get("CamelHttpQuery");
	def queryParams = qParm.split('&');
	def mapParams = queryParams.collectEntries { param -> param.split('=').collect { URLDecoder.decode(it) }};
	def id = mapParams['id'].toString();
	def version = mapParams['version'].toString();
	def mName = mapParams['mapName'].toString();
	if(version == "null" || version == "Draft")
		version = "1.0.0";
	message.setProperty("ID", id);
	message.setProperty("Version", version);
	message.setProperty("mName", mName);
	message.setHeader("CamelHttpQuery", "");
    return message;
}