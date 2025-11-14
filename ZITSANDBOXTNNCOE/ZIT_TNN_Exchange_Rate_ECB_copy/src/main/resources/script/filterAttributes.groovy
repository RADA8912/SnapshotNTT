import com.sap.it.api.mapping.*

def void filterAttributes(String[] attributes, String[] values, String[] attributeToLocate, Output output, MappingContext context) {
    def searchFor = attributeToLocate[0]
    def foundIndex = Arrays.asList(attributes).indexOf(searchFor)
    def value = foundIndex == -1 ? "default value" : values[foundIndex]
    output.addValue(value)
}