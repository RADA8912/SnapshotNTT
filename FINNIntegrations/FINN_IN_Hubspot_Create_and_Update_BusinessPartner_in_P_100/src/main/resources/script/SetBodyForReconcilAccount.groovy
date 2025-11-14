import com.sap.gateway.ip.core.customdev.util.Message

import groovy.xml.XmlUtil

/**
 * This methods sets the fixed body for the Z_API to set the selection rule.
 * @param message
 * @return message
 */

def Message processData(Message message) {

    def body = message.getBody(Reader)
    def rootNode = new XmlParser().parse(body)


    input = rootNode.title.text()

    //Get BP Number from <title type="text">A_BusinessPartner('1013153')</title>
    String result = input.substring(input.indexOf("'")+1, input.length()-2);
    String reconclAcc = message.getProperties().get("ReconciliationAccount")
    //Set message Body for the selection rule addding
    String messageBody = '''{
    "ReconciliationAccount":"''' + reconclAcc + '''"\n}''' 
    message.setProperty("CustomerID", result)
    message.setBody(messageBody)
    message.setHeader("Content-Type", "application/json")

    return message

}