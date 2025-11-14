import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.*

/**
* Set the company code based on the asset class as property.
*
*
* */

def Message processData(Message incomingBody) {
    //Get the body for the XML Parsing
    def body = incomingBody.getBody(java.lang.String)
    //Parste the text as XML
    def list = new XmlSlurper().parseText(body) 
    //Get Asset Number if existent
    asset_class = list.el_sap_final_export_data_interface_asset_master_filtered.asset_class

    if (asset_class.equals('1004130 FINN: Vehicles owned - ABS')){
        incomingBody.setProperty("Z_COMPANY_CODE", 1030)
    } else {
        incomingBody.setProperty("Z_COMPANY_CODE", 1010)

    }
    return incomingBody;
}