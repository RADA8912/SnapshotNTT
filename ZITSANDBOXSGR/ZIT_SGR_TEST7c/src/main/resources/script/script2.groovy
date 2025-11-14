import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {

//    String version = Runtime.version()
    String version2 = Runtime.class.getPackage().getImplementationVersion()
    
    message.setBody(version2)

    return message
}

/**
private static int getVersion() {
    String version = System.getProperty("java.version")
    if(version.startsWith("1.")) {
        version = version.substring(2, 3)
    } else {
        int dot = version.indexOf(".")
        if(dot != -1) {
            version = version.substring(0, dot)
        }
    }
    return Integer.parseInt(version)
}
*/