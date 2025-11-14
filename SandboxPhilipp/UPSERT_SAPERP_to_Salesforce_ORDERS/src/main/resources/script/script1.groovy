package CREATE_SAPERP_to_Salesforce_Account.src.main.resources.script

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import java.text.SimpleDateFormat

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

    //Accessmessagebodyandproperties
    Reader reader = message.getBody(Reader)
    Map properties = message.getProperties()

    //DefineXMLparserandbuilder
    def graph = new XmlSlurper().parse(reader)
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)

    //Definetargetpayloadmapping
    builder.graph {
        //DefineForEachCompoundEmployee
        def validIdoc = graph.IDOC.findAll {
            iDoc ->


                // Filter
               iDoc != ''
        }

        //IterateeachCompoundEmployee
        validIdoc.each {
            iDoc ->
                'graphs'{
                    'graphId'("Graph")
                    def validE1EDKA1 = iDoc.E1EDKA1.findAll {
                        edkai ->
                            edkai != ''
                    }
                    validE1EDKA1.each {
                        edkai ->
                            if (edkai.PARVW == 'AG') {
                                'compositeRequest_Account' {
                                    'method'("GET")
                                    'url'("/services/data/v48.0/sobjects/Account/SAPInterfaceId__c/" + iDoc.EDI_DC40.SNDPOR.toString() + "-" + iDoc.EDI_DC40.MANDT.toString() + "-" + edkai.PARTN.toString())
                                    'referenceId'("refPartnerAccount_AG")
                                }
                            }
                            if (edkai.PARVW == 'RE') {
                                'compositeRequest_Account' {
                                    'method'("GET")
                                    'url'("/services/data/v48.0/sobjects/Account/SAPInterfaceId__c/" + iDoc.EDI_DC40.SNDPOR.toString() + "-" + iDoc.EDI_DC40.MANDT.toString() + "-" + edkai.PARTN.toString())
                                    'referenceId'("refPartnerAccount_RE")
                                }
                            }
                            if (edkai.PARVW == 'RG') {
                                'compositeRequest_Account' {
                                    'method'("GET")
                                    'url'("/services/data/v48.0/sobjects/Account/SAPInterfaceId__c/" + iDoc.EDI_DC40.SNDPOR.toString() + "-" + iDoc.EDI_DC40.MANDT.toString() + "-" + edkai.PARTN.toString())
                                    'referenceId'("refPartnerAccount_RG")
                                }
                            }
                            if (edkai.PARVW == 'WE') {
                                'compositeRequest_Account' {
                                    'method'("GET")
                                    'url'("/services/data/v48.0/sobjects/Account/SAPInterfaceId__c/" + iDoc.EDI_DC40.SNDPOR.toString() + "-" + iDoc.EDI_DC40.MANDT.toString() + "-" + edkai.PARTN.toString())
                                    'referenceId'("refPartnerAccount_WE")
                                }
                            }
                    }
                    'compositeRequest'{
                        'body'{
                            'SAPSystem__c'(iDoc.EDI_DC40.SNDPOR)
                            'SAPClient__c'(iDoc.EDI_DC40.MANDT) //Lookup
                            def validE1EDK14 = iDoc.E1EDK14.findAll {
                                edk14 ->
                                    edk14 != ''
                            }
                            validE1EDK14.each {
                                edk14 ->
                                    if (edk14.QUALF == '007') {
                                        'SAPDistributionChannel__c'(edk14.ORGID)
                                    }
                                    if (edk14.QUALF == '006') {
                                        'SAPDivision__c'(edk14.ORGID)
                                    }
                                    if (edk14.QUALF == '008') {
                                        'SAPSalesOrganization__c'(edk14.ORGID)
                                    }
                                    if (edk14.QUALF == '012') {
                                        'SAPOrderType__c'(edk14.ORGID)
                                    }
                                    if (edk14.QUALF == '001') {
                                        'SAPCustomerPurchaseOrderNumber__c'(iDoc.E1EDK02.BELNR)
                                        'SAPDeliveryConditions__c'(iDoc.E1EDK17.LKOND)
                                    }
                                    if (edk14.QUALF == '006') {
                                        'SAPCustomerContractNumber__c'(iDoc.E1EDK02.BELNR)
                                    }
                            }
                            def validE1EDKA1_new = iDoc.E1EDKA1.findAll {
                                edkai ->
                                    edkai != ''
                            }
                            validE1EDKA1_new.each {
                                edkai ->
                                    if (edkai.PARVW == 'AG') {
                                        'Account__c'("@{refPartnerAccount_AG}")
                                        'Sold-ToName1__c'(edkai.NAME1)
                                        'Sold-ToName2__c'(edkai.NAME2)
                                        'Sold-ToName3__c'(edkai.NAME3)
                                        'Sold-To-Street__c'(edkai.STRAS)
                                        'Sold-To-Street2__c'(edkai.STRS2)
                                        'Sold-To-City__c'(edkai.ORT01)
                                        'Sold-To-Country__c'(edkai.LAND1)
                                        'Sold-To-ZipCode__c'(edkai.PSTLZ)
                                    }
                                    if (edkai.PARVW == 'RE') {
                                        'Bill-To__c'("@{refPartnerAccount_RE}")
                                        'Bill-ToName1__c'(edkai.NAME1)
                                        'Bill-ToName2__c'(edkai.NAME2)
                                        'Bill-ToName3__c'(edkai.NAME3)
                                        'Bill-To-Street__c'(edkai.STRAS)
                                        'Bill-To-Street2__c'(edkai.STRS2)
                                        'Bill-To-City__c'(edkai.ORT01)
                                        'Bill-To-Country__c'(edkai.LAND1)
                                        'Bill-To-ZipCode__c'(edkai.PSTLZ)
                                    }
                                    if (edkai.PARVW == 'RG') {
                                        'Payer__c'("@{refPartnerAccount_RG}")
                                    }
                                    if (edkai.PARVW == 'WE') {
                                        'Ship-To__c'("@{refPartnerAccount_WE}")
                                    }
                            }
                            'CurrencyIsoCode'(iDoc.E1EDK01.CURCY)
                            'SAPPaymentTerms__c'(iDoc.E1EDK01.ZTERM)
                            'SAPOrderReasonDesc__c'(iDoc.E1EDK01.AUGRU)
                            if (iDoc.E1EDK03.IDDAT == 002) {
                                'SAPReqDeliveryDate__c'(iDoc.E1EDK03.DATUM)
                            } else {
                                if (iDoc.E1EDK03.IDDAT == 022) {
                                    'SAPPurchaseDate__c'(iDoc.E1EDK03.DATUM)
                                } else {
                                    if (iDoc.E1EDK03.IDDAT == 022) {
                                        'SAPDateBooking__c'(iDoc.E1EDK03.DATUM+"-"+iDoc.E1EDK03.UZEIT)
                                    }
                                }
                            }
                            'SAPVATTax__c'(iDoc.E1EDK04.MWSBT) //SUMUP?
                            if (iDoc.E1EDS01.SUMID == 002){
                                'SAPTotalNetValue__c'(iDoc.E1EDS01.SUMME)
                            }
                        }
                        'method'("PATCH")
                        'url'("/services/data/v48.0/sobjects/SAPSalesOrder__c/SAPInterfaceId__c/" + iDoc.EDI_DC40.SNDPOR.toString() + "-" + iDoc.EDI_DC40.MANDT.toString() + "-" + iDoc.E1EDK01.BELNR.toString())
                        'referenceId'("Order")
                    }
                    def validE1EDP01 = iDoc.E1EDP01.findAll {
                        edp01 ->
                            edp01 != ''
                    }
                    Item = 1
                    validE1EDP01.each {
                        edp01 ->
                        'compositeRequest'{
                            'body'{
                                'Name'(edp01.POSEX)
                                'SAPIDocNumber__c'(iDoc.EDI_DC40.DOCNUM)
                                'SAPQuantity__c'(edp01.MENGE)
                                'CancellationReason__c'(edp01.ABGRU)
                                'UnitOfMeasure__c'(edp01.MENEE)
                                'SAPPriceNet__c'(edp01.VPREI)
                                'SAPItemNumber__c'(edp01.POSEX)
                                if (iDoc.E1EDK14.QUALF == 002) {
                                    'SAPProduct__c'(iDoc.E1EDP01.E1EDP19)
                                }
                                'CurrencyIsoCode'(iDoc.E1EDK01.CURCY)
                                'ItemCategory__c'(iDoc.E1EDK01.PSTYV)
                                'ItemValue__c'(iDoc.E1EDK01.NETWR)
                                //'SAPSalesOrder__c'(iDoc.EDI_DC40.SNDPOR.toString() + "-" + iDoc.EDI_DC40.MANDT.toString() + "-" + iDoc.E1EDK01.BELNR.toString())
                                }
                            'method'("PATCH")
                            'url'("/services/data/v48.0/sobjects/SAPSalesOrderItem__c/SAPInterfaceId__c/" + iDoc.EDI_DC40.SNDPOR.toString() + "-" + iDoc.EDI_DC40.MANDT.toString() + "-" + iDoc.E1EDK01.BELNR.toString() + "-" + edp01.POSEX.toString())
                            'referenceId'("OrderItem-"+Item.toString())
                        }
                        Item = Item + 1
                    }
                }
        }

    }

    //Generateoutput
    message.setBody(writer.toString())
    return message
}
