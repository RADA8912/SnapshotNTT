import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.*;
import com.sap.it.api.mapping.*;

	def String generateID(String a) {
	def myID = "";
		def NO_OF_CHARS = 4;
		def CHARS = ('0'..'9');	
		def random = new Random();
			
			
			
			for(i in 1..NO_OF_CHARS){
			myID += CHARS[random.nextInt(CHARS.size())];
			}
			return myID;

	}

public String capitalize(String s){
	return s.substring(0, 1).toUpperCase().concat(s.substring(1).toLowerCase());
}

public void mapAddressLine(String[] addressLines, Output output){
        for(String addressLine: addressLines){
                String[] values = addressLine.split(",");
                for(String value: values){
        	output.addValue(value);
                }
        }    
  
} 
public String getDeptCode(String s){
if((s.indexOf("N/A") == -1) & (s.indexOf("(") != -1) & (s.indexOf(")") != -1))
	return s.substring(s.indexOf('(')+1, s.indexOf(')'));
else
return s;
}