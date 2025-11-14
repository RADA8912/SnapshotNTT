import com.sap.it.api.mapping.*
//Lesen von Header Variablen
def String setHeader(String instanceID,MappingContext context)
{
  String HeaderValue = context.setHeader("SAPInterfaceID__c",instanceID);
  return "";
}