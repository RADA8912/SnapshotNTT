import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import de.itelligence.mappings.mf.ItelliMappingIDocInvoic02_to_IDocInvoicGenCombined
import javax.activation.DataHandler;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document




def Message processData(Message message) {
    
    try{


        def body = message.getBody();
        def bodyAsInputStream = message.getBody(InputStream.class);
        def baos = new ByteArrayOutputStream();
        if (bodyAsInputStream == null) {
            throw new NullPointerException("No Content transformer from " + body.class + " to InputStream registered.")
        }

        new ItelliMappingIDocInvoic02_to_IDocInvoicGenCombined().performMapping(bodyAsInputStream, baos,false);
        message.setBody(baos.toByteArray());

    }catch(Exception e){

        throw new Exception(e)

    }
    
    return message;
    
}
