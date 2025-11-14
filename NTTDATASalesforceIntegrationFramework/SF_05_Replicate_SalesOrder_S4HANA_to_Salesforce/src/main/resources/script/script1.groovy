import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import java.text.SimpleDateFormat
import java.util.HashMap;

def String trimZeroLeft(String value) {
    String output = ""
    if (value != null) {
        if (value.trim().length() == 0) {
            output = value
        } else {
            output = value.replaceAll("^0*", "")
            if (output.trim().length() == 0) {
                output = "0"
            }
        }
    } else {
        output = value
    }
    return output
}

Message processData(Message message) {

    Reader reader = message.getBody(Reader)
    Map properties = message.getProperties()

    def A_SalesOrder = new XmlSlurper().parse(reader)
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)
    def Where = new String()
    def WhereOrder = new String()
    def CountRejected = 0
    def CountItems = 0

        
    builder.sObjectsComposite {
        
        A_SalesOrder.A_SalesOrderType.each {
            A_SalesOrderType ->    
        
            if(A_SalesOrderType.SalesOrderType.toString() == "YST0" || A_SalesOrderType.SalesOrderType.toString() == "YSTA" || A_SalesOrderType.SalesOrderType.toString() == "YSTV" ){ //"YST0" || "YSTA" || "YSTV" || "YSKL" || "YSK0" || "YSRA" || "YSSA" || "YSRE"){
                OrderId = A_SalesOrderType.SalesOrder.toString()
                Where = Where + " or Order.SAP_SalesOrder_Ref__c = '"+OrderId+"'"
                WhereOrder = WhereOrder + " or SAP_SalesOrder_Ref__c = '"+OrderId+"'"
                CountRejected = 0
                CountItems = 0
                
                'compositeRequest' {
                    'method'("PATCH")
                    message.setProperty("OrderID", A_SalesOrderType.SalesOrder.toString())
                    'url'("/services/data/v52.0/sobjects/Order/SAP_SalesOrder_Ref__c/" + A_SalesOrderType.SalesOrder.toString())
                    'referenceId'("RefOrder_Draft_"+A_SalesOrderType.SalesOrder.toString())
                    'body' {
                        'RecordTypeId'(message.getProperty("RecordTypeId"))
                        'EffectiveDate'(A_SalesOrderType.CreationDate)
                        'Shipping_Date__c'(A_SalesOrderType.RequestedDeliveryDate)
                        'Status'("Draft")
                        switch (A_SalesOrderType.OverallDeliveryStatus.toString()) {
                            case "A":
                                'Delivery_Status__c'("Implementation")
                                break;
                            case "A":
                                'Delivery_Status__c'("Partly Delivered")
                                break;
                            case "A":
                                'Delivery_Status__c'("Completed")
                                break;
                            default:
                                break;
                        }
                        //'Description'("empty")
                        'Name'(A_SalesOrderType.SalesOrder)
                        //'OrderReferenceNumber'(A_SalesOrderType.SalesOrder)
                        'CurrencyIsoCode'(A_SalesOrderType.TransactionCurrency)
                        'Account'{
                            'Account_Number__c'(A_SalesOrderType.SoldToParty)
                        }
                        'Pricebook2Id'(message.getProperty("PriceBookId"))
                    }
                }
                A_SalesOrderType.to_Item.A_SalesOrderItemType.each {
                    A_SalesOrderItemType ->
                    CountItems = CountItems + 1
                    
                    if(A_SalesOrderItemType.SalesDocumentRjcnReason.toString() == ''){
                        'compositeRequest' {
                            'method'("PATCH")
                            'url'("/services/data/v52.0/sobjects/OrderItem/SAP_OrderItem_Ref__c/" + A_SalesOrderItemType.SalesOrderItem.toString())
                            'referenceId'("RefOrderItem_"+A_SalesOrderItemType.SalesOrderItem.toString())
                            'body' {
                                'Product_Seqeunce__c'
                                'Quantity'(A_SalesOrderItemType.RequestedQuantity)
                                'UnitPrice'(A_SalesOrderItemType.NetAmount.toDouble()/A_SalesOrderItemType.RequestedQuantity.toDouble())
                                'Description'(A_SalesOrderItemType.SalesOrderItemText)
                                'Order'{
                                    'SAP_SalesOrder_Ref__c'(A_SalesOrderType.SalesOrder)
                                }
                                'Product2'{
                                    'SAP_Material_Ref__c'("SAP_"+A_SalesOrderItemType.Material)
                                }
                                'Order_Quanity_ISO_Unit__c'(A_SalesOrderItemType.OrderQuantityISOUnit)
                                'PricebookEntry'{
                                    'SAP_PriceBookEntry_Ref__c'("SAP_"+A_SalesOrderItemType.Material.toString()+"_"+A_SalesOrderType.TransactionCurrency.toString()+"_Standard Price Book")
                                }
                            }
                        }
                    } else {
                        CountRejected = CountRejected + 1
                    }
                }
                if (CountRejected == CountItems){
                    'compositeRequest' {
                        'method'("DELETE")
                        'url'("/services/data/v52.0/sobjects/Order/SAP_SalesOrder_Ref__c/" + A_SalesOrderType.SalesOrder.toString())
                        'referenceId'("RefOrder_Delete_"+A_SalesOrderType.SalesOrder.toString())
                    }
                } else {
                    'compositeRequest' {
                        'method'("PATCH")
                        'url'("/services/data/v52.0/sobjects/Order/SAP_SalesOrder_Ref__c/" + A_SalesOrderType.SalesOrder.toString())
                        'referenceId'("RefOrder_Activated_"+A_SalesOrderType.SalesOrder.toString())
                        'body' {
                            'Status'("Activated")
                        }
                    }
                }

            }
        }
    }
    
    message.setBody(writer.toString())
    newBody = message.getBody()
    message.setProperty("SalesforceMessage", newBody)
    Where = Where.substring(4, Where.length())
    message.setProperty("QueryWhereOrderItem", Where)
    WhereOrder = WhereOrder.substring(4, WhereOrder.length())
    message.setProperty("QueryWhereOrder", WhereOrder)
    return message
}