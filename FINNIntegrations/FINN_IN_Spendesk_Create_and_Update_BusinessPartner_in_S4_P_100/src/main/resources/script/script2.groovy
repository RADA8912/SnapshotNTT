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
import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import groovy.xml.*;

def Message processData(Message message) {
       def xml = message.getBody(String.class)
       
       
    	if (xml.contains("?xml")) {
    		// Remove xml header line
    		int i = xml.indexOf(">")
    		xml = xml.substring(i + 1)
    
    
    	}
       
       def completeXml= new XmlSlurper().parseText(xml)
       
       
       //Root elements
       def BusinessPartnerCategory = completeXml.BusinessPartnerCategory.text().toString()
       def BusinessPartnerGrouping = completeXml.BusinessPartnerGrouping.text()
       def FirstName = completeXml.FirstName.text()
       def Language = completeXml.Language.text()
       def CorrespondenceLanguage = completeXml.CorrespondenceLanguage.text()
       def LastName = completeXml.LastName.text()

       //AdressElements
       def Country = completeXml.to_BusinessPartnerAddress.results.Country.text()

       
       //Identificaton
       def BPIdentificationTypeHub = completeXml.to_BuPaIdentification.results.BPIdentificationType.text()
       def BPIdentificationNumberHub = completeXml.to_BuPaIdentification.results.BPIdentificationNumber.text()
       
       def BPIdentificationTypeSTR= completeXml.to_BuPaIdentification.results[1].BPIdentificationType.text()
       def BPIdentificationNumberSTR = completeXml.to_BuPaIdentification.results[1].BPIdentificationNumber.text()
       
       
       //Customer Details
       def CompanyCode = completeXml.to_Customer.to_CustomerCompany.results.CompanyCode.text()
       def ReconciliationAccount = completeXml.to_Customer.to_CustomerCompany.results.ReconciliationAccount.text()
       def RecordPaymentHistoryIndicator = completeXml.to_Customer.to_CustomerCompany.results.RecordPaymentHistoryIndicator.text()

       //PartnerRoleInformation
       def BusinessPartnerRole = completeXml.to_BusinessPartnerRole.results.BusinessPartnerRole.text()

        //Build the JSON
       def builder = new JsonBuilder()
    
   
        builder BusinessPartnerCategory: BusinessPartnerCategory,
          BusinessPartnerGrouping: BusinessPartnerGrouping,
          FirstName: FirstName,
          Language: Language,
          CorrespondenceLanguage: CorrespondenceLanguage,
          LastName: LastName
          to_BusinessPartnerAddress: {		
            results: [		
              	
                    Country: Country,
              
            ]
        }
        /***
          "to_BuPaIdentification" {
            "results" [
              {
        
                "BPIdentificationType" BPIdentificationTypeHub
                "BPIdentificationNumber" BPIdentificationNumberHub
               
              },
        	  {
        
                "BPIdentificationType" BPIdentificationTypeSTR
                "BPIdentificationNumber" BPIdentificationNumberSTR
               
              }
        	  
        	  ]
         }
         "to_Customer" {	
        
        	 "to_CustomerCompany" {
        		  "results" [			
        			{			
        			"CompanyCode" CompanyCode		
        			"ReconciliationAccount" ReconciliationAccount		
        			"RecordPaymentHistoryIndicator" RecordPaymentHistoryIndicator		
        					
        			}
        		]
        	 }
         }
        
          "to_BusinessPartnerRole" {
              "results" [
                 {
                "BusinessPartnerRole" BusinessPartnerRole
                 }
              ] 
          }
    
        
        }
      ***/

        
     

 
       
      message.setBody(builder.toString())
      
       
       return message
}



