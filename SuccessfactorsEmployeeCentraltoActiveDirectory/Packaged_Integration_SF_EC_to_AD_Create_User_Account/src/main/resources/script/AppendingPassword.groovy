/* The authencation password driven from external parameters is appended to header as per Active Drectory validating design.
*The password associated as a header should be converted to unicode byte array format. 
 */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import javax.xml.bind.DatatypeConverter;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

def Message processData(Message message) 
{
	def prop = message.getProperties();
	Attributes attributes = new BasicAttributes();
	String quotedPassword=prop.get("Password");
    byte[] unicodePasswordByteArray = quotedPassword.getBytes("UTF-16LE");
	attributes.put(new BasicAttribute("unicodePwd", unicodePasswordByteArray));
	message.setHeader("SAP_LDAPAttributes",attributes);
	return message;
}

