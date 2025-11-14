import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.*;

def Message processData(Message message) {
    
    //Body 
    def body = message.getBody(java.lang.String) as String;
    
    XmlParser parser = new XmlParser();
    def xml = parser.parseText(body);
    def salesOrders = xml.'**'.findAll({ it -> it.name() == 'A_SalesOrderType' });
    salesOrders.each({ ordertype ->
        def orderId = ordertype.SalesOrder.text();
        orderId = orderId.padLeft(10,'0');
        ordertype.SalesOrder[0].value = orderId;
        
        def salesOrderItems = ordertype.to_Item[0].children();
        salesOrderItems.each({ orderItem ->
            def orderItemId = orderItem.SalesOrderItem.text();
            orderItemId = orderItemId.padLeft(6,'0');
            
            orderItem.SalesOrderItem[0].value = orderId + orderItemId;
            orderItem.SalesOrder[0].value = orderId;
        })
        
    })
    
    message.setBody(XmlUtil.serialize(xml));
    
    return message;
}