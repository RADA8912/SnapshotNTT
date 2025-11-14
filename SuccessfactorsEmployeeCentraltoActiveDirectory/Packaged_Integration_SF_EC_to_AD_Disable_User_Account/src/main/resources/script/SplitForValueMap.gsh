import com.sap.it.api.mapping.*;

//Add MappingContext as an additional argument to read or set Headers and properties.

def String customFunc(String value,String index)
{
	String returnValue="";
	if(value != null)
	{
		String[] strArr = value.split(";"); 
		returnValue = strArr[Integer.parseInt(index)];
	} 
	return returnValue 
}
