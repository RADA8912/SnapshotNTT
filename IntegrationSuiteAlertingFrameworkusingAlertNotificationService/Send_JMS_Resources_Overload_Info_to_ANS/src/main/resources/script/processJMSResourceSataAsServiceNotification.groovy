import com.sap.gateway.ip.core.customdev.util.Message
import com.sap.it.api.ITApiFactory
import groovy.json.JsonOutput

Message processData(Message message) {
    
    String JMSStatus = message.getProperty("JMSStatus");
    String CapacityStatus = message.getProperty("CapacityStatus"); 
    String PercentCapacity = message.getProperty("PercentCapacity"); 
    String QueueCapacityError = message.getProperty("QueueCapacityError"); 
    String QueueCapacityWarning = message.getProperty("QueueCapacityWarning"); 
    String QueueCapacityOK = message.getProperty("QueueCapacityOK"); 
    String QueuesCriticalNames = message.getProperty("QueuesCriticalNames"); 
    String QueuesExhaustedNames = message.getProperty("QueuesExhaustedNames"); 
    String QueueStatus = message.getProperty("QueueStatus"); 
    String Queues = message.getProperty("Queues"); 
    String MaxQueues = message.getProperty("MaxQueues"); 
    String TransactionStatus = message.getProperty("TransactionStatus"); 
    String ConsumerStatus = message.getProperty("ConsumerStatus"); 
    String ProviderStatus = message.getProperty("ProviderStatus"); 
    
    Event event = new Event(
			eventType: "JMSQueueDepletion",
			resource: new Resource(
					resourceName: "CPIJMSQueue",
					resourceType: "JMSQueue"
			),
			severity: "WARNING",
			category: "ALERT",
			subject: "Alert - CI - JMS resource depletion: " + JMSStatus, 
			body: "The JMS Resources on the Cloud Integration tenant are ${JMSStatus}.\nOverall Queue Capacity Status: ${CapacityStatus} (${PercentCapacity} percent)\n\nCapacity Status for Queues:\nOK: ${QueueCapacityOK} Queues\nWarning: ${QueueCapacityWarning} Queues\nNames of queues with warnings:${QueuesCriticalNames}\nError: ${QueueCapacityError} Queues \nNames of queues with errors: ${QueuesExhaustedNames}\n\nQueue Status: ${QueueStatus} ${Queues} / ${MaxQueues}\nTransactions Status: ${TransactionStatus}\nConsumer Connections Status: ${ConsumerStatus}\nProvider Connections Status: ${ProviderStatus}"
            )
    
    def eventMsg = JsonOutput.toJson(event);
    message.setBody(eventMsg);
	return message
}

class Event {
	String eventType
	Resource resource
	String severity
	String category
	String subject
	String body
    Map<String, String> tags
}

class Resource {
	String resourceName
	String resourceType
}

