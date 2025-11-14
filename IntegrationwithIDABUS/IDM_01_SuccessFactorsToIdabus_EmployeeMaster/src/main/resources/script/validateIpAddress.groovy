package src.main.resources.script

import com.sap.gateway.ip.core.customdev.util.Message

import java.util.HashMap


static HashSet<String> getSetOfValuesFromParam(String paramValue, String sep) {
    HashSet<String> values = new HashSet<String>()
    if (!paramValue && !sep) {
        return values
    }
    try {
        String[] tokens = paramValue.trim().split(sep)
        String[] tokensNorm = tokens.collect {
            it.trim()
        }.findAll {
            return it && it != ""
        }
        values.addAll(tokensNorm)
    } catch (Exception e) {
        // passed
    }
    return values
}

Message processData(Message message) {
    def map = message.getHeaders()
    def value = map.get("x-forwarded-for")
    
    Set<String> allowedIps = getSetOfValuesFromParam(message.getProperty("allowedIpAddresses"), ",")
    
    def list = value.split(',').collect{it as String};
    String clientActualIP = ""

    for (int i = 0; i < list.size(); i++) {
        clientActualIP = list[i]
        if (clientActualIP != null && allowedIps.size() > 0 && !allowedIps.contains(clientActualIP.trim())) {
            message.setProperty("ipAddressCheckFailed", "X")
            message.setProperty("errorMessage", "Your IP address " + clientActualIP + " is not in the allowed list")
            message.setProperty("httpResponseCode", "403")

            return message
        }
    }
    
    return message
}