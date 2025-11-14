import com.sap.it.api.mapping.*;
import java.time.*;

/**
* set globals
**/
class globals{
   
   //date conversion pattern
   static String sPattern = "yyyyMMdd";
   //current date
   static Date dNow = new Date();
   static Date dInvoice = new Date();
}


/**
 * Maps every context value to itself when not null, not NIL and not SUPPRESS - maybe the context queue will get shoter.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return context values without NIL and SUPPRESS.
 */
public def void deleteSuppressCustom(String[] contextValues, Output output, MappingContext context) {
    
    
	if (contextValues.length > 0) {
		contextValues.each {s->
			if(!(output.isSuppress(s))) {
				output.addValue(s)
			}
		}
	}
}

/**
Wenn Rechnungsmonat = aktueller Monat oder wenn Rechnungsmonat = Vormonat und aktueller Tag <=9 --> Rechnungsdatum 1:1, 
sonst wenn aktueller Tag <=9 und Rechnungsmonat < Vormonat --> Rechnungsdatum = 1. des Vormonats, 
sonst Rechnungsdatum = 1. des aktuellen Monats.
**/
def String checkPostingDate(String sInvoiceDate){
    
    // set invoice date object to given invoice date string
    globals.dInvoice = Date.parse(globals.sPattern, sInvoiceDate);

    //if invoice month == current month or invoice month == previous month and current day <= 9, invoice date 1:1
    if (dateValueHelper("MONTH", globals.dInvoice) == dateValueHelper("MONTH", globals.dNow) || (dateValueHelper("MONTH", globals.dInvoice) == dateValueHelper("PREV_MONTH", globals.dNow) && dateValueHelper("DAY", globals.dNow) <= 9)){
        return sInvoiceDate;
    } // if current day <= 9 and invoice month < previous month --> first of previous month
    else if (dateValueHelper("DAY", globals.dNow) <= 9 && dateValueHelper("MONTH", globals.dInvoice) < dateValueHelper("PREV_MONTH", globals.dNow)){
        //if PREV_MONTH > CURRENT_MONTH, we do have a year change, so year -1
        if (dateValueHelper("PREV_MONTH", globals.dNow) > dateValueHelper("MONTH", globals.dNow)){
            return ((dateValueHelper("YEAR", globals.dNow) - 1).toString() + monthFormatHelper(dateValueHelper("PREV_MONTH", globals.dNow)) + "01");
        }
        else {
            return ((dateValueHelper("YEAR", globals.dNow)).toString() + monthFormatHelper(dateValueHelper("PREV_MONTH", globals.dNow)) + "01");
        }
    } //invoice date == first of current month
    else{
        return ((dateValueHelper("YEAR", globals.dNow)).toString() + monthFormatHelper(dateValueHelper("MONTH", globals.dNow)) + "01");
    }
    
    return "19700101";
}

/**
* private function date value helper
*
* @param sValue (day, month, year)
* @param dDate (dateObject)
* @return integer
*/
private def dateValueHelper(sValue, date){

    //create calendar object to get gregorian time formats
    Calendar calendar = Calendar.getInstance();
    //set date object to calendar
    calendar.setTime(date);
    
    switch(sValue){

        case "DAY":
            return calendar.get(Calendar.DAY_OF_MONTH);
            break;

        case "MONTH":
            println(calendar.get(Calendar.MONTH));
            return calendar.get(Calendar.MONTH);
            break;

        case "PREV_MONTH":
            if (calendar.get(Calendar.MONTH) == 0){
                // 0 == January, 11 == December
                println("Month 11");
                return 11;
            }
            else{
                println(calendar.get(Calendar.MONTH) - 1);
                return calendar.get(Calendar.MONTH) - 1;
            }
            break;

        case "YEAR":
            return calendar.get(Calendar.YEAR);
            break;

        case "PREV_YEAR":
            return calendar.get(Calendar.YEAR -1);
            break;

        default:
            return 0;
            break;
    }
}

/**
* Value helper for printout in IDOC
* -month is zero based-
*/
private def String monthFormatHelper(int month){

    month++;
    String sMonth = month.toString();

    if (sMonth.length() == 1)
        return "0" + sMonth;
    else return sMonth;

    return "00";
}


/**
* Take a substring of the allocationNr when it is longer than 16 characters. 
* Important here is that the last 16 values needs to be taken.
*/
public String transformAllocationNumber (String allocNr){
    
    // get Length of the allocNr
    int length = allocNr.length();
    
    if (length > 16){
        int startPos = length - 16;
        return allocNr.substring(startPos, length);
        
    }
    
    else {
        return allocNr;
    }
}

/**
* func to load property from iflow context
*/
def String getProperty(String propertyName,MappingContext context)  { 
    String PropertyValue= context.getProperty(propertyName); 
    PropertyValue= PropertyValue.toString(); 
    return PropertyValue; 
    
}

/**
* read header vars
*/
def String getHeader(String headerName,MappingContext context) { 
    String HeaderValue = context.getHeader(headerName); 
    HeaderValue= HeaderValue.toString();

    return HeaderValue; 
    
}

/**
 * Check if data can be charaterized as a Payment. If the amtDoccurNet < 0 or payment_type is invoice payment the it's
 * not a payment. Otherwise underlying data is a payment
 * **/ 

public String isInvoiceOrCredit (String payment_type, String amtDoccurNet){

    //Define booleans to identify whether it is a Invoice or CreditNote
    boolean paymentType = payment_type.equals("Invoice Payment");
    double amtDoccurDouble = amtDoccurNet.toDouble()
    //Is it a credit note or an invoice?
    if (amtDoccurDouble < 0.0 || paymentType){
        return "true";
        //If not, it is a payment
    } else {
        return "false";
    }

}


/**
 * Identify which type of invocie the underlying data record presents. It differentiates within standard invoice or credit note,
 * asset invoices before capitalization, asset invocies after capitalization and payments. Based on its type
 * different GLAccounts need to be set. This method returns the type of payment coming from Spendesk. In case of asset
 * invoices before capitalization and payments, we directly return the GL Account number.
 * @param creditOrInvoice
 * @param firstAcquisition
 * @param costCenterBoolean
 * @return paymentType
 */
public String determinePaymentType(String creditOrInvoice, String firstAcquisition, String costCenterBoolean){

    //Convert to boolean for the comparisons
    boolean cred = creditOrInvoice.toBoolean()
    boolean first = firstAcquisition.toBoolean()
    boolean cost = costCenterBoolean.toBoolean()


     //It is an invoice or credit note, but not referred to assets.
    if (cred && !cost){
        return "CreditOrInvoice"


    }
    //It is an invoice or credit note, has its first acqusition date set and is referred to assets.
    else if (cred && cost && first) {
        return "1000200088"

    }
    //It is an invoice or credit note, has its first acqusition date is not set and is referred to assets.
    else if (cred && cost && !first) {
        return "Asset Invoice"

    }
    //Otherwise it is a payment
    else  {
        return "2004200010"

    }

}

/**
 * fun to check asset number
 */
public String checkAssetNumber (String assetNumber){


  
    //Check if null value is present
    int length = 0;
    if (assetNumber != "null"){
        length = assetNumber.length();
        if (length > 0){
            return "true";
           }
           
    } else {
        return 'false';
    }
    

    
   
}


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


/**
 * Maps every context value to itself when not null and not SUPPRESS - maybe the context queue will get shoter.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return context values without SUPPRESS.
 */
public def void deleteSuppress(String[] contextValues, Output output, MappingContext context) {
	if (contextValues.length > 0) {
		contextValues.each {s->
			if(s !=null && !output.isSuppress(s)) {
				output.addValue(s)
			}
		}
	}
}

/**
 * Produces empty value if argument is not empty and exists, SUPPRESS otherwise.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Context
 * @return context with values "" or SUPPRESS.
 */
public def void createIfExistsAndHasValue(String[] contextValues, Output output, MappingContext context) {
	if (contextValues != null && contextValues.length > 0) {
		for (int i = 0; i < contextValues.length; i++) {
			String value = contextValues[i]
			if (value != null && value.trim().length() > 0 && !output.isSuppress(value)) {
				output.addValue("")
			} else {
				output.addSuppress()
			}
		}
	} else {
		output.addSuppress()
	}
}


/**
 * Produces empty value if argument is not empty. First argument are context values.
 * Execution mode: All values of context
 *
 * @param contectValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return context with values "" or SUPPRESS.
 */
public def void createIfHasValue(String[] contextValues, Output output, MappingContext context) {
	if (contextValues != null && contextValues.length > 0) {
		for (int i = 0; i < contextValues.length; i++) {
			String value = contextValues[i]
			if (value != null && value.trim().length() > 0
					&& !output.isSuppress(value)) {
				output.addValue("")
			} else {
				output.addSuppress()
			}
		}
	}
}

/**
 * 
 * function to check amtDoccur value based on criteria given in input
 * */

public String evaluateAmtDoccurGeneralLedger(String paymentType, String amtDoccur){
    
    double amtDoccurValue = amtDoccur.toDouble();

    if(paymentType == "Invoice Payment" || paymentType == "Payment" || paymentType == "Reimbursement" && amtDoccurValue >= 0){
        return amtDoccurValue.toString();
    }else{
        return (amtDoccurValue * -1).toString();
    }
    
    return "0"
}

/**
 * 
 * function to check amtDoccur value based on criteria given in input
 * */

public String evaluateAmtDoccurVendor(String paymentType, String amtDoccur){
    
    double amtDoccurValue = amtDoccur.toDouble();

    if(paymentType == "Invoice Payment" || paymentType == "Payment" || paymentType == "Reimbursement" && amtDoccurValue >= 0){
        return (amtDoccurValue * -1).toString();
    }else{
        return amtDoccurValue.toString();
    }
    
    return "0"
}

/**
 * Determine logical system to filter out Santander, Lease Buyouts and Bank transfers
 */
public String checkLogicalSystem (String paymentMethod, String glAccount, String vendorNr, String allocNr, String costCenter){
    
        String rexExp = '^42830.*'

       if (paymentMethod == 'bank transfer cars' || glAccount == '540300' || vendorNr == 'dp_glkt5b75a_b' || allocNr.matches(rexExp) || glAccount == '135001' || glAccount == '590003'){

              return "SPENDVIN"
              
       } else {

             if (glAccount == '137070' && costCenter == '119920' ){
               
                return "SPENDVIN"
               
           }else{
               
               return "SPENDESK"
           }

       }

}
/**
 * Determine the tax amount based on the given information.  
 * If the tax code is 94 or 511, we need to overwrite the tax amount by calculating
 * VAT of 19%.
 * 
 * 
 */
public String evaluateTaxAmount(String paymentType, String amtDoccur, String taxCode, String taxAmount){

       
        double amtDoccurValue = amtDoccur.toDouble();
        double amtDoccurTax = taxAmount.toDouble();

        if(taxCode == '94' || taxCode == '511'){
            amtDoccurTax = (amtDoccurValue * 0.19);
        }

        if(paymentType == "Invoice Payment" || paymentType == "Payment" || paymentType == "Reimbursement" && amtDoccurValue >= 0){
            return amtDoccurTax.round(2).toString();
        }else{
            return (amtDoccurTax * -1).round(2).toString();
        }

        return "0"

        }