/**
 * This method splits BPNames that exceed the limit of 40 characters. The result is stored as a property
 * so that the message mapping can later on take the correct values. 
 * @param message
 * @return message
 */

import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    
       //Properties 
       map = message.getProperties();
       name = map.get("HubspotName");

    	if (name != null) {
    	
            int lengthInt = 40
            int lengthValue = name.length()

            //Split string into arrays

            if (lengthValue <= lengthInt){
                message.setProperty("OrganizationBPName1", name)
                message.setProperty("OrganizationBPName2", '')
            }

            else {
                elements = name.split("[\\s\\xA0]+");
                String output = ''
                int counter = 1
                int tmpLength = 0

                for (string in elements) {
                    tmpLength = tmpLength + string.length() + 1
                    println(tmpLength)

                    if (tmpLength < lengthInt){
                        output = output + string + " "
                        
                    }
                    else {
                        message.setProperty("OrganizationBPName" + counter.toString(), output)
                        counter = counter + 1
                        tmpLength = string.length()
                        output = string + " "

                    }
                    


                }
                message.setProperty("OrganizationBPName" + counter.toString(), output)

            }
           
            

        }
    

          
       return message;
}