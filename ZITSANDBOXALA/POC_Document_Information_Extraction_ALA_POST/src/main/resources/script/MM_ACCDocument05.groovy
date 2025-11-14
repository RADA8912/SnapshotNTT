import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import groovy.json.JsonSlurper

Message processData(Message message) {
    def body = message.getBody(String)
    def json = new JsonSlurper().parseText(body)

    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)

    def itemCounter = 1
    def totalTax = 0.0
    def totalNetAmount = 0.0

    xml.ACC_DOCUMENT {
        // Get the taxAmount from headerFields (headerFields contains a list of fields with taxAmount)
        def headerTaxAmounts = json.extraction.headerFields?.findAll { it.name == "taxAmount" }

        // Sum up all the taxAmounts from the headerFields section
        headerTaxAmounts.each { taxItem ->
            totalTax += taxItem.value?.toDouble() ?: 0.0
        }

        def lineItems = json.extraction.lineItems ?: []
        lineItems.eachWithIndex { item, index ->
            def getField = { key -> item.find { it.name == key }?.value }

            def description = getField("description") ?: "N/A"
            def netAmount = getField("netAmount")?.toDouble() ?: 0.0
            def unitPrice = getField("unitPrice")?.toDouble() ?: 0.0
            def taxAmount = 0.0

            // Extract taxAmount from each line item (if any)
            def taxAmountItems = item.findAll { it.name == "taxAmount" }
            taxAmountItems.each { taxItem ->
                taxAmount += taxItem.value?.toDouble() ?: 0.0
            }

            def taxRate = getField("taxRate")?.toDouble() ?: 0.0

            // GL Segment
            def glItemNo = String.format("%06d", itemCounter++)
            E1BPACGL09 {
                ITEMNO_ACC(glItemNo)
                GL_ACCOUNT("400000")
                COMP_CODE("1000")
                ITEM_TEXT("Item ${index + 1} revenue: ${description}")
            }

            // CR for GL
            E1BPACCR09 {
                ITEMNO_ACC(glItemNo)
                COMP_CODE("1000")
                GL_ACCOUNT("400000")
                CURRENCY("EUR")
                AMT_DOCCUR(String.format("%.2f", netAmount))
                ITEM_TEXT("CR for GL ${description}")
            }

            // AR Segment
            def arItemNo = String.format("%06d", itemCounter++)
            E1BPACAR09 {
                ITEMNO_ACC(arItemNo)
                COMP_CODE("1000")
                CUSTOMER("100200")
                ITEM_TEXT("Item ${index + 1} customer: ${description}")
            }

            // CR for AR
            E1BPACCR09 {
                ITEMNO_ACC(arItemNo)
                COMP_CODE("1000")
                CUSTOMER("100200")
                CURRENCY("EUR")
                AMT_DOCCUR(String.format("%.2f", unitPrice))
                ITEM_TEXT("CR for AR ${description}")
            }

            // Aggregating tax for all items (taxAmount from line items)
            if (taxAmount > 0) {
                totalTax += taxAmount
                totalNetAmount += netAmount
            }
        }

        // If any tax amount was collected (from headerFields or line items), create a single tax segment
        if (totalTax > 0) {
            def taxItemNo = String.format("%06d", itemCounter++)
            E1BPACTX09 {
                ITEMNO_ACC(taxItemNo)
                COMP_CODE("1000")
                AMT_DOCCUR(String.format("%.2f", totalTax))  // Aggregate tax amount for all items + header tax
                ITEM_TEXT("Total tax amount for all items and header")
            }
        } else {
            // Handle case where no tax was found
            println "No tax amounts were found in the document."
        }
    }

    // Set the response body to the generated XML
    message.setBody(writer.toString())
    return message
}
