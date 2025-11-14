import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    Reader reader = message.getBody(Reader)
    def order = new XmlSlurper().parse(reader)

    //ArrayList
    def items = order.'**'.findAll{node -> node.name() == 'SalesOrderItem'}*.text()
    def count = items.size()
    println(count)
    message.setProperty("ItemNo", items[1])
    /*
    def lastItem = items.last()
    message.setProperty("lastItem", lastItem)
    */
    return message
}
