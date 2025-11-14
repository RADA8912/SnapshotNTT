import com.sap.it.api.mapping.*;

/**
 * Uses the first argument (the context should have exactly one value) as often as the length of the second context indicates.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param secondContext Second context
 * @param output Output
 * @param context Mapping Context
 * @return Return the second context filled with the value from the first argument.
 */
public def void simpleUseOneAsMany(String[] contextValues, String[] secondContext, Output output, MappingContext context) {
	if (contextValues != null && contextValues.length > 0 && secondContext != null && secondContext.length > 0) {
		String value = null
		if (contextValues.length == 1) {
			value = contextValues[0]
		} else {
			for (int i = 0; i < contextValues.length; i++) {
				if (!output.isSuppress(contextValues[i])) {
					value = contextValues[i]
					break
				}
			}
			if (value == null) {
				value = contextValues[0]
			}
		}
		for (int i = 0; i < secondContext.length; i++) {
			output.addValue(value)
		}
	}
}

/*
*
* format epoch time to human readable time yyyyMMdd
*
*/
def String formatDatefromEpochTime(String epochTime){
	Date stripeDate = new Date(epochTime.toLong() * 1000);
	return (stripeDate.format("yyyyMMdd")).toString(); 
}

/*
*
* Used to add 7 days to epoch time
*
*/
def String epochTimePlus7d(String epochTime){
	long time = epochTime.toLong() + 604800;
	return time.toString();
}

/*
*
* Used to add 1 days to epoch time
*
*/
def String epochTimePlus1d(String epochTime){
	long time = epochTime.toLong() + 86400;
	return time.toString();
}

/*
*
* Used to add 1 month to epoch time
*
*/
def String epochTimePlus1M(String epochTime){
	long time = epochTime.toLong() + 2629743;
	return time.toString();
}

/*
*
* helper function to get property
*
*/
def String getProperty(String propertyName, MappingContext context) {
    def propertyValue = context.getProperty(propertyName);
    return propertyValue;
}

/*
*
* item text helper 'Subscription ' & deal_id & PSTNG_DATE (MM) & '/' & PSTNG_DATE (YYYY)
*
*/
def String createGLitemText(String dealId, String finalizedDate, String periodStartDate) {
    
    String monthFromPeriodStart = (formatDatefromEpochTime(epochTimePlus1d(periodStartDate))).substring(4,6);
    
    String yearFromFinalized = (formatDatefromEpochTime(finalizedDate)).substring(0,4);
    
    return "Subscription " + dealId + " " + monthFromPeriodStart + "/" + yearFromFinalized;
}


/*
*
* item text helper 'Subscription ' & deal_id & invoice_finalized_at (MM+1) & '/' & invoice_finalized_at (YYYY)
*
*/
def String createGL2itemText(String dealId, String finalizedDate) {
    
    String monthFinalized = (formatDatefromEpochTime(epochTimePlus1M(finalizedDate))).substring(4,6);
    
    String yearFromFinalized = (formatDatefromEpochTime(epochTimePlus1M(finalizedDate))).substring(0,4);
    
    return "Subscription " + dealId + " " + monthFinalized + "/" + yearFromFinalized;
}

/*
*
* item text helper 'Subscription ' & deal_id & PSTNG_DATE (MM) & '/' & PSTNG_DATE (YYYY)
*
*/
def String createGL3itemText(String dealId, String finalizedDate, String periodStartDate) {
    
    String monthFromPeriodStart = (formatDatefromEpochTime(epochTimePlus1d(periodStartDate))).substring(4,6);
    
    String yearFromFinalized = (formatDatefromEpochTime(finalizedDate)).substring(0,4);
    
    return "Subscription " + dealId + " " + monthFromPeriodStart + "/" + yearFromFinalized;
}


/*
*
* calc amount gross | = amount_gross / 1,19 * ( contract_start_date(DD) - 30 ) / 30
*
*/
def String calcAmountGross(String epochTime, float amount){
	
	long time = epochTime.toLong(); 
	
	Date contractStartDate = new Date(time * 1000);
	
	Calendar calendar = Calendar.getInstance();
    calendar.setTime(contractStartDate);

    int contractStartDay = calendar.get(Calendar.DAY_OF_MONTH);
    
    float amount_gross = (amount / 1.19) * ((contractStartDay - 30) / 30);
	
	return amount_gross.toString();
}

/*
*
* calc amount gross | = amount_gross / -1,19 * (contract_start_date(DD) / 30)
*
*/
def String calcAmountGross2(String epochTime, float amount){
	
	long time = epochTime.toLong(); 
	
	Date contractStartDate = new Date(time * 1000);
	
	Calendar calendar = Calendar.getInstance();
    calendar.setTime(contractStartDate);

    int contractStartDay = calendar.get(Calendar.DAY_OF_MONTH);
    
    float amount_gross = (amount / (-1.19)) * (contractStartDay / 30);
	
	return amount_gross.toString();
}

/*
*
* calc amount gross | = amount_gross / 1,19 * (contract_start_date(DD) / 30)
*
*/
def String calcAmountGross3(String epochTime, float amount){
	
	long time = epochTime.toLong(); 
	
	Date contractStartDate = new Date(time * 1000);
	
	Calendar calendar = Calendar.getInstance();
    calendar.setTime(contractStartDate);

    int contractStartDay = calendar.get(Calendar.DAY_OF_MONTH);
    
    float amount_gross = (amount / 1.19) * (contractStartDay / 30);
	
	return amount_gross.toString();
}



/*
If the amount of CRM positions exceed the amount of INV positions, special behavior needs to be applied.
In the specific case rhe following applies: E1EDKA1-PARVW (2 segments): customer is BE, company code is AG (i.e. their roles change).
*/

def String specialPARVWAssignment (String specialBehaviour, String partnerIndicator){
    
    def PARW = new String()
    
    if (specialBehaviour == 'true' && partnerIndicator == 'BE'){
        PARW =  'AG'
    } else if (specialBehaviour == 'true' && partnerIndicator == 'AG'){
        PARW = 'BE'
    } else {
        PARW = partnerIndicator
    }
    
    return PARW
    
}
/*
If the amount of CRM positions exceed the amount of INV positions, special behavior needs to be applied.
In the specific case rhe following applies: E1IDPU1-DOCNAME: CRM becomes INV, INV becomes CRM
*/
def String specialDocNameAssignment (String specialBehaviour, String docName){
    def docResult = new  String()
    if (specialBehaviour == 'true' && docName == 'CRM'){
        docResult = 'INV'
    } else if (specialBehaviour == 'true' && docName == 'INV'){
        docResult = 'CRM'
    } else {
        docResult = docName
    }
    
    return docResult
    
}
