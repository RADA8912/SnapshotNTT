import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    String path = ''
//    path = System.getenv("PATH")
    String osName = System.properties['os.name']
    String tenantName = System.env['TENANT_NAME']
    String tenantUX = System.env['IT_TENANT_UX_DOMAIN']
    
    message.setBody(tenantName + "\r\n" + tenantUX + "\r\n" + osName)

    return message
}