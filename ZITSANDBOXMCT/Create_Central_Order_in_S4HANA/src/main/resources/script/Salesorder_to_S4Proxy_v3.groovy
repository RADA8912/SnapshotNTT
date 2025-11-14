import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.xml.MarkupBuilder
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    
import java.lang.*;

def Message processData(Message message) {
    
    1Reader reader = message.getBody(Reader)
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
            'InvoiceNumber' input.invoiceNumber              
            'ExternalDocumentID' String.valueOf(input.purchaseProcessId).toUpperCase()            
            'DistributionChannel' String.valueOf(input.source).toUpperCase()            
            'SalesOrderDate' input.orderDate
            'TransactionCurrency' input.currency            
            'OrganizationDivision' input.profitCenter            
            'AssignmentReference' input.subscriptionNumber            
            'ProcessType' input.processType
            'ShippingCountry' input.countryOfShipping
            'Payment' {
                'PaymentMethod' input.payments.paymentMethod
                'NextDelivery' input.payments.nextDeliveryDate
                'OnlinePayment' {
                    'TransactionId' input.payments.onlinePayment.transactionId
                    'MerchantId' input.payments.onlinePayment.merchantId
                    'Amount' (input.payments.onlinePayment.amount, currencyCode: 'EUR')
                }
                'InstallPlan' {
                    'TotalAmount' input.payments.installmentPlan.totalAmount,currencyCode:'EUR'
                
                'InterestsKey' input.payments.installmentPlan.interestsKey
                'DateOfStart' input.payments.installmentPlan.dateOfStart
                'Type' String.valueOf(input.payments.installmentPlan.type).toUpperCase()
                }
                'BankDetails' {
                    'DateOfSign' input.payments.bankDetails.dateOfSign
                    'CityOfSign' input.payments.bankDetails.cityOfSign
                    'PartyBic' input.payments.bankDetails.bic
                    'PartyIban' input.payments.bankDetails.iban
                    'SepaMandate' input.payments.bankDetails.mandateReference
                    'SepaPayType' input.payments.bankDetails.mandateType                
                'Depositor' {
                    'Title' input.payments.bankDetails.depositor.title
                    'Salutation' input.payments.bankDetails.depositor.salutation
                    'FirstName' input.payments.bankDetails.depositor.firstName
                    'LastName' input.payments.bankDetails.depositor.lastName
                    'AddressAddition'  String.valueOf(input.payments.bankDetails.depositor.addressAddition).substring(0, Math.min(40, String.valueOf(input.payments.bankDetails.depositor.addressAddition).length()))
                    'StreetName' input.payments.bankDetails.depositor.street
                    'HouseNumber' input.payments.bankDetails.depositor.houseNr
                    'PostalCode' input.payments.bankDetails.depositor.postalCode
                    'CityName' input.payments.bankDetails.depositor.city
                    'Country' input.payments.bankDetails.depositor.country
                    }
                }           
            }
            'Partner' {
                'PartnerFunction' input.customer.customerNumberType
                'BusinessPartnerID' input.customer.customerNumber
                'DateOfBirth' input.customer.dateOfBirth
                'Address' {
                    'PhysicalAddress' {
                        'Title' input.customer.address.title
                        'Salutation' input.customer.address.salutation
                        'FirstName' input.customer.address.firstName
                        'LastName'  input.customer.address.lastName
                        'AddressAddition' String.valueOf(input.customer.address.addressAddition).substring(0, Math.min(40, String.valueOf(input.customer.address.addressAddition).length()))
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
                    'PhysicalAddress' {
                        'Title' input.customer.address.title
                        'Salutation' input.shippingAddress.salutation
                        'FirstName' input.shippingAddress.firstName
                        'LastName'  input.shippingAddress.lastName
                        'AddressAddition' String.valueOf(input.shippingAddress.addressAddition).substring(0, Math.min(40, String.valueOf(input.shippingAddress.addressAddition).length()))
                        'StreetName' input.shippingAddress.street
                        'PostalCode' input.shippingAddress.postalCode
                        'CityName' input.shippingAddress.city
                        'Country' input.shippingAddress.country
                    }
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
            'Attachment' {
                'PdfFile' input.attachmemts.pdfFile
                'Url' input.attachmemts.url            
            }
        }
    }
    message.setProperty("SenderBusinessSystemID", String.valueOf(input.source).toUpperCase())
    message.setBody(writer.toString())
    return message
}