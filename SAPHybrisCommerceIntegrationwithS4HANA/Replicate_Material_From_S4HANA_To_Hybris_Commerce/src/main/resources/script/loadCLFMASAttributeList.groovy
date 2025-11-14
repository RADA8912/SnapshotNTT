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
 */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    def clfmas_full_xml = message.getProperty("CLFMAS_XML") as java.lang.String;
    def	clfmas_full_text = new XmlSlurper().parseText(clfmas_full_xml);

    def clf_code = clfmas_full_text.IDOC.E1OCLFM.OBJEK_LONG !=''?clfmas_full_text.IDOC.E1OCLFM.OBJEK_LONG.text():clfmas_full_text.IDOC.E1OCLFM.OBJEK.text();
	def categoryType = clfmas_full_text.IDOC.E1OCLFM.KLART.text();
	def unique_attribute_name =   clfmas_full_text.IDOC.E1OCLFM.E1AUSPM.collect {it.ATNAM.text()} .unique();
    message.setProperty("PRODUCT_CODE", clf_code);
	//message.setBody(clfmas_full_xml);
    return message;
}
