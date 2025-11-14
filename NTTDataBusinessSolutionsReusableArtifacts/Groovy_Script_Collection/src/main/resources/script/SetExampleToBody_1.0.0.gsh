import com.sap.gateway.ip.core.customdev.util.Message

/**
* SetExampleToBody
* This Groovy script sets an example payload to body.
* So you can easy save examples as part of Groovy Script in Integration Flow an reuse it for testing’s.
* You can save example name, example description and example payload.
* You can switch examples in 'SELECTED_EXAMPLE' or use a script function.
* 
* Script Function for processing steps:
* - empty -> Default set example like configured in constant SELECTED_EXAMPLE
* - setExample1 -> set example 1
* - setExample2 -> set example 2
* - setExample3 -> set example 3
* - setExample4 -> set example 4
* - setExample5 -> set example 5
* - setExample6 -> set example 6
* - setExample7 -> set example 7
* - setExample8 -> set example 8
* - setExample9 -> set example 9
* - setExample10 -> set example 10
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	// Here you can switch examples
	final String SELECTED_EXAMPLE = 'Example_1' // ('Example_1' to 'Example_10')

	// Set Values to properties and message body
	setValues(message, SELECTED_EXAMPLE)
	return message
}

/**
* setExample1
* This Groovy script sets an example 1 payload to body.
*/
def Message setExample1(Message message) {
	// Set Values to properties and message body
	setValues(message, 'Example_1')
	return message
}

/**
* setExample2
* This Groovy script sets an example 2 payload to body.
*/
def Message setExample2(Message message) {
	// Set Values to properties and message body
	setValues(message, 'Example_2')
	return message
}

/**
* setExample3
* This Groovy script sets an example 3 payload to body.
*/
def Message setExample3(Message message) {
	// Set Values to properties and message body
	setValues(message, 'Example_3')
	return message
}

/**
* setExample4
* This Groovy script sets an example 4 payload to body.
*/
def Message setExample4(Message message) {
	// Set Values to properties and message body
	setValues(message, 'Example_4')
	return message
}

/**
* setExample5
* This Groovy script sets an example 5 payload to body.
*/
def Message setExample5(Message message) {
	// Set Values to properties and message body
	setValues(message, 'Example_5')
	return message
}

/**
* setExample6
* This Groovy script sets an example 6 payload to body.
*/
def Message setExample6(Message message) {
	// Set Values to properties and message body
	setValues(message, 'Example_6')
	return message
}

/**
* setExample7
* This Groovy script sets an example 7 payload to body.
*/
def Message setExample7(Message message) {
	// Set Values to properties and message body
	setValues(message, 'Example_7')
	return message
}

/**
* setExample8
* This Groovy script sets an example 8 payload to body.
*/
def Message setExample8(Message message) {
	// Set Values to properties and message body
	setValues(message, 'Example_8')
	return message
}

/**
* setExample9
* This Groovy script sets an example 9 payload to body.
*/
def Message setExample9(Message message) {
	// Set Values to properties and message body
	setValues(message, 'Example_9')
	return message
}

/**
* setExample10
* This Groovy script sets an example 10 payload to body.
*/
def Message setExample10(Message message) {
	// Set Values to properties and message body
	setValues(message, 'Example_10')
	return message
}

/**
* setValues
* Sets values to properties and message body.
*/
private String setValues(Message message, String selectedExample) {
	String body = ''
	String[] arrExampleConfig

	switch(selectedExample.toLowerCase()) {
		case 'example_1':
			arrExampleConfig = getConfigurationExample1()
			break
		case 'example_2':
			arrExampleConfig = getConfigurationExample2()
			break
		case 'example_3':
			arrExampleConfig = getConfigurationExample3()
			break
		case 'example_4':
			arrExampleConfig = getConfigurationExample4()
			break
		case 'example_5':
			arrExampleConfig = getConfigurationExample5()
			break
		case 'example_6':
			arrExampleConfig = getConfigurationExample6()
			break
		case 'example_7':
			arrExampleConfig = getConfigurationExample7()
			break
		case 'example_8':
			arrExampleConfig = getConfigurationExample8()
			break
		case 'example_9':
			arrExampleConfig = getConfigurationExample9()
			break
		case 'example_10':
			arrExampleConfig = getConfigurationExample10()
			break
	 default: 
		throw Exception("Please insert a defind 'SELECTED_EXAMPLE' ('Example_1' to 'Example_10').") 
		break
	}

	// Set example configuration
	message.setProperty('SetExampleToBody.Name',arrExampleConfig[0])
	message.setProperty('SetExampleToBody.Description',arrExampleConfig[1])
	body = arrExampleConfig[2]

	// Set example to body
	message.setBody(body)
}

/**
* Get Example Configuration
*/

/**
 * getExample1
 * @return arrReturn Return array for example configuration.
 */
private String[] getConfigurationExample1() {
	def arrReturn = new String[3]
	// Name
	arrReturn[0] = 'Example 1: XML'
	// Description
	arrReturn[1] = 'XML Example'
	// Example
	arrReturn[2] = '''<?xml version="1.0" encoding="UTF-8"?>
<CSV_XML>
	<ROW>
		<COL_1>2</COL_1>
		<COL_2>bbb</COL_2>
	</ROW>
	<ROW>
		<COL_1>1</COL_1>
		<COL_2>aaa</COL_2>
	</ROW>
	<ROW>
		<COL_1>4</COL_1>
		<COL_2>ddd</COL_2>
	</ROW>
	<ROW>
		<COL_1>3</COL_1>
		<COL_2>ccc</COL_2>
	</ROW>
</CSV_XML>'''

	return arrReturn
}

/**
 * getExample2
 * @return arrReturn Return array for example configuration.
 */
private String[] getConfigurationExample2() {
	def arrReturn = new String[3]
	// Name
	arrReturn[0] = 'Example 2: JSON'
	// Description
	arrReturn[1] = 'JSON Example'
	// Example
	arrReturn[2] = '''{
	"ROW": [
		{
			"COL_1": "11",
			"COL_2": "",
			"COL_3": "13"
		},
		{
			"COL_1": "21",
			"COL_2": "22",
			"COL_3": ""
		}
	]
}'''

	return arrReturn
}

/**
 * getExample3
 * @return arrReturn Return array for example configuration.
 */
private String[] getConfigurationExample3() {
	def arrReturn = new String[3]
	// Name
	arrReturn[0] = 'Example 3: CSV'
	// Description
	arrReturn[1] = 'CSV Example'
	// Example
	arrReturn[2] = '''a;b;c
1;2;3'''

	return arrReturn
}

/**
 * getExample4
 * @return arrReturn Return array for example configuration.
 */
private String[] getConfigurationExample4() {
	def arrReturn = new String[3]
	// Name
	arrReturn[0] = 'Example 4:'
	// Description
	arrReturn[1] = 'Example 4'
	// Example
	arrReturn[2] = ''''''

	return arrReturn
}

/**
 * getExample5
 * @return arrReturn Return array for example configuration.
 */
private String[] getConfigurationExample5() {
	def arrReturn = new String[3]
	// Name
	arrReturn[0] = 'Example 5:'
	// Description
	arrReturn[1] = 'Example 5'
	// Example
	arrReturn[2] = ''''''

	return arrReturn
}

/**
 * getExample6
 * @return arrReturn Return array for example configuration.
 */
private String[] getConfigurationExample6() {
	def arrReturn = new String[3]
	// Name
	arrReturn[0] = 'Example 6:'
	// Description
	arrReturn[1] = 'Example 6'
	// Example
	arrReturn[2] = ''''''

	return arrReturn
}

/**
 * getExample7
 * @return arrReturn Return array for example configuration.
 */
private String[] getConfigurationExample7() {
	def arrReturn = new String[3]
	// Name
	arrReturn[0] = 'Example 7:'
	// Description
	arrReturn[1] = 'Example 7'
	// Example
	arrReturn[2] = ''''''

	return arrReturn
}

/**
 * getExample8
 * @return arrReturn Return array for example configuration.
 */
private String[] getConfigurationExample8() {
	def arrReturn = new String[3]
	// Name
	arrReturn[0] = 'Example 8:'
	// Description
	arrReturn[1] = 'Example 8'
	// Example
	arrReturn[2] = ''''''

	return arrReturn
}

/**
 * getExample9
 * @return arrReturn Return array for example configuration.
 */
private String[] getConfigurationExample9() {
	def arrReturn = new String[3]
	// Name
	arrReturn[0] = 'Example 9:'
	// Description
	arrReturn[1] = 'Example 9'
	// Example
	arrReturn[2] = ''''''

	return arrReturn
}

/**
 * getExample10
 * @return arrReturn Return array for example configuration.
 */
private String[] getConfigurationExample10() {
	def arrReturn = new String[3]
	// Name
	arrReturn[0] = 'Example 10:'
	// Description
	arrReturn[1] = 'Example 10'
	// Example
	arrReturn[2] = ''''''

	return arrReturn
}