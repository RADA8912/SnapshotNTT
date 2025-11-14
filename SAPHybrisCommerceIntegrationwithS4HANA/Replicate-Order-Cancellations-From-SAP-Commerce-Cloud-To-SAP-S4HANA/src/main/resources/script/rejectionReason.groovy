import com.sap.it.api.mapping.*;

def String getRejectionReason(String arg, MappingContext context){
	return (context.getProperty("rejectionReason") == "LateDelivery") ? "01" : "00";
}