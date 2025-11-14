import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.JsonSlurper;
import groovy.json.JsonOutput;

def Message processData(Message message) {
    // Konstanten für die Akzeptanz und Ablehnungsniveaus
    final double ACCEPT_LEVEL = 0.95;
    final double DECLINE_LEVEL = 0.2;

    // Eingangs-Nachricht als JSON-String
    String jsonString = message.getBody(String.class);
    JsonSlurper slurper = new JsonSlurper();
    def parsedJson = slurper.parseText(jsonString);

    // Validierungsergebnis initialisieren
    def validationResult = [:]

    if (parsedJson.features.size() == 0) {
        validationResult.validation = 'NOT_CONFIRMED'
    } else {
        def address = parsedJson.features[0].properties

        if (address.rank.confidence >= ACCEPT_LEVEL) {
            validationResult.validation = 'CONFIRMED'
        } else if (address.rank.confidence < DECLINE_LEVEL) {
            validationResult.validation = 'NOT_CONFIRMED'
        } else {
            validationResult.validation = 'PARTIALLY_CONFIRMED'
            if (!address.rank.confidence_city_level || address.rank.confidence_city_level < ACCEPT_LEVEL) {
                validationResult.validation_details = 'CITY_LEVEL_DOUBTS'
            } else if (!address.rank.confidence_street_level || address.rank.confidence_street_level < ACCEPT_LEVEL) {
                validationResult.validation_details = 'STREET_LEVEL_DOUBTS'
            } else {
                validationResult.validation_details = 'BUILDING_NOT_FOUND'
            }
        }
    }

    // Validierungsergebnis als JSON zurück in die Nachricht schreiben
    message.setBody(JsonOutput.toJson(validationResult))
    return message
}
