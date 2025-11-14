/*

 * The integration developer needs to create the method processData 

 * This method takes Message object of package com.sap.gateway.ip.core.customdev.util

 * which includes helper methods useful for the content developer:

 * 

 * The methods available are:

    public java.lang.Object getBody()

    

    //This method helps User to retrieve message body as specific type ( InputStream , String , byte[] ) - e.g. message.getBody(java.io.InputStream)

    public java.lang.Object getBody(java.lang.String fullyQualifiedClassName)



    public void setBody(java.lang.Object exchangeBody)



    public java.util.Map<java.lang.String,java.lang.Object> getHeaders()



    public void setHeaders(java.util.Map<java.lang.String,java.lang.Object> exchangeHeaders)



    public void setHeader(java.lang.String name, java.lang.Object value)



    public java.util.Map<java.lang.String,java.lang.Object> getProperties()



    public void setProperties(java.util.Map<java.lang.String,java.lang.Object> exchangeProperties) 



	public void setProperty(java.lang.String name, java.lang.Object value)

 * 

 */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.mapping.ValueMappingApi;

def Message processData(Message message) {
    
//Runtime
def helperValMap = ITApiFactory.getApi(ValueMappingApi.class, null)
def map = message.getProperties()
def keyIflow = map.get('IntegrationFlowName')
    
//Get Receiver Mail
def keyReceiver = helperValMap.getMappedValue('IntegrationFlowName', 'Receiver', keyIflow , 'Receiver', 'Mail')

//Set Propertie - Receiver Address 
message.setProperty("receiverMail", keyReceiver)

def body = message.getBody(String.class);

def currentDate = new Date()

def newCurrentDate = (currentDate.format("yyyy-MM-dd'T'HH:mm"))

def currentMinutes = (currentDate.format("mm"))

def currentHours = (currentDate.format("HH:mm"))

def Priority = message.getProperty("Priority")

//debugging
//message.setHeader("CurMinutes", currentMinutes)
//message.setHeader("CurHours", currentHours)
//message.setHeader("CurPriority", Priority)


if ((Priority == "2") &&  (currentMinutes.matches("(00|10|20|30|40|50).*"))){

	message.setHeader("Boolean", "true")

} else if ((Priority == "3") &&  (currentMinutes.matches("(00|30).*"))){

	message.setHeader("Boolean", "true")

} else if ((Priority == "4") &&  (currentHours.matches("(00:00|02:00|04:00|06:00|08:00|10:00|12:00|14:00|16:00|18:00|20:00|22:00).*"))){

	message.setHeader("Boolean", "true")

} else if ((Priority == "5") &&  (currentHours.matches("(00:00).*"))){

	message.setHeader("Boolean", "true")

} else {
 
    message.setHeader("Boolean", "false")
    
}

	return message;

}