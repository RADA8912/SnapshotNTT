import com.sap.gateway.ip.core.customdev.util.Message
/**
* Set EDIFACT version from ":.:911" to ":92:1".
*/
def Message processData(Message message) {
	def body = message.getBody(java.lang.String) as String
	
	//Replace Message Version
	body = body.replaceAll("DELFOR:1:911","DELFOR:92:1")
	body = body.replaceAll("INVOIC:2:911","INVOIC:92:1")

    //Replace Syntax Version
	String[][] chartable= new String[6][2]
	chartable[0][0]="UNOA:1"
	chartable[0][1]="UNOA:3"
	chartable[1][0]="UNOA:2"
	chartable[1][1]="UNOA:3"
	chartable[2][0]="UNOB:1"
	chartable[2][1]="UNOB:3"
	chartable[3][0]="UNOB:2"
	chartable[3][1]="UNOB:3"
	chartable[4][0]="UNOC:1"
	chartable[4][1]="UNOC:3"
	chartable[5][0]="UNOC:2"
	chartable[5][1]="UNOC:3"
	for (int i=0;i<chartable.length;i++) {
		body = body.replaceAll(chartable[i][0],chartable[i][1])
	}
	
	message.setBody(body)
	return message
}