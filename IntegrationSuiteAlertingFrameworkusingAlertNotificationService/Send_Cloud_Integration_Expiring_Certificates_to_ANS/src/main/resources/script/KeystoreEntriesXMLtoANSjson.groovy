// Below Script reads Keystore XML and converts it to a JSON format accepted by BTP Alert Notification Service 
// Sample Source XML and Target JSON are mentioned as comments at end of script.

import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonBuilder
import java.text.SimpleDateFormat

    
    def Message processData(Message message) {
    //Source Body 
    Reader reader = message.getBody(Reader)

    // Define XML parser for parsing inbound message processing log XML
    def ksXML = new XmlSlurper().parse(reader)

     // BTP ANS format JSON
    def ansJSON = buildANSjson(ksXML)
    message.setBody(ansJSON.json);
    //Setting property to determine if an alert needs to be generated or not based on number of expiring/ expired entries
    message.setProperty("AlertRequired", ansJSON.expireAlert)
    return message
}

// Function to build ANS specific JSON by parsing source keystore XML
def buildANSjson(ksXML){

    //Fields required in ANS JSON
    def eventType, eventTimestamp, category, severity, subject, resourceType, 
    resourceName, tags_environment, tags_IntegrationFlowID, tags_IntegrationFlowPackageName, body

    //Initialising with default values
    eventType = "CPIKeystoreEntryExpiry"
    category = "NOTIFICATION"
    severity = "INFO"
    
    //Getting the current Date Time stamp and converting it to Unix format

    def date = new Date()
    def sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")  
    String unixTime = Date.parse('yyyy-MM-dd HH:mm:ssZ', sdf.format(date)).time
    eventTimestamp = unixTime.substring(0,10) // ANS Service accepts only 10 chars length

    // Variable to determine if an alert is required or not based on number of expiring or expired entries
    boolean expireAlert = false

    //Get Expired and Expiring Entries
    def expiredEnt = []
    def expiryingEnt = []

    // Initialize tags as an empty map
    def tags = [:]

    //Reading the keystore xml
    ksXML.depthFirst().find{ node -> (node.name() == 'KeystoreEntries')}.each
    {       
        //Fetching current date and date after 30 days to keystore entry date
        def currentDate = new Date().format("yyyy-MM-dd")
        def expireDate = new Date().plus(30).format("yyyy-MM-dd")
  


        it.KeystoreEntry.each{
            String validityEndDate =  it.ValidNotAfter
            validityEndDate = validityEndDate.substring(0,10)
            def endDate = new Date().format("yyyy-MM-dd")
            endDate = validityEndDate

            if(endDate > currentDate && endDate < expireDate)
            {
                expiryingEnt.add("Certificate Alias - '" + it.Alias + "' expiring on "+ validityEndDate)
            }
            if(endDate < currentDate)
            {
                expiredEnt.add("Certificate Alias - '" + it.Alias + "' expired on "+ validityEndDate)
            }
        }

        
        // Getting the count of error messages in source XML and setting the alert body and subject based on that.
        body = "There are in total '" + it.KeystoreEntry.size() + "' entries in the keystore on Cloud Integration. '" + expiryingEnt.size() + "' of them are expiring within the next 30 days, and '" + expiredEnt.size() + "' of them has/have expired. Please check details below."
        subject = "Alert - CI - Keystore Entry : Expiring (" + expiryingEnt.size() + ") / Expired ("+ expiredEnt.size() +") Certifcate"

        if (expiryingEnt.size() > 0) {
            tags.Expiring = expiryingEnt.join(" , ")
        }

        if (expiredEnt.size() > 0) {
            tags.Expired = expiredEnt.join(" , ")
        }
   
        if(expiryingEnt.size() > 0 || expiredEnt.size() > 0)
        {
            expireAlert = true
        }

        resourceType = "Certificate"
        resourceName = "Keystore Entry"

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

    // Create a map to store the JSON and a variable to determine if there are any expiring or expired entries
    def result = [
            json: builder.toPrettyString(),
            expireAlert: expireAlert 
    ]
      return result
}
 

/* Source XML is in format - 
<KeystoreEntries>
    <KeystoreEntry>
        <ValidNotBefore>2008-04-02T00:00:00.000</ValidNotBefore>
        <KeyType>RSA</KeyType>
        <Alias>sap_verisign universal root certification authority</Alias>
        <ValidNotAfter>2037-12-01T23:59:59.000</ValidNotAfter>
        <Hexalias>87987979</Hexalias>
    </KeystoreEntry>
    <KeystoreEntry>
        <ValidNotBefore>2000-05-12T18:46:00.000</ValidNotBefore>
        <KeyType>RSA</KeyType>
        <Alias>sap_baltimore cybertrust root</Alias>
        <ValidNotAfter>2021-05-12T23:59:00.000</ValidNotAfter>
        <Hexalias>45455</Hexalias>
    </KeystoreEntry>
    <KeystoreEntry>
        <ValidNotBefore>2013-08-01T12:00:00.000</ValidNotBefore>
        <KeyType>RSA</KeyType>
        <Alias>sap_digicert global root g2</Alias>
        <ValidNotAfter>2023-10-01T12:00:00.000</ValidNotAfter>
        <Hexalias>1233434343343</Hexalias>
    </KeystoreEntry>
</KeystoreEntries>
*/


/* Target ANS specific JSON is in format - 
{
    "eventType": "CPIIntegrationFlowExecutionFailure",
    "eventTimestamp": "1586337386",
    "category": "NOTIFICATION",
    "severity": "INFO",
    "subject": "Alert - CPI Integration Flow 'Concat_Mapping': Execution Failure",
    "resource": {
        "resourceType": "INTEGRATION_FLOW",
        "resourceName": "Concat_Mapping"
    },
    "tags": {
        "IntegrationFlowID": "Concat_Mapping",
        "IntegrationFlowPackageName": "ZPOC",
        "Error Message IDs": "<a href=\"https://abc.integrationsuite.cfapps.ap20.hana.ondemand.com:443/shell/monitoring/Messages/%7B%22identifier%22%3A%22AGUDDJBLK7bvjmGjethN-CNLE2zU%22%7D\">AGUDDJBLK7bvjmGjethN-CNLE2zU</a> , <a href=\"https://abc.integrationsuite.cfapps.ap20.hana.ondemand.com:443/shell/monitoring/Messages/%7B%22identifier%22%3A%22AGUDDTyWq0iEr73SggPZpPcm5yur%22%7D\">AGUDDTyWq0iEr73SggPZpPcm5yur</a> , <a href=\"https://development.integrationsuite.cfapps.ap20.hana.ondemand.com:443/shell/monitoring/Messages/%7B%22identifier%22%3A%22AGUDEMJ3qWRrMl3kuR74XlBTThcZ%22%7D\">AGUDEMJ3qWRrMl3kuR74XlBTThcZ</a>",
        "Cloud Integration Monitoring Link": "https://abc.integrationsuite.cfapps.ap20.hana.ondemand.com:443/shell/monitoring/",
        "Error Text": "com.sap.xi.mapping.camel.XiMappingException: Runtime exception during processing target field mapping /TargetFields/MaterialTypeFields/products/code. "
    },
    "body": "There were '3' failed messages for INTEGRATION_FLOW - 'Concat_Mapping'. Please check details below."
}
*/