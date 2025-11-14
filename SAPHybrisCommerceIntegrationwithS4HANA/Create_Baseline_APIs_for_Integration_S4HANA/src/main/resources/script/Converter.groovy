import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.*;
import java.util.HashMap;
import java.util.stream.Collectors;
import src.main.resources.script.*;
import static src.main.resources.script.IntegrationObjectItem.*;
import static src.main.resources.script.IntegrationType.*;
import static src.main.resources.script.IntegrationObject.*;
import static src.main.resources.script.IntegrationObjectItemAttribute.*;
import static src.main.resources.script.AttributeDescriptor.*;
import groovy.xml.StreamingMarkupBuilder;
import groovy.transform.Field

@Field String errorMessages="";
def Message processData(Message message) {
    def reader = message.getBody(java.io.Reader) ;
    def headers = message.getHeaders() as Map<String, Object>;
	def properties = message.getProperties() as Map<String, Object>;
	def propertiesAsString ="\n";
	properties.each{ it -> propertiesAsString = propertiesAsString + "${it}" + "\n" };
	def headersAsString ="\n";
	headers.each{ it -> headersAsString = headersAsString + "${it}" + "\n" };
	Data data=new Data(reader);
	errorMessages+=data.getErrorMessages();
	createPayLoad(data,message);
    return message;
}

def createPayLoad(Data data,Message message)
{
    Map<String, Map<String, List<String>>> mapOfImpex = data.getMapOfImpex();
    Map<String, List<String>> integrationObjectLinesMap = mapOfImpex.get("IO");
    Map<String, List<String>> integrationObjectItemLinesMap = mapOfImpex.get("IOI");
    Map<String, List<String>> integrationObjectItemAttributeLinesMap = mapOfImpex.get("IOIA");
    List<IntegrationObject> integrationObjectList = new ArrayList<>();
    String xml="";
    for (String key : integrationObjectLinesMap.keySet()) {
        String integrationObjectLine = integrationObjectLinesMap.get(key).get(0);
        List<String> integrationObjectItemLines = integrationObjectItemLinesMap.get(key);
        List<String> integrationObjectItemAttributeLines = integrationObjectItemAttributeLinesMap.get(key);
        integrationObjectItemLinesMap.remove(key);
        integrationObjectItemAttributeLinesMap.remove(key);
        IntegrationObject integrationObject = createIntegrationObject(integrationObjectLine, integrationObjectItemLines, integrationObjectItemAttributeLines);
        integrationObjectList.add(integrationObject);
    }

    if (integrationObjectList.size() > 0) {
         xml += convertIntegrationObjectsListToXMLPayLoad(integrationObjectList);
    }

    List<IntegrationObjectItem> integrationObjectItemList=new ArrayList<IntegrationObjectItem>();
    for(String key: integrationObjectItemLinesMap.keySet())
    {
        List<String> integrationObjectItemLines=integrationObjectItemLinesMap.get(key);
        List<String> integrationObjectItemAttributeLines=integrationObjectItemAttributeLinesMap.get(key);
        integrationObjectItemAttributeLinesMap.remove(key);
        integrationObjectItemList.addAll(createIntegrationObjectItemList(integrationObjectItemLines,integrationObjectItemAttributeLines));
        
    }
    if(integrationObjectItemList.size()> 0)
    {
        xml+=convertIntegrationObjectItemsListToXMLPayLoad(integrationObjectItemList);
    }
    List<IntegrationObjectItemAttribute> integrationObjectItemAttributesList=new ArrayList<IntegrationObjectItemAttribute>();
    for(String key: integrationObjectItemAttributeLinesMap.keySet())
    {
        List<String> integrationObjectItemAttributeLines=integrationObjectItemAttributeLinesMap.get(key);
        integrationObjectItemAttributesList.addAll(createIntegrationObjectItemAttributesFromImpex(null, integrationObjectItemAttributeLines));
        
    }
    if(integrationObjectItemAttributesList.size() >0)
    {
        xml+=convertIntegrationObjectItemAttributesListToXMLPayLoad(integrationObjectItemAttributesList);
    }
    
    message.setHeader("Content-Type", "application/xml" + "; charset=utf-8" );
    message.setHeader("errorMessages",errorMessages);
    message.setBody("<batchParts>"+xml+"</batchParts>");


}




def createIntegrationObject(String integrationObjectLine, List<String> integrationObjectItemLines, List<String> integrationObjectItemAttributeLines) {
    Map<String, IntegrationObjectItem> integrationObjectItemsMap = new HashMap<>();
    IntegrationObject integrationObject = createIntegrationObjectFromImpex(integrationObjectLine);
    if (integrationObjectItemLines!=null &&   integrationObjectItemLines.size() > 0) {
        List<IntegrationObjectItem> integrationObjectItemsList = createIntegrationObjectItemsFromImpex(integrationObjectItemsMap, integrationObjectItemLines);
        addIOItoAllIOI(integrationObjectItemsMap, integrationObjectItemsList);
        integrationObject.getItems().addAll(integrationObjectItemsList)
    }
    if (integrationObjectItemAttributeLines!=null && integrationObjectItemAttributeLines.size() > 0) {
        createIntegrationObjectItemAttributesFromImpex(integrationObjectItemsMap, integrationObjectItemAttributeLines);
    }
   return integrationObject;

}


def createIntegrationObjectItemList( List<String> integrationObjectItemLines, List<String> integrationObjectItemAttributeLines)
{
    List<IntegrationObjectItem> integrationObjectItemsList = null;
    Map<String, IntegrationObjectItem> integrationObjectItemsMap = new HashMap<>();
    if(integrationObjectItemLines!=null && integrationObjectItemLines.size()>0)
    {
        integrationObjectItemsList = createIntegrationObjectItemsFromImpex(integrationObjectItemsMap, integrationObjectItemLines);
        addIOItoAllIOI(integrationObjectItemsMap, integrationObjectItemsList);

        if(integrationObjectItemAttributeLines!=null && integrationObjectItemAttributeLines.size() > 0)
        {
            createIntegrationObjectItemAttributesFromImpex(integrationObjectItemsMap, integrationObjectItemAttributeLines);
        }

    }
    return integrationObjectItemsList;

}

def convertIntegrationObjectsListToXMLPayLoad(List<IntegrationObject> integrationObjectList) {
    def markupBuilder = new StreamingMarkupBuilder();
    def xml = markupBuilder.bind { builder ->
            integrationObjectList.each { integrationObject ->
                batchChangeSet {
                    batchChangeSetPart {
                        method('POST')
                        IntegrationObjects {
                            buildIntegrationObject(builder, integrationObject)
                        }
                    }
                }
            }

    }

    return xml;
}


def buildIntegrationObject(builder, IntegrationObject integrationObject) {
    return builder.IntegrationObject {
        code(integrationObject.getCode())
        integrationType {
            IntegrationType {
                code(integrationObject.getIntegrationType().getCode())
            }
        }
        items {
            integrationObject.getItems().each { integrationObjectItem -> buildIntegrationObjectItem(builder, integrationObjectItem) }
        }
    }
}


def convertIntegrationObjectItemsListToXMLPayLoad(List<IntegrationObjectItem> integrationObjectItemList) {
    def markupBuilder = new StreamingMarkupBuilder();
    def xml = markupBuilder.bind { builder ->
            integrationObjectItemList.each { integrationObjectItem ->
                batchChangeSet {
                    batchChangeSetPart {
                        method('POST')
                        IntegrationObjectItems {
                            buildIntegrationObjectItem(builder, integrationObjectItem)
                        }
                    }
                }
            }
    }

    return xml;
}


def buildIntegrationObjectItem(builder, IntegrationObjectItem integrationObjectItem) {
    return builder.IntegrationObjectItem {
        code(integrationObjectItem.getCode())
        root(integrationObjectItem.getRoot())
        type {
            ComposedType {
                code(integrationObjectItem.getType().getCode())
            }
        }
        integrationObject {
            IntegrationObject {
                code(integrationObjectItem.getIntegrationObject().getCode())
                items {
                    integrationObjectItem.getIntegrationObject().getItems().each { item ->
                        IntegrationObjectItem {
                            code(item.getCode())
                            root(item.getRoot())
                            type {
                                ComposedType {
                                    code(item.getType().getCode())
                                }
                            }
                            integrationObject {
                                IntegrationObject {
                                    code(item.getIntegrationObject().getCode())
                                }
                            }
                        }
                    }

                }
            }
        }
        attributes {
            integrationObjectItem.getAttributes().each { attribute -> buildIntegrationObjectItemAttribute(builder, attribute) }
        }
    }
}


def convertIntegrationObjectItemAttributesListToXMLPayLoad(List<IntegrationObjectItemAttribute> integrationObjectItemAttributesList) {
    def markupBuilder = new StreamingMarkupBuilder();
    def xml = markupBuilder.bind { builder ->
            integrationObjectItemAttributesList.each { integrationObjectItemAttribute ->
                batchChangeSet {
                    batchChangeSetPart {
                        method('POST')
                        IntegrationObjectItemAttributes {
                            buildIntegrationObjectItemAttribute(builder, integrationObjectItemAttribute)
                        }

                    }
                }

            }
    }
    return xml;
}


def buildIntegrationObjectItemAttribute(builder, integrationObjectItemAttribute) {
    return builder.IntegrationObjectItemAttribute {
        attributeName(integrationObjectItemAttribute.getAttributeName())
        unique(integrationObjectItemAttribute.isUnique())
        autoCreate(integrationObjectItemAttribute.isAutoCreate())
        attributeDescriptor {
            AttributeDescriptor {
                qualifier(integrationObjectItemAttribute.getAttributeDescriptor().getQualifier())
                enclosingType {
                    ComposedType {
                        code(integrationObjectItemAttribute.getAttributeDescriptor().getEnclosingType().getCode())
                    }
                }
            }
        }
        integrationObjectItem {
            IntegrationObjectItem {
                code(integrationObjectItemAttribute.getIntegrationObjectItem().getCode())
                integrationObject {
                    IntegrationObject {
                        code(integrationObjectItemAttribute.getIntegrationObjectItem().getIntegrationObject().getCode())
                    }
                }
            }
        }
        if (integrationObjectItemAttribute.getReturnIntegrationObjectItem() != null) {
            returnIntegrationObjectItem {
                IntegrationObjectItem {
                    code(integrationObjectItemAttribute.getReturnIntegrationObjectItem().getCode())
                    integrationObject {
                        IntegrationObject {
                            code(integrationObjectItemAttribute.getReturnIntegrationObjectItem().getIntegrationObject().getCode())
                        }
                    }
                }
            }
        }
    }
}


def createIntegrationObjectFromImpex(String integrationObjectLine) {
    String line = integrationObjectLine;
    String[] tokens = line.split(";");
    String code = tokens[1].trim();
    String integrationType = "INBOUND";
    if (tokens.length == 3) {
        //token[2] may not be present and "Inbound" is the default in such cases
        integrationType = tokens[2].trim();
    }
    IntegrationObject integrationObject = IntegrationObject.Builder.class.newInstance().withCode(code)
            .withIntegrationType(IntegrationType.Builder.class.newInstance().withCode(integrationType).build()).build();
    return integrationObject;
}





def createIntegrationObjectItemsFromImpex(Map<String, IntegrationObjectItem> integrationObjectItemsMap, List<String> integrationObjectItemLines) {
    String errorMessageIOI="";
    List<IntegrationObjectItem> integrationObjectItemsList = new ArrayList<>();
    integrationObjectItemLines.forEach { integrationObjectItemLine ->
  try{  
        String line = integrationObjectItemLine;
        String[] tokens = line.split(";");
        String code = tokens[2].trim();
        String integrationObjectCode = tokens[1].trim();
        String integrationTypeCode = tokens[3].trim();
        Boolean isRoot=false;
        if(tokens.length > 4)
        {
            isRoot = new Boolean(Boolean.valueOf(tokens[4].trim().toLowerCase()));
        }
        IntegrationObjectItem item = IntegrationObjectItem.Builder.class.newInstance().withCode(code)
                .withIntegrationType(IntegrationType.Builder.class.newInstance().withCode(integrationTypeCode).build())
                .withIntegrationObject(IntegrationObject.Builder.class.newInstance().withCode(integrationObjectCode).build())
                .withRoot(isRoot)
                .build();
        integrationObjectItemsList.add(item);
        integrationObjectItemsMap.put(code, item);
    }
    catch(Exception ex)
    {
         errorMessageIOI+="Exception while reading the IOIA line \n"+ integrationObjectItemLine +"\n";
    }
    }
    errorMessages+=errorMessageIOI;
    return integrationObjectItemsList;
}

def createIntegrationObjectItemAttributesFromImpex(Map<String, IntegrationObjectItem> integrationObjectItemsMap, List<String> integrationObjectItemAttributeLines) {
     String errorMessageIOIA="";
    List<IntegrationObjectItemAttribute> integrationObjectItemAttributesList = new ArrayList<>(); ;
    integrationObjectItemAttributeLines.forEach { integrationObjectItemAttributeLine ->
      try{
        String line = integrationObjectItemAttributeLine;
        String[] tokens = line.split(";");
        String[] x = tokens[1].trim().split(":");
        IntegrationObjectItem integrationObjectItem = IntegrationObjectItem.Builder.class.newInstance()
                .withIntegrationObject(IntegrationObject.Builder.class.newInstance().withCode(x[0].trim()).build())
                .withCode(x[1].trim())
                .withRoot(null)
                .build();
        String attributeName = tokens[2].trim();
        boolean isUnique = false;
        if(tokens.length >= 6 )
        {
            isUnique= Boolean.valueOf(tokens[5].trim());
        }
        boolean isAutoCreate = false;
        if (tokens.length == 7) {
            isAutoCreate = Boolean.valueOf(tokens[6].trim());
        }
        String[] y = tokens[3].trim().split(":");
        AttributeDescriptor attributeDescriptor = AttributeDescriptor.Builder.class.newInstance()
                .withQualifier(y[1].trim())
                .withIntegrationType(IntegrationType.Builder.class.newInstance().withCode(y[0].trim()).build())
                .build();
        IntegrationObjectItem returnIntegrationObjectItem = null;
        if (tokens.length>4 && tokens[4].trim().length() != 0) {
            String[] z = tokens[4].trim().split(":");
            returnIntegrationObjectItem = IntegrationObjectItem.Builder.class.newInstance().withCode(z[1])
                    .withIntegrationObject(IntegrationObject.Builder.class.newInstance().withCode(z[0]).build())
                    .withRoot(null)
                    .build();
        }
        String lookUpCode = x[1].trim();
        IntegrationObjectItemAttribute attribute = IntegrationObjectItemAttribute.Builder.class.newInstance()
                .withIntegrationObjectItem(integrationObjectItem)
                .withAttributeName(attributeName)
                .withAttributeDescriptor(attributeDescriptor)
                .withUnique(isUnique)
                .withAutoCreate(isAutoCreate)
                .build();
        if (returnIntegrationObjectItem != null) {
            attribute.setReturnIntegrationObjectItem(returnIntegrationObjectItem);
        }
        if (integrationObjectItemsMap!=null && integrationObjectItemsMap.size() > 0) {
            integrationObjectItemsMap.get(lookUpCode).getAttributes().add(attribute);
        }
        integrationObjectItemAttributesList.add(attribute);
    }
    catch(Exception ex)
    {
        errorMessageIOIA+="Exception while reading the IOIA line \n"+ integrationObjectItemAttributeLine +"\n";
    }
    }
    errorMessages+=errorMessageIOIA;
    return integrationObjectItemAttributesList;
}


def addIOItoAllIOI(Map<String, IntegrationObjectItem> integrationObjectItemsMap, List<IntegrationObjectItem> integrationObjectItemsList) {
    List<IntegrationObjectItem> innerItems = integrationObjectItemsMap.values().stream().map { integrationObjectItem -> new IntegrationObjectItem(integrationObjectItem) }.collect(Collectors.toList());
    integrationObjectItemsList.stream().forEach { integrationObjectItem -> integrationObjectItem.getIntegrationObject().getItems().addAll(innerItems) };
}









