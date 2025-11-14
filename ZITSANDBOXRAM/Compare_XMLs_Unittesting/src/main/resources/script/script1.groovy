import com.sap.gateway.ip.core.customdev.util.Message
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.dom.DOMSource
import org.xmlunit.builder.DiffBuilder
import org.xmlunit.diff.Diff
import org.xmlunit.diff.DifferenceEvaluators

import org.xmlunit.diff.DefaultNodeMatcher
import org.xmlunit.diff.ElementSelectors
import org.xmlunit.diff.DifferenceEvaluator
import org.xmlunit.diff.ComparisonResult

Message processData(Message message) {
    
    def result
    // Lese das eingehende XML (transformiertes XML) aus der Nachricht
    def expectedXml = message.getBody(String)

    // Erwartetes XML wird als Property oder Header hinzugefügt
    def inputXml = message.getProperty("expectedXml") // Beispiel: Lade erwartetes XML

    // Konvertiere die XML-Strings in Dokumente für den Vergleich
    def dbFactory = DocumentBuilderFactory.newInstance()
    dbFactory.setNamespaceAware(true)

    def inputDoc = dbFactory.newDocumentBuilder().parse(new ByteArrayInputStream(inputXml.bytes))
    def expectedDoc = dbFactory.newDocumentBuilder().parse(new ByteArrayInputStream(expectedXml.bytes))

    // Custom DifferenceEvaluator zur Behandlung von SIMILAR-Unterschieden
    DifferenceEvaluator customEvaluator = { comparison, outcome ->
        if (outcome == ComparisonResult.SIMILAR) {
            return ComparisonResult.EQUAL // Behandle SIMILAR wie EQUAL
        }
        return outcome
    } as DifferenceEvaluator

    // Erstelle den XML-Vergleich mit XMLUnit
    Diff xmlDiff = DiffBuilder.compare(new DOMSource(expectedDoc))
            .withTest(new DOMSource(inputDoc))
            .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndAllAttributes))
            .withDifferenceEvaluator(DifferenceEvaluators.chain(DifferenceEvaluators.Default, customEvaluator))
            .ignoreWhitespace()
            .ignoreComments()
            .checkForSimilar() // checkForSimilar ist wichtig, um auch Ähnlichkeiten zu prüfen
            .build()

    // Ergebnis des Vergleichs
    if (xmlDiff.hasDifferences()) {
        def differences = xmlDiff.getDifferences()
                .findAll { it.getResult() == ComparisonResult.DIFFERENT } // Nur DIFFERENT berücksichtigen
                .collect { difference ->
                    def comparison = difference.getComparison()
                    def controlDetails = comparison.getControlDetails()
                    def testDetails = comparison.getTestDetails()
                    
                    // Extrahiere die XPath-Positionen und Werte
                    def controlXPath = controlDetails.getXPath()
                    def controlValue = controlDetails.getValue() ?: "null"
                    def testXPath = testDetails.getXPath()
                    def testValue = testDetails.getValue() ?: "null"

                    // Ausgabe im gewünschten Format
                    return "Difference at:\n" +
                           "Control XPath: ${controlXPath}\nControl Value: ${controlValue}\n" +
                           "Test XPath: ${testXPath}\nTest Value: ${testValue}"
                }
                .join("\n\n") // Trenne einzelne Unterschiede optisch

        result = differences ? "Differences found:\n\n" + differences : "No significant differences found."
        
        
    } else {
        result = "The XMLs are identical."
       
    }

    message.setBody(result)

    return message
}
