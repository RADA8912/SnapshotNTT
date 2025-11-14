/*
Set Filter Date Time To Header
 */
import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap

def Message processData(Message message) {
	//Create DateStart and DateEnd
	use(groovy.time.TimeCategory) {
		def dateNewStart = new Date() - 8.day
		filterDateStart = dateNewStart.format("yyyy-MM-dd'T'23:00:00.000")
		def dateNewEnd = new Date() + 1.hour
		filterDateEnd = dateNewEnd.format("yyyy-MM-dd'T'HH:mm:ss.SSS")
	}

	//Set Value To Header
	message.setHeader("FilterDateStart", filterDateStart)
	message.setHeader("FilterDateEnd", filterDateEnd)

	return message;
}