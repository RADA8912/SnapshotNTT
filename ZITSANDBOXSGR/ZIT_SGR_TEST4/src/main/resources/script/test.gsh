import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap

// add a typed property via script, needed for check in the different parallelized messages after splitter
def Message addTypedProperty(Message message){
    String linesStr = ""
        //read configuration file with pair values
//        String file = this.getClass().getResource('/src/main/resources/parameters.prop').text // geht
//        String file = this.getClass().getResource('/src/main/resources/parameters.propdef').text // geht
//        String file = this.getClass().getResource('/META-INF/MANIFEST.MF').text // geht ist aber nicht das Manifest des IFlows
//        String file = this.getClass().getResource('/metainfo.prop').text // geht nicht
//        String file = this.getClass().getResource('/.project').text // g??

        file.eachLine() { line ->
            linesStr = linesStr + "\n" + line
          
//            def (parameter,value) = line.tokenize( ": ")
//            if (parameter == "Bundle-Name") {
//                message.setProperty("SAP_Mkt_Host",value.replaceAll('\\\\',''))
//                message.setBody(value)
//            }
        }
    
    message.setBody(linesStr)
    message
}