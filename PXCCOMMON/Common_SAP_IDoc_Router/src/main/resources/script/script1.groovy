import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.mapping.ValueMappingApi;

def Message processData(Message message) {
    //Get IDoc EDI_DC40 information from properties
    map = message.getProperties();
    cimtyp = map.get("CIMTYP");
    idoctyp = map.get("IDOCTYP");
    mesfct = map.get("MESFCT");
    mestyp = map.get("MESTYP");
    sndprn = map.get("SNDPRN");
    rcvprn = map.get("RCVPRN");
    
    //Build ProcessDirect address
    def address = "/$sndprn/$rcvprn/$mestyp"
    
    if (mesfct != '') {
        address += "_$mesfct"
    }
    
    address += "/$idoctyp"
    
    if (cimtyp != '') {
        address += "_$cimtyp"
    }
    
    //Check if there is an entry within the Value Mapping.
    def helperValMap = ITApiFactory.getApi(ValueMappingApi.class, null);
    def keyReceiver = helperValMap.getMappedValue('IDoc', 'EDI_DC40_INFO', address , 'CPI', 'ProcessDirectAddr');
    if (keyReceiver != null) {
        address = keyReceiver
    }
    
    //Set ProcessDirect address to properties
    message.setProperty("address", address)

    return message;
}