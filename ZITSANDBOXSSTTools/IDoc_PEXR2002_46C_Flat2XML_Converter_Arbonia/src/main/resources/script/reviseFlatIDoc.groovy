import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message)
{
	String[] lines = message.getBody(String).tokenize("\n")
	def result
	def lineNumber = 1
	def lineNumberParent = 0
	def lineNumberParentSet = false
	for (String line: lines){
		if (!line.equals("")){
			if (line.startsWith("EDI_DC40")){
				result = line + "\n"
			}
			else if (line.length() >= 61){
				def linestart = line.substring(0, 49)
				def lineend = line.substring(61, line.length())
				if (line.startsWith("E1IDKU1") || line.startsWith("E1EDK03")  || line.startsWith("E1IDKU3")  || line.startsWith("E1IDKU5") || line.startsWith("E1EDKA1") || line.startsWith("E1IDPU1") || line.startsWith("E1IDLU5")){
					lineNumberParent = 0
					lineNumberParentSet = false
					result += linestart + String.format("%06d", lineNumber) + String.format("%06d", lineNumberParent) + lineend + "\n"
				}
				else if (line.startsWith("E1IDPU5") || line.startsWith("E1EDK14") || line.startsWith("E1EDP03") || line.startsWith("E1EDP02")){
					if (!lineNumberParentSet){
						lineNumberParent = lineNumber-1
						lineNumberParentSet = true
					}
					result += linestart + String.format("%06d", lineNumber) + String.format("%06d", lineNumberParent) + lineend + "\n"
				}
				else {
					result += "Error\n"
				}
				lineNumber++
			}
			else {
				result += "Error\n"
			}
		}
	}
    message.setBody(result)
    return message
}