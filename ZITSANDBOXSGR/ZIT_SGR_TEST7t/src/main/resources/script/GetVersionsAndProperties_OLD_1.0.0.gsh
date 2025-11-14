import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.camel.impl.DefaultCamelContext
import org.osgi.framework.FrameworkUtil
import org.apache.camel.Exchange
import org.apache.camel.builder.SimpleBuilder

/**
* GetVersionsAndProperties
* This Groovy script gets used versions and properties of tenant.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	final String LINE_SEPARATOR = '\r\n'
	String body = ""

	// Get tenant information
	body = "SAP Cloud Integration" + LINE_SEPARATOR
	body += "---------------------" + LINE_SEPARATOR
	def bundleContext = FrameworkUtil.getBundle(Class.forName("com.sap.gateway.ip.core.customdev.util.Message")).getBundleContext()
	def profileBundle = bundleContext.getBundles().find() {"com.sap.it.node.stack.profile".equalsIgnoreCase(it.getSymbolicName())}

	body += "Build number: " + profileBundle.getVersion().toString() + LINE_SEPARATOR

    String tenantName = getSimpleExpression(message, '${sysenv.TENANT_NAME}')
	body += "Tenant name: " + tenantName + LINE_SEPARATOR

    String systemID = getSimpleExpression(message, '${sysenv.IT_SYSTEM_ID}')
	body += "System ID: " + systemID + LINE_SEPARATOR
	String cfInstanceIndex = getSimpleExpression(message, '${sysenv.CF_INSTANCE_INDEX}')
	if (cfInstanceIndex) {
		body += "Environment: Cloud Foundry" + LINE_SEPARATOR + LINE_SEPARATOR
	} else {
		body += "Environment: Neo" + LINE_SEPARATOR + LINE_SEPARATOR
	}

	// Get runtime versions
	def camelCtx = new DefaultCamelContext()
	body += "Runtime versions" + LINE_SEPARATOR
	body += "----------------" + LINE_SEPARATOR
	body += "Camel version: " + camelCtx.version + LINE_SEPARATOR
	body += "Groovy version: " + GroovySystem.getVersion() + LINE_SEPARATOR
	body += "Java version: " + getSimpleExpression(message, '${properties:java.version}') + LINE_SEPARATOR + LINE_SEPARATOR
	
	// Get supported Cloud Foundry environment variables
	String[] variableNames = null
	if (cfInstanceIndex) {
		body += "Cloud Foundry environment values:" + LINE_SEPARATOR
		body += "---------------------------------" + LINE_SEPARATOR
		variableNames = ["TENANT_NAME", "IT_SYSTEM_ID", "IT_TENANT_UX_DOMAIN", "TENANT_ID", "CF_INSTANCE_PORT", "SERVER_PORT", "VCAP_APPLICATION"]
	} else {
		body += "Neo environment values:" + LINE_SEPARATOR
		body += "-----------------------" + LINE_SEPARATOR
		variableNames = ["HC_ACCOUNT", "HC_APPLICATION", "HC_APPLICATION_URL", "HC_AVAILABILITY_ZONE", "HC_HOST", "HC_HOST_CERT", "HC_HOST_SVC", "HC_LANDSCAPE", "HC_LOCAL_HTTP_PORT", "HC_OP_HTTP_PROXY_HOST", "HC_OP_HTTP_PROXY_PORT", "HC_PROCESS_ID", "HC_REGION"]
	}
	for (int i = 1; i < variableNames.length; i++) {
        body += variableNames[i] + "=" + getSimpleExpression(message, '${sysenv.' + variableNames[i] +'}') + LINE_SEPARATOR
	}
	body += LINE_SEPARATOR

	// Get operating system and version
	body += "Operating System" + LINE_SEPARATOR
	body += "----------------" + LINE_SEPARATOR
	body += "OS name: " + System.properties['os.name'] + LINE_SEPARATOR
	body += "OS version: " + System.properties['os.version'] + LINE_SEPARATOR
	body += "OS architecture: " + System.properties['os.arch'] + LINE_SEPARATOR

	File fileRelease = new File("/proc/version")
	String systemVersion = fileRelease.getText("UTF-8")
	body += "OS details: " + systemVersion + LINE_SEPARATOR

	// Get all JVM properties
	body += "JVM system properties:" + LINE_SEPARATOR
	body += "----------------------" + LINE_SEPARATOR
	System.properties.each { key, value -> 
		body += "${key}=${value}" + LINE_SEPARATOR
	}

	message.setBody(body)

	return message
}

/**
 * getSimpleExpression
 * Gets simple expression.
 * @param simpleExpression: Simple expression
 * Execution mode: Single value
 * @return expression result.
 */
private def String getSimpleExpression(Message message, String simpleExpression) {
	Exchange ex = message.exchange

	def result = SimpleBuilder.simple(simpleExpression).evaluate(ex, String)
	return result.toString()
}