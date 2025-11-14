import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.xml.MarkupBuilder
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    
import java.lang.*;

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
            'SenderBusinessSystemID' String.valueOf(input.source).toUpperCase()
            'StandardID' input.source
        }
        def SalesOrderInp = input.SalesOrder
        'SalesOrder' {
            'SalesOrderID' input.orderNumber                                  
            int branchNumberInt = input.branchNumber.toInteger()            
            'SalesOrganization' String.format("%04d", branchNumberInt)                  
            'ExternalDocumentID' String.valueOf(input.purchaseProcessId).toUpperCase()
            'DistributionChannel' String.valueOf(input.source).toUpperCase()
            'SalesOrderDate' input.orderDate
            'TransactionCurrency' input.currency
            'OrganizationDivision' input.profitCenter
            'AssignmentReference' input.subscriptionNumber
            'ProcessType' input.processType
            'CountryOfShipping' input.countryOfShipping
            'Payment' {
                'PaymentServiceProvider' String.valueOf(input.payments.paymentServiceProvider).toUpperCase()
                'TransactionId' input.payments.transactionId
                'Content' (input.payments.amount, currencyCode: 'EUR')                  
                'IBAN' input.payments.iban
                'BIC' input.payments.bic
                'SepaMandate' input.payments.mandateReference
                'SepaPayType' input.payments.mandateType
                'NextDelivery' input.payments.nextDeliveryDate
                'Depositor' {
                    'title' input.payments.depositor.title
                    'firstName' input.payments.depositor.firstName
                    'lastName' input.payments.depositor.lastName
                    'addressAddition' input.payments.depositor.addressAddition
                    'street' input.payments.depositor.street
                    'houseNr' input.payments.depositor.houseNr
                    'postalCode' input.payments.depositor.postalCode
                    'city' input.payments.depositor.city
                    'country' input.payments.depositor.country
                }           
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
                    'ArticleId' String.valueOf(item.articleKey.value).toUpperCase()
                    'SalesOrderItemCategory' String.valueOf(item.articleKey.type).toUpperCase()
                    'SalesOrderItemText' item.name
                    'Eye' item.eye
                    'Quantity' item.quantity
                    'GrossPrice' (item.price.gross, currencyCode: 'EUR')                    
                    'DiscountArticle' (item.price.articleDiscount, currencyCode: 'EUR')
                    'DiscountPosition' (item.price.positionDiscount, currencyCode: 'EUR')
                    'VatRate' item.vatRate                    
                }
            }
            'Shipping' {
                'ShippingCharge' (input.shippingCharge.gross, currencyCode: 'EUR')
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
    message.setProperty("SenderBusinessSystemID", String.valueOf(input.source).toUpperCase())
    message.setBody(writer.toString())
    return message
}