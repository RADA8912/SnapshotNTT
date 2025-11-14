
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    //In case of exception at the HTTP Adapter level, retrive the cached exchage headers from Property
    def headersFromCache = message.getProperty("Original_Exchange_Headers");
    if (headersFromCache != null)
    {
        for (header in headersFromCache)  {
            message.setHeader(header.getKey().toString(), header.getValue())
        }
    }

    return message;
}
