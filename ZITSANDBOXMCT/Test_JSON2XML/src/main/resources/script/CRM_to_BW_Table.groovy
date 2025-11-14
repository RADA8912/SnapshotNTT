import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    
import java.text.SimpleDateFormat 


def Message processData(Message message) {

    Reader reader = message.getBody(Reader)
    def input = new XmlSlurper().parse(reader)
    Writer writer = new StringWriter()
    def indentPrinter = new IndentPrinter(writer, '    ')
    def builder = new MarkupBuilder(indentPrinter)
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd")        
    LocalDateTime now = LocalDateTime.now()
    String time = dtf.format(now)
    DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")         
    Date date = new Date().plus(-30)
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    SimpleDateFormat output = new SimpleDateFormat("yyyyMMdd")


    //  set url parameters        
    String to = dtf2.format(now)          
    String from = date.format("yyyy-MM-dd").toString()     
    
    // build request
    builder.'dataTable' {
            def entry = input.element 
                entry.each { elements ->  
                    dataRow {
                        'IMPORTDATE_KEY' time        
                        'AGENCYID' elements.agent.number      
                        'AGENCYNAME' elements.agent.name
                        'BONUS' elements.agent.bonus
                        'BONUSCUR' ('EUR')
                        'CONTRACT' elements.quote.offerNumberSap                                                                                           
                        createdat = output.format(sdf.parse ((elements.quote.createdAt).toString()))                          
                        'CREATEDAT' createdat                        
                    }
                }         
    }
    message.setProperty("from", from)
    message.setProperty("to", to)
    message.setBody(writer.toString())
    return message
}