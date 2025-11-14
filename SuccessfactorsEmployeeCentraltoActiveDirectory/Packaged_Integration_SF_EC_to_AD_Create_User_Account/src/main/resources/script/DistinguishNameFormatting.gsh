import com.sap.it.api.mapping.*;

//Add MappingContext as an additional argument to read or set Headers and properties.

def String customFunc(String middleName,String lastName,String firstName,String commonNameFormat,String personIdExternal)
{
//Based on the process property option selection for common name formatting,the CN is constructed.

String commonName="";
if (commonNameFormat == "1")
{
  commonName = firstName + " " + lastName;
}
else if (commonNameFormat == "2")
{
  commonName = firstName+ " " + middleName + " " + lastName;
} 
else if (commonNameFormat == "3")
{
  commonName = lastName + " " + firstName;
} 
else if (commonNameFormat == "4")
{
  commonName = lastName + " " + firstName + " " + middleName;
} 
else if (commonNameFormat == "5")
{
  commonName = personIdExternal;
} 
else if (commonNameFormat == "6")
{
  commonName = firstName + "." + lastName;
}
else
{
	commonName = firstName + " " + lastName;
}
	return commonName; 
}
