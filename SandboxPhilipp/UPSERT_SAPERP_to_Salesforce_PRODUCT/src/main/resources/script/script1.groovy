package UPSERT_SAPERP_to_Salesforce_CASE.src.main.resources.script

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import java.text.SimpleDateFormat


Message processData(Message message) {

    //Accessmessagebodyandproperties
    Reader reader = message.getBody(Reader)
    Map properties = message.getProperties()

    //DefineXMLparserandbuilder
    def SAPSalesOrder = new XmlSlurper().parse(reader)
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)

    //Definetargetpayloadmapping
    builder.Case {
        //
        def validIdoc = SAPSalesOrder.IDOC.E1NTHDR.findAll {
            iDoc ->
                // Filter
                iDoc != ''
        }

        //
        validIdoc.each {
            iDoc ->
                //
                message.setProperty("SAPInterfaceID__c", SAPSalesOrder.IDOC.EDI_DC40.SNDPOR.toString() + "-" + SAPSalesOrder.IDOC.EDI_DC40.MANDT.toString() + "-" + iDoc.QMNUM.toString())
                'SAPIDocNumber__c'(SAPSalesOrder.IDOC.EDI_DC40.DOCNUM)
                'SAPSystemID__c'(SAPSalesOrder.IDOC.EDI_DC40.SNDPOR)
                'SAPClient__c'(SAPSalesOrder.IDOC.EDI_DC40.MANDT)
                'SAPNotificationNumber__c'(iDoc.QMNUM)
                'SAPNotificationType__c'(iDoc.QMART)
                'Subject'(iDoc.QMTXT)
                'Language__c'(iDoc.NOTIF_LANGU)
                String oldDate1 = iDoc.QMDAT.toString() + "-" + iDoc.MZEIT.toString()
                Date date1 = new SimpleDateFormat("yyyyMMdd-HHmmss").parse(oldDate1)
                String newDate1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+02:00'").format(date1)
                //'CreatedDate'(newDate1)
                'Priority'(iDoc.PRIOK)
                if (iDoc.STTXT.toString().contains('MMAB') == true){
                    'Status'('Closed')
                } else {
                    if (iDoc.STTXT.toString().contains('CUST') == true){
                        'Status'('Customer notification pending')
                    } else {
                        'Status'('Open')
                    }
                }
                def validSegment = iDoc.ZQNOTIF.findAll {
                    Segment ->
                        Segment != ''
                }
                validSegment.each {
                    Segment ->
                        'SAPDistributionChannel__c'(Segment.VTWEG)
                        'SAPDivision__c'(Segment.SPART)
                        'SAPSalesOrganization__c'(Segment.VKORG)
                        if (Segment.MATNR.toString().length() > 0) {
                            'Product__r' {
                                'SAPInterfaceID__c'(SAPSalesOrder.IDOC.EDI_DC40.SNDPOR.toString() + "-" + SAPSalesOrder.IDOC.EDI_DC40.MANDT.toString() + "-" + Segment.MATNR)
                            }
                        }
                        'SAPBatch__c'(Segment.CHARG)
                        'SAPCode__c	'(Segment.QMGRP.toString() +"-"+Segment.QMCOD.toString())
                        'SAPSalesOrder__c'(Segment.LS_KDAUF)
                        'SAPSalesOrderPosition__c'(Segment.LS_KDPOS)
                        'SAPDelivery__c'(Segment.LS_VBELN)
                        'SAPDeliveryPosition__c'(Segment.LS_POSNR.toString())
                        'Account'{
                            'SAPInterfaceID__c'(SAPSalesOrder.IDOC.EDI_DC40.SNDPOR.toString() + "-" + SAPSalesOrder.IDOC.EDI_DC40.MANDT.toString() + "-" + Segment.PARNR_AG)
                        }
                        if (Segment.PARNR_AP.toString().length() > 0) {
                            'Contact' {
                                'SAPInterfaceID__c'(SAPSalesOrder.IDOC.EDI_DC40.SNDPOR.toString() + "-" + SAPSalesOrder.IDOC.EDI_DC40.MANDT.toString() + "-" + Segment.PARNR_AP)
                            }
                        }
                        if (Segment.PARNR_Z1.toString().length() > 0) {
                            'SAPResponsibleSalesRep__r' {
                                'SAPInterfaceID__c'(SAPSalesOrder.IDOC.EDI_DC40.SNDPOR.toString() + "-" + SAPSalesOrder.IDOC.EDI_DC40.MANDT.toString() + "-" + Segment.PARNR_Z1)
                            }
                        }
                        if (Segment.CUSTINFO_ERDAT.toString() != '00000000'){
                            String oldDate2 = Segment.CUSTINFO_ERDAT.toString()
                            Date date2 = new SimpleDateFormat("yyyyMMdd").parse(oldDate2)
                            String newDate2 = new SimpleDateFormat("yyyy-MM-dd").format(date2)
                            'SolutionProposalDate__c'(newDate2)
                        }
                        String oldDate3 = Segment.QMDAB.toString()+"-"+Segment.QMZAB.toString()
                        Date date3 = new SimpleDateFormat("yyyyMMdd-HHmmss").parse(oldDate3)
                        String newDate3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+02:00'").format(date3)
                        //'ClosedDate'(newDate3)
                }
                notfication =
                        'Description'(iDoc.E1ORHDR_LTXT.TDLINE.toString().drop(2).replaceAll('\\*', '').replaceAll('  ', ' '))
                def validSegment2 = iDoc.E1NTITM.findAll {
                    Segment2 ->
                        Segment2 != ''
                }
                validSegment2.each {
                    Segment2 ->
                        if (Segment2.URGRP.toString() == "Q1-QK02") {
                            'SAPSolutionCategory__c'(Segment2.URCOD)
                        }
                }
                'SolutionDescription__c'(iDoc.E1NTITM.E1NTITM_CAUSE_LTXT.TDLINE.toString().drop(2).replaceAll('\\*', '').replaceAll('  ', ' '))
        }
    }

    //Generateoutput
    message.setBody(writer.toString())
    return message
}
