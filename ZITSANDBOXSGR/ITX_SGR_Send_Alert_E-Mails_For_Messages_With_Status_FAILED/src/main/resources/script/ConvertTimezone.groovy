import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import static java.util.Calendar.*
import java.util.Map;
import com.sap.it.api.mapping.*
import com.sap.it.api.mapping.MappingContext
import java.text.SimpleDateFormat
import java.util.Date

def Message processData(Message message) {   
    
    def headers = message.getHeaders()
    def inputDateStart = headers.get("LogStart")
    def inputDateEnd = headers.get("LogEnd")
    def inputDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    def dateStart = new SimpleDateFormat(inputDateFormat).parse(inputDateStart)
    def dateEnd = new SimpleDateFormat(inputDateFormat).parse(inputDateEnd)

    // Convert Timezone
    def outputDateFormat = 'dd-MM-yyyy HH:mm:ss z'
    def localTimeZone = TimeZone.getTimeZone('Europe/Berlin')

    message.setHeader("LogStart", dateStart.format(outputDateFormat, localTimeZone))
    message.setHeader("LogEnd", dateEnd.format(outputDateFormat, localTimeZone))
    
    //Get tenant ID
	String appUrl = System.getenv("HC_APPLICATION_URL")
	appId = appUrl.substring(8, appUrl.indexOf("ifl"))
    
    message.setHeader("TenantID", appId)
    
    return message
}