import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

def Message processData(Message message) {
	
//Body 
	def body = message.getBody();
	
	StringBuffer str = new StringBuffer();
	//Headers 
	def pMap = message.getProperties();
	def company_ext = pMap.get("company");
	def employee_class_ext = pMap.get("employee_class");
	def company_territory_code_ext = pMap.get("company_territory_code");
	def business_unit_ext = pMap.get("business_unit");
	def location_ext = pMap.get("location");
	def pay_group_ext = pMap.get("pay_group");
	def person_id_external_ext = pMap.get("person_id_external");
	def last_modified_on = pMap.get("TEMP_LAST_MODIFIED_ON");
	def date_off_set = pMap.get("DATEOFFSET");
	def adv_date_off_set = pMap.get("ADVDATEOFFSET");
	def cur_date_off_set = pMap.get("CURDATEOFFSET");
	
	/*if(!(person_id_external_ext.trim().isEmpty()))
	{
		def filter1='&$filter=';
		filter1= filter1+"employmentNav/personIdExternal IN (" + person_id_external_ext+")";
		filter1=filter1+"&asOfDate ='" +date_off_set+ "'";	
		str=filter1;
	}
	else*/
	if(last_modified_on != null)
	{
		str.append("eventNav/externalCode eq 'H'");
		str.append(" and ((lastModifiedDateTime ge datetimeoffset'"+last_modified_on+"'");
		str.append(" and createdDateTime ge datetimeoffset'"+last_modified_on+"')");
		str.append(" or ((startDate ge datetime'"+adv_date_off_set+"'");
		str.append(" and startDate lt datetime'"+cur_date_off_set+"')");
		str.append(" and (employmentNav/personNav/personalInfoNav/startDate ge datetime'"+adv_date_off_set+"'");
		str.append(" and employmentNav/personNav/personalInfoNav/startDate le datetime'"+cur_date_off_set+"')))");
		str.append("&asOfDate="+date_off_set);
		//str.append("&fromDate="+date_off_set);
		//str.append("&toDate="+date_off_set);
	}
	
	

	message.setProperty("QueryFilter",str.toString());
	
	return message;

}

