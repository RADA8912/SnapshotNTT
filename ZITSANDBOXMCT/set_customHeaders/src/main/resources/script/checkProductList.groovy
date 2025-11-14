import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

Message processData(Message message) {
    // Body lesen und JSON parsen
    Reader reader = message.getBody(Reader)
    def input = new JsonSlurper().parse(reader)
    def productListStatus = input.ProductList.isEmpty()

    // Schleifenzähler aktualisieren
    def counter = message.getProperty("CustomHeaderCounter") as Integer
    if (counter == null) {
        counter = 1
    } else {
        counter += 1
    }
    message.setProperty("CustomHeaderCounter", counter)

    // Aktuellen Status als Property setzen
    message.setProperty("Productlist", productListStatus ? "empty" : "notEmpty")
    message.setProperty("LastProductlistIsEmpty", productListStatus)

    // Prüfen, ob es der letzte Schleifendurchlauf ist
    def isLastLoop = message.getProperty("IsLastLoop")?.toString()?.toLowerCase() == "true"

    def messageLog = messageLogFactory.getMessageLog(message)
    if (messageLog && isLastLoop) {
        // Nur im letzten Durchlauf setzen
        messageLog.addCustomHeaderProperty("NoOfLoops", counter.toString())
        messageLog.addCustomHeaderProperty("ProductlistIsEmpty", productListStatus.toString())
    }

    return message
}
