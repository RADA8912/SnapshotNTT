import com.sap.it.api.mapping.*;

def String getClassificationClass(String header,MappingContext context)
{
    return context.getProperty("CLASS");
}

def String getClassificationType(String header,MappingContext context)
{
    return context.getProperty("KLART");
}