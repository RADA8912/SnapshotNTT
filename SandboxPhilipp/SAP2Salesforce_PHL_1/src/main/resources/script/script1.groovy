package script

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
    def Account = new XmlSlurper().parse(reader)
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)

    //Definetargetpayloadmapping
    builder.Account {
        //DefineForEachCompoundEmployee
        def validIdoc = Account.IDOC.findAll {
            iDoc ->


                // Filter
               iDoc != ''
        }

        //IterateeachCompoundEmployee
        validIdoc.each {
            iDoc ->
                        message.setHeader("SAPInterfaceID__c", iDoc.EDI_DC40.SNDPOR.toString() + "-" + iDoc.EDI_DC40.MANDT.toString() + "-" + iDoc.E1KNA1M.KUNNR.toString())
                        'Name'(iDoc.E1KNA1M.NAME1)
                        'Id'('') //coming from SAP, new field
                        'SAPSystemID__c'(iDoc.EDI_DC40.SNDPOR)
                        'SAPClient__c'(iDoc.EDI_DC40.MANDT) //Lookup
                        'IDocNumber__c'(iDoc.EDI_DC40.DOCNUM)
                        'SAPAccountID__c'(trimZeroLeft(iDoc.E1KNA1M.KUNNR.toString())) //without leading zeros
                        'GlobalLocationNumber__c'(iDoc.E1KNA1M.BBBNR.toString() + iDoc.E1KNA1M.BBSNR.toString() + iDoc.E1KNA1M.BUBKZ.toString())
                        'Name'(iDoc.E1KNA1M.NAME1)
                        'Name2__c'(iDoc.E1KNA1M.NAME2)
                        'Name3__c'(iDoc.E1KNA1M.NAME3)
                        'BillingStreet'(iDoc.E1KNA1M.STRAS)
                        'BillingCity'(iDoc.E1KNA1M.ORT01)
                        'BillingPostalCode'(iDoc.E1KNA1M.PSTLZ)
                        'BillingCountryCode'(iDoc.E1KNA1M.LAND1)
                        'Phone'(iDoc.E1KNA1M.TELF1)
                        'SAPLanguage__c'(iDoc.E1KNA1M.SPRASISO) //Lookup
                        if (iDoc.E1KNA1M.LOEVM.toString() == "X") {
                            'SAPDeletionFlag__c'(true)
                        } else {
                            'SAPDeletionFlag__c'(false)
                        }
                        'RecordType'(iDoc.E1KNA1M.E1KNVVM.KTOKD) //Constant Lookup: Logic to be defined by KTOKD
                        //'SAPInterfaceID__c'(iDoc.EDI_DC40.SNDPOR.toString() + "-" + iDoc.EDI_DC40.MANDT.toString() + "-" + iDoc.E1KNA1M.KUNNR.toString())
                        'SAP_Division__c'(iDoc.E1KNA1M.E1KNVVM.SPART)
                        'SAP_Sales_Channel__c'(iDoc.E1KNA1M.E1KNVVM.VTWEG)
                        'SAPDivision__c'(iDoc.E1KNA1M.E1KNVVM.SPART)
                        'SAPSalesChannel__c'(iDoc.E1KNA1M.E1KNVVM.VTWEG)
                        'SAPSalesOrg__c'(iDoc.E1KNA1M.E1KNVVM.VKORG)
                        'SAPCustomerGroup__c'(iDoc.E1KNA1M.E1KNVVM.KDGRP)
                        'IncoTerms__c'(iDoc.E1KNA1M.E1KNVVM.INCO1)
                        'DeliveryPriority__c'(iDoc.E1KNA1M.E1KNVVM.LPRIO)
                        'DeliveryTerms__c'(iDoc.E1KNA1M.E1KNVVM.VSBED)
                        'PaymentTerms__c'(iDoc.E1KNA1M.E1KNVVM.ZTERM)
                        'CurrencyIsoCode'(iDoc.E1KNA1M.E1KNVVM.WAERS)
                        'UserEmail__c'(iDoc.E1KNA1M.ZZSFDC.EMAIL) //TBD Schuelke

        }

    }

    //Generateoutput
    message.setBody(writer.toString())
    return message
}
