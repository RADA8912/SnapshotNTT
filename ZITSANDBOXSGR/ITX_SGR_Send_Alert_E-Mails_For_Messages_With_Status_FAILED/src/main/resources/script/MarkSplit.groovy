import groovy.xml.*

String fileContents = new File('ODATA_ALERTING_RESPONSE.xml').text

def message = new XmlSlurper().parse('MessageLog-alerting_test-attachment_1-(Untitled).txt')

def predecessor = "";
def successor = "";

message.entry.content.children().each { data ->
                if((data.IntegrationFlowName == predecessor) || predecessor == "initial") {
                 println("iflowname:" + data.IntegrationFlowName)
                 } else{
                    println("BREAK iflowname:" + data.IntegrationFlowName)
                    data.appendNode{
                        Splitter("true")
                    }
                 }
                predecessor = data.IntegrationFlowName
           }

//new File('test.xml').withWriter('utf-8') { outWriter -> 
//         XmlUtil.serialize( new StreamingMarkupBuilder().bind{ mkp.yield xml }, outWriter )
//      }  

