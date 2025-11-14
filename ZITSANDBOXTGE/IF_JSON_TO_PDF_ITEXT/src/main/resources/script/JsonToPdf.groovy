import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.UnitValue
import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

def Message processData(Message message) {
    Reader reader = message.getBody(Reader)
    def input = new JsonSlurper().parse(reader)
    def items = input.Order.Items.findAll { it.Valid }
    OutputStream outstream = new ByteArrayOutputStream()
    PdfWriter writer = new PdfWriter(outstream)
    PdfDocument pdf = new PdfDocument(writer)
    Document document = new Document(pdf)
    Table table = new Table(UnitValue.createPercentArray(3))
    table.useAllAvailableWidth()
    table.addHeaderCell('Item number')
    table.addHeaderCell('Product code')
    table.addHeaderCell('Quantity')
    items.each { item ->
        table.addCell(item.ItemNumber.padLeft(3, '0') as String)
        table.addCell(item.MaterialNumber as String)
        table.addCell(item.Quantity as String)
    }
    document.add(table)
    document.close()
    message.setBody(outstream.toByteArray())
    return message
}