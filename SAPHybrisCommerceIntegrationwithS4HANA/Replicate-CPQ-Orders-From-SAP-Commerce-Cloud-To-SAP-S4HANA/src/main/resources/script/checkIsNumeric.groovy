import com.sap.it.api.mapping.*;

def String checkIsNumeric(String partnerId){
    
    if(partnerId.isNumber())
    {
        def mz = 10 - partnerId.length();
    for(i = 0; i < mz ; i++){
        partnerId = 0 + partnerId;
    }
        return partnerId;
    }
	return partnerId;
}