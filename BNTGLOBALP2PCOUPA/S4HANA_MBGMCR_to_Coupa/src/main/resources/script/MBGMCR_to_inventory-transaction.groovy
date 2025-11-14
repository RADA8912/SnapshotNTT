import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder

def Message processData(Message message) {
	// Variables
	def coupaPayload = message.getBody(Reader)
    def coupaXml = new XmlSlurper().parse(coupaPayload)
	
	def idocPayload = message.getProperty("idoc_xml")
    def input = new XmlSlurper(false, false).parseText(idocPayload)
	
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)
	
	// Mapping Tables
	def mapMoveType =	["101":"created",
						 "102":"voided"]
    
    //get posex from idoc and find the coupa order line id
    def coupa_orderid = message.getProperty("po_number")
    def coupa_orderlineid = ""
    def sap_orderlineid = input.IDOC.E1MBGMCR.E1BP2017_GM_ITEM_CREATE.PO_ITEM.toString().replaceFirst("^0+(?!\$)", "")
    coupa_orderlineid = coupaXml.'order-lines'.'order-line'.find{it.'line-num' == sap_orderlineid}.id.toString()
    
	// Build XML request        
    builder.mkp.xmlDeclaration(version: '1.0', encoding: 'UTF-8')
    builder.'inventory-transaction' {
		'quantity'(type:'decimal', input.IDOC.E1MBGMCR.E1BP2017_GM_ITEM_CREATE.ENTRY_QNT.toString())
		'rfid-tag' input.IDOC.E1MBGMCR.E1BP2017_GM_HEAD_01.REF_DOC_NO.toString()
		'type' 'ReceivingQuantityConsumption'
		def mapstatus = mapMoveType.get(input.IDOC.E1MBGMCR.E1BP2017_GM_ITEM_CREATE.MOVE_TYPE.toString())
		if (mapstatus == null || mapstatus.equals("")){ mapstatus = "created" }
		'status' mapstatus
		'exported'(type:'boolean', 'true')
		'custom-fields'{
			'sap-created'(type:'boolean', 'true')
			'external-id' input.IDOC.E1MBGMCR.E1BP2017_GM_ITEM_CREATE.GR_NUMBER.toString()
		}
		'order-line' {
			'id'(type:'integer', coupa_orderlineid)
			//'line-num' input.IDOC.E1MBGMCR.E1BP2017_GM_ITEM_CREATE.PO_ITEM.toString().replaceFirst("^0+(?!\$)", "")
			'order-header-id' coupa_orderid
			//'order-header-number' input.IDOC.E1MBGMCR.E1BP2017_GM_ITEM_CREATE.PO_NUMBER.toString().replaceFirst("^0+(?!\$)", "")
		}
		'uom'{
			'code' input.IDOC.E1MBGMCR.E1BP2017_GM_ITEM_CREATE.ENTRY_UOM.toString()
		}
	}
	
	// Return
    message.setBody(writer.toString())
    return message
}