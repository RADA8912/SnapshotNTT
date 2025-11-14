/*
Set Filter Date Time To Header
 */
import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap

def Message processData(Message message) {
	//Create DateStart and DateEnd
	use(groovy.time.TimeCategory) {
		def dateNewStart = new Date() - 1.hour
		filterDateStart = dateNewStart.format("yyyy-MM-dd'T'HH:mm:00.000")
		def dateNewEnd = new Date()
		filterDateEnd = dateNewEnd.format("yyyy-MM-dd'T'HH:mm:00.000")
	}

	//Set Value To Header
	message.setHeader("FilterDateStart", filterDateStart)
	message.setHeader("FilterDateEnd", filterDateEnd)

	return message;
}