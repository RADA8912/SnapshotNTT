import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {	
	def body = message.getBody(java.lang.String) as String

    def xml = new XmlSlurper().parseText(body)
    def hybrisItems = xml.'*'.findAll { it.@header != '' }

    String globalImpex = ""

    def tupleList = hybrisItems.collect { it ->
        def hybrisFields = it.'*'.findAll { it.@header != '' };

        String header = "INSERT_UPDATE " + it.@header
        header += hybrisFields.collect{ ";" + it.@header }.join()
        
        String row = hybrisFields.collect{ ";" + it }.join()
        
        new Tuple(header, row)
    }

    tupleList.inject([:]) { result, tuple ->
        String header = tuple[0]
        String row = tuple[1]
        
        if (result.containsKey(header)) {
            result[header] = result[header] << row
        } else {
            result[header] = [row]
        }
        
        result
    }.each { k, v ->
        String impex = k + "\n"
        impex += v.collect { it -> it + "\n" }.join()
        
        globalImpex += impex
    }
	
	message.setBody(globalImpex);

	return message;
}