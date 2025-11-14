import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper;

def Message processData(Message message) {
	def body = message.getProperty("CreateResponse")
    def error_msg
    
    //In case there was a create scenario 
    if(body != null){   
    //creating the error message that needs to be sent to C4C outbound 
	body.batchChangeSetResponse.each{
		if(it.batchChangeSetPartResponse.statusCode.text() != "201"){ //201 is for create
			if(error_msg == null){
				error_msg = 'StatusCode: ' + it.batchChangeSetPartResponse.statusCode.text() + ' ,Status: ' + it.batchChangeSetPartResponse.statusInfo.text() + ' ,Error Message: ' + it.batchChangeSetPartResponse.body.text() + " ; "
			}
			else{
				error_msg = error_msg + 'StatusCode: ' + it.batchChangeSetPartResponse.statusCode.text() +' Status: ' + it.batchChangeSetPartResponse.statusInfo.text() + ' ,Error Message: ' + it.batchChangeSetPartResponse.body.text() + " ; "
			}
		}
	}
	
	//Throwing the exceptions to C4C
	if ( error_msg != null){
		throw new Exception(error_msg[0..-2])
	}
	else{
		return message
	}
    }
    else{
        return message
    }
}
