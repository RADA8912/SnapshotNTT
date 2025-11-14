import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonBuilder
import groovy.json.JsonOutput


import java.util.Base64
import groovy.util.XmlSlurper
import java.io.Reader
import java.text.SimpleDateFormat



def convertScientificToDouble(String scientificNotation) {
    try {
        // Konvertiere die wissenschaftliche Notation in einen Double-Wert
        return Double.parseDouble(scientificNotation)
    } catch (NumberFormatException e) {
        println "Ungültige Zahl: ${scientificNotation}"
        return null
    }
}

def Message processData(Message message) {

    def properties = message.getProperties();
    def body = properties.get("saveBody");

    Reader reader = message.getBody(Reader)

    def sourceXml = new XmlSlurper().parse(reader)

    def jsonObject = []
    def jobInsert = []
    def grouping = []
    def jobObject = []

    def idocNr = sourceXml.IDOC.EDI_DC40.DOCNUM.text()

    def jobId = sourceXml.IDOC.E1AFKOL.AUFNR.text()
    def auftragsart = sourceXml.IDOC.E1AFKOL.AUTYP.text()
    def bmeng = sourceXml.IDOC.E1AFKOL.BMENGE.text()
    def datasupplierId = "SAP "+ sourceXml.IDOC.EDI_DC40.SNDPRN.text()

    sourceXml.IDOC.each { idoc ->
        def jobType = idoc.E1AFKOL.AUART.text()
        def articleId = idoc.E1AFKOL.MATNR.text()
        def unitId = idoc.E1AFKOL.BMEINS.text()

        def startEarliest = "${idoc.E1AFKOL.GSTRP.text()}T${idoc.E1AFKOL.GSUZP.text()}"
        def startScheduled = "${idoc.E1AFKOL.GSTRS.text()}T${idoc.E1AFKOL.GSUZS.text()}"
        def endLatest = "${idoc.E1AFKOL.GLTRP.text()}T${idoc.E1AFKOL.GLUZP.text()}"
        def endScheduled = "${idoc.E1AFKOL.GLTRS.text()}T${idoc.E1AFKOL.GLUZS.text()}"

        def inputFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss")
        Date stEatliest = inputFormat.parse(startEarliest)
        Date stScheduled = inputFormat.parse(startScheduled)
        Date endLate = inputFormat.parse(endLatest)
        Date edScheduled = inputFormat.parse(endScheduled)

        def outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
        def formatstEarliest = outputFormat.format(stEatliest)
        def formatstSchedul = outputFormat.format(stScheduled)
        def formatendLatest = outputFormat.format(endLate)
        def formatedScheduled = outputFormat.format(edScheduled)


        def yield = idoc.E1AFKOL.GAMNG.text()
        def scrap = idoc.E1AFKOL.GASMG.text()
        def planningGroup = idoc.E1AFKOL.ZPPEXTKO.PLANNING_GROUP.text()

        jsonObject.add([
                acronym: "job.id",
                valueList: [jobId]
        ])

        jsonObject.add([
                acronym: "jobtype.id",
                valueList: [jobType]
        ])

        jsonObject.add([
                acronym: "article.id",
                valueList: [articleId]
        ])

        jsonObject.add([
                acronym: "job.plan.unit.id",
                valueList: [unitId]
        ])


        jsonObject.add([
                acronym: "job.datasupplier.id",
                valueList: [datasupplierId]
        ])

        jsonObject.add([
                acronym: "job.plan.start_earliest_ts",
                valueList: [formatstEarliest]
        ])

        jsonObject.add([
                acronym: "job.plan.start_scheduled_ts",
                valueList: [formatstSchedul]
        ])

        jsonObject.add([
                acronym: "job.plan.end_latest_ts",
                valueList: [formatendLatest]
        ])
        
        if (planningGroup.length() > 0){
            jsonObject.add([
                    acronym: "processing_code.id",
                    valueList: [planningGroup ]
            ])
        }
        else{
            jsonObject.add([
                    acronym: "processing_code.id",
                    valueList: ["SYSTEM" ]
            ])
        }
        
        
        jsonObject.add([
                acronym: "job.plan.end_scheduled_ts",
                valueList: [formatedScheduled]
        ])

        jsonObject.add([
                acronym: "job.plan.yield",
                valueList: [yield]
        ])

        jsonObject.add([
                acronym: "job.plan.scrap",
                valueList: [scrap]
        ])

        jsonObject.add([
                acronym: "responsibilityarea.id",
                valueList: ["SYSTEM"]
        ])

        jsonObject.add([
                acronym: "person.id",
                valueList: [99998888]
        ])

        jsonObject.add([
                acronym: "job.detail.payload_format",
                valueList: ["XML"]
        ])
        def detailType = []
        def detailNames = []
        def detailValue = []
        def name
        def discrip
        def value
        def longName

        def autyp = idoc.E1AFKOL.AUTYP.text()
        def werks = idoc.E1AFKOL.WERKS.text()
        def pruflos = idoc.E1AFKOL.PRUEFLOS.text()
        def matnrLong = idoc.E1AFKOL.MATNR_LONG.text()
        def fevor = idoc.E1AFKOL.FEVOR.text()
        def dispo = idoc.E1AFKOL.DISPO.text()
        def cy = idoc.E1AFKOL.CY_SEQNR.text()
        def ean11 = idoc.E1AFKOL.ZPPEXTKO.EAN11.text()
        def arktx = idoc.E1AFKOL.ZPPEXTKO.ARKTX.text()
        def maktx = idoc.E1AFKOL.ZPPEXTKO.MAKTX.text()
        def kdmat = idoc.E1AFKOL.ZPPEXTKO.KDMAT.text()
        def numPoBlock = idoc.E1AFKOL.ZPPEXTKO.NUM_PO_BLOCK.text()
        def lgort = idoc.E1AFKOL.E1AFPOL.LGORT.text()
        def matnr = idoc.E1AFKOL.E1AFPOL.MATNR.text()
        def meins = idoc.E1AFKOL.E1AFPOL.MEINS.text()
        def psmng = idoc.E1AFKOL.E1AFPOL.PSMNG.text()
        def charg = idoc.E1AFKOL.E1AFPOL.CHARG.text()
        def lgum = idoc.E1AFKOL.E1AFPOL.LGNUM.text()
        def kdauf = idoc.E1AFKOL.E1AFPOL.KDAUF.text()
        def kdpos = idoc.E1AFKOL.E1AFPOL.KDPOS.text()
        def kunag = idoc.E1AFKOL.E1AFPOL.KUNAG.text()
        def name1 = idoc.E1AFKOL.E1AFPOL.NAME1.text()
        def psmngFloat = Float.parseFloat(psmng)
        def psmngInt = psmngFloat.toInteger()



        if(werks.length() > 0){
            detailNames.add('Werk')
            detailType.add(2)
            detailValue.add(werks.toInteger())
        }
        
        if(name1.length() > 0){
            detailNames.add('Kundenname')
            detailType.add(1)
            detailValue.add(name1)
        }

        if(kunag.length() > 0){
            detailNames.add('Kundennummer')
            detailType.add(1)
            detailValue.add(kunag)
        }

        if(kdauf.length() > 0){
            detailNames.add('Kundenauftrag')
            detailType.add(1)
            detailValue.add(kdauf)
        }

        if(kdpos.length() > 0){
            detailNames.add('Kundenauftragsposition')
            detailType.add(2)
            detailValue.add(kdpos.toInteger())
        }

        if(pruflos.length() > 0){
            detailNames.add('Prueflos')
            detailType.add(2)
            detailValue.add(pruflos.toLong())
        }

        if(matnrLong.length() > 0){
            detailNames.add('Materialnummer_lang')
            detailType.add(1)
            detailValue.add(matnrLong)
        }

        if(fevor.length() > 0){
            detailNames.add('Fertigungssteuerer')
            detailType.add(1)
            detailValue.add(fevor)
        }

        if(dispo.length() > 0){
            detailNames.add('Disponent')
            detailType.add(1)
            detailValue.add(dispo)
        }

        if(cy.length() > 0){
            detailNames.add('Sequenznummer')
            detailType.add(2)
            detailValue.add(cy.toLong())
        }

        if(lgort.length() > 0){
            detailNames.add('Lagerort')
            detailType.add(1)
            detailValue.add(lgort)
        }

        if(matnr.length() > 0){
            detailNames.add('Materialnummer')
            detailType.add(1)
            detailValue.add(matnr)
        }

        if(meins.length() > 0){
            detailNames.add('Einheit')
            detailType.add(1)
            detailValue.add(meins)
        }

        if(psmng.length() > 0){
            detailNames.add('Sollmenge')
            detailType.add(2)
            detailValue.add(psmngInt)
        }

        if(charg.length() > 0){
            detailNames.add('Charge')
            detailType.add(1)
            detailValue.add(charg)
        }

        if(lgum.length() > 0){
            detailNames.add('Lagernummer')
            detailType.add(1)
            detailValue.add(lgum)
        }

        def count = 0
        idoc.E1AFKOL.E1JSTKL.each { e1jstkl ->

            def auftrag = e1jstkl.STAT.text()

            if(auftrag.length() > 0){

                count++
            }

            detailNames.add('Auftragsstatus'+count)
            detailType.add(1)
            detailValue.add(auftrag)
        }

        if(ean11.length() > 0){
            detailNames.add('EAN11')
            detailType.add(1)
            detailValue.add(ean11)
        }

        if(arktx.length() > 0){
            detailNames.add('Positionstext')
            detailType.add(1)
            detailValue.add(arktx)
        }

        if(maktx.length() > 0){
            detailNames.add('Materialkurztext')
            detailType.add(1)
            detailValue.add(maktx)
        }

        if(kdmat.length() > 0){
            detailNames.add('Kundenmaterialnummer')
            detailType.add(1)
            detailValue.add(kdmat)
        }

        if(numPoBlock.length() > 0){
            detailNames.add('Blockauftragsmenge')
            detailType.add(2)
            detailValue.add(numPoBlock.toInteger())
        }

        if(autyp.length() > 0){
            detailNames.add('Auftragsart')
            detailType.add(2)
            detailValue.add(autyp.toInteger())
        }

        if(planningGroup.length() > 0){
            detailNames.add('Plangruppe')
            detailType.add(1)
            detailValue.add(planningGroup)
        }



        def countPeggingup = 0
        def countPeggingupSequ = 0
        idoc.E1AFKOL.ZPPEXTKO.ZPPDSPEGUP.each { zppdspegup ->

            def peggingup = zppdspegup.AUFNR_PEGUP.text()
            def peggingSequ = zppdspegup.CY_SEQNR_PEGUP.text()

            if(peggingup.length() > 0)  {

                countPeggingup++


                detailNames.add('Peggingup_Aufnr' + countPeggingup)
                detailType.add(1)
                detailValue.add(peggingup)

            }

            if(peggingSequ.length() > 0) {

                peggingSequ++

                detailNames.add('Peggingup_Seqnr' + countPeggingup)
                detailType.add(1)
                detailValue.add(peggingSequ)
            }
        }


        idoc.E1AFKOL.E1VCCHR.each{ detaiName ->

           def type = detaiName.ATFOR.text()
            name = detaiName.ATNAM.text()
            value = detaiName.ATFOR.text()
            discrip = detaiName.E1VCCHT.ATBEZ.text()
            longName = detaiName.E1VCCHT.ATWTB.text()
            

            def Integer numberType
            if(type.length() > 0) {
                switch (type) {
                    case "CHAR":
                        numberType = 1
                        break

                    case "NUM":
                        numberType = 3
                        break
                }

            }



            if(discrip.length() > 0){
                name = name + "|"+ "DES"
                detailNames.add(name)
                detailType.add(1)
                name = detaiName.ATNAM.text()
            }

            if(value.length() > 0){
                name = name + "|"+ "VAL"
                detailNames.add(name)
                detailType.add(numberType)
                name = detaiName.ATNAM.text()
            }

            if(longName.length() > 0){
                name = name + "|"+ "LONG"
                detailNames.add(name)
                detailType.add(1)
                name = detaiName.ATNAM.text()
            }

        }
        jsonObject.add([
                acronym: "job.detail.name",
                operator: "IN",
                valueList: detailNames + 'Zusatzdaten'
        ])



        jsonObject.add([
                acronym: "job.detail.data_type",
                operator: "IN",
                valueList: detailType + 10
        ])


        def encodedXml = body.bytes.encodeBase64().toString()


        idoc.E1AFKOL.E1VCCHR.each{ besch ->
            name = besch.E1VCCHT.ATBEZ.text()
            def type = besch.ATFOR.text()
            longName = besch.E1VCCHT.ATWTB_LONG.text()
            value = besch.ATWRT.text()


            if(name.length() > 0) {
                detailValue.add(name)
            }
            if(type == 'NUM'){
                def valueWSW = besch.ATFLV.toString()
                
                value = convertScientificToDouble(valueWSW)
                detailValue.add(value)

            }else {
                detailValue.add(value)
            }

            if(longName.length() > 0){

                detailValue.add(longName)
            }

        }
        jsonObject.add([
                acronym: "job.detail.value",
                operator: "IN",
                valueList: detailValue + encodedXml

        ])
    }

    def output = [
            serviceParameterList: jsonObject
    ]

    String jsonString = JsonOutput.toJson(output)

    String jobEncoded = Base64.getEncoder().encodeToString(jsonString.bytes)

    jobInsert.add([
            acronym: "staging.category.1.id",
            valueList: ["Job Management"]
    ])

    jobInsert.add([
            acronym: "staging.category.2.id",
            valueList: ["Maintenance"]
    ])

    jobInsert.add([
            acronym: "staging.category.3.id",
            valueList: ["Job"]
    ])

    jobInsert.add([
            acronym: "staging.category.4.id",
            valueList: ["Insert"]
    ])

    jobInsert.add([
            acronym: "staging.data_supplier.id",
            valueList: [datasupplierId]
    ])

    jobInsert.add([
            acronym: "staging.id",
            valueList: ["jm_inbound"]
    ])

    jobInsert.add([
            acronym: "staging.payload.processing_method.id",
            valueList: ["JobManagement.stageInboundProcessing"]
    ])

    jobInsert.add([
            acronym: "staging.keys.data_type",
            operator: "IN",
            valueList: ["char","char","char"]
    ])

    jobInsert.add([
            acronym: "staging.keys.name",
            operator: "IN",
            valueList: ["order.id", "transaction.id", "system.id"]
    ])

    jobInsert.add([
            acronym: "staging.keys.value_char",
            operator: "IN",
            valueList: [jobId,idocNr,datasupplierId]
    ])

    jobInsert.add([
            acronym: "staging.keys.value_datetime",
            operator: "IN",
            valueList: [null, null, null]
    ])

    jobInsert.add([
            acronym: "staging.keys.value_decimal",
            operator: "IN",
            valueList: [null, null, null]
    ])

    jobInsert.add([
            acronym: "staging.keys.value_integer",
            operator: "IN",
            valueList: [null, null, null]
    ])

    jobInsert.add([
            acronym: "staging.keys.value_unknown",
            operator: "IN",
            valueList: [null, null, null]
    ])

    jobInsert.add([
            acronym: "staging.payload.data",
            valueList: [jobEncoded]
    ])

    jobObject.add(serviceParameterList:jobInsert)
    
    def groupingBuilder = new JsonBuilder(jobObject)

    message.setBody(groupingBuilder.toPrettyString())
    
    return message
}
