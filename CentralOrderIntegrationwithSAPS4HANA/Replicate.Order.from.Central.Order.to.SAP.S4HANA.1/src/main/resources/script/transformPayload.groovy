import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.transform.Field;
import groovy.json.JsonSlurper;
import groovy.json.JsonOutput;

//Address Types in OMF
@Field String SHIP_TO_KEY_OMF = 'SHIP_TO';
@Field String BILL_TO_KEY_OMF = 'BILL_TO';
@Field String SOLD_TO_KEY_OMF = 'SOLD_TO';

//Partner Types in S4
@Field String SOLD_TO_KEY_S4 = 'AG';
@Field String SHIP_TO_KEY_S4 = 'WE';
@Field String BILL_TO_KEY_S4 = 'RE';

//Transform customer structure of inbound payload to facilitate field mapping
def Message transformAddresses(Message message) {
	
	def body = message.getBody(java.lang.String) as String;
	//Parse message body as JSON
	def bodyJson = new JsonSlurper().parseText(body);
	//Save Partner Id
	String fulfillmentSystemReferenceId = bodyJson.customer.fulfillmentSystemReference ? bodyJson.customer.fulfillmentSystemReference.id : "";
	String externalSystemReferenceId = bodyJson.customer.externalSystemReference ? bodyJson.customer.externalSystemReference.externalId : "";
	// Use fulfillmentSystemReference->id if available; otherwise fallback to externalSystemReference->externalId
	String soldToPartnerId = fulfillmentSystemReferenceId ?: externalSystemReferenceId;
	boolean isSoldToAddressPresent = false;
	def soldToNode = [:];
	
	//First loop through "customer->addresses" to:
	//1 - Transform address type from OMF to the corresponding partner type of S4
	//2 - Add partnerId to "address" block
	//3 - Verify if sold-to address is present
	bodyJson.customer.addresses.each { address ->
		//convert OMF addressType key to S/4HANA partner type key, and add partnerId
		if (address.addressType == SHIP_TO_KEY_OMF) {
			address.addressType = SHIP_TO_KEY_S4;
			address.partnerId = soldToPartnerId;
		}
		if (address.addressType == BILL_TO_KEY_OMF) {
			address.addressType = BILL_TO_KEY_S4;
			address.partnerId = soldToPartnerId;
		}
		if (address.addressType == SOLD_TO_KEY_OMF) {
			address.addressType = SOLD_TO_KEY_S4;
			address.partnerId = soldToPartnerId;
			//Set flag if sold-to address is present
			isSoldToAddressPresent = true;
		}
	}

	//Verify if this is a registered user scenario, or CPD/guest scenario
	boolean isCPDOrGuest = (bodyJson.customer.guest == true) || (bodyJson.customer.oneTimeCustomer == true);
	
	//In the CPD/guest scenario:
	//since a deviating address is mandatory in S4, check if sold-to address is present;
	//if not, apply fallback logic to add address fields to a newly created sold-to node based on the bill-to address provided
	if (!isSoldToAddressPresent && isCPDOrGuest) {
		//Loop through addresses again to copy the bill-to address to a newly created sold-to node
		bodyJson.customer.addresses.each { address ->
			if (address.addressType == BILL_TO_KEY_S4) {
				soldToNode.street = address.street;
				soldToNode.houseNumber = address.houseNumber;
				soldToNode.poBox = address.poBox;
				soldToNode.postalCode = address.postalCode;
				soldToNode.city = address.city;
				soldToNode.country = address.country;
				soldToNode.district = address.district;
				soldToNode.state = address.state;
				soldToNode.phone = address.phone;
				soldToNode.email = address.email;
				soldToNode.fax = address.fax;
				soldToNode.correspondenceLanguage = address.correspondenceLanguage;
				if (address.person != null) {
					def personNode = [:];
					personNode.firstName = address.person.firstName;
					personNode.middleName = address.person.middleName;
					personNode.lastName = address.person.lastName;
					personNode.salutationCode = address.person.salutationCode;
					soldToNode.put("person", personNode);
				} else {
					soldToNode.person = null;
				}
			}
		}
	}
	
	//For both registered user and CPD/guest scenarios:
	//since a sold-to partner is mandatory in S4, if the sold-to node does not already exist (in the original payload), do the following:
	//1 - Add the partner type and partner Id to the node
	//2 - Add the sold-to node to "customer->addresses"
	//Note: in the registered user case, an address is not required; only the partner type and partner number
	if (!isSoldToAddressPresent) {
		//add S4 partner type and partner Id fields to the sold-to node
		soldToNode.addressType = SOLD_TO_KEY_S4;
		soldToNode.partnerId = soldToPartnerId;
		//add sold-to node to "customer->addresses"
		bodyJson.customer.addresses.add(soldToNode);
	}
	
	//add ship-to (WE) node, if structure exists in inbound payload
	if (bodyJson.shipToParty != null) {
		def shipToNode = [:];
		//set partner ID
		String fulfillmentSystemReferenceIdShipTo = bodyJson.shipToParty.fulfillmentSystemReference ? bodyJson.shipToParty.fulfillmentSystemReference.id : "";
		String externalSystemReferenceIdShipTo = bodyJson.shipToParty.externalSystemReference ? bodyJson.shipToParty.externalSystemReference.externalId : "";
		// Use fulfillmentSystemReference->id if available; otherwise fallback to externalSystemReference->externalId
		shipToNode.partnerId = fulfillmentSystemReferenceIdShipTo ?: externalSystemReferenceIdShipTo;
		//set partner type
		shipToNode.addressType = SHIP_TO_KEY_S4;
		//set address fields
		shipToNode.street = bodyJson.shipToParty.address.street;
		shipToNode.houseNumber = bodyJson.shipToParty.address.houseNumber;
		shipToNode.poBox = bodyJson.shipToParty.address.poBox;
		shipToNode.postalCode = bodyJson.shipToParty.address.postalCode;
		shipToNode.city = bodyJson.shipToParty.address.city;
		shipToNode.country = bodyJson.shipToParty.address.country;
		shipToNode.district = bodyJson.shipToParty.address.district;
		shipToNode.state = bodyJson.shipToParty.address.state;
		shipToNode.phone = bodyJson.shipToParty.address.phone;
		shipToNode.email = bodyJson.shipToParty.address.email;
		shipToNode.fax = bodyJson.shipToParty.address.fax;
		shipToNode.correspondenceLanguage = bodyJson.shipToParty.address.correspondenceLanguage;
		if (bodyJson.shipToParty.person != null) {
			//set person fields
			def shipToPersonNode = [:];
			shipToPersonNode.firstName = bodyJson.shipToParty.person.firstName; 
			shipToPersonNode.middleName = bodyJson.shipToParty.person.middleName;
			shipToPersonNode.lastName = bodyJson.shipToParty.person.lastName;
			shipToPersonNode.salutationCode = bodyJson.shipToParty.person.salutationCode;
			shipToNode.put("person", shipToPersonNode);
		} else {
			shipToNode.person = null;
		}
		bodyJson.customer.addresses.add(shipToNode);
	}
		
	//Remove "fees" node if empty
	if (bodyJson.fees != null && bodyJson.fees.size < 1) {
		bodyJson.remove("fees");
	}
	
	message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(bodyJson)));
						
	return message;
}

def Message transformSourcingInfo(Message message) {
	
	def body = message.getBody(java.lang.String) as String;
	def bodyJson = new JsonSlurper().parseText(body);
	
	def deliveryNode = [:];
	deliveryNode.deliveryOption = null;
	def isSourcing = bodyJson.shipment == null ? false : true;
	
	//add sourcing information to each item
	bodyJson.orderItems.eachWithIndex { orderItem, index ->
		def sourcingNode = [:];
		sourcingNode.fulfillmentSite = [:];
		sourcingNode.fulfillmentSite.id = isSourcing ? bodyJson.shipment.fulfillmentSite.id : null;
		sourcingNode.fulfillmentSite.dropShipId = isSourcing ? bodyJson.shipment.fulfillmentSite.dropShipId : null;
		bodyJson.orderItems[index].put("sourcing", sourcingNode);
	}
	
	//add delivery information on header level
	if (isSourcing)
	{
		def delivery = bodyJson.shipment.delivery;
		if (delivery != null && delivery.deliveryOption != null) {
			deliveryNode.deliveryOption = delivery.deliveryOption;
		}
	}
	
	bodyJson.put("delivery", deliveryNode);
	message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(bodyJson)));	
	return message;
	
}