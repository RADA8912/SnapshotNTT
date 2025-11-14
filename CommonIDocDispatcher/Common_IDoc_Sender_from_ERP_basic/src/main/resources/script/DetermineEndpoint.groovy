import com.sap.gateway.ip.core.customdev.util.Message
import com.sap.it.api.ITApiFactory
import com.sap.it.api.mapping.ValueMappingApi

Message processData(Message message) {
    Map properties = message.getProperties()
    def receiverPartnerNumber = properties.get('RCVPRN')
    def receiverPartnerType = properties.get('RCVPRT')
    def messageType = properties.get('MESTYP')
    def messageCode = properties.get('MESCOD')
    def messageFunction = properties.get('MESFCT')

    def sourceValue = "${receiverPartnerNumber}|${receiverPartnerType}|${messageType}|${messageCode}|${messageFunction}"

    ValueMappingApi api = ITApiFactory.getService(ValueMappingApi, null)
    def processDirectEndpoint = api.getMappedValue('ECC', 'IDocKey', sourceValue, 'CPI', 'Endpoint')
    if (processDirectEndpoint)
        message.setProperty('processDirectEndpoint', processDirectEndpoint)
    else
        message.setProperty('processDirectEndpoint', 'Invalid')
    
    return message
}