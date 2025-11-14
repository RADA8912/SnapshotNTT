import com.sap.it.api.mapping.MappingContext

// Lesen von Exchange Properties
def String getProperty(String propertyName, MappingContext context) {
    String propertyValue = context.getProperty(propertyName)
    return propertyValue?.toString()
}

// Lesen von Header Variablen
def String getHeader(String headerName, MappingContext context) {
    String headerValue = context.getHeader(headerName)
    return headerValue?.toString()
}
