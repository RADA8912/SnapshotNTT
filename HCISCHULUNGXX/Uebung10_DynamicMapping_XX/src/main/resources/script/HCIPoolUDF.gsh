import com.sap.aii.mapping.api.*; 
import com.sap.it.api.mapping.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;




def String getProperty(String propertyName,MappingContext context)

{

  String PropertyValue= context.getProperty(propertyName);

  PropertyValue= PropertyValue.toString();

  return PropertyValue;

}


def String getHeader(String headerName,MappingContext context)

{

  String HeaderValue = context.getHeader(headerName);

  HeaderValue= HeaderValue.toString();

  return HeaderValue;

}

public void PartyRoleCodeStarts(String[] var1, Output result) {
		
		//This code checks if party role Code starts with Z or not. This is required to check if party is customDefined party or not
		// In ERP customer can create multiple Z parties of standar parties which will be part of custom define parties in C4C

		for (int i=0; i<var1.length;i++){
		boolean output  = var1[i].startsWith("Z");
		if( output ) {
		 result.addValue(true);
		}
		else{
		  result.addSuppress();
		}

		}
}

public void CreateItemParty(String[] var1,String[] var2, Output result) {
		/*Create an output node where each element = var2[0] in the input array
		var2[0] = can be WE, AP, AG etc...
		*/
		for (int i=0;i<var1.length;i++)
		{	if (var1[i].equals(var2[0])){
					result.addValue("");
			}
		}
}

public void CreatePartyAddressNode(String[] var1,String[] var2,String[] var3, Output result) {
		/*Create AddressNode if PARTN_ROLE is WE and the ADDR_ORIG is B
		*/
		for (int i=0;i<var1.length;i++)
		{
		if (var1[i].equals(var3[0])){   // PARTN_ROLE = WE
				if (var2[i].equals("B"))  //ADDR_ORIG = B
					result.addValue("");   // Create a new Address node
			else
				result.addSuppress(); // Create a new Address node but Suppressed 
			}
		}
}

public void CalculatePartyAddressNodeElements(String[] var1,String[] var2,String[] var3,String[] var4, Output result) {
		/*Function to calculate  how many ProductRecepientAddressNodeElements should be created based on 
		if PARTN_ROLE is WE and ADDR_ORIG is B create an temp element with value ADDRESS
		if PARTN_ROLE is WE and ADDR_ORIG is not equal to B, create an temp element with empty value
		The output of this function is given to function CreateItemProductRecepientAddressNodeElements which creates the target element values
		*/
		for (int i=0;i<var1.length;i++)
		{
		if (var1[i].equals(var4[0])){
				if (var2[i].equals("B"))
						result.addValue(var3[i]);				
			else
				result.addValue("");
			}
		}
}

public void CreatePartyAddressNodeElements(String[] var1,String[] var2,String[] var3, Output result) {
		/*
		This Queue based function gets input from CalculateItemProductRecepientAddressNodeElements (var1) and from the Source element which needs to be mapped in var2
		(example COUNTRY), 
		and ADDR_NO (var3) from E101BAPIADDR1
		Here we are comparing the ADDRESS from //E101COD_S_SLS_ITEM_DATA/E102BAPIPARNR with ADDR_NO from header 
		/COD_REPLICATE_SALES_ORDER01/IDOC/E101COD_S_REP_SLSORD_DATA/E101BAPIADDR1
		if the Address no matches we populate the corresponding item element in the address node for Item party address
		*/
		for (int i=0;i<var1.length;i++){ // for no of  elements in CalculateItemProductRecepientAddressNodeElements
			if( var1[i].equals(""))   // if the element value is empty
					result.addValue(ResultList.SUPPRESS); // create the element with SUPPRESS
			else {
					for (int j=0;j<var3.length;j++){ // Compare with all ADDR_NO in the Header E101BAPIADDR1
						if (var3[j].equals(var1[i])){
								if (var2[j].equals(""))
											result.addSuppress();
								else
								result.addValue(var2[j]);
								break;
					}
				}
			}
		}

}


