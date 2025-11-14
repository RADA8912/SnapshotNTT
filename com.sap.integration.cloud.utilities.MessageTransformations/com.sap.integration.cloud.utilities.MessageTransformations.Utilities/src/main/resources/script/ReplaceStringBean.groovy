import com.sap.gateway.ip.core.customdev.util.Message;

//ReplaceStringBean
//Version 1.0.0

def Message processData(Message message) {
    final String C_AUX_SEPARATOR = "|";
    Map<String, Object> props = message.getProperties();
    String separator = props.get("replace.separator");
    separator = separator != null && separator != '' ? separator : C_AUX_SEPARATOR;

    Map<String, Object> paramMap = props.findAll { key, value ->
        key.startsWith("replace.param");
    };
    String body = message.getBody(String);

    for (String temp : paramMap.values()){
        String[] aux = temp.tokenize(separator);
        String stringFind = aux[0];
        String stringReplace = aux[1];
        stringFind = stringValidation(stringFind);
        stringReplace = stringValidation(stringReplace);

        body = body.replaceAll(stringFind, stringReplace);
    }

    message.setBody(body);
    return message;
}

private static String stringValidation(String value){
    final String C_BLANK_STRING = "blankString";
    final String C_EMPTY_STRING = "emptyString";

    value = value == C_BLANK_STRING ? " " : value;
    value = value == C_EMPTY_STRING ? "" : value;

    return value;
}
