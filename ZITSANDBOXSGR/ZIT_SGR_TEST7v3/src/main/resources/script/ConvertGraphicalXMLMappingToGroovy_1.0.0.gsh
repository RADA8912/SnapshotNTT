import com.sap.gateway.ip.core.customdev.util.Message

/**
* ConvertGraphicalXMLMappingToGroovy
* This Groovy script reads a textual graphical mapping from SAP PO and creates a Groovy Script mapping template. A single attribute is also mapped to a node.
* Please note that textual graphical mapping need to have line break and an equal character.
* If there are mandatory segments that are not mepped, you should map it with an empty constant to get an entry in textual mapping.
* Supported mappings:
* - mapping of a constant
* - direct node-mappings without a formula
* - standard methods (concat(), copyValue(), count(), currentDate(), fixedValues(), formatNumber(), getMessageID(), mapWithDefault(), receiver(), replaceString(), sender(), substring(), 
*						toLowerCase(), toUpperCase(), transformDate(), trim(), valueMap())
* - user defined functions (UDF) (assignValueByCondition(), dynamicSubstring(), fillLeadingZeros(), fillUpToLengthWithSpace(), formatValueBySpace(), formatValueByZero(), getDateAfterDays(),
*							getExchangeProperty(), getDate(), getDateFormat(), getHeader(), getMondayOfWeek(), getOnlyNumbers(), getValueByCondition(), headString(), isNumber(),
*							minusFromBeginToEnd(), minusFromEndToBegin(), removeAlgebraicSign(), removeAlgebraicSignPlus(), removeAllCharacters(), removeAllSpaces(), removeAllSpecialCharacters(),
*							removeCarriageReturnLineFeed(), removeDecimalIfZero(), removeLeadingZeros(), replaceSpecialCharacters(), replaceUmlauts(), segmentHasOneOfSuchValues(),
*							setDecimalSeparatorPoint(), setDefaultAsCurrentDate(), setDirectory(), setFileName(), stripSpaces(), tailString(), throwExceptionIfNoValue(), toNumber(), trimRight()
*							trimZeroLeft())
* Sub segment creation is not supported yet.
* Dynamic target namespace is not supported yet. Only one static target namespace can configure yet.
* 'xsi:nill' is not supported yet.
*
* Groovy script parameters (exchange properties)
* - ConvertMap.DebugMode = Debug mode ('true', 'false')
* - ConvertMap.CreateOnlyDebug = Create only debug ('true', 'false')
* - ConvertMap.CreateTargetDescription = Create target description ('true', 'false')
* - ConvertMap.CreateTargetXpath = Create target xpath ('true', 'false')
* - ConvertMap.CreateMapRule = Create map rule ('true', 'false')
* - ConvertMap.CreateNoConvertableFormula = Create no convertable formula ('true', 'false')
* - ConvertMap.CreateSegmentHierarchy = Create segment hierarchy ('true', 'false')
* - ConvertMap.CreateRootTargetNamespace1 = Create root target namespace 1 ('true', 'false')
* - ConvertMap.CreateNodesTargetNamespace1 = Create Nodes Target Namespace 1 ('true', 'false')
* - ConvertMap.TargetNamespacePrefix1 = Target Namespace Prefix 1
* - ConvertMap.TargetNamespace1 = Target Namespace 1
* - ConvertMap.AppendAllGroovyCostumFunctions = Append all Groovy costum functions ('true', 'false')
* - ConvertMap.AppendMappingExamples = Append mapping examples ('true', 'false')
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {

/**
* Define constants and values
*/

	// Default values
	final boolean DEBUG_MODE = false
	final boolean CREATE_ONLY_DEBUG = false
	final boolean CREATE_TARGET_DESCRIPTION = true
	final boolean CREATE_TARGET_XPATH = true
	final boolean CREATE_MAP_RULE = true
	final boolean CREATE_NO_CONVERTABLE_FORMULA = true
	final boolean CREATE_SEGMENT_HIERARCHY = true
	final String TARGET_NODE_START = '/ns0:Messages/ns0:Message1'
	final String LINE_BREAK = '\r\n'
	// Target namespace
	final boolean CREATE_ROOT_TARGET_NAMESPACE_1 = false
	final boolean CREATE_NODES_TARGET_NAMESPACE_1 = false
	final String TARGET_NAMESPACE_PREFIX_1 = 'ns0'
	final String TARGET_NAMESPACE_1 = 'http://www.target-address.com'
	final boolean APPEND_ALL_GROOVY_COSTUM_FUNCTIONS = true
	final boolean APPEND_MAPPING_EXAMPLES = true
	// Configuration variables
	boolean debugMode = false
	boolean createOnlyDebug = false
	boolean createTargetDescription = false
	boolean createTargetXpath = false
	boolean createMapRule = false
	boolean createNoConvertableFormula = false
	boolean createSegmentHierarchy = false
	boolean createRootTargetNamespace1 = false
	boolean createNodesTargetNamespace1 = false
	boolean appendAllGroovyCostumFunctions = false
	boolean mappingExamples = false
	int l = -1
	def targetNodes = []
	// Configuration for target segment node. You can set it here if this is not part of body. For example: [true,false,false]
	def isTargetSegments = []
	def targetSegments = []
	def targetHierarchyLevels = []
	// Configuration for target description. You can set it here if this is not part of body. For example: ['Node A','Node B','Node C']
	def targetDescription = []
	// Configuration for map rule. You can set it here if this is not part of body. For example: ['constant \'1\'','1:1','1:1']
	def mapRules = []
	def sourceNodes = []
	def sourceSegmentsReduce = []
	def sourceTypes = []
	def lineActivation = []
	def lineTabs = []
	def rightCurlyBrackets = []
	String targetNode = ''
	String sourceNode = ''
	String sourceType = ''
	String sourceRootName = ''
	String bodyNew = ''
	boolean methodIsValidLocale = false
	boolean methodAssignValueByCondition = false
	boolean methodDynamicSubstring = false
	boolean methodFillLeadingZeros = false
	boolean methodFillUpToLengthWithSpace = false
	boolean methodFixedValues = false
	boolean methodFormatNumber = false
	boolean methodFormatValueBySpace = false
	boolean methodFormatValueByZero = false
	boolean methodGetDate = false
	boolean methodGetDateAfterDays = false
	boolean methodGetDateFormat = false
	boolean methodGetExchangeProperty = false
	boolean methodGetHeader = false
	boolean methodGetMondayOfWeek = false
	boolean methodGetOnlyNumbers = false
	boolean methodGetValueByCondition = false
	boolean methodHeadString = false
	boolean methodIsNumber = false
	boolean methodMapWithDefault = false
	boolean methodMinusFromBeginToEnd = false
	boolean methodMinusFromEndToBegin = false
	boolean methodRemoveAlgebraicSign = false
	boolean methodRemoveAlgebraicSignPlus = false
	boolean methodRemoveAllCharacters = false
	boolean methodRemoveAllSpaces = false
	boolean methodRemoveAllSpecialCharacters = false
	boolean methodRemoveCarriageReturnLineFeed = false
	boolean methodRemoveDecimalIfZero = false
	boolean methodReplaceSpecialCharacters = false
	boolean methodReplaceUmlauts = false
	boolean methodSegmentHasOneOfSuchValues = false
	boolean methodSetCurrentDate = false
	boolean methodSetDecimalSeparatorPoint = false
	boolean methodSetDefaultAsCurrentDate = false
	boolean methodSetDirectory = false
	boolean methodSetFileName = false
	boolean methodStripSpaces = false
	boolean methodTailString = false
	boolean methodThrowExceptionIfNoValue = false
	boolean methodToNumber = false
	boolean methodTransformDate = false
	boolean methodTrimRight = false
	boolean methodTrimZeroLeft = false
	boolean methodValueMap = false

/**
* Get exchange properties
*/

	// Debug mode
	String debugModeIn = getExchangeProperty(message, "ConvertMap.DebugMode", false)
	if (debugModeIn) {
		if ('true'.equalsIgnoreCase(debugModeIn)) {
			debugMode = true
		}
	} else {
		debugMode = DEBUG_MODE
	}
	// Create only debug (If 'true' no Groovy creation part is running.)
	String createOnlyDebugIn = getExchangeProperty(message, "ConvertMap.CreateOnlyDebug", false)
	if (createOnlyDebugIn) {
		if ('true'.equalsIgnoreCase(createOnlyDebugIn)) {
			createOnlyDebug = true
		}
	} else {
		createOnlyDebug = CREATE_ONLY_DEBUG
	}
	// Create target description
	String createTargetDescriptionIn = getExchangeProperty(message, "ConvertMap.CreateTargetDescription", false)
	if (createTargetDescriptionIn) {
		if ('true'.equalsIgnoreCase(createTargetDescriptionIn)) {
			createTargetDescription = true
		}
	} else {
		createTargetDescription = CREATE_TARGET_DESCRIPTION
	}
	// Create target XPATH as description
	String createTargetXpathIn = getExchangeProperty(message, "ConvertMap.CreateTargetXpath", false)
	if (createTargetXpathIn) {
		if ('true'.equalsIgnoreCase(createTargetXpathIn)) {
			createTargetXpath = true
		}
	} else {
		createTargetXpath = CREATE_TARGET_XPATH
	}
	// Create map rule as description
	String createMapRuleIn = getExchangeProperty(message, "ConvertMap.CreateMapRule", false)
	if (createMapRuleIn) {
		if ('true'.equalsIgnoreCase(createMapRuleIn)) {
			createMapRule = true
		}
	} else {
		createMapRule = CREATE_MAP_RULE
	}
	// Create no convertable formula
	String createNoConvertableFormulaIn = getExchangeProperty(message, "ConvertMap.CreateNoConvertableFormula", false)
	if (createNoConvertableFormulaIn) {
		if ('true'.equalsIgnoreCase(createNoConvertableFormulaIn)) {
			createNoConvertableFormula = true
		}
	} else {
		createNoConvertableFormula = CREATE_NO_CONVERTABLE_FORMULA
	}
	// Create segment hierarchy
	String createSegmentHierarchyIn = getExchangeProperty(message, "ConvertMap.CreateSegmentHierarchy", false)
	if (createSegmentHierarchyIn) {
		if ('true'.equalsIgnoreCase(createSegmentHierarchyIn)) {
			createSegmentHierarchy = true
		}
	} else {
		createSegmentHierarchy = CREATE_SEGMENT_HIERARCHY
	}
	// Create root target namespace 1
	String createRootTargetNamespace1In = getExchangeProperty(message, "ConvertMap.CreateRootTargetNamespace1", false)
	if (createRootTargetNamespace1In) {
		if ('true'.equalsIgnoreCase(createRootTargetNamespace1In)) {
			createRootTargetNamespace1 = true
		}
	} else {
		createRootTargetNamespace1 = CREATE_ROOT_TARGET_NAMESPACE_1
	}
	// Create nodes target namespace 1
	String createNodesTargetNamespace1In = getExchangeProperty(message, "ConvertMap.CreateNodesTargetNamespace1", false)
	if (createNodesTargetNamespace1In) {
		if ('true'.equalsIgnoreCase(createNodesTargetNamespace1In)) {
			createNodesTargetNamespace1 = true
		}
	} else {
		createNodesTargetNamespace1 = CREATE_NODES_TARGET_NAMESPACE_1
	}
	// Target namespace prefix 1
	String targetNamespacePrefix1 = getExchangeProperty(message, "ConvertMap.TargetNamespacePrefix1", false)
	if (!targetNamespacePrefix1) {
		targetNamespacePrefix1 = TARGET_NAMESPACE_PREFIX_1
	}
	if (targetNamespacePrefix1.length() == 0) {
		throw Exception("The target namespace prefix 1 must have one or more characters.")
	}
	// Target Namespace 1
	String targetNamespace1 = getExchangeProperty(message, "ConvertMap.TargetNamespace1", false)
	if (!targetNamespace1) {
		targetNamespace1 = TARGET_NAMESPACE_1
	}
	if (targetNamespace1.length() == 0) {
		throw Exception("The target namespace 1 is not defined.")
	}
	// Append all Groovy costum functions
	String appendAllGroovyCostumFunctionsIn = getExchangeProperty(message, "ConvertMap.AppendAllGroovyCostumFunctions", false)
	if (appendAllGroovyCostumFunctionsIn) {
		if ('true'.equalsIgnoreCase(appendAllGroovyCostumFunctionsIn)) {
			appendAllGroovyCostumFunctions = true
		}
	} else {
		appendAllGroovyCostumFunctions = APPEND_ALL_GROOVY_COSTUM_FUNCTIONS
	}
	// Append mapping examples
	String appendMappingExamplesIn = getExchangeProperty(message, "ConvertMap.AppendMappingExamples", false)
	if (appendMappingExamplesIn) {
		if ('true'.equalsIgnoreCase(appendMappingExamplesIn)) {
			mappingExamples = true
		}
	} else {
		mappingExamples = APPEND_MAPPING_EXAMPLES
	}

/**
* Read body values to lists
*/

	if (message.getBodySize() > 0) {
		// Get body 
		def body = message.getBody(java.lang.String) as String

		// Check for CRLF
		if (body.indexOf("\n") == -1) {
			throw Exception("Input body is not a valid mapping with line feed.")
		}

		// Loop all lines
		body.eachLine { line, count ->
			int lineLength = line.length()
			if (lineLength > 0) {
				l ++
				int startOfNode = 0
				if (line.startsWith(TARGET_NODE_START)) {
					startOfNode = TARGET_NODE_START.length()
				}
				int indexOfSeparator = line.indexOf('=')
				if (line.indexOf('Message2') > -1) {
					throw Exception("A multi-mapping with more than one message creation were found. This should be split in different mappings with a split.")
				} else if (indexOfSeparator != -1) {
					// Set default line activation
					lineActivation[l] = true
					// Get target node
					targetNode = line.substring(startOfNode, indexOfSeparator)
					// Remove all namespace prefix for target node
					targetNode = removeAllPrefix(targetNode)
					// Set back
					targetNodes[l] = targetNode
					// Get source node
					sourceNode = line.substring(indexOfSeparator + 1, lineLength)
					message.setProperty('ConvertMap.LastLineSourceNode1',sourceNode)
					// Remove all namespace prefix for source node
					sourceNode = removeAllPrefix(sourceNode)
					message.setProperty('ConvertMap.LastLineSourceNode2',sourceNode)
					// Compute some source node values
					int indexOfFormulaSeparator = sourceNode.indexOf('=')
					int indexOfClosingParenthesis = sourceNode.indexOf(')')
					int bracketCount = sourceNode.count(')')
					int sourceLength = sourceNode.length()
					// Creat some valid values from formula
					// EmptyConstant
					if (sourceLength == 0 || 'const(value=)'.equals(sourceNode)) {
						sourceNode = getEmptyConstant()
						sourceType = 'constant'
						message.setProperty('ConvertMap.LastLineSourceFormula','EmptyConstant')
					// ConstantValue
					} else if (sourceNode.startsWith('const(') && indexOfClosingParenthesis + 1 == sourceLength && indexOfClosingParenthesis > indexOfFormulaSeparator + 1 && bracketCount == 1) {
						sourceNode = getConstantValue(sourceNode, indexOfFormulaSeparator, indexOfClosingParenthesis)
						sourceType = 'constant'
						message.setProperty('ConvertMap.LastLineSourceFormula','ConstantValue')
					// Direct node mapping
					} else if (sourceNode.startsWith('/') && bracketCount == 0){
						sourceNode = getDirectNode(sourceNode)
						sourceType = 'node'
						message.setProperty('ConvertMap.LastLineSourceFormula','Direct node mapping')
					// SimpleMapWithDefault
					} else if (sourceNode.startsWith('mapWithDefault(') && bracketCount == 1) {
						sourceNode = getSimpleMapWithDefault(sourceNode)
						sourceType = 'formula'
						methodMapWithDefault = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleMapWithDefault')
					// SimpleFixedValues
					} else if (sourceNode.startsWith('FixValues(') && bracketCount == 1) {
						sourceNode = getSimpleFixedValues(sourceNode)
						sourceType = 'formula'
						methodFixedValues = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleFixedValues')
					// SimpleValueMap
					} else if (sourceNode.startsWith('valuemap(') && bracketCount == 1) {
						sourceNode = getSimpleValueMap(sourceNode)
						sourceType = 'formula'
						methodValueMap = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleValueMap')
					// SimpleFormatNumber
					} else if (sourceNode.startsWith('formatNumber(') && bracketCount == 1) {
						sourceNode = getSimpleFormatNumber(sourceNode)
						sourceType = 'formula'
						methodFormatNumber = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleFormatNumber')
					// SimpleTrim
					} else if (sourceNode.startsWith('trim(') && bracketCount == 1) {
						sourceNode = getSimpleTrim(sourceNode)
						sourceType = 'formula'
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleTrim')
					// SimpleTrimZeroLeft
					} else if ((sourceNode.startsWith('removeLeadingZeros(') || sourceNode.startsWith('trimZeroLeft(')) && bracketCount == 1) {
						sourceNode = getSimpleTrimZeroLeft(sourceNode)
						sourceType = 'formula'
						methodTrimZeroLeft = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleTrimZeroLeft')
					// SimpleTrimRight
					} else if (sourceNode.startsWith('trimRight(') && bracketCount == 1) {
						sourceNode = getSimpleTrimRight(sourceNode)
						sourceType = 'formula'
						methodTrimRight = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleTrimRight')
					// SimpleConcat (with two values, a constant and a value, or two constants)
					} else if ((sourceNode.startsWith('concat(') && bracketCount == 1) || (sourceNode.startsWith('concat(') && sourceNode.count('const(') == 1 && bracketCount == 2) || (sourceNode.startsWith('concat(') && sourceNode.count('const(') == 2 && bracketCount == 3)) {
						sourceNode = getSimpleConcat(sourceNode)
						sourceType = 'formula'
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleConcat')
					// SimpleTransformDate
					} else if (sourceNode.startsWith('TransformDate(') && bracketCount == 1) {
						sourceNode = getSimpleTransformDate(sourceNode)
						sourceType = 'formula'
						methodTransformDate = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleTransformDate')
					// SimpleGetDateAfterDays
					} else if (sourceNode.startsWith('getDateAfterDays(') && bracketCount == 2 && sourceNode.count('const(value=') == 1) {
						sourceNode = getSimpleGetDateAfterDays(sourceNode)
						sourceType = 'formula'
						methodGetDateAfterDays = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleGetDateAfterDays')
					// SimpleGetDateFormat
					} else if (sourceNode.startsWith('getDateFormat(') && bracketCount == 1) {
						sourceNode = getSimpleGetDateFormat(sourceNode)
						sourceType = 'formula'
						methodGetDateFormat = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleGetDateFormat')
					// SimpleFormatValueByZero
					} else if (sourceNode.startsWith('formatValueByZero(') && sourceNode.count('const(') == 4 && bracketCount == 5) {
						sourceNode = getSimpleFormatValueByZero(sourceNode)
						sourceType = 'formula'
						methodFormatValueByZero = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleFormatValueByZero')
					// SimpleStripSpaces
					} else if (sourceNode.startsWith('stripSpaces(') && bracketCount == 1) {
						sourceNode = getSimpleStripSpaces(sourceNode)
						sourceType = 'formula'
						methodStripSpaces = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleStripSpaces')
					// SimpleToLowerCase
					} else if (sourceNode.startsWith('toLowerCase(') && bracketCount == 1) {
						sourceNode = getSimpleToLowerCase(sourceNode)
						sourceType = 'formula'
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleToLowerCase')
					// SimpleToUpperCase
					} else if (sourceNode.startsWith('toUpperCase(') && bracketCount == 1) {
						sourceNode = getSimpleToUpperCase(sourceNode)
						sourceType = 'formula'
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleToUpperCase')
					// SimpleCopyValue
					} else if (sourceNode.startsWith('CopyValue(') && bracketCount == 1) {
						sourceNode = getSimpleCopyValue(sourceNode)
						sourceType = 'formula'
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleCopyValue')
					// SimpleMinusFromBeginToEnd
					} else if (sourceNode.startsWith('minusFromBeginToEnd(') && bracketCount == 1) {
						sourceNode = getSimpleMinusFromBeginToEnd(sourceNode)
						sourceType = 'formula'
						methodMinusFromBeginToEnd = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleMinusFromBeginToEnd')
					// SimpleMinusFromEndToBegin
					} else if (sourceNode.startsWith('minusFromEndToBegin(') && bracketCount == 1) {
						sourceNode = getSimpleMinusFromEndToBegin(sourceNode)
						sourceType = 'formula'
						methodMinusFromEndToBegin = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleMinusFromEndToBegin')
					// SimpleDynamicSubstring
					} else if ((sourceNode.startsWith('substring(') || sourceNode.startsWith('dynamicSubstring(')) && bracketCount == 1) {
						sourceNode = getSimpleDynamicSubstring(sourceNode)
						sourceType = 'formula'
						methodDynamicSubstring = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleDynamicSubstring')
					// SimpleReplaceString
					} else if (sourceNode.startsWith('replaceString(') && bracketCount == 3 && sourceNode.count('const(value=') == 2) {
						sourceNode = getSimpleReplaceString(sourceNode)
						sourceType = 'formula'
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleReplaceString')	
					// SimpleSetCurrentDate
					} else if (sourceNode.startsWith('currentDate(') && bracketCount == 1) {
						sourceNode = getSimpleSetCurrentDate(sourceNode)
						sourceType = 'formula'
						methodSetCurrentDate = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleSetCurrentDate')	
					// SimpleSetDecimalSeparatorPoint
					} else if (sourceNode.startsWith('setDecimalSeparatorPoint(') && bracketCount == 1) {
						sourceNode = getSimpleSetDecimalSeparatorPoint(sourceNode)
						sourceType = 'formula'
						methodSetDecimalSeparatorPoint = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleSetDecimalSeparatorPoint')
					// SimpleSetDefaultAsCurrentDate
					} else if (sourceNode.startsWith('setDefaultAsCurrentDate(') && bracketCount == 2) {
						sourceNode = getSimpleSetDefaultAsCurrentDate(sourceNode)
						sourceType = 'formula'
						methodSetDefaultAsCurrentDate = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleSetDefaultAsCurrentDate')
					// SimpleCount
					} else if (sourceNode.startsWith('count(') && bracketCount == 1) {
						sourceNode = getSimpleCount(sourceNode)
						sourceType = 'formula'
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleSimpleCount')
					// SimpleToNumber
					} else if (sourceNode.startsWith('toNumber(') && bracketCount == 1) {
						sourceNode = getSimpleToNumber(sourceNode)
						sourceType = 'formula'
						methodToNumber = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleToNumber')
					// SimpleRemoveAlgebraicSign
					} else if (sourceNode.startsWith('removeAlgebraicSign(') && bracketCount == 1) {
						sourceNode = getSimpleRemoveAlgebraicSign(sourceNode)
						sourceType = 'formula'
						methodRemoveAlgebraicSign = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleRemoveAlgebraicSign')
					// SimpleRemoveAlgebraicSignPlus
					} else if (sourceNode.startsWith('removeAlgebraicSignPlus(') && bracketCount == 1) {
						sourceNode = getSimpleRemoveAlgebraicSignPlus(sourceNode)
						sourceType = 'formula'
						methodRemoveAlgebraicSignPlus = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleRemoveAlgebraicSignPlus')
					// SimpleRemoveAllCharacters
					} else if (sourceNode.startsWith('removeAllCharacters(') && bracketCount == 1) {
						sourceNode = getSimpleRemoveAllCharacters(sourceNode)
						sourceType = 'formula'
						methodRemoveAllCharacters = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleRemoveAllCharacters')
					// SimpleRemoveAllSpaces
					} else if (sourceNode.startsWith('removeAllSpaces(') && bracketCount == 1) {
						sourceNode = getSimpleRemoveAllSpaces(sourceNode)
						sourceType = 'formula'
						methodRemoveAllSpaces = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleRemoveAllSpaces')
					// SimpleRemoveAllSpecialCharacters
					} else if (sourceNode.startsWith('removeAllSpecialCharacters(') && bracketCount == 1) {
						sourceNode = getSimpleRemoveAllSpecialCharacters(sourceNode)
						sourceType = 'formula'
						methodRemoveAllSpecialCharacters = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleRemoveAllSpecialCharacters')
					// SimpleGetOnlyNumbers
					} else if (sourceNode.startsWith('getOnlyNumbers(') && bracketCount == 1) {
						sourceNode = getSimpleGetOnlyNumbers(sourceNode)
						sourceType = 'formula'
						methodGetOnlyNumbers = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleGetOnlyNumbers')
					// SimpleHeadString
					} else if (sourceNode.startsWith('headString(') && bracketCount == 2) {
						sourceNode = getSimpleHeadString(sourceNode)
						sourceType = 'formula'
						methodHeadString = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleHeadString')
					// SimpleTailString
					} else if (sourceNode.startsWith('tailString(') && bracketCount == 2) {
						sourceNode = getSimpleTailString(sourceNode)
						sourceType = 'formula'
						methodTailString = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleTailString')
					// SimpleFillLeadingZeros
					} else if (sourceNode.startsWith('fillLeadingZeros(') && bracketCount == 2) {
						sourceNode = getSimpleFillLeadingZeros(sourceNode)
						sourceType = 'formula'
						methodFillLeadingZeros = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleFillLeadingZeros')
					// SimpleFillUpToLengthWithSpace
					} else if (sourceNode.startsWith('fillUpToLengthWithSpace(') && bracketCount == 2) {
						sourceNode = getSimpleFillUpToLengthWithSpace(sourceNode)
						sourceType = 'formula'
						methodFillUpToLengthWithSpace = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleFillUpToLengthWithSpace')
					// SimpleRemoveCarriageReturnLineFeed
					} else if (sourceNode.startsWith('removeCarriageReturnLineFeed(') && bracketCount == 1) {
						sourceNode = getSimpleRemoveCarriageReturnLineFeed(sourceNode)
						sourceType = 'formula'
						methodRemoveCarriageReturnLineFeed = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleRemoveCarriageReturnLineFeed')
					// SimpleRemoveDecimalIfZero
					} else if (sourceNode.startsWith('removeDecimalIfZero(') && bracketCount == 1) {
						sourceNode = getSimpleRemoveDecimalIfZero(sourceNode)
						sourceType = 'formula'
						methodRemoveDecimalIfZero = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleRemoveDecimalIfZero')
					// SimpleReplaceSpecialCharacters
					} else if (sourceNode.startsWith('replaceSpecialCharacters(') && bracketCount == 1) {
						sourceNode = getSimpleReplaceSpecialCharacters(sourceNode)
						sourceType = 'formula'
						methodReplaceSpecialCharacters = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleReplaceSpecialCharacters')
					// SimpleReplaceUmlauts
					} else if (sourceNode.startsWith('replaceUmlauts(') && bracketCount == 1) {
						sourceNode = getSimpleReplaceUmlauts(sourceNode)
						sourceType = 'formula'
						methodReplaceUmlauts = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleReplaceUmlauts')
					// SimpleThrowExceptionIfNoValue
					} else if (sourceNode.startsWith('throwExceptionIfNoValue(') && bracketCount == 2) {
						sourceNode = getSimpleThrowExceptionIfNoValue(sourceNode)
						sourceType = 'formula'
						methodThrowExceptionIfNoValue = true
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleThrowExceptionIfNoValue')
					// SimpleGetMessageID
					} else if (sourceNode.startsWith('getMessageID(') && bracketCount == 2) {
						sourceNode = getSimpleMessageID()
						sourceType = 'formula'
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleGetMessageID')
					// SimpleSender
					} else if ('sender()'.equals(sourceNode)) {
						sourceNode = getSimpleSender()
						sourceType = 'formula'
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleSender')
					// SimpleReceiver
					} else if ('receiver()'.equals(sourceNode)) {
						sourceNode = getSimpleReceiver()
						sourceType = 'formula'
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleReceiver')
					// SimpleVariable	
					} else if (sourceNode.startsWith('*') && bracketCount == 0) {
						sourceNode = getSimpleVariable(sourceNode)
						sourceType = 'formula'
						message.setProperty('ConvertMap.LastLineSourceFormula','SimpleVariable')
					// Deactivate line, because no valid groovy formula
					} else {
						sourceNode = getComplexFormula(sourceNode)
						lineActivation[l] = false
						sourceType = 'formula'
						message.setProperty('ConvertMap.LastLineSourceFormula','Deactivate line')
					}
					sourceNodes[l] = sourceNode
					sourceTypes[l] = sourceType
				} else {
					throw Exception("Input body is not a valid mapping.")
				}
			}
		}

/**
* Compute and expand array lists
*/

		// These lists needs to expand if needed with default values to prevent consequential errors
		int targetNodesSize = targetNodes.size()
		// Compute target segment, if there is no input
		if (isTargetSegments[0] == null) {
			isTargetSegments = computeIsTargetSegment(targetNodes, isTargetSegments)
		}
		expandList(isTargetSegments, targetNodesSize, false)
		expandList(sourceTypes, targetNodesSize, 'constant')
		expandList(sourceNodes, targetNodesSize, '')

		if (targetSegments[0] == null) {
			targetSegments = computeTargetSegment(targetNodes, isTargetSegments, targetSegments, sourceTypes, sourceNodes)
		}
		expandList(targetSegments, targetNodesSize, '')
		if (targetHierarchyLevels[0] == null) {
			targetHierarchyLevels = computeTargetHierarchyLevel(targetNodes, isTargetSegments, targetHierarchyLevels)
		}

		sourceSegmentsReduce = computeSourceSegmentsReduce(isTargetSegments, sourceNodes, sourceTypes, sourceSegmentsReduce)
		expandList(sourceSegmentsReduce, targetNodesSize, '')
		expandList(targetHierarchyLevels, targetNodesSize, 0)	
		expandList(targetDescription, targetNodesSize, '')
		expandList(mapRules, targetNodesSize, '')
		expandList(lineActivation, targetNodesSize, true)
		if (lineTabs[0] == null) {
			lineTabs = computeLineTabs(targetHierarchyLevels, isTargetSegments, sourceTypes, lineTabs)
		}
		expandList(lineTabs, targetNodesSize, 3)
		// Reduce source segments in source nodes
		if (createSegmentHierarchy == true) {
			reduceSourceSegments(sourceNodes, sourceSegmentsReduce)
		}
		if (rightCurlyBrackets[0] == null) {
			rightCurlyBrackets = computeRightCurlyBrackets(isTargetSegments, targetHierarchyLevels, lineTabs, rightCurlyBrackets)
		}
		expandList(rightCurlyBrackets, targetNodesSize, '')

/**
* Create Groovy Script
*/

		if (createOnlyDebug == false) {
			// Tabulators
			int defaultTabCount = 2
			String tabs = addTabs(defaultTabCount)
			
			// Define string builder
			StringBuilder sb = new StringBuilder()
			// Build new groovy mapping
			sb.append('import com.sap.gateway.ip.core.customdev.util.Message' + LINE_BREAK)
			sb.append('import groovy.util.XmlSlurper' + LINE_BREAK)
			sb.append('import groovy.xml.MarkupBuilder' + LINE_BREAK)
			if (appendAllGroovyCostumFunctions == true || methodGetMondayOfWeek == true) {
				sb.append('import java.util.Locale' + LINE_BREAK)
			}
			if (appendAllGroovyCostumFunctions == true || methodSetCurrentDate == true || methodTransformDate == true || methodSetDefaultAsCurrentDate == true || methodGetDateAfterDays == true || methodSetDirectory == true || methodSetFileName == true) {
				sb.append('import java.util.TimeZone' + LINE_BREAK)
			}
			if (appendAllGroovyCostumFunctions == true || methodGetMondayOfWeek == true) {
				sb.append('import java.text.DateFormat' + LINE_BREAK)
			}
			if (appendAllGroovyCostumFunctions == true || methodSetCurrentDate == true || methodTransformDate == true || methodSetDefaultAsCurrentDate == true || methodGetDateAfterDays == true || methodSetDirectory == true || methodSetFileName == true || methodGetMondayOfWeek == true) {
				sb.append('import java.text.SimpleDateFormat' + LINE_BREAK)
			}
			if (appendAllGroovyCostumFunctions == true || methodFormatNumber == true) {
				sb.append('import java.text.DecimalFormatSymbols' + LINE_BREAK)
				sb.append('import java.text.DecimalFormat' + LINE_BREAK)
			}
			if (appendAllGroovyCostumFunctions == true || methodValueMap == true) {
				sb.append('import com.sap.it.api.ITApiFactory' + LINE_BREAK)
				sb.append('import com.sap.it.api.mapping.ValueMappingApi' + LINE_BREAK)
			}
			sb.append(LINE_BREAK)
			sb.append('/**' + LINE_BREAK)
			sb.append('* MapXMLToXML' + LINE_BREAK)
			sb.append('* This Groovy script is a XML to XML mapping template.' + LINE_BREAK)
			sb.append('*' + LINE_BREAK)
			sb.append('* @author nttdata-solutions.com' + LINE_BREAK)
			sb.append('* @version 1.0.0' + LINE_BREAK)
			sb.append('*/' + LINE_BREAK)
			sb.append('def Message processData(Message message) {' + LINE_BREAK)
			sb.append("	final String LOCALE_LANGUAGE = 'de'" + LINE_BREAK)
			sb.append("	final String LOCALE_COUNTRY = 'DE'" + LINE_BREAK)
			sb.append("	final String TIME_ZONE = 'Europe/Berlin'" + LINE_BREAK)
			sb.append(LINE_BREAK)
			sb.append('	def messageLog = messageLogFactory.getMessageLog(message)' + LINE_BREAK)
			if (appendAllGroovyCostumFunctions == true || methodValueMap == true) {
				sb.append('	def valueMapApi = ITApiFactory.getService(ValueMappingApi.class, null)' + LINE_BREAK)
			}
			sb.append(LINE_BREAK)
			sb.append('	if (message.getBodySize() > 0) {' + LINE_BREAK)
			if (appendAllGroovyCostumFunctions == true || methodGetMondayOfWeek == true) {
				sb.append(tabs + '// Set locale' + LINE_BREAK)
				sb.append(tabs + 'Locale locale' + LINE_BREAK)							  
				sb.append(tabs + "if (isValidLocale(LOCALE_LANGUAGE + '_' + LOCALE_COUNTRY)) {" + LINE_BREAK)
				sb.append(tabs + '	locale = new Locale(LOCALE_LANGUAGE, LOCALE_COUNTRY)' + LINE_BREAK)
				sb.append(tabs + '} else {' + LINE_BREAK)
				sb.append(tabs + '	throw Exception("\'$LOCALE_LANGUAGE" + "_" + "$LOCALE_COUNTRY\' is not valid locale.")' + LINE_BREAK)
				sb.append(tabs + '}' + LINE_BREAK)
			}
			if (appendAllGroovyCostumFunctions == true || methodSetCurrentDate == true || methodTransformDate == true || methodSetDefaultAsCurrentDate == true) {
				sb.append(tabs + '// Set time zone' + LINE_BREAK)
				sb.append(tabs + 'TimeZone timeZone' + LINE_BREAK)
				sb.append(tabs + 'if (TimeZone.getAvailableIDs().contains(TIME_ZONE)) {' + LINE_BREAK)
				sb.append(tabs + '	timeZone = TimeZone.getTimeZone(TIME_ZONE)' + LINE_BREAK)
				sb.append(tabs + '} else {' + LINE_BREAK)
				sb.append(tabs + '	throw Exception("\'$TIME_ZONE\' is not valid time zone.")' + LINE_BREAK)
				sb.append(tabs + '}' + LINE_BREAK)
				sb.append(LINE_BREAK)
			}
			sb.append(tabs + '// Get body' + LINE_BREAK)
			sb.append(tabs + 'Reader reader = message.getBody(Reader)' + LINE_BREAK)
			// Get source root name
			sourceRootName = getSourceRootName(sourceNodes, sourceTypes)
			sb.append(tabs + 'def ' + sourceRootName + ' = new XmlSlurper().parse(reader)' + LINE_BREAK)
			sb.append(LINE_BREAK)
			sb.append(tabs + '// Create writer' + LINE_BREAK)
			sb.append(tabs + 'def writer = new StringWriter()' + LINE_BREAK)
			sb.append(tabs + '// Create markup builder' + LINE_BREAK)
			sb.append(tabs + 'def xml = new MarkupBuilder(writer)' + LINE_BREAK)
			sb.append(tabs + '// Set double quotes' + LINE_BREAK)
			sb.append(tabs + 'xml.setDoubleQuotes(true)' + LINE_BREAK)
			sb.append(tabs + '// Set XML declaration' + LINE_BREAK)
			sb.append(tabs + "xml.mkp.xmlDeclaration(version: '1.0', encoding: 'utf-8')" + LINE_BREAK)
			sb.append(LINE_BREAK)
			// Append initialize variables before line settings
			boolean hasVariables = false
			for (int i = 0; i < targetNodes.size(); i++) {
				// Compute node name / attribute / variable
				String nodeName = targetNodes[i].substring(1, targetNodes[i].length())
				String variableName = ''
				if (nodeName.indexOf('/') > -1) {
					def nodeParts = nodeName.split('/')
					// Get variable name
					if (nodeName.indexOf('*') > 0) {
						variableName = nodeParts[nodeParts.size() - 1].replaceAll('\\*', '')
						if (lineActivation[i] == true && variableName) {
							if (hasVariables == false) {
								hasVariables = true
								sb.append(tabs + "// Variables used in mapping" + LINE_BREAK)
							}
							// Append initialize variable
							sb.append(tabs + "String " + variableName + " = '' " + LINE_BREAK)
						}
					}
				}
			}
			if (hasVariables == true) {
				sb.append(LINE_BREAK)
			}
			sb.append(tabs + '// TODO: Create sub segment formulas' + LINE_BREAK)
			sb.append(tabs + '// TODO: Create complex mapping formulas' + LINE_BREAK)
			sb.append(LINE_BREAK)
			sb.append(tabs + '// Map nodes to target structure' + LINE_BREAK)
			// Get target root node name
			String targetRootName = getTargetRootName(targetNodes)
			if (createRootTargetNamespace1 == true) {
				targetRootName = "'" + targetNamespacePrefix1 + ":" + targetRootName + "'('xmlns:" + targetNamespacePrefix1 + "': '" + targetNamespace1 + "')"
			}
			if (createSegmentHierarchy == false) {
				sb.append('		xml.' + targetRootName + ' {' + LINE_BREAK)
			}
			// Append mapping lines
			for (int i = 0; i < targetNodes.size(); i++) {
				// Compute right curly brackets before segment creation
				if (createSegmentHierarchy == true && isTargetSegments[i] == true) {
					String rightCurlyBracket = createRightCurlyBrackets(rightCurlyBrackets[i])
					if (rightCurlyBracket) {
						sb.append(rightCurlyBracket)
					}
				}
				// Compute leading tabulators
				if (createSegmentHierarchy == true) {
					int lineTab = lineTabs[i]
					if(lineTab.toString().isNumber()) {
						tabs = addTabs(lineTab)
					} else {
						tabs = addTabs(3)
					}
				} else {
					tabs = addTabs(3)
				}
				// Create target description and XPath
				if (createTargetDescription == true || createTargetXpath == true) {
					sb.append(tabs + '//')
					if (createTargetDescription == true && targetDescription[i].length() > 0) {
						sb.append(' ' + targetDescription[i])
					}
					if (createTargetXpath == true) {
						sb.append(' -> XPath: ' + targetNodes[i])
					}
					sb.append(LINE_BREAK)
				}
				// Create map rule
				if (createMapRule == true && mapRules[i].length() > 0) {
					sb.append(tabs + '// Map Rule: ' + mapRules[i] + LINE_BREAK)
				}
				// Create a segment note
				if (isTargetSegments[i] == true && createSegmentHierarchy == false) {
						sb.append(tabs + '// TODO: Create new Segment' + LINE_BREAK)
				}
				// Compute node name / attribute / variable
				String nodeName = targetNodes[i].substring(1, targetNodes[i].length())
				String attributeName = ''
				String variableName = ''
				if (nodeName.indexOf('/') > -1) {
					def nodeParts = nodeName.split('/')
					// Get attribute name
					if (nodeName.indexOf('@') > 0) {
						nodeName = nodeParts[nodeParts.size() - 2]
						attributeName = nodeParts[nodeParts.size() - 1].replaceAll('@', '')
					// Get variable name
					} else if (nodeName.indexOf('*') > 0) {
						variableName = nodeParts[nodeParts.size() - 1].replaceAll('\\*', '')
					// Get node name
					} else {
						nodeName = nodeParts[nodeParts.size() - 1]
					}
				}
				// Put namespace prefix in front of node name
				if (createNodesTargetNamespace1 == true) {
					nodeName = targetNamespacePrefix1 + ':' + nodeName
				}
				// Use line activation for segment-nodes, nodes, attributes and variables
				// Append segment-node
				if (createSegmentHierarchy == true && lineActivation[i] == true && isTargetSegments[i] == true) {
					String targetSegment = targetSegments[i]
					if (targetSegment) {
						targetSegment = targetSegment.replaceAll('_cRLf_','\r\n\t' + tabs)
					}
					sb.append(tabs + targetSegment + LINE_BREAK)	
				// Append node
				} else if (lineActivation[i] == true && !attributeName && !variableName) {
					sb.append(tabs + "'" + nodeName + "' " + sourceNodes[i] + LINE_BREAK)
				} else if (lineActivation[i] == false && !attributeName && !variableName) {
					sb.append(tabs + "'" + nodeName + "' '' ")
					if (createNoConvertableFormula == true) {
						sb.append("// " + sourceNodes[i])
					}
					sb.append(LINE_BREAK)
				// Append Atribute
				} else if (lineActivation[i] == true && attributeName) {
					sb.append(tabs + "'" + nodeName + "'(" + attributeName + ": " + sourceNodes[i] + ")" + LINE_BREAK)
				} else if (lineActivation[i] == false && attributeName) {
					sb.append(tabs + "'" + nodeName + "'(" + attributeName + ": '')")
					if (createNoConvertableFormula == true) {
						sb.append("// " + sourceNodes[i])
					}
					sb.append(LINE_BREAK)
				// Append Variable
				} else if (lineActivation[i] == true && variableName) {
					sb.append(tabs + variableName + " = " + sourceNodes[i] + LINE_BREAK)
				} else if (lineActivation[i] == false && !attributeName && !variableName) {
					sb.append(tabs + nodeName + " = '' ")
					if (createNoConvertableFormula == true) {
						sb.append("// " + sourceNodes[i])
					}
					sb.append(LINE_BREAK)
				}
				// Compute right curly brackets after last line
				if (createSegmentHierarchy == true && i == targetNodes.size() - 1) {
					String rightCurlyBracket = createRightCurlyBrackets(rightCurlyBrackets[i])
					if (rightCurlyBracket) {
						sb.append(rightCurlyBracket)
					}
				}
			}
			tabs = addTabs(defaultTabCount)			
			if (createSegmentHierarchy == false) {
				sb.append(tabs + '}' + LINE_BREAK)
			}
			sb.append(LINE_BREAK)
			tabs = addTabs(defaultTabCount)
			sb.append(tabs + '// Create new body' + LINE_BREAK)
			sb.append(tabs + 'String bodyNew = writer.toString()' + LINE_BREAK)
			sb.append(tabs + '// Set new body' + LINE_BREAK)
			sb.append(tabs + 'message.setBody(bodyNew)' + LINE_BREAK)
			sb.append(LINE_BREAK)
			sb.append(tabs + '// Set Custom Header Property TODO: Check global monitoring concept for Custom Header Names and place where you need to set it.' + LINE_BREAK)
			sb.append(tabs + 'if (messageLog) {' + LINE_BREAK)
			sb.append(tabs + "	messageLog.addCustomHeaderProperty('IDocNumber', '') // TODO: Set source like: XPath+SourceNodeName.Text()" + LINE_BREAK)
			sb.append(tabs + "	messageLog.addCustomHeaderProperty('DocumentNumber', '') // TODO: Set source like: XPath+SourceNodeName.Text()" + LINE_BREAK)
			sb.append(tabs + "	messageLog.addCustomHeaderProperty('PartnerNumber', '') // TODO: Set source like: XPath+SourceNodeName.Text()" + LINE_BREAK)
			sb.append(tabs + "	messageLog.addCustomHeaderProperty('Plant', '') // TODO: Set source like: XPath+SourceNodeName.Text()" + LINE_BREAK)
			sb.append(tabs + '}' + LINE_BREAK)
			sb.append('	}' + LINE_BREAK)
			sb.append('	return message' + LINE_BREAK)
			sb.append('}')

			// Append Groovy mapping examples
			appendMappingExamples(sb, mappingExamples, LINE_BREAK)

			// Append needed Groovy Methods
			// Append method isValidLocale()
			appendMethodIsValidLocale(sb, methodIsValidLocale, appendAllGroovyCostumFunctions, LINE_BREAK)

			// Append Groovy Custom Functions
			// Append method assignValueByCondition()
			appendMethodAssignValueByCondition(sb, methodAssignValueByCondition, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method dynamicSubstring()
			appendMethodDynamicSubstring(sb, methodDynamicSubstring, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method fillLeadingZeros()
			appendMethodFillLeadingZeros(sb, methodFillLeadingZeros, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method fillUpToLengthWithSpace()
			appendMethodFillUpToLengthWithSpace(sb, methodFillUpToLengthWithSpace, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method fixedValues()
			appendMethodFixedValues(sb, methodFixedValues, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method formatNumber()
			appendMethodFormatNumber(sb, methodFormatNumber, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method formatValueBySpace()
			appendMethodFormatValueBySpace(sb, methodFormatValueBySpace, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method formatValueByZero()
			appendMethodFormatValueByZero(sb, methodFormatValueByZero, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method getDate()
			appendMethodGetDate(sb, methodGetDate, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method getDateAfterDays()
			appendMethodGetDateAfterDays(sb, methodGetDateAfterDays, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method getDateFormat()
			appendMethodGetDateFormat(sb, methodGetDateFormat, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method getExchangeProperty()
			appendMethodGetExchangeProperty(sb, methodGetExchangeProperty, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method getHeader()
			appendMethodGetHeader(sb, methodGetHeader, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method getMondayOfWeek()
			appendMethodGetMondayOfWeek(sb, methodGetMondayOfWeek, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method getOnlyNumbers()
			appendMethodGetOnlyNumbers(sb, methodGetOnlyNumbers, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method getValueByCondition()
			appendMethodGetValueByCondition(sb, methodGetValueByCondition, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method headString()
			appendMethodHeadString(sb, methodHeadString, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method isNumber()
			appendMethodIsNumber(sb, methodIsNumber, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method mapWithDefault()
			appendMethodMapWithDefault(sb, methodMapWithDefault, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method minusFromBeginToEnd()
			appendMethodMinusFromBeginToEnd(sb, methodMinusFromBeginToEnd, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method minusFromEndToBegin()
			appendMethodMinusFromEndToBegin(sb, methodMinusFromEndToBegin, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method removeAlgebraicSign()
			appendMethodRemoveAlgebraicSign(sb, methodRemoveAlgebraicSign, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method removeAlgebraicSignPlus()
			appendMethodRemoveAlgebraicSignPlus(sb, methodRemoveAlgebraicSignPlus, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method removeAllCharacters()
			appendMethodRemoveAllCharacters(sb, methodRemoveAllCharacters, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method removeAllSpaces()
			appendMethodRemoveAllSpaces(sb, methodRemoveAllSpaces, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method removeAllSpecialCharacters()
			appendMethodRemoveAllSpecialCharacters(sb, methodRemoveAllSpecialCharacters, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method removeCarriageReturnLineFeed()
			appendMethodRemoveCarriageReturnLineFeed(sb, methodRemoveCarriageReturnLineFeed, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method removeDecimalIfZero()
			appendMethodRemoveDecimalIfZero(sb, methodRemoveDecimalIfZero, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method replaceSpecialCharacters()
			appendMethodReplaceSpecialCharacters(sb, methodReplaceSpecialCharacters, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method replaceUmlauts()
			appendMethodReplaceUmlauts(sb, methodReplaceUmlauts, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method segmentHasOneOfSuchValues()
			appendMethodSegmentHasOneOfSuchValues(sb, methodSegmentHasOneOfSuchValues, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method setCurrentDate()
			appendMethodSetCurrentDate(sb, methodSetCurrentDate, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method setDecimalSeparatorPoint()
			appendMethodSetDecimalSeparatorPoint(sb, methodSetDecimalSeparatorPoint, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method setDefaultAsCurrentDate()
			appendMethodSetDefaultAsCurrentDate(sb, methodSetDefaultAsCurrentDate, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method setDirectory()
			appendMethodSetDirectory(sb, methodSetDirectory, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method setFileName()
			appendMethodSetFileName(sb, methodSetFileName, appendAllGroovyCostumFunctions, LINE_BREAK)			
			// Append method stripSpaces()
			appendMethodStripSpaces(sb, methodStripSpaces, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method tail()
			appendMethodTailString(sb, methodTailString, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method throwExceptionIfNoValue()
			appendMethodThrowExceptionIfNoValue(sb, methodThrowExceptionIfNoValue, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method ToNumber()
			appendMethodToNumber(sb, methodToNumber, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method transformDate()
			appendMethodTransformDate(sb, methodTransformDate, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method trimRight()			
			appendMethodTrimRight(sb, methodTrimRight, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method trimZeroLeft()
			appendMethodTrimZeroLeft(sb, methodTrimZeroLeft, appendAllGroovyCostumFunctions, LINE_BREAK)
			// Append method valueMap()
			appendMethodValueMap(sb, methodValueMap, appendAllGroovyCostumFunctions, LINE_BREAK)

			// Create new body
			bodyNew = sb.toString()
		}

		// Debug to new body
		if (debugMode == true) {
			// Concate lists
			def debugLines = []
			debugLines[0] = 'Target Node\tIs Target Segment\tTarget Segment\tTarget Hierarchy Level\tLine Tabs\tRight Curly Brackets\tSource Node\tSource Segment Reduce\tSource Type\tLine Activation\tTarget Description\tMap Rule'
			for (int i = 0; i < targetNodes.size(); i++) {
				debugLines[i+1] = targetNodes[i] + '\t' + isTargetSegments[i] + '\t' + targetSegments[i] + '\t' + targetHierarchyLevels[i] + '\t' + lineTabs[i] + '\t' + rightCurlyBrackets[i] + '\t' + sourceNodes[i] + '\t' + sourceSegmentsReduce[i] + '\t' + sourceTypes[i] + '\t' + lineActivation[i] + '\t' + targetDescription[i] + '\t' + mapRules[i]
			}
			if (createOnlyDebug == false){
				bodyNew += LINE_BREAK
			}
			bodyNew += '-----------------' + LINE_BREAK + 'DEBUG LIST' + LINE_BREAK + '-----------------' + LINE_BREAK + debugLines.join(LINE_BREAK)
		}

		// Set new body
		message.setBody(bodyNew)
	}
	return message
}

/**
* Addtional methods
*/

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
* removeAllPrefix
* @param value This is the value.
* @return value Return value without prefix.
*/
private String removeAllPrefix(String value) {
	String nodeSeparator = '/'
	int bracketCount = value.count(')')
	String valueNew = value
	// Do not change this because additional colon in string
	if(value.indexOf('http:') == -1 && value.indexOf('currentDate(') == -1 && value.indexOf('TransformDate(') == -1 && !(value.indexOf('concat(') == 0 || bracketCount == 1)) {
		if (value.indexOf(':') > -1) {
			// Split into some parts
			def tmpList = []
			tmpList = value.split(nodeSeparator, -1)
			for (int i = 0; i < tmpList.size(); i++) {
				String theValue = tmpList[i]
				int indexPrefix = theValue.indexOf(':')
				if (indexPrefix > -1) {
					// Remove namespace prefix
					tmpList[i] = theValue.substring(indexPrefix + 1, theValue.length())
				}
			}
			// Join values
			valueNew = tmpList.join(nodeSeparator)
		}
	}
	return valueNew
}

/**
* getSourceRootName
* @param sourceNodes This is the source nodes list.
* @param sourceTypes This is the source types list.
* @return sourceRootName Return source root name.
*/
private String getSourceRootName(def sourceNodes, def sourceTypes) {
	// Set a default source root name
	String sourceRootName = 'root'
	for (int i = 0; i < sourceNodes.size(); i++) {
		String sourceNode = sourceNodes[i]
		String sourceType = sourceTypes[i]
		
		if (sourceType == 'node') {
		    // Take a subsegment / subnode as source because constants are also in array
			if (sourceNode.indexOf('.') > -1) {
				sourceRootName = sourceNode.substring(0, sourceNode.indexOf('.'))
				// Remove namespace
				if (sourceRootName.indexOf(':') > -1) {
					sourceRootName = sourceNode.substring(sourceRootName.indexOf(':') + 1, sourceRootName.length())
				}
				break
			}
		}
	}
	// Remove some not needed characters
	sourceRootName = sourceRootName.replaceAll("['-]", '')
	if (sourceRootName.startsWith('_')) {
		sourceRootName = sourceRootName.substring(1, sourceRootName.length())
	}
	return sourceRootName
}

/**
* getTargetRootName
* @param targetNodes This is targetNodes list.
* @return targetRootName Return target root name.
*/
private String getTargetRootName(def targetNodes) {
	String targetNode = targetNodes[0].substring(1, targetNodes[0].length())
	String targetRootName = ''
	if (targetNode.indexOf('/') == -1) {
		targetRootName = targetNode
	} else {
		targetRootName = targetNode.substring(0, targetNode.indexOf('/'))
	}
	if ((targetRootName.indexOf(':') > -1) || (targetRootName.indexOf('_') > -1) || (targetRootName.indexOf('-') > -1)) {
		targetRootName = "'" + targetRootName + "'"
	}
	return targetRootName
}

/**
* computeIsTargetSegment
* @param targetNodes This is targetNodes list.
* @param isTargetSegments This is the isTargetSegments object.
* @return isTargetSegments Return isTargetSegments object.
*/
private def computeIsTargetSegment(def targetNodes, def isTargetSegments) {
	final String SEPARATOR = '/'
	String nodeBefore = ''
	String nodeNext = ''
	def isTargetSegmentsNew

	if (targetNodes != null) {
		// Set reverse to loop from end to begin
		def targetNodesRevers = targetNodes.reverse()

		for (int i = 0; i < targetNodesRevers.size(); i++) {
			if (i == 0) {
				nodeBefore = targetNodesRevers[i]
				isTargetSegments[i] = false
			} else {
				nodeNext = targetNodesRevers[i]
				isTargetSegments[i] = false
				if (nodeBefore.indexOf(SEPARATOR) > -1) {
					if (nodeNext.equals(nodeBefore.substring(0, nodeBefore.lastIndexOf(SEPARATOR)))) {
						isTargetSegments[i] = true
					}
				}
				nodeBefore = nodeNext
			}
		}
		isTargetSegmentsNew = isTargetSegments.reverse()
	}
	return isTargetSegmentsNew
}

/**
* computeTargetSegment
* @param targetNodes This is targetNodes list.
* @param isTargetSegments This is the isTargetSegments object.
* @param isTargetSegments This is the targetSegments object.
* @param sourceTypes This is the sourceTypes object.
* @param sourceNodes This is the sourceNodes object.
* @return targetSegments Return targetSegments object.
*/
private def computeTargetSegment(def targetNodes, def isTargetSegments, def targetSegments, def sourceTypes, def sourceNodes) {
	final String SEPARATOR_XPATH = '/'
	final String SEPARATOR_SEGMEMT = '.'

	if (targetNodes != null) {
		for (int i = 0; i < targetNodes.size(); i++) {
			boolean isTargetSegment = isTargetSegments[i]
			if (isTargetSegment) {
				String targetNode = targetNodes[i]
				String targetSegmentName = ''
				if (targetNode.indexOf(SEPARATOR_XPATH) > -1) {
					def tmpListTargetNode = []
					tmpListTargetNode = targetNode.split(SEPARATOR_XPATH)
					targetSegmentName = tmpListTargetNode.last()
				} else {
					targetSegmentName = targetNode
				}
				String sourceType = sourceTypes[i]
				String sourceSegmentName = ''
				String sourceNode = ''
				// Set targetSegments
				if (i == 0) {
					// Roote node
					targetSegments[i] = "xml.$targetSegmentName {"
				} else if ('constant'.equalsIgnoreCase(sourceType)) {
					// Target segment is create for occurency of 1..1
					targetSegments[i] = "$targetSegmentName {"
				} else if ('node'.equalsIgnoreCase(sourceType)) {
					// Target segment is create for occurency of 0..unbounded
					sourceNode = sourceNodes[i]
					if (sourceNode) {
						sourceNode = sourceNode.replaceAll('\\.text\\(\\)','')
					}
					if (sourceNode.indexOf(SEPARATOR_SEGMEMT) > -1) {
						def tmpListSourceNode = []
						tmpListSourceNode = sourceNode.split("\\$SEPARATOR_SEGMEMT")
						sourceSegmentName = tmpListSourceNode.last()
					} else {
						sourceSegmentName = sourceNode
					}
					if (sourceNode.endsWith('.')) {
						targetSegments[i] = sourceNode + "each{ " + sourceSegmentName + " ->_cRLf_" + targetSegmentName + " {"
					} else {
						targetSegments[i] = sourceNode + ".each{ " + sourceSegmentName + " ->_cRLf_" + targetSegmentName + " {"
					}
				} else {
					// On complex fomula no computing is possible
					targetSegments[i] = "$targetSegmentName {"
				}
			} else {
				targetSegments[i] = ''
			}
		}
	}
	return targetSegments
}

/**
* computeTargetHierarchyLevel
* @param targetNodes This is targetNodes list.
* @param isTargetSegments This is the vale to check if this is segment.
* @param targetHierarchyLevels This is the targetHierarchyLevels object.
* @return targetHierarchyLevels Return targetHierarchyLevels object.
*/
private def computeTargetHierarchyLevel(def targetNodes, def isTargetSegments, def targetHierarchyLevels) {
	final String SEPARATOR = '/'

	if (targetNodes != null) {
		for (int i = 0; i < targetNodes.size(); i++) {
			String targetNode = targetNodes[i]
			if (targetNode.startsWith(SEPARATOR)) {
				int separatorCount = targetNodes[i].count(SEPARATOR)
				boolean isTargetSegment = isTargetSegments[i]
				if (isTargetSegment) {
					// Segment node
					targetHierarchyLevels[i] = separatorCount - 1
				} else {
					// Normal node
					targetHierarchyLevels[i] = separatorCount - 2
				}
			} else {
				targetHierarchyLevels[i] = null
			}
		}
	}
	return targetHierarchyLevels
}

/**
* computeSourceSegmentsReduce
* @param isTargetSegments This is the vale to check if this is segment.
* @param sourceNodes This is the sourceNodes object.
* @param sourceTypes This is the sourceTypes object.
* @param sourceSegmentsReduce This is the sourceSegmentsReduce object.
* @return sourceSegmentsReduce Return sourceSegmentsReduce object.
*/
private def computeSourceSegmentsReduce(def isTargetSegments, def sourceNodes, def sourceTypes, def sourceSegmentsReduce) {
	final String SEPARATOR_SEGMEMT = '.'
	String sourceNode = ''
	String sourceType = ''
	String sourceSegmentReduce = ''
	if (sourceNodes != null) {
		for (int i = 0; i < sourceNodes.size(); i++) {
			boolean isTargetSegment = isTargetSegments[i]
			sourceNode = ''
			sourceType = sourceTypes[i]
			if (isTargetSegment) {
				if ('constant'.equals(sourceType)) {
					// On 'constant' there is no XPath on segment level
					sourceSegmentReduce = ''
				} else {
					// Compute reduce segment path to replace it later
					sourceNode = sourceNodes[i]
					if (sourceNode) {
						sourceNode = sourceNode.replaceAll('\\.text\\(\\)','')
					}
					if (sourceNode.indexOf(SEPARATOR_SEGMEMT) > -1) {
						def tmpListSourceNode = []
						tmpListSourceNode = sourceNode.split("\\$SEPARATOR_SEGMEMT")
						int tmpListSourceNodeSize = tmpListSourceNode.size()
						if (tmpListSourceNodeSize > 1) {
							// Concat source segment reduce
							for (int s = 0; s < tmpListSourceNodeSize - 1; s++) {
								if (s==0) {
									sourceSegmentReduce = ''
								}
								sourceSegmentReduce += tmpListSourceNode[s] + SEPARATOR_SEGMEMT
							}
						}
					}
				}
                sourceSegmentsReduce[i] = ''
			} else {
				// Set value
				sourceSegmentsReduce[i] = sourceSegmentReduce
			}
		}
	}
	return sourceSegmentsReduce
}

/**
* computeLineTabs
* @param targetHierarchyLevels This is the targetHierarchyLevels object.
* @param isTargetSegments This is the isTargetSegments object.
* @param sourceTypes this is the sourceTypes object.
* @param lineTabs This is the lineTabs object.
* @return lineTabs Return lineTabs object.
*/
private def computeLineTabs(def targetHierarchyLevels, def isTargetSegments, def sourceTypes, def lineTabs) {
	final int DEFAULT_TABS = 2
	int targetHierarchyLevel
	boolean isTargetSegment
	String sourceType
	int tabs
	def tmpArrLineTabs = []
	// Initialize with root segment
	def mapHierarchyTabs = [0:DEFAULT_TABS]
	int targetHierarchyLevelPrev
	// Compute mapHierarchyTabs - Key: Hierarchy level: Count of leading tabs on segment level
	// Example ['0':2,'1':3,'2':6]
	if (targetHierarchyLevels != null) {
		for (int i = 0; i < targetHierarchyLevels.size(); i++) {
			isTargetSegment = isTargetSegments[i]
			targetHierarchyLevel = targetHierarchyLevels[i]
			sourceType = sourceTypes[i]
			// Segment node
			if (isTargetSegment == true) {
				// Compute hierarchy tabs
				if (targetHierarchyLevel == 0) {
					tabs = DEFAULT_TABS
					tmpArrLineTabs[i] = tabs
					mapHierarchyTabs.put(targetHierarchyLevel, tabs)
					tabs += 1
				} else {
					// Reset to higher or equals hierarchy level
					targetHierarchyLevelPrev = targetHierarchyLevels[i-1]
					if (targetHierarchyLevel <= targetHierarchyLevelPrev) {
						tabs = mapHierarchyTabs.get(targetHierarchyLevel)
					}
					// Set values to temporary line tabs with previous value
					tmpArrLineTabs[i] = tabs
					mapHierarchyTabs.put(targetHierarchyLevel, tabs)
					// Set for non-segment nodes and deeper segment nodes
					if ('node'.equals(sourceType)) {
						// Loop case (Loop over all segments -> + 2)
						tabs += 2
					} else {
						// Constant case (only one segment -> + 1)
						tabs += 1
					}
				}
			// Node tabs are calculated depending on parent segment tabs
			} else {
				tmpArrLineTabs[i] = tabs
			}
		}
	}
	// Set line tabs
	if (targetHierarchyLevels != null) {
		for (int i = 0; i < targetHierarchyLevels.size(); i++) {
			def tmpLineTabs = tmpArrLineTabs.get(i)
			if (tmpLineTabs) {
				lineTabs[i] = tmpLineTabs
			} else {
				lineTabs[i] = DEFAULT_TABS + 1
			}
		}
	}
	return lineTabs
}

/**
* computeRightCurlyBrackets
* @param isTargetSegments This is the isTargetSegments object.
* @param lineTabs This is the lineTabs object.
* @param targetHierarchyLevels This is the targetHierarchyLevels object.
* @param rightCurlyBrackets This is the rightCurlyBrackets object.
* @return rightCurlyBrackets Return rightCurlyBrackets object.
*/
private def computeRightCurlyBrackets(def isTargetSegments, def targetHierarchyLevels, def lineTabs, def rightCurlyBrackets) {
	final String SEPARATOR = ';'
	int countSegments = 0
	// Compute right curly brackets
	if (isTargetSegments != null) {
		for (int i = 0; i < isTargetSegments.size(); i++) {
			rightCurlyBrackets[i] = ''
			boolean isTargetSegment = isTargetSegments[i]
			// Close brackets before segment
			if (isTargetSegment == true && i < isTargetSegments.size() - 1) {
				if (countSegments > 0) {
					def hierarchyLevelPrevious = targetHierarchyLevels[i-1]
					if (hierarchyLevelPrevious.toString().isInteger() == false) {
						hierarchyLevelPrevious = 0
					}
					def hierarchyLevelCurrent = targetHierarchyLevels[i]
					if (hierarchyLevelCurrent.toString().isInteger() == false) {
						hierarchyLevelCurrent = 0
					}
					// Set on deeper or same hierarchy level
					if (hierarchyLevelCurrent <= hierarchyLevelPrevious) {
						def tabsPrevious = lineTabs[i-1]
						if (tabsPrevious.toString().isInteger() == false) {
							tabsPrevious = 0
						}
						def tabsCurrent = lineTabs[i]
						if (tabsCurrent.toString().isInteger() == false) {
							tabsCurrent = 0
						}
						String brackets = ''
						for (int b = tabsPrevious; b > tabsCurrent; b--) {
							if (b == tabsPrevious) {
								brackets += (b - 1).toString() 
							} else {
								brackets += SEPARATOR + (b - 1).toString()
							}
						}
						rightCurlyBrackets[i] = brackets
					}
				}
				countSegments ++
			}
			// Close brackets after last line
			if (i == isTargetSegments.size() - 1) {
				def tabsCurrent = lineTabs[i]
				if (tabsCurrent.toString().isInteger() == false) {
					tabsCurrent = 0
				}
				int tabsLast = 2
				String brackets = ''
				for (int c = tabsCurrent; c > tabsLast; c--) {
					if (c == tabsCurrent) {
						brackets += (c - 1).toString()
					} else {
						brackets += SEPARATOR + (c - 1).toString() 
					}
				}
				rightCurlyBrackets[i] = brackets
			}
		}
	}
	return rightCurlyBrackets
}

/**
* expandList
* @param theList This is the list.
* @param listSize This is the list size.
* @param defaultValue This is the default value.
* @return theList Return the list.
*/
private def expandList(def theList, int listSize, def defaultValue) {
	if (theList.size() < listSize) {
		for (int i = theList.size(); i < listSize; i++) {
			theList[i] = defaultValue
		}
	}
	return theList
}

/**
* reduceSourceSegments
* @param theList This is the list.
* @param listSize This is the list size.
* @param defaultValue This is the default value.
* @return theList Return the list.
*/
private def reduceSourceSegments(def sourceNodes, def sourceSegmentsReduce) {
	if (sourceNodes.size() == sourceSegmentsReduce.size()) {
		for (int i = 0; i < sourceNodes.size(); i++) {
			String sourceNode = sourceNodes[i]
			if (sourceNode) {
				sourceNodes[i] = sourceNode.replaceAll(sourceSegmentsReduce[i], '')
			}
		}
	} else {
		throw new Exception("reduceSourceSegments: Size of sourceNodes[] and size sourceSegmentsReduce[] is not equals.")
	}
	return sourceNodes
}

/**
* createMapString
* @param value This is the value.
* @return value Return value as map string.
*/
private def createMapString(String value) {
    String valueNew = value
	// Input value example: {A=1, B=2} -> Output: ['A':'1', 'B':'2']
	if (valueNew.startsWith("{")) {
		valueNew = valueNew.replaceAll("\\{", "['")
	} else {
		valueNew = "['" + valueNew
	}
	if (valueNew.endsWith("}")) {
		valueNew = valueNew.replaceAll("\\}", "']")
	} else {
		valueNew += "']"
	}
	valueNew = valueNew.replaceAll("=", "':'").replaceAll(", ", "', '")
	return valueNew
}

/**
* addTabs
* @param count This is the count.
* @return result Return repeated tabulators.
*/
private String addTabs(int count) {
	String result = ''
	result = '\t'.multiply(Math.abs(count))
	return result
}

/**
* createRightCurlyBrackets
* @param count This is the count.
* @return result Return create tabulators and right curly brackets .
*/
private String createRightCurlyBrackets(String counts) {
	final String SEPARATOR = ';'
	final String LINE_BREAK = '\r\n'
	String result = ''

	def arrCounts = counts.split(SEPARATOR)

	for (int i = 0; i < arrCounts.size(); i++) {
		String countStr = arrCounts[i]
		if (countStr) {
			int countInt = countStr.toInteger()
			if (countInt) {
				result += '\t'.multiply(Math.abs(countInt)) + '}' + LINE_BREAK
			}
		}
	}
	return result
}

/**
* getEmptyConstant
* @return sourceNode Return source node.
*/
private String getEmptyConstant() {
	return "''"
}

/**
* getConstantValue
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getConstantValue(String sourceNode, int indexOfFormulaSeparator, int indexOfClosingParenthesis) {
	String sourceNodeNew = "'" + sourceNode.substring(indexOfFormulaSeparator + 1, indexOfClosingParenthesis) + "'"
	return sourceNodeNew
}

/**
* getDirectNode
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getDirectNode(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.substring(1, sourceNodeNew.length())
	sourceNodeNew += '.text()'
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.')
	// Set simple quotation marks "'" around node name, if needed
	sourceNodeNew = markNode(sourceNodeNew)
	return sourceNodeNew
}

/**
* getSimpleConcat
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleConcat(String sourceNode) {
	// Simple concat with two values (concat with constant is not supported yet)
	String tmpSourceNode = sourceNode.replaceAll('/', '.').replaceAll('concat\\(', '').replaceAll('delimeter=', '').replaceAll('\\)', '')
	// Split into 3 parts
	def tmpList = []
	tmpList = tmpSourceNode.split(', ', -1)
	for (int i = 0; i < tmpList.size(); i++) {
		String tmpValue = tmpList[i]
		if (tmpValue.startsWith('.') && i < 2) {
			// Remove leading point
			tmpValue = tmpValue.substring(1, tmpValue.length())
			// Set simple quotation marks "'" around node name, if needed
			tmpValue = markNode(tmpValue)
			tmpList[i] = tmpValue + ".text()"
		} else if (tmpValue.startsWith('const(') && i < 2) {
			tmpList[i] = "'" + tmpValue.substring(12, tmpValue.length()) + "'"
		}
	}
	// Create formula
	String result = tmpList[0] + ".concat('" + tmpList[2] + "').concat(" + tmpList[1] + ")"
	return result
}

/**
* getSimpleTransformDate
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleTransformDate(String sourceNode) {
	String sourceNodeNew = sourceNode.replaceAll(', iform=',',_').replaceAll(', oform=',',_').replaceAll(', ',',_')
	String tmpSourceNode = sourceNodeNew.substring(14, sourceNodeNew.length() - 1)
	// Split into 4 parts
	def tmpList = []
	tmpList = tmpSourceNode.split(',_', -1)
	for (int i = 0; i < tmpList.size(); i++) {
		String tmpValue = tmpList[i]
		if (i == 0) {
			// Remove all prefix (Need to do here because date format can have colon)
			tmpValue = removeAllPrefix(tmpValue)
			// Remove leading point
			tmpValue = tmpValue.replaceAll('/', '.').substring(1, tmpValue.length())
			// Set simple quotation marks "'" around node name, if needed
			tmpValue = markNode(tmpValue)
			tmpList[i] = tmpValue
		}
	}
	// Create formula
	String result = "transformDate(timeZone, " + tmpList[0] + '.text(), "' + tmpList[1] + '", "' + tmpList[2] + '")'
	return result
}

/**
* getSimpleGetDateAfterDays
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleGetDateAfterDays(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(').replaceAll(', const\\(value=',',_').replaceAll(', ',',_').replaceAll('\\)','')
	String tmpSourceNode = sourceNodeNew.substring(17, sourceNodeNew.length())
	// Split into 2 parts
	def tmpList = []
	tmpList = tmpSourceNode.split(',_', -1)
	for (int i = 0; i < tmpList.size(); i++) {
		String tmpValue = tmpList[i]
		if (i == 0) {
			// Set simple quotation marks "'" around node name, if needed
			tmpValue = markNode(tmpValue)
			tmpList[i] = tmpValue
		}
	}
	// Create formula
	String result = "getDateAfterDays(timeZone, " + tmpList[0] + '.text(), "yyyyMMdd", ' + tmpList[1] + ', "yyyyMMdd")'
	return result
}

/**
* getSimpleGetDateFormat
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleGetDateFormat(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(14, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = 'getDateFormat(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleFormatValueByZero
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleFormatValueByZero(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(').replaceAll(', const\\(value=',',_').replaceAll(', ',',_').replaceAll('\\)','')
	String tmpSourceNode = sourceNodeNew.substring(18, sourceNodeNew.length())
	// Split into 5 parts
	def tmpList = []
	tmpList = tmpSourceNode.split(',_', -1)
	for (int i = 0; i < tmpList.size(); i++) {
		String tmpValue = tmpList[i]
		if (i == 0) {
			// Set simple quotation marks "'" around node name, if needed
			tmpValue = markNode(tmpValue)
			tmpList[i] = tmpValue
		}
	}
	// Create formula
	String result = "formatValueByZero(" + tmpList[0] + ".text(), " + tmpList[1] + ", '" + tmpList[2] + "', " + tmpList[3] + ", '" + tmpList[4] + "')"
	return result
}

/**
* getSimpleMapWithDefault
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleMapWithDefault(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(').replaceAll(', result\\)',")").replaceAll(', default_value=',", ")
	String tmpSourceNode = sourceNodeNew.substring(15, sourceNodeNew.length() - 1)
	// Split into 2 parts
	def tmpList = []
	tmpList = tmpSourceNode.split(', ', -1)
	for (int i = 0; i < tmpList.size(); i++) {
		if (i == 0) {
			// Set simple quotation marks "'" around node name, if needed
			tmpList[i] = markNode(tmpList[i])
			break
		}
	}
	// Create formula
	String result = "mapWithDefault(" + tmpList[0] + ".text(), '" + tmpList[1] + "')"
	if (!tmpList[1]) {
		result += " // TODO: Please check if mapWithDefault() with an empty value is really needed here."
	}
	return result
}

/**
* getSimpleFixedValues
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleFixedValues(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(').replaceAll(', table=FixedValues\\{',',_').replaceAll('\\}','').replaceAll(', vmstrategy=',',_').replaceAll(', vmdefault=',',_')
	String tmpSourceNode = sourceNodeNew.substring(10, sourceNodeNew.length() - 1)
	// Split into 4 parts
	def tmpList = []
	tmpList = tmpSourceNode.split(',_', -1)
	for (int i = 0; i < tmpList.size(); i++) {
		if (i == 0) {
			// Set simple quotation marks "'" around node name, if needed
			tmpList[i] = markNode(tmpList[i])
		} else if (i == 1) {
			// Create map string
			tmpList[i] = createMapString(tmpList[i])
		} else if (i == 2) {
			switch(tmpList[i]) {
				case '0':
					tmpList[i] = 'Throw exception'
					break
				case '1':
					tmpList[i] = 'Use key'
					break
				case '2':
					tmpList[i] = 'Default value'
					break
				default:
					tmpList[i] = ''
					break
			}
		}
	}
	// Create formula
	String result = "fixedValues(" + tmpList[0] + ".text(), " + tmpList[1] + ", '" + tmpList[2] + "', '" + tmpList[3] + "')"
	return result
}

/**
* getSimpleValueMap
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleValueMap(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(').replaceAll('\\}','').replaceAll(', srcns=',',_')
							.replaceAll(', agency1=',',_').replaceAll(', schema1=',',_').replaceAll(', agency2=',',_').replaceAll(', schema2=',',_')
							.replaceAll(', vmstrategy=',',_').replaceAll(', vmdefault=',',_')
	String tmpSourceNode = sourceNodeNew.substring(9, sourceNodeNew.length() - 1)
	// Split into 8 parts
	def tmpList = []
	tmpList = tmpSourceNode.split(',_', -1)
	for (int i = 0; i < tmpList.size(); i++) {
		if (i == 0) {
			// Set simple quotation marks "'" around node name, if needed
			tmpList[i] = markNode(tmpList[i])
		} else if (i == 6) {
			switch(tmpList[i]) {
				case '0':
					tmpList[i] = 'Throw exception'
					break
				case '1':
					tmpList[i] = 'Use key'
					break
				case '2':
					tmpList[i] = 'Default value'
					break
				default:
					tmpList[i] = ''
					break
			}
		}
	}
	// Create formula
	String result = "valueMap(valueMapApi, '" + tmpList[2] + "', '" + tmpList[3] + "', " + tmpList[0] + ".text(), '" + tmpList[4] + "', '" + tmpList[5] + "', '" + tmpList[6] + "', '" + tmpList[7] + "')"
	return result
}

/**
* getSimpleFormatNumber
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleFormatNumber(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(').replaceAll('\\}','').replaceAll(', nformat=',',_').replaceAll(', separator=',',_')
	String tmpSourceNode = sourceNodeNew.substring(13, sourceNodeNew.length() - 1)
	// Split into 3 parts
	def tmpList = []
	tmpList = tmpSourceNode.split(',_', -1)
	for (int i = 0; i < tmpList.size(); i++) {
		if (i == 0) {
			// Set simple quotation marks "'" around node name, if needed
			tmpList[i] = markNode(tmpList[i])
			break
		}
	}
	// Create formula
	String result = "formatNumber(" + tmpList[0] + ".text(), '" + tmpList[1] + "', '" + tmpList[2] + "')"
	return result
}

/**
* getSimpleTrim
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleTrim(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(5, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = sourceNodeNew + '.text().trim()'
	return sourceNodeNew
}

/**
* getSimpleTrimZeroLeft
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleTrimZeroLeft(String sourceNode) {
    String sourceNodeNew = sourceNode
	int startIndex
	String methodName = ''
	if (sourceNodeNew.startsWith('removeLeadingZeros(')) {
		startIndex = 19
		methodName = 'trimZeroLeft'
	} else {
		startIndex = 13
		methodName = 'trimZeroLeft'
	}
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')	
	sourceNodeNew = sourceNodeNew.substring(startIndex, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = methodName + '(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleTrimRight
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleTrimRight(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(10, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = 'trimRight(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleSetCurrentDate
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleSetCurrentDate(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('set', '').replaceAll('oform=', '')
	String dateFormat = ''
	if (sourceNodeNew.indexOf(',') > -1) {
		dateFormat = sourceNodeNew.substring(12, sourceNodeNew.indexOf(','))
	} else {
		dateFormat = sourceNodeNew.substring(13, sourceNodeNew.length()-1)
		dateFormat = dateFormat.replaceAll('\"', '')
	}
	String result = 'setCurrentDate(timeZone, "' + dateFormat + '")'
	return result
}

/**
* getSimpleSetDecimalSeparatorPoint
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleSetDecimalSeparatorPoint(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(25, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	String result = 'setDecimalSeparatorPoint(' + sourceNodeNew + '.text())'
	return result
}

/**
* getSimpleSetDefaultAsCurrentDate
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleSetDefaultAsCurrentDate(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(').replaceAll('const\\(value=', '').replaceAll('\\)', '')
	if (sourceNodeNew.startsWith('"')) {
		sourceNodeNew = sourceNodeNew.substring(25, sourceNodeNew.length()-1)
	} else {
		sourceNodeNew = sourceNodeNew.substring(24, sourceNodeNew.length())
	}
	// Split into 2 parts
	def tmpList = []
	tmpList = sourceNodeNew.split(', ', -1)
	for (int i = 0; i < tmpList.size(); i++) {
		String tmpValue = tmpList[i]
		
		if (i == 0) {
			// Set simple quotation marks "'" around node name, if needed
			tmpList[i] = markNode(tmpValue)
		} else if (i == 1 && tmpValue.startsWith('"')) {
			tmpList[i] = tmpValue.replaceAll('\"', '')
		}
	}
	String result = 'setDefaultAsCurrentDate(timeZone, ' + tmpList[0] + '.text(), "' + tmpList[1] + '")'
	return result
}

/**
* getSimpleStripSpaces
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleStripSpaces(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(12, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = 'stripSpaces(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleToLowerCase
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleToLowerCase(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(12, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = sourceNodeNew + '.text().toLowerCase()'
	return sourceNodeNew
}

/**
* getSimpleToUpperCase
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleToUpperCase(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(12, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = sourceNodeNew + '.text().toUpperCase()'
	return sourceNodeNew
}

/**
* getSimpleCopyValue
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleCopyValue(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(10, sourceNodeNew.indexOf(','))
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew += '.text()'
	return sourceNodeNew
}

/**
* getSimpleMinusFromBeginToEnd
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleMinusFromBeginToEnd(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(20, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = 'minusFromBeginToEnd(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleMinusFromEndToBegin
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleMinusFromEndToBegin(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(20, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = 'minusFromEndToBegin(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleDynamicSubstring
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleDynamicSubstring(String sourceNode) {
    String sourceNodeNew = sourceNode
    int startIndex
	if (sourceNodeNew.startsWith('substring(')) {
		startIndex = 10
		sourceNodeNew = sourceNodeNew.replaceAll('start=', '').replaceAll('count=', '')
	} else {
		startIndex = 17
	}
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(startIndex, sourceNodeNew.length() - 1)
	// Split into 3 parts
	def tmpList = []
	tmpList = sourceNodeNew.split(', ', -1)
	for (int i = 0; i < tmpList.size(); i++) {
		if (i == 0) {
			// Set simple quotation marks "'" around node name, if needed
			tmpList[i] = markNode(tmpList[i])
			break
		}
	}	
	String result = 'dynamicSubstring(' + tmpList[0] + '.text(), ' + tmpList[1] + ', ' + tmpList[2] + ')'
	return result
}

/**
* getSimpleReplaceString
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleReplaceString(sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(').replaceAll('const\\(value=', '').replaceAll('\\)', '')
	sourceNodeNew = sourceNodeNew.substring(14, sourceNodeNew.length())
	// Split into 3 parts
	def tmpList = []
	tmpList = sourceNodeNew.split(', ', -1)
	for (int i = 0; i < tmpList.size(); i++) {
		if (i == 0) {
			// Set simple quotation marks "'" around node name, if needed
			tmpList[i] = markNode(tmpList[i])
		} else if (i == 1) {
			// Some charcter replacement are needed because RegEx
			String regex = tmpList[i]
			if (regex == '.') {
				tmpList[i] = '\\\\.'
			}
		}
	}	
	String result = tmpList[0] + ".text().replaceAll('" + tmpList[1] + "', '" + tmpList[2] + "')"
	return result
}

/**
* getSimpleCount
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleCount(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(6, sourceNodeNew.length() - 9)
	// Need to use size() instead of count()
	sourceNodeNew = sourceNodeNew + '.size()'
	return sourceNodeNew
}

/**
* getSimpleToNumber
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleToNumber(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(9, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = 'toNumber(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleRemoveAlgebraicSign
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleRemoveAlgebraicSign(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(20, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = 'removeAlgebraicSign(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleRemoveAlgebraicSignPlus
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleRemoveAlgebraicSignPlus(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(24, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = 'removeAlgebraicSignPlus(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleRemoveAllCharacters
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleRemoveAllCharacters(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(20, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = 'removeAllCharacters(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleRemoveAllSpaces
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleRemoveAllSpaces(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(16, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = 'removeAllSpaces(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleRemoveAllSpecialCharacters
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleRemoveAllSpecialCharacters(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(27, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = 'removeAllSpecialCharacters(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleGetOnlyNumbers
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleGetOnlyNumbers(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(15, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = 'getOnlyNumbers(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleHeadString
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleHeadString(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(11, sourceNodeNew.length() - 1)	
	// Split into 2 parts
	def tmpList = []
	tmpList = sourceNodeNew.split(', ', -1)
	for (int i = 0; i < tmpList.size(); i++) {
		if (i == 0) {
			// Set simple quotation marks "'" around node name, if needed
			tmpList[i] = markNode(tmpList[i])
		} else if (i == 1) {
			tmpList[i] = tmpList[i].replaceAll('const\\(value=', '').replaceAll('\\)', '')
		}
	}	
	String result = 'headString(' + tmpList[0] + '.text(), ' + tmpList[1] + ')'
	return result
}

/**
* getSimpleTailString
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleTailString(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(11, sourceNodeNew.length() - 1)	
	// Split into 2 parts
	def tmpList = []
	tmpList = sourceNodeNew.split(', ', -1)
	for (int i = 0; i < tmpList.size(); i++) {
		if (i == 0) {
			// Set simple quotation marks "'" around node name, if needed
			tmpList[i] = markNode(tmpList[i])
		} else if (i == 1) {
			tmpList[i] = tmpList[i].replaceAll('const\\(value=', '').replaceAll('\\)', '')
		}
	}	
	String result = 'tailString(' + tmpList[0] + '.text(), ' + tmpList[1] + ')'
	return result
}

/**
* getSimpleFillLeadingZeros
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleFillLeadingZeros(sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(17, sourceNodeNew.length() - 1)	
	// Split into 2 parts
	def tmpList = []
	tmpList = sourceNodeNew.split(', ', -1)
	for (int i = 0; i < tmpList.size(); i++) {
		if (i == 0) {
			// Set simple quotation marks "'" around node name, if needed
			tmpList[i] = markNode(tmpList[i])
		} else if (i == 1) {
			tmpList[i] = tmpList[i].replaceAll('const\\(value=', '').replaceAll('\\)', '')
		}
	}	
	String result = 'fillLeadingZeros(' + tmpList[0] + '.text(), ' + tmpList[1] + ')'
	return result
}

/**
* getSimpleFillUpToLengthWithSpace
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleFillUpToLengthWithSpace(sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(24, sourceNodeNew.length() - 1)
	// Split into 2 parts
	def tmpList = []
	tmpList = sourceNodeNew.split(', ', -1)
	for (int i = 0; i < tmpList.size(); i++) {
		if (i == 0) {
			// Set simple quotation marks "'" around node name, if needed
			tmpList[i] = markNode(tmpList[i])
		} else if (i == 1) {
			tmpList[i] = tmpList[i].replaceAll('const\\(value=', '').replaceAll('\\)', '')
		}
	}	
	String result = 'fillUpToLengthWithSpace(' + tmpList[0] + '.text(), ' + tmpList[1] + ')'
	return result
}

/**
* getSimpleRemoveCarriageReturnLineFeed
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleRemoveCarriageReturnLineFeed(sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(29, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = 'removeCarriageReturnLineFeed(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleRemoveDecimalIfZero
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleRemoveDecimalIfZero(sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(20, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = 'removeDecimalIfZero(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleReplaceSpecialCharacters
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleReplaceSpecialCharacters(sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(25, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = 'replaceSpecialCharacters(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleReplaceUmlauts
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleReplaceUmlauts(sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(15, sourceNodeNew.length() - 1)
	sourceNodeNew = markNode(sourceNodeNew)
	sourceNodeNew = 'replaceUmlauts(' + sourceNodeNew + '.text())'
	return sourceNodeNew
}

/**
* getSimpleThrowExceptionIfNoValue
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getSimpleThrowExceptionIfNoValue(sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(')
	sourceNodeNew = sourceNodeNew.substring(24, sourceNodeNew.length() - 1)	
	// Split into 2 parts
	def tmpList = []
	tmpList = sourceNodeNew.split(', ', -1)
	for (int i = 0; i < tmpList.size(); i++) {
		if (i == 0) {
			// Set simple quotation marks "'" around node name, if needed
			tmpList[i] = markNode(tmpList[i])
		} else if (i == 1) {
			tmpList[i] = tmpList[i].replaceAll('const\\(value=', '').replaceAll('\\)', '')
		}
	}	
	sourceNodeNew = "throwExceptionIfNoValue(" + tmpList[0] + ".text(), '" + tmpList[1] + "')"
	return sourceNodeNew
}

/**
* getSimpleMessageID
* @return sourceNode Return source node.
*/
private String getSimpleMessageID() {
	return "message.getProperty('SAP_MessageProcessingLogID')"
}

/**
* getSimpleSender
* @return sourceNode Return source node.
*/
private String getSimpleSender() {
	return "message.getHeader('SAP_Sender', String)"
}

/**
* getSimpleReceiver
* @return sourceNode Return source node.
*/
private String getSimpleReceiver() {
	return "message.getHeader('SAP_Receiver', String)"
}

/**
* setSimpleVariable
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String setSimpleVariable(String sourceNode) {
    String sourceNodeNew = sourceNode
	if (sourceNodeNew.startsWith('setVariable')) {
		sourceNodeNew = sourceNodeNew.replaceAll('\\)','')
		// Split into 2 parts
		def tmpList = []
		tmpList = sourceNodeNew.split(',', -1)
		sourceNodeNew = tmpList[1]
	}
	return sourceNodeNew
}

/**
* getSimpleVariable
* @param sourceNode This is source node.
* @param message This is the message.
* @return sourceNode Return source node.
*/
private String getSimpleVariable(String sourceNode) {
    String sourceNodeNew = sourceNode
	if (sourceNodeNew.startsWith('getVariable')) {
		sourceNodeNew = sourceNodeNew.substring(13, sourceNodeNew.length() - 2)
	} else {
		sourceNodeNew = sourceNodeNew.substring(1, sourceNodeNew.length())
	}
	return sourceNodeNew
}

/**
* getComplexFormula
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String getComplexFormula(String sourceNode) {
    String sourceNodeNew = sourceNode
	sourceNodeNew = 'TODO: ' + sourceNodeNew
	// Change '/' to '.' for easier use nodes in Groovy
	if(sourceNodeNew.indexOf('http:') == -1) {
		sourceNodeNew = sourceNodeNew.replaceAll('/', '.').replaceAll('\\(\\.','(').replaceAll(', \\.',', ')
	}
	return sourceNodeNew
}

/**
* markNode
* @param sourceNode This is source node.
* @return sourceNode Return source node.
*/
private String markNode(String sourceNode) {
    String sourceNodeNew = sourceNode
	if (sourceNodeNew.indexOf('-') > -1 || sourceNodeNew.indexOf(':') > -1) {
		def tmpList = []
		tmpList = sourceNodeNew.split('\\.', -1)
		for (int i = 0; i < tmpList.size(); i++) {
			String tmpValue = tmpList[i]
			if (i == 0) {
				// Remove leading underscore and all minus
				if (tmpValue.startsWith('_')) {
					tmpValue = tmpValue.substring(1, tmpValue.length())
				}
				if (tmpValue.indexOf('-') > -1) {
					tmpValue = tmpValue.replaceAll('-', '')
				}
				tmpList[i] = tmpValue
			} else {
				// Set simple quotation marks "'" around node name
				if (tmpValue.startsWith('_') || tmpValue.indexOf('-') > -1 || tmpValue.indexOf(':') > -1) {
					tmpList[i] = "'" + tmpValue + "'"
				}
			}
		}
		sourceNodeNew = tmpList.join('.')
	}
	return sourceNodeNew
}

/**
* appendMethodIsValidLocale
* @param sb This is the string builder.
* @param methodIsValidLocale This is activation for methodIsValidLocale.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodIsValidLocale(def sb, boolean methodIsValidLocale, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodIsValidLocale == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Checks if locale is valid.' + lineBreak)
		sb.append('* @param value This is the value.' + lineBreak)
		sb.append('* @return result Returns true if locale is valid.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def boolean isValidLocale(String value) {' + lineBreak)
		sb.append('	boolean result = false' + lineBreak)
		sb.append('	Locale[] locales = Locale.getAvailableLocales()' + lineBreak)
		sb.append('	for (Locale locale : locales) {' + lineBreak)
		sb.append('		if (value.equals(locale.toString())) {' + lineBreak)
		sb.append('			result = true' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodAssignValueByCondition
* @param sb This is the string builder.
* @param methodAssignValueByCondition This is activation for methodAssignValueByCondition.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodAssignValueByCondition(def sb, boolean methodAssignValueByCondition, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodAssignValueByCondition == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Passes a value from value node when the corresponding value from condition node equals such a value.' + lineBreak)
		sb.append('* @param conditionValue This is the condition value.' + lineBreak)
		sb.append('* @param suchValues This is the such values.' + lineBreak)
		sb.append('* @param returnValue This is the return value.' + lineBreak)
		sb.append('* @return result Returns a value when the corresponding value has one of the such value contained.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String assignValueByCondition(String conditionValue, def suchValues, String returnValue) {' + lineBreak)
		sb.append('	String[] theSuchValues' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	if (suchValues instanceof List) {' + lineBreak)
		sb.append('		theSuchValues = suchValues' + lineBreak)
		sb.append('	} else {' + lineBreak)
		sb.append('		theSuchValues = [suchValues.toString()]' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	// Check suchValue' + lineBreak)
		sb.append('	if (!theSuchValues[0]) {' + lineBreak)
		sb.append('		throw new Exception("assignValueByCondition: There is no suchValue.")' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	for (int i = 0; i < theSuchValues.size(); i++) {' + lineBreak)
		sb.append('		if (conditionValue.equals(theSuchValues[i])) {' + lineBreak)
		sb.append('			result = returnValue' + lineBreak)
		sb.append('			break' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodDynamicSubstring
* @param sb This is the string builder.
* @param methodDynamicSubstring This is activation for methodDynamicSubstring.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodDynamicSubstring(def sb, boolean methodDynamicSubstring, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodDynamicSubstring == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append("* Gets dynamic substring from value. First argument is value. Second argument is start, begins with '0'. Third argument is length." + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @param start Start' + lineBreak)
		sb.append('* @param length Length' + lineBreak)
		sb.append('* @return result Returns the substring.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String dynamicSubstring(String value, int start, int length) {' + lineBreak)
		sb.append('	int startPos = start' + lineBreak)
		sb.append('	int endPos = start + length - 1' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	if (startPos < 0) {' + lineBreak)
		sb.append('		// Start position is before start of value, return empty string' + lineBreak)
		sb.append("		result = ''" + lineBreak)
		sb.append('	} else if (startPos >= 0 && startPos < value.length()) {' + lineBreak)
		sb.append('		if (endPos < value.length()) {' + lineBreak)
		sb.append('			// Start & end positions are before end of value, return the partial substring' + lineBreak)
		sb.append('			result = value.substring( startPos, endPos + 1 )' + lineBreak)
		sb.append('		 } else if (endPos >= value.length() ) {' + lineBreak)
		sb.append('			// Start position is before start of value but end position is after end of value, return from start till end of value' + lineBreak)
		sb.append('			result = value.substring(startPos, value.length())' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	} else if (startPos >= value.length()) {' + lineBreak)
		sb.append('		// Start position is after end of value, return empty string' + lineBreak)
		sb.append("		result = ''" + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodFillLeadingZeros
* @param sb This is the string builder.
* @param methodFillLeadingZeros This is activation for methodFillLeadingZeros.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodFillLeadingZeros(def sb, boolean methodFillLeadingZeros, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodFillLeadingZeros == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Creates a new string with leading zeros from a non-empty first argument with length given by the second. Values will not be cut.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @param length Length' + lineBreak)
		sb.append('* @return result Returns an new string with leading zeros.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String fillLeadingZeros(String value, int totalLength) {' + lineBreak)
		sb.append('	String result = value' + lineBreak)
		sb.append('	if (value && totalLength > 0) {' + lineBreak)
		sb.append('		int repeatLength = totalLength - value.length()' + lineBreak)
		sb.append('		if (repeatLength > 0) {' + lineBreak)
		sb.append("			result = '0'.multiply(repeatLength) + result" + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodFillUpToLengthWithSpace
* @param sb This is the string builder.
* @param methodFillUpToLengthWithSpace This is activation for methodFillUpToLengthWithSpace.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodFillUpToLengthWithSpace(def sb, boolean methodFillUpToLengthWithSpace, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodFillUpToLengthWithSpace == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Creates a new string with space at end of string from a non-empty first argument with length given by the second. Values will not be cut.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @param totalLength Total length' + lineBreak)
		sb.append('* @return result Returns an new string with spaces at end of string.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String fillUpToLengthWithSpace(String value, int totalLength) {' + lineBreak)
		sb.append('	String result = value' + lineBreak)
		sb.append('	if (value && totalLength > 0) {' + lineBreak)
		sb.append('		int repeatLength = totalLength - value.length()' + lineBreak)
		sb.append('		if (repeatLength > 0) {' + lineBreak)
		sb.append("			result += ' '.multiply(repeatLength)" + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodFixedValues
* @param sb This is the string builder.
* @param methodFixedValues This is activation for methodFixedValues.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodFixedValues(def sb, boolean methodFixedValues, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodFixedValues == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* fixedValues' + lineBreak)
		sb.append('* @param value This is the value.' + lineBreak)
		sb.append('* @param mapTable This is the table.' + lineBreak)
		sb.append("* @param behaviour This is the behaviour if lookup fails. ('Default value', 'Use key', 'Throw exception')" + lineBreak)
		sb.append('* @param defaultValue This is the default value.' + lineBreak)
		sb.append('* @return result Returns the result.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String fixedValues(String value, def mapTable, String behaviour, String defaultValue) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append("	String behaviourStr = behaviour.toLowerCase().replaceAll(' ','')" + lineBreak)
		sb.append('	// Check input values' + lineBreak)
		sb.append('	if (mapTable == null || mapTable.any() == false) {' + lineBreak)
		sb.append('		throw Exception("fixedValues: No \'mapTable\' parameter found in \'fixedValues\' for value \'$value\'.")' + lineBreak)
		sb.append("	} else if (!('defaultvalue'.equals(behaviourStr) || 'usekey'.equals(behaviourStr) || 'throwexception'.equals(behaviourStr))) {" + lineBreak)
		sb.append('		throw Exception("fixedValues: No allowed \'behaviour\' parameter found in \'fixedValues\' for value \'$value\'. \'Default value\', \'Use key\' and \'Throw exception\' are allowed.")' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	//Do the mapping' + lineBreak)
		sb.append('	result = mapTable.get(value)' + lineBreak)
		sb.append('	// Error strategy' + lineBreak)
		sb.append('	if(result == null) {' + lineBreak)
		sb.append('		switch(behaviourStr) {' + lineBreak)
		sb.append("			case 'throwexception':" + lineBreak)
		sb.append('				throw Exception("fixedValues: No entry for value \'$value\' found in \'fixedValues\' map table [" + mapTable.collect{it}.join(\',\') + "].")' + lineBreak)
		sb.append("			case 'usekey':" + lineBreak)
		sb.append('				result = value' + lineBreak)
		sb.append('				break' + lineBreak)
		sb.append("			case 'defaultvalue':" + lineBreak)
		sb.append('				result = defaultValue' + lineBreak)
		sb.append('				break' + lineBreak)
		sb.append('			default:' + lineBreak)
		sb.append('				throw Exception("fixedValues: Behaviour parameter \'$behaviour\' not set properly in \'fixedValues\'.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodFormatNumber
* @param sb This is the string builder.
* @param methodFormatNumber This is activation for methodFormatNumber.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodFormatNumber(def sb, boolean methodFormatNumber, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodFormatNumber == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Format the number by defined pattern and decimal separator.' + lineBreak)
		sb.append('* @param numberStr Number string' + lineBreak)
 		sb.append('* @param pattern Pattern (number format)' + lineBreak)
		sb.append('* @param decimalSeparator Decimal separator in output number string' + lineBreak)
		sb.append('* @return result Returns an formatted number string.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String formatNumber(String numberStr, String pattern, String decimalSeparator) {' + lineBreak)
		sb.append('	String result = numberStr' + lineBreak)
		sb.append('	DecimalFormat decimalFormat' + lineBreak)
		sb.append('	if (numberStr && pattern && decimalSeparator) {' + lineBreak)
		sb.append('		char decimalSeparatorChar = decimalSeparator.charAt(0)' + lineBreak)
		sb.append('		// Set decimal format symbols' + lineBreak)
		sb.append('		DecimalFormatSymbols symbols = new DecimalFormatSymbols()' + lineBreak)
		sb.append('		symbols.setDecimalSeparator(decimalSeparatorChar)' + lineBreak)
		sb.append("		if (decimalSeparator.startsWith(',')) {" + lineBreak)
		sb.append("			char groupingSeparatorChar = '.'" + lineBreak)
		sb.append('			symbols.setGroupingSeparator(groupingSeparatorChar)' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Convert input string to double' + lineBreak)
		sb.append('		double numberDouble = 0' + lineBreak)
		sb.append('		try {' + lineBreak)
		sb.append('			numberDouble = Double.parseDouble(numberStr)' + lineBreak)
		sb.append('		} catch (Exception) {' + lineBreak)
		sb.append('			throw Exception("formatNumber: Cannot cast \'$numberStr\' to decimal number.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Initialize decimal formatter' + lineBreak)
		sb.append('		try {' + lineBreak)
		sb.append('			decimalFormat = new DecimalFormat(pattern, symbols)' + lineBreak)
		sb.append('		} catch (Exception) {' + lineBreak)
		sb.append('			throw Exception("formatNumber: Malformed pattern \'$pattern\'.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Format number string' + lineBreak)
		sb.append('		try {' + lineBreak)
		sb.append('			result = decimalFormat.format(numberDouble)' + lineBreak)
		sb.append('		} catch (Exception) {' + lineBreak)
		sb.append('			throw Exception("formatNumber: \'$numberStr\' could not change to pattern \'$pattern\'.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodFormatValueBySpace
* @param sb This is the string builder.
* @param methodFormatValueBySpace This is activation for methodFormatValueBySpace.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodFormatValueBySpace(def sb, boolean methodFormatValueBySpace, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodFormatValueBySpace == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Creates a new string from a non empty first argument with length given by the second as follows: cut at left or right end (the third argument) or fill (if the fourth says \'true\') at left or right side (defined by the fifth).' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @param length Length' + lineBreak)
		sb.append("* @param cutLengthDirection Cut length direction 'left' or 'right' end cut" + lineBreak)
		sb.append("* @param fillSpace Fill space 'true' = fill" + lineBreak)
		sb.append("* @param fillSpaceDirection Fill space direction 'left' or 'right' end fill" + lineBreak)
		sb.append('* @return result Returns a new string from a non empty.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String formatValueBySpace(String value, int length, String cutLengthDirection, boolean fillSpace, String fillSpaceDirection) {' + lineBreak)
		sb.append('	String result = null' + lineBreak)
		sb.append('	if (value != null) {' + lineBreak)
		sb.append('		int lengthValue = value.length()' + lineBreak)
		sb.append('		if (lengthValue > 0 && lengthValue != length) {' + lineBreak)
		sb.append('			if (lengthValue > length) {' + lineBreak)
		sb.append('				if ("left".equalsIgnoreCase(cutLengthDirection)) {' + lineBreak)
		sb.append('					int offset = lengthValue - length' + lineBreak)
		sb.append('					result = value.substring(offset, lengthValue)' + lineBreak)
		sb.append('				} else if ("right".equalsIgnoreCase(cutLengthDirection)) {' + lineBreak)
		sb.append('					result = value.substring(0, length)' + lineBreak)
		sb.append('				} else {' + lineBreak)
		sb.append('					throw new Exception("formatValueBySpace: Unexpected value \'$cutLengthDirection\' for the cutDirection.")' + lineBreak)
		sb.append('				}' + lineBreak)
		sb.append('			} else {' + lineBreak)
		sb.append('				if (fillSpace) {' + lineBreak)
		sb.append('					// now lengthValue < length' + lineBreak)
		sb.append('					int offset = length - lengthValue' + lineBreak)
		sb.append('					String SpaceString = "\u00A0"' + lineBreak)
		sb.append('					for (int i = 0; i < offset - 1; i++) {' + lineBreak)
		sb.append('						SpaceString += "\u00A0"' + lineBreak)
		sb.append('					}' + lineBreak)
		sb.append('					if ("left".equalsIgnoreCase(fillSpaceDirection)) {' + lineBreak)
		sb.append('						result = SpaceString + value' + lineBreak)
		sb.append('					} else if ("right".equalsIgnoreCase(fillSpaceDirection)) {' + lineBreak)
		sb.append('						result = value + SpaceString' + lineBreak)
		sb.append('					} else {' + lineBreak)
		sb.append('						throw new Exception("formatValueBySpace: Unexpected value \'$fillSpaceDirection\' for the fillDirection.")' + lineBreak)
		sb.append('					}' + lineBreak)
		sb.append('				}' + lineBreak)
		sb.append('			}' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		if (result == null) {' + lineBreak)
		sb.append('			result = value' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	} else {' + lineBreak)
		sb.append('		result = value' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodFormatValueByZero
* @param sb This is the string builder.
* @param methodFormatValueByZero This is activation for methodFormatValueByZero.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodFormatValueByZero(def sb, boolean methodFormatValueByZero, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodFormatValueByZero == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Creates a new string from a non-empty first argument with length given by the second as follows: cut at left or right end (the third argument) or fill with \'0\' (if the fourth says \'true\') at left or right side (defined by the fifth).' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @param length Length' + lineBreak)
		sb.append("* @param cutLengthDirection Cut length direction 'left' or 'right' end cut" + lineBreak)
		sb.append("* @param fillZero Fill zero 'true' = fill" + lineBreak)
		sb.append("* @param fillZeroDirection Fill zero direction 'left' or 'right' end fill" + lineBreak)
		sb.append('* @return result Returns a new string with zeros.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String formatValueByZero(String value, int length, String cutLengthDirection, boolean fillZero, String fillZeroDirection) {' + lineBreak)
		sb.append('	String result = null' + lineBreak)
		sb.append('	if (value) {' + lineBreak)
		sb.append('		int lengthValue = value.length()' + lineBreak)
		sb.append('		if (lengthValue > 0 && lengthValue != length) {' + lineBreak)
		sb.append('			if (lengthValue > length) {' + lineBreak)
		sb.append('				if ("left".equalsIgnoreCase(cutLengthDirection)) {' + lineBreak)
		sb.append('					int offset = lengthValue - length' + lineBreak)
		sb.append('					result = value.substring(offset, lengthValue)' + lineBreak)
		sb.append('				} else if ("right".equalsIgnoreCase(cutLengthDirection)) {' + lineBreak)
		sb.append('					result = value.substring(0, length)' + lineBreak)
		sb.append('				} else {' + lineBreak)
		sb.append('					throw new Exception("formatValueByZero: Unexpected value \'$cutLengthDirection\' for the cutDirection.")' + lineBreak)
		sb.append('				}' + lineBreak)
		sb.append('			} else {' + lineBreak)
		sb.append('				if (fillZero) {' + lineBreak)
		sb.append('					// now lengthValue < length' + lineBreak)
		sb.append('					int offset = length - lengthValue' + lineBreak)
		sb.append('					String zeroString = "0"' + lineBreak)
		sb.append('					for (int i = 0; i < offset - 1; i++) {' + lineBreak)
		sb.append('						zeroString += "0"' + lineBreak)
		sb.append('					}' + lineBreak)
		sb.append('					if ("left".equalsIgnoreCase(fillZeroDirection)) {' + lineBreak)
		sb.append('						result = zeroString + value' + lineBreak)
		sb.append('					} else if ("right".equalsIgnoreCase(fillZeroDirection)) {' + lineBreak)
		sb.append('						result = value + zeroString' + lineBreak)
		sb.append('					} else {' + lineBreak)
		sb.append('						throw new Exception("formatValueByZero: Unexpected value \'$fillZeroDirection\' for the fillDirection.")' + lineBreak)
		sb.append('					}' + lineBreak)
		sb.append('				}' + lineBreak)
		sb.append('			}' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		if (result == null) {' + lineBreak)
		sb.append('			result = value' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	} else {' + lineBreak)
		sb.append('		result = value' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodGetDate
* @param sb This is the string builder.
* @param methodGetExchangeProperty This is activation for methodGetDate.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodGetDate(def sb, boolean methodGetDate, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodGetDate == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Creates a yyyyMMdd date or time by using an EDIFACT-F2379-conform qualifier.' + lineBreak)
		sb.append('*' + lineBreak)
		sb.append('* @param locale Locale' + lineBreak)
		sb.append('* @param dateValue Date values' + lineBreak)
		sb.append("* @param dateFormat EDIFACT Date format ('2', '3', '101', '102', '201, '202', '203', '204', '401', '402', '718')" + lineBreak)
		sb.append("* @param mode Mode ('date', 'dateFrom', 'dateTo', 'time')" + lineBreak)
		sb.append('* @return formatted date values (yyyyMMdd) if a date-mode is set or time if time-mode is set.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String getDate(Locale locale, String dateValue, String dateFormat, String mode) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)	
		sb.append('	// Check locale' + lineBreak)
		sb.append('	if (!locale) {' + lineBreak)
		sb.append('		throw new Exception("Custom Function getDate: cannot get locale.")' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	// Format date' + lineBreak)
		sb.append('	if (dateValue) {' + lineBreak)
		sb.append('		if (!"date".equalsIgnoreCase(mode) && !"dateto".equalsIgnoreCase(mode) && !"datefrom".equalsIgnoreCase(mode) && !"time".equalsIgnoreCase(mode)) {' + lineBreak)
		sb.append('			throw new Exception("Custom Function getDate: unvalid mode \'" + mode + "\'. Please use Custom Function constantDate, constantDateFrom, constantDateTo or constantTime.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		try {' + lineBreak)
		sb.append('			String dateValueStr = dateValue.trim()' + lineBreak)
		sb.append('			int dateLength = dateValueStr.length()' + lineBreak)
		sb.append('			int dtmFormat = Integer.parseInt(dateFormat)' + lineBreak)
		sb.append('			boolean unexpectedLength = false' + lineBreak)
		sb.append('			boolean modeIsDateTo = "dateto".equalsIgnoreCase(mode)' + lineBreak)
		sb.append('			boolean modeIsTime = "time".equalsIgnoreCase(mode)' + lineBreak)
		sb.append('			String date = null' + lineBreak)
		sb.append('			if (dateValue == null || dateValue.length() == 0 || dateValue.replaceAll("^0*", "").length() == 0) {' + lineBreak)
		sb.append("				result = ''" + lineBreak)
		sb.append('			} else {' + lineBreak)
		sb.append('				try {' + lineBreak)
		sb.append('					switch (dtmFormat) {' + lineBreak)
		sb.append('						case 2: // DDMMYY' + lineBreak)
		sb.append('							if (dateLength != 6) {' + lineBreak)
		sb.append('								unexpectedLength = true' + lineBreak)
		sb.append('							} else if (!modeIsTime) {' + lineBreak)
		sb.append('								date = "20" + dateValueStr.substring(4, 6) + dateValueStr.substring(2, 4) + dateValueStr.substring(0, 2)' + lineBreak)
		sb.append('							}' + lineBreak)
		sb.append('							break' + lineBreak)
		sb.append('						case 3: // MMDDYY' + lineBreak)
		sb.append('							if (dateLength != 6) {' + lineBreak)
		sb.append('								unexpectedLength = true' + lineBreak)
		sb.append('							} else if (!modeIsTime) {' + lineBreak)
		sb.append('								date = "20" + dateValueStr.substring(4, 6) + dateValueStr.substring(0, 2) + dateValueStr.substring(2, 4)' + lineBreak)								
		sb.append('							}' + lineBreak)
		sb.append('							break' + lineBreak)
		sb.append('						case 101: // YYMMDD' + lineBreak)
		sb.append('							if (dateLength != 6) {' + lineBreak)
		sb.append('								unexpectedLength = true' + lineBreak)
		sb.append('							} else if (!modeIsTime) {' + lineBreak)
		sb.append('								date = "20" + dateValueStr' + lineBreak)
		sb.append('							}' + lineBreak)
		sb.append('							break' + lineBreak)
		sb.append('						case 102: // CCYYMMDD' + lineBreak)
		sb.append('							if (dateLength != 8) {' + lineBreak)
		sb.append('								unexpectedLength = true' + lineBreak)
		sb.append('							} else if (!modeIsTime) {' + lineBreak)
		sb.append('								date = dateValueStr' + lineBreak)
		sb.append('							}' + lineBreak)
		sb.append('							break' + lineBreak)
		sb.append('						case 201: // YYMMDDHHMM' + lineBreak)
		sb.append('							if (dateLength != 10) {' + lineBreak)
		sb.append('								unexpectedLength = true' + lineBreak)
		sb.append('							} else if (!modeIsTime) {' + lineBreak)
		sb.append('								date = "20" + dateValueStr.substring(0, 6)' + lineBreak)
		sb.append('							} else if (modeIsTime) {' + lineBreak)
		sb.append('								date = dateValueStr.substring(6, 10)' + lineBreak)
		sb.append('							}' + lineBreak)
		sb.append('							break' + lineBreak)
		sb.append('						case 202: // YYMMDDHHMMSS' + lineBreak)
		sb.append('							if (dateLength != 12) {' + lineBreak)
		sb.append('								unexpectedLength = true' + lineBreak)
		sb.append('							} else if (!modeIsTime) {' + lineBreak)
		sb.append('								date = "20" + dateValueStr.substring(0, 6)' + lineBreak)
		sb.append('							} else if (modeIsTime) {' + lineBreak)
		sb.append('								date = dateValueStr.substring(6, 12)' + lineBreak)
		sb.append('							}' + lineBreak)
		sb.append('							break' + lineBreak)
		sb.append('						case 203: // CCYYMMDDHHMM' + lineBreak)
		sb.append('							if (dateLength != 12) {' + lineBreak)
		sb.append('								unexpectedLength = true' + lineBreak)
		sb.append('							} else if (!modeIsTime) {' + lineBreak)
		sb.append('								date = dateValueStr.substring(0, 8)' + lineBreak)
		sb.append('							} else if (modeIsTime) {' + lineBreak)
		sb.append('								date = dateValueStr.substring(8, 12)' + lineBreak)
		sb.append('							}' + lineBreak)
		sb.append('							break' + lineBreak)
		sb.append('						case 204: // CCYYMMDDHHMMSS' + lineBreak)
		sb.append('							if (dateLength != 14) {' + lineBreak)
		sb.append('								unexpectedLength = true' + lineBreak)
		sb.append('							} else if (!modeIsTime) {' + lineBreak)
		sb.append('								date = dateValueStr.substring(0, 8)' + lineBreak)
		sb.append('							} else if (modeIsTime) {' + lineBreak)
		sb.append('								date = dateValueStr.substring(8, 14)' + lineBreak)
		sb.append('							}' + lineBreak)
		sb.append('							break' + lineBreak)
		sb.append('						case 401: // HHMM' + lineBreak)
		sb.append('							if (dateLength != 4) {' + lineBreak)
		sb.append('								unexpectedLength = true' + lineBreak)
		sb.append('							} else if (modeIsTime) {' + lineBreak)
		sb.append('								date = dateValueStr' + lineBreak)
		sb.append('							}' + lineBreak)
		sb.append('							break' + lineBreak)
		sb.append('						case 402: // HHMMSS' + lineBreak)
		sb.append('							if (dateLength != 6) {' + lineBreak)
		sb.append('								unexpectedLength = true' + lineBreak)
		sb.append('							} else if (modeIsTime) {' + lineBreak)
		sb.append('								date = dateValueStr' + lineBreak)
		sb.append('							}' + lineBreak)
		sb.append('							break' + lineBreak)
		sb.append('						case 718: // CCYYMMDD-CCYYMMDD with and without hyphen' + lineBreak)
		sb.append('							if (dateLength == 17) {' + lineBreak)
		sb.append('								dateValueStr = dateValueStr.replaceAll("-", "")' + lineBreak)
		sb.append('								dateLength = dateValueStr.length()' + lineBreak)
		sb.append('							}' + lineBreak)
		sb.append('							if (dateLength != 16) {' + lineBreak)
		sb.append('								unexpectedLength = true' + lineBreak)
		sb.append('							} else if (modeIsDateTo) {' + lineBreak)
		sb.append('								date = dateValueStr.substring(8)' + lineBreak)
		sb.append('							} else if (!modeIsTime) {' + lineBreak)
		sb.append('								date = dateValueStr.substring(0, 8)' + lineBreak)
		sb.append('							}' + lineBreak)
		sb.append('							break' + lineBreak)
		sb.append('						default:' + lineBreak)
		sb.append('							throw new Exception("Custom Function getDate: Unexpected date time EDIFACT qualifier \'" + dateFormat + "\'.")' + lineBreak)
		sb.append('					}' + lineBreak)
		sb.append('					if (unexpectedLength) {' + lineBreak)
		sb.append('						throw new Exception("Custom Function getDate: Length of date string \'" + date + "\' does not fit date format \'" + dateFormat + "\'.")' + lineBreak)
		sb.append('					}' + lineBreak)
		sb.append('					// Format and set date' + lineBreak)
		sb.append('					if (date == null) {' + lineBreak)
		sb.append("						date = ''" + lineBreak)
		sb.append('					} else if (!modeIsTime) {' + lineBreak)
		sb.append('						DateFormat formatter = new SimpleDateFormat("yyyyMMdd", locale)' + lineBreak)
		sb.append('						formatter.setLenient(false)' + lineBreak)
		sb.append('						try {' + lineBreak)
		sb.append('							formatter.parse(date)' + lineBreak)
		sb.append('						} catch (Exception ex) {' + lineBreak)
		sb.append('							throw new Exception("Custom Function getDate: can not parse date string \'" + date + "\' of format \'" + dateFormat + "\'.")' + lineBreak)
		sb.append('						}' + lineBreak)						
		sb.append('					}' + lineBreak)
		sb.append('					result = date' + lineBreak)
		sb.append('				} catch (RuntimeException ex) {' + lineBreak)
		sb.append('					throw new Exception("Custom Function getDate: dateValue \'" + dateValue + "\', dateFormat \'" + dateFormat + "\' causes \'" + ex + "\'.")' + lineBreak)
		sb.append('				}' + lineBreak)
		sb.append('			}' + lineBreak)
		sb.append('		} catch (Exception ex) {' + lineBreak)
		sb.append('			throw new Exception("Custom Function getDate: " + ex, ex)' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodGetDateAfterDays
* @param sb This is the string builder.
* @param methodGetExchangeProperty This is activation for methodGetDateAfterDays.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodGetDateAfterDays(def sb, boolean methodGetDateAfterDays, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodGetDateAfterDays == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* getDateAfterDays' + lineBreak)
		sb.append('* @param timeZone This is the time zone.' + lineBreak)
		sb.append('* @param date This is the date.' + lineBreak)
		sb.append('* @param dateInFormat This is the date in format.' + lineBreak)
		sb.append('* @param dateOutFormat This is the date out format.' + lineBreak)
		sb.append('* @return result Returns a formated date.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private String getDateAfterDays(def timeZone, String date, String dateInFormat, int days, String dateOutFormat) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	def dateIn' + lineBreak)
		sb.append('	def dateOut' + lineBreak)
		sb.append('	if (timeZone && date && dateInFormat && dateOutFormat) {' + lineBreak)
		sb.append('		try {' + lineBreak)
		sb.append('			// Initialize date in' + lineBreak)
		sb.append('			SimpleDateFormat sdfIn = new SimpleDateFormat(dateInFormat)' + lineBreak)
		sb.append('			sdfIn.setTimeZone(timeZone)' + lineBreak)
		sb.append('			dateIn = sdfIn.parse(date)' + lineBreak)
		sb.append('		} catch (Exception ex) {' + lineBreak)
		sb.append('			throw new Exception("getDateAfterDays: Cannot parse date in \'$date\' to date format \'$dateInFormat\'.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Add x days' + lineBreak)
		sb.append('		dateIn = dateIn.plus(days)' + lineBreak)
		sb.append('		try {' + lineBreak)
		sb.append('			// Set date out' + lineBreak)
		sb.append('			SimpleDateFormat sdfOut = new SimpleDateFormat(dateOutFormat)' + lineBreak)
		sb.append('			sdfOut.setTimeZone(timeZone)' + lineBreak)
		sb.append('			// Format date' + lineBreak)
		sb.append('			dateOut = sdfOut.format(dateIn)' + lineBreak)
		sb.append('			result = dateOut.toString()' + lineBreak)
		sb.append('		} catch (Exception ex) {' + lineBreak)
		sb.append('			throw new Exception("getDateAfterDays: Cannot parse date out \'$dateIn\' to date format \'$dateOutFormat\'.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append(' 	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodGetExchangeProperty
* @param sb This is the string builder.
* @param methodGetExchangeProperty This is activation for methodGetExchangeProperty.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodGetExchangeProperty(def sb, boolean methodGetExchangeProperty, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodGetExchangeProperty == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* getExchangeProperty' + lineBreak)
		sb.append('* @param message This is message.' + lineBreak)
		sb.append('* @param propertyName This is name of property.' + lineBreak)
		sb.append('* @param mandatory This is parameter if property is mandatory.' + lineBreak)
		sb.append('* @return propertyValue Return property value.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def getExchangeProperty(Message message, String propertyName, boolean mandatory) {' + lineBreak)
		sb.append('	String propertyValue = message.properties.get(propertyName) as String' + lineBreak)
		sb.append('	if (mandatory) {' + lineBreak)
		sb.append('		if (!propertyValue) {' + lineBreak)
		sb.append('			throw Exception("Mandatory exchange property \'$propertyName\' is missing.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return propertyValue' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodGetDateFormat
* @param sb This is the string builder.
* @param methodGetDateFormat This is activation for methodGetDateFormat.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodGetDateFormat(def sb, boolean methodGetDateFormat, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodGetDateFormat == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Gets an EDIFACT-F2379-conform qualifier descriping the dates format.' + lineBreak)
		sb.append('* @param date Date' + lineBreak)
		sb.append('* @return result Return an EDIFACT-F2379-conform qualifier.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String getDateFormat(String date) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	String dateString = date != null ? date.trim() : null' + lineBreak)
		sb.append('	if (date != null && dateString.length() > 0) {' + lineBreak)
		sb.append('		int dateLength = date.length()' + lineBreak)
		sb.append('		switch (dateLength) {' + lineBreak)
		sb.append('			case 6:' + lineBreak)
		sb.append("				result = '101'" + lineBreak)
		sb.append('				break' + lineBreak)
		sb.append('			case 8:' + lineBreak)
		sb.append("				result = '102'" + lineBreak)
		sb.append('				break' + lineBreak)
		sb.append('			case 10:' + lineBreak)
		sb.append("				result = '201'" + lineBreak)
		sb.append('				break' + lineBreak)
		sb.append('			case 12:' + lineBreak)
		sb.append('				String yearBegin = date.substring(0, 2)' + lineBreak)
		sb.append("				if ('20'.equalsIgnoreCase(yearBegin) || '19'.equalsIgnoreCase(yearBegin)) {" + lineBreak)
		sb.append("					result = '203'" + lineBreak)
		sb.append('				} else {' + lineBreak)
		sb.append("					result = '202'" + lineBreak)
		sb.append('				}' + lineBreak)
		sb.append('				break' + lineBreak)
		sb.append('			case 14:' + lineBreak)
		sb.append("				result = '204'" + lineBreak)
		sb.append('				break' + lineBreak)
		sb.append('			default:' + lineBreak)
		sb.append('				throw new Exception("getDateFormat: The date length \'$dateLength\' cannot be processed.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodGetHeader
* @param sb This is the string builder.
* @param methodGetHeader This is activation for methodGetExchangeProperty.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodGetHeader(def sb, boolean methodGetHeader, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodGetHeader == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* getHeader' + lineBreak)
		sb.append('* @param message This is message.' + lineBreak)
		sb.append('* @param headerName This is name of header.' + lineBreak)
		sb.append('* @param mandatory This is parameter if header is mandatory.' + lineBreak)
		sb.append('* @return headerValue Return header value.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private getHeader(Message message, String headerName, boolean mandatory) {' + lineBreak)
		sb.append('	String headerValue = message.getHeaders().get(headerName) as String' + lineBreak)
		sb.append('	if (mandatory) {' + lineBreak)
		sb.append('		if (!headerValue) {' + lineBreak)
		sb.append('			throw Exception("Mandatory header \'$headerName\' is missing.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return headerValue' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodGetMondayOfWeek
* @param sb This is the string builder.
* @param methodGetMondayOfWeek This is activation for methodGetMondayOfWeek.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodGetMondayOfWeek(def sb, boolean methodGetMondayOfWeek, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodGetMondayOfWeek == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Gets monday of week. First argument is year. Second argument is week. Default locale is used.' + lineBreak)
		sb.append('* @param locale This is locale.' + lineBreak)
		sb.append('* @param year This is the year.' + lineBreak)
		sb.append('* @param week This is the Week.' + lineBreak)
		sb.append('* @return date Returns date of monday of week.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String getMondayOfWeek(Locale locale, String year, String week) {' + lineBreak)
		sb.append('	final String DATE_IN_FORMAT = "yyyyww"' + lineBreak)
		sb.append('	final String DATE_OUT_FORMAT = "yyyyMMdd"' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	Calendar calendar = null' + lineBreak)
		sb.append('	Date date = null' + lineBreak)
		sb.append("	String weekOfYear = ''" + lineBreak)
		sb.append("	String yearStr = ''" + lineBreak)
		sb.append("	String weekStr = ''" + lineBreak)
		sb.append('	if (locale && year && week) {' + lineBreak)
		sb.append('		// Check year and create 4 digits' + lineBreak)
		sb.append('		try {' + lineBreak)
		sb.append('			int yearInt = Integer.parseInt(year)' + lineBreak)
		sb.append('			yearInt = yearInt.abs()' + lineBreak)
		sb.append('			if (yearInt < 100) {' + lineBreak)
		sb.append('				yearInt = 2000 + yearInt' + lineBreak)
		sb.append('			}' + lineBreak)
		sb.append('			yearStr = yearInt.toString()' + lineBreak)
		sb.append('		} catch (Exception ex) {' + lineBreak)
		sb.append('			throw new Exception("getMondayOfWeek: cannot parse year \'$year\' to integer.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Check week' + lineBreak)
		sb.append('		try {		' + lineBreak)
		sb.append('			int weekInt = Integer.parseInt(week)' + lineBreak)
		sb.append('			weekStr = weekInt.abs().toString()' + lineBreak)
		sb.append('		} catch (Exception ex) {' + lineBreak)
		sb.append('			throw new Exception("getMondayOfWeek: cannot parse week \'$week\' to integer.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		try {' + lineBreak)
		sb.append('			// Set week' + lineBreak)
		sb.append('			DateFormat formatterWeek = new SimpleDateFormat(DATE_IN_FORMAT, locale)' + lineBreak)
		sb.append('			weekOfYear = yearStr + weekStr' + lineBreak)
		sb.append('			date = formatterWeek.parse(weekOfYear)' + lineBreak)
		sb.append('			// Set monday of calendar week' + lineBreak)
		sb.append('			calendar = Calendar.getInstance(locale)' + lineBreak)
		sb.append('			calendar.setTimeInMillis(date.getTime())' + lineBreak)
		sb.append('			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)' + lineBreak)
		sb.append('		} catch (Exception ex) {' + lineBreak)
		sb.append('			throw new Exception("getMondayOfWeek: Cannot parse date \'$weekOfYear\' to date format \'$DATE_IN_FORMAT\'.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		try {' + lineBreak)
		sb.append('			// Get formatted day' + lineBreak)
		sb.append('			DateFormat formatterDate = new SimpleDateFormat(DATE_OUT_FORMAT, locale)' + lineBreak)
		sb.append('			formatterDate.setLenient(false)' + lineBreak)
		sb.append('			result = formatterDate.format(calendar.getTime())' + lineBreak)
		sb.append('		} catch (Exception ex) {' + lineBreak)
		sb.append('			throw new Exception("getMondayOfWeek: Cannot parse date \'$weekOfYear\' to date format \'$DATE_OUT_FORMAT\'.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodGetOnlyNumbers
* @param sb This is the string builder.
* @param methodGetOnlyNumbers This is activation for methodGetOnlyNumbers.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodGetOnlyNumbers(def sb, boolean methodGetOnlyNumbers, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodGetOnlyNumbers == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Get only numbers from string.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return result Returns a value with only numbers.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String getOnlyNumbers(String value) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	if(value) {' + lineBreak)
		sb.append('		// Use RegEx for remove all spaces' + lineBreak)
		sb.append("		result = value.replaceAll('[^0-9]', '')" + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodGetValueByCondition
* @param sb This is the string builder.
* @param methodGetValueByCondition This is activation for methodGetValueByCondition.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodGetValueByCondition(def sb, boolean methodGetValueByCondition, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodGetValueByCondition == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Gets a single value from value node when the corresponding value from condition node equals such a value.' + lineBreak)
		sb.append('* @param conditionPath This is the conditionPath object.' + lineBreak)
		sb.append('* @param nodeNameConditionValue This is the node name of condition value.' + lineBreak)
		sb.append('* @param suchValue This is the such value.' + lineBreak)
		sb.append('* @param nodeNameValue This is the node name of value.' + lineBreak)
		sb.append('* @return result Returns a value from the third argument when the corresponding value from first argument has one of the value contained.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String getValueByCondition(def conditionPath, String nodeNameConditionValue, String suchValue, String nodeNameValue) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	// Check suchValue' + lineBreak)
		sb.append('	if (!suchValue) {' + lineBreak)
		sb.append('		throw new Exception("getValueByCondition: There is no suchValue.")' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	if (conditionPath && nodeNameConditionValue && nodeNameValue) {' + lineBreak)
		sb.append('		result = conditionPath.find{it."$nodeNameConditionValue".toString().equals(suchValue)}."$nodeNameValue".toString()' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodHeadString
* @param sb This is the string builder.
* @param methodHeadString This is activation for methodHeadString.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodHeadString(def sb, boolean methodHeadString, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodHeadString == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Removes the trailing characters of the first argument leaving the number of characters given by the second argument.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @param headLength Head length' + lineBreak)
		sb.append('* @return result Returns head string of input value.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String headString(String value, int headLength){' + lineBreak)
		sb.append('	String result = null' + lineBreak)
		sb.append('	if (value != null && headLength > 0) {' + lineBreak)
		sb.append('		if (value.length() > headLength) {' + lineBreak)
		sb.append('			result = value.substring(0, headLength)' + lineBreak)
		sb.append('		} else {' + lineBreak)
		sb.append('			result = value' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	} else {' + lineBreak)
		sb.append('		result = value' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodIsNumber
* @param sb This is the string builder.
* @param methodIsNumber This is activation for methodIsNumber.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodIsNumber(def sb, boolean methodIsNumber, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodIsNumber == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Check if value is a number.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append("* @return result Return 'true' if the argument is a number, 'false' otherwise." + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def boolean isNumber(String value) {' + lineBreak)
		sb.append('	try {' + lineBreak)
		sb.append('		Double.parseDouble(value)' + lineBreak)
		sb.append('		return true' + lineBreak)
		sb.append('	} catch (Exception ex) {' + lineBreak)
		sb.append('		return false' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodMapWithDefault
* @param sb This is the string builder.
* @param methodMapWithDefault This is activation for methodMapWithDefault.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodMapWithDefault(def sb, boolean methodMapWithDefault, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodMapWithDefault == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Map a default value if node is empty.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return result Returns a value.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String mapWithDefault(String value, String defaultValue) {' + lineBreak)
		sb.append('	String result = value' + lineBreak)
		sb.append('	if (!value) {' + lineBreak)
		sb.append('		result = defaultValue' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodMinusFromBeginToEnd
* @param sb This is the string builder.
* @param methodMinusFromBeginToEnd This is activation for methodMinusFromBeginToEnd.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodMinusFromBeginToEnd(def sb, boolean methodMinusFromBeginToEnd, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodMinusFromBeginToEnd == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Sets the minus sign from the beginning to the end.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return result Returns a value with the minus at the end.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String minusFromBeginToEnd(String value) {' + lineBreak)
		sb.append('	String result = value' + lineBreak)
		sb.append('	if (value != null && value.startsWith("-")) {' + lineBreak)
		sb.append('		result = value.substring(1, value.length()) + "-"' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodMinusFromEndToBegin
* @param sb This is the string builder.
* @param methodMinusFromEndToBegin This is activation for methodMinusFromEndToBegin.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodMinusFromEndToBegin(def sb, boolean methodMinusFromEndToBegin, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodMinusFromEndToBegin == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Sets the minus sign from the end to the beginning.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return result Returns a value with the minus at the beginning.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String minusFromEndToBegin(String value) {' + lineBreak)
		sb.append('	String result = value' + lineBreak)
		sb.append('	if (value != null && value.endsWith("-")) {' + lineBreak)
		sb.append('		result = "-" + value.substring(0, value.length() - 1)' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodRemoveAlgebraicSign
* @param sb This is the string builder.
* @param methodRemoveAlgebraicSign This is activation for methodRemoveAlgebraicSign.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodRemoveAlgebraicSign(def sb, boolean methodRemoveAlgebraicSign, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodRemoveAlgebraicSign == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Removes algebraic sign plus and minus from value.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return string without algebraic sign plus and minus.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String removeAlgebraicSign(String value) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	// Check value' + lineBreak)
		sb.append('	if(value) {' + lineBreak)
		sb.append("		if (value.startsWith('-')) {" + lineBreak)
		sb.append('			result = value.substring(1, value.length())' + lineBreak)
		sb.append("		} else if (value.startsWith('+')) {" + lineBreak)
		sb.append('			result = value.substring(1, value.length())' + lineBreak)
		sb.append("		} else if (value.endsWith('-')) {" + lineBreak)
		sb.append('			result = value.substring(0, value.length() - 1)' + lineBreak)
		sb.append('		} else {' + lineBreak)
		sb.append('			result = value' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodRemoveAlgebraicSignPlus
* @param sb This is the string builder.
* @param methodRemoveAlgebraicSignPlus This is activation for methodRemoveAlgebraicSignPlus.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodRemoveAlgebraicSignPlus(def sb, boolean methodRemoveAlgebraicSignPlus, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodRemoveAlgebraicSignPlus == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Removes only algebraic sign plus from value.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return string without algebraic sign plus.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String removeAlgebraicSignPlus(String value) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	// Check value' + lineBreak)
		sb.append('	if(value.length() == 0){' + lineBreak)
		sb.append("		result = ''" + lineBreak)
		sb.append("	} else if (value.startsWith('+')) {" + lineBreak)
		sb.append('		result = value.substring(1, value.length())' + lineBreak)
		sb.append('	} else {' + lineBreak)
		sb.append('		result = value' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodRemoveAllCharacters
* @param sb This is the string builder.
* @param methodRemoveAllCharacters This is activation for methodRemoveAllCharacters.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodRemoveAllCharacters(def sb, boolean methodRemoveAllCharacters, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodRemoveAllCharacters == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Remove all characters from string.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return result Returns a string without characters.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private String removeAllCharacters(String value) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	if (value) {' + lineBreak)
		sb.append('		// Use RegEx for remove all characters' + lineBreak)
		sb.append('		result = value.replaceAll("[a-zA-ZäüöÄÜÖß]","")' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodRemoveAllSpaces
* @param sb This is the string builder.
* @param methodRemoveAllSpaces This is activation for methodRemoveAllSpaces.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodRemoveAllSpaces(def sb, boolean methodRemoveAllSpaces, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodRemoveAllSpaces == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Remove all spaces from string.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return result Returns a value without spaces.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String removeAllSpaces(String value) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	if(value) {' + lineBreak)
		sb.append('		// Use RegEx for remove all spaces' + lineBreak)
		sb.append('		result = value.replaceAll(" ","")' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodRemoveAllSpecialCharacters
* @param sb This is the string builder.
* @param methodRemoveAllSpecialCharacters This is activation for methodRemoveAllSpecialCharacters.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodRemoveAllSpecialCharacters(def sb, boolean methodRemoveAllSpecialCharacters, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodRemoveAllSpecialCharacters == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Remove all special characters from string.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return result Returns a value without special characters.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String removeAllSpecialCharacters(String value) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	if (value) {' + lineBreak)
		sb.append('		// Use RegEx for remove all special characters' + lineBreak)
		sb.append('		result = value.replaceAll("[^a-zA-Z0-9/.,: _+-]+","")' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodRemoveCarriageReturnLineFeed
* @param sb This is the string builder.
* @param methodRemoveCarriageReturnLineFeed This is activation for methodRemoveCarriageReturnLineFeed.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodRemoveCarriageReturnLineFeed(def sb, boolean methodRemoveCarriageReturnLineFeed, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodRemoveCarriageReturnLineFeed == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Remove carriage return line feed.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return result Returns a value without carriage return line feed.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String removeCarriageReturnLineFeed(String value) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	if(value) {' + lineBreak)
		sb.append('		// Use RegEx for remove carriage return line feed' + lineBreak)
		sb.append('		result = value.replaceAll("[\\r\\n]","")' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodRemoveDecimalIfZero
* @param sb This is the string builder.
* @param methodRemoveDecimalIfZero This is activation for methodRemoveDecimalIfZero.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodRemoveDecimalIfZero(def sb, boolean methodRemoveDecimalIfZero, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodRemoveDecimalIfZero == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Removes decimal if zero. The dot is used as decimal separator.' + lineBreak)
		sb.append('* @param number Number' + lineBreak)
		sb.append('* @return result Returns a value without decimal if zero.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String removeDecimalIfZero(String number) {' + lineBreak)
		sb.append('	String firstPart = ""' + lineBreak)
		sb.append('	String secondPart = ""' + lineBreak)
		sb.append('	int indexOfDelimiter = 0' + lineBreak)
		sb.append('	String result = ""' + lineBreak)
		sb.append('	if (number) {' + lineBreak)
		sb.append('		indexOfDelimiter = number.indexOf(".")' + lineBreak)
		sb.append('		if (indexOfDelimiter == -1) {' + lineBreak)
		sb.append('			result = number' + lineBreak)
		sb.append('		} else {' + lineBreak)
		sb.append('			firstPart = number.substring(0, indexOfDelimiter)' + lineBreak)
		sb.append('			secondPart = number.substring(indexOfDelimiter + 1)' + lineBreak)
		sb.append('			if (new Integer(secondPart).intValue() == 0) {' + lineBreak)
		sb.append('				result = firstPart' + lineBreak)
		sb.append('			} else {' + lineBreak)
		sb.append('				result = number' + lineBreak)
		sb.append('			}' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodReplaceSpecialCharacters
* @param sb This is the string builder.
* @param methodReplaceSpecialCharacters This is activation for methodReplaceSpecialCharacters.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodReplaceSpecialCharacters(def sb, boolean methodReplaceSpecialCharacters, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodReplaceSpecialCharacters == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Replaces all special characters in string.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return result Returns string without special characters.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String replaceSpecialCharacters(String value) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	if (value) {' + lineBreak)
		sb.append('		result = value' + lineBreak)
		sb.append('		// Replacements - Using Unicode-Escapes' + lineBreak)
		sb.append('		Map map = new HashMap()' + lineBreak)
		sb.append('		map.put("\u00C0", "A")	// À' + lineBreak)
		sb.append('		map.put("\u00C1", "A")	// Á' + lineBreak)
		sb.append('		map.put("\u00C2", "A")	// Â' + lineBreak)
		sb.append('		map.put("\u00C3", "A")	// Ã' + lineBreak)
		sb.append('		map.put("\u00C4", "Ae")	// Ä' + lineBreak)
		sb.append('		map.put("\u00C5", "A")	// Å' + lineBreak)
		sb.append('		map.put("\u00C6", "A")	// Æ' + lineBreak)
		sb.append('		map.put("\u00C7", "C")	// Ç' + lineBreak)
		sb.append('		map.put("\u00D0", "D")	// Ð' + lineBreak)
		sb.append('		map.put("\u00C8", "E")	// È' + lineBreak)
		sb.append('		map.put("\u00C9", "E")	// É' + lineBreak)
		sb.append('		map.put("\u00CA", "E")	// Ê' + lineBreak)
		sb.append('		map.put("\u00CB", "E")	// Ë' + lineBreak)
		sb.append('		map.put("\u0191", "F")	// Ƒ' + lineBreak)
		sb.append('		map.put("\u00CC", "I")	// Ì' + lineBreak)
		sb.append('		map.put("\u00CD", "I")	// Í' + lineBreak)
		sb.append('		map.put("\u00CE", "I")	// Î' + lineBreak)
		sb.append('		map.put("\u00CF", "I")	// Ï' + lineBreak)
		sb.append('		map.put("\u00D1", "N")	// Ñ' + lineBreak)
		sb.append('		map.put("\u00D2", "O")	// Ò' + lineBreak)
		sb.append('		map.put("\u00D3", "O")	// Ó' + lineBreak)
		sb.append('		map.put("\u00D4", "O")	// Ô' + lineBreak)
		sb.append('		map.put("\u00D5", "O")	// Õ' + lineBreak)
		sb.append('		map.put("\u00D6", "Oe")	// Ö' + lineBreak)
		sb.append('		map.put("\u00D8", "O")	// Ø' + lineBreak)
		sb.append('		map.put("\u0152", "OE")	// Œ' + lineBreak)
		sb.append('		map.put("\u0160", "S")	// Š' + lineBreak)
		sb.append('		map.put("\u015E", "S")	// Ş' + lineBreak)
		sb.append('		map.put("\u00D9", "U")	// Ù' + lineBreak)
		sb.append('		map.put("\u00DA", "U")	// Ú' + lineBreak)
		sb.append('		map.put("\u00DB", "U")	// Û' + lineBreak)
		sb.append('		map.put("\u00DC", "Ue")	// Ü' + lineBreak)
		sb.append('		map.put("\u0178", "Y")	// Ÿ' + lineBreak)
		sb.append('		map.put("\u00DD", "Y")	// Ý' + lineBreak)
		sb.append('		map.put("\u017D", "Z")	// Ž' + lineBreak)
		sb.append('		map.put("\u00E0", "a")	// à' + lineBreak)
		sb.append('		map.put("\u00E1", "a")	// á' + lineBreak)
		sb.append('		map.put("\u00E2", "a")	// â' + lineBreak)
		sb.append('		map.put("\u00E3", "a")	// ã' + lineBreak)
		sb.append('		map.put("\u00e4", "ae")	// ä' + lineBreak)
		sb.append('		map.put("\u00E5", "a")	// å' + lineBreak)
		sb.append('		map.put("\u00E6", "ae")	// æ' + lineBreak)
		sb.append('		map.put("\u00E7", "c")	// ç' + lineBreak)
		sb.append('		map.put("\u00F0", "d")	// ð' + lineBreak)
		sb.append('		map.put("\u00E8", "e")	// è' + lineBreak)
		sb.append('		map.put("\u00E9", "e")	// é' + lineBreak)
		sb.append('		map.put("\u00EA", "e")	// ê' + lineBreak)
		sb.append('		map.put("\u00EB", "e")	// ë' + lineBreak)
		sb.append('		map.put("\u0192", "f")	// ƒ' + lineBreak)
		sb.append('		map.put("\u00EC", "i")	// ì' + lineBreak)
		sb.append('		map.put("\u00ED", "i")	// í' + lineBreak)
		sb.append('		map.put("\u00EE", "i")	// î' + lineBreak)
		sb.append('		map.put("\u00EF", "i")	// ï' + lineBreak)
		sb.append('		map.put("\u00F1", "n")	// ñ' + lineBreak)
		sb.append('		map.put("\u00F2", "o")	// ò' + lineBreak)
		sb.append('		map.put("\u00F3", "o")	// ó' + lineBreak)
		sb.append('		map.put("\u00F4", "o")	// ô' + lineBreak)
		sb.append('		map.put("\u00F5", "o")	// õ' + lineBreak)
		sb.append('		map.put("\u00F6", "oe")	// ö' + lineBreak)
		sb.append('		map.put("\u00F8", "o")	// ø' + lineBreak)
		sb.append('		map.put("\u0153", "oe")	// œ' + lineBreak)
		sb.append('		map.put("\u0161", "s")	// š' + lineBreak)
		sb.append('		map.put("\u015F", "s")	// ş' + lineBreak)
		sb.append('		map.put("\u00F9", "u")	// ù' + lineBreak)
		sb.append('		map.put("\u00FA", "u")	// ú' + lineBreak)
		sb.append('		map.put("\u00FB", "u")	// û' + lineBreak)
		sb.append('		map.put("\u00FC", "ue")	// ü' + lineBreak)
		sb.append('		map.put("\u00FF", "y")	// ÿ' + lineBreak)
		sb.append('		map.put("\u00FD", "y")	// ý' + lineBreak)
		sb.append('		map.put("\u017E", "z")	// ž' + lineBreak)
		sb.append('		map.put("\u00DF", "ss")	// ß' + lineBreak)
		sb.append('		map.put("\u00AE", "(R)")	// ®' + lineBreak)
		sb.append('		map.put("\u00A9", "(C)")	// ©' + lineBreak)
		sb.append('		map.put("\u00B1", "+-")	// ' + lineBreak)
		sb.append('		map.put("\u00B2", "^2")	// ²' + lineBreak)
		sb.append('		map.put("\u00B3", "^3")	// ³' + lineBreak)
		sb.append('		map.put("\u00B4", "\'")	// ´' + lineBreak)
		sb.append('		map.put("\u0060", "\'")	// `' + lineBreak)
		sb.append('		map.put("\u00B5", "^10−6")// µ' + lineBreak)
		sb.append('		map.put("\u00B0","grade")	// °' + lineBreak)
		sb.append('		map.put("\u2122","TM")	// ™' + lineBreak)
		sb.append('		// Replace values in map with RegEx' + lineBreak)
		sb.append('		Set entrySet = map.entrySet()' + lineBreak)
		sb.append('		for (Iterator it = entrySet.iterator(); it.hasNext();) {' + lineBreak)
		sb.append('			Map.Entry mapEntry = (Map.Entry) it.next()' + lineBreak)
		sb.append('			result = result.replaceAll("[" + (String) mapEntry.getKey() + "]", (String) mapEntry.getValue())' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodReplaceUmlauts
* @param sb This is the string builder.
* @param methodReplaceUmlauts This is activation for methodReplaceUmlauts.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodReplaceUmlauts(def sb, boolean methodReplaceUmlauts, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodReplaceUmlauts == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Replaces all umlauts in string.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return result Returns a value without umlauts.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String replaceUmlauts(String value) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	if (value) {' + lineBreak)
		sb.append('		result = value' + lineBreak)
		sb.append('		// Replacements - Using Unicode-Escapes' + lineBreak)
		sb.append('		Map map = new HashMap()' + lineBreak)
		sb.append("		map.put('\u00e4', 'ae') // ä" + lineBreak)
		sb.append("		map.put('\u00f6', 'oe') // ö" + lineBreak)
		sb.append("		map.put('\u00fc', 'ue') // ü" + lineBreak)
		sb.append("		map.put('\u00df', 'ss') // ß" + lineBreak)
		sb.append("		map.put('\u00c4', 'Ae') // Ä" + lineBreak)
		sb.append("		map.put('\u00d6', 'Oe') // Ö" + lineBreak)
		sb.append("		map.put('\u00dc', 'Ue') // Ü" + lineBreak)
		sb.append('		// Replace values in map with RegEx' + lineBreak)
		sb.append('		Set entrySet = map.entrySet()' + lineBreak)
		sb.append('		for (Iterator it = entrySet.iterator(); it.hasNext();) {' + lineBreak)
		sb.append('			Map.Entry mapEntry = (Map.Entry) it.next()' + lineBreak)
		sb.append('			result = result.replaceAll("[" + (String) mapEntry.getKey() + "]", (String) mapEntry.getValue())' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodSegmentHasOneOfSuchValues
* @param sb This is the string builder.
* @param methodSegmentHasOneOfSuchValues This is activation for methodSegmentHasOneOfSuchValues.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodSegmentHasOneOfSuchValues(def sb, boolean methodSegmentHasOneOfSuchValues, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodSegmentHasOneOfSuchValues == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)

		sb.append('/**' + lineBreak)
		sb.append('* Gets true if value from value node is corresponding to segment value, otherwise false.' + lineBreak)
		sb.append('* @param conditionPath This is the conditionPath object.' + lineBreak)
		sb.append('* @param nodeNameConditionValue This is the node name of condition value.' + lineBreak)
		sb.append('* @param suchValue This is the such value.' + lineBreak)
		sb.append('* @return result Returns true if value from value node is corresponding to segment value, otherwise false.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def boolean segmentHasOneOfSuchValues(def conditionPath, String nodeNameConditionValue, def suchValues) {' + lineBreak)
		sb.append('	String[] theSuchValues' + lineBreak)
		sb.append('	String value = null' + lineBreak)
		sb.append('	boolean result = false' + lineBreak)
		sb.append('	// Get suchValues' + lineBreak)
		sb.append('	if (suchValues instanceof List) {' + lineBreak)
		sb.append('		theSuchValues = suchValues' + lineBreak)
		sb.append('	} else {' + lineBreak)
		sb.append('		theSuchValues = [suchValues.toString()]' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	// Check suchValues' + lineBreak)
		sb.append('	if (!theSuchValues[0]) {' + lineBreak)
		sb.append('		throw new Exception("segmentHasOneOfSuchValues: There is no suchValue.")' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	// Search for value in segment' + lineBreak)
		sb.append('	if (conditionPath && nodeNameConditionValue) {' + lineBreak)
		sb.append('		for (int i = 0; i < theSuchValues.size(); i++) {' + lineBreak)
		sb.append('			value = conditionPath.find{it."$nodeNameConditionValue".toString().equals(theSuchValues[i])}."$nodeNameConditionValue".toString()' + lineBreak)
		sb.append('			if (value) {' + lineBreak)
		sb.append('				result = true' + lineBreak)
		sb.append('				break' + lineBreak)
		sb.append('			}' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}		

/**
* appendMethodSetCurrentDate
* @param sb This is the string builder.
* @param methodSetCurrentDate This is activation for methodSetCurrentDate.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodSetCurrentDate(def sb, boolean methodSetCurrentDate, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodSetCurrentDate == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Get current date' + lineBreak)
		sb.append('* @param timeZone This is the time zone.' + lineBreak)
		sb.append('* @param dateFormat This is the date format.' + lineBreak)
		sb.append('* @return result Returns current formated date.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String setCurrentDate(def timeZone, String dateFormat) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append("	def date" + lineBreak)
		sb.append("	def dateOut" + lineBreak)
		sb.append('	if (timeZone) {' + lineBreak)
		sb.append('		try {' + lineBreak)
		sb.append('			// Set simple date format' + lineBreak)
		sb.append('			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat)' + lineBreak)
		sb.append('			sdf.setTimeZone(timeZone)' + lineBreak)
		sb.append('			// Create new date' + lineBreak)
		sb.append('			date = new Date()' + lineBreak)
		sb.append('			// Format date' + lineBreak)
		sb.append('			dateOut = sdf.format(date)' + lineBreak)
		sb.append('			result = dateOut.toString()' + lineBreak)
		sb.append('		} catch (Exception ex) {' + lineBreak)
		sb.append('			throw new Exception("setCurrentDate: Cannot parse date \'$date\' to date format \'$dateFormat\'.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodSetDecimalSeparatorPoint
* @param sb This is the string builder.
* @param methodSetDecimalSeparatorPoint This is activation for methodSetDecimalSeparatorPoint.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodSetDecimalSeparatorPoint(def sb, boolean methodSetDecimalSeparatorPoint, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodSetDecimalSeparatorPoint == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Sets decimal separator to point and remove thousands separator.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return value with decimal separator point witout thousands separator.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String setDecimalSeparatorPoint(String value) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	if(value) {' + lineBreak)
		sb.append('		// Remove whitespaces' + lineBreak)
		sb.append("		result = value.replaceAll('\\\\s','')" + lineBreak)
		sb.append('		// Remove multiple comma thousands separators' + lineBreak)
		sb.append("		if(result.indexOf(',') > -1 && result.lastIndexOf(',') < result.lastIndexOf('.')) {" + lineBreak)
		sb.append("			result = result.replaceAll(',','')" + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Remove multiple point thousands separator only if comma is decimal separator' + lineBreak)
		sb.append("		if(result.indexOf('.') > -1 && result.lastIndexOf('.') < result.lastIndexOf(',')) {" + lineBreak)
		sb.append("			result = result.replaceAll('\\\\.','')" + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Replace comma to point' + lineBreak)
		sb.append("		result = result.replaceAll(',','.')" + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodSetDefaultAsCurrentDate
* @param sb This is the string builder.
* @param methodSetDefaultAsCurrentDate This is activation for methodSetDefaultAsCurrentDate.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodSetDefaultAsCurrentDate(def sb, boolean methodSetDefaultAsCurrentDate, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodSetDefaultAsCurrentDate == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Set default as current date' + lineBreak)
		sb.append('* @param timeZone This is the time zone.' + lineBreak)
		sb.append('* @param value This is the date value.' + lineBreak)
		sb.append('* @param dateFormat This is the date format.' + lineBreak)
		sb.append('* @return result Returns current formated date.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private String setDefaultAsCurrentDate(def timeZone, String value, String dateFormat) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	def date' + lineBreak)
		sb.append('	def dateNew' + lineBreak)
		sb.append('	if (timeZone) {' + lineBreak)
		sb.append('		if (value) {' + lineBreak)
		sb.append('			result = value' + lineBreak)
		sb.append('		} else {' + lineBreak)
		sb.append('			try {' + lineBreak)
		sb.append('				// Set simple date format' + lineBreak)
		sb.append('				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat)' + lineBreak)
		sb.append('				sdf.setTimeZone(timeZone)' + lineBreak)
		sb.append('				// Create new date' + lineBreak)
		sb.append('				date = new Date()' + lineBreak)
		sb.append('				// Format date' + lineBreak)
		sb.append('				dateNew = sdf.format(date)' + lineBreak)
		sb.append('				result = dateNew.toString()' + lineBreak)
		sb.append('			} catch (Exception ex) {' + lineBreak)
		sb.append('				throw new Exception("setDefaultAsCurrentDate: Cannot parse date \'$date\' to date format \'$dateFormat\'.")' + lineBreak)
		sb.append('			}' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodSetDirectory
* @param sb This is the string builder.
* @param methodSetDirectory This is activation for methodSetDirectory.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodSetDirectory(def sb, boolean methodSetDirectory, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodSetDirectory == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Compute and set directory.' + lineBreak)
		sb.append('* @param message This is the message.' + lineBreak)
		sb.append('* @param timeZone This is the time zone.' + lineBreak)
		sb.append('* @param rootFolder This is the root folder.' + lineBreak)
		sb.append("* @param addYear Add year ('true','false')." + lineBreak)
		sb.append("* @param addMonth Add month ('true','false')." + lineBreak)
		sb.append("* @param addDay Add day ('true','false')." + lineBreak)
		sb.append('* @param sender This is the sender.' + lineBreak)
		sb.append('* @param messageType This is the message type.' + lineBreak)
		sb.append('* @param messageVersion This is the message version.' + lineBreak)
		sb.append('* @param messageRelease This is the message release.' + lineBreak)
		sb.append('* @return result Returns a directory path.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String setDirectory(Message message, def timeZone, String rootFolder, boolean addYear, boolean addMonth, boolean addDay, String sender, messageType, String messageVersion, String messageRelease) {' + lineBreak)
		sb.append("	final String DIRECTORY_SEPARATOR = '/'" + lineBreak)
		sb.append("	final String ROOT_FOLDER = 'Archive'" + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	def date' + lineBreak)
		sb.append('	def dateNew' + lineBreak)
		sb.append("	def dateNewStr = ''" + lineBreak)
		sb.append('	// Set directory' + lineBreak)
		sb.append('	String directory = DIRECTORY_SEPARATOR + rootFolder' + lineBreak)
		sb.append('	if (!rootFolder) {' + lineBreak)
		sb.append('		directory = DIRECTORY_SEPARATOR + ROOT_FOLDER' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	if (timeZone && directory) {' + lineBreak)
		sb.append('		// Get current date' + lineBreak)
		sb.append('		try {' + lineBreak)
		sb.append('			// Set simple date format' + lineBreak)
		sb.append("			String dateFormat = 'yyyyMMdd'" + lineBreak)
		sb.append('			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat)' + lineBreak)
		sb.append('			sdf.setTimeZone(timeZone)' + lineBreak)
		sb.append('			// Create new date' + lineBreak)
		sb.append('			date = new Date()' + lineBreak)
		sb.append('			// Format date' + lineBreak)
		sb.append('			dateNew = sdf.format(date)' + lineBreak)
		sb.append('			dateNewStr = dateNew.toString()' + lineBreak)
		sb.append('		} catch (Exception ex) {' + lineBreak)
		sb.append('			throw new Exception("setDirectory: Cannot parse date \'$date\' to date format \'$dateFormat\'.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Add year' + lineBreak)
		sb.append('		if (addYear) {' + lineBreak)
		sb.append('			directory += DIRECTORY_SEPARATOR + dateNewStr.substring(0, 4)' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Add month' + lineBreak)
		sb.append('		if (addMonth) {' + lineBreak)
		sb.append('			directory += DIRECTORY_SEPARATOR + dateNewStr.substring(4, 6)' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Add day' + lineBreak)
		sb.append('		if (addDay) {' + lineBreak)
		sb.append('			directory += DIRECTORY_SEPARATOR + dateNewStr.substring(6, 8)' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Add sender' + lineBreak)
		sb.append('		if (sender) {' + lineBreak)
		sb.append('			directory += DIRECTORY_SEPARATOR + sender' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Add message type' + lineBreak)
		sb.append('		if (messageType) {' + lineBreak)
		sb.append('			directory += DIRECTORY_SEPARATOR + messageType' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Add message version' + lineBreak)
		sb.append('		if (messageVersion) {' + lineBreak)
		sb.append('			directory += DIRECTORY_SEPARATOR + messageVersion' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Add message release' + lineBreak)
		sb.append('		if (messageRelease) {' + lineBreak)
		sb.append('			directory += DIRECTORY_SEPARATOR + messageRelease' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Create valid directory with RegEx. Remove invalide character.' + lineBreak)
		sb.append("		// Supported characters for file name are letters, numbers, spaces, and '()_/-.'." + lineBreak)
		sb.append("		directory = directory.replaceAll('[^a-zA-Z0-9 ( ) _ / \\\\- .]', '')" + lineBreak)
		sb.append('		// Remove leading and tailing whitespace with RegEx' + lineBreak)
		sb.append("		directory = directory.replaceAll('^\\\\s+|\\\\s+\$','')" + lineBreak)
		sb.append('		// Set directory to header' + lineBreak)
		sb.append("		message.setHeader('CamelFilePath', directory)" + lineBreak)
		sb.append('		result = directory' + lineBreak)
		sb.append('	} else {' + lineBreak)
		sb.append('		throw new Exception("setDirectory: Directory can not be empty. Please check all input parameter.")' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodSetFileName
* @param sb This is the string builder.
* @param methodSetFileName This is activation for methodSetFileName.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodSetFileName(def sb, boolean methodSetFileName, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodSetFileName == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Compute and set file name.' + lineBreak)
		sb.append('* @param message This is the message.' + lineBreak)
		sb.append('* @param timeZone This is the time zone.' + lineBreak)
		sb.append('* @param messageType This is the message type.' + lineBreak)
		sb.append('* @param messageVersion This is the message version.' + lineBreak)
		sb.append('* @param messageRelease This is the message release.' + lineBreak)
		sb.append("* @param addTimeStamp Add time stamp ('true','false')." + lineBreak)
		sb.append('* @param dateFormat This is the date format.' + lineBreak)
		sb.append('* @param documentNumber This is the document number.' + lineBreak)
		sb.append("* @param addMessageId Add message ID ('true','false')." + lineBreak)
		sb.append("* @param addCorrelationId Add correlation ID ('true','false')." + lineBreak)
		sb.append('* @param nameSeparator This is the name separator.' + lineBreak)
		sb.append('* @param fileType This is the file type.' + lineBreak)
		sb.append('* @return result Returns a file name.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String setFileName(Message message, def timeZone, String messageType, String messageVersion, String messageRelease, boolean addTimeStamp, String dateFormat, String documentNumber, boolean addMessageId, boolean addCorrelationId, String nameSeparator, String fileType) {' + lineBreak)
		sb.append("	final String NAME_SEPARATOR = '_'" + lineBreak)
		sb.append("	final String DATE_FORMAT = 'yyyy-MM-dd_HH-mm-ss.SSS'" + lineBreak)
		sb.append('	String fileName = messageType' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append("	def date" + lineBreak)
		sb.append("	def dateNew" + lineBreak)
		sb.append("	String timeStamp = ''" + lineBreak)
		sb.append('	String dateFormatNew = dateFormat' + lineBreak)
		sb.append('	if (messageType && fileType && addMessageId != null && addCorrelationId != null && addTimeStamp != null) {' + lineBreak)
		sb.append('		// Set nameSeparator' + lineBreak)
		sb.append('		String nameSeparatorNew = nameSeparator' + lineBreak)
		sb.append('		if (!nameSeparatorNew) {' + lineBreak)
		sb.append('			nameSeparatorNew = NAME_SEPARATOR' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Add EDI message release' + lineBreak)
		sb.append('		if (messageVersion) {' + lineBreak)
		sb.append('			fileName += nameSeparatorNew + messageVersion' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Add EDI message release' + lineBreak)
		sb.append('		if (messageRelease) {' + lineBreak)
		sb.append('			fileName += nameSeparatorNew + messageRelease' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Add time stamp' + lineBreak)
		sb.append('		if (addTimeStamp) {' + lineBreak)
		sb.append('			try {' + lineBreak)
		sb.append('				if (!dateFormatNew) {' + lineBreak)
		sb.append('					dateFormatNew = DATE_FORMAT' + lineBreak)
		sb.append('				}' + lineBreak)
		sb.append('				// Set simple date format' + lineBreak)
		sb.append('				SimpleDateFormat sdf = new SimpleDateFormat(dateFormatNew)' + lineBreak)
		sb.append('				sdf.setTimeZone(timeZone)' + lineBreak)
		sb.append('				// Create new date' + lineBreak)
		sb.append('				date = new Date()' + lineBreak)
		sb.append('				// Format date' + lineBreak)
		sb.append('				dateNew = sdf.format(date)' + lineBreak)
		sb.append('				timeStamp = dateNew.toString()' + lineBreak)
		sb.append('			} catch (Exception ex) {' + lineBreak)
		sb.append('				throw new Exception("setFileName: Cannot parse date \'$date\' to date format \'$dateFormat\'.")' + lineBreak)
		sb.append('			}' + lineBreak)
		sb.append('			fileName += nameSeparatorNew + timeStamp' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Add document number' + lineBreak)
		sb.append('		if (documentNumber) {' + lineBreak)
		sb.append('			fileName += nameSeparatorNew + documentNumber' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Add message ID' + lineBreak)
		sb.append('		if (addMessageId) {' + lineBreak)
		sb.append('			String messageId = message.getHeader("SAP_MessageProcessingLogID", String)' + lineBreak)
		sb.append('			if (!messageId) {' + lineBreak)
		sb.append('				// Create an ID' + lineBreak)
		sb.append("				messageId = UUID.randomUUID().toString().replaceAll('-', '').toUpperCase()" + lineBreak)
		sb.append('			}' + lineBreak)
		sb.append('			fileName += nameSeparatorNew + messageId' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Add correlation ID' + lineBreak)
		sb.append('		if (addCorrelationId) {' + lineBreak)
		sb.append('			String correlationId = message.getHeader("SAP_MplCorrelationId", String)' + lineBreak)
		sb.append('			if (!correlationId) {' + lineBreak)
		sb.append('				// Create an ID' + lineBreak)
		sb.append("				correlationId = UUID.randomUUID().toString().replaceAll('-', '').toUpperCase()" + lineBreak)
		sb.append('			}' + lineBreak)
		sb.append('			fileName += nameSeparatorNew + correlationId' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Add file type' + lineBreak)
		sb.append("		if (!fileType.startsWith('.')) {" + lineBreak)
		sb.append("			fileName += '.' + fileType" + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		// Create valid file name with RegEx. Remove invalide character.' + lineBreak)
		sb.append("		// Supported characters for file name are letters, numbers, spaces, and '()_-,.'." + lineBreak)
		sb.append("		fileName = fileName.replaceAll('[^a-zA-Z0-9 ( ) _ \\\\- , .]', '')" + lineBreak)
		sb.append('		// Remove leading and tailing whitespace with RegEx' + lineBreak)
		sb.append("		fileName = fileName.replaceAll('^\\\\s+|\\\\s+\$','')" + lineBreak)
		sb.append('		// Set file name to header' + lineBreak)
		sb.append('		message.setHeader("CamelFileName", fileName)' + lineBreak)
		sb.append('		result = fileName' + lineBreak)
		sb.append('	} else {' + lineBreak)
		sb.append('		throw new Exception("setFileName: File name can not be empty. Please check all input parameter.")' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodStripSpaces
* @param sb This is the string builder.
* @param methodStripSpaces This is activation for methodStripSpaces.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodStripSpaces(def sb, boolean methodStripSpaces, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodStripSpaces == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Remove leading and tailing whitespaces from string.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return result Returns a value without leading and tailing whitespaces.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String stripSpaces(String value) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	if(value) {' + lineBreak)
		sb.append('		// Use RegEx for remove leading and tailing whitespaces from string' + lineBreak)
		sb.append("		result = value.replaceAll('^\\\\s+|\\\\s+\$', '')" + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodTailString
* @param sb This is the string builder.
* @param methodTailString This is activation for methodTailString
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodTailString(def sb, boolean methodTailString, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodTailString == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Removes the leading characters of the first argument leaving the number of characters given by the second argument. Leading or trailing white spaces do not count.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @param tailLength Tail length' + lineBreak)
		sb.append('* @return result Returns tail string of input value.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String tailString(String value, int tailLength) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	if (value != null && tailLength > 0) {' + lineBreak)
		sb.append('		String trimmedValue = ""' + lineBreak)
		sb.append('		for (int i = value.length(); i > 0; i--) {' + lineBreak)
		sb.append("			if (value.charAt(i - 1) != ' ') {" + lineBreak)
		sb.append('				trimmedValue = value.substring(0, i)' + lineBreak)
		sb.append('				break' + lineBreak)
		sb.append('			}' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		int length = trimmedValue.length()' + lineBreak)
		sb.append('		if (length > tailLength) {' + lineBreak)
		sb.append('			result = trimmedValue.substring(length - tailLength)' + lineBreak)
		sb.append('		} else {' + lineBreak)
		sb.append('			result = value' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	} else {' + lineBreak)
		sb.append('		result = value' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodThrowExceptionIfNoValue
* @param sb This is the string builder.
* @param methodThrowExceptionIfNoValue This is activation for methodThrowExceptionIfNoValue
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodThrowExceptionIfNoValue(def sb, boolean methodThrowExceptionIfNoValue, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodThrowExceptionIfNoValue == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Throw exception if there is no value.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @param valueName Value name' + lineBreak)
		sb.append('* @return result Returns a exception if there is no value.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String throwExceptionIfNoValue(String value, String valueName) {' + lineBreak)
		sb.append("	String returnMessage = ''" + lineBreak)
		sb.append('	// Create return message' + lineBreak)
		sb.append('	if (valueName.trim().length() != 0) {' + lineBreak)
		sb.append('		returnMessage = "Value for node \'" + valueName + "\' can not be blank."' + lineBreak)
		sb.append('	} else {' + lineBreak)
		sb.append('		returnMessage = "Value can not be blank."' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	// Check if there is a value' + lineBreak)
		sb.append('	if (!value || value.trim().length() == 0) {' + lineBreak)
		sb.append('		throw Exception(returnMessage)' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return value' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodToNumber
* @param sb This is the string builder.
* @param methodToNumber This is activation for methodToNumber.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return result Returns a number.
*/
private def appendMethodToNumber(def sb, boolean methodToNumber, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodToNumber == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Formats a string like a number (removes + sign and leading/trailing zeros).' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return result Returns a number.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String toNumber(String numberString) {' + lineBreak)
		sb.append('	if (numberString != null && numberString.trim().length() == 0) {' + lineBreak)
		sb.append('		return null' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	try {' + lineBreak)
		sb.append('		return new BigDecimal(numberString).toString()' + lineBreak)
		sb.append('	} catch (Exception ex) {' + lineBreak)
		sb.append('		throw new Exception("toNumber: can not transform numberString \'$numberString\' to a number.")' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodTransformDate
* @param sb This is the string builder.
* @param methodTransformDate This is activation for methodTransformDate.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodTransformDate(def sb, boolean methodTransformDate, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodTransformDate == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* transform Date' + lineBreak)
		sb.append('* @param timeZone This is the time zone.' + lineBreak)
		sb.append('* @param dateInString This is the date.' + lineBreak)
		sb.append('* @param dateInFormat This is the date in format.' + lineBreak)
		sb.append('* @param dateOutFormat This is the date out format.' + lineBreak)
		sb.append('* @return result Returns a formated date.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String transformDate(def timeZone, String date, String dateInFormat, String dateOutFormat) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	def dateIn' + lineBreak)
		sb.append('	def dateOut' + lineBreak)
		sb.append('	if (timeZone && date) {' + lineBreak)
		sb.append('		try {' + lineBreak)
		sb.append('			// Initialize date in' + lineBreak)
		sb.append('			SimpleDateFormat sdfIn = new SimpleDateFormat(dateInFormat)' + lineBreak)
		sb.append('			sdfIn.setTimeZone(timeZone)' + lineBreak)
		sb.append('			dateIn = sdfIn.parse(date)' + lineBreak)
		sb.append('		} catch (Exception ex) {' + lineBreak)
		sb.append('			throw new Exception("transformDate: Cannot parse date in \'$date\' to date format \'$dateInFormat\'.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		try {' + lineBreak)
		sb.append('			// Set date out' + lineBreak)
		sb.append('			SimpleDateFormat sdfOut = new SimpleDateFormat(dateOutFormat)' + lineBreak)
		sb.append('			sdfOut.setTimeZone(timeZone)' + lineBreak)
		sb.append('			// Format date' + lineBreak)
		sb.append('			dateOut = sdfOut.format(dateIn)' + lineBreak)
		sb.append('			result = dateOut.toString()' + lineBreak)
		sb.append('		} catch (Exception ex) {' + lineBreak)
		sb.append('			throw new Exception("transformDate: Cannot parse date out \'$dateOut\' to date format \'$dateOutFormat\'.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append(' 	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodTrimRight
* @param sb This is the string builder.
* @param methodTrimRight This is activation for methodTrimRight.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodTrimRight(def sb, boolean methodTrimRight, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodTrimRight == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Removes trailing white spaces (leading white spaces may be significant).' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return result Returns a value without trailing white spaces.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String trimRight(String value) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	int length = value.length()' + lineBreak)
		sb.append('	if (length > 0) {' + lineBreak)
		sb.append('		char[] chars = value.toCharArray()' + lineBreak)
		sb.append('		int trailing = length - 1' + lineBreak)
		sb.append("		while (trailing > -1 && chars[trailing] == ' ') {" + lineBreak)
		sb.append('			trailing--' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('		result = value.substring(0, trailing + 1)' + lineBreak)
		sb.append('	} else {' + lineBreak)
		sb.append('		result = value' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodTrimZeroLeft
* @param sb This is the string builder.
* @param methodTrimZeroLeft This is activation for methodTrimZeroLeft.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodTrimZeroLeft(def sb, boolean methodTrimZeroLeft, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodTrimZeroLeft == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* Removes leading zeros.' + lineBreak)
		sb.append('* @param value Value' + lineBreak)
		sb.append('* @return result Returns a number without leading zeros.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String trimZeroLeft(String value) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append('	if (value) {' + lineBreak)
		sb.append('		if (value.trim().length() == 0) {' + lineBreak)
		sb.append('			result = value' + lineBreak)
		sb.append('		} else {' + lineBreak)
		sb.append('			result = value.replaceAll("^0*", "")' + lineBreak)
		sb.append('			if (result.trim().length() == 0) {' + lineBreak)
		sb.append('				result = "0"' + lineBreak)
		sb.append('			}' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	} else {' + lineBreak)
		sb.append('		result = value' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMethodValueMap
* @param sb This is the string builder.
* @param methodValueMap This is activation for methodValueMap.
* @param appendAllGroovyCostumFunctions This is activation for append all Groovy costum functions.
* @return sb Return string builder.
*/
private def appendMethodValueMap(def sb, boolean methodValueMap, boolean appendAllGroovyCostumFunctions, String lineBreak) {
	if (sb && methodValueMap == true || sb && appendAllGroovyCostumFunctions == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('* valueMap' + lineBreak)
		sb.append('* @param valueMapApi This is the valueMapApi object.' + lineBreak)
		sb.append('* @param sourceAgency This is the source agency.' + lineBreak)
		sb.append('* @param sourceIdentifier This is the source identifier.' + lineBreak)
		sb.append('* @param sourceKey This is the source key.' + lineBreak)
		sb.append('* @param targetAgency This is the target agency.' + lineBreak)
		sb.append('* @param targetIdentifier This is the target identifier' + lineBreak)
		sb.append("* @param behaviour This is the behaviour if lookup fails. ('Default value', 'Use key', 'Throw exception')" + lineBreak)
		sb.append('* @param defaultValue This is the default value.' + lineBreak)
		sb.append('* @return result Returns the result.' + lineBreak)
		sb.append('*/' + lineBreak)
		sb.append('private def String valueMap(def valueMapApi, String sourceAgency, String sourceIdentifier, String sourceKey, String targetAgency, String targetIdentifier, String behaviour, String defaultValue) {' + lineBreak)
		sb.append("	String result = ''" + lineBreak)
		sb.append("	String behaviourStr = behaviour.toLowerCase().replaceAll(' ','')" + lineBreak)
		sb.append('	// Check input values' + lineBreak)
		sb.append('	if (!sourceAgency) {' + lineBreak)
		sb.append('		throw Exception("valueMap: No \'sourceAgency\' parameter found in value mapping: \'$sourceAgency:$sourceIdentifier\' -> \'$targetAgency:$targetIdentifier\' for value \'$sourceKey\'.")' + lineBreak)
		sb.append('	} else if (!sourceIdentifier) {' + lineBreak)
		sb.append('		throw Exception("valueMap: No \'sourceIdentifier\' parameter found in value mapping: \'$sourceAgency:$sourceIdentifier\' -> \'$targetAgency:$targetIdentifier\' for value \'$sourceKey\'.")' + lineBreak)
		sb.append('	} else if (!targetAgency) {' + lineBreak)
		sb.append('		throw Exception("valueMap: No \'targetAgency\' parameter found in value mapping: \'$sourceAgency:$sourceIdentifier\' -> \'$targetAgency:$targetIdentifier\' for value \'$sourceKey\'.")' + lineBreak)
		sb.append('	} else if (!targetIdentifier) {' + lineBreak)
		sb.append('		throw Exception("valueMap: No \'targetIdentifier\' parameter found in value mapping: \'$sourceAgency:$sourceIdentifier\' -> \'$targetAgency:$targetIdentifier\' for value \'$sourceKey\'.")' + lineBreak)
		sb.append("	} else if (!('defaultvalue'.equals(behaviourStr) || 'usekey'.equals(behaviourStr) || 'throwexception'.equals(behaviourStr))) {" + lineBreak)
		sb.append('		throw Exception("valueMap: No allowed \'behaviour\' parameter \'$behaviour\' found in value mapping: \'$sourceAgency:$sourceIdentifier\' -> \'$targetAgency:$targetIdentifier\' for value \'$sourceKey\'. \'Default value\', \'Use key\' and \'Throw exception\' are allowed.")' + lineBreak)
		sb.append('	} else if (!valueMapApi) {' + lineBreak)
		sb.append('		throw Exception("valueMap: No \'valueMapApi-object\' parameter found in value mapping: \'$sourceAgency:$sourceIdentifier\' -> \'$targetAgency:$targetIdentifier\' for value \'$sourceKey\'.")' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	// Get target value for source key from value mapping' + lineBreak)
		sb.append('	result = valueMapApi.getMappedValue(sourceAgency, sourceIdentifier, sourceKey, targetAgency, targetIdentifier)' + lineBreak)
		sb.append('	// Error strategy' + lineBreak)
		sb.append('	if(result == null) {' + lineBreak)
		sb.append('		switch(behaviourStr) {' + lineBreak)
		sb.append("			case 'throwexception':" + lineBreak)
		sb.append('				throw Exception("valueMap: No entry for value \'$sourceKey\' found in value mapping: \'$sourceAgency:$sourceIdentifier\' -> \'$targetAgency:$targetIdentifier\'.")' + lineBreak)
		sb.append("			case 'usekey':" + lineBreak)
		sb.append('				result = sourceKey' + lineBreak)
		sb.append('				break' + lineBreak)
		sb.append("			case 'defaultvalue':" + lineBreak)
		sb.append('				result = defaultValue' + lineBreak)
		sb.append('				break' + lineBreak)
		sb.append('			default:' + lineBreak)
		sb.append('				throw Exception("valueMap: Behaviour parameter \'$behaviour\' not set properly in value mapping: \'$sourceAgency:$sourceIdentifier\' -> \'$targetAgency:$targetIdentifier\'.")' + lineBreak)
		sb.append('		}' + lineBreak)
		sb.append('	}' + lineBreak)
		sb.append('	return result' + lineBreak)
		sb.append('}')
	}
	return sb
}

/**
* appendMappingExamples
* @param sb This is the string builder.
* @param mappingExamples This is activation for mappingExamples.
* @return sb Return string builder.
*/
private def appendMappingExamples(def sb, boolean mappingExamples, String lineBreak) {
	if (sb && mappingExamples == true) {
		sb.append(lineBreak + lineBreak)
		sb.append('/**' + lineBreak)
		sb.append('Node segment creation examples' + lineBreak)
		sb.append('------------------------------' + lineBreak)
		sb.append("- Create a single target segment node:									TargetSegmentNodeName { ... }" + lineBreak)
		sb.append("- Create a target segment node for each source segment node:			root.SourceSegmentNodeName.each{SourceSegmentNodeName -> TargetSegmentNodeName { ... }}" + lineBreak)
		sb.append("- Create a target segment node for each source segment node with indes:	root.SourceSegmentNodeName.eachWithIndex{SourceSegmentNodeName, index -> TargetSegmentNodeName { ... }}" + lineBreak + lineBreak)
		sb.append('Node standard mapping examples' + lineBreak)	
		sb.append('------------------------------' + lineBreak)
		sb.append("- Set empty node: 											'TargetNodeName' ''" + lineBreak)
		sb.append("- Set constant:												'TargetNodeName' 'abc'" + lineBreak)
		sb.append("- Create a direct mapping from source node:					'TargetNodeName' SourceNodeName.text()" + lineBreak)
		sb.append("- Trim source node value:									'TargetNodeName' SourceNodeName.text().trim()')" + lineBreak)
		sb.append("- Set source node value to lower case:						'TargetNodeName' SourceNodeName.text().toLowerCase()" + lineBreak)
		sb.append("- Set source node value to upper case:						'TargetNodeName' SourceNodeName.text().toUpperCase()" + lineBreak)
		sb.append("- Set length of source node value:							'TargetNodeName' SourceNodeName.text().length()" + lineBreak)
		sb.append("- Replace a character or string in source node value:		'TargetNodeName' SourceNodeName.replaceAll(',','.')" + lineBreak)
		sb.append("  (replaceAll() method uses RegEx for finding. Please note" + lineBreak)
		sb.append("   that if source is null, you will get an error.)" + lineBreak)
		sb.append("- Set count of source segment node:							'TargetNodeName' SourceSegmentNodeName.size()" + lineBreak)
		sb.append("- Get message ID:											'TargetNodeName' message.getProperty('SAP_MessageProcessingLogID')" + lineBreak)
		sb.append("- Get an exchange property value:							'TargetNodeName' message.getProperty('ExchangePropertyName')" + lineBreak)
		sb.append("- Get sender from header:									'TargetNodeName' message.getHeader('SAP_Sender', String)" + lineBreak)
		sb.append("- Get receiver from header:									'TargetNodeName' message.getHeader('SAP_Receiver', String)" + lineBreak)
		sb.append("- Get file name from header:								'TargetNodeName' message.getHeader('CamelFileName', String)" + lineBreak)		
		sb.append("- Get a header value:										'TargetNodeName' message.getHeader('HeaderValueName', String)" + lineBreak + lineBreak)
		sb.append('Node mapping examples with Groovy Custom Functions' + lineBreak)	
		sb.append('--------------------------------------------------' + lineBreak)
		sb.append("- Passes a value from condition node value with method assignValueByCondition(conditionValue, suchValues, returnValue): 	'TargetNodeName' assignValueByCondition(SourceNodeName1.text(), ['005','006'], SourceNodeName2.text())" + lineBreak)
		sb.append("- Dynamic substring with method dynamicSubstring(value, start, length):													'TargetNodeName' dynamicSubstring(SourceNodeName.text(), 0, 4)" + lineBreak)
		sb.append("- Fill up string to length with leading zeros with method fillLeadingZeros(value, totalLength):							'TargetNodeName' fillLeadingZeros(SourceNodeName.text(), 10)" + lineBreak)
		sb.append("- Fill up string to length with space with method fillUpToLengthWithSpace(value, totalLength):							'TargetNodeName' fillUpToLengthWithSpace(SourceNodeName.text(), 10)" + lineBreak)
		sb.append("- Use source node with method fixedValues(value, mapTable, behaviour, defaultValue):									'TargetNodeName' fixedValues(SourceNodeName.text(), ['a':'1','b':'2'], 2, 'Default value')" + lineBreak)
		sb.append("  Behaviour if lookup fails: 'Default value', 'Use key', 'Throw exception'" + lineBreak)
		sb.append("- Format a number with method formatNumber(numberStr, pattern, decimalSeparator):										'TargetNodeName' formatNumber(SourceNodeName.text(), '#,##0.00', ',')" + lineBreak)
		sb.append("- Format useing space with method formatValueBySpace(value, length, cutLengthDirection, fillSapce, fillSpaceDirection):	'TargetNodeName' formatValueBySpace(SourceNodeName.text(), 10, 'right', true, 'right')" + lineBreak)
		sb.append("- Format using zero with method formatValueByZero(value, length, cutLengthDirection, fillZero, fillZeroDirection):		'TargetNodeName' formatValueByZero(SourceNodeName.text(), 10, 'right', true, 'right')" + lineBreak)
		sb.append("- Get a date after x days with method getDateAfterDays(timeZone, date, dateInFormat, days, dateOutFormat):				'TargetNodeName' getDateAfterDays(timeZone, SourceNodeName.text(), 'yyyyMMdd', 1, 'yyyyMMdd')" + lineBreak)
		sb.append("- Gets an EDIFACT-F2379-conform qualifier descriping the dates format with method getDateFormat(date):					'TargetNodeName' getDateFormat(SourceNodeName.text())" + lineBreak)
		sb.append("- Get a mandatory exchange property value with method getExchangeProperty(message, propertyName, mandatory):			'TargetNodeName' getExchangeProperty(message, 'ExchangePropertyName', true)" + lineBreak)
		sb.append("- Get a mandatory header value with method getHeader(message, HeaderName, mandatory):									'TargetNodeName' getHeader(message, 'HeaderName', true)" + lineBreak)
		sb.append("- Check if value is a number with method isNumber(value):																'TargetNodeName' isNumber(SourceNodeName.text())" + lineBreak)
		sb.append("- Get date of Monday in week with method getMondayOfWeek(locale, year, week):											'TargetNodeName' getMondayOfWeek(locale, SourceNode1Name.text(), SourceNode2Name.text())" + lineBreak)
		sb.append("- Get only numbers from string with method getOnlyNumbers(value):														'TargetNodeName' getOnlyNumbers(SourceNodeName.text())" + lineBreak)
		sb.append("- Gets a single value from condition node value with method" + lineBreak)
		sb.append("  getValueByCondition(conditionPath, nodeNameConditionValue, suchValue, nodeNameValue)									'TargetNodeName' getValueByCondition(SourcePath1, SourceNodeName1, '005', SourceNodeName2))" + lineBreak)
		sb.append("- Removes the trailing characters with method headString(value, headLength):											'TargetNodeName' headString(SourceNodeName.text(), 10)" + lineBreak)
		sb.append("- Use source node with method mapWithDefault(value, defaultValue):														'TargetNodeName' mapWithDefault(SourceNodeName.text(), 'Default value')" + lineBreak)
		sb.append("- Change minus from begin to end with method minusFromBeginToEnd(value):												'TargetNodeName' minusFromBeginToEnd(SourceNodeName.text())" + lineBreak)
		sb.append("- Change minus from end to begin with method minusFromEndToBegin(value):												'TargetNodeName' minusFromEndToBegin(SourceNodeName.text())" + lineBreak)
		sb.append("- Remove all algebraic signs with method removeAlgebraicSign(value):													'TargetNodeName' removeAlgebraicSign(SourceNodeName.text())" + lineBreak)
		sb.append("- Remove only algebraic sign plus with method removeAlgebraicSignPlus(value):											'TargetNodeName' removeAlgebraicSignPlus(SourceNodeName.text())" + lineBreak)
		sb.append("- Remove all characters with method removeAllCharacters(value):															'TargetNodeName' removeAllCharacters(SourceNodeName.text())" + lineBreak)
		sb.append("- Remove all spaces with method removeAllSpaces(value):																	'TargetNodeName' removeAllSpaces(SourceNodeName.text())" + lineBreak)
		sb.append("- Remove all special characters with method removeAllSpecialCharacters(value):											'TargetNodeName' removeAllSpecialCharacters(SourceNodeName.text())" + lineBreak)
		sb.append("- Remove all carriage return line Feed (CRLF) with method removeCarriageReturnLineFeed(value):							'TargetNodeName' removeCarriageReturnLineFeed(SourceNodeName.text())" + lineBreak)
		sb.append("- Remove decimal if zero with methode removeDecimalIfZero(value):														'TargetNodeName' removeDecimalIfZero(SourceNodeName.text())" + lineBreak)
		sb.append("- Replace special characters with methode replaceSpecialCharacters(value):												'TargetNodeName' replaceSpecialCharacters(SourceNodeName.text())" + lineBreak)
		sb.append("- Replace umlauts with methode replaceUmlauts(value):																	'TargetNodeName' replaceUmlauts(SourceNodeName.text())" + lineBreak)
		sb.append("- Gets true if value from value node is corresponding to segment value, otherwise false with methode" + lineBreak)
		sb.append("  segmentHasOneOfSuchValues(conditionPath, nodeNameConditionValue, suchValues):											'TargetNodeName' segmentHasOneOfSuchValues(SourceNodePath, SourceNodeName, '005')" + lineBreak)
		sb.append("- Set current date with method currentDate(timeZone, dateFormat):														'TargetNodeName' setCurrentDate(timeZone, 'yyyyMMdd')" + lineBreak)
		sb.append("- Set current time with method currentDate(timeZone, timeFormat):														'TargetNodeName' setCurrentDate(timeZone, 'HHmmss')" + lineBreak)
		sb.append("- Sets decimal separator to point and remove thousands separator with method setDecimalSeparatorPoint(value):			'TargetNodeName' setDecimalSeparatorPoint(SourceNodeName.text())" + lineBreak)
		sb.append("- Set default current time with method setDefaultAsCurrentDate(timeZone, value, dateFormat):							'TargetNodeName' setDefaultAsCurrentDate(timeZone, SourceNodeName.text(), 'yyyyMMdd')" + lineBreak)
		sb.append("- Set directory with method setDirectory(message, timeZone, rootFolder, addYear, addMonth, addDay, sender, messageType," + lineBreak)
		sb.append("  messageVersion, messageRelease):																						'TargetNodeName' setDirectory(message, timeZone, 'root', true, true, true, '', '', '', '')" + lineBreak)
		sb.append("- Set file name with method setFileName(message, timeZone, messageType, messageVersion, messageRelease, addTimeStamp," + lineBreak)
		sb.append("  dateFormat, documentNumber, addMessageId, addCorrelationId, nameSeparator, fileType):									'TargetNodeName' setFileName(message, timeZone, 'INVOIC', '', '', true, '', '', true, false, '_', 'xml')" + lineBreak)
		sb.append("- Strip space with method stripSpaces(value):																			'TargetNodeName' stripSpaces(SourceNodeName.text())" + lineBreak)
		sb.append("- Removes the leading characters with method tailString(value, tailLength):												'TargetNodeName' tailString(SourceNodeName.text(), 10)" + lineBreak)
		sb.append("- Throw exception if no value (mandatory node) with method throwExceptionIfNoValue(value):								'TargetNodeName' throwExceptionIfNoValue(SourceNodeName.text())" + lineBreak)
		sb.append("- Format a string like a number with method toNumber(value):															'TargetNodeName' toNumber(SourceNodeName.text())" + lineBreak)
		sb.append("- Transform a date to another date format with method transformDate(timeZone, dateString, dateInFormat, dateOutFormat):	'TargetNodeName' transformDate(timeZone, SourceNodeName.text(), 'yyyyMMdd', 'yyyy-MM-dd')" + lineBreak)
		sb.append("- Trim right with method trimRight(value):																				'TargetNodeName' trimRight(SourceNodeName.text())" + lineBreak)
		sb.append("- Trim zero left with method trimZeroLeft(value):																		'TargetNodeName' trimZeroLeft(SourceNodeName.text())" + lineBreak)
		sb.append("- Use source node with method valueMap(valueMapApi, sourceAgency, sourceIdentifier, sourceKey, targetAgency, ..." + lineBreak)
		sb.append("  targetIdentifier, behaviour, defaultValue) Behaviour if lookup fails: 'Default value', 'Use key', 'Throw exception':	'TargetNodeName' valueMap(valueMapApi, 'sourceAgency', 'sourceIdentifier', SourceNodeName.text(), 'targetAgency', 'targetIdentifier', 'default value', '')" + lineBreak + lineBreak)
		sb.append('Additional settings in Groovy Script' + lineBreak)
		sb.append('------------------------------------' + lineBreak)
		sb.append("- Set a source value to an exchange property value:		'TargetNodeName' message.setProperty('ExchangePropertyName', SourceNodeName.text())" + lineBreak)
		sb.append("- Set a source value to a header value:					'TargetNodeName' message.setHeader('HeaderValueName', SourceNodeName.text())" + lineBreak)
		sb.append("- Set a source value to message type in header:			'TargetNodeName' message.setHeader('SAP_MessageType', SourceNodeName.text())" + lineBreak)
		sb.append("- Dynamic configuration for adapter and other objects:	https://help.sap.com/docs/cloud-integration/sap-cloud-integration/headers-and-exchange-properties-provided-by-integration-framework" + lineBreak)
		sb.append('*/')
	}
	return sb
}