import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.*
import groovy.json.JsonOutput

def Message processData(Message message) {
    def body = message.getBody(String)
    def xml = new XmlParser().parseText(body)

    def tags = []

    if (xml.value.labels.size() > 0) {
        xml.value.labels[0].children().each { labelNode ->
            def value = labelNode.text().trim()
            if (value) {
                tags.add([tagName: value])
            }
        }
    }
    else
    {
        return message
    }

    def graphqlVariable = [
        op   : "add",
        path : "/tags",
        value: JsonOutput.toJson(tags)
    ]

    def graphqlVariableJson = JsonOutput.toJson(graphqlVariable)
    message.setProperty("SubAccount_tags", "," +graphqlVariableJson) 
    return message
}
