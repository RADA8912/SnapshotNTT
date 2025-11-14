import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonBuilder
import java.util.Base64
import groovy.util.XmlSlurper
import java.io.Reader
import java.text.SimpleDateFormat

def Message processData(Message message) {

    Reader reader = message.getBody(Reader)

    def sourceXml = new XmlSlurper().parse(reader)

   def jsonObject = []
    def jobId = sourceXml.IDOC.E1AFKOL.AUFNR.text()
    def bmeng = sourceXml.IDOC.E1AFKOL.BMENGE.toDouble()

    sourceXml.IDOC.E1AFKOL.E1AFFLL.each { idoc ->
        def taskId = idoc.E1AFVOL.VORNR.text()

        def piece = idoc.E1AFVOL.E1RESBL.BDMNG.text()
        def articlID = idoc.E1AFVOL.E1RESBL.MATNR.text()
        def posi = idoc.E1AFVOL.E1RESBL.POSNR.text()
        def yield = idoc.E1AFVOL.E1RESBL.BDMNG.text()
        def unitID = idoc.E1AFVOL.E1RESBL.MEINS.text()
        def berchnung
        if(bmeng != 0){
           berchnung = yield.toDouble()/bmeng
        }
        else {
            berchnung = 0
        }


        jsonObject.add([
                acronym: "job.id",
                valueList: [jobId]
        ])

        jsonObject.add([
                acronym: "task.id",
                valueList: [taskId]
        ])

        jsonObject.add([
                acronym: "article.id",
                valueList: [articlID]
        ])

        jsonObject.add([
                acronym: "component.position",
                valueList: [posi]
        ])


        jsonObject.add([
                acronym: "component.datasupplier.id",
                valueList: ["SAP"]
        ])

        jsonObject.add([
                acronym: "component.plan.yield",
                valueList: [yield]
        ])

        jsonObject.add([
                acronym: "component.plan.scrap",
                valueList: ["0.000"]
        ])

        jsonObject.add([
                acronym: "component.plan.unit.id",
                valueList: [unitID]
        ])

        jsonObject.add([
                acronym: "piece.plan.yield",
                valueList: [berchnung.toString()]
        ])

        jsonObject.add([
                acronym: "piece.plan.scrap",
                valueList: ["0.000"]
        ])

        jsonObject.add([
                acronym: "piece.plan.unit.id",
                valueList: ["ST"]
        ])

        jsonObject.add([
                acronym: "person.id",
                valueList: [99998888]
        ])



        jsonObject.add([
                acronym: "component.detail.payload_format",
                valueList: ["JSON"]
        ])

        def werk =  idoc.E1AFVOL.E1RESBL.WERKS.text()
        def matLong = idoc.E1AFVOL.E1RESBL.MATNR_LONG.text()
        def charg = idoc.E1AFVOL.E1RESBL.CHARG.text()
        def lgort = idoc.E1AFVOL.E1RESBL.LGORT.text()
        def lgnum = idoc.E1AFVOL.E1RESBL.LGNUM.text()
        def sobkz = idoc.E1AFVOL.E1RESBL.SOBKZ.text()
        def prvbl = idoc.E1AFVOL.E1RESBL.PRVBE.text()
        def postp = idoc.E1AFVOL.E1RESBL.POSTP.text()
        def rsnum = idoc.E1AFVOL.E1RESBL.RSNUM.text()
        def rspos = idoc.E1AFVOL.E1RESBL.RSPOS.text()
        def backflush = idoc.E1AFVOL.E1RESBL.BACKFLUSH.text()
        def maktxRB = idoc.E1AFVOL.E1RESBL.ZPPEXTRB.MAKTX_RB.text()
        def plan = idoc.E1AFVOL.E1RESBL.ZPPEXTRB.PLANNING_GROUP_RB.text()
        def warehous = idoc.E1AFVOL.E1RESBL.ZPPEXTRB.WAREHOUSE_EWM.text()
        def stge = idoc.E1AFVOL.E1RESBL.ZPPEXTRB.STGE_BIN_EWM.text()


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
        idoc.E1AFVOL.E1RESBL.ZPPEXTRB.ZPPDSPEGDOWN.each { zppdspegdown ->

            def peggingdown = zppdspegdown.AUFNR_PEGDOWN.text()
            def peggingBatch = zppdspegdown.BATCH_PEGDOWN.text()

            if(peggingdown.length() > 0)  {

                countPeggingdown++


                detailNames.add('Peggingdown_Aufnr' + countPeggingdown)
                detailType.add(1)
                detailValue.add(peggingdown)

            }

            if(peggingBatch.length() > 0) {

                countPeggingbatch++

                detailNames.add('Peggingdown_Charge' + countPeggingbatch)
                detailType.add(1)
                detailValue.add(peggingBatch)
            }
        }


        jsonObject.add([
                acronym: "component.detail.name",
                operator: "IN",
                valueList: comDetailNames
        ])



        jsonObject.add([
                acronym: "component.detail.data_type",
                operator: "IN",
                valueList: comDetailType
        ])

        jsonObject.add([
                acronym: "component.detail.value",
                operator: "IN",
                valueList: comDetailValue

        ])
    }

    def output = [
            serviceParameterList: jsonObject
    ]

    def builder = new JsonBuilder(output)
    message.setBody(builder)

    return message
}
