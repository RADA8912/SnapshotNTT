import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.*

// Methode zur Verarbeitung der Nachricht
Message processData(Message message) {
    
    // XML-Inhalt als String aus der Nachricht holen
    def body = message.getBody(String)

    // Erstellen des XML-Parsers (Namespaces bleiben deaktiviert)
    def parser = new XmlParser(false, false) // Namespaces werden deaktiviert
    def xml = parser.parseText(body)

    // Liste der <entry>-Elemente sammeln
    def entries = xml.'entry'

    // Builder für das Ergebnis
    StringBuilder result = new StringBuilder()

    // Durch jedes <entry>-Element iterieren
    entries.each { entry ->
        def properties = entry.'m:properties'
        if (properties != null) {
            // Liste der extrahierten Werte innerhalb eines <entry>-Knotens
            def values = []
            properties.each { prop ->
                prop.children().each { property ->
                    values.add(property.text()) // Wert zur Liste hinzufügen
                }
            }
            // Füge die Werte mit Komma getrennt zur Ausgabe hinzu
            result.append(values.join(","))
        }
        result.append("\n") // Zeilenumbruch nach jedem Eintrag
    }

    // Setze das Ergebnis als neuen Nachrichtentext
    message.setBody(result.toString().trim())

    return message
}
