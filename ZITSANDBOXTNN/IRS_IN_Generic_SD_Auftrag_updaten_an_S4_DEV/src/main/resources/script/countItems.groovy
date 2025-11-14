import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.xml.*;

def Message processData(Message message)
{
Reader reader = message.getBody(Reader)
def rootNode = new XmlSlurper().parse(reader)

def sumItems = rootNode.Items.to_Item.size()

message.setProperty("sumItems",sumItems)

    return message
}

