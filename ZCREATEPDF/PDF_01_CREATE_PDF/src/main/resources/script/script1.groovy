import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap

Message processData(Message message) {
    // Body als String holen
    def body = message.getBody(String) as String
    // Split bei ### oder -
    def lines = body.split(/###/)

    // Liste für die formatierten Absätze
    def formattedParagraphs = []

    // Jede Zeile einzeln behandeln
    lines.each { line ->
        // Whitespace bereinigen
        line = line.trim()

        while (line.length() > 0) {
            if (line.length() <= 120) {
                formattedParagraphs.add("0 -15 Td")
                formattedParagraphs.add("(${line}) Tj")
                
                break
            }

            // Suche das letzte Leerzeichen vor der 80-Zeichen-Grenze
            int breakIndex = line.lastIndexOf(' ', 120)
            if (breakIndex == -1) {
                breakIndex = 120 // kein Leerzeichen gefunden, harter Umbruch
            }

            def segment = line.substring(0, breakIndex).trim()
            formattedParagraphs.add("0 -15 Td")
            formattedParagraphs.add("(${segment}) Tj")

            // Restliche Zeile weiterverarbeiten
            line = line.substring(breakIndex).trim()
        }
    }

    // Neuen Body mit Zeilenumbrüchen erstellen
    def newBody = formattedParagraphs.join("\n")

    // Body setzen
    message.setBody(newBody)
    return message
}
