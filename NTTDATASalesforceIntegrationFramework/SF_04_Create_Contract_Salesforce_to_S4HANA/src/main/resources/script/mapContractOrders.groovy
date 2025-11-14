import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import java.text.SimpleDateFormat

def Message processData(Message message) {

    //Access message body and properties
    Reader reader = message.getBody(Reader)
    Map properties = message.getProperties()

    //Define XML parser and builder
    def Contract = new XmlSlurper().parse(reader)
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)

    //get the nested payload from XML
    //def payload = Contract.findAll()

    //build xml
    builder.ORDERS05 {

        'IDOC'(BEGIN: '1') {
            'EDI_DC40'(SEGMENT: '1') {
                'TABNAM'("EDI_DC40")
                'MANDT'("050")
                'DIRECT'("2")
                'IDOCTYP'("ORDERS05")
                'MESTYP'("REQOTE")
                'SNDPOR'("ZS_SFDC")
                'SNDPRT'("LS")
                'SNDPRN'("ZS_SFDC")
                'RCVPOR'(message.getProperty("RCVPRO"))
                'RCVPRT'("LS")
                'RCVPRN'("E10050")
            }
            'E1EDK01'(SEGMENT: '1') {
                ACTION('000')
                'ZTERM'(Contract.subscribeTopic.data.payload.PaymentTerms__c)
    
            }
            'E1EDK14'(SEGMENT: '1') {
                'QUALF'("018")
                'ORGID'("ZSKA")
            }
    
            'E1EDK14'(SEGMENT: '1') {
                'QUALF'("008")
                'ORGID'(Contract.subscribeTopic.data.payload.SAPSalesOrg__c)
            }
    
            'E1EDK14'(SEGMENT: '1') {
                'QUALF'("007")
                'ORGID'(Contract.subscribeTopic.data.payload.SAPSalesChannel__c)
            }
    
            'E1EDK14'(SEGMENT: '1') {
                'QUALF'("006")
                'ORGID'(Contract.subscribeTopic.data.payload.SAPDivision__c)
            }
    
            'E1EDK03'(SEGMENT: '1') {
                IDDAT('002')
                DATUM(Contract.subscribeTopic.data.payload.OrderDate__c)
    
            }
    
            'E1EDK03'(SEGMENT: '1') {
                IDDAT('006')
                DATUM(Contract.subscribeTopic.data.payload.ValidTo__c.toString().replaceAll('-',''))
    
            }
    
            'E1EDK03'(SEGMENT: '1') {
                IDDAT('014')
                DATUM(Contract.subscribeTopic.data.payload.ValidFrom__c.toString().replaceAll('-',''))
    
            }
    
            'E1EDKA1'(SEGMENT: '1') {
                PARVW('AG')
                PARTN(Contract.subscribeTopic.data.payload.SAPAccountNumber__c)
            }
    
            'E1EDK02'(SEGMENT: '1') {
                QUALF("001")
                BELNR(Contract.subscribeTopic.data.payload.CustomerOrderNo__c)
    
            }
    
            'E1EDK02'(SEGMENT: '1') {
                QUALF("ZTN")
                BELNR(Contract.subscribeTopic.data.payload.ContractNo__c)
    
            }
            
            'E1EDK02'(SEGMENT: '1') {
                QUALF("ZID")
                BELNR(Contract.subscribeTopic.data.payload.InvoiceDates__c)
    
            }
    
            'E1EDK02'(SEGMENT: '1') {
                QUALF("ZTX")
                ztxDescription = Contract.subscribeTopic.data.payload.Description__c.toString()
                 if (ztxDescription.length()>39){
                     BELNR(ztxDescription.substring(0,39))
                 } else {
                    BELNR(ztxDescription)
                 }
                 
            }
    
            'E1EDK02'(SEGMENT: '1') {
                QUALF("068")
                BELNR(Contract.subscribeTopic.data.payload.ContractNo__c)
    
            }
            
            'E1EDK17'(SEGMENT: '1') {
                QUALF("001")
                LKOND(Contract.subscribeTopic.data.payload.IncoTerms__c)
            }
            
            'E1EDK17'(SEGMENT: '1') {
                QUALF("002")
                LKTEXT(Contract.subscribeTopic.data.payload.IncoTerms2__c)
            }
            
            def shortDescription = new String()
            def Description = new String()
            
            'E1EDKT1'(SEGMENT: '1') {
                TDID("0018")
                Description = Contract.subscribeTopic.data.payload.Description__c.toString()
                if (Description.length()>70){
                    'E1EDKT2'(SEGMENT: '1') {
                    TDFORMAT("*")
                    shortDescription = Description.substring(0,69)
                    Description = Description.substring(70)
                    TDLINE(shortDescription)
                    }
                }
                while(Description.length()>70){
                    'E1EDKT2'(SEGMENT: '1') {
                        TDFORMAT("=")
                        shortDescription = Description.substring(0,69)
                        Description = Description.substring(70)
                        TDLINE(shortDescription)
                    }
                }
                'E1EDKT2'(SEGMENT: '1') {
                    TDFORMAT("=")
                    TDLINE(Description)
                }
            }
    
            Contract.QueryResult.records.eachWithIndex {
                OrderItem,
                index->
    
                'E1EDP01'(SEGMENT: '1') {
    
                    POSEX((index + 1) * 10)
                    MENGE(OrderItem.Quantity)
                    MENEE(OrderItem.QuantityUnitOfMeasure__c)
                    CURCY(OrderItem.CurrencyIsoCode)
                    if (OrderItem.UnitPrice.toString() == '0.0'){
                        PSTYV("AF03")
                    } else {
                        PSTYV("AF01")
                        
                        'E1EDP05'(SEGMENT: '1') {
                            ALCKZ('+')
                            if (Contract.subscribeTopic.data.payload.SAPSalesOrg__c.toString() == '1090'){
                               KSCHL('ZCEN')
                            }   else {
                               KSCHL('ZMKP') 
                            }
                           
                            KRATE(OrderItem.UnitPrice)
                            UPRBS('1')
                            MEAUN(OrderItem.QuantityUnitOfMeasure__c)
                            KOEIN(OrderItem.CurrencyIsoCode)
                        }
                    }
                    'E1EDP19'(SEGMENT: '1') {
                        QUALF('002')
                        IDTNR(OrderItem.ProductCode)
                    }
                }
            }
        }

    }

    //set body
    message.setBody(writer.toString())
    return message
}
