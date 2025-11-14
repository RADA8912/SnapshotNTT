import com.sap.it.api.mapping.*;

//Add MappingContext as an additional argument to read or set Headers and properties.

def String customFunc(String firstName,String lastName,String fullNameFormat)
{
	String fullName="";
	if (fullNameFormat == "1")
	{
		fullName = firstName + " " + lastName;
	}
	else if (fullNameFormat == "2")
	{
		fullName = lastName + ", " + firstName;
	}
	else
	{
		fullName = firstName + " " + lastName;
	}
	return fullName; 
}
