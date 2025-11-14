/*
Set Filter Date Time To Header
 */
import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap

def Message processData(Message message) {
       //Create DateStart and DateEnd
        use(groovy.time.TimeCategory) {
            def dateNew = new Date() - 1.day
            filterDateStart = dateNew.format("yyyy-MM-dd'T'00:00:00.000")
            filterDateEnd = dateNew.format("yyyy-MM-dd'T'23:59:59.999")
        }
       
       //Set Value To Header
       message.setHeader("FilterDateStart", filterDateStart)
       message.setHeader("FilterDateEnd", filterDateEnd)
       
       return message;
}