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
    def LeanIX_InterfaceID = property.get("LeanIX_InterfaceID");
    def LeanIX_ITComponent = "2d7f3646-136b-469a-b11d-923a3720df10";
    
    def var = '{"query":"mutation ($patches: [Patch]!) {updateFactSheet(id: \\"'+LeanIX_InterfaceID+'\\", patches: $patches) {factSheet {id displayName ... on Interface {relInterfaceToITComponent { edges { node { id }}}}}}}","variables":{"patches":[{"op":"add","path":"/relInterfaceToITComponent/new_1","value":"{\\"factSheetId\\":\\"'+LeanIX_ITComponent+'\\"}"}]}}';
    message.setBody(var);

    return message;
}