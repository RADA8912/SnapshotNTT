import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.xml.*
import groovy.xml.XmlUtil

/**
 * This method stores important information for the monitoring of the iFlow. Business Partner, as well as names and the
 * payload is stored so that is visible within the monitor of the Cloud Integration
 * @param message
 * @return message
 */


def Message processData(Message message) {
    //Retrieve the body and parse the message to an XMLParser
    def body = message.getBody(Reader)
    def rootNode = new XmlParser().parse(body)

 
    
    //E1IDKU1-BGMREF: The next IDoc also needs an individual number as this becomes the payment advice number.
    // BigInteger oldLongBGMRef = new BigInteger(message.getProperties().get('BGMRef'))
    // def longBGMRef = oldLongBGMRef + message.getProperties().get('CamelSplitIndex')
    // rootNode.IDOC.E1IDKU1.BGMREF.replaceNode({
    //         BGMREF(longBGMRef)
    // })
    
    // Replaced the logic to be mapped to new Stripe Property according to SAPS-39
    def splitIndex = message.getProperties().get('CamelSplitIndex')
    rootNode.IDOC.E1IDKU1.BGMREF.replaceNode({
            BGMREF(rootNode.IDOC.E1IDKU1.BGMREF.text() + "-" + splitIndex )
    })    
    

    //E1IDKU5-MOABETR and E1IDLU5-MOABETR:: Adjust the total value 
    // according to the items included in the IDoc.

    BigDecimal sumCRM = 0
    BigDecimal sumINV = 0

    //Retrieve the sum of all positions with CRM.

    rootNode.IDOC.E1IDPU1.E1IDPU5.findAll 
    {it.parent().DOCNAME.text() == 'CRM' }.each {
        //Calculate the sum of the positions for this avis.
        sumCRM += new BigDecimal(it.MOABETR.text())

    };
    //Retrieve the sum of all positions with CRM.
    rootNode.IDOC.E1IDPU1.E1IDPU5.findAll 
    {it.parent().DOCNAME.text() == 'INV' }.each {
        //Calculate the sum of the positions for this avis.
        sumINV += new BigDecimal(it.MOABETR.text())

    };
    //Calculate the total sum which is the inv - crm positions.
    def totalSum = sumINV - sumCRM
    //If due to the IDoc split the total amount of CRM positions exceeds the total 
    //amount of INV positions of an IDoc, special behavior is necessary which will be 
    //done in the followed message mapping. Therefore a property is set.

    if (totalSum <= 0){
        totalSum = totalSum * (-1)
        message.setProperty("ExceptionCRMExceed", "true")
    } else {
        message.setProperty("ExceptionCRMExceed", "false")

    }

    //Replace the values in the according segments   
    rootNode.IDOC.E1IDKU5.MOABETR.replaceNode({
            MOABETR(totalSum)
    })

    //Build a wait mechanism so that iDocs can be processed on the SAP backend
    sleep(15000)
 
    message.setBody(XmlUtil.serialize(rootNode))

    return message;
}