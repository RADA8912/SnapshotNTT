import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    map = message.getHeaders();
    def top = map.get("top");
	def skip = map.get("skip");
	def personIdExternal = map.get("personIdExternal");
	def effectiveDate = map.get("effectiveDate");

    def param = "";
	
	if ( (personIdExternal != null && personIdExternal != "") && (effectiveDate == null || effectiveDate != "") ) {
		//param = param + "&$filter=personIdExternal eq '${property.personIdExternal}'";
        //param = param + "&\$filter=personIdExternal eq '" + personIdExternal + "' and ('20193' eq emailNav/emailType or emailNav eq null) and employmentNav ne null";
        param = param + "&\$filter=personIdExternal eq '" + personIdExternal + "'";
        //if (effectiveDate != null && effectiveDate != "") {
        //    param = param + " and lastModifiedDateTime ge datetimeoffset'" + effectiveDate + "T00:00:00Z'";
        //}
	} else {
        if (effectiveDate != null && effectiveDate != "") {
            //param = param + "&\$filter=lastModifiedDateTime ge datetimeoffset'" + effectiveDate + "T00:00:00Z' and ('20193' eq emailNav/emailType or emailNav eq null) and employmentNav ne null";
            param = param + "&\$filter=lastModifiedDateTime ge datetimeoffset'" + effectiveDate + "T00:00:00Z' and personalInfoNav/startDate ge datetimeoffset'1900-01-01T00:00:00Z' and (employmentNav/startDate gt datetimeoffset'1900-01-01T00:00:00Z')";
        } else {
            //param = param + "&\$filter=('20193' eq emailNav/emailType or emailNav eq null) and employmentNav ne null";
            param = param + "&\$filter=personalInfoNav/startDate ge datetimeoffset'1900-01-01T00:00:00Z' and (employmentNav/startDate gt datetimeoffset'1900-01-01T00:00:00Z')";
        }
        if ( (top != null) && (top != "") ) {
            //param = param + "&$top=${property.top}";
            param = param + "&\$top=" + top;
        }
        if (skip != null && skip != "") {
            //param = param + "&$skip=${property.skip}";
            param = param + "&\$skip=" + skip;
        }
    }
	
	message.setProperty("ODataParam", param);
	
    return message;
}