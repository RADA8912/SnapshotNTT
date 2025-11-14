import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.*


def Message removeProductRootNode(Message message) {

	def body = message.getBody(java.lang.String) as String;
	
	if(body != null && !body.trim().isEmpty()){
		def bodyJson = new JsonSlurper().parseText(body);
		if (!bodyJson.product) {
		    throw new PhysicalProductFormatException("Message format is not as defined in physical-product.xsd. Root node 'product' is missing!")
		}
		def products = bodyJson.product;
		if (products.size == null) { // if there's only 1 product, put it in an array
			def product = products; 
			products = [];
			products.push(product);
		}
		message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(products)));
	}
	return message;
}

def Message extractIdsFromPayload(Message message) {

	def body = message.getBody(java.lang.String) as String;

	if(body != null && !body.trim().isEmpty()){
		def products = new JsonSlurper().parseText(body);
		if (products.size == 0) { // if there's only 1 product, put it in an array
			def product = products; 
			products = [];
			products.push(product);
		}
		def productIds = [];
		
		for(i=0; i<products.size; i++) {
			String productId = '{"id": "' + products[i].id + '"}';
			productIds.push(new JsonSlurper().parseText(productId));
		}
		message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(productIds)));
	}
	return message;
}

def Message updateProductVersion(Message message) {

	def body = message.getBody(java.lang.String) as String;

	if(body != null && !body.trim().isEmpty()) {
		def products = new JsonSlurper().parseText(body);
		if (products.size == 0) { 
			def product = products; 
			products = [];
			products.push(product);
		}
		def getProductsResponse = new JsonSlurper().parseText(message.getProperty("getProductsResponse") as String);

		for(response in getProductsResponse) {
			if(response.statusCodeValue == 200) {
				for(product in products) { 
					if(product.id == response.body.id) { 
						product.put("version", response.body.version);
						break;
					}
				}
			}
		}
		def updatedProducts = JsonOutput.toJson(products);
		message.setBody(JsonOutput.prettyPrint(updatedProducts));
	}
	return message;
}


def Message seperateFoundProducts(Message message) {
	
	def body = message.getBody(java.lang.String) as String;

	if(body != null && !body.trim().isEmpty()) {
		def products = new JsonSlurper().parseText(body);
		if (products.size == 0) { 
			def product = products; 
			products = [];
			products.push(product);
		}
		
		def getProductsResponse = new JsonSlurper().parseText(message.getProperty("getProductsResponse") as String);
		def foundProducts = [];
		def notFoundProducts = [];
		
		for(product in products) {
			def notFound = true;
			for(response in getProductsResponse) {
				if(response.statusCodeValue == 200 && response.body.id == product.id ) {  
					foundProducts.push(product);
					notFound = false;
					break;
				}
			}
			if(notFound) {
				notFoundProducts.push(product);
			}
		}
		message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(foundProducts)));
		message.setProperty("notFoundProducts", JsonOutput.prettyPrint(JsonOutput.toJson(notFoundProducts)));
	}
	return message;
}

// Exception
public class PhysicalProductFormatException extends Exception { 
	public PhysicalProductFormatException(String message) { 
		super(message);
	}
}
