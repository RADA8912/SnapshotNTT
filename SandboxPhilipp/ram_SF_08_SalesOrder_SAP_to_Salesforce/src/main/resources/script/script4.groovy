package UPSERT_SAPERP_to_Salesforce_ORDERS.src.main.resources.script

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import java.text.SimpleDateFormat
import java.util.Date


Message processData(Message message) {

    //Accessmessagebodyandproperties
    Reader reader = message.getBody(Reader)
    Map properties = message.getProperties()

    //DefineXMLparserandbuilder
    def SAPSalesOrder = new XmlSlurper().parse(reader)
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)

    //Definetargetpayloadmapping
    builder.SAPSalesOrder {
        //DefineForEachOrder/Quote
        def validIdoc = SAPSalesOrder.IDOC.findAll {
            iDoc ->


                // Filter
                iDoc != ''
        }

        //IterateEachOrder/Quote
        validIdoc.each {
            iDoc ->
                message.setProperty("SAPInterfaceID__c", iDoc.E1EDK01.BELNR.toString().replaceAll('"',''))
                    'graphs' {
                        'graphId'("Graph")
                        'compositeRequest' {
                            'method'("PATCH")
                            'url'("/services/data/v52.0/sobjects/SAPSalesOrder__c/SAPInterfaceId__c/" + iDoc.E1EDK01.BELNR.toString().replaceAll('"', ''))
                            'referenceId'("Order")
                            message.setProperty("type", "SAPSalesOrder__c")
                            'body' {
                                'Name'(iDoc.E1EDK01.BELNR.toString().replaceAll('"', ''))
                                'SAPOrderReasonDesc__c'(iDoc.E1EDK01.AUGRU.toString().replaceAll('"', ''))
                                def validE1EDKA1_new = iDoc.E1EDKA1.findAll {
                                    edkai ->
                                        edkai != ''
                                }
                                validE1EDKA1_new.each {
                                    edkai ->
                                    if (edkai.PARVW == 'AG') {
                                        'Sold_To__r' {
                                            'SAPInterfaceId__c'(edkai.PARTN.toString().replaceAll('"', ''))
                                        }
                                    }
                                }
                                def validE1EDK03 = iDoc.E1EDK03.findAll {
                                    k03 ->
                                        k03 != ''
                                }
                                validE1EDK03.each {
                                    k03 ->
                                    if (k03.IDDAT.toString() == "002") {
                                        String oldDate = k03.DATUM.toString()
                                        Date date = new SimpleDateFormat("yyyyMMdd").parse(oldDate)
                                        String newDate = new SimpleDateFormat("yyyy-MM-dd").format(date)
                                        'SAPReqDeliveryDate__c'(newDate)
                                    }
                                    if (k03.IDDAT.toString() == "022") {
                                        String oldDate = k03.DATUM.toString()
                                        Date date = new SimpleDateFormat("yyyyMMdd").parse(oldDate)
                                        String newDate = new SimpleDateFormat("yyyy-MM-dd").format(date)
                                        'SAPPurchaseDate__c'(newDate)
                                    }
                                    if (k03.IDDAT.toString() == "025") {
                                        String oldDate = k03.DATUM.toString() + "-" + k03.UZEIT.toString()
                                        Date date = new SimpleDateFormat("yyyyMMdd-HHmmss").parse(oldDate)
                                        String newDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+02:00'").format(date)
                                        'SAPDateBooking__c'(newDate)
                                    }
                                }
                            }
                        }
                    }
                    def validE1EDP01 = iDoc.E1EDP01.findAll {
                        edp01 ->
                            edp01 != ''
                    }
                    validE1EDP01.each {
                        edp01 ->
                            'graphs' {
                                'graphId'("Item_Graph_"+edp01.POSEX.toString())
                                'compositeRequest' {
                                    'method'("PATCH")
                                    'url'("/services/data/v52.0/sobjects/SAPSalesOrderItem__c/SAPInterfaceId__c/" + iDoc.E1EDK01.BELNR.toString().replaceAll('"','') + "-" + edp01.POSEX.toString().replaceAll('"',''))
                                    'referenceId'("OrderItem")
                                    'body' {
                                        'SAPSalesOrder__r'{
                                            'SAPInterfaceId__c'(iDoc.E1EDK01.BELNR.toString().replaceAll('"',''))
                                        }
                                        'SAPQuantity__c'(edp01.MENGE.toString().replaceAll('"',''))
                                        'UnitOfMeasure__c'(edp01.MENEE.toString().replaceAll('"',''))
                                        'SAPPriceNet__c'(edp01.VPREI.toString().replaceAll('"',''))
                                        'SAPItemNumber__c'(edp01.POSEX.toString().replaceAll('"',''))
                                        def validE1EDP19 = edp01.E1EDP19.findAll {
                                            edp19 ->
                                                edp19 != ''
                                        }
                                        validE1EDP19.each {
                                            edp19 ->
                                            if (edp19.QUALF.toString() == "002") {
                                                'SAPProduct__r' {
                                                    'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + edp19.IDTNR.toString().replaceAll('"', ''))
                                                }
                                                'SAPPositionText__c'(edp19.KTEXT.toString().replaceAll('"', ''))
                                            }   
                                        }  
                                    }
                                }
                            }
                    }
            }
    }


    //Generateoutput
    message.setBody(writer.toString())
    return message
}
