import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

def Message processData(Message message) {
    Reader reader = message.getBody(Reader)
    def jsonInput = new JsonSlurper().parse(reader)

    def headerMap = message.getHeaders();
    def city = headerMap.get("City")

    def tempKelvin = jsonInput.main.temp as String
    def tempCelsius = Math.round(Double.parseDouble(tempKelvin) - 273.15);


    def builder = new JsonBuilder()
    builder.Weather {
        'Orts-Infos' {
            'Ort' "${city}"
            'Plz' headerMap.get('ZipCode')
        }
        'Celsius' tempCelsius
        'Beschreibung' jsonInput.weather[0].description
    }

    message.setBody(builder.toString())

    return message

}