import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap

def Message processData(Message message) {

       URLvalue = message.getBody().getText("UTF-8")
       URLvalue = URLvalue.toString()

       Start = URLvalue.indexOf('<meta content="/images')
       Ende = URLvalue.indexOf('itemprop="image">')

       imageURL = URLvalue.substring(Start+15, Ende-2)

       message.setBody(URLvalue)
       message.setHeader("URL", imageURL.toString())
       return message;
}