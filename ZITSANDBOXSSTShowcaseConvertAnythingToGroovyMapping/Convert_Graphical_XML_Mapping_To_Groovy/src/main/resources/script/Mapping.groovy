import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper
import groovy.xml.MarkupBuilder
import java.util.Locale
import java.util.TimeZone
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.text.DecimalFormatSymbols
import java.text.DecimalFormat
import com.sap.it.api.ITApiFactory
import com.sap.it.api.mapping.ValueMappingApi

/**
* MapXMLToXML
* This Groovy script is a XML to XML mapping template.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	final String LOCALE_LANGUAGE = 'de'
	final String LOCALE_COUNTRY = 'DE'
	final String TIME_ZONE = 'Europe/Berlin'

	def messageLog = messageLogFactory.getMessageLog(message)
	def valueMapApi = ITApiFactory.getService(ValueMappingApi.class, null)

	if (message.getBodySize() > 0) {
		// Set locale
		Locale locale
		if (isValidLocale(LOCALE_LANGUAGE + '_' + LOCALE_COUNTRY)) {
			locale = new Locale(LOCALE_LANGUAGE, LOCALE_COUNTRY)
		} else {
			throw Exception("'$LOCALE_LANGUAGE" + "_" + "$LOCALE_COUNTRY' is not valid locale.")
		}
		// Set time zone
		TimeZone timeZone
		if (TimeZone.getAvailableIDs().contains(TIME_ZONE)) {
			timeZone = TimeZone.getTimeZone(TIME_ZONE)
		} else {
			throw Exception("'$TIME_ZONE' is not valid time zone.")
		}

		// Get body
		Reader reader = message.getBody(Reader)
		def MATMAS06 = new XmlSlurper().parse(reader)

		// Create writer
		def writer = new StringWriter()
		// Create markup builder
		def xml = new MarkupBuilder(writer)
		// Set double quotes
		xml.setDoubleQuotes(true)
		// Set XML declaration
		xml.mkp.xmlDeclaration(version: '1.0', encoding: 'utf-8')

		// TODO: Create sub segments
		// TODO: Create complex mapping formulas

		// Map nodes to target structure
		// -> XPath: /MaterialBundle
		xml.MaterialBundle {
			// -> XPath: /MaterialBundle/Material
			MATMAS06.IDOC.each{ IDOC ->
				Material {
					// -> XPath: /MaterialBundle/Material/Name
					'Name' IDOC.E1MARAM.MATNR.text()
					// -> XPath: /MaterialBundle/Material/Name2
					'Name2' trimZeroLeft(IDOC.E1MARAM.MATNR.text())
					// -> XPath: /MaterialBundle/Material/Name3
					'Name3' trimZeroLeft(IDOC.E1MARAM.MATNR.text())
					// -> XPath: /MaterialBundle/Material/Name4
					'Name4' IDOC.E1MARAM.MATNR.text().concat('_').concat(IDOC.E1MARAM.MTART.text())
					// -> XPath: /MaterialBundle/Material/TransportConditionA
					'TransportConditionA' 'abc'
					// -> XPath: /MaterialBundle/Material/TransportConditionB
					'TransportConditionB' '' // TODO: SplitByValue(useOneAsMany(mapWithDefault(IDOC.E1MARAM.TRAGR, default_value=, result), IDOC.E1MARAM.E1MARCM, IDOC.E1MARAM.E1MARCM, result), type=0)
					// -> XPath: /MaterialBundle/Material/ID
					'ID' message.getProperty('SAP_MessageProcessingLogID')
					// -> XPath: /MaterialBundle/Material/TempB1
					'TempB1' trimRight(IDOC.E1MARAM.TEMPB.text())
					// -> XPath: /MaterialBundle/Material/TempB2
					'TempB2' mapWithDefault(IDOC.E1MARAM.TEMPB.text(), '999')
					// -> XPath: /MaterialBundle/Material/Text
					MATMAS06.IDOC.E1MARAM.E1MAKTM.each{ E1MAKTM ->
						Text {
							// -> XPath: /MaterialBundle/Material/Text/Qualifier
							'Qualifier' E1MAKTM.MSGFN.text()
							// -> XPath: /MaterialBundle/Material/Text/Language
							'Language' E1MAKTM.SPRAS.text()
							// -> XPath: /MaterialBundle/Material/Text/Description
							'Description' E1MAKTM.MAKTX.text()
							// -> XPath: /MaterialBundle/Material/Text/ISOKey
							'ISOKey' E1MAKTM.SPRAS_ISO.text()
							// -> XPath: /MaterialBundle/Material/Text/Text2
							MATMAS06.IDOC.E1MARAM.E1MAKTM.E1MAKTM2.each{ E1MAKTM2 ->
								Text2 {
									// -> XPath: /MaterialBundle/Material/Text/Text2/Qualifier
									'Qualifier' E1MAKTM2.MSGFN.text()
									// -> XPath: /MaterialBundle/Material/Text/Text2/Language
									'Language' E1MAKTM2.SPRAS.text()
									// -> XPath: /MaterialBundle/Material/Text/Text2/Description
									'Description' E1MAKTM2.MAKTX.text()
									// -> XPath: /MaterialBundle/Material/Text/Text2/ISOKey
									'ISOKey' E1MAKTM2.SPRAS_ISO.text()
								}
							}
						}
					}
					// -> XPath: /MaterialBundle/Material/PlantData
					MATMAS06.IDOC.E1MARAM.E1MARCM.each{ E1MARCM ->
						PlantData {
							// -> XPath: /MaterialBundle/Material/PlantData/Function
							'Function' E1MARCM.MSGFN.text()
							// -> XPath: /MaterialBundle/Material/PlantData/Plant
							'Plant' E1MARCM.WERKS.text()
						}
					}
				}
			}
		}

		// Create new body
		String bodyNew = writer.toString()
		// Set new body
		message.setBody(bodyNew)

		// Set Custom Header Property TODO: Check global monitoring concept for Custom Header Names and place where you need to set it.
		if (messageLog) {
			messageLog.addCustomHeaderProperty('IDocNumber', '') // TODO: Set source like: XPath+SourceNodeName.Text()
			messageLog.addCustomHeaderProperty('DocumentNumber', '') // TODO: Set source like: XPath+SourceNodeName.Text()
			messageLog.addCustomHeaderProperty('PartnerNumber', '') // TODO: Set source like: XPath+SourceNodeName.Text()
			messageLog.addCustomHeaderProperty('Plant', '') // TODO: Set source like: XPath+SourceNodeName.Text()
		}
	}
	return message
}

/**
Node segment creation examples
------------------------------
- Create a single target segment node:									TargetSegmentNodeName { ... }
- Create a target segment node for each source segment node:			root.SourceSegmentNodeName.each{SourceSegmentNodeName -> TargetSegmentNodeName { ... }}
- Create a target segment node for each source segment node with indes:	root.SourceSegmentNodeName.eachWithIndex{SourceSegmentNodeName, index -> TargetSegmentNodeName { ... }}

Node standard mapping examples
------------------------------
- Set empty node: 											'TargetNodeName' ''
- Set constant:												'TargetNodeName' 'abc'
- Create a direct mapping from source node:					'TargetNodeName' SourceNodeName.text()
- Trim source node value:									'TargetNodeName' SourceNodeName.text().trim()')
- Set source node value to lower case:						'TargetNodeName' SourceNodeName.text().toLowerCase()
- Set source node value to upper case:						'TargetNodeName' SourceNodeName.text().toUpperCase()
- Set length of source node value:							'TargetNodeName' SourceNodeName.text().length()
- Replace a character or string in source node value:		'TargetNodeName' SourceNodeName.replaceAll(',','.')
  (replaceAll() method uses RegEx for finding. Please note
   that if source is null, you will get an error.)
- Set count of source segment node:							'TargetNodeName' SourceSegmentNodeName.size()
- Get message ID:											'TargetNodeName' message.getProperty('SAP_MessageProcessingLogID')
- Get an exchange property value:							'TargetNodeName' message.getProperty('ExchangePropertyName')
- Get sender from header:									'TargetNodeName' message.getHeader('SAP_Sender', String)
- Get receiver from header:									'TargetNodeName' message.getHeader('SAP_Receiver', String)
- Get file name from header:								'TargetNodeName' message.getHeader('CamelFileName', String)
- Get a header value:										'TargetNodeName' message.getHeader('HeaderValueName', String)

Node mapping examples with Groovy Custom Functions
--------------------------------------------------
- Passes a value from condition node value with method assignValueByCondition(conditionValue, suchValues, returnValue): 	'TargetNodeName' assignValueByCondition(SourceNodeName1.text(), ['005','006'], SourceNodeName2.text())
- Dynamic substring with method dynamicSubstring(value, start, length):													'TargetNodeName' dynamicSubstring(SourceNodeName.text(), 0, 4)
- Fill up string to length with leading zeros with method fillLeadingZeros(value, totalLength):							'TargetNodeName' fillLeadingZeros(SourceNodeName.text(), 10)
- Fill up string to length with space with method fillUpToLengthWithSpace(value, totalLength):							'TargetNodeName' fillUpToLengthWithSpace(SourceNodeName.text(), 10)
- Use source node with method fixedValues(value, mapTable, behaviour, defaultValue):									'TargetNodeName' fixedValues(SourceNodeName.text(), ['a':'1','b':'2'], 2, 'Default value')
  Behaviour if lookup fails: 'Default value', 'Use key', 'Throw exception'
- Format a number with method formatNumber(numberStr, pattern, decimalSeparator):										'TargetNodeName' formatNumber(SourceNodeName.text(), '#,##0.00', ',')
- Format useing space with method formatValueBySpace(value, length, cutLengthDirection, fillSapce, fillSpaceDirection):	'TargetNodeName' formatValueBySpace(SourceNodeName.text(), 10, 'right', true, 'right')
- Format using zero with method formatValueByZero(value, length, cutLengthDirection, fillZero, fillZeroDirection):		'TargetNodeName' formatValueByZero(SourceNodeName.text(), 10, 'right', true, 'right')
- Get a date after x days with method getDateAfterDays(timeZone, date, dateInFormat, days, dateOutFormat):				'TargetNodeName' getDateAfterDays(timeZone, SourceNodeName.text(), 'yyyyMMdd', 1, 'yyyyMMdd')
- Gets an EDIFACT-F2379-conform qualifier descriping the dates format with method getDateFormat(date):					'TargetNodeName' getDateFormat(SourceNodeName.text())
- Get a mandatory exchange property value with method getExchangeProperty(message, propertyName, mandatory):			'TargetNodeName' getExchangeProperty(message, 'ExchangePropertyName', true)
- Get a mandatory header value with method getHeader(message, HeaderName, mandatory):									'TargetNodeName' getHeader(message, 'HeaderName', true)
- Check if value is a number with method isNumber(value):																'TargetNodeName' isNumber(SourceNodeName.text())
- Get date of Monday in week with method getMondayOfWeek(locale, year, week):											'TargetNodeName' getMondayOfWeek(locale, SourceNode1Name.text(), SourceNode2Name.text())
- Get only numbers from string with method getOnlyNumbers(value):														'TargetNodeName' getOnlyNumbers(SourceNodeName.text())
- Gets a single value from condition node value with method
  getValueByCondition(conditionPath, nodeNameConditionValue, suchValue, nodeNameValue)									'TargetNodeName' getValueByCondition(SourcePath1, SourceNodeName1, '005', SourceNodeName2))
- Removes the trailing characters with method headString(value, headLength):											'TargetNodeName' headString(SourceNodeName.text(), 10)
- Use source node with method mapWithDefault(value, defaultValue):														'TargetNodeName' mapWithDefault(SourceNodeName.text(), 'Default value')
- Change minus from begin to end with method minusFromBeginToEnd(value):												'TargetNodeName' minusFromBeginToEnd(SourceNodeName.text())
- Change minus from end to begin with method minusFromEndToBegin(value):												'TargetNodeName' minusFromEndToBegin(SourceNodeName.text())
- Remove all algebraic signs with method removeAlgebraicSign(value):													'TargetNodeName' removeAlgebraicSign(SourceNodeName.text())
- Remove only algebraic sign plus with method removeAlgebraicSignPlus(value):											'TargetNodeName' removeAlgebraicSignPlus(SourceNodeName.text())
- Remove all characters with method removeAllCharacters(value):															'TargetNodeName' removeAllCharacters(SourceNodeName.text())
- Remove all spaces with method removeAllSpaces(value):																	'TargetNodeName' removeAllSpaces(SourceNodeName.text())
- Remove all special characters with method removeAllSpecialCharacters(value):											'TargetNodeName' removeAllSpecialCharacters(SourceNodeName.text())
- Remove all carriage return line Feed (CRLF) with method removeCarriageReturnLineFeed(value):							'TargetNodeName' removeCarriageReturnLineFeed(SourceNodeName.text())
- Remove decimal if zero with methode removeDecimalIfZero(value):														'TargetNodeName' removeDecimalIfZero(SourceNodeName.text())
- Replace special characters with methode replaceSpecialCharacters(value):												'TargetNodeName' replaceSpecialCharacters(SourceNodeName.text())
- Replace umlauts with methode replaceUmlauts(value):																	'TargetNodeName' replaceUmlauts(SourceNodeName.text())
- Gets true if value from value node is corresponding to segment value, otherwise false with methode
  segmentHasOneOfSuchValues(conditionPath, nodeNameConditionValue, suchValues):											'TargetNodeName' segmentHasOneOfSuchValues(SourceNodePath, SourceNodeName, '005')
- Set current date with method currentDate(timeZone, dateFormat):														'TargetNodeName' setCurrentDate(timeZone, 'yyyyMMdd')
- Set current time with method currentDate(timeZone, timeFormat):														'TargetNodeName' setCurrentDate(timeZone, 'HHmmss')
- Sets decimal separator to point and remove thousands separator with method setDecimalSeparatorPoint(value):			'TargetNodeName' setDecimalSeparatorPoint(SourceNodeName.text())
- Set default current time with method setDefaultAsCurrentDate(timeZone, value, dateFormat):							'TargetNodeName' setDefaultAsCurrentDate(timeZone, SourceNodeName.text(), 'yyyyMMdd')
- Set directory with method setDirectory(message, timeZone, rootFolder, addYear, addMonth, addDay, sender, messageType,
  messageVersion, messageRelease):																						'TargetNodeName' setDirectory(message, timeZone, 'root', true, true, true, '', '', '', '')
- Set file name with method setFileName(message, timeZone, messageType, messageVersion, messageRelease, addTimeStamp,
  dateFormat, documentNumber, addMessageId, addCorrelationId, nameSeparator, fileType):									'TargetNodeName' setFileName(message, timeZone, 'INVOIC', '', '', true, '', '', true, false, '_', 'xml')
- Strip space with method stripSpaces(value):																			'TargetNodeName' stripSpaces(SourceNodeName.text())
- Removes the leading characters with method tailString(value, tailLength):												'TargetNodeName' tailString(SourceNodeName.text(), 10)
- Throw exception if no value (mandatory node) with method throwExceptionIfNoValue(value):								'TargetNodeName' throwExceptionIfNoValue(SourceNodeName.text())
- Format a string like a number with method toNumber(value):															'TargetNodeName' toNumber(SourceNodeName.text())
- Transform a date to another date format with method transformDate(timeZone, dateString, dateInFormat, dateOutFormat):	'TargetNodeName' transformDate(timeZone, SourceNodeName.text(), 'yyyyMMdd', 'yyyy-MM-dd')
- Trim right with method trimRight(value):																				'TargetNodeName' trimRight(SourceNodeName.text())
- Trim zero left with method trimZeroLeft(value):																		'TargetNodeName' trimZeroLeft(SourceNodeName.text())
- Use source node with method valueMap(valueMapApi, sourceAgency, sourceIdentifier, sourceKey, targetAgency, ...
  targetIdentifier, behaviour, defaultValue) Behaviour if lookup fails: 'Default value', 'Use key', 'Throw exception':	'TargetNodeName' valueMap(valueMapApi, 'sourceAgency', 'sourceIdentifier', SourceNodeName.text(), 'targetAgency', 'targetIdentifier', 'default value', '')

Additional settings in Groovy Script
------------------------------------
- Set a source value to an exchange property value:		'TargetNodeName' message.setProperty('ExchangePropertyName', SourceNodeName.text())
- Set a source value to a header value:					'TargetNodeName' message.setHeader('HeaderValueName', SourceNodeName.text())
- Set a source value to message type in header:			'TargetNodeName' message.setHeader('SAP_MessageType', SourceNodeName.text())
- Dynamic configuration for adapter and other objects:	https://help.sap.com/docs/cloud-integration/sap-cloud-integration/headers-and-exchange-properties-provided-by-integration-framework
*/

/**
* Checks if locale is valid.
* @param value This is the value.
* @return result Returns true if locale is valid.
*/
private def boolean isValidLocale(String value) {
	boolean result = false
	Locale[] locales = Locale.getAvailableLocales()
	for (Locale locale : locales) {
		if (value.equals(locale.toString())) {
			result = true
		}
	}
	return result
}

/**
* Passes a value from value node when the corresponding value from condition node equals such a value.
* @param conditionValue This is the condition value.
* @param suchValues This is the such values.
* @param returnValue This is the return value.
* @return result Returns a value when the corresponding value has one of the such value contained.
*/
private def String assignValueByCondition(String conditionValue, def suchValues, String returnValue) {
	String[] theSuchValues
	String result = ''
	if (suchValues instanceof List) {
		theSuchValues = suchValues
	} else {
		theSuchValues = [suchValues.toString()]
	}
	// Check suchValue
	if (!theSuchValues[0]) {
		throw new Exception("assignValueByCondition: There is no suchValue.")
	}
	for (int i = 0; i < theSuchValues.size(); i++) {
		if (conditionValue.equals(theSuchValues[i])) {
			result = returnValue
			break
		}
	}
	return result
}

/**
* Gets dynamic substring from value. First argument is value. Second argument is start, begins with '0'. Third argument is length.
* @param value Value
* @param start Start
* @param length Length
* @return result Returns the substring.
*/
private def String dynamicSubstring(String value, int start, int length) {
	int startPos = start
	int endPos = start + length - 1
	String result = ''
	if (startPos < 0) {
		// Start position is before start of value, return empty string
		result = ''
	} else if (startPos >= 0 && startPos < value.length()) {
		if (endPos < value.length()) {
			// Start & end positions are before end of value, return the partial substring
			result = value.substring( startPos, endPos + 1 )
		 } else if (endPos >= value.length() ) {
			// Start position is before start of value but end position is after end of value, return from start till end of value
			result = value.substring(startPos, value.length())
		}
	} else if (startPos >= value.length()) {
		// Start position is after end of value, return empty string
		result = ''
	}
	return result
}

/**
* Creates a new string with leading zeros from a non-empty first argument with length given by the second. Values will not be cut.
* @param value Value
* @param length Length
* @return result Returns an new string with leading zeros.
*/
private def String fillLeadingZeros(String value, int totalLength) {
	String result = value
	if (value && totalLength > 0) {
		int repeatLength = totalLength - value.length()
		if (repeatLength > 0) {
			result = '0'.multiply(repeatLength) + result
		}
	}
	return result
}

/**
* Creates a new string with space at end of string from a non-empty first argument with length given by the second. Values will not be cut.
* @param value Value
* @param totalLength Total length
* @return result Returns an new string with spaces at end of string.
*/
private def String fillUpToLengthWithSpace(String value, int totalLength) {
	String result = value
	if (value && totalLength > 0) {
		int repeatLength = totalLength - value.length()
		if (repeatLength > 0) {
			result += ' '.multiply(repeatLength)
		}
	}
	return result
}

/**
* fixedValues
* @param value This is the value.
* @param mapTable This is the table.
* @param behaviour This is the behaviour if lookup fails. ('Default value', 'Use key', 'Throw exception')
* @param defaultValue This is the default value.
* @return result Returns the result.
*/
private def String fixedValues(String value, def mapTable, String behaviour, String defaultValue) {
	String result = ''
	behaviour = behaviour.toLowerCase().replaceAll(' ','')
	// Check input values
	if (mapTable == null || mapTable.any() == false) {
		throw Exception("fixedValues: No 'mapTable' parameter found in 'fixedValues' for value '$value'.")
	} else if (!('defaultvalue'.equals(behaviour) || 'usekey'.equals(behaviour) || 'throwexception'.equals(behaviour))) {
		throw Exception("fixedValues: No allowed 'behaviour' parameter found in 'fixedValues' for value '$value'. 'Default value', 'Use key' and 'Throw exception' are allowed.")
	}
	//Do the mapping
	result = mapTable.get(value)
	// Error strategy
	if(result == null) {
		switch(behaviour) {
			case 'throwexception':
				throw Exception("fixedValues: No entry for value '$value' found in 'fixedValues' map table [" + mapTable.collect{it}.join(',') + "].")
				break
			case 'usekey':
				result = value
				break
			case 'defaultvalue':
				result = defaultValue
				break
			default:
				throw Exception("fixedValues: Behaviour parameter '$behaviour' not set properly in 'fixedValues'.")
				break
		}
	}
	return result
}

/**
* Format the number by defined pattern and decimal separator.
* @param numberStr Number string
* @param pattern Pattern (number format)
* @param decimalSeparator Decimal separator in output number string
* @return result Returns an formatted number string.
*/
private def String formatNumber(String numberStr, String pattern, String decimalSeparator) {
	String result = numberStr
	DecimalFormat decimalFormat
	if (numberStr && pattern && decimalSeparator) {
		char decimalSeparatorChar = decimalSeparator.charAt(0)
		// Set decimal format symbols
		DecimalFormatSymbols symbols = new DecimalFormatSymbols()
		symbols.setDecimalSeparator(decimalSeparatorChar)
		if (decimalSeparator.startsWith(',')) {
			char groupingSeparatorChar = '.'
			symbols.setGroupingSeparator(groupingSeparatorChar)
		}
		// Convert input string to double
		double numberDouble = 0
		try {
			numberDouble = Double.parseDouble(numberStr)
		} catch (Exception) {
			throw Exception("formatNumber: Cannot cast '$numberStr' to decimal number.")
		}
		// Initialize decimal formatter
		try {
			decimalFormat = new DecimalFormat(pattern, symbols)
		} catch (Exception) {
			throw Exception("formatNumber: Malformed pattern '$pattern'.")
		}
		// Format number string
		try {
			result = decimalFormat.format(numberDouble)
		} catch (Exception) {
			throw Exception("formatNumber: '$numberStr' could not change to pattern '$pattern'.")
		}
	}
	return result
}

/**
* Creates a new string from a non empty first argument with length given by the second as follows: cut at left or right end (the third argument) or fill (if the fourth says 'true') at left or right side (defined by the fifth).
* @param value Value
* @param length Length
* @param cutLengthDirection Cut length direction 'left' or 'right' end cut
* @param fillSpace Fill space 'true' = fill
* @param fillSpaceDirection Fill space direction 'left' or 'right' end fill
* @return result Returns a new string from a non empty.
*/
private def String formatValueBySpace(String value, int length, String cutLengthDirection, boolean fillSpace, String fillSpaceDirection) {
	String result = null
	if (value != null) {
		int lengthValue = value.length()
		if (lengthValue > 0 && lengthValue != length) {
			if (lengthValue > length) {
				if ("left".equalsIgnoreCase(cutLengthDirection)) {
					int offset = lengthValue - length
					result = value.substring(offset, lengthValue)
				} else if ("right".equalsIgnoreCase(cutLengthDirection)) {
					result = value.substring(0, length)
				} else {
					throw new Exception("formatValueBySpace: Unexpected value '$cutLengthDirection' for the cutDirection.")
				}
			} else {
				if (fillSpace) {
					// now lengthValue < length
					int offset = length - lengthValue
					String SpaceString = " "
					for (int i = 0; i < offset - 1; i++) {
						SpaceString += " "
					}
					if ("left".equalsIgnoreCase(fillSpaceDirection)) {
						result = SpaceString + value
					} else if ("right".equalsIgnoreCase(fillSpaceDirection)) {
						result = value + SpaceString
					} else {
						throw new Exception("formatValueBySpace: Unexpected value '$fillSpaceDirection' for the fillDirection.")
					}
				}
			}
		}
		if (result == null) {
			result = value
		}
	} else {
		result = value
	}
	return result
}

/**
* Creates a new string from a non-empty first argument with length given by the second as follows: cut at left or right end (the third argument) or fill with '0' (if the fourth says 'true') at left or right side (defined by the fifth).
* @param value Value
* @param length Length
* @param cutLengthDirection Cut length direction 'left' or 'right' end cut
* @param fillZero Fill zero 'true' = fill
* @param fillZeroDirection Fill zero direction 'left' or 'right' end fill
* @return result Returns a new string with zeros.
*/
private def String formatValueByZero(String value, int length, String cutLengthDirection, boolean fillZero, String fillZeroDirection) {
	String result = null
	if (value) {
		int lengthValue = value.length()
		if (lengthValue > 0 && lengthValue != length) {
			if (lengthValue > length) {
				if ("left".equalsIgnoreCase(cutLengthDirection)) {
					int offset = lengthValue - length
					result = value.substring(offset, lengthValue)
				} else if ("right".equalsIgnoreCase(cutLengthDirection)) {
					result = value.substring(0, length)
				} else {
					throw new Exception("formatValueByZero: Unexpected value '$cutLengthDirection' for the cutDirection.")
				}
			} else {
				if (fillZero) {
					// now lengthValue < length
					int offset = length - lengthValue
					String zeroString = "0"
					for (int i = 0; i < offset - 1; i++) {
						zeroString += "0"
					}
					if ("left".equalsIgnoreCase(fillZeroDirection)) {
						result = zeroString + value
					} else if ("right".equalsIgnoreCase(fillZeroDirection)) {
						result = value + zeroString
					} else {
						throw new Exception("formatValueByZero: Unexpected value '$fillZeroDirection' for the fillDirection.")
					}
				}
			}
		}
		if (result == null) {
			result = value
		}
	} else {
		result = value
	}
	return result
}

/**
* getDateAfterDays
* @param timeZone This is the time zone.
* @param date This is the date.
* @param dateInFormat This is the date in format.
* @param dateOutFormat This is the date out format.
* @return result Returns a formated date.
*/
private String getDateAfterDays(def timeZone, String date, String dateInFormat, int days, String dateOutFormat) {
	String result = ''
	def dateIn
	def dateOut
	if (timeZone && date && dateInFormat && dateOutFormat) {
		try {
			// Initialize date in
			SimpleDateFormat sdfIn = new SimpleDateFormat(dateInFormat)
			sdfIn.setTimeZone(timeZone)
			dateIn = sdfIn.parse(date)
		} catch (Exception ex) {
			throw new Exception("getDateAfterDays: Cannot parse date in '$date' to date format '$dateInFormat'.")
		}
		// Add x days
		dateIn = dateIn.plus(days)
		try {
			// Set date out
			SimpleDateFormat sdfOut = new SimpleDateFormat(dateOutFormat)
			sdfOut.setTimeZone(timeZone)
			// Format date
			dateOut = sdfOut.format(dateIn)
			result = dateOut.toString()
		} catch (Exception ex) {
			throw new Exception("getDateAfterDays: Cannot parse date out '$dateIn' to date format '$dateOutFormat'.")
		}
	}
 	return result
}

/**
* Gets an EDIFACT-F2379-conform qualifier descriping the dates format.
* @param date Date
* @return result Return an EDIFACT-F2379-conform qualifier.
*/
private def String getDateFormat(String date) {
	String result = ''
	String dateString = date != null ? date.trim() : null
	if (date != null && dateString.length() > 0) {
		int dateLength = date.length()
		switch (dateLength) {
			case 6:
				result = '101'
				break
			case 8:
				result = '102'
				break
			case 10:
				result = '201'
				break
			case 12:
				String yearBegin = date.substring(0, 2)
				if ('20'.equalsIgnoreCase(yearBegin) || '19'.equalsIgnoreCase(yearBegin)) {
					result = '203'
				} else {
					result = '202'
				}
				break
			case 14:
				result = '204'
				break
			default:
				throw new Exception("getDateFormat: The date length '$dateLength' cannot be processed.")
		}
	}
	return result
}

/**
* getExchangeProperty
* @param message This is message.
* @param propertyName This is name of property.
* @param mandatory This is parameter if property is mandatory.
* @return propertyValue Return property value.
*/
private def getExchangeProperty(Message message, String propertyName, boolean mandatory) {
	String propertyValue = message.properties.get(propertyName) as String
	if (mandatory) {
		if (!propertyValue) {
			throw Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}

/**
* getHeader
* @param message This is message.
* @param headerName This is name of header.
* @param mandatory This is parameter if header is mandatory.
* @return headerValue Return header value.
*/
private getHeader(Message message, String headerName, boolean mandatory) {
	String headerValue = message.getHeaders().get(headerName) as String
	if (mandatory) {
		if (!headerValue) {
			throw Exception("Mandatory header '$headerName' is missing.")
		}
	}
	return headerValue
}

/**
* Gets monday of week. First argument is year. Second argument is week. Default locale is used.
* @param locale This is locale.
* @param year This is the year.
* @param week This is the Week.
* @return date Returns date of monday of week.
*/
private def String getMondayOfWeek(Locale locale, String year, String week) {
	final String DATE_IN_FORMAT = "yyyyww"
	final String DATE_OUT_FORMAT = "yyyyMMdd"
	String result = ''
	Calendar calendar = null
	Date date = null
	String weekOfYear = ''
	if (locale && year && week) {
		// Check year and create 4 digits
		try {
			int yearInt = Integer.parseInt(year)
			yearInt = yearInt.abs()
			if (yearInt < 100) {
				yearInt = 2000 + yearInt
			}
			year = yearInt.toString()
		} catch (Exception ex) {
			throw new Exception("getMondayOfWeek: cannot parse year '$year' to integer.")
		}
		// Check week
		try {		
			int weekInt = Integer.parseInt(week)
			week = weekInt.abs().toString()
		} catch (Exception ex) {
			throw new Exception("getMondayOfWeek: cannot parse week '$week' to integer.")
		}
		try {
			// Set week
			DateFormat formatterWeek = new SimpleDateFormat(DATE_IN_FORMAT, locale)
			weekOfYear = year + week
			date = formatterWeek.parse(weekOfYear)
			// Set monday of calendar week
			calendar = Calendar.getInstance(locale)
			calendar.setTimeInMillis(date.getTime())
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
		} catch (Exception ex) {
			throw new Exception("getMondayOfWeek: Cannot parse date '$weekOfYear' to date format '$DATE_IN_FORMAT'.")
		}
		try {
			// Get formatted day
			DateFormat formatterDate = new SimpleDateFormat(DATE_OUT_FORMAT, locale)
			formatterDate.setLenient(false)
			result = formatterDate.format(calendar.getTime())
		} catch (Exception ex) {
			throw new Exception("getMondayOfWeek: Cannot parse date '$weekOfYear' to date format '$DATE_OUT_FORMAT'.")
		}
	}
	return result
}

/**
* Get only numbers from string.
* @param value Value
* @return result Returns a value with only numbers.
*/
private def String getOnlyNumbers(String value) {
	String result = ''
	if(value) {
		// Use RegEx for remove all spaces
		result = value.replaceAll('[^0-9]', '')
	}
	return result
}

/**
* Gets a single value from value node when the corresponding value from condition node equals such a value.
* @param conditionPath This is the conditionPath object.
* @param nodeNameConditionValue This is the node name of condition value.
* @param suchValue This is the such value.
* @param nodeNameValue This is the node name of value.
* @return result Returns a value from the third argument when the corresponding value from first argument has one of the value contained.
*/
private def String getValueByCondition(def conditionPath, String nodeNameConditionValue, String suchValue, String nodeNameValue) {
	String result = ''
	// Check suchValue
	if (!suchValue) {
		throw new Exception("getValueByCondition: There is no suchValue.")
	}
	if (conditionPath && nodeNameConditionValue && nodeNameValue) {
		result = conditionPath.find{it."$nodeNameConditionValue".toString().equals(suchValue)}."$nodeNameValue".toString()
	}
	return result
}

/**
* Removes the trailing characters of the first argument leaving the number of characters given by the second argument.
* @param value Value
* @param headLength Head length
* @return result Returns head string of input value.
*/
private def String headString(String value, int headLength){
	String result = null
	if (value != null && headLength > 0) {
		if (value.length() > headLength) {
			result = value.substring(0, headLength)
		} else {
			result = value
		}
	} else {
		result = value
	}
	return result
}

/**
* Check if value is a number.
* @param value Value
* @return result Return 'true' if the argument is a number, 'false' otherwise.
*/
private def boolean isNumber(String value) {
	try {
		Double.parseDouble(value)
		return true
	} catch (Exception ex) {
		return false
	}
}

/**
* Map a default value if node is empty.
* @param value Value
* @param value Value
* @return result Returns a value.
*/
private def String mapWithDefault(String value, String defaultValue) {
	String result = value
	if (!value) {
		result = defaultValue
	}
	return result
}

/**
* Sets the minus sign from the beginning to the end.
* @param value Value
* @return result Returns a value with the minus at the end.
*/
private def String minusFromBeginToEnd(String value) {
	String result = value
	if (value != null && value.startsWith("-")) {
		result = value.substring(1, value.length()) + "-"
	}
	return result
}

/**
* Sets the minus sign from the end to the beginning.
* @param value Value
* @return result Returns a value with the minus at the beginning.
*/
private def String minusFromEndToBegin(String value) {
	String result = value
	if (value != null && value.endsWith("-")) {
		result = "-" + value.substring(0, value.length() - 1)
	}
	return result
}

/**
* Removes algebraic sign plus and minus from value.
* @param value Value
* @return string without algebraic sign plus and minus.
*/
private def String removeAlgebraicSign(String value) {
	String result = ''
	// Check value
	if(value) {
		if (value.startsWith('-')) {
			result = value.substring(1, value.length())
		} else if (value.startsWith('+')) {
			result = value.substring(1, value.length())
		} else if (value.endsWith('-')) {
			result = value.substring(0, value.length() - 1)
		} else {
			result = value
		}
	}
	return result
}

/**
* Removes only algebraic sign plus from value.
* @param value Value
* @return string without algebraic sign plus.
*/
private def String removeAlgebraicSignPlus(String value) {
	String result = ''
	// Check value
	if(value.length() == 0){
		result = ''
	} else if (value.startsWith('+')) {
		result = value.substring(1, value.length())
	} else {
		result = value
	}
	return result
}

/**
* Remove all characters from string.
* @param value Value
* @return result Returns a string without characters.
*/
private String removeAllCharacters(String value) {
	String result = ''
	if (value) {
		// Use RegEx for remove all characters
		result = value.replaceAll("[a-zA-ZäüöÄÜÖß]","")
	}
	return result
}

/**
* Remove all spaces from string.
* @param value Value
* @return result Returns a value without spaces.
*/
private def String removeAllSpaces(String value) {
	String result = ''
	if(value) {
		// Use RegEx for remove all spaces
		result = value.replaceAll(" ","")
	}
	return result
}

/**
* Remove all special characters from string.
* @param value Value
* @return result Returns a value without special characters.
*/
private def String removeAllSpecialCharacters(String value) {
	String result = ''
	if (value) {
		// Use RegEx for remove all special characters
		result = value.replaceAll("[^a-zA-Z0-9/.,: _+-]+","")
	}
	return result
}

/**
* Remove carriage return line feed.
* @param value Value
* @return result Returns a value without carriage return line feed.
*/
private def String removeCarriageReturnLineFeed(String value) {
	String result = ''
	if(value) {
		// Use RegEx for remove carriage return line feed
		result = value.replaceAll("[\r\n]","")
	}
	return result
}

/**
* Removes decimal if zero. The dot is used as decimal separator.
* @param number Number
* @return result Returns a value without decimal if zero.
*/
private def String removeDecimalIfZero(String number) {
	String firstPart = ""
	String secondPart = ""
	int indexOfDelimiter = 0
	String result = ""
	if (number) {
		indexOfDelimiter = number.indexOf(".")
		if (indexOfDelimiter == -1) {
			result = number
		} else {
			firstPart = number.substring(0, indexOfDelimiter)
			secondPart = number.substring(indexOfDelimiter + 1)
			if (new Integer(secondPart).intValue() == 0) {
				result = firstPart
			} else {
				result = number
			}
		}
	}
	return result
}

/**
* Replaces all special characters in string.
* @param value Value
* @return result Returns string without special characters.
*/
private def String replaceSpecialCharacters(String value) {
	String result = ''
	if (value) {
		result = value
		// Replacements - Using Unicode-Escapes
		Map map = new HashMap()
		map.put("À", "A")	// À
		map.put("Á", "A")	// Á
		map.put("Â", "A")	// Â
		map.put("Ã", "A")	// Ã
		map.put("Ä", "Ae")	// Ä
		map.put("Å", "A")	// Å
		map.put("Æ", "A")	// Æ
		map.put("Ç", "C")	// Ç
		map.put("Ð", "D")	// Ð
		map.put("È", "E")	// È
		map.put("É", "E")	// É
		map.put("Ê", "E")	// Ê
		map.put("Ë", "E")	// Ë
		map.put("Ƒ", "F")	// Ƒ
		map.put("Ì", "I")	// Ì
		map.put("Í", "I")	// Í
		map.put("Î", "I")	// Î
		map.put("Ï", "I")	// Ï
		map.put("Ñ", "N")	// Ñ
		map.put("Ò", "O")	// Ò
		map.put("Ó", "O")	// Ó
		map.put("Ô", "O")	// Ô
		map.put("Õ", "O")	// Õ
		map.put("Ö", "Oe")	// Ö
		map.put("Ø", "O")	// Ø
		map.put("Œ", "OE")	// Œ
		map.put("Š", "S")	// Š
		map.put("Ş", "S")	// Ş
		map.put("Ù", "U")	// Ù
		map.put("Ú", "U")	// Ú
		map.put("Û", "U")	// Û
		map.put("Ü", "Ue")	// Ü
		map.put("Ÿ", "Y")	// Ÿ
		map.put("Ý", "Y")	// Ý
		map.put("Ž", "Z")	// Ž
		map.put("à", "a")	// à
		map.put("á", "a")	// á
		map.put("â", "a")	// â
		map.put("ã", "a")	// ã
		map.put("ä", "ae")	// ä
		map.put("å", "a")	// å
		map.put("æ", "ae")	// æ
		map.put("ç", "c")	// ç
		map.put("ð", "d")	// ð
		map.put("è", "e")	// è
		map.put("é", "e")	// é
		map.put("ê", "e")	// ê
		map.put("ë", "e")	// ë
		map.put("ƒ", "f")	// ƒ
		map.put("ì", "i")	// ì
		map.put("í", "i")	// í
		map.put("î", "i")	// î
		map.put("ï", "i")	// ï
		map.put("ñ", "n")	// ñ
		map.put("ò", "o")	// ò
		map.put("ó", "o")	// ó
		map.put("ô", "o")	// ô
		map.put("õ", "o")	// õ
		map.put("ö", "oe")	// ö
		map.put("ø", "o")	// ø
		map.put("œ", "oe")	// œ
		map.put("š", "s")	// š
		map.put("ş", "s")	// ş
		map.put("ù", "u")	// ù
		map.put("ú", "u")	// ú
		map.put("û", "u")	// û
		map.put("ü", "ue")	// ü
		map.put("ÿ", "y")	// ÿ
		map.put("ý", "y")	// ý
		map.put("ž", "z")	// ž
		map.put("ß", "ss")	// ß
		map.put("®", "(R)")	// ®
		map.put("©", "(C)")	// ©
		map.put("±", "+-")	// 
		map.put("²", "^2")	// ²
		map.put("³", "^3")	// ³
		map.put("´", "'")	// ´
		map.put("`", "'")	// `
		map.put("µ", "^10−6")// µ
		map.put("°","grade")	// °
		map.put("™","TM")	// ™
		// Replace values in map with RegEx
		Set entrySet = map.entrySet()
		for (Iterator it = entrySet.iterator(); it.hasNext();) {
			Map.Entry mapEntry = (Map.Entry) it.next()
			result = result.replaceAll("[" + (String) mapEntry.getKey() + "]", (String) mapEntry.getValue())
		}
	}
	return result
}

/**
* Replaces all umlauts in string.
* @param value Value
* @return result Returns a value without umlauts.
*/
private def String replaceUmlauts(String value) {
	String result = ''
	if (value) {
		result = value
		// Replacements - Using Unicode-Escapes
		Map map = new HashMap()
		map.put('ä', 'ae') // ä
		map.put('ö', 'oe') // ö
		map.put('ü', 'ue') // ü
		map.put('ß', 'ss') // ß
		map.put('Ä', 'Ae') // Ä
		map.put('Ö', 'Oe') // Ö
		map.put('Ü', 'Ue') // Ü
		// Replace values in map with RegEx
		Set entrySet = map.entrySet()
		for (Iterator it = entrySet.iterator(); it.hasNext();) {
			Map.Entry mapEntry = (Map.Entry) it.next()
			result = result.replaceAll("[" + (String) mapEntry.getKey() + "]", (String) mapEntry.getValue())
		}
	}
	return result
}

/**
* Gets true if value from value node is corresponding to segment value, otherwise false.
* @param conditionPath This is the conditionPath object.
* @param nodeNameConditionValue This is the node name of condition value.
* @param suchValue This is the such value.
* @return result Returns true if value from value node is corresponding to segment value, otherwise false.
*/
private def boolean segmentHasOneOfSuchValues(def conditionPath, String nodeNameConditionValue, def suchValues) {
	String[] theSuchValues
	String value = null
	boolean result = false
	// Get suchValues
	if (suchValues instanceof List) {
		theSuchValues = suchValues
	} else {
		theSuchValues = [suchValues.toString()]
	}
	// Check suchValues
	if (!theSuchValues[0]) {
		throw new Exception("segmentHasOneOfSuchValues: There is no suchValue.")
	}
	// Search for value in segment
	if (conditionPath && nodeNameConditionValue) {
		for (int i = 0; i < theSuchValues.size(); i++) {
			value = conditionPath.find{it."$nodeNameConditionValue".toString().equals(theSuchValues[i])}."$nodeNameConditionValue".toString()
			if (value) {
				result = true
				break
			}
		}
	}
	return result
}

/**
* Get current date
* @param timeZone This is the time zone.
* @param dateFormat This is the date format.
* @return result Returns current formated date.
*/
private def String setCurrentDate(def timeZone, String dateFormat) {
	String result = ''
	def date
	def dateOut
	if (timeZone) {
		try {
			// Set simple date format
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat)
			sdf.setTimeZone(timeZone)
			// Create new date
			date = new Date()
			// Format date
			dateOut = sdf.format(date)
			result = dateOut.toString()
		} catch (Exception ex) {
			throw new Exception("setCurrentDate: Cannot parse date '$date' to date format '$dateFormat'.")
		}
	}
	return result
}

/**
* Sets decimal separator to point and remove thousands separator.
* @param value Value
* @return value with decimal separator point witout thousands separator.
*/
private def String setDecimalSeparatorPoint(String value) {
	String result = ''
	if(value) {
		// Remove whitespaces
		result = value.replaceAll('\\s','')
		// Remove multiple comma thousands separators
		if(result.indexOf(',') > -1 && result.lastIndexOf(',') < result.lastIndexOf('.')) {
			result = result.replaceAll(',','')
		}
		// Remove multiple point thousands separator only if comma is decimal separator
		if(result.indexOf('.') > -1 && result.lastIndexOf('.') < result.lastIndexOf(',')) {
			result = result.replaceAll('\\.','')
		}
		// Replace comma to point
		result = result.replaceAll(',','.')
	}
	return result
}

/**
* Set default as current date
* @param timeZone This is the time zone.
* @param value This is the date value.
* @param dateFormat This is the date format.
* @return result Returns current formated date.
*/
private String setDefaultAsCurrentDate(def timeZone, String value, String dateFormat) {
	String result = ''
	def date
	def dateNew
	if (timeZone) {
		if (value) {
			result = value
		} else {
			try {
				// Set simple date format
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat)
				sdf.setTimeZone(timeZone)
				// Create new date
				date = new Date()
				// Format date
				dateNew = sdf.format(date)
				result = dateNew.toString()
			} catch (Exception ex) {
				throw new Exception("setDefaultAsCurrentDate: Cannot parse date '$date' to date format '$dateFormat'.")
			}
		}
	}
	return result
}

/**
* Compute and set directory.
* @param message This is the message.
* @param timeZone This is the time zone.
* @param rootFolder This is the root folder.
* @param addYear Add year ('true','false').
* @param addMonth Add month ('true','false').
* @param addDay Add day ('true','false').
* @param sender This is the sender.
* @param messageType This is the message type.
* @param messageVersion This is the message version.
* @param messageRelease This is the message release.
* @return result Returns a directory path.
*/
private def String setDirectory(Message message, def timeZone, String rootFolder, boolean addYear, boolean addMonth, boolean addDay, String sender, messageType, String messageVersion, String messageRelease) {
	final String DIRECTORY_SEPARATOR = '/'
	final String ROOT_FOLDER = 'Archive'
	String result = ''
	def date
	def dateNew
	def dateNewStr = ''
	// Set directory
	String directory = DIRECTORY_SEPARATOR + rootFolder
	if (!rootFolder) {
		directory = DIRECTORY_SEPARATOR + ROOT_FOLDER
	}
	if (timeZone && directory) {
		// Get current date
		try {
			// Set simple date format
			String dateFormat = 'yyyyMMdd'
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat)
			sdf.setTimeZone(timeZone)
			// Create new date
			date = new Date()
			// Format date
			dateNew = sdf.format(date)
			dateNewStr = dateNew.toString()
		} catch (Exception ex) {
			throw new Exception("setDirectory: Cannot parse date '$date' to date format '$dateFormat'.")
		}
		// Add year
		if (addYear) {
			directory += DIRECTORY_SEPARATOR + dateNewStr.substring(0, 4)
		}
		// Add month
		if (addMonth) {
			directory += DIRECTORY_SEPARATOR + dateNewStr.substring(4, 6)
		}
		// Add day
		if (addDay) {
			directory += DIRECTORY_SEPARATOR + dateNewStr.substring(6, 8)
		}
		// Add sender
		if (sender) {
			directory += DIRECTORY_SEPARATOR + sender
		}
		// Add message type
		if (messageType) {
			directory += DIRECTORY_SEPARATOR + messageType
		}
		// Add message version
		if (messageVersion) {
			directory += DIRECTORY_SEPARATOR + messageVersion
		}
		// Add message release
		if (messageRelease) {
			directory += DIRECTORY_SEPARATOR + messageRelease
		}
		// Create valid directory with RegEx. Remove invalide character.
		// Supported characters for file name are letters, numbers, spaces, and '()_/-.'.
		directory = directory.replaceAll('[^a-zA-Z0-9 ( ) _ / \\- .]', '')
		// Remove leading and tailing whitespace with RegEx
		directory = directory.replaceAll('^\\s+|\\s+$','')
		// Set directory to header
		message.setHeader('CamelFilePath', directory)
		result = directory
	} else {
		throw new Exception("setDirectory: Directory can not be empty. Please check all input parameter.")
	}
	return result
}

/**
* Compute and set file name.
* @param message This is the message.
* @param timeZone This is the time zone.
* @param messageType This is the message type.
* @param messageVersion This is the message version.
* @param messageRelease This is the message release.
* @param addTimeStamp Add time stamp ('true','false').
* @param dateFormat This is the date format.
* @param documentNumber This is the document number.
* @param addMessageId Add message ID ('true','false').
* @param addCorrelationId Add correlation ID ('true','false').
* @param nameSeparator This is the name separator.
* @param fileType This is the file type.
* @return result Returns a file name.
*/
private def String setFileName(Message message, def timeZone, String messageType, String messageVersion, String messageRelease, boolean addTimeStamp, String dateFormat, String documentNumber, boolean addMessageId, boolean addCorrelationId, String nameSeparator, String fileType) {
	final String NAME_SEPARATOR = '_'
	final String DATE_FORMAT = 'yyyy-MM-dd_HH-mm-ss.SSS'
	String fileName = messageType
	String result = ''
	def date
	def dateNew
	String timeStamp = ''
	if (messageType && fileType && addMessageId != null && addCorrelationId != null && addTimeStamp != null) {
		// Set nameSeparator
		if (!nameSeparator) {
			nameSeparator = NAME_SEPARATOR
		}
		// Add EDI message release
		if (messageVersion) {
			fileName += nameSeparator + messageVersion
		}
		// Add EDI message release
		if (messageRelease) {
			fileName += nameSeparator + messageRelease
		}
		// Add time stamp
		if (addTimeStamp) {
			try {
				if (!dateFormat) {
					dateFormat = DATE_FORMAT
				}
				// Set simple date format
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat)
				sdf.setTimeZone(timeZone)
				// Create new date
				date = new Date()
				// Format date
				dateNew = sdf.format(date)
				timeStamp = dateNew.toString()
			} catch (Exception ex) {
				throw new Exception("setFileName: Cannot parse date '$date' to date format '$dateFormat'.")
			}
			fileName += nameSeparator + timeStamp
		}
		// Add document number
		if (documentNumber) {
			fileName += nameSeparator + documentNumber
		}
		// Add message ID
		if (addMessageId) {
			String messageId = message.getHeader("SAP_MessageProcessingLogID", String)
			if (!messageId) {
				// Create an ID
				messageId = UUID.randomUUID().toString().replaceAll('-', '').toUpperCase()
			}
			fileName += nameSeparator + messageId
		}
		// Add correlation ID
		if (addCorrelationId) {
			String correlationId = message.getHeader("SAP_MplCorrelationId", String)
			if (!correlationId) {
				// Create an ID
				correlationId = UUID.randomUUID().toString().replaceAll('-', '').toUpperCase()
			}
			fileName += nameSeparator + correlationId
		}
		// Add file type
		if (!fileType.startsWith('.')) {
			fileName += '.' + fileType
		}
		// Create valid file name with RegEx. Remove invalide character.
		// Supported characters for file name are letters, numbers, spaces, and '()_-,.'.
		fileName = fileName.replaceAll('[^a-zA-Z0-9 ( ) _ \\- , .]', '')
		// Remove leading and tailing whitespace with RegEx
		fileName = fileName.replaceAll('^\\s+|\\s+$','')
		// Set file name to header
		message.setHeader("CamelFileName", fileName)
		result = fileName
	} else {
		throw new Exception("setFileName: File name can not be empty. Please check all input parameter.")
	}
	return result
}

/**
* Remove leading and tailing whitespaces from string.
* @param value Value
* @return result Returns a value without leading and tailing whitespaces.
*/
private def String stripSpaces(String value) {
	String result = ''
	if(value) {
		// Use RegEx for remove leading and tailing whitespaces from string
		result = value.replaceAll('^\\s+|\\s+$', '')
	}
	return result
}

/**
* Removes the leading characters of the first argument leaving the number of characters given by the second argument. Leading or trailing white spaces do not count.
* @param value Value
* @param tailLength Tail length
* @return result Returns tail string of input value.
*/
private def String tailString(String value, int tailLength) {
	String result = ''
	if (value != null && tailLength > 0) {
		String trimmedValue = ""
		for (int i = value.length(); i > 0; i--) {
			if (value.charAt(i - 1) != ' ') {
				trimmedValue = value.substring(0, i)
				break
			}
		}
		int length = trimmedValue.length()
		if (length > tailLength) {
			result = trimmedValue.substring(length - tailLength)
		} else {
			result = value
		}
	} else {
		result = value
	}
	return result
}

/**
* Throw exception if there is no value.
* @param value Value
* @param valueName Value name
* @return result Returns a exception if there is no value.
*/
private def String throwExceptionIfNoValue(String value, String valueName) {
	String returnMessage = ''
	// Create return message
	if (valueName.trim().length() != 0) {
		returnMessage = "Value for node '" + valueName + "' can not be blank."
	} else {
		returnMessage = "Value can not be blank."
	}
	// Check if there is a value
	if (!value || value.trim().length() == 0) {
		throw Exception(returnMessage)
	}
	return value
}

/**
* Formats a string like a number (removes + sign and leading/trailing zeros).
* @param value Value
* @return result Returns a number.
*/
private def String toNumber(String numberString) {
	if (numberString != null && numberString.trim().length() == 0) {
		return null
	}
	try {
		return new BigDecimal(numberString).toString()
	} catch (Exception ex) {
		throw new Exception("toNumber: can not transform numberString '$numberString' to a number.")
	}
}

/**
* transform Date
* @param timeZone This is the time zone.
* @param dateInString This is the date.
* @param dateInFormat This is the date in format.
* @param dateOutFormat This is the date out format.
* @return result Returns a formated date.
*/
private def String transformDate(def timeZone, String date, String dateInFormat, String dateOutFormat) {
	String result = ''
	def dateIn
	def dateOut
	if (timeZone && date) {
		try {
			// Initialize date in
			SimpleDateFormat sdfIn = new SimpleDateFormat(dateInFormat)
			sdfIn.setTimeZone(timeZone)
			dateIn = sdfIn.parse(date)
		} catch (Exception ex) {
			throw new Exception("transformDate: Cannot parse date in '$date' to date format '$dateInFormat'.")
		}
		try {
			// Set date out
			SimpleDateFormat sdfOut = new SimpleDateFormat(dateOutFormat)
			sdfOut.setTimeZone(timeZone)
			// Format date
			dateOut = sdfOut.format(dateIn)
			result = dateOut.toString()
		} catch (Exception ex) {
			throw new Exception("transformDate: Cannot parse date out '$dateOut' to date format '$dateOutFormat'.")
		}
	}
 	return result
}

/**
* Removes trailing white spaces (leading white spaces may be significant).
* @param value Value
* @return result Returns a value without trailing white spaces.
*/
private def String trimRight(String value) {
	String result = ''
	int length = value.length()
	if (length > 0) {
		char[] chars = value.toCharArray()
		int trailing = length - 1
		while (trailing > -1 && chars[trailing] == ' ') {
			trailing--
		}
		result = value.substring(0, trailing + 1)
	} else {
		result = value
	}
	return result
}

/**
* Removes leading zeros.
* @param value Value
* @return result Returns a number without leading zeros.
*/
private def String trimZeroLeft(String value) {
	String result = ''
	if (value) {
		if (value.trim().length() == 0) {
			result = value
		} else {
			result = value.replaceAll("^0*", "")
			if (result.trim().length() == 0) {
				result = "0"
			}
		}
	} else {
		result = value
	}
	return result
}

/**
* valueMap
* @param valueMapApi This is the valueMapApi object.
* @param sourceAgency This is the source agency.
* @param sourceIdentifier This is the source identifier.
* @param sourceKey This is the source key.
* @param targetAgency This is the target agency.
* @param targetIdentifier This is the target identifier
* @param behaviour This is the behaviour if lookup fails. ('Default value', 'Use key', 'Throw exception')
* @param defaultValue This is the default value.
* @return result Returns the result.
*/
private def String valueMap(def valueMapApi, String sourceAgency, String sourceIdentifier, String sourceKey, String targetAgency, String targetIdentifier, String behaviour, String defaultValue) {
	String result = ''
	behaviour = behaviour.toLowerCase().replaceAll(' ','')
	// Check input values
	if (!sourceAgency) {
		throw Exception("valueMap: No 'sourceAgency' parameter found in value mapping: '$sourceAgency:$sourceIdentifier' -> '$targetAgency:$targetIdentifier' for value '$sourceKey'.")
	} else if (!sourceIdentifier) {
		throw Exception("valueMap: No 'sourceIdentifier' parameter found in value mapping: '$sourceAgency:$sourceIdentifier' -> '$targetAgency:$targetIdentifier' for value '$sourceKey'.")
	} else if (!targetAgency) {
		throw Exception("valueMap: No 'targetAgency' parameter found in value mapping: '$sourceAgency:$sourceIdentifier' -> '$targetAgency:$targetIdentifier' for value '$sourceKey'.")
	} else if (!targetIdentifier) {
		throw Exception("valueMap: No 'targetIdentifier' parameter found in value mapping: '$sourceAgency:$sourceIdentifier' -> '$targetAgency:$targetIdentifier' for value '$sourceKey'.")
	} else if (!('defaultvalue'.equals(behaviour) || 'usekey'.equals(behaviour) || 'throwexception'.equals(behaviour))) {
		throw Exception("valueMap: No allowed 'behaviour' parameter '$behaviour' found in value mapping: '$sourceAgency:$sourceIdentifier' -> '$targetAgency:$targetIdentifier' for value '$sourceKey'. 'Default value', 'Use key' and 'Throw exception' are allowed.")
	} else if (!valueMapApi) {
		throw Exception("valueMap: No 'valueMapApi-object' parameter found in value mapping: '$sourceAgency:$sourceIdentifier' -> '$targetAgency:$targetIdentifier' for value '$sourceKey'.")
	}
	// Get target value for source key from value mapping
	result = valueMapApi.getMappedValue(sourceAgency, sourceIdentifier, sourceKey, targetAgency, targetIdentifier)
	// Error strategy
	if(result == null) {
		switch(behaviour) {
			case 'throwexception':
				throw Exception("valueMap: No entry for value '$sourceKey' found in value mapping: '$sourceAgency:$sourceIdentifier' -> '$targetAgency:$targetIdentifier'.")
				break
			case 'usekey':
				result = sourceKey
				break
			case 'defaultvalue':
				result = defaultValue
				break
			default:
				throw Exception("valueMap: Behaviour parameter '$behaviour' not set properly in value mapping: '$sourceAgency:$sourceIdentifier' -> '$targetAgency:$targetIdentifier'.")
				break
		}
	}
	return result
}