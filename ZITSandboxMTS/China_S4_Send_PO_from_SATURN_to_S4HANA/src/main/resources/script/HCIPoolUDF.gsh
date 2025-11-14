import com.sap.it.api.mapping.*



def String getProperty(String propertyName,MappingContext context)

{

  String PropertyValue= context.getProperty(propertyName);

  PropertyValue= PropertyValue.toString();

  return PropertyValue;

}


def String getHeader(String headerName,MappingContext context)

{

  String HeaderValue = context.getHeader(headerName);

  HeaderValue= HeaderValue.toString();

  return HeaderValue;

}