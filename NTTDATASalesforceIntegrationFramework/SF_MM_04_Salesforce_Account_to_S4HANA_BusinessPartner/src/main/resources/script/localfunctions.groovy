import com.sap.it.api.mapping.*;


def String setBusinessPartnerAddressUrl(String bp_id, String address_id){
	return "A_BusinessPartnerAddress(BusinessPartner='" + bp_id + "',AddressID='" + address_id + "')" 
}

def String setAddressPhoneNumberUrl(String address_id, String person, String ordinal_num){
    return "A_AddressPhoneNumber(AddressID='" + address_id + "',Person='" + person + "',OrdinalNumber='" + ordinal_num + "')"
}

def String setBusinessPartnerUrl(String bp_id){
    return "A_BusinessPartner('" + bp_id +"')"
}

def void splitOrganizationName(String[] names, String[] numberToSplit, Output output, MappingContext context) {
    String name = names[0];
	int num = Integer.parseInt(numberToSplit[0]);
    int length = 40;
    for (int i = 0; i < num; i++) {
    	if (name.length() > i*length) {
        	int e = (name.length() > (i*length + length)? i*length+length: name.length());
        	output.addValue(name.substring(i*length, e));
    	} else {
    		//output.addSuppress();
    		output.addValue("");
    	}
    }
}

def void getNameAt(String[] names, String[] position, Output output, MappingContext context) {
	int num = Integer.parseInt(position[0]);
    output.addValue(names[num]);
}

//combinedStreet: "12345 Augusta Ave"
def void splitStreet(String[] combinedStreet, Output output, MappingContext context) {
    String[] parts = combinedStreet[0].split(" ")
    if ( parts != null && parts.size() > 1) {
        
        if (parts[0].isNumber()) {
            // street number
            output.addValue(parts[0])
            // street name
            output.addValue(combinedStreet[0].substring(parts[0].length()+1))
        } else {
            // street number
            output.addSuppress()
            // street name
            output.addValue(combinedStreet[0])
        }
    // single value
    } else {
        // street number
        output.addSuppress()
        // street name
        output.addValue(combinedStreet[0])
    }
}