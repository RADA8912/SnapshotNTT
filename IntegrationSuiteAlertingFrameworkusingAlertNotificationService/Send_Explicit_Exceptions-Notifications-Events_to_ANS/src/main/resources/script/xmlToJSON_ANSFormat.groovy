// Below Script reads explicit notification xml and converts it to a JSON format accepted by BTP Alert Notification Service 
// Sample Source XML and Target JSON are mentioned as comments at end of script.

import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonBuilder
import java.text.SimpleDateFormat
    
    def Message processData(Message message) {
    //Source Body 
    Reader reader = message.getBody(Reader)

    // Define XML parser for parsing inbound message processing log XML
    def mplXML = new XmlSlurper().parse(reader)

     // BTP ANS format JSON
    def ansJSON = buildANSjson(mplXML)
    message.setBody(ansJSON);
    return message
}

// Function to build ANS specific JSON by parsing source message processing log XML
def buildANSjson(mplXML)
{

    //Fields required in ANS JSON
    def eventType, eventTimestamp, category, severity, subject, resourceType,
    resourceName, tags_environment, tags_IntegrationFlowID, tags_IntegrationFlowPackageName, body
 
     //Getting the current Date Time stamp and converting it to Unix format
    def date = new Date()
    def sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")  
    String unixTime = Date.parse('yyyy-MM-dd HH:mm:ssZ', sdf.format(date)).time
    eventTimestamp = unixTime.substring(0,10) // ANS Service accepts only 10 chars length

    def tags = []
    //Reading the source xml
    mplXML.depthFirst().find{ node -> (node.name() == 'ExplicitAlert')}.each
    {       
        eventType = it.eventType?.text()
        category = it.category?.text()
        severity = it.severity?.text()
        resourceType = it.resourceType?.text()
        resourceName = it.resourceName?.text()


        body = "There was an explicit custom alert generated for  '" + resourceName + "'. Please check details below."
        subject = "Alert - CI - Integration Flow '"+ resourceName +"': Explicit Notification"

        // Collecting all the tags passed in source XML
       
        tags = it.tags.children().collectEntries { tag ->
            [(tag.name()): tag.text()]
        }


     }
    
      // JSON builder for generating output i.e. ANS json 
      def builder = new JsonBuilder(
            eventType: eventType,
            eventTimestamp: eventTimestamp,
            category: category,
            severity: severity,
            subject: subject,
            resource: [
                resourceType: resourceType,
                resourceName: resourceName
            ],
            tags: tags,
            body: body
        )
      
      return builder.toPrettyString()
}
 

/* Source XML is in format - 
<?xml version="1.0"?>
<ExplicitAlert>
<eventType>CPIIntegrationFlowExecutionFailure</eventType>
<category>NOTIFICATION</category>
<severity>INFO</severity>
<resourceType>INTEGRATION_FLOW</resourceType>
<resourceName>NR_APIM_to_CPI_Error_AlertToANS</resourceName>
<tags>
<IntegrationFlowID>NR_APIM_to_CPI_Error_AlertToANS</IntegrationFlowID>
<MessageID>AGUThSvb16qFVLRquyXEgYLEliDo</MessageID>
<HTTPresponsecode>405</HTTPresponsecode>
<Exception_Message>HTTP operation failed invoking https://acb-trial.integrationsuitetrial-apim.ap21.hana.ondemand.com:443/acb/errorresponse with statusCode: 405</Exception_Message>
<Error>org.apache.camel.component.ahc.AhcOperationFailedException: HTTP operation failed invoking https://acb-trial.integrationsuitetrial-apim.ap21.hana.ondemand.com:443/acb/errorresponse with statusCode: 405</Error>
</tags>
</ExplicitAlert>
*/


/* Target ANS specific JSON is in format - 
{
    "eventType": "CPIIntegrationFlowExecutionFailure",
    "eventTimestamp": "1695778884",
    "category": "NOTIFICATION",
    "severity": "INFO",
    "subject": "Alert - CPI Integration Flow 'NR_APIM_to_CPI_Error_AlertToANS': Explicit Notification",
    "resource": {
        "resourceType": "INTEGRATION_FLOW",
        "resourceName": "NR_APIM_to_CPI_Error_AlertToANS"
    },
    "tags": {
        "IntegrationFlowID": "NR_APIM_to_CPI_Error_AlertToANS",
        "MessageID": "AGUThSvb16qFVLRquyXEgYLEliDo",
        "HTTPresponsecode": "405",
        "Exception_Message": "HTTP operation failed invoking https://acb-trial.integrationsuitetrial-apim.ap21.hana.ondemand.com:443/acb/errorresponse with statusCode: 405",
        "Error": "org.apache.camel.component.ahc.AhcOperationFailedException: HTTP operation failed invoking https://acb-trial.integrationsuitetrial-apim.ap21.hana.ondemand.com:443/acb/errorresponse with statusCode: 405"
    },
    "body": "There was an explicit custom alert generated for  'NR_APIM_to_CPI_Error_AlertToANS'. Please check details below."
}
*/