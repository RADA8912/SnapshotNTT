import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
    // Body
    def body = message.getBody(String) as String;

    // JSON-Struktur manuell aufbauen
    def outPutJson = """[
        {
            "params": [{
                "acronym": "payload",
                "operator": "EQUAL",
                "value":" $body "
            }]
        }
    ]""";

    message.setBody(outPutJson);

    return message;
}
