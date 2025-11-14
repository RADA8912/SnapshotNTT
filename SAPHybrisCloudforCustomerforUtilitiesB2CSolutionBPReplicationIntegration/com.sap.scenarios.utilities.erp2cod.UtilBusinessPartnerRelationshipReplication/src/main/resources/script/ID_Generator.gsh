import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.*;
import com.sap.it.api.mapping.*;

	def String generateID(String a) {
	def myID = "";
		def NO_OF_CHARS = 32;
		def CHARS = ('0'..'9');	
		def random = new Random();
			
			
			
			for(i in 1..NO_OF_CHARS){
			myID += CHARS[random.nextInt(CHARS.size())]
			}
			return myID;

	}