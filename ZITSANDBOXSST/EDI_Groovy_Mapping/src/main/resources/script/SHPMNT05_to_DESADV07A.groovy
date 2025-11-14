import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder

Message processData(Message message) {
	
	/*
	 * Initialize
	 */
    Reader reader = message.getBody(Reader)
    def SHPMNT05 = new XmlSlurper().parse(reader)
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)
	def date = new Date()
	
	/* 
	 * Fetch the NRO from the Property "CorrelatedNRO".
	 * The NRO needs to have a correlation to the Header "SAP_EDI_Message_Number", to get the same NRO in case of reties.
	 * Create a new Property with a Content Modifier:
	 * 		Name: CorrelatedNRO
	 *		Source Type: Number Range
	 *		Source Value: <NRO_Name>:${header.SAP_EDI_Message_Number}
	 * Don't forget to set the Header "SAP_EDI_Message_Number" within the Allowed Headers in the IFlow.
	 * Do not set the same NRO within the Agreement, otherwise it will be counted up twice per message.
	 */
	def correlatedNRO = message.getProperty("CorrelatedNRO")
	
	/*
	 * Mapping Tables
	 */
	def mapPackages =	["BOX":"PK",
						 "CAGE1":"ZZ"]
	
	def mapUoMs =		["EA":"PCE",
						 "ZZ":"ZZZ"]
	
	/*
	 * Build the target message
	 */
	builder.DESADV07A {
//		'S_UNA'(':+.? \'')
		S_UNB{
			C_S001{
				'D_0001'('UNOC')		//Syntax identifier
				'D_0002'('3')			//Syntax version number
			}
			C_S002{
				'D_0004'('MyCompany')	//Interchange sender identification
				'D_0007'('ZZZ')			//Interchange code qualifier
			}
			C_S003{
				'D_0010'('Customer')	//Interchange receiver identification
				'D_0007'('ZZZ')			//Interchange code qualifier
			}
			C_S004{
				'D_0017'(date.format("yyMMdd"))		//Date
				'D_0019'(date.format("HHmm"))		//Time
			}
			'D_0020'('')	        	//Interchange control reference -> Number Range Object
//			'D_0035'('1')				//Test Indicator
		}
		M_DESADV{
			S_UNH{
				'D_0062'('1')			//Message reference number
				C_S009{
					'D_0065'('DESADV')	//Message type
					'D_0052'('D')		//Message version number
					'D_0054'('07A')		//Message release number
					'D_0051'('UN')		//Controlling agency
					'D_0057'('GMI022')	//Association assigned code
				}
			}
			S_BGM{						//Beginning of message				
				C_C002{
					'D_1001'('351')		//351 Despatch advice
				}
				C_C106{
					'D_1004'(SHPMNT05.IDOC.E1EDT20.E1EDL20.VBELN.toString())
				}
				'D_1225'('9')			//9 Original
			}
			S_DTM{						//Despatch advice date
				C_C507{
					'D_2005'('137')		//137 Document issue date time
					'D_2380'(date.format("yyyyMMdd"))
					'D_2379'('102')		//102 CCYYMMDD
				}
			}
			S_DTM{						//Estimated Arrival Date
				C_C507{
					'D_2005'('132')		//132 Transport means arrival date time, estimated
					'D_2380'(SHPMNT05.IDOC.E1EDT20.E1EDT10.find{it.QUALF.toString().equals("005")}.IEDD.toString())
					'D_2379'('102')		//102 CCYYMMDD
				}
			}
			S_MEA{						//Consignment's Gross Weight
				'D_6311'('AAX')			//AAX Consignment measurement
				C_C502{
					'D_6313'('AAD')		//AAD Consignment gross weight
				}
				C_C174{
					'D_6411'(SHPMNT05.IDOC.E1EDT20.E1EDL20.GEWEI.toString())
					'D_6314'(SHPMNT05.IDOC.E1EDT20.E1EDL20.BTGEW.toString())
				}
			}
			G_SG1{						//Shipment / delivery note reference
				S_RFF{					//Reference
					C_C506{
						'D_1153'('AAU')	//AAU Despatch note document identifier
						'D_1154'(SHPMNT05.IDOC.E1EDT20.E1EDL20.VBELN.toString())
					}
				}
			}
			G_SG2{						//Ship-To
				S_NAD{					//ShipTo Name and address
					'D_3035'('ST')		//ST Ship to
					C_C082{
						'D_3039'(SHPMNT05.IDOC.E1EDT20.E1EDL20.E1ADRM1.find{it.PARTNER_Q.toString().equals("WE")}.E1ADRE1.EXTEND_D.toString())
						'D_3055'('92')	//92 Assigned by buyer or buyer's agent
					}
					C_C080{
						'D_3036'(SHPMNT05.IDOC.E1EDT20.E1EDL20.E1ADRM1.find{it.PARTNER_Q.toString().equals("WE")}.NAME1.toString())
					}
					C_C059{
						'D_3042'(SHPMNT05.IDOC.E1EDT20.E1EDL20.E1ADRM1.find{it.PARTNER_Q.toString().equals("WE")}.STREET1.toString())
					}
					'D_3164'(SHPMNT05.IDOC.E1EDT20.E1EDL20.E1ADRM1.find{it.PARTNER_Q.toString().equals("WE")}.CITY1.toString())
					'D_3251'(SHPMNT05.IDOC.E1EDT20.E1EDL20.E1ADRM1.find{it.PARTNER_Q.toString().equals("WE")}.POSTL_COD1.toString())
					'D_3207'(SHPMNT05.IDOC.E1EDT20.E1EDL20.E1ADRM1.find{it.PARTNER_Q.toString().equals("WE")}.COUNTRY1.toString())
				}
			}
			G_SG5{						//Terms of Delivery
				S_TOD{					//Terms of delivery or transport
					C_C100{
						'D_4053'('EXW')	//EXW Ex Works (... named place)
					}
				}
			}
			G_SG6{						//Means of Transport
				S_TDT{					//Transport information
					'D_8051'('12')		//12 at departure
					C_C220{
						'D_8067'('30')	//30 Road transport
					}
				}
			}
			G_SG8{						//Equipment
				S_EQD{					//Equipment details
					'D_8053'('TE')		//TE Trailer
					C_C237{
						'D_8260'(SHPMNT05.IDOC.E1EDT20.E1EDL20.TRAID.toString())
					}
				}
			}
			def counter_D_7164 = 0
			SHPMNT05.IDOC.E1EDT20.E1EDL20.E1EDL37.each{ // Use of E1EDL37 = Only homogeneous packages
			E1EDL37 ->
				if(!E1EDL37.E1EDL44.POSNR.toString().equals('000000')){
					counter_D_7164++
					G_SG10{							//Despatch control line / group of inner packaging items and article line
						S_CPS{						//Consignment packing sequence
							'D_7164'(counter_D_7164)
							'D_7075'('4')			//4 No packaging hierarchy
						}
						G_SG11{						//Group of inner packaging items
							S_PAC{					//Package
								'D_7224'('1')		//Package quantity
								C_C202{				//Package type
									'D_7065'(mapPackages.get(E1EDL37.VHILM.toString()))
									'D_3055'('92')	//Code list responsible agency code
								}
								C_C402{				//Package type identification
									'D_7077'('F')	//F Free-form
									'D_7064_2'(E1EDL37.VHILM.toString())
									'D_7143_2'('SA')//SA Supplier's article number
								}
							}
							S_QTY{					//Quantity per package
								C_C186{
									'D_6063'('52')	//52 Quantity per pack
									'D_6060'(E1EDL37.E1EDL44.VEMNG.toString())
									'D_6411'(mapUoMs.get(E1EDL37.E1EDL44.VEMEH.toString()))
								}
							}
							G_SG13{					//Individual packaging item
								S_PCI{				//Package identification
									'D_4233'('17')	//17 Seller's instructions
									C_C827{
										'D_7511'('M')//M Unique number assigned to a homogeneous handling unit
										'D_3055'('5')//5 ISO (International Organization for Standardization)
									}
								}
								G_SG15{				//Label serial number(s)
									S_GIN{			//Goods identity number
										'D_7405'('ML')//ML Marking/label number
										C_C208{
											'D_7402'(E1EDL37.EXIDV.toString())
										}
									}
								}
							}
						}
						G_SG17{						//Article and Despatched Article
							S_LIN{					//Line item
								'D_1082'(counter_D_7164)
								C_C212{
									'D_7140'(SHPMNT05.IDOC.E1EDT20.E1EDL20.E1EDL24.find{it.POSNR.toString().equals(E1EDL37.E1EDL44.POSNR.toString())}.KDMAT.toString())
									'D_7143'('IN')	//IN Buyer's item number
								}
							}
							S_ALI{					//Additional information
								'D_3239'(SHPMNT05.IDOC.E1EDT20.E1EDL20.E1EDL24.find{it.POSNR.toString().equals(E1EDL37.E1EDL44.POSNR.toString())}.E1EDL35.HERKL.toString())//Country of origin identifier
							}
						}
					}
				}
			}
			S_UNT{
				'D_0074'('0')			//Number of segments -> Will be overwritten by TPM
				'D_0062'('1')			//Message reference number
			}
		}
		S_UNZ{
			'D_0036'('1')				//Interchange control count
			'D_0020'('')	        	//Interchange control reference -> Number Range Object
		}
	}
	
	/*
	 * Return message
	 */
    message.setBody(writer.toString())
    return message
}