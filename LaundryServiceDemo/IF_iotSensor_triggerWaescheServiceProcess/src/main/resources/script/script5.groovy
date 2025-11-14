import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*;


def Message processData(Message message) {
    
    //Get Body 
    def body = message.getBody(String.class);
    def jsonSlurper = new JsonSlurper();
    def list = jsonSlurper.parseText(body);

    def val=list.ID.toString();
    message.setHeader("bagId",val);

    def locVal=list.locationId.toString();
    message.setHeader("locationId",locVal);

    def orderDate = new Date().format( 'yyyyMMdd' );
    message.setHeader("orderDate", orderDate);
    
    def orderDate2 = new Date().format( "yyyy-MM-dd'T'HH:mm:ss.SSSZ" );
    message.setHeader("orderDate2", orderDate2);

     	def orderId = "";
		def NO_OF_CHARS = 4;
		def CHARS = ('0'..'9');	
		def random = new Random();
			
			for(i in 1..NO_OF_CHARS){
			orderId+= CHARS[random.nextInt(CHARS.size())]
			message.setHeader("orderId", orderId); 
	}

    return message;
}