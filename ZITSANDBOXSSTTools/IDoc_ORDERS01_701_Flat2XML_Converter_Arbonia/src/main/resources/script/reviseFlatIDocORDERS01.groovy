import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message)
{
	String[] lines = message.getBody(String).tokenize("\n")
	def result
	def lineNumber = 1
	def lineNumberParentL1 = 0
	def lineNumberParentL2 = 0
	def lineNumberParentSetL1 = false
	def lineNumberParentSetL2 = false
	for (String line: lines){
		if (!line.equals("")){
			if (line.startsWith("EDI_DC40")){
				result = line + "\n"
			}
			else if (line.length() >= 61){
				def linestart = line.substring(0, 49)
				def lineend = line.substring(61, line.length())
				if (line.startsWith("E2EDK01") || line.startsWith("E2EDK14")  || line.startsWith("E2EDK03")  || line.startsWith("E2EDKA1") || line.startsWith("E2EDK02") || line.startsWith("E2EDKT1") || line.startsWith("E2EDP01")){
					lineNumberParentL1 = 0
					lineNumberParentL2 = 0
					lineNumberParentSetL1 = false
					lineNumberParentSetL2 = false
					result += linestart + String.format("%06d", lineNumber) + String.format("%06d", 0) + lineend + "\n"
				}
				else if (line.startsWith("E2EDKT2") || line.startsWith("E2EDP03") || line.startsWith("E2EDP19") || line.startsWith("E2EDPT1")){
					lineNumberParentL2 = 0
					lineNumberParentSetL2 = false
					if (!lineNumberParentSetL1){
						lineNumberParentL1 = lineNumber-1
						lineNumberParentSetL1 = true
					}
					result += linestart + String.format("%06d", lineNumber) + String.format("%06d", lineNumberParentL1) + lineend + "\n"
				}
				else if (line.startsWith("E2EDPT2")){
					if (!lineNumberParentSetL2){
						lineNumberParentL2 = lineNumber-1
						lineNumberParentSetL2 = true
					}
					result += linestart + String.format("%06d", lineNumber) + String.format("%06d", lineNumberParentL2) + lineend + "\n"
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