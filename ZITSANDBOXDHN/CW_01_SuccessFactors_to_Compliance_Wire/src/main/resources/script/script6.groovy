import com.sap.gateway.ip.core.customdev.util.Message;
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat
import java.lang.Object
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.codec.digest.DigestUtils;
import com.sap.it.api.securestore.SecureStoreService;
import com.sap.it.api.securestore.UserCredential;
import com.sap.it.api.securestore.exception.SecureStoreException;
import com.sap.it.api.ITApiFactory;


def Message processData(Message message) {
def body = message.getBody(java.lang.String) as String

//************* Hash do Body using apache commons DigestUtils sha256Hex ************* 
def hashBody = DigestUtils.sha256Hex(body)

//************* Mapping the properties - The filename was set in previous groovyScript ************* 
def map = message.getProperties()
def headers = message.getHeaders()

String region = map.get('AWS_Region')
String service = map.get('AWS_Service')
String bucket = map.get('AWS_Bucket')
String directory = map.get('AWS_Directory')

//************* Iniciating variables *************    
String method = "PUT";    
//String host = "ds-idap-qas-work.s3.ap-northeast-1.amazonaws.com";
String host = bucket+"."+service+"."+region+".amazonaws.com";
//String region = "ap-northeast-1";    
//String service = "s3";
//String endpoint = "s3.ap-northeast-1.amazonaws.com";
String endpoint = service+"."+region+".amazonaws.com";

// Read AWS access key from security artifacts. Best practice is NOT to embed credentials in code.

String secureParameterAlias = map.get("GetUserCredential.Alias")
def secureStorageService = ITApiFactory.getService(SecureStoreService.class, null)
def secureParameter = secureStorageService.getUserCredential(secureParameterAlias)
def password = secureParameter.getPassword().toString()
def username = secureParameter.getUsername().toString()

def access_key = username
def secret_key = password

// Create a date for headers and the credential string
def now = new Date()
def amzFormat = new SimpleDateFormat( "yyyyMMdd'T'HHmmss'Z'" )
def formattedDate = new SimpleDateFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'")
def stampFormat = new SimpleDateFormat( "yyyyMMdd" )
def amzDate = amzFormat.format(now)
def date_stamp = stampFormat.format(now)

//************* Canonical Request variables ************* 
//String canonical_uri = "/dsi_it_dataintegration/GLMS/GLMS_VAL/source/HRIS/DSE/"+headers.get('FileName');
String canonical_uri = directory+headers.get('FileName');
String canonical_querystring = "";
String canonical_headers = "host:" + host + "\n"+ "x-amz-content-sha256:" + hashBody + "\n" + "x-amz-date:" + amzDate + "\n";
String signed_headers = "host;x-amz-content-sha256;x-amz-date";
String canonical_request = method + "\n" + canonical_uri + "\n" + canonical_querystring + "\n" + canonical_headers + "\n" + signed_headers + "\n" + hashBody;

//************* Sing to Sing variables ************* 
String algorithm = "AWS4-HMAC-SHA256";
String credential_scope = date_stamp + "/" + region + "/" + service + "/" + "aws4_request";
String string_to_sign = algorithm + "\n" +  amzDate + "\n" +  credential_scope + "\n" + DigestUtils.sha256Hex(canonical_request);

//************* Generating the Singning Key ************* 
byte[] signing_key = getSignatureKey(secret_key, date_stamp, region, service);

//************* Generating the HmacSHA256 - Amazon ************* 
byte[] signature = HmacSHA256(string_to_sign,signing_key);

//************* Generating the Hex of the Signature ************* 
String strHexSignature = bytesToHex(signature);

//************* Generating the authorization header signed - Amazon V4 S3 Bucket ************* 
String authorization_header = algorithm + " " + "Credential=" + access_key + "/" + credential_scope + ", " +  "SignedHeaders=" + signed_headers + ", " + "Signature=" + strHexSignature;

//************* Seting the headers of HTTP call ************* 
message.setHeader("x-amz-date",amzDate);
message.setHeader("x-amz-content-sha256", hashBody)
message.setHeader("Authorization", authorization_header);
message.setHeader("Host", bucket+"."+service+"."+region+".amazonaws.com");
message.setHeader("content-type", "text/plain");

//************* Setting the body to be store in Amazon ************* 
message.setBody(body)
return message
}
//************* Function bytes to Hex ************* 
String bytesToHex(byte[] bytes) {
    char[] hexArray = "0123456789ABCDEF".toCharArray();           
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars).toLowerCase();
}
//************* Function HmacSHA256 ************* 
byte[] HmacSHA256(String data, byte[] key) throws Exception {
    String algorithm="HmacSHA256";
    Mac mac = Mac.getInstance(algorithm);
    mac.init(new SecretKeySpec(key, algorithm));
    return mac.doFinal(data.getBytes("UTF8"));
}
//************* Function getSignature ************* 
byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception {
    byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
    byte[] kDate = HmacSHA256(dateStamp, kSecret);
    byte[] kRegion = HmacSHA256(regionName, kDate);
    byte[] kService = HmacSHA256(serviceName, kRegion);
    byte[] kSigning = HmacSHA256("aws4_request", kService);
    return kSigning;
}

