import groovy.xml.*;
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
/**
*
*   function to merge list of line items with sap bp id filled in hubspot customer into original message.
*
*/
def Message processData(Message message) {
    
    //define properties
    def properties = message.getProperties();

    //load original payload from property
    def stripePayoutsXML = new XmlParser().parseText(properties.get("fullPayload"));
    def lineItemsWithCustomer = new XmlParser().parseText(message.getBody(String));
    
    //iterate through stripe_paypout header segments to get payoutID
    for (int payout = 0; payout < stripePayoutsXML.stripe_payouts.size(); payout++){
        
        //payout from current loop run
        String currentPayoutID = stripePayoutsXML.stripe_payouts[payout].stripe_payout_id.text();

        //find all lineitems with that payoutID
        def matchedLineItems = lineItemsWithCustomer.items.findAll { it.stripe_payout_id.text() == currentPayoutID };
        
        //since it is not possible to replace a list of nodes with another list of nodes
        //we need to make sure matchedLineItems list has the same number of items as the matching stripe_payout header segment
        if (stripePayoutsXML.stripe_payouts[payout].items.size() == matchedLineItems.items.size()){

            //iterate through header segment and replace node
            //this can be done with the same counter as the list is the same size and should be in the same order as the original message
            for (int counter = 0; counter < matchedLineItems.size(); counter++){
                
                //always remove first node because we append at the end
                stripePayoutsXML.stripe_payouts[payout].remove(stripePayoutsXML.stripe_payouts[payout].items[0]);
                stripePayoutsXML.stripe_payouts[payout].append(matchedLineItems[counter]);
                
                 // check if the amount is not negative, then save the business partner as the one used in KA1
                if (matchedLineItems[counter].amount.text().toFloat() >= 0){
                   message.setProperty("debitorBP", matchedLineItems[counter].business_partner.text());
                }
            }
       }
    }
   message.setBody(XmlUtil.serialize(stripePayoutsXML));
   return message;
}