import com.sap.gateway.ip.core.customdev.util.Message

/**
* Set EDIFACT Syntax version number from 1 or 2 to 3.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {

	def body = message.getBody(java.lang.String) as String

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