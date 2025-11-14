import com.sap.it.api.mapping.*;


def String getExchangePropertyValue(String propertyValue, MappingContext context) {
         
         return context.getProperty(propertyValue);
}

def String getserviceRequestID(String propertyValue, MappingContext context) {
         
         def serviceRequestID = propertyValue.split('#');         
         return serviceRequestID[1];
}