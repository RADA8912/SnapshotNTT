import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.xml.MarkupBuilder
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

def Message processData(Message message) {
    
    Reader reader = message.getBody(Reader)
    def input = new XmlSlurper().parse(reader)
    message.setHeader('SAP_ApplicationID', input.correlationId)
    
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'")
    LocalDateTime now = LocalDateTime.now()
    String time = dtf.format(now)  

    Writer writer = new StringWriter()
    def indentPrinter = new IndentPrinter(writer, '    ')
    def builder = new MarkupBuilder(indentPrinter)
    
    builder.'ord:SalesOrderRequest' {
        'MessageHeader' {
            'ReferenceID' input.correlationId
            'CreationDateTime' time
            'SenderBusinessSystemID' input.source
        }
        def SalesOrderInp = input.SalesOrder
        'SalesOrder' {
            'SalesOrderID' input.orderNumber
            'SalesOrganization' input.branchNumber                     
            'ExternalDocumentID' String.valueOf(input.purchaseProcessId).toUpperCase()
            'DistributionChannel' input.source
            'SalesOrderDate' input.orderDate
            'TransactionCurrency' input.currency
            'OrganizationDivision' input.profitCenter
            'AssignmentReference' input.subscriptionNumber
            'ProcessType' input.processType
            'CountryOfShipping' input.countryOfShipping
            'Payment' {
                'PaymentServiceProvider' String.valueOf(input.payments.paymentServiceProvider).toUpperCase()
                'TransactionId' input.payments.transactionId
                'PaymentAmount' input.payments.amount  
                'Depositor' input.payments.depositor
                'IBAN' input.payments.IBAN 
                'Mandatereference' input.payments.mandateReference
                'NextDeliveryDate' input.payments.nextDeliveryDate            
            }
            'Partner' {
                'PartnerFunction' input.customer.customerNumberType
                'BusinessPartnerID' input.customer.customerNumber
                'DateOfBirth' input.customer.dateOfBirth
                'Address' {
                    'PhysicalAddress' {
                        'Title'
                        'Salutation' input.customer.address.salutation
                        'FirstName' input.customer.address.firstName
                        'LastName'  input.customer.address.lastName
                        'AddressAddition' ''
                        'StreetName' input.customer.address.street
                        'PostalCode' input.customer.address.postalCode
                        'CityName' input.customer.address.city
                        'Country' input.customer.address.country
                    }
                    'Communication' {
                        'CorrespondenceLanguage' input.customer.language
                        'EmailAddress'  input.customer.emailAddress
                    }
                }
                'ShippingAddress' {
                    'Title'
                    'Salutation' input.shippingAddress.salutation
                    'FirstName' input.shippingAddress.firstName
                    'LastName'  input.shippingAddress.lastName
                    'AddressAddition' ''
                    'StreetName' input.shippingAddress.street
                    'PostalCode' input.shippingAddress.postalCode
                    'CityName' input.shippingAddress.city
                    'Country' input.shippingAddress.country
                }
            }
            def items = input.items
            items.each { item ->
                'Item' {
                    'SalesOrderItemID' item.itemNumber
                    'ArticleId' item.articleKey.value
                    'SalesOrderItemCategory' item.articleKey.type
                    'SalesOrderItemText' item.name
                    'Quantity' item.quantity
                    'GrossPrice' item.price.gross
                    'ArticleDiscount' item.price.articleDiscount
                    'PositionDiscount' item.price.positionDiscount
                    'VatRate' item.vatRate
                }
            }
            'Shipping' {
                'ShippingCharge' input.shippingCharge.gross
                'CarrierName' input.shipment.carrier
                'CarrierService' input.shipment.service
            }
            'Invoice' {
                'InvoiceNumber' input.invoiceNumber
            }
            'Attachment' {
                'Type' "InvoicePDF"
            }
        }
    }
    message.setBody(writer.toString())
    return message
}