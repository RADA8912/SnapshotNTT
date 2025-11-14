import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import com.sap.it.api.ITApiFactory
import com.sap.it.api.mapping.ValueMappingApi

def Message processData(Message message) {
	// API Factory for Value Mapping access
	def helperValMap = ITApiFactory.getApi(ValueMappingApi.class, null)
	
	// Variables
	def reader = message.getBody(Reader)
    def input = new XmlSlurper().parse(reader)
	
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)
		
	// SAP variables
    def sndpor = message.getProperty("SNDPOR")
	def sndprt = message.getProperty("SNDPRT")
    def sndprn = message.getProperty("SNDPRN")
    def rcvpor = message.getProperty("RCVPOR")    
    def rcvprt = message.getProperty("RCVPRT")
    def rcvprn = message.getProperty("RCVPRN")
    
	// Decide if a PO-Create or PO-Change must be mapped.
	def latestIntegrationStatusText = input.'order-header'.'custom-fields'.'integration-status'.toString()
	def poStatus = input.'order-header'.'order-lines'.'order-line'.status  
	def isPoCreate = true
	if (latestIntegrationStatusText.equals("")){
		for (item in poStatus){
			if (item.toString().equals("soft_closed_for_invoicing") || item.toString().equals("soft_closed_for_receiving")){
				isPoCreate = false
			}
		}
	} else if (latestIntegrationStatusText.indexOf(":") >= 0){
		def latestIntegrationStatus = latestIntegrationStatusText.split(":")[0]
		def latestIntegrationMessageType = ""
		if (latestIntegrationStatus.indexOf(" ") >= 0){
			def latestIntegrationStatusSplits = latestIntegrationStatus.split(" ")
			latestIntegrationStatus = latestIntegrationStatusSplits[0]
			latestIntegrationMessageType = latestIntegrationStatusSplits[1]
		}
		if (latestIntegrationStatus.equals("Success")){
			isPoCreate = false
		} else {
			if (latestIntegrationMessageType.equals("PO-Create")){
				isPoCreate = true
			} else if (latestIntegrationMessageType.equals("PO-Change") || latestIntegrationMessageType.equals("")){
				isPoCreate = false
			}
		}
	}
	
    // Build XML request
	if (isPoCreate){	
		builder.mkp.xmlDeclaration(version: '1.0', encoding: 'UTF-8', standalone: 'no')
		builder.'PORDCR103' {             
			IDOC('', BEGIN: "1") {
				EDI_DC40('', SEGMENT: "1") {
					'TABNAM' 'EDI_DC40'
					'DIRECT' '2'
					'IDOCTYP' 'PORDCR103'
					'MESTYP' 'PORDCR1'
					'SNDPOR' sndpor
					'SNDPRT' sndprt
					'SNDPRN' sndprn
					'RCVPOR' rcvpor
					'RCVPRT' rcvprt
					'RCVPRN' rcvprn
				}
				E1PORDCR1('', SEGMENT: "1") {
					'NO_MESSAGING' 'X'
					E1BPMEPOHEADER('', SEGMENT: "1") {
						'PO_NUMBER' input.'order-header'.'po-number'.toString()
						'COMP_CODE' input.'order-header'.'order-lines'.'order-line'.'account'.'segment-1'[0].toString()
						'DOC_TYPE' 'ZCOU'
						
						def creatDate = input.'order-header'.'created-at'.toString()
						if (!creatDate.equals("")){
							creatDate = Date.parse('yyyy-MM-dd', creatDate).format('yyyyMMdd')
						}	
						'CREAT_DATE' creatDate
						
						def assignedBuyer = input.'order-header'.'custom-fields'.'assigned-buyer'.'external-ref-code'.toString().split("_")
						if (assignedBuyer.length >= 2){
							'CREATED_BY' assignedBuyer[1]
						}
						
						if (input.'order-header'.'supplier'.'number'.toString().trim().matches("\\d+")){
							'VENDOR' String.format("%010d", Integer.valueOf(input.'order-header'.'supplier'.'number'.toString()))
						}
						'PMNTTRMS' input.'order-header'.'payment-term'.'code'.toString()
						
						def plant = ""
                        plant = input.'order-header'.'order-lines'.'order-line'.'account'.'segment-1'[0].toString()
                        if(plant.equals("")){
                            plant = input.'order-header'.'order-lines'.'order-line'.'account-allocations'.'account-allocation'.account.'segment-1'[0].toString()
                        }
						def purchOrg = helperValMap.getMappedValue('Coupa', 'Plant', plant, 'SAP', 'PurchasingOrganization')
						if(purchOrg != null){
							'PURCH_ORG' purchOrg
						}
						else{
							'PURCH_ORG' '1000'
						}
						
						def purGroup = '200'
						if (assignedBuyer.length >= 1 && !assignedBuyer[0].equals("")){
							purGroup = assignedBuyer[0]
						}						
						'PUR_GROUP' purGroup
						
						'CURRENCY' input.'order-header'.'order-lines'.'order-line'.currency.code[0].toString()
						'REF_1' input.'order-header'.'custom-fields'.'hci-activity-id'.toString()
						'INCOTERMS1' input.'order-header'.'shipping-term'.code.toString()
						'INCOTERMS2' input.'order-header'.'ship-to-address'.city.toString()
						'OUR_REF' input.'order-header'.'custom-fields'.'navision-po-'.toString()
					}
					E1BPMEPOHEADERX('', SEGMENT: "1") {
						'PO_NUMBER' 'X'
						'COMP_CODE' 'X'
						'DOC_TYPE' 'X'
						'CREAT_DATE' 'X'
						'CREATED_BY' 'X'
						'VENDOR' 'X'
						'PMNTTRMS' 'X'
						'PURCH_ORG' 'X'
						'PUR_GROUP' 'X'
						'CURRENCY' 'X'
						'REF_1' 'X'
						'INCOTERMS1' 'X'
						'INCOTERMS2' 'X'
						'OUR_REF' 'X'
					}
					def counterPckgNo = 1
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPMEPOITEM('', SEGMENT: "1") {
							'PO_ITEM' orderline.'line-num'.toString()
							
							def description = orderline.description.toString()
							if (description.length() > 40){
								description = description.substring(0,40)
							}
							'SHORT_TEXT' description
							
							def plant = ""
                            plant = orderline.'account'.'segment-1'.toString()
                            if(plant.equals("")){
                                plant = orderline.'account-allocations'.'account-allocation'.account.'segment-1'.toString()
                            }
							'PLANT' plant
							
							'MATL_GROUP' orderline.'custom-fields'.'sap-material-class'.toString()
							
							def vendMat = ""
							def qty = ""
							def unit = ""
							if (orderline.type.toString().equals("OrderAmountLine")){
								vendMat = "SERVICE (AMT)"
								qty = "1"
								unit = "C62"
							}else{
								vendMat = orderline.'source-part-num'.toString()
								qty = orderline.quantity.toString()
								unit = orderline.uom.code.toString()
							}							
							'VEND_MAT' vendMat
							'QUANTITY' qty
							'PO_UNIT_ISO' unit
							
							'NET_PRICE' orderline.price.toString()
							
							'PRICE_UNIT' '1'
							
							def noMoreGr = ""
							if (orderline.type.toString().equals("OrderQuantityLine") && (orderline.status.toString().equals("soft_closed_for_receiving") || orderline.status.toString().equals("soft_closed_for_invoicing"))){
								noMoreGr = "X"
							}
							'NO_MORE_GR' noMoreGr
							
							def finalInv = ""
							if ((orderline.type.toString().equals("OrderQuantityLine") || orderline.type.toString().equals("OrderAmountLine")) && (orderline.status.toString().equals("soft_closed_for_receiving") || orderline.status.toString().equals("soft_closed_for_invoicing"))){
								finalInv = "X"
							}
							'FINAL_INV' finalInv
							
							def itemCat = ""
							if (orderline.type.toString().equals("OrderAmountLine")){
								itemCat = "B"
							}
							'ITEM_CAT' itemCat
							
							def isSeg3empty = false
							if (!orderline.account.isEmpty()){
							    def seg3 = orderline.account.'segment-3'.toString()
    							if (seg3.trim().matches("\\d+")) {
    								Integer i = Integer.valueOf(seg3);
    								if (i==0){
    									isSeg3empty = true
    								}
    							}else if (seg3.equals("")){
    								isSeg3empty = true
    							}
							}
							def isSeg3AllocEmpty = false
							if (!orderline.'account-allocations'.'account-allocation'.isEmpty()){
							    isSeg3AllocEmpty = true
    							orderline.'account-allocations'.'account-allocation'.each{ accountallocation ->
        						    def seg3Alloc = accountallocation.account.'segment-3'.toString()
        							if (seg3Alloc.equals("")) {
        							}
        							else if (!seg3Alloc.trim().matches("\\d+")) {
        							    isSeg3AllocEmpty = false
        							} else {
        								Integer i = Integer.valueOf(seg3Alloc);
        								if (i!=0){
        									isSeg3AllocEmpty = false
        								}
        							}
    							}
							}
							def acc = "P"
							if (isSeg3empty || isSeg3AllocEmpty){
								acc = "K"
							}
							'ACCTASSCAT' acc
							
							def distrib = ""
							if (!orderline.'account-allocations'.'account-allocation'.isEmpty()){
							    distrib = "2"
							}
							'DISTRIB' distrib
							
							def irInd = "X"
							def freeItem = ""
							if (Double.valueOf(orderline.price.toString()) == 0){
								irInd = ""
								freeItem = "X"
							}
							'IR_IND' irInd
							'FREE_ITEM' freeItem
							
							'NET_WEIGHT' orderline.'custom-fields'.'net-weight-amount'.toString()
							
							'WEIGHTUNIT' 'KG'
							
							'PREQ_NAME' orderline.'requester'.'employee-number'.toString()
							
							def pckgNo = ""
							if (orderline.type.toString().equals("OrderAmountLine")){
								pckgNo = String.format("%09d", counterPckgNo)
								counterPckgNo++
							}
							'PCKG_NO' pckgNo
							
							E1BPMEPOITEM1('', SEGMENT: "1") {
								def milestonesLineAmountNotEmpty = true
								if (orderline.milestones.milestone.amount.toString().equals("")){
									milestonesLineAmountNotEmpty = false
								}
								def downpayType = ""
								if (milestonesLineAmountNotEmpty){
									downpayType = "M"
								}
								'DOWNPAY_TYPE' downpayType
								
								'DOWNPAY_AMOUNT' orderline.milestones.milestone.amount.toString()
								
								def dueDate = orderline.milestones.milestone.'due-date'.toString()
								if (!dueDate.equals("")){
									dueDate = Date.parse('yyyy-MM-dd', dueDate).format('yyyyMMdd')
								}
								'DOWNPAY_DUEDATE' dueDate
								
								def prodType = ""
								if (orderline.type.toString().equals("OrderAmountLine")){
									prodType = "2"
								}
								'PRODUCTTYPE' prodType
								
								def stDate = ""
								def endDate = ""
								if (orderline.type.toString().equals("OrderAmountLine")){
									stDate = orderline.'custom-fields'.'service-start-date'.toString()
									if (!stDate.equals("")){
										stDate = Date.parse('yyyy-MM-dd', stDate).format('yyyyMMdd')
									}
									endDate = orderline.'custom-fields'.'service-end-date'.toString()
									if (!endDate.equals("")){
										endDate = Date.parse('yyyy-MM-dd', endDate).format('yyyyMMdd')
									}									
								}
								'STARTDATE' stDate
								'ENDDATE' endDate
							}
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPMEPOITEMX('', SEGMENT: "1") {
							def isOrderAmountLine = false
							if (orderline.type.toString().equals("OrderAmountLine")){
								isOrderAmountLine = true
							}
							
							'PO_ITEM' orderline.'line-num'.toString()
							'PO_ITEMX' 'X'
							'SHORT_TEXT' 'X'
							'PLANT' 'X'
							'MATL_GROUP' 'X'
							'VEND_MAT' 'X'
							'QUANTITY' 'X'
							'PO_UNIT_ISO' 'X'
							'NET_PRICE' 'X'
							'PRICE_UNIT' 'X'
							'NO_MORE_GR' 'X'
							'FINAL_INV' 'X'
							
							if (isOrderAmountLine){
								'ITEM_CAT' 'X'
							}							
							
							'ACCTASSCAT' 'X'
							'DISTRIB' 'X'
							'IR_IND' 'X'
							'FREE_ITEM' 'X'
							'NET_WEIGHT' 'X'
							'WEIGHTUNIT' 'X'
							'PREQ_NAME' 'X'
							
							if (isOrderAmountLine){
								'PCKG_NO' 'X'
							}	
							
							def milestonesLineAmountNotEmpty = true
							if (orderline.milestones.milestone.amount.toString().equals("")){
								milestonesLineAmountNotEmpty = false
							}
							def downpayX = ""
							if (milestonesLineAmountNotEmpty){
								downpayX = 'X'
							}
							'DOWNPAY_TYPE'downpayX
							'DOWNPAY_AMOUNT' downpayX
							'DOWNPAY_DUEDATE' downpayX
							
							if (isOrderAmountLine){
								'PRODUCTTYPE' 'X'
								'STARTDATE' 'X'
								'ENDDATE' 'X'
							}
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPMEPOADDRDELIVERY('', SEGMENT: "1") {
							'PO_ITEM' orderline.'line-num'.toString()
							'NAME' input.'order-header'.'ship-to-address'.name.toString()
							'CITY' input.'order-header'.'ship-to-address'.city.toString()
							'POSTL_COD1' input.'order-header'.'ship-to-address'.'postal-code'.toString()
							'STREET' input.'order-header'.'ship-to-address'.street1.toString()
							'COUNTRY' input.'order-header'.'ship-to-address'.country.code.toString()
							'REGION' input.'order-header'.'ship-to-address'.state.toString()
							E1BPMEPOADDRDELIVERY1('', SEGMENT: "1") {
								'E_MAIL' input.'order-header'.'ship-to-user'.email.toString()
							}
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPMEPOSCHEDULE('', SEGMENT: "1") {
							'PO_ITEM' orderline.'line-num'.toString()
							'SCHED_LINE' '1'
							
							def deliveryDate = ""
							if (orderline.type.toString().equals("OrderQuantityLine") && !orderline.'custom-fields'.'service-start-date'.toString().equals("") && !orderline.'custom-fields'.'service-end-date'.toString().equals("")){
								deliveryDate = orderline.'custom-fields'.'service-end-date'.toString()
							} else {
								deliveryDate = orderline.'need-by-date'.toString()
							}
							if (!deliveryDate.equals("")){
								deliveryDate = Date.parse('yyyy-MM-dd', deliveryDate).format('yyyyMMdd')
							}	
							'DELIVERY_DATE' deliveryDate
							
							def qtyScheduleLine = orderline.quantity.toString()
							if (orderline.type.toString().equals("OrderAmountLine")){
								qtyScheduleLine = "1"
							}
							'QUANTITY' qtyScheduleLine
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPMEPOSCHEDULX('', SEGMENT: "1") {
							'PO_ITEM' orderline.'line-num'.toString()
							'SCHED_LINE' '1'
							
							def deliveryDateX = ""
							def deliveryDate = ""
							if (orderline.type.toString().equals("OrderQuantityLine") && !orderline.'custom-fields'.'service-start-date'.toString().equals("") && !orderline.'custom-fields'.'service-end-date'.toString().equals("")){
								deliveryDate = orderline.'custom-fields'.'service-end-date'.toString()
							} else {
								deliveryDate = orderline.'need-by-date'.toString()
							}
							if (!deliveryDate.equals("")){
								deliveryDateX = 'X'
							}
							'DELIVERY_DATE' deliveryDateX
							
							'QUANTITY' 'X'
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						if (orderline.'account-allocations'.'account-allocation'.isEmpty()){
							E1BPMEPOACCOUNT('', SEGMENT: "1") {
								'PO_ITEM' orderline.'line-num'.toString()
								
								def serialNo = ""
								if (orderline.type.toString().equals("OrderAmountLine")){
									serialNo = "01"
								}
								'SERIAL_NO' serialNo
								
								'QUANTITY' orderline.quantity.toString()
								
								def seg3 = orderline.account.'segment-3'.toString()
								def isSeg3empty = false
								if (seg3.trim().matches("\\d+")) {
									Integer i = Integer.valueOf(seg3);
									if (i==0){
										isSeg3empty = true
									}
								}else if (seg3.equals("")){
									isSeg3empty = true
								}
								def costCntr = ""
								if (isSeg3empty && orderline.account.'segment-2'.toString().trim().matches("\\d+"))
								{
									costCntr = String.format("%010d", Integer.valueOf(orderline.account.'segment-2'.toString()))
								}
								'COSTCENTER' costCntr
								
								'GR_RCPT' input.'order-header'.'ship-to-attention'.toString()
								
								def wbsElmnt = ""
								if (!isSeg3empty)
								{
									wbsElmnt = orderline.account.'segment-3'.toString()
								}
								'WBS_ELEMENT' wbsElmnt
							}
						} else {
							def serialNo = 1
							orderline.'account-allocations'.'account-allocation'.each{ accountallocation ->
								E1BPMEPOACCOUNT('', SEGMENT: "1") {
									'PO_ITEM' orderline.'line-num'.toString()
									'SERIAL_NO' serialNo++
									'DISTR_PERC' accountallocation.pct.toString()
									
									def seg3 = accountallocation.account.'segment-3'.toString()
									def isSeg3empty = false
									if (seg3.trim().matches("\\d+")) {
										Integer i = Integer.valueOf(seg3);
										if (i==0){
											isSeg3empty = true
										}
									}else if (seg3.equals("")){
										isSeg3empty = true
									}
									def costCntr = ""
									if (isSeg3empty && accountallocation.account.'segment-2'.toString().trim().matches("\\d+"))
									{
										costCntr = String.format("%010d", Integer.valueOf(accountallocation.account.'segment-2'.toString()))
									}
									'COSTCENTER' costCntr
									
									'GR_RCPT' input.'order-header'.'ship-to-attention'.toString()
									
									def wbsElmnt = ""
									if (!isSeg3empty)
									{
										wbsElmnt = accountallocation.account.'segment-3'.toString()
									}
									'WBS_ELEMENT' wbsElmnt
								}
							}
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						if (orderline.'account-allocations'.'account-allocation'.isEmpty()){
							E1BPMEPOACCOUNTX('', SEGMENT: "1") {
								'PO_ITEM' orderline.'line-num'.toString()
								
								def serialNo = ""
								if (orderline.type.toString().equals("OrderAmountLine")){
									serialNo = "01"
								}
								'SERIAL_NO' serialNo
								
								'QUANTITY' 'X'
								'COSTCENTER' 'X'
								'GR_RCPT' 'X'
								'WBS_ELEMENT' 'X'
							}
						} else {
							def serialNo = 1
							orderline.'account-allocations'.'account-allocation'.each{ accountallocation ->
								E1BPMEPOACCOUNTX('', SEGMENT: "1") {
									'PO_ITEM' orderline.'line-num'.toString()
									'SERIAL_NO' serialNo++
									'DISTR_PERC' 'X'
									'COSTCENTER' 'X'
									'GR_RCPT' 'X'
									'WBS_ELEMENT' 'X'
								}
							}
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPMEPOCOND('', SEGMENT: "1") {
							'ITM_NUMBER' orderline.'line-num'.toString()
							'COND_TYPE' 'PMP0'
							'COND_VALUE' orderline.price.toString()
							'CURRENCY' orderline.currency.code.toString()
							'CHANGE_ID' 'U'
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPMEPOCONDX('', SEGMENT: "1") {
							'ITM_NUMBER' orderline.'line-num'.toString()
							'COND_TYPE' 'X'
							'COND_VALUE' 'X'
							'CURRENCY' 'X'
							'CHANGE_ID' 'X'
						}
					}
					counterPckgNo = 1
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						if (orderline.type.toString().equals("OrderAmountLine")){
							E1BPESUHC('', SEGMENT: "1") {
								'PCKG_NO' String.format("%09d", counterPckgNo)
								counterPckgNo++
								'LIMIT' orderline.price.toString()
								'EXP_VALUE' orderline.price.toString()
							}
						}
					}
					counterPckgNo = 1
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						if (orderline.type.toString().equals("OrderAmountLine")){
							E1BPESKLC('', SEGMENT: "1") {
								'PCKG_NO' String.format("%09d", counterPckgNo)
								counterPckgNo++
								'SERNO_LINE' '01'
								'SERIAL_NO' '01'
							}
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPEIPO('', SEGMENT: "1") {
							'PO_ITEM' orderline.'line-num'.toString()
							'COUNTRYORI' orderline.'custom-fields'.'country-of-origin'.'external-ref-code'.toString()
							'COMM_CODE' orderline.'custom-fields'.'intrastat-commodity-code'.toString()
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPEIPOX('', SEGMENT: "1") {
							'PO_ITEM' orderline.'line-num'.toString()
							'COUNTRYORI' 'X'
							'COMM_CODE' 'X'
						}
					}
					
					def attachText = ""
					def attachTextTemp = input.'order-header'.attachments.attachment.text
					attachText = attachTextTemp.join()
					def position = 0
					def pieceLength = 132
					def attachTextLength = attachText.length()
					def maxSegmentsCounter = 0
					while(position < attachTextLength && maxSegmentsCounter < 9999){
						maxSegmentsCounter++
						def positionTo = position + pieceLength
						if (positionTo > attachTextLength){
							positionTo = attachTextLength
						}
						E1BPMEPOTEXTHEADER('', SEGMENT: "1") {
							'TEXT_ID' 'F01'
							'TEXT_FORM' '*'
							'TEXT_LINE' attachText.substring(position, positionTo)
						}
						position = positionTo
					}
					
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						if (orderline.type.toString().equals("OrderQuantityLine") && !(orderline.'custom-fields'.'service-start-date'.toString().equals("")) && !(orderline.'custom-fields'.'service-end-date'.toString().equals(""))) {
							E1BPMEPOTEXTHEADER('', SEGMENT: "1") {
								'TEXT_ID' 'F02'
								'TEXT_FORM' '/'
								'TEXT_LINE' "Line Item " + orderline.'line-num'.toString() + " Start Date: " + Date.parse('yyyy-MM-dd', orderline.'custom-fields'.'service-start-date'.toString()).format('yyyyMMdd') + " End Date: " + Date.parse('yyyy-MM-dd', orderline.'custom-fields'.'service-end-date'.toString()).format('yyyyMMdd')
							}
						}
					}
				}
			}
		}
	} else {
		builder.mkp.xmlDeclaration(version: '1.0', encoding: 'UTF-8', standalone: 'no')
		builder.'PORDCH03' {             
			IDOC('', BEGIN: "1") {
				EDI_DC40('', SEGMENT: "1") {
					'TABNAM' 'EDI_DC40'
					'DIRECT' '2'
					'IDOCTYP' 'PORDCH03'
					'MESTYP' 'PORDCH'
					'SNDPOR' sndpor
					'SNDPRT' sndprt
					'SNDPRN' sndprn
					'RCVPOR' rcvpor
					'RCVPRT' rcvprt
					'RCVPRN' rcvprn
				}
				E1PORDCH('', SEGMENT: "1") {
					'PURCHASEORDER' input.'order-header'.'po-number'.toString()
					'NO_MESSAGING' 'X'
					E1BPMEPOHEADER('', SEGMENT: "1") {
						'PO_NUMBER' input.'order-header'.'po-number'.toString()
						'COMP_CODE' input.'order-header'.'order-lines'.'order-line'.'account'.'segment-1'[0].toString()
						'DOC_TYPE' 'ZCOU'
						
						def creatDate = input.'order-header'.'created-at'.toString()
						if (!creatDate.equals("")){
							creatDate = Date.parse('yyyy-MM-dd', creatDate).format('yyyyMMdd')
						}	
						'CREAT_DATE' creatDate
						
						if (input.'order-header'.'supplier'.'number'.toString().trim().matches("\\d+")){
							'VENDOR' String.format("%010d", Integer.valueOf(input.'order-header'.'supplier'.'number'.toString()))
						}
						'PMNTTRMS' input.'order-header'.'payment-term'.'code'.toString()
						
						def plant = ""
                        plant = input.'order-header'.'order-lines'.'order-line'.'account'.'segment-1'[0].toString()
                        if(plant.equals("")){
                            plant = input.'order-header'.'order-lines'.'order-line'.'account-allocations'.'account-allocation'.account.'segment-1'[0].toString()
                        }
						def purchOrg = helperValMap.getMappedValue('Coupa', 'Plant', plant, 'SAP', 'PurchasingOrganization')
						if(purchOrg != null){
							'PURCH_ORG' purchOrg
						}
						else{
							'PURCH_ORG' '1000'
						}
						
						def assignedBuyer = input.'order-header'.'custom-fields'.'assigned-buyer'.'external-ref-code'.toString().split("_")
						def purGroup = '200'
						if (assignedBuyer.length >= 1 && !assignedBuyer[0].equals("")){
							purGroup = assignedBuyer[0]
						}						
						'PUR_GROUP' purGroup
						
						'CURRENCY' input.'order-header'.'order-lines'.'order-line'.currency.code[0].toString()
						'REF_1' input.'order-header'.'custom-fields'.'hci-activity-id'.toString()
						'INCOTERMS1' input.'order-header'.'shipping-term'.code.toString()
						'INCOTERMS2' input.'order-header'.'ship-to-address'.city.toString()
						'OUR_REF' input.'order-header'.'custom-fields'.'navision-po-'.toString()
					}
					E1BPMEPOHEADERX('', SEGMENT: "1") {
						'PO_NUMBER' 'X'
						'COMP_CODE' 'X'
						'DOC_TYPE' 'X'
						'CREAT_DATE' 'X'
						'VENDOR' 'X'
						'PMNTTRMS' 'X'
						'PURCH_ORG' 'X'
						'PUR_GROUP' 'X'
						'CURRENCY' 'X'
						'REF_1' 'X'
						'INCOTERMS1' 'X'
						'INCOTERMS2' 'X'
						'OUR_REF' 'X'
					}
					def counterPckgNo = 1
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPMEPOITEM('', SEGMENT: "1") {
							'PO_ITEM' orderline.'line-num'.toString()
							
							def deleteInd = ""
							if (input.'order-header'.'status-status'.toString().equals("cancelled"))
							{
								deleteInd = "X"
							}
							if (orderline.status.toString().equals("cancelled"))
							{
								deleteInd = "X"
							}
							'DELETE_IND' deleteInd							
							
							def description = orderline.description.toString()
							if (description.length() > 40){
								description = description.substring(0,40)
							}
							'SHORT_TEXT' description
							
							def plant = ""
                            plant = orderline.'account'.'segment-1'.toString()
                            if(plant.equals("")){
                                plant = orderline.'account-allocations'.'account-allocation'.account.'segment-1'.toString()
                            }
							'PLANT' plant
							
							'MATL_GROUP' orderline.'custom-fields'.'sap-material-class'.toString()
							
							def vendMat = ""
							def qty = ""
							def unit = ""
							if (orderline.type.toString().equals("OrderAmountLine")){
								vendMat = "SERVICE (AMT)"
								qty = "1"
								unit = "C62"
							}else{
								vendMat = orderline.'source-part-num'.toString()
								qty = orderline.quantity.toString()
								unit = orderline.uom.code.toString()
							}							
							'VEND_MAT' vendMat
							'QUANTITY' qty
							'PO_UNIT_ISO' unit
							
							'NET_PRICE' orderline.price.toString()
							
							'PRICE_UNIT' '1'
							
							def noMoreGr = ""
							if (orderline.type.toString().equals("OrderQuantityLine") && (orderline.status.toString().equals("soft_closed_for_receiving") || orderline.status.toString().equals("soft_closed_for_invoicing"))){
								noMoreGr = "X"
							}
							'NO_MORE_GR' noMoreGr
							
							def finalInv = ""
							if ((orderline.type.toString().equals("OrderQuantityLine") || orderline.type.toString().equals("OrderAmountLine")) && (orderline.status.toString().equals("soft_closed_for_receiving") || orderline.status.toString().equals("soft_closed_for_invoicing"))){
								finalInv = "X"
							}
							'FINAL_INV' finalInv
							
							def itemCat = ""
							if (orderline.type.toString().equals("OrderAmountLine")){
								itemCat = "B"
							}
							'ITEM_CAT' itemCat
							
							def isSeg3empty = false
							if (!orderline.account.isEmpty()){
							    def seg3 = orderline.account.'segment-3'.toString()
    							if (seg3.trim().matches("\\d+")) {
    								Integer i = Integer.valueOf(seg3);
    								if (i==0){
    									isSeg3empty = true
    								}
    							}else if (seg3.equals("")){
    								isSeg3empty = true
    							}
							}
							def isSeg3AllocEmpty = false
							if (!orderline.'account-allocations'.'account-allocation'.isEmpty()){
							    isSeg3AllocEmpty = true
    							orderline.'account-allocations'.'account-allocation'.each{ accountallocation ->
        						    def seg3Alloc = accountallocation.account.'segment-3'.toString()
        							if (seg3Alloc.equals("")) {
        							}
        							else if (!seg3Alloc.trim().matches("\\d+")) {
        							    isSeg3AllocEmpty = false
        							} else {
        								Integer i = Integer.valueOf(seg3Alloc);
        								if (i!=0){
        									isSeg3AllocEmpty = false
        								}
        							}
    							}
							}
							def acc = "P"
							if (isSeg3empty || isSeg3AllocEmpty){
								acc = "K"
							}
							'ACCTASSCAT' acc
							
							def distrib = ""
							if (!orderline.'account-allocations'.'account-allocation'.isEmpty()){
							    distrib = "2"
							}
							'DISTRIB' distrib
							
							def irInd = "X"
							def freeItem = ""
							if (Double.valueOf(orderline.price.toString()) == 0){
								irInd = ""
								freeItem = "X"
							}
							'IR_IND' irInd
							'FREE_ITEM' freeItem
							
							'NET_WEIGHT' orderline.'custom-fields'.'net-weight-amount'.toString()
							
							'WEIGHTUNIT' 'KG'
							
							'PREQ_NAME' orderline.'requester'.'employee-number'.toString()
							
							def pckgNo = ""
							if (orderline.type.toString().equals("OrderAmountLine")){
								pckgNo = String.format("%09d", counterPckgNo)
								counterPckgNo++
							}
							'PCKG_NO' pckgNo
							
							E1BPMEPOITEM1('', SEGMENT: "1") {
								def milestonesLineAmountNotEmpty = true
								if (orderline.milestones.milestone.amount.toString().equals("")){
									milestonesLineAmountNotEmpty = false
								}
								def downpayType = ""
								if (milestonesLineAmountNotEmpty){
									downpayType = "M"
								}
								'DOWNPAY_TYPE' downpayType
								
								'DOWNPAY_AMOUNT' orderline.milestones.milestone.amount.toString()
								
								def dueDate = orderline.milestones.milestone.'due-date'.toString()
								if (!dueDate.equals("")){
									dueDate = Date.parse('yyyy-MM-dd', dueDate).format('yyyyMMdd')
								}
								'DOWNPAY_DUEDATE' dueDate
								
								def prodType = ""
								if (orderline.type.toString().equals("OrderAmountLine")){
									prodType = "2"
								}
								'PRODUCTTYPE' prodType
								
								def stDate = ""
								def endDate = ""
								if (orderline.type.toString().equals("OrderAmountLine")){
									stDate = orderline.'custom-fields'.'service-start-date'.toString()
									if (!stDate.equals("")){
										stDate = Date.parse('yyyy-MM-dd', stDate).format('yyyyMMdd')
									}
									endDate = orderline.'custom-fields'.'service-end-date'.toString()
									if (!endDate.equals("")){
										endDate = Date.parse('yyyy-MM-dd', endDate).format('yyyyMMdd')
									}									
								}
								'STARTDATE' stDate
								'ENDDATE' endDate
							}
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPMEPOITEMX('', SEGMENT: "1") {
							def isOrderAmountLine = false
							if (orderline.type.toString().equals("OrderAmountLine")){
								isOrderAmountLine = true
							}
							
							'PO_ITEM' orderline.'line-num'.toString()
							'PO_ITEMX' 'X'
							'DELETE_IND' 'X'
							'SHORT_TEXT' 'X'
							'PLANT' 'X'
							'MATL_GROUP' 'X'
							'VEND_MAT' 'X'
							'QUANTITY' 'X'
							'PO_UNIT_ISO' 'X'
							'NET_PRICE' 'X'
							'PRICE_UNIT' 'X'
							'NO_MORE_GR' 'X'
							'FINAL_INV' 'X'
							
							if (isOrderAmountLine){
								'ITEM_CAT' 'X'
							}							
							
							'ACCTASSCAT' 'X'
							'DISTRIB' 'X'
							'IR_IND' 'X'
							'FREE_ITEM' 'X'
							'NET_WEIGHT' 'X'
							'WEIGHTUNIT' 'X'
							'PREQ_NAME' 'X'
							
							if (isOrderAmountLine){
								'PCKG_NO' 'X'
							}	
							
							def milestonesLineAmountNotEmpty = true
							if (orderline.milestones.milestone.amount.toString().equals("")){
								milestonesLineAmountNotEmpty = false
							}
							def downpayX = ""
							if (milestonesLineAmountNotEmpty){
								downpayX = 'X'
							}
							'DOWNPAY_TYPE'downpayX
							'DOWNPAY_AMOUNT' downpayX
							'DOWNPAY_DUEDATE' downpayX
							
							if (isOrderAmountLine){
								'PRODUCTTYPE' 'X'
								'STARTDATE' 'X'
								'ENDDATE' 'X'
							}
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPMEPOADDRDELIVERY('', SEGMENT: "1") {
							'PO_ITEM' orderline.'line-num'.toString()
							'NAME' input.'order-header'.'ship-to-address'.name.toString()
							'CITY' input.'order-header'.'ship-to-address'.city.toString()
							'POSTL_COD1' input.'order-header'.'ship-to-address'.'postal-code'.toString()
							'STREET' input.'order-header'.'ship-to-address'.street1.toString()
							'COUNTRY' input.'order-header'.'ship-to-address'.country.code.toString()
							'REGION' input.'order-header'.'ship-to-address'.state.toString()
							E1BPMEPOADDRDELIVERY1('', SEGMENT: "1") {
								'E_MAIL' input.'order-header'.'ship-to-user'.email.toString()
							}
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPMEPOSCHEDULE('', SEGMENT: "1") {
							'PO_ITEM' orderline.'line-num'.toString()
							'SCHED_LINE' '1'
							
							def deliveryDate = ""
							if (orderline.type.toString().equals("OrderQuantityLine") && !orderline.'custom-fields'.'service-start-date'.toString().equals("") && !orderline.'custom-fields'.'service-end-date'.toString().equals("")){
								deliveryDate = orderline.'custom-fields'.'service-end-date'.toString()
							} else {
								deliveryDate = orderline.'need-by-date'.toString()
							}
							if (!deliveryDate.equals("")){
								deliveryDate = Date.parse('yyyy-MM-dd', deliveryDate).format('yyyyMMdd')
							}	
							'DELIVERY_DATE' deliveryDate
							
							def qtyScheduleLine = orderline.quantity.toString()
							if (orderline.type.toString().equals("OrderAmountLine")){
								qtyScheduleLine = "1"
							}
							'QUANTITY' qtyScheduleLine
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPMEPOSCHEDULX('', SEGMENT: "1") {
							'PO_ITEM' orderline.'line-num'.toString()
							'SCHED_LINE' '1'
							
							def deliveryDateX = ""
							def deliveryDate = ""
							if (orderline.type.toString().equals("OrderQuantityLine") && !orderline.'custom-fields'.'service-start-date'.toString().equals("") && !orderline.'custom-fields'.'service-end-date'.toString().equals("")){
								deliveryDate = orderline.'custom-fields'.'service-end-date'.toString()
							} else {
								deliveryDate = orderline.'need-by-date'.toString()
							}
							if (!deliveryDate.equals("")){
								deliveryDateX = 'X'
							}
							'DELIVERY_DATE' deliveryDateX
							
							'QUANTITY' 'X'
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						if (orderline.'account-allocations'.'account-allocation'.isEmpty()){
							E1BPMEPOACCOUNT('', SEGMENT: "1") {
								'PO_ITEM' orderline.'line-num'.toString()
								
								def serialNo = ""
								if (orderline.type.toString().equals("OrderAmountLine")){
									serialNo = "01"
								}
								'SERIAL_NO' serialNo
								
								'QUANTITY' orderline.quantity.toString()
								
								def seg3 = orderline.account.'segment-3'.toString()
								def isSeg3empty = false
								if (seg3.trim().matches("\\d+")) {
									Integer i = Integer.valueOf(seg3);
									if (i==0){
										isSeg3empty = true
									}
								}else if (seg3.equals("")){
									isSeg3empty = true
								}
								def costCntr = ""
								if (isSeg3empty && orderline.account.'segment-2'.toString().trim().matches("\\d+"))
								{
									costCntr = String.format("%010d", Integer.valueOf(orderline.account.'segment-2'.toString()))
								}
								'COSTCENTER' costCntr
								
								'GR_RCPT' input.'order-header'.'ship-to-attention'.toString()
								
								def wbsElmnt = ""
								if (!isSeg3empty)
								{
									wbsElmnt = orderline.account.'segment-3'.toString()
								}
								'WBS_ELEMENT' wbsElmnt
							}
						} else {
							def serialNo = 1
							orderline.'account-allocations'.'account-allocation'.each{ accountallocation ->
								E1BPMEPOACCOUNT('', SEGMENT: "1") {
									'PO_ITEM' orderline.'line-num'.toString()
									'SERIAL_NO' serialNo++
									'DISTR_PERC' accountallocation.pct.toString()
									
									def seg3 = accountallocation.account.'segment-3'.toString()
									def isSeg3empty = false
									if (seg3.trim().matches("\\d+")) {
										Integer i = Integer.valueOf(seg3);
										if (i==0){
											isSeg3empty = true
										}
									}else if (seg3.equals("")){
										isSeg3empty = true
									}
									def costCntr = ""
									if (isSeg3empty && accountallocation.account.'segment-2'.toString().trim().matches("\\d+"))
									{
										costCntr = String.format("%010d", Integer.valueOf(accountallocation.account.'segment-2'.toString()))
									}
									'COSTCENTER' costCntr
									
									'GR_RCPT' input.'order-header'.'ship-to-attention'.toString()
									
									def wbsElmnt = ""
									if (!isSeg3empty)
									{
										wbsElmnt = accountallocation.account.'segment-3'.toString()
									}
									'WBS_ELEMENT' wbsElmnt
								}
							}
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						if (orderline.'account-allocations'.'account-allocation'.isEmpty()){
							E1BPMEPOACCOUNTX('', SEGMENT: "1") {
								'PO_ITEM' orderline.'line-num'.toString()
								
								def serialNo = ""
								if (orderline.type.toString().equals("OrderAmountLine")){
									serialNo = "01"
								}
								'SERIAL_NO' serialNo
								
								'QUANTITY' 'X'
								'COSTCENTER' 'X'
								'GR_RCPT' 'X'
								'WBS_ELEMENT' 'X'
							}
						} else {
							def serialNo = 1
							orderline.'account-allocations'.'account-allocation'.each{ accountallocation ->
								E1BPMEPOACCOUNTX('', SEGMENT: "1") {
									'PO_ITEM' orderline.'line-num'.toString()
									'SERIAL_NO' serialNo++								
									'DISTR_PERC' 'X'
									'COSTCENTER' 'X'
									'GR_RCPT' 'X'
									'WBS_ELEMENT' 'X'
								}
							}
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPMEPOCOND('', SEGMENT: "1") {
							'ITM_NUMBER' orderline.'line-num'.toString()
							'COND_TYPE' 'PMP0'
							'COND_VALUE' orderline.price.toString()
							'CURRENCY' orderline.currency.code.toString()
							'CHANGE_ID' 'U'
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPMEPOCONDX('', SEGMENT: "1") {
							'ITM_NUMBER' orderline.'line-num'.toString()
							'COND_TYPE' 'X'
							'COND_VALUE' 'X'
							'CURRENCY' 'X'
							'CHANGE_ID' 'X'
						}
					}
					counterPckgNo = 1
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						if (orderline.type.toString().equals("OrderAmountLine")){
							E1BPESUHC('', SEGMENT: "1") {
								'PCKG_NO' String.format("%09d", counterPckgNo)
								counterPckgNo++
								'LIMIT' orderline.price.toString()
								'EXP_VALUE' orderline.price.toString()
							}
						}
					}
					counterPckgNo = 1
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						if (orderline.type.toString().equals("OrderAmountLine")){
							E1BPESKLC('', SEGMENT: "1") {
								'PCKG_NO' String.format("%09d", counterPckgNo)
								counterPckgNo++
								'SERNO_LINE' '01'
								'SERIAL_NO' '01'
							}
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPEIPO('', SEGMENT: "1") {
							'PO_ITEM' orderline.'line-num'.toString()
							'COUNTRYORI' orderline.'custom-fields'.'country-of-origin'.'external-ref-code'.toString()
							'COMM_CODE' orderline.'custom-fields'.'intrastat-commodity-code'.toString()
						}
					}
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						E1BPEIPOX('', SEGMENT: "1") {
							'PO_ITEM' orderline.'line-num'.toString()
							'COUNTRYORI' 'X'
							'COMM_CODE' 'X'
						}
					}
					
					def attachText = ""
					def attachTextTemp = input.'order-header'.attachments.attachment.text
					attachText = attachTextTemp.join()
					def position = 0
					def pieceLength = 132
					def attachTextLength = attachText.length()
					def maxSegmentsCounter = 0
					while(position < attachTextLength && maxSegmentsCounter < 9999){
						maxSegmentsCounter++
						def positionTo = position + pieceLength
						if (positionTo > attachTextLength){
							positionTo = attachTextLength
						}
						E1BPMEPOTEXTHEADER('', SEGMENT: "1") {
							'TEXT_ID' 'F01'
							'TEXT_FORM' '*'
							'TEXT_LINE' attachText.substring(position, positionTo)
						}
						position = positionTo
					}
					
					input.'order-header'.'order-lines'.'order-line'.each{ orderline ->
						if (orderline.type.toString().equals("OrderQuantityLine") && !(orderline.'custom-fields'.'service-start-date'.toString().equals("")) && !(orderline.'custom-fields'.'service-end-date'.toString().equals(""))) {
							E1BPMEPOTEXTHEADER('', SEGMENT: "1") {
								'TEXT_ID' 'F02'
								'TEXT_FORM' '/'
								'TEXT_LINE' "Line Item " + orderline.'line-num'.toString() + " Start Date: " + Date.parse('yyyy-MM-dd', orderline.'custom-fields'.'service-start-date'.toString()).format('yyyyMMdd') + " End Date: " + Date.parse('yyyy-MM-dd', orderline.'custom-fields'.'service-end-date'.toString()).format('yyyyMMdd')
							}
						}
					}
				}
			}
		}
	}
	
	// Return
    message.setBody(writer.toString())
    return message
}