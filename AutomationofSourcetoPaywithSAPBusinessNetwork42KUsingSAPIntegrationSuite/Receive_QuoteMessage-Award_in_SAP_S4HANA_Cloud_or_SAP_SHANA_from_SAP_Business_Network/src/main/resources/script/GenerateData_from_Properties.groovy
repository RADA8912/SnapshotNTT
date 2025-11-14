import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.*

def Message processData(Message message) {
	
	def properties = message.getProperties()
	doctype = message.getHeader('SAP_MessageType', String)
	
	crossref = properties.findAll{it.key.startsWith('_crossref_') && it.value != ''}.collectEntries{ [it.key.substring(10),it.value] }
	if (crossref) message.setProperty('intPackageCrossRef',generate_crossref());
	
	lookup = properties.findAll{it.key.startsWith('_lookup_') && it.value != ''}.collectEntries{ [it.key.substring(8),it.value] }
    if (lookup) message.setProperty('intPackageLookup',generate_lookuptable());

	//message.setProperty('anTextUOM',generate_uom())
    return message
	
}

//Handler functions for different message contents
String generate_crossref() {
	
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)

    xml.ParameterCrossReference(){
        ListOfItems(){
            Item{
				DocumentType("$doctype")
                crossref.each{
					"${it.key}"("${it.value}")
                }
            }
        }
    }
    return writer.toString()
}

String generate_lookuptable() {
	
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)

    xml.LOOKUPTABLE(){
        Parameter(){
            DocumentType("$doctype")
            Name("${lookup.Name}")
            ProductType("${lookup.ProductType}")
            ListOfItems(){
                Item{
                    Name("${lookup.ItemName}")
                    Value("${lookup.ItemValue}")
                }
            }
        }
    }
    return writer.toString()
}

String generate_uom() {
	
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)

    xml.UOM(){
        ListOfItems(){
            Item{
                uom.each{
					"${it.key}"("${it.value}")
				}
            }
        }
    }
    return writer.toString()
}