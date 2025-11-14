package UPSERT_SAPERP_to_Salesforce_ORDERS.src.main.resources.script

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import java.text.SimpleDateFormat
import java.util.Date

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
                message.setProperty("SAPInterfaceID__c", iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"','') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"','') + "-" + iDoc.E1EDK01.BELNR.toString().replaceAll('"',''))
                //If MESTYP == ORDERS generate order request or contract request
                if (iDoc.EDI_DC40.MESTYP == "ORDERS") {
                    def validE1EDK14zkm = iDoc.E1EDK14.findAll {
                        edk14zkm ->
                            edk14zkm != ''
                    }
                    validE1EDK14zkm.each {
                        edk14zkm ->
                            //If QUALF.012 == ZKM generate contract request
                            if(edk14zkm.QUALF == '012'){
                                if(edk14zkm.ORGID == 'ZKM'){
                                    'graphs' {
                                        'graphId'("Graph")
                                        'compositeRequest' {
                                            'method'("PATCH")
                                            'url'("/services/data/v52.0/sobjects/SAPContract__c/SAPInterfaceID__c/" + iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + iDoc.E1EDK01.BELNR.toString().replaceAll('"', ''))
                                            'referenceId'("Contract")
                                            message.setProperty("type", "SAPContract__c")
                                            'body' {
                                                'Name'(iDoc.E1EDK01.BELNR.toString().replaceAll('"', ''))
                                                'SAPIDocNumber__c'(iDoc.EDI_DC40.DOCNUM.toString().replaceAll('"', ''))
                                                'SAPSystemID__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', ''))
                                                'SAPClient__c'(iDoc.EDI_DC40.MANDT.toString().replaceAll('"', ''))
                                                'CurrencyIsoCode'(iDoc.E1EDK01.CURCY.toString().replaceAll('"', ''))
                                                'SAPPaymentTerms__c'(iDoc.E1EDK01.ZTERM.toString().replaceAll('"', ''))
                                                SummeTAX = 0
                                                def validE1EDK04 = iDoc.E1EDK04.findAll {
                                                    edk04 ->
                                                        edk04 != ''
                                                }
                                                validE1EDK04.each {
                                                    edk04 ->
                                                        SummeTAX = SummeTAX + edk04.MWSBT.toFloat()
                                                }
                                                'SAPVATTax__c'(SummeTAX.toString().replaceAll('"', ''))
                                                def validE1EDK02 = iDoc.E1EDK02.findAll {
                                                    edk02 ->
                                                        edk02 != ''
                                                }
                                                validE1EDK02.each {
                                                    edk02 ->
                                                        if (edk02.QUALF == '001') {
                                                            'SAPCustomerPurchaseOrderNumber__c'(edk02.BELNR.toString().replaceAll('"', ''))
                                                        }
                                                }
                                                def VKORG = new String()
                                                def VTWEG = new String()
                                                def SPART = new String()
                                                def validE1EDK14 = iDoc.E1EDK14.findAll {
                                                    edk14 ->
                                                        edk14 != ''
                                                }
                                                validE1EDK14.each {
                                                    edk14 ->
                                                        if (edk14.QUALF == '007') {
                                                            VTWEG = edk14.ORGID.toString().replaceAll('"', '')
                                                            'SAPDistributionChannel__c'(VTWEG)
                                                        }
                                                        if (edk14.QUALF == '006') {
                                                            SPART = edk14.ORGID.toString().replaceAll('"', '')
                                                            'SAPDivision__c'(SPART)
                                                        }
                                                        if (edk14.QUALF == '008') {
                                                            VKORG = edk14.ORGID.toString().replaceAll('"', '')
                                                            'SAPSalesOrganization__c'(VKORG)
                                                        }
                                                        if (edk14.QUALF == '012') {
                                                            'SAPOrderType__c'(edk14.ORGID.toString().replaceAll('"', ''))
                                                        }
                                                        if (edk14.QUALF.toString() == "ZOS"){
                                                            'SAPContractStatus__c'(edk14.ORGID)
                                                        }
                                                }
                                                def validE1EDK17 = iDoc.E1EDK17.findAll {
                                                    edk17 ->
                                                        edk17 != ''
                                                }
                                                validE1EDK17.each {
                                                    edk17 ->
                                                        if (edk17.QUALF == '001') {
                                                            'SAPDeliveryConditions__c'(edk17.LKOND.toString().replaceAll('"', ''))
                                                        }
                                                }
                                                def KUNNR = new String()
                                                def validE1EDKA1_new = iDoc.E1EDKA1.findAll {
                                                    edkai ->
                                                        edkai != ''
                                                }
                                                validE1EDKA1_new.each {
                                                    edkai ->
                                                        if (edkai.PARVW == 'AG') {
                                                            'SoldTo__r' {
                                                                'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + edkai.PARTN.toString().replaceAll('"', ''))
                                                            }
                                                            KUNNR = edkai.PARTN.toString().replaceAll('"', '')
                                                            'SoldToName1__c'(edkai.NAME1.toString().replaceAll('"', ''))
                                                            'SoldToName2__c'(edkai.NAME2.toString().replaceAll('"', ''))
                                                            'SoldToName3__c'(edkai.NAME3.toString().replaceAll('"', ''))
                                                            'SoldToAddress__Street__s'(edkai.STRAS.toString().replaceAll('"', '') + " " + edkai.STRS2.toString().replaceAll('"', ''))
                                                            'SoldToAddress__City__s'(edkai.ORT01.toString().replaceAll('"', ''))
                                                            'SoldToAddress__CountryCode__s'(edkai.LAND1.toString().replaceAll('"', ''))
                                                            'SoldToAddress__PostalCode__s'(edkai.PSTLZ.toString().replaceAll('"', ''))
                                                        }
                                                        if (edkai.PARVW == 'RE') {
                                                            'BillTo__r' {
                                                                'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + edkai.PARTN.toString().replaceAll('"', ''))
                                                            }
                                                            'BillToName1__c'(edkai.NAME1.toString().replaceAll('"', ''))
                                                            'BillToName2__c'(edkai.NAME2.toString().replaceAll('"', ''))
                                                            'BillToName3__c'(edkai.NAME3.toString().replaceAll('"', ''))
                                                            'BillToAddress__Street__s'(edkai.STRAS.toString().replaceAll('"', '') + " " + edkai.STRS2.toString().replaceAll('"', ''))
                                                            'BillToAddress__City__s'(edkai.ORT01.toString().replaceAll('"', ''))
                                                            'BillToAddress__CountryCode__s'(edkai.LAND1.toString().replaceAll('"', ''))
                                                            'BillToAddress__PostalCode__s'(edkai.PSTLZ.toString().replaceAll('"', ''))
                                                        }
                                                        if (edkai.PARVW == 'RG') {
                                                            'Payer__r' {
                                                                'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + edkai.PARTN.toString().replaceAll('"', ''))
                                                            }
                                                        }
                                                        if (edkai.PARVW == 'WE') {
                                                            'ShipTo__r' {
                                                                'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + edkai.PARTN.toString().replaceAll('"', ''))
                                                            }
                                                        }
                                                }
                                                def validE1EDK03 = iDoc.E1EDK03.findAll {
                                                    k03 ->
                                                        k03 != ''
                                                }
                                                validE1EDK03.each {
                                                    k03 ->
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
                                                        if (k03.IDDAT.toString() == "019") {
                                                            String oldDate = k03.DATUM.toString()
                                                            Date date = new SimpleDateFormat("yyyyMMdd").parse(oldDate)
                                                            String newDate = new SimpleDateFormat("yyyy-MM-dd").format(date)
                                                            'SAPValidFromDate__c'(newDate)
                                                        }
                                                        if (k03.IDDAT.toString() == "020") {
                                                            String oldDate = k03.DATUM.toString()
                                                            Date date = new SimpleDateFormat("yyyyMMdd").parse(oldDate)
                                                            String newDate = new SimpleDateFormat("yyyy-MM-dd").format(date)
                                                            'SAPValidToDate__c'(newDate)
                                                        }
                                                }
                                                def validE1EDS01 = iDoc.E1EDS01.findAll {
                                                    s01 ->
                                                        s01 != ''
                                                }
                                                validE1EDS01.each {
                                                    s01 ->
                                                        if (s01.SUMID.toString() == "002") {
                                                            m = s01.SUMME.toString().length()
                                                            if (s01.SUMME.toString().drop(m-1) == "-"){
                                                                'SAPTotalNetValue__c'("-"+s01.SUMME.toString().replaceAll('-', ''))
                                                            }else{
                                                                'SAPTotalNetValue__c'(s01.SUMME.toString())
                                                            }
                                                        }
                                                }
                                                'SAPSalesArea__r'{
                                                    'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString() + "-" + iDoc.EDI_DC40.MANDT.toString() + "-" + KUNNR + "-" + VKORG +"-" + VTWEG+ "-" + SPART)
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
                                                    'url'("/services/data/v52.0/sobjects/SAPContractLineItem__c/SAPInterfaceId__c/" + iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"','') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"','') + "-" + iDoc.E1EDK01.BELNR.toString().replaceAll('"','') + "-" + edp01.POSEX.toString().replaceAll('"',''))
                                                    'referenceId'("ContractItem")
                                                    'body' {
                                                        'SAPContract__r'{
                                                            'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"','') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"','') + "-" + iDoc.E1EDK01.BELNR.toString().replaceAll('"',''))
                                                        }
                                                        'SAPIDocNumber__c'(iDoc.EDI_DC40.DOCNUM.toString().replaceAll('"',''))
                                                        'CancellationReason__c'(edp01.ABGRU.toString().replaceAll('"',''))
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
                                                                    if (edp19.IDTNR.toString().length() > 0) {
                                                                        'SAPProduct__r' {
                                                                            'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + edp19.IDTNR.toString().replaceAll('"', ''))
                                                                        }
                                                                    } else {
                                                                        'SAPProduct__r'{
                                                                            'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"','') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"','') + "-" + "000000000000000000")
                                                                        }
                                                                    }
                                                                }
                                                        }
                                                        'CurrencyIsoCode'(edp01.CURCY.toString().replaceAll('"',''))
                                                        'ItemCategory__c'(edp01.PSTYV.toString().replaceAll('"',''))
                                                        m = edp01.NETWR.toString().length()
                                                        if (edp01.NETWR.toString().drop(m-1) == "-"){
                                                            'ItemValue__c'(edp01.NETWR.toString().replaceAll('-', ''))
                                                        }else{
                                                            'ItemValue__c'(edp01.NETWR.toString())
                                                        }
                                                        def validE1EDP02 = edp01.E1EDP02.findAll {
                                                            edp02 ->
                                                                edp02 != ''
                                                        }
                                                        validE1EDP02.each {
                                                            edp02 ->
                                                                if (edp02.QUALF == 'ZTQ') {
                                                                    'SAPTargetQuantity__c'(edp02.BELNR.toString().replaceAll('"', ''))
                                                                }
                                                                if (edp02.QUALF == 'ZOQ') {
                                                                    'SAPOrderQuantity__c'(edp02.BELNR.toString().replaceAll('"', ''))
                                                                }
                                                        }
                                                    }
                                                }
                                            }
                                    }
                                } else {
                                    //else generate order request
                                    'graphs' {
                                        'graphId'("Graph")
                                        'compositeRequest' {
                                            'method'("PATCH")
                                            'url'("/services/data/v52.0/sobjects/SAPSalesOrder__c/SAPInterfaceId__c/" + iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + iDoc.E1EDK01.BELNR.toString().replaceAll('"', ''))
                                            'referenceId'("Order")
                                            message.setProperty("type", "SAPSalesOrder__c")
                                            'body' {
                                                'Name'(iDoc.E1EDK01.BELNR.toString().replaceAll('"', ''))
                                                'SAPIDocNumber__c'(iDoc.EDI_DC40.DOCNUM.toString().replaceAll('"', ''))
                                                'SAPSystemID__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', ''))
                                                'SAPClient__c'(iDoc.EDI_DC40.MANDT.toString().replaceAll('"', ''))
                                                'CurrencyIsoCode'(iDoc.E1EDK01.CURCY.toString().replaceAll('"', ''))
                                                'SAPPaymentTerms__c'(iDoc.E1EDK01.ZTERM.toString().replaceAll('"', ''))
                                                'SAPOrderReasonDesc__c'(iDoc.E1EDK01.AUGRU.toString().replaceAll('"', ''))
                                                SummeTAX = 0
                                                def validE1EDK04 = iDoc.E1EDK04.findAll {
                                                    edk04 ->
                                                        edk04 != ''
                                                }
                                                validE1EDK04.each {
                                                    edk04 ->
                                                        SummeTAX = SummeTAX + edk04.MWSBT.toFloat()
                                                }
                                                'SAPVATTax__c'(SummeTAX.toString().replaceAll('"', ''))
                                                def validE1EDK02 = iDoc.E1EDK02.findAll {
                                                    edk02 ->
                                                        edk02 != ''
                                                }
                                                validE1EDK02.each {
                                                    edk02 ->
                                                        if (edk02.QUALF == '001') {
                                                            'SAPCustomerPurchaseOrderNumber__c'(edk02.BELNR.toString().replaceAll('"', ''))
                                                        }
                                                        if (edk02.QUALF == '006') {
                                                            'SAPCustomerContractNumber__c'(edk02.BELNR.toString().replaceAll('"', ''))
                                                        }
                                                }
                                                def validE1EDK14 = iDoc.E1EDK14.findAll {
                                                    edk14 ->
                                                        edk14 != ''
                                                }
                                                validE1EDK14.each {
                                                    edk14 ->
                                                        if (edk14.QUALF == '007') {
                                                            VTWEG = edk14.ORGID.toString().replaceAll('"', '')
                                                            'SAPDistributionChannel__c'(VTWEG)
                                                        }
                                                        if (edk14.QUALF == '006') {
                                                            SPART = edk14.ORGID.toString().replaceAll('"', '')
                                                            'SAPDivision__c'(SPART)
                                                        }
                                                        if (edk14.QUALF == '008') {
                                                            VKORG = edk14.ORGID.toString().replaceAll('"', '')
                                                            'SAPSalesOrganization__c'(VKORG)
                                                        }
                                                        if (edk14.QUALF == '012') {
                                                            'SAPOrderType__c'(edk14.ORGID.toString().replaceAll('"', ''))
                                                        }
                                                        if (edk14.QUALF.toString() == "ZOS"){
                                                            'SAPOrderStatus__c'(edk14.ORGID)
                                                        }
                                                        if (edk14.QUALF.toString() == "ZDS"){
                                                            'SAPOrderDeliveryStatus__c'(edk14.ORGID)
                                                        }
                                                }
                                                def validE1EDK17 = iDoc.E1EDK17.findAll {
                                                    edk17 ->
                                                        edk17 != ''
                                                }
                                                validE1EDK17.each {
                                                    edk17 ->
                                                        if (edk17.QUALF == '001') {
                                                            'IncoTerms__c'(edk17.LKOND.toString().replaceAll('"', ''))
                                                        }
                                                }
                                                def validE1EDKA1_new = iDoc.E1EDKA1.findAll {
                                                    edkai ->
                                                        edkai != ''
                                                }
                                                validE1EDKA1_new.each {
                                                    edkai ->
                                                        if (edkai.PARVW == 'AG') {
                                                            'Sold_To__r' {
                                                                'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + edkai.PARTN.toString().replaceAll('"', ''))
                                                            }
                                                            KUNNR = edkai.PARTN.toString().replaceAll('"', '')
                                                            'SoldToName1__c'(edkai.NAME1.toString().replaceAll('"', ''))
                                                            'SoldToName2__c'(edkai.NAME2.toString().replaceAll('"', ''))
                                                            'SoldToName3__c'(edkai.NAME3.toString().replaceAll('"', ''))
                                                            'Sold_To_Address__Street__s'(edkai.STRAS.toString().replaceAll('"', '') + " " + edkai.STRS2.toString().replaceAll('"', ''))
                                                            'Sold_To_Address__City__s'(edkai.ORT01.toString().replaceAll('"', ''))
                                                            'Sold_To_Address__CountryCode__s'(edkai.LAND1.toString().replaceAll('"', ''))
                                                            'Sold_To_Address__PostalCode__s'(edkai.PSTLZ.toString().replaceAll('"', ''))
                                                        }
                                                        if (edkai.PARVW == 'RE') {
                                                            'Bill_To__r' {
                                                                'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + edkai.PARTN.toString().replaceAll('"', ''))
                                                            }
                                                            'BillToName1__c'(edkai.NAME1.toString().replaceAll('"', ''))
                                                            'BillToName2__c'(edkai.NAME2.toString().replaceAll('"', ''))
                                                            'BillToName3__c'(edkai.NAME3.toString().replaceAll('"', ''))
                                                            'Bill_To_Address__Street__s'(edkai.STRAS.toString().replaceAll('"', '') + " " + edkai.STRS2.toString().replaceAll('"', ''))
                                                            'Bill_To_Address__City__s'(edkai.ORT01.toString().replaceAll('"', ''))
                                                            'Bill_To_Address__CountryCode__s'(edkai.LAND1.toString().replaceAll('"', ''))
                                                            'Bill_To_Address__PostalCode__s'(edkai.PSTLZ.toString().replaceAll('"', ''))
                                                        }
                                                        if (edkai.PARVW == 'RG') {
                                                            'Payer__r' {
                                                                'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + edkai.PARTN.toString().replaceAll('"', ''))
                                                            }
                                                            'PayerName1__c'(edkai.NAME1.toString().replaceAll('"', ''))
                                                            'PayerName2__c'(edkai.NAME2.toString().replaceAll('"', ''))
                                                            'PayerName3__c'(edkai.NAME3.toString().replaceAll('"', ''))
                                                            'Payer_Address__Street__s'(edkai.STRAS.toString().replaceAll('"', '') + " " + edkai.STRS2.toString().replaceAll('"', ''))
                                                            'Payer_Address__City__s'(edkai.ORT01.toString().replaceAll('"', ''))
                                                            'Payer_Address__CountryCode__s'(edkai.LAND1.toString().replaceAll('"', ''))
                                                            'Payer_Address__PostalCode__s'(edkai.PSTLZ.toString().replaceAll('"', ''))

                                                        }
                                                        if (edkai.PARVW == 'WE') {
                                                            'Ship_To__r' {
                                                                'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + edkai.PARTN.toString().replaceAll('"', ''))
                                                            }
                                                            'Ship_To_Address__Street__s'(edkai.STRAS.toString().replaceAll('"', '') + " " + edkai.STRS2.toString().replaceAll('"', ''))
                                                            'Ship_To_Address__City__s'(edkai.ORT01.toString().replaceAll('"', ''))
                                                            'Ship_To_Address__CountryCode__s'(edkai.LAND1.toString().replaceAll('"', ''))
                                                            'Ship_To_Address__PostalCode__s'(edkai.PSTLZ.toString().replaceAll('"', ''))
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
                                                def validE1EDS01 = iDoc.E1EDS01.findAll {
                                                    s01 ->
                                                        s01 != ''
                                                }
                                                validE1EDS01.each {
                                                    s01 ->
                                                        if (s01.SUMID.toString() == "002") {
                                                            m = s01.SUMME.toString().length()
                                                            if (s01.SUMME.toString().drop(m-1) == "-"){
                                                                'SAPTotalNetValue__c'("-"+s01.SUMME.toString().replaceAll('-', ''))
                                                            }else{
                                                                'SAPTotalNetValue__c'(s01.SUMME.toString())
                                                            }
                                                        }
                                                }
                                                'SalesArea__r'{
                                                    'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString() + "-" + iDoc.EDI_DC40.MANDT.toString() + "-" + KUNNR + "-" + VKORG +"-" + VTWEG+ "-" + SPART)
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
                                                    'url'("/services/data/v52.0/sobjects/SAPSalesOrderItem__c/SAPInterfaceId__c/" + iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"','') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"','') + "-" + iDoc.E1EDK01.BELNR.toString().replaceAll('"','') + "-" + edp01.POSEX.toString().replaceAll('"',''))
                                                    'referenceId'("OrderItem")
                                                    'body' {
                                                        'SAPSalesOrder__r'{
                                                            'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"','') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"','') + "-" + iDoc.E1EDK01.BELNR.toString().replaceAll('"',''))
                                                        }
                                                        'SAPIDocNumber__c'(iDoc.EDI_DC40.DOCNUM.toString().replaceAll('"',''))
                                                        'SAPQuantity__c'(edp01.MENGE.toString().replaceAll('"',''))
                                                        'CancellationReason__c'(edp01.ABGRU.toString().replaceAll('"',''))
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
                                                                    if (edp19.IDTNR.toString().length() > 0) {
                                                                        'SAPProduct__r' {
                                                                            'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + edp19.IDTNR.toString().replaceAll('"', ''))
                                                                        }
                                                                    } else {
                                                                        'SAPProduct__r'{
                                                                            'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"','') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"','') + "-" + "000000000000000000")
                                                                        }
                                                                    }
                                                                    'SAPPositionText__c'(edp19.KTEXT.toString().replaceAll('"', ''))
                                                                }
                                                        }
                                                        'CurrencyIsoCode'(edp01.CURCY.toString().replaceAll('"',''))
                                                        'ItemCategory__c'(edp01.PSTYV.toString().replaceAll('"',''))
                                                        m = edp01.NETWR.toString().length()
                                                        if (edp01.NETWR.toString().drop(m-1) == "-"){
                                                            'ItemValue__c'(edp01.NETWR.toString().replaceAll('-', ''))
                                                        }else{
                                                            'ItemValue__c'(edp01.NETWR.toString())
                                                        }
                                                        def validE1EDP02 = edp01.E1EDP02.findAll {
                                                            edp02 ->
                                                                edp02 != ''
                                                        }
                                                        validE1EDP02.each {
                                                            edp02 ->
                                                                if (edp02.QUALF == 'ZPS') {
                                                                    'SAPPositionStatus__c'(edp02.BELNR.toString().replaceAll('"', ''))
                                                                }
                                                                if (edp02.QUALF == 'ZDS') {
                                                                    'SAPPositionDeliveryStatus__c'(edp02.BELNR.toString().replaceAll('"', ''))
                                                                }
                                                                if (edp02.QUALF == 'ZOQ') {
                                                                    'SAPPositionDeliveryQuantity__c'(edp02.BELNR.toString().replaceAll('"', ''))
                                                                }
                                                                if (edp02.QUALF == 'ZSC' || edp02.QUALF == 'ZSQ' || edp02.QUALF == 'ZSO' || edp02.QUALF == 'ZSR' || edp02.QUALF == 'ZSF') {
                                                                    'SAPPredecessorDocument__c'(edp02.BELNR.toString().replaceAll('"', ''))
                                                                }
                                                        }
                                                    }
                                                }
                                            }
                                    }
                                }
                            }
                    }
                }
                //If MESTYP == QUOTES generate quotes request
                if (iDoc.EDI_DC40.MESTYP == "QUOTES") {
                    'graphs' {
                        'graphId'("Graph")
                        'compositeRequest' {
                            'method'("PATCH")
                            'url'("/services/data/v52.0/sobjects/SAPQuote__c/SAPInterfaceId__c/" + iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + iDoc.E1EDK01.BELNR.toString().replaceAll('"', ''))
                            'referenceId'("Quote")
                            message.setProperty("type", "SAPQuote__c")
                            'body' {
                                'Name'(iDoc.E1EDK01.BELNR.toString().replaceAll('"', ''))
                                'CurrencyIsoCode'(iDoc.E1EDK01.CURCY.toString().replaceAll('"', ''))
                                'SAPPaymentTerms__c'(iDoc.E1EDK01.ZTERM.toString().replaceAll('"', ''))
                                'SAPOrderReasonDesc__c'(iDoc.E1EDK01.AUGRU.toString().replaceAll('"', ''))
                                'SAPIDocNumber__c'(iDoc.EDI_DC40.DOCNUM.toString().replaceAll('"', ''))
                                'SAPSystemID__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', ''))
                                'SAPClient__c'(iDoc.EDI_DC40.MANDT.toString().replaceAll('"', ''))
                                SummeTAX = 0
                                def validE1EDK04 = iDoc.E1EDK04.findAll {
                                    edk04 ->
                                        edk04 != ''
                                }
                                validE1EDK04.each {
                                    edk04 ->
                                        SummeTAX = SummeTAX + edk04.MWSBT.toFloat()
                                }
                                'SAPVATTax__c'(SummeTAX.toString().replaceAll('"', ''))
                                def validE1EDK02 = iDoc.E1EDK02.findAll {
                                    edk02 ->
                                        edk02 != ''
                                }
                                validE1EDK02.each {
                                    edk02 ->
                                        if (edk02.QUALF == '001') {
                                            'SAPCustomerPurchaseOrderNumber__c'(edk02.BELNR.toString().replaceAll('"', ''))
                                        }
                                        if (edk02.QUALF == '006') {
                                            'SAPCustomerContractNumber__c'(edk02.BELNR.toString().replaceAll('"', ''))
                                        }
                                }
                                def validE1EDK14 = iDoc.E1EDK14.findAll {
                                    edk14 ->
                                        edk14 != ''
                                }
                                validE1EDK14.each {
                                    edk14 ->
                                        if (edk14.QUALF == '007') {
                                            VTWEG = edk14.ORGID.toString().replaceAll('"', '')
                                            'SAPDistributionChannel__c'(VTWEG)
                                        }
                                        if (edk14.QUALF == '006') {
                                            SPART = edk14.ORGID.toString().replaceAll('"', '')
                                            'SAPDivision__c'(SPART)
                                        }
                                        if (edk14.QUALF == '008') {
                                            VKORG = edk14.ORGID.toString().replaceAll('"', '')
                                            'SAPSalesOrganization__c'(VKORG)
                                        }
                                        if (edk14.QUALF == '018') {
                                            'SAPOrderType__c'(edk14.ORGID.toString().replaceAll('"', ''))
                                        }
                                }
                                def validE1EDK17 = iDoc.E1EDK17.findAll {
                                    edk17 ->
                                        edk17 != ''
                                }
                                validE1EDK17.each {
                                    edk17 ->
                                        if (edk17.QUALF == '001') {
                                            'IncoTerms__c'(edk17.LKOND.toString().replaceAll('"', ''))
                                        }
                                }
                                def validE1EDKA1_new = iDoc.E1EDKA1.findAll {
                                    edkai ->
                                        edkai != ''
                                }
                                validE1EDKA1_new.each {
                                    edkai ->
                                        if (edkai.PARVW == 'AG') {
                                            'Account__r' {
                                                'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + edkai.PARTN.toString().replaceAll('"', ''))
                                            }
                                            KUNNR = edkai.PARTN.toString().replaceAll('"', '')
                                            'SoldToName1__c'(edkai.NAME1.toString().replaceAll('"', ''))
                                            'SoldToName2__c'(edkai.NAME2.toString().replaceAll('"', ''))
                                            'SoldToName3__c'(edkai.NAME3.toString().replaceAll('"', ''))
                                            'SoldToAddress__Street__s'(edkai.STRAS.toString().replaceAll('"', '') + " " + edkai.STRS2.toString().replaceAll('"', ''))
                                            'SoldToAddress__City__s'(edkai.ORT01.toString().replaceAll('"', ''))
                                            'SoldToAddress__CountryCode__s'(edkai.LAND1.toString().replaceAll('"', ''))
                                            'SoldToAddress__PostalCode__s'(edkai.PSTLZ.toString().replaceAll('"', ''))
                                        }
                                        if (edkai.PARVW == 'RE') {
                                            'BillTo__r' {
                                                'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + edkai.PARTN.toString().replaceAll('"', ''))
                                            }
                                            'BillToName1__c'(edkai.NAME1.toString().replaceAll('"', ''))
                                            'BillToName2__c'(edkai.NAME2.toString().replaceAll('"', ''))
                                            'BillToName3__c'(edkai.NAME3.toString().replaceAll('"', ''))
                                            'BillToAddress__Street__s'(edkai.STRAS.toString().replaceAll('"', '') + " " + edkai.STRS2.toString().replaceAll('"', ''))
                                            'BillToAddress__City__s'(edkai.ORT01.toString().replaceAll('"', ''))
                                            'BillToAddress__CountryCode__s'(edkai.LAND1.toString().replaceAll('"', ''))
                                            'BillToAddress__PostalCode__s'(edkai.PSTLZ.toString().replaceAll('"', ''))
                                        }
                                        if (edkai.PARVW == 'RG') {
                                            'Payer__r' {
                                                'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + edkai.PARTN.toString().replaceAll('"', ''))
                                            }
                                            'PayerName1__c'(edkai.NAME1.toString().replaceAll('"', ''))
                                            'PayerName2__c'(edkai.NAME2.toString().replaceAll('"', ''))
                                            'PayerName3__c'(edkai.NAME3.toString().replaceAll('"', ''))
                                            'PayerAddress__Street__s'(edkai.STRAS.toString().replaceAll('"', '') + " " + edkai.STRS2.toString().replaceAll('"', ''))
                                            'PayerAddress__City__s'(edkai.ORT01.toString().replaceAll('"', ''))
                                            'PayerAddress__CountryCode__s'(edkai.LAND1.toString().replaceAll('"', ''))
                                            'PayerAddress__PostalCode__s'(edkai.PSTLZ.toString().replaceAll('"', ''))
                                        }
                                        if (edkai.PARVW == 'WE') {
                                            'ShipTo__r' {
                                                'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + edkai.PARTN.toString().replaceAll('"', ''))
                                            }
                                            'ShipToAddress__Street__s'(edkai.STRAS.toString().replaceAll('"', '') + " " + edkai.STRS2.toString().replaceAll('"', ''))
                                            'ShipToAddress__City__s'(edkai.ORT01.toString().replaceAll('"', ''))
                                            'ShipToAddress__CountryCode__s'(edkai.LAND1.toString().replaceAll('"', ''))
                                            'ShipToAddress__PostalCode__s'(edkai.PSTLZ.toString().replaceAll('"', ''))
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
                                        if (k03.IDDAT.toString() == "006") {
                                            String oldDate = k03.DATUM.toString()
                                            Date date = new SimpleDateFormat("yyyyMMdd").parse(oldDate)
                                            String newDate = new SimpleDateFormat("yyyy-MM-dd").format(date)
                                            'SAPValidTo__c'(newDate)
                                        }
                                        if (k03.IDDAT.toString() == "025") {
                                            String oldDate = k03.DATUM.toString() + "-" + k03.UZEIT.toString()
                                            Date date = new SimpleDateFormat("yyyyMMdd-HHmmss").parse(oldDate)
                                            String newDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+02:00'").format(date)
                                            'SAPDateBooking__c'(newDate)
                                        }
                                }
                                def validE1EDS01 = iDoc.E1EDS01.findAll {
                                    s01 ->
                                        s01 != ''
                                }
                                validE1EDS01.each {
                                    s01 ->
                                        if (s01.SUMID.toString() == "002") {
                                            m = s01.SUMME.toString().length()
                                            if (s01.SUMME.toString().drop(m-1) == "-"){
                                                'SAPTotalNetValue__c'("-"+s01.SUMME.toString().replaceAll('-', ''))
                                            }else{
                                                'SAPTotalNetValue__c'(s01.SUMME.toString())
                                            }
                                        }
                                }
                                'SalesArea__r'{
                                    'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString() + "-" + iDoc.EDI_DC40.MANDT.toString() + "-" + KUNNR + "-" + VKORG +"-" + VTWEG+ "-" + SPART)
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
                                    'url'("/services/data/v52.0/sobjects/SAPQuoteItem__c/SAPInterfaceId__c/" + iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"','') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"','') + "-" + iDoc.E1EDK01.BELNR.toString().replaceAll('"','') + "-" + edp01.POSEX.toString().replaceAll('"',''))
                                    'referenceId'("QuoteItem")
                                    'body' {
                                        'SAPQuote__r'{
                                            'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"','') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"','') + "-" + iDoc.E1EDK01.BELNR.toString().replaceAll('"',''))
                                        }
                                        'SAPIDocNumber__c'(iDoc.EDI_DC40.DOCNUM.toString().replaceAll('"',''))
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
                                                    if (edp19.IDTNR.toString().length() > 0) {
                                                        'SAPProduct__r' {
                                                            'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"', '') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"', '') + "-" + edp19.IDTNR.toString().replaceAll('"', ''))
                                                        }
                                                    } else {
                                                        'SAPProduct__r'{
                                                            'SAPInterfaceId__c'(iDoc.EDI_DC40.SNDPOR.toString().replaceAll('"','') + "-" + iDoc.EDI_DC40.MANDT.toString().replaceAll('"','') + "-" + "000000000000000000")
                                                        }
                                                    }
                                                    'SAPPositionText__c'(edp19.KTEXT.toString().replaceAll('"', ''))
                                                }
                                        }
                                        'CurrencyIsoCode'(edp01.CURCY.toString().replaceAll('"',''))
                                        'ItemCategory__c'(edp01.PSTYV.toString().replaceAll('"',''))
                                        m = edp01.NETWR.toString().length()
                                        if (edp01.NETWR.toString().drop(m-1) == "-"){
                                            'ItemValue__c'(edp01.NETWR.toString().replaceAll('-', ''))
                                        }else{
                                            'ItemValue__c'(edp01.NETWR.toString())
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
