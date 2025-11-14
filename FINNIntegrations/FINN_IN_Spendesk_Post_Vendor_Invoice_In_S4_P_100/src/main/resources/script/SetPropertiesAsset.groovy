import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.*


def Message processData(Message incomingBody) {
    //Get the body for the XML Parsing
    def body = incomingBody.getBody(java.lang.String)
    //Parste the text as XML
    def list = new XmlSlurper().parseText(body) 

    //Get Asset Number if existent
    assetNumber = list.entry.content.properties.AssetNumber
    //Get FirstAcquisitionOn if existent
    firstAcquisitionOn = list.entry.content.properties.FirstAcquisitionOn
    //Get Company code
    companyCode = list.entry.content.properties.CompanyCode
    //Get Asset class
    assetClass = list.entry.content.properties.AssetClass
    

    if (assetNumber != ''){
        incomingBody.setProperty("Z_AssetNumber", assetNumber[0])
        incomingBody.setProperty("Z_CompanyCode", companyCode[0])
        incomingBody.setProperty("Z_AssetClass", assetClass[0])

        
    } else {
        incomingBody.setProperty("Z_AssetNumber", '')

    }
    //If firstAcquisitionOn is set, then asset has been capitalized
    if (firstAcquisitionOn != ''){
        incomingBody.setHeader("FirstAcquisitionOn", true)
    } else {
        incomingBody.setHeader("FirstAcquisitionOn", false)

    }
    

    incomingBody.setBody(incomingBody.getProperty("message"))
    return incomingBody;
}




