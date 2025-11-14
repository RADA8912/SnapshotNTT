//Below Script takes source XMl and converts it into Alert Notification Service required JSON format.
import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonBuilder

def Message processData(Message message) {
  // Fetching Source XML Body
  def body = message.getBody(String);
  def rootNode = new XmlSlurper().parseText(body)
  //Creating ANS JSON Structure
  def jsonObject = [
    eventType: rootNode.eventType.text(),
    eventTimestamp: rootNode.eventTimestamp.text().toLong(),
    category: rootNode.category.text(),
    severity: rootNode.severity.text(), subject: rootNode.subject.text(),
    resource: [
      resourceType: rootNode.resource.resourceType.text(),
      resourceName: rootNode.resource.resourceName.text(),
      tags: [env: rootNode.resource.tags.env.text()]
    ],
    tags: [
      customtag1: rootNode.tags.customtag1.text(),
      customtag2: rootNode.tags.customtag2.text()
    ],
    body: rootNode.body.text()
  ]

  def json = new JsonBuilder(jsonObject).toPrettyString()
  //Setting Target ANS JSON Body
  message.setBody(json);

  return message;
}