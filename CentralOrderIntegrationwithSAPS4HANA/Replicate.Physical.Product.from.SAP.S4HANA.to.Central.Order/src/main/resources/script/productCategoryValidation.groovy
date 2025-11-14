import com.sap.gateway.ip.core.customdev.util.Message
import groovy.transform.Field
import groovy.xml.XmlUtil
import src.main.resources.script.ProductLoggingUtils

@Field String UNSUPPORTED_TYPES_LOG	= '2a. Product Category Validation';
@Field String LOG_BANNER 	= '****************************************************'
@Field String NEW_LINE 		= '\n'

def Message validateMaterialCategoryForArticles(Message message){
	def body = message.getBody(java.io.Reader)
	def xml = new XmlParser().parse(body)
	def loggerUtil = new ProductLoggingUtils(messageLogFactory)
	
	def articleTypeTexts = ['00' : 'Single material', '01' : 'Generic material', '02' : 'Variant', '10' : 'Sales set', '11' : 'Prepack', '12' : 'Display', '20' : 'Material group material', '21' : 'Hierarchy material', '22' : 'Group material', '30' : 'Material group reference material']
	def supportedArticleTypes = ['00', '02', '10', '12']
	def startProductCount = xml.IDOC.size()
	def logMessage = "${LOG_BANNER}${NEW_LINE}${UNSUPPORTED_TYPES_LOG}${NEW_LINE}${LOG_BANNER}${NEW_LINE}" as String
	logMessage+= "Products received: ${startProductCount}${NEW_LINE}"
	
    xml.IDOC.E1BPE1MATHEAD.findAll { node ->
		!supportedArticleTypes.contains(node.MATL_CAT.text())
	}.each { node ->
		def displayId = node.MATERIAL_LONG.text()
		if (displayId.isEmpty()) {
			displayId = node.MATERIAL.text()
		}
		logMessage += "Error: Cannot transfer product '${displayId}' from sender '${node.parent().EDI_DC40.SNDPRN.text()}' due to unsupported product category: '${articleTypeTexts[node.MATL_CAT.text()]}'.${NEW_LINE}"
		node.parent().parent().remove(node.parent())
	}
	logMessage+= "Products that have passed the validation: ${xml.IDOC.size()} out of ${startProductCount}${NEW_LINE}"
    
    def xmlString = XmlUtil.serialize(xml)
	message.setBody(xmlString)
	loggerUtil.outputToLog(message, UNSUPPORTED_TYPES_LOG, logMessage)
		
	return message;
}
