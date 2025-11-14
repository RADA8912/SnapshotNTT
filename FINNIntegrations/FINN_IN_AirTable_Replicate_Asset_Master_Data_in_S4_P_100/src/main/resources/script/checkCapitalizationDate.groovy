import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime

/**
* This script checks whether the asset needs to be capitalized. If so
* it sets a property accordingly to be used in the subsequent rooter
*
*/

def Message processData(Message incomingBody) {
    //Get the body for the XML Parsing
    def body = incomingBody.getBody(java.lang.String)
    //Parste the text as XML
    def list = new XmlSlurper().parseText(body) 
    //Get asset_capitalization_date if existent
    String asset_capitalization_date = list.el_sap_final_export_data_interface_asset_master_filtered.asset_capitalization_date
    
    
    //if asset_capitalization_date is existent. get the diff of the asset_capitalization_date_month and the current date
    if (asset_capitalization_date != ''){

        //get month and year of the capitalization date
        def cap_date_month = asset_capitalization_date.substring(5,7) as Integer
        def cap_date_year = asset_capitalization_date.substring(8) as Integer


        //get the current Date
        LocalDateTime now = LocalDateTime.now();
		//ge the current month
		int current_month = now.getMonthValue();
        
        //calcualte the diff of the of the capitalization month and the current_month
        date_diff_month = cap_date_month - current_month

        // if the asset_capitalization_date_month is in the prevoius month set property asset_capitalization = "true"
        // if diff = -1 the asset_capitalization_date_month OR If the diff = 11 the asset_capitalization_date_month is 12 and the current_month is 1 is in the past.
        if(date_diff_month == -1 || date_diff_month == 11){
            incomingBody.setProperty("asset_capitalization", "true")

        }else{
            incomingBody.setProperty("asset_capitalization", '')
        }

        
    } else {
        incomingBody.setProperty("asset_capitalization", '')
        

    }
    
    return incomingBody;
}