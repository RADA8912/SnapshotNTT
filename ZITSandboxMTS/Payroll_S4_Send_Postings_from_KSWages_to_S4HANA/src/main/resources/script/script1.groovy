import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.io.File
import groovy.xml.MarkupBuilder

def Message processData(Message message) {
    //Body 
       def body = message.getBody(java.lang.String)as String;
	   
	   def varStringWriter = new StringWriter();
	   def varXMLBuilder   = new MarkupBuilder(varStringWriter);
	   
	   String newRecord ;
	   body.eachLine{
			line -> newRecord = line ;
			String newRecord1 = newRecord.substring(0).trim();


			varXMLBuilder.RecordSet{
				Record(newRecord1);
			}
		}
		def xml = varStringWriter.toString();
		xml="<RecordSetLines>"+xml+"</RecordSetLines>" ;

		message.setBody(xml);
        
       return message;
}