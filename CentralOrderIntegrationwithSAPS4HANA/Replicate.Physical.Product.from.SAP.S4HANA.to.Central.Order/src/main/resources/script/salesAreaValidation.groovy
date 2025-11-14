import com.sap.gateway.ip.core.customdev.util.Message
import groovy.transform.Field
import groovy.xml.XmlUtil
import src.main.resources.script.ProductLoggingUtils

@Field String LOG_NAME_MATERIAL = '2a. Sales Area Validation'
@Field String LOG_NAME_ARTICLE 	= '2b. Sales Area Validation'
@Field String LOG_BANNER 	= '****************************************************'
@Field String NEW_LINE 		= '\n'
@Field String VALIDATED_IDOC_LOG	= '2b. Validated IDoc';
@Field String VALIDATED_IDOC_LOG_ARTICLE	= '2c. Validated IDoc';

def Message validateSalesAreaForMaterial(Message message){
	def body = message.getBody(java.io.Reader)
	def xml = new XmlParser().parse(body)
	def startProductCount = xml.IDOC.size()
	def loggerUtil = new ProductLoggingUtils(messageLogFactory)
	def logMessage = "${LOG_BANNER}${NEW_LINE}${LOG_NAME_MATERIAL}${NEW_LINE}${LOG_BANNER}${NEW_LINE}" as String
	logMessage+= "Products received: ${startProductCount}${NEW_LINE}"

	xml.IDOC.E1MARAM.E1MVKEM.findAll { node ->
		node.VKORG.size() == 0 || node.VKORG[0].value() == [] || node.VTWEG.size() == 0 || node.VTWEG[0].value() == []
	}.each { node ->
		logMessage+= "Warning: Removed incomplete sales area in product with ${node.parent().MATNR[0].name()} = ${node.parent().MATNR.text()}${NEW_LINE}"
		node.parent().remove(node)
	}

	xml.IDOC.findAll { node ->
		node.E1MARAM.E1MVKEM.size() == 0
	}.each { node ->
		logMessage+= "Error: Cannot transfer product with ${node.E1MARAM.MATNR[0].name()} = ${node.E1MARAM.MATNR.text()}.  Reason: Missing sales area information. ${NEW_LINE}"
		node.parent().remove(node);
		message.setProperty("hasErrors", true);
	}

	logMessage+= "Products that passed the validation: ${xml.IDOC.size()} out of ${startProductCount}${NEW_LINE}"
	loggerUtil.outputToLog(message, LOG_NAME_MATERIAL,logMessage);

	if(xml.IDOC.size() <= 0) {
		throw new SalesAreaValidationException("No additional products to transfer.  Reason: Missing or invalid sales area information.  Check log ${LOG_NAME_MATERIAL} for more details.")
	}

	def xmlString = XmlUtil.serialize(xml)
	message.setBody(xmlString)
	loggerUtil.outputDebugLog( message, VALIDATED_IDOC_LOG,xmlString, "application/xml");

	return message;
}

def Message validateSalesAreaForArticles(Message message){
	def body = message.getBody(java.io.Reader)
	def xml = new XmlParser().parse(body)
	def startProductCount = xml.IDOC.size()
	def loggerUtil = new ProductLoggingUtils(messageLogFactory)
	def logMessage = "${LOG_BANNER}${NEW_LINE}${LOG_NAME_ARTICLE}${NEW_LINE}${LOG_BANNER}${NEW_LINE}" as String
	logMessage+= "Products received: ${startProductCount}${NEW_LINE}"

	xml.IDOC.E1BPE1MVKERT.findAll { node ->
		node.SALES_ORG.size() == 0 || node.SALES_ORG[0].value() == [] || node.DISTR_CHAN.size() == 0 || node.DISTR_CHAN[0].value() == []
	}.each { node ->
		logMessage+= "Warning: Removed incomplete sales area in product with ${node.parent().E1BPE1MATHEAD.MATERIAL[0].name()} = ${node.parent().E1BPE1MATHEAD.MATERIAL[0].text()}${NEW_LINE}"
		node.parent().remove(node)
	}

	xml.IDOC.findAll { node ->
		node.E1BPE1MVKERT.size() == 0
	}.each { node ->
		logMessage+= "Error: Cannot transfer product with ${node.E1BPE1MATHEAD.MATERIAL[0].name()} = ${node.E1BPE1MATHEAD.MATERIAL[0].text()}.  Reason: Missing sales area information. ${NEW_LINE}"
		node.parent().remove(node);
		message.setProperty("hasErrors", true);
	}

	logMessage+= "Products that passed the validation: ${xml.IDOC.size()} out of ${startProductCount}${NEW_LINE}"
	loggerUtil.outputToLog(message, LOG_NAME_ARTICLE, logMessage);

	if(xml.IDOC.size() <= 0) {
	    
		throw new SalesAreaValidationException("No additional products to transfer.  Reason: Missing or invalid sales area information.  Check log ${LOG_NAME_ARTICLE} for more details.")
	}

	def xmlString = XmlUtil.serialize(xml)
	message.setBody(xmlString)
	loggerUtil.outputDebugLog(message, VALIDATED_IDOC_LOG_ARTICLE, xmlString, "application/xml");

	return message;
}

public class SalesAreaValidationException extends Exception {
	public SalesAreaValidationException(String message) {
		super(message);
	}
}
