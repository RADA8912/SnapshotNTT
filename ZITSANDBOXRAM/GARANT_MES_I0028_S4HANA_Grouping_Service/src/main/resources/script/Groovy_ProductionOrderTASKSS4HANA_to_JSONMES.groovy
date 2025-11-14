import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonBuilder
import groovy.json.JsonOutput

import java.util.Base64
import groovy.util.XmlSlurper
import java.io.Reader
import java.text.SimpleDateFormat

def Message processData(Message message) {

    Reader reader = message.getBody(Reader)

    def sourceXml = new XmlSlurper().parse(reader)

    def jsonObject = []
    def taskInsert = []

    def grouping = []

    def idocNr = sourceXml.IDOC.EDI_DC40.DOCNUM.text()
    def jobId = sourceXml.IDOC.E1AFKOL.AUFNR.text()

    def auftragsart = sourceXml.IDOC.E1AFKOL.AUTYP.text()
    def bmeng = sourceXml.IDOC.E1AFKOL.BMENGE.toDouble().toInteger()
    def datasupplierId = "SAP "+ sourceXml.IDOC.EDI_DC40.SNDPRN.text()
    def articleVersion = sourceXml.IDOC.E1AFKOL.STLAL.text()
    def planningGroup = sourceXml.IDOC.E1AFKOL.ZPPEXTKO.PLANNING_GROUP.text()
    def taskId

    sourceXml.IDOC.E1AFKOL.E1AFFLL.E1AFVOL.each { idoc ->


        taskId = idoc.VORNR.text()
        def descTask = idoc.LTXA1.text()
        def unitId = idoc.MEINH.text()
        def arbpl = idoc.ARBPL.toString()


        def startEarliest = "${idoc.FSAVD.text()}T${idoc.FSAVZ.text()}"
        def startLatest = "${idoc.SSAVD.text()}T${idoc.SSAVZ.text()}"
        def endEarliest = "${idoc.FSEDD.text()}T${idoc.FSEDZ.text()}"
        def endLatest = "${idoc.SSEDD.text()}T${idoc.SSEDZ.text()}"


        def inputFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss")
        Date stEarliest = inputFormat.parse(startEarliest)
        Date stLatest = inputFormat.parse(startLatest)
        Date edEarliest = inputFormat.parse(endEarliest)
        Date edLatest = inputFormat.parse(endLatest)

        def outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
        def formatstEarliest = outputFormat.format(stEarliest)
        def formatstLatest = outputFormat.format(stLatest)
        def formatedEarliest = outputFormat.format(edEarliest)
        def formatedLatest = outputFormat.format(edLatest)


        def yield = idoc.MGVRG.text()
        def wplace = idoc.ARBPL.text()

        jsonObject.add([
                acronym: "task.id",
                valueList: [taskId]
        ])

        jsonObject.add([
                acronym: "job.id",
                valueList: [jobId]
        ])

        jsonObject.add([
                acronym: "task.description",
                valueList: [descTask]
        ])

        jsonObject.add([
                acronym: "task.plan.unit.id",
                valueList: [unitId]
        ])

        jsonObject.add([
                acronym: "task.datasupplier.id",
                valueList: [datasupplierId]
        ])

        jsonObject.add([
                acronym: "person.id",
                valueList: [99998888]
        ])

        jsonObject.add([
                acronym: "task.plan.start_earliest_ts",
                valueList: [formatstEarliest]
        ])

        jsonObject.add([
                acronym: "task.plan.start_latest_ts",
                valueList: [formatstLatest]
        ])

        jsonObject.add([
                acronym: "task.plan.start_scheduled_ts",
                valueList: [formatstEarliest]
        ])

        jsonObject.add([
                acronym: "task.plan.end_earliest_ts",
                valueList: [formatedEarliest]
        ])

        jsonObject.add([
                acronym: "task.plan.end_latest_ts",
                valueList: [formatedLatest]
        ])

        jsonObject.add([
                acronym: "task.plan.end_scheduled_ts",
                valueList: [formatedEarliest]
        ])

        jsonObject.add([
                acronym: "task.plan.yield",
                valueList: [yield]
        ])

        jsonObject.add([
                acronym: "task.plan.scrap",
                valueList: ["0.000"]
        ])

        def duration = []
        def activ1 = idoc.E1AFVOL2.TO_BE_CONF_ACTIVITY1.text()
        def activ2 = idoc.E1AFVOL2.TO_BE_CONF_ACTIVITY2.text()
        def activ3 = idoc.E1AFVOL2.TO_BE_CONF_ACTIVITY3.text()
        def activ1Float = Float.parseFloat(activ1)
        def activ2Float = Float.parseFloat(activ2)
        def activ3Float = Float.parseFloat(activ3)
        def intAc1 =activ1Float.toInteger()
        def intAc2 =activ2Float.toInteger()
        def intAc3 =activ3Float.toInteger()
        duration.add(intAc1)
        duration.add(intAc2)
        duration.add(intAc3)
        jsonObject.add([
                acronym: "task.duration_seconds",
                valueList: duration
        ])

        jsonObject.add([
                acronym: "task.duration_class.id",
                valueList: ["Setup",
                            "Processing",
                            "Staff" ]
        ])

        jsonObject.add([
                acronym: "responsibilityarea.id",
                valueList: ["SYSTEM"]
        ])

        jsonObject.add([
                acronym: "task.plan.wplace.id",
                valueList: [wplace]
        ])

        jsonObject.add([
                acronym: "task.plan.wplace.group_id",
                valueList: [""]
        ])

        def nacARBPL = arbpl.contains("NAC")
        def lagARBPL = arbpl.contains("LAG")

        if (nacARBPL == true){
            jsonObject.add([
                    acronym: "processing_code.id",
                    valueList: ["NACH" ]
            ])
        }

        if (lagARBPL == true){
            jsonObject.add([
                    acronym: "processing_code.id",
                    valueList: ["LAGER" ]
            ])
        }

        if((lagARBPL == false) && (nacARBPL == false)){

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

        }

        jsonObject.add([
                acronym: "task.detail.payload_format",
                valueList: ["JSON"]
        ])

        def ruek =  idoc.RUEK.text()
        def anzma = idoc.ANZMA.text()
        def anzmaValueFloat = Float.parseFloat(anzma)
        def anzmaValueInt = anzmaValueFloat.toInteger()
        def vgw01 = idoc.VGW01.text()
        def vgw02 = idoc.VGW02.text()
        def vgw03 = idoc.VGW03.text()
        def vge01 = idoc.VGE01.text()
        def vge02 = idoc.VGE02.text()
        def vge03 = idoc.VGE03.text()


        def taskDetailNames = []
        def taskDetailType = []
        def taskDetailValue = []

        if(ruek.length() > 0){
            taskDetailNames.add('Rückmeldenummer')
            taskDetailType.add(1)
            taskDetailValue.add(ruek)
        }

        if(anzma.length() > 0){
            taskDetailNames.add('Anzahlmitarbeiter')
            taskDetailType.add(2)
            taskDetailValue.add(anzmaValueInt)
        }

        if(vgw01.length() > 0){
            taskDetailNames.add('Rüstzeit Vorgabe')
            taskDetailType.add(3)
            taskDetailValue.add(vgw01)
        }

        if(vgw02.length() > 0){
            taskDetailNames.add('Maschinenzeit Vorgabe')
            taskDetailType.add(3)
            taskDetailValue.add(vgw02)
        }

        if(vgw03.length() > 0){
            taskDetailNames.add('Personalzeit Vorgabe')
            taskDetailType.add(3)
            taskDetailValue.add(vgw03)
        }

        if(vge01.length() > 0){
            taskDetailNames.add('Rüstzeit Einheit')
            taskDetailType.add(1)
            taskDetailValue.add(vge01)
        }

        if(vge02.length() > 0){
            taskDetailNames.add('Maschinenzeit Einheit')
            taskDetailType.add(1)
            taskDetailValue.add(vge02)
        }

        if(vge03.length() > 0){
            taskDetailNames.add('Personalzeit Einheit')
            taskDetailType.add(1)
            taskDetailValue.add(vge03)
        }

        def count = 0
        idoc.E1JSTVL.each { e1jstvl ->

            def auftrag = e1jstvl.STAT.text()

            if(auftrag.length() > 0){

                count++
            }


            taskDetailNames.add('Arbeitsgangstatus'+count)
            taskDetailType.add(1)
            taskDetailValue.add(auftrag)
        }


        jsonObject.add([
                acronym: "task.detail.name",
                operator: "IN",
                valueList: taskDetailNames
        ])



        jsonObject.add([
                acronym: "task.detail.data_type",
                operator: "IN",
                valueList: taskDetailType
        ])

        jsonObject.add([
                acronym: "task.detail.value",
                operator: "IN",
                valueList: taskDetailValue

        ])


        def output = [
                serviceParameterList: jsonObject
        ]

        String jsonString = JsonOutput.toJson(output)

        String taskEncoded = Base64.getEncoder().encodeToString(jsonString.bytes)

        //Insert JSON



        taskInsert.add([
                acronym: "staging.category.1.id",
                valueList: ["Job Management"]
        ])

        taskInsert.add([
                acronym: "staging.category.2.id",
                valueList: ["Maintenance"]
        ])

        taskInsert.add([
                acronym: "staging.category.3.id",
                valueList: ["Task"]
        ])

        taskInsert.add([
                acronym: "staging.category.4.id",
                valueList: ["Insert"]
        ])

        taskInsert.add([
                acronym: "staging.data_supplier.id",
                valueList: [datasupplierId]
        ])

        taskInsert.add([
                acronym: "staging.id",
                valueList: ["jm_inbound"]
        ])

        taskInsert.add([
                acronym: "staging.payload.processing_method.id",
                valueList: ["JobManagement.stageInboundProcessing"]
        ])

        taskInsert.add([
                acronym: "staging.keys.data_type",
                operator: "IN",
                valueList: ["char","char","char","char"]
        ])

        taskInsert.add([
                acronym: "staging.keys.name",
                operator: "IN",
                valueList: ["order.id", "transaction.id", "system.id", "task.id"]
        ])

        taskInsert.add([
                acronym: "staging.keys.value_char",
                operator: "IN",
                valueList: [jobId,idocNr,datasupplierId,taskId]
        ])

        taskInsert.add([
                acronym: "staging.keys.value_datetime",
                operator: "IN",
                valueList: [null, null, null, null]
        ])

        taskInsert.add([
                acronym: "staging.keys.value_decimal",
                operator: "IN",
                valueList: [null, null, null, null]
        ])

        taskInsert.add([
                acronym: "staging.keys.value_integer",
                operator: "IN",
                valueList: [null, null, null, null]
        ])

        taskInsert.add([
                acronym: "staging.keys.value_unknown",
                operator: "IN",
                valueList: [null, null, null, null]
        ])

        taskInsert.add([
                acronym: "staging.payload.data",
                valueList: [taskEncoded]
        ])


        // def insertTask = []

        //def builderTaskInsert = new JsonBuilder(insertTask)

        grouping.add(serviceParameterList:taskInsert)

        //Creat Component insert

        idoc.E1RESBL.each { idocCom ->

            def comJsonObject = []
            def comInsert = []

            def piece = idocCom.BDMNG.text()
            def articlID = idocCom.MATNR.text()
            def posi = idocCom.POSNR.text()
            def mengCom = idocCom.BDMNG.toDouble()
            def unitID = idocCom.MEINS.text()
            def berchnung

            if(bmeng != 0.00){
                berchnung = mengCom/bmeng
            }
            else {
                berchnung = 0
            }

            comJsonObject.add([
                    acronym: "job.id",
                    valueList: [jobId]
            ])

            comJsonObject.add([
                    acronym: "task.id",
                    valueList: [taskId]
            ])

            comJsonObject.add([
                    acronym: "article.id",
                    valueList: [articlID]
            ])

            comJsonObject.add([
                    acronym: "component.position",
                    valueList: [posi]
            ])


            comJsonObject.add([
                    acronym: "component.datasupplier.id",
                    valueList: ["SAP"]
            ])

            comJsonObject.add([
                    acronym: "component.plan.yield",
                    valueList: [mengCom.toString()]
            ])

            comJsonObject.add([
                    acronym: "component.plan.scrap",
                    valueList: ["0.000"]
            ])

            comJsonObject.add([
                    acronym: "component.plan.unit.id",
                    valueList: [unitID]
            ])

            comJsonObject.add([
                    acronym: "piece.plan.yield",
                    valueList: [berchnung.toString()]
            ])

            comJsonObject.add([
                    acronym: "piece.plan.scrap",
                    valueList: ["0.000"]
            ])

            comJsonObject.add([
                    acronym: "piece.plan.unit.id",
                    valueList: ["ST"]
            ])

            comJsonObject.add([
                    acronym: "person.id",
                    valueList: [99998888]
            ])


            comJsonObject.add([
                    acronym: "component.detail.payload_format",
                    valueList: ["JSON"]
            ])

            def werk =  idocCom.E1RESBL.WERKS.text()
            def matLong = idocCom.MATNR_LONG.text()
            def charg = idocCom.CHARG.text()
            def lgort = idocCom.LGORT.text()
            def lgnum = idocCom.LGNUM.text()
            def sobkz = idocCom.SOBKZ.text()
            def prvbl = idocCom.PRVBE.text()
            def postp = idocCom.POSTP.text()
            def rsnum = idocCom.RSNUM.text()
            def rspos = idocCom.RSPOS.text()
            def backflush = idocCom.BACKFLUSH.text()
            def maktxRB = idocCom.ZPPEXTRB.MAKTX_RB.text()
            def plan = idocCom.ZPPEXTRB.PLANNING_GROUP_RB.text()
            def warehous = idocCom.ZPPEXTRB.WAREHOUSE_EWM.text()
            def stge = idocCom.ZPPEXTRB.STGE_BIN_EWM.text()


            def comDetailNames = []
            def comDetailType = []
            def comDetailValue = []

            if(werk.length() > 0){
                comDetailNames.add('Werk')
                comDetailType.add(1)
                comDetailValue.add(werk)
            }

            if(matLong.length() > 0){
                comDetailNames.add('Material')
                comDetailType.add(1)
                comDetailValue.add(matLong)
            }

            if(charg.length() > 0){
                comDetailNames.add('Charge')
                comDetailType.add(1)
                comDetailValue.add(charg)
            }

            if(lgort.length() > 0){
                comDetailNames.add('Lagerort')
                comDetailType.add(1)
                comDetailValue.add(lgort)
            }

            if(lgnum.length() > 0){
                comDetailNames.add('Lagernummer')
                comDetailType.add(1)
                comDetailValue.add(lgnum)
            }

            if(sobkz.length() > 0){
                comDetailNames.add('Sonderbestand')
                comDetailType.add(1)
                comDetailValue.add(sobkz)
            }

            if(prvbl.length() > 0){
                comDetailNames.add('PVB')
                comDetailType.add(1)
                comDetailValue.add(prvbl)
            }

            if(rsnum.length() > 0){
                comDetailNames.add('Reservierungsnummer')
                comDetailType.add(1)
                comDetailValue.add(rsnum)
            }

            if(rspos.length() > 0){
                comDetailNames.add('Reservierungsposition')
                comDetailType.add(1)
                comDetailValue.add(rspos)
            }

            if(postp.length() > 0){
                comDetailNames.add('Positionstyp')
                comDetailType.add(1)
                comDetailValue.add(postp)
            }

            if(backflush.length() > 0){
                comDetailNames.add('Retrograd')
                comDetailType.add(1)
                comDetailValue.add(backflush)
            }

            if(maktxRB.length() > 0){
                comDetailNames.add('Materialkurztext')
                comDetailType.add(1)
                if(maktxRB.contains('"')){
                    def maktNew = maktxRB.replaceAll('"', '')
                    comDetailValue.add(maktNew)
                }
                else{
                    comDetailValue.add(maktxRB)
                }

            }

            if(warehous.length() > 0){
                comDetailNames.add('LagernummerEWM')
                comDetailType.add(1)
                comDetailValue.add(warehous)
            }

            if(plan.length() > 0){
                comDetailNames.add('Planungsgruppe')
                comDetailType.add(1)
                comDetailValue.add(plan)
            }

            if(stge.length() > 0){
                comDetailNames.add('Lagerplatz')
                comDetailType.add(1)
                comDetailValue.add(stge)
            }

            def countPeggingdown = 0
            def countPeggingbatch = 0
            idocCom.E1RESBL.ZPPEXTRB.ZPPDSPEGDOWN.each { zppdspegdown ->

                def peggingdown = zppdspegdown.AUFNR_PEGDOWN.text()
                def peggingBatch = zppdspegdown.BATCH_PEGDOWN.text()

                if(peggingdown.length() > 0)  {

                    countPeggingdown++


                    comDetailNames.add('Peggingdown_Aufnr' + countPeggingdown)
                    comDetailType.add(1)
                    comDetailValue.add(peggingdown)

                }

                if(peggingBatch.length() > 0) {

                    countPeggingbatch++

                    comDetailNames.add('Peggingdown_Charge' + countPeggingbatch)
                    comDetailType.add(1)
                    comDetailValue.add(peggingBatch)
                }
            }


            comJsonObject.add([
                    acronym: "component.detail.name",
                    operator: "IN",
                    valueList: comDetailNames
            ])



            comJsonObject.add([
                    acronym: "component.detail.data_type",
                    operator: "IN",
                    valueList: comDetailType
            ])

            comJsonObject.add([
                    acronym: "component.detail.value",
                    operator: "IN",
                    valueList: comDetailValue

            ])

            def outputCom = [
                    serviceParameterList: comJsonObject
            ]

            String comString = JsonOutput.toJson(outputCom)

            String componentEncoded = Base64.getEncoder().encodeToString(comString.bytes)

            //creat Insert Json

            comInsert.add([
                    acronym: "staging.category.1.id",
                    valueList: ["Job Management"]
            ])

            comInsert.add([
                    acronym: "staging.category.2.id",
                    valueList: ["Maintenance"]
            ])

            comInsert.add([
                    acronym: "staging.category.3.id",
                    valueList: ["Component"]
            ])

            comInsert.add([
                    acronym: "staging.category.4.id",
                    valueList: ["Insert"]
            ])

            comInsert.add([
                    acronym: "staging.data_supplier.id",
                    valueList: [datasupplierId]
            ])

            comInsert.add([
                    acronym: "staging.id",
                    valueList: ["jm_inbound"]
            ])

            comInsert.add([
                    acronym: "staging.payload.processing_method.id",
                    valueList: ["JobManagement.stageInboundProcessing"]
            ])

            comInsert.add([
                    acronym: "staging.keys.data_type",
                    operator: "IN",
                    valueList: ["char","char","char","char","char"]
            ])

            comInsert.add([
                    acronym: "staging.keys.name",
                    operator: "IN",
                    valueList: ["order.id","task.id", "transaction.id", "system.id", "article.id"]
            ])

            comInsert.add([
                    acronym: "staging.keys.value_char",
                    operator: "IN",
                    valueList: [jobId,taskId,idocNr,datasupplierId,articlID]
            ])

            comInsert.add([
                    acronym: "staging.keys.value_datetime",
                    operator: "IN",
                    valueList: [null, null, null, null, null]
            ])

            comInsert.add([
                    acronym: "staging.keys.value_decimal",
                    operator: "IN",
                    valueList: [null, null, null, null, null]
            ])

            comInsert.add([
                    acronym: "staging.keys.value_integer",
                    operator: "IN",
                    valueList: [null, null, null, null, null]
            ])

            comInsert.add([
                    acronym: "staging.keys.value_unknown",
                    operator: "IN",
                    valueList: [null, null, null, null, null]
            ])

            comInsert.add([
                    acronym: "staging.payload.data",
                    valueList: [componentEncoded]
            ])

            // def insertComponent = [serviceParameterList:]

            // def comgild =  new JsonBuilder(insertComponent)
            grouping.add(serviceParameterList:comInsert)


        }
    }


    def groupingBuilder = new JsonBuilder(grouping)

    message.setBody(groupingBuilder.toPrettyString())

    return message
}
