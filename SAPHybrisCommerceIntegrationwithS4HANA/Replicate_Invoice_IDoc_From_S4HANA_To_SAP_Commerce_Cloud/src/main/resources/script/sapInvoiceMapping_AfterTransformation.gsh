import com.sap.it.api.mapping.*;

def String getOverAllTax(String header,MappingContext context)
{
    return context.getProperty("overAllTax").toString();
}

def String getGrandTotal(String header,MappingContext context)
{
    return context.getProperty("grandTotal").toString();
}

def String getAmount(String header,MappingContext context)
{
    return context.getProperty("amount").toString();
}

def String getB2bunit(String header,MappingContext context)
{
    return context.getProperty("b2bunit").toString();
}

def String getOrderNumber(String header,MappingContext context)
{
    return context.getProperty("orderNumber").toString();
}

def String getOrderDate(String header,MappingContext context)
{
    return context.getProperty("orderDate").toString();
}

def String getDocumentDate(String header,MappingContext context)
{
    return context.getProperty("documentDate").toString();
}

def String getDeliveryNumber(String header,MappingContext context)
{
    return context.getProperty("deliveryNumber").toString();
}

def String getDeliveryDate(String header,MappingContext context)
{
    return context.getProperty("deliveryDate").toString();
}

def String getTermsOfPayment(String header,MappingContext context)
{
    return context.getProperty("termsOfPayment").toString();
}

def String getTermsOfDelivery(String header,MappingContext context)
{
    String termsOfDelivery1 = context.getProperty("termsOfDelivery1").toString();
    String termsOfDelivery2 = context.getProperty("termsOfDelivery2").toString();
    return termsOfDelivery1+" "+termsOfDelivery2;
}

def String getSalesunit(String header,MappingContext context)
{
    String salesOrg = context.getHeader("salesOrg");
    String distChannel = context.getHeader("distChannel");
    String division = context.getHeader("division");
    //logic to remove leading zeros in B2bUnit
    String b2bUnit = String.valueOf(Integer.parseInt(context.getProperty("b2bunit").toString()));
    return b2bUnit+"_"+salesOrg+"_"+distChannel+"_"+division;
}

def String getDocumentNumber(String header,MappingContext context)
{
    return context.getProperty("documentNumber").toString();
}



