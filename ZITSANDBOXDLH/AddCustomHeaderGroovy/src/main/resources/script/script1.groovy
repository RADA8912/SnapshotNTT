import com.sap.gateway.ip.core.customdev.util.Message;
import javax.xml.xpath.*
import javax.xml.parsers.DocumentBuilderFactory


def Message processData(Message message) {

    /**
     * Uses properties to set Custom Headers from XML Payload
     * property:    "CustomHeaders_xpaths"      example:    "/BOMHeader/BOMHeaderType/BillOfMaterial#/BOMHeader/BOMHeaderType/TechnicalObject"
     * Property:    "CustomHeaders_attributes"     example     "att1#att2"
     */

    def xml

    if(message.getProperty("___IDE___") == null){
        xml = message.getBody(String)
    } else {
        xml = message.getBody() as String
    }


    def properties = message.getProperties();

    def search_attributes = properties.get("CustomHeaders_attributes");
    def attribute_xpaths = properties.get("CustomHeaders_xpaths");

    def messageLog

    if(message.getProperty("___IDE___") == null){
        messageLog = messageLogFactory.getMessageLog(message);
    } else {
        System.out.println("LOG: " + message);
    }

    String[] search_attributes_array = search_attributes.split("#");
    String[] attribute_xpaths_array = attribute_xpaths.split("#");



    if(search_attributes_array.length == attribute_xpaths_array.length )
    {
        def xpath = XPathFactory.newInstance().newXPath()
        def builder     = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        def inputStream = new ByteArrayInputStream( xml.bytes )
        def records     = builder.parse(inputStream).documentElement


        for(int i=0; i<attribute_xpaths_array.length; i++)
        {
            String temp_value;


            temp_value = xpath.evaluate( attribute_xpaths_array[i], records )

            if(temp_value != null)
            {
                if(message.getProperty("___IDE___") == null){
                    messageLog.addCustomHeaderProperty(search_attributes_array[i], temp_value);
                } else {
                    System.out.println("setCustomHEader: " + search_attributes_array[i] + " = " + temp_value);
                }
            }

        }
    }

    return message;
}