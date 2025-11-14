/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/en-US/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/en-US/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;


def Message processData(Message message) {
    //Body
    def body = message.getBody();
    def property = message.getProperties();
    def flowname = property.get("Name");
    def desc = property.get("Description");
    def var = '{"query":"mutation ($input: BaseFactSheetInput!,$patches:[Patch]!){createFactSheet(input: $input, patches: $patches) {factSheet {id name description type}}}","variables":{"input":{"name":"'+flowname+'","id":"ID_ID","type":"Interface"},"patches":[{"op":"replace","path":"/description","value":"'+desc+'"}]}}';
 
    message.setBody(var);

    return message;
}