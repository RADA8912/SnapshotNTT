/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/de/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/de/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String
    
    String theBody = "/ns0:DB_TRACK_ORDERRESPONSE_InsertStatement_Message/DeleteHeaderData"
    
    body = removeAllPrefix(theBody)
    
    message.setBody(body)

    return message
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
				if (theValue) {
    				int indexPrefix = theValue.indexOf(':')
    				if (indexPrefix > -1) {
    					// Remove namespace prefix
    					tmpList[i] = theValue.substring(indexPrefix + 1, theValue.length())
    				}
				}
			}
			// Join values
			valueNew = tmpList.join(nodeSeparator)
		} else {
	        valueNew = "ELSE_2"
		}
	} else {
	    valueNew = "ELSE_1"
	}
	return valueNew
}