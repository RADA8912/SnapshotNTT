/*
 * 
 * 
 */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.util.Properties;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

def Message processData(Message message) {

	prop = message.getProperties();
	head = message.getHeaders();
	
	// Initialize Parameters
 	String tempDate = null;
 	Date advanceDate = null;
 	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
 	String offset = prop.get("AdvancedReplicationPeriod");
 	tempDate = prop.get("TEMP_LAST_MODIFIED_ON");
 	
	Date today = new Date();
	Date currentDatePlusOffset = today;
 	
 	message.setProperty("ADVDATEOFFSET","");
  	message.setProperty("TODATEOFFSET","");
  	message.setProperty("CURDATEOFFSET","");
  	message.setProperty("DATEOFFSET","");
  	
  	if(offset == "")
  	{
  		offset=0;
  	}
  	if(tempDate.contains('Z'))
  	{
  		tempDate=tempDate.replace('Z','');
  	}
  
 	//Check if no offset is selected in process properties 
 	if(offset == "Default" || offset.contains('{'))
 	{
 		if(null != tempDate)
 		{
 			advanceDate = dateFormat.parse(tempDate) + 1;
	 		message.setProperty("DATEOFFSET",currentDatePlusOffset.format("yyyy-MM-dd"));
	 		currentDatePlusOffset = currentDatePlusOffset + 1;
	 		message.setProperty("ADVDATEOFFSET",advanceDate.format("yyyy-MM-dd'T'HH:mm:ss"));
	  		message.setProperty("CURDATEOFFSET",currentDatePlusOffset.format("yyyy-MM-dd'T'HH:mm:ss"));
	  		message.setProperty("TODATEOFFSET",currentDatePlusOffset.format("yyyy-MM-dd"));
	  	}
 	}
 	 //If offset is selected in process properties
 	 else{
	if(null != tempDate)
	{
	  if(null != offset && offset != "")
	  {
		advanceDate = dateFormat.parse(tempDate) + Integer.valueOf(offset) + 1;
		currentDatePlusOffset = currentDatePlusOffset + Integer.valueOf(offset);
		message.setProperty("DATEOFFSET",currentDatePlusOffset.format("yyyy-MM-dd"));
		currentDatePlusOffset = currentDatePlusOffset + 1;
		message.setProperty("CURDATEOFFSET",currentDatePlusOffset.format("yyyy-MM-dd'T'HH:mm:ss"));
		message.setProperty("ADVDATEOFFSET",advanceDate.format("yyyy-MM-dd'T'HH:mm:ss"));
		message.setProperty("TODATEOFFSET",currentDatePlusOffset.format("yyyy-MM-dd"));
	  }
	}
  }
  return message;
}