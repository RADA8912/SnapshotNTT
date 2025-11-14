
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import javax.mail.util.ByteArrayDataSource;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.camel.Attachment;
import com.sap.gateway.ip.core.customdev.util.AttachmentWrapper;
import javax.activation.DataHandler;
import java.util.Iterator;
import org.apache.commons.io.IOUtils;
import org.apache.camel.impl.DefaultAttachment;
import java.lang.Byte;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.lang.StringBuilder;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.apache.http.entity.ContentType;

def Message processData(Message message) {
    //Body 

    byte[] payload = message.getBody(byte[].class);
    String source = null
    def map = message.getHeaders();

    String SOAPEnvelopeSize = map.get("anPayloadSize")

    // Data source
    MimeMessage mimeMessage = new MimeMessage(source);
    def MimeMultipart mimeMultipart = new MimeMultipart("related");

    //SOAP Payload
    def MimeBodyPart soapapayload = new MimeBodyPart();
    soapapayload.setHeader("Content-Length", SOAPEnvelopeSize)

    ByteArrayDataSource soapDataSource = new ByteArrayDataSource(payload, "application/xop+xml")
    soapapayload.setDataHandler(new DataHandler(soapDataSource));
    soapapayload.setHeader("Content-Type", "application/xop+xml; charset=utf-8; type=\"text/xml\"")
    mimeMultipart.addBodyPart(soapapayload)

    InputStream inputStream = null;
    ByteArrayOutputStream boas = null;
    //get and set attachments
    def attMap = message.getAttachments();
    try
    {
        def anAttCount = attMap.size()
        if(anAttCount > 0)

        {

            Map<String, AttachmentWrapper> attMapN = message.getAttachmentWrapperObjects();
            Iterator<Entry<String, AttachmentWrapper>> attIteratorN = attMapN.entrySet().iterator();
            int j = 1;
            while(attIteratorN.hasNext())
            {

                Entry<String, AttachmentWrapper> entryN = attIteratorN.next();
                AttachmentWrapper attachment = entryN.getValue();
                DataHandler dataHandler = attachment.getDataHandler();
                inputStream = dataHandler.getInputStream();
                byte[] bytes = IOUtils.toByteArray(inputStream);
                int anEachAttachmentSize = bytes.length
                anAttSize = Long.toString(anEachAttachmentSize)
                def MimeBodyPart soapAttachmentBodyPart = new MimeBodyPart();
                Collection<String> headerList = attachment.getHeaderNames();
                soapAttachmentBodyPart.setDataHandler(dataHandler)
                for (String attachmentHeaderName : headerList) {
                    String attachmentHeaderValue = attachment.getHeader(attachmentHeaderName);
                    soapAttachmentBodyPart.addHeader(attachmentHeaderName, attachmentHeaderValue);
                }
                mimeMultipart.addBodyPart(soapAttachmentBodyPart)
                j++;
            }
        }

        StringBuilder builder
        String searchStr
        String replaceStr
        String resultString

        String mimeContentType = mimeMultipart.getContentType();
        try {
            ContentType contentType = ContentType.parse(mimeContentType);
            mimeContentType =  contentType.toString();
        } catch (Exception e)
        {
            //
        }
        builder = new StringBuilder(mimeContentType);
        searchStr = "multipart/related"
        replaceStr = "Multipart/Related;type=\"application/xop+xml\"; start-info=\"text/xml\""
        resultString = rmReplaceAll(builder, searchStr, replaceStr)
        message.setHeader("Content-Type",resultString);
        boas = new ByteArrayOutputStream();
        mimeMultipart.writeTo(boas);
        message.setBody(boas);
        message.setHeader("accept","application/xop+xml ,text/xml")
    }
    catch (IOException ex) {
        if (inputStream) {
            inputStream.close();
        }
        if (boas) {
            boas.close();
        }
        throw new IllegalStateException(ex.getMessage());
    }
    finally {
        if (inputStream) {
            inputStream.close();
        }
        if (boas) {
            boas.close();
        }
    }

    return message;
}

def rmReplaceAll(builder, String from, String to)
{
    int index = builder.indexOf(from);
    while (index != -1)
    {
        builder.replace(index, index + from.length(), to);
        index += to.length();
        index = builder.indexOf(from, index);
    }
    String replacedString = new String(builder);
    return replacedString;
}

