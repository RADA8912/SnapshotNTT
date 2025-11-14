import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.camel.impl.DefaultCamelContext
import org.osgi.framework.FrameworkUtil

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
	def profileBundle = bundleContext.getBundles().find() {it.getSymbolicName() == "com.sap.it.node.stack.profile"}
	body += "Build number: " + profileBundle.getVersion().toString() + LINE_SEPARATOR

	String tenantName = System.getenv('TENANT_NAME')
	if (tenantName == null || tenantName.length() == 0) {
		tenantName = System.properties['com.sap.it.node.tenant.name']
	}
	body += "Tenant name: " + tenantName + LINE_SEPARATOR

	String tenantID = System.getenv('IT_SYSTEM_ID')
	if (tenantID != null && tenantID.length() > 0) {
		tenantID = tenantID.split("-")[1]
	} else {
		String appUrl = System.getenv("HC_APPLICATION_URL")
		tenantID = appUrl.substring(8, appUrl.indexOf("ifl"))
	}
	body += "Tenant ID: " + tenantID + LINE_SEPARATOR
	if (System.getenv('CF_INSTANCE_INDEX') != null) {
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
	body += "Java version: " + System.getProperty('java.version') + LINE_SEPARATOR + LINE_SEPARATOR

	// Get supported Cloud Foundry environment variables
	String[] variableNames = null
	if (System.getenv('CF_INSTANCE_INDEX') != null) {
		body += "Cloud Foundry environment values:" + LINE_SEPARATOR
		body += "---------------------------------" + LINE_SEPARATOR
		variableNames = ["TENANT_NAME", "IT_SYSTEM_ID", "IT_TENANT_UX_DOMAIN", "TENANT_ID", "CF_INSTANCE_PORT", "SERVER_PORT", "VCAP_APPLICATION"]
	} else {
		body += "Neo environment values:" + LINE_SEPARATOR
		body += "-----------------------" + LINE_SEPARATOR
		variableNames = ["HC_ACCOUNT", "HC_APPLICATION", "HC_APPLICATION_URL", "HC_AVAILABILITY_ZONE", "HC_HOST", "HC_HOST_CERT", "HC_HOST_SVC", "HC_LANDSCAPE", "HC_LOCAL_HTTP_PORT", "HC_OP_HTTP_PROXY_HOST", "HC_OP_HTTP_PROXY_PORT", "HC_PROCESS_ID", "HC_REGION"]
	}
	for (int i = 1; i < variableNames.length; i++) {
		body += variableNames[i] + "=" + System.getenv(variableNames[i]) + LINE_SEPARATOR
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