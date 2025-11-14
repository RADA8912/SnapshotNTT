import com.sap.it.api.mapping.*;

def String orderCode(String arg, MappingContext context) {
    return context.getHeader("orderCode");
}