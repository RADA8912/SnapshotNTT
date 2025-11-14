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

    if (assetNumber != ''){
        incomingBody.setProperty("Z_AssetNumber", assetNumber[0])
    } else {
        incomingBody.setProperty("Z_AssetNumber", '')

    }
    
    if (firstAcquisitionOn != ''){
        incomingBody.setProperty("FirstAcquisitionOn", firstAcquisitionOn[0])
    } else {
        incomingBody.setProperty("FirstAcquisitionOn", '')

    }
    

    incomingBody.setBody(incomingBody.getProperty("message"))
    return incomingBody;
}




