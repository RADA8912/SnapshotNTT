import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
    
    def properties = message.getProperties();
    def receiverPartner = properties.get("ReceiverPartner");
    
    def receiverClient;
    def receiverCredentials;
    
    switch (receiverPartner) {
        case "P40CLNT200":
        case "Q40CLNT200":
            receiverClient = 200;
            receiverCredentials = properties.get("RcvCredentialsDataLayer");
            break;
        case "D40CLNT200":
            receiverClient = 200;
            receiverCredentials = properties.get("DevRcvCredentialsDataLayer");
            break;
        case "P40CLNT100":
        case "Q40CLNT100":
            receiverClient = 100;
            receiverCredentials = properties.get("RcvCrededentials");
            break;
        case "D40CLNT100":
            receiverClient = 100;
            receiverCredentials = properties.get("DevRcvCredentials");
            break;
    }
    
    message.setProperty("ReceiverClient", receiverClient);
    message.setProperty("ReceiverCredentials", receiverCredentials);
    
    return message;
}