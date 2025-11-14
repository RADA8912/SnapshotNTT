import com.sap.gateway.ip.core.customdev.util.Message


def Message processData(Message message) {
    String body = ''

    String currentStepName = message.getCurrentStepName()

    def messageHistories = message.getProperty(Exchange.MESSAGE_HISTORY).join(',')
    def lastCallActivity = messageHistories.substring(messageHistories.lastIndexOf("CallActivity"), messageHistories.length());
    def activity = lastCallActivity.substring(0, lastCallActivity.indexOf("]"));

    body = currentStepName
    
	message.setBody(body)
    return message
}