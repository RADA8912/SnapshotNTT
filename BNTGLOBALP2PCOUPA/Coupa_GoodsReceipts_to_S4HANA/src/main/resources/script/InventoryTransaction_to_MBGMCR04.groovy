import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder

def Message processData(Message message) {
	// Variables
	def reader = message.getBody(Reader)
    def input = new XmlSlurper().parse(reader)
	
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)
	
	def date = new Date()
	
	// Mapping Tables
	def mapMoveType =	["created":"101",
						 "voided":"102"]
	
	// SAP variables
    def sndpor = message.getProperty("SNDPOR")
	def sndprt = message.getProperty("SNDPRT")
    def sndprn = message.getProperty("SNDPRN")
    def rcvpor = message.getProperty("RCVPOR")    
    def rcvprt = message.getProperty("RCVPRT")
    def rcvprn = message.getProperty("RCVPRN")
    
    // Build XML request        
    builder.mkp.xmlDeclaration(version: '1.0', encoding: 'UTF-8', standalone: 'no')
    builder.'MBGMCR04' {                
		IDOC('', BEGIN: "1") {
			EDI_DC40('', SEGMENT: "1") {
				'TABNAM' 'EDI_DC40'
				'DIRECT' '2'
				'IDOCTYP' 'MBGMCR04'
				'MESTYP' 'MBGMCR'
				'SNDPOR' sndpor
				'SNDPRT' sndprt
				'SNDPRN' sndprn
				'RCVPOR' rcvpor
				'RCVPRT' rcvprt
				'RCVPRN' rcvprn
			}
			E1MBGMCR('', SEGMENT: "1") {
				E1BP2017_GM_HEAD_01('', SEGMENT: "1") {
					'PSTNG_DATE' date.format('yyyyMMdd')
					'DOC_DATE' Date.parse('yyyy-MM-dd', input.'inventory-transaction'.'transaction-date'.toString()).format('yyyyMMdd')
					'REF_DOC_NO' input.'inventory-transaction'.'rfid-tag'.toString()
					'HEADER_TXT' 'Coupa ID: ' + input.'inventory-transaction'.id.toString()
				}
				E1BP2017_GM_CODE('', SEGMENT: "1") {
					'GM_CODE' '01'
				}
				E1BP2017_GM_ITEM_CREATE('', SEGMENT: "1") {
					'MOVE_TYPE' mapMoveType.get(input.'inventory-transaction'.status.toString())
					'ENTRY_QNT' input.'inventory-transaction'.quantity.toString()
					'ENTRY_UOM_ISO' input.'inventory-transaction'.uom.code.toString()
					'PO_NUMBER' input.'inventory-transaction'.'order-line'.'order-header-number'.toString()
					'PO_ITEM' input.'inventory-transaction'.'order-line'.'line-num'.toString()					
					def itemText = ""
					if(!input.'inventory-transaction'.'asset-tags'.'asset-tag'.tag[0].toString().equals("")) {
						itemText = input.'inventory-transaction'.'asset-tags'.'asset-tag'.tag[0].toString()
						if(!input.'inventory-transaction'.'asset-tags'.'asset-tag'.tag[1].toString().equals("")) {
							itemText += '  More FLOC IDs in Coupa'
						}
					}
					else if (!input.'inventory-transaction'.'order-line'.'asset-tags'.'asset-tag'.tag[0].toString().equals("")) {
						itemText = input.'inventory-transaction'.'order-line'.'asset-tags'.'asset-tag'.tag[0].toString()
						if(!input.'inventory-transaction'.'order-line'.'asset-tags'.'asset-tag'.tag[1].toString().equals("")) {
							itemText += '  More FLOC IDs in Coupa'
						}
					}
					else {
						itemText = input.'inventory-transaction'.'order-line'.'source-part-num'.toString() + " " + input.'inventory-transaction'.'order-line'.commodity.name.toString()
					}
					'ITEM_TEXT' itemText
					'GR_RCPT' input.'inventory-transaction'.'updated-by'.'custom-fields'.'employee-id-number'.toString()
					'MVT_IND' 'B'
				}
			}
		}
	}
	
	// Return
    message.setBody(writer.toString())
    return message
}