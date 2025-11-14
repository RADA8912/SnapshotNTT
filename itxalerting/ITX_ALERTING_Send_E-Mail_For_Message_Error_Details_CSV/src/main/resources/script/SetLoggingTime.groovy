import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import groovy.time.TimeCategory


def Message processData(Message message) {

    def headers = message.getHeaders()
    int scheduledMinutes = headers.get("Minutes").toInteger()
    def currentDate = new Date()
    MailDate = (currentDate.format("dd-MM-yyyy"))
    LogEnd = (currentDate.format("yyyy-MM-dd'T'HH:mm:ss"))
    MailEndTime = (currentDate.format("HH:mm"))
    
    use( TimeCategory ) {
       logStart = currentDate - scheduledMinutes.minutes
    }
    
    LogStart = (logStart.format("yyyy-MM-dd'T'HH:mm:ss"))
    MailStartTime = (logStart.format("HH:mm"))
    
    // Set Properties
    message.setProperty("logStart", LogStart)
    message.setProperty("logEnd", LogEnd)
    message.setProperty("mailStartTime", MailStartTime)
    message.setProperty("mailEndTime", MailEndTime)
    message.setProperty("mailDate", MailDate)
    
    return message
}