import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {

    message.setProperty("newProperty", runningOnCloudFoundry().toString())

    return message
}

private boolean runningOnCloudFoundry() {
    return System.getenv('VCAP_APPLICATION') != null
}