import com.sap.gateway.ip.core.customdev.util.Message;
def Message processData(Message message) {

    String body = message.getBody(String);
    body = body.replace('sbxs24.solco.global.nttdata.com:8000', 'ntt-pi-dev.it-cpi001-rt.cfapps.eu10.hana.ondemand.com:443/http/odata/s24/passthrough/')
    body = body.replace('sbxs24.solco.global.nttdata.com:44300', 'ntt-pi-dev.it-cpi001-rt.cfapps.eu10.hana.ondemand.com:443/http/odata/s24/passthrough/')
    
    message.setBody(body);
    return message;
}