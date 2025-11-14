import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.camel.Exchange
import org.apache.camel.builder.SimpleBuilder

def Message processData(Message message) {
	Exchange ex = message.exchange
	StringBuilder sb = new StringBuilder()
	
	def evaluateSimple = { simpleExpression ->
		SimpleBuilder.simple(simpleExpression).evaluate(ex, String)
	}
	
	sb << 'Camel ID: ' + evaluateSimple('${camelContext.getName}') + '\r\n'
	sb << 'Exchange ID: ' + evaluateSimple('${exchangeId}') + '\r\n'
	sb << evaluateSimple('${messageHistory}') + '\r\n'
	message.setBody(sb.toString())	
	
	return message
}