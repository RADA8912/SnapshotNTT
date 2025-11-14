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
def buildANSjson(mplXML){

    //Fields required in ANS JSON
    def eventType, eventTimestamp, category, severity, subject, resourceType, 
    resourceName, tags_environment, tags_IntegrationFlowID, tags_IntegrationFlowPackageName, body

    //Initialising with default values
    eventType = "CPIIntegrationFlowExecutionFailure"
    category = "NOTIFICATION"
    severity = "INFO"
    
     //Getting the current Date Time stamp and converting it to Unix format
    def date = new Date()
    def sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")  
    String unixTime = Date.parse('yyyy-MM-dd HH:mm:ssZ', sdf.format(date)).time
    eventTimestamp = unixTime.substring(0,10) // ANS Service accepts only 10 chars length
    
    def tags = []

    //Reading the mpl xml
    mplXML.depthFirst().find{ node -> (node.name() == 'Record')}.each
    {       
        // Getting the count of error messages in source XML and setting the alert body and subject based on that.
        body = "'" + it.depthFirst().findAll{ node -> (node.name() == 'error')}.size() + "' failed message(s) found for " + it.Type.text() + " - '" + it.IntegrationFlowName.text() +"'. Please check details below."
        subject = "Alert - CI - Integration Flow '" + it.IntegrationFlowName.text() + "': Execution Failure"
           
        resourceType = it.Type?.text()
        resourceName = it.IntegrationFlowName?.text()
        
        tags = [IntegrationFlowID: it.IntegrationFlowName?.text(), IntegrationFlowPackageName: it.IntegrationFlowPackageName?.text() ]
      
    

        //Extracting the error weblinks for each message ID and adding to an arraylist
        
        it.depthFirst().findAll{ node -> (node.name() == 'error')}.each{

            // Adding Error Text - first 150 chars of error text only
            def errorText = it.ErrorText?.text()
            if(errorText.length()>100){
                errorText = errorText.substring(0, 100)
            }
            // Adding Message ID and respective weblink
            def msgIDlink = "'<a href=\""+ it.AlternateWebLink?.text() + "\">" + it.MessageGuid?.text() + "</a>'"
            tags["MessageID - " + msgIDlink] = errorText
         }


        // Adding Cloud Integration Monitoring Link
        def cloudIntLink = it.depthFirst().find{ node -> (node.name() == 'AlternateWebLink')}?.text()
        tags["Cloud Integration Monitoring Link"] = cloudIntLink.substring(0, cloudIntLink.lastIndexOf("Messages"))
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
<?xml version='1.0' encoding='UTF-8'?><MessageProcessingLog>
    <Record>
        <IntegrationFlowName>Concat_Mapping</IntegrationFlowName>
        <IntegrationFlowID>Concat_Mapping</IntegrationFlowID>
        <IntegrationFlowPackageName>ZPOC</IntegrationFlowPackageName>
        <Type>INTEGRATION_FLOW</Type>
        <error>
            <MessageGuid>AGUDDJBLK7bvjmGjethN-CNLE2zU</MessageGuid>
            <Status>FAILED</Status>
            <LogStart>2023-09-14T13:37:20.123</LogStart>
            <LogEnd>2023-09-14T13:37:20.158</LogEnd>
            <AlternateWebLink>https://abc.integrationsuite.cfapps.ap20.hana.ondemand.com:443/shell/monitoring/Messages/%7B%22identifier%22%3A%22AGUDDJBLK7bvjmGjethN-CNLE2zU%22%7D</AlternateWebLink>
            <OriginComponentName>CPI_development</OriginComponentName>
            <CorrelationId>AGUDDJAELjvvjMh4YRrZYSWPH-cg</CorrelationId>
            <ErrorText>com.sap.xi.mapping.camel.XiMappingException: Runtime exception during processing target field mapping /TargetFields/MaterialTypeFields/products/code. The root message is: Exception:[com.sap.aii.mappingtool.tf7.rt.BehaviorInvocationException: Function formatByExample: Queues have not equal number of values.] in class com.sap.aii.mappingtool.flib7.NodeFunctions method formatByExample[[Ljava.lang.String;@12cb47a8, [Ljava.lang.String;@74c62522, com.sap.aii.mappingtool.tf7.rt.ResultListImpl@14f5ad76, com.sap.aii.mappingtool.tf7.rt.Context@293c8872], cause: com.sap.aii.mappingtool.tf7.FunctionException: Function formatByExample: Queues have not equal number of values.</ErrorText>
        </error>
        <error>
            <MessageGuid>AGUDDTyWq0iEr73SggPZpPcm5yur</MessageGuid>
            <Status>FAILED</Status>
            <LogStart>2023-09-14T13:40:12.823</LogStart>
            <LogEnd>2023-09-14T13:40:12.858</LogEnd>
            <AlternateWebLink>https://abc.integrationsuite.cfapps.ap20.hana.ondemand.com:443/shell/monitoring/Messages/%7B%22identifier%22%3A%22AGUDDTyWq0iEr73SggPZpPcm5yur%22%7D</AlternateWebLink>
            <OriginComponentName>CPI_development</OriginComponentName>
            <CorrelationId>AGUDDTyCc7a4DL98A3mI1J_IRvbU</CorrelationId>
            <ErrorText>com.sap.xi.mapping.camel.XiMappingException: Runtime exception during processing target field mapping /TargetFields/MaterialTypeFields/products/code. The root message is: Exception:[com.sap.aii.mappingtool.tf7.rt.BehaviorInvocationException: Function formatByExample: Queues have not equal number of values.] in class com.sap.aii.mappingtool.flib7.NodeFunctions method formatByExample[[Ljava.lang.String;@4e94bd49, [Ljava.lang.String;@1784eec4, com.sap.aii.mappingtool.tf7.rt.ResultListImpl@3ca8a7b4, com.sap.aii.mappingtool.tf7.rt.Context@bcb0413], cause: com.sap.aii.mappingtool.tf7.FunctionException: Function formatByExample: Queues have not equal number of values.</ErrorText>
        </error>
        <error>
            <MessageGuid>AGUDEMJ3qWRrMl3kuR74XlBTThcZ</MessageGuid>
            <Status>FAILED</Status>
            <LogStart>2023-09-14T13:55:14.781</LogStart>
            <LogEnd>2023-09-14T13:55:14.821</LogEnd>
            <AlternateWebLink>https://abc.integrationsuite.cfapps.ap20.hana.ondemand.com:443/shell/monitoring/Messages/%7B%22identifier%22%3A%22AGUDEMJ3qWRrMl3kuR74XlBTThcZ%22%7D</AlternateWebLink>
            <OriginComponentName>CPI_development</OriginComponentName>
            <CorrelationId>AGUDEMKrUTTWtZptiYO9SCazHwIr</CorrelationId>
            <ErrorText>com.sap.xi.mapping.camel.XiMappingException: Runtime exception during processing target field mapping /TargetFields/MaterialTypeFields/products/code. The root message is: Exception:[com.sap.aii.mappingtool.tf7.rt.BehaviorInvocationException: Function formatByExample: Queues have not equal number of values.] in class com.sap.aii.mappingtool.flib7.NodeFunctions method formatByExample[[Ljava.lang.String;@2e3c2b1b, [Ljava.lang.String;@6edf6bd4, com.sap.aii.mappingtool.tf7.rt.ResultListImpl@4d23ad53, com.sap.aii.mappingtool.tf7.rt.Context@4007f9ca], cause: com.sap.aii.mappingtool.tf7.FunctionException: Function formatByExample: Queues have not equal number of values.</ErrorText>
        </error>
    </Record></MessageProcessingLog>
*/


/* Target ANS specific JSON is in format - 
{
    "eventType": "CPIIntegrationFlowExecutionFailure",
    "eventTimestamp": "1695261652",
    "category": "NOTIFICATION",
    "severity": "INFO",
    "subject": "Alert - CPI Integration Flow 'Venkat_Test_iFlow_ANS': Execution Failure",
    "resource": {
        "resourceType": "INTEGRATION_FLOW",
        "resourceName": "Venkat_Test_iFlow_ANS"
    },
    "tags": {
        "IntegrationFlowID": "Venkat_Test_iFlow_ANS",
        "IntegrationFlowPackageName": "ZPOC",
        "MessageID - '<a href=\"https://abc.integrationsuite.cfapps.ap20.hana.ondemand.com:443/shell/monitoring/Messages/%7B%22identifier%22%3A%22AGTkM1qgeX3TuUmw7zdaC0HG4AUM%22%7D\">AGTkM1qgeX3TuUmw7zdaC0HG4AUM</a>'": "org.apache.camel.CamelExecutionException: Exception occurred during execution on the exchange: Excha",
        "MessageID - '<a href=\"https://abc.integrationsuite.cfapps.ap20.hana.ondemand.com:443/shell/monitoring/Messages/%7B%22identifier%22%3A%22AGTkNCWSASIHT53ibsQ8DmRHEgcY%22%7D\">AGTkNCWSASIHT53ibsQ8DmRHEgcY</a>'": "org.apache.camel.CamelExecutionException: Exception occurred during execution on the exchange: Excha",
        "MessageID - '<a href=\"https://abc.integrationsuite.cfapps.ap20.hana.ondemand.com:443/shell/monitoring/Messages/%7B%22identifier%22%3A%22AGTkNdFAx2YfvEIPaGES-X-lOzLL%22%7D\">AGTkNdFAx2YfvEIPaGES-X-lOzLL</a>'": "org.apache.camel.CamelExecutionException: Exception occurred during execution on the exchange: Excha",
"Cloud Integration Monitoring Link": "https://abc.integrationsuite.cfapps.ap20.hana.ondemand.com:443/shell/monitoring/"
    },
    "body": "There were '14' failed messages for INTEGRATION_FLOW - 'Venkat_Test_iFlow_ANS'. Please check details below."
}
*/