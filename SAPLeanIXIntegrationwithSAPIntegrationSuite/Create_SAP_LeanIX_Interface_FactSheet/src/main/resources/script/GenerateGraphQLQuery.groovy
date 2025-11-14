import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.*
import groovy.json.JsonOutput

def Message processData(Message message) {
    // UUID validation regex
    def uuidRegex = /^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$/

    // Extract properties from message
    def properties = message.getProperties()
    def interfaceId = properties.get("interfaceId")
    def interfaceName = properties.get("interfaceName")
    def sourceApplicationApiHubId = properties.get("sourceApplicationApiHubId")

    // Input XML as String
    def body = message.getBody(java.lang.String) as String

    // Remove XML header if it exists
    body = body.replaceFirst(/^<\?xml.*\?>/, "").trim()

    // Parse XML
    def xml = new XmlSlurper().parseText(body)

    // Extract unique TargetApplication IDs using a set to avoid duplicates
    def targetApplicationIds = xml.'**'.findAll { it.name() == 'ApiHubId' }*.text().toSet()

    // Prepare patches list
    def patches = [
        [
            "op": "add",
            "path": "/description",
            "value": "Interface created from SAP Integration Assessment."
        ],
        [
            "op": "add",
            "path": "/externalId",
            "value": "{\"type\":\"ExternalId\",\"externalId\":\"${interfaceId}\"}"
        ]
    ]

    // Add InterfaceToProviderApplication patch if ID exists and is valid UUID
    if (sourceApplicationApiHubId?.trim() && sourceApplicationApiHubId ==~ uuidRegex) {
        patches << [
            "op": "add",
            "path": "/relInterfaceToProviderApplication/new_1",
            "value": "{\"factSheetId\":\"${sourceApplicationApiHubId}\"}"
        ]
    }

    // Add TargetApplication patches if IDs exist and are valid UUIDs
    if (!targetApplicationIds.isEmpty()) {
        targetApplicationIds.eachWithIndex { id, index ->
            if (id?.trim() && id ==~ uuidRegex) {
                patches << [
                    "op": "add",
                    "path": "/relInterfaceToConsumerApplication/new_${index + 2}",
                    "value": "{\"factSheetId\":\"${id}\"}"
                ]
            }
        }
    }

    // Prepare JSON query
    def query = [
        "query": "mutation (\$input: BaseFactSheetInput!, \$patches: [Patch]!) { createFactSheet(input: \$input, patches: \$patches) { factSheet { id name type ... on Interface { externalId { externalId } alias } } }}",
        "variables": [
            "input": [
                "name": interfaceName,
                "type": "Interface"
            ],
            "patches": patches
        ]
    ]

    // Convert query to JSON string without pretty print
    def jsonOutput = JsonOutput.toJson(query)

    // Set JSON string as message body
    message.setBody(jsonOutput)
    return message
}
