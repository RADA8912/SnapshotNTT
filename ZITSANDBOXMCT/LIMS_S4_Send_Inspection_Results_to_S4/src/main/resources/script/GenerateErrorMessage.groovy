import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput
import java.text.SimpleDateFormat

def Message processData(Message message) {
    def map = message.getProperties()
    def headers = message.getHeaders()
    def ex = map.get("CamelExceptionCaught")
    def responseBody = "No response body available"

    if (ex != null && ex.getClass().getCanonicalName().equals("org.apache.camel.component.ahc.AhcOperationFailedException")) {
        responseBody = ex.getResponseBody() ?: "No response body available"
    }

    // Convert Kafka timestamp from milliseconds to ISO 8601 date format
    def kafkaTimestamp = headers.get("kafka.TIMESTAMP")
    def isoFormattedDate = "No timestamp available"
    if (kafkaTimestamp) {
        try {
            def date = new Date(kafkaTimestamp as long)
            def dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
            isoFormattedDate = dateFormat.format(date)
        } catch (Exception e) {
            isoFormattedDate = "Invalid timestamp format"
        }
    }

    def json = [
        inspectionLot: map.get("InspectionLot"),
        inspectionLotOperation: map.get("InspectionLotOperation"),
        batch: map.get("Batch"),
        httpStatusCode: headers.get("CamelHttpResponseCode"),
        httpErrorMessage: responseBody,
        kafkaTransactionId: headers.get("transaction_id"),
        integrationTransactionId: map.get("SAP_MessageProcessingLogID"),
        kafkaTimestamp: isoFormattedDate
    ]

    message.setBody(JsonOutput.toJson(json))
    return message
}