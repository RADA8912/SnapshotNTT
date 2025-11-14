// Below Script reads Message processing log XML and converts it to a JSON format accepted by BTP Alert Notification Service 
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

    //Initialising with default values
    eventType = "CPIIntegrationFlowDeploymentFailure"
    category = "NOTIFICATION"
    severity = "INFO"
    
     //Getting the current Date Time stamp and converting it to Unix format
    def date = new Date()
    def sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")  
    String unixTime = Date.parse('yyyy-MM-dd HH:mm:ssZ', sdf.format(date)).time
    eventTimestamp = unixTime.substring(0,10) // ANS Service accepts only 10 chars length
    
    def tags =[:]

    //Reading the mpl xml
    mplXML.depthFirst().find{ node -> (node.name() == 'IntegrationRuntimeArtifacts')}.each
    {       
        // Getting the count of error iflows in source XML and setting the alert body and subject based on that.
        body = "'" + it.depthFirst().findAll{ node -> (node.name() == 'Status' && node.text()=='ERROR')}.size() + "' iflow(s) deployed in ERROR status. Please check details below."
        subject = "Alert - CI - Integration Flow : Deployment Failure"

        resourceType = "INTEGRATION_FLOW"

        //Get Error Entries
        def error = []
        def failedIflows = []

        it.IntegrationRuntimeArtifact.findAll { it.Status.text() == 'ERROR' }.each { errorArtifact ->
            failedIflows.add(errorArtifact.Id?.text())

            def id = errorArtifact.Id?.text()
            def errorText = "'Deployed by - "+errorArtifact.DeployedBy?.text()+" on " +errorArtifact.DeployedOn?.text()
            tags[id] = errorText
        }
            resourceName = failedIflows.join(" , ")
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
<IntegrationRuntimeArtifacts>
<IntegrationRuntimeArtifact>
<Status>STARTED</Status>
<Type>INTEGRATION_FLOW</Type>
<Version>0.1</Version>
<DeployedBy>abc@sap.com</DeployedBy>
<Id>S4HANA_to_Commerce_Cloud_Post_AssortmentDeals_Material_Type</Id>
<DeployedOn>2023-09-21T06:45:50.226</DeployedOn>
<Name>S4HANA to Commerce Cloud Post Assortment Deals Material Type</Name>
</IntegrationRuntimeArtifact>
<IntegrationRuntimeArtifact>
<Status>STARTED</Status>
<Type>INTEGRATION_FLOW</Type>
<Version>1.0.0</Version>
<DeployedBy>abc@sap.com</DeployedBy>
<Id>S4HANA_to_Commerce_Cloud_Post_Assortment_Deals_Customer_Accounts</Id>
<DeployedOn>2023-09-19T06:15:51.892</DeployedOn>
<Name>S4HANA to Commerce Cloud Post Assortment Deals Customer Accounts</Name>
</IntegrationRuntimeArtifact>
</IntegrationRuntimeArtifacts>
*/


/* Target ANS specific JSON is in format - 
{
    "eventType": "CPIIntegrationFlowDeploymentFailure",
    "eventTimestamp": "1695789159",
    "category": "NOTIFICATION",
    "severity": "INFO",
    "subject": "Alert - CI Integration Flow : Deployment Failure",
    "resource": {
        "resourceType": "INTEGRATION_FLOW",
        "resourceName": "Utilities_ExceptionHandler , Common_InterfaceReport_Notification"
    },
    "tags": {
        "SUtilities_ExceptionHandler": "' Deployed by - abc@sap.com on 2023-09-07T14:23:16.144",
        "Common_InterfaceReport_Notification": "' Deployed by - abc@sap.com on 2023-08-20T07:36:17.916"
    },
    "body": "There were '2' iflows deployed in ERROR status. Please check details below."
}
*/