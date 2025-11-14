/*
 The integration developer needs to create the method processData 
 This method takes Message object of package com.sap.gateway.ip.core.customdev.util 
which includes helper methods useful for the content developer:
The methods available are:
    public java.lang.Object getBody()
	public void setBody(java.lang.Object exchangeBody)
    public java.util.Map<java.lang.String,java.lang.Object> getHeaders()
    public void setHeaders(java.util.Map<java.lang.String,java.lang.Object> exchangeHeaders)
    public void setHeader(java.lang.String name, java.lang.Object value)
    public java.util.Map<java.lang.String,java.lang.Object> getProperties()
    public void setProperties(java.util.Map<java.lang.String,java.lang.Object> exchangeProperties) 
    public void setProperty(java.lang.String name, java.lang.Object value)
    public java.util.List<com.sap.gateway.ip.core.customdev.util.SoapHeader> getSoapHeaders()
    public void setSoapHeaders(java.util.List<com.sap.gateway.ip.core.customdev.util.SoapHeader> soapHeaders) 
       public void clearSoapHeaders()
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