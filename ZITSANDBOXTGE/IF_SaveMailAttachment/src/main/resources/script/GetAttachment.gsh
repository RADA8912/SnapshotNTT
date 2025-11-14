import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	def attachments = message.getAttachments();
	
	// Set Mail properly for the Account check request
	def headerMap = message.getHeaders();
	def fullMailSender = headerMap.get("MailSender");
	//def simpleMail = (fullMailSender.split('<')[1]).subSequence(0, (fullMailSender.split('<')[1]).length() - 1);
	message.setProperty("simpleMailSender", fullMailSender);
	
	def subject = headerMap.get("Subject");
	message.setProperty("mailSubject", subject);
	

	def bTypeSupported = false;
	message.setProperty("MailContentTypeSupported", "False");
	message.setProperty("MailContentType", "OTHER");
	if ((attachments!=null)&&(!attachments.isEmpty())) {
	    
	    // Checking the attachment type 
	    attachments.values().each{ attachment ->
	        if (bTypeSupported == true){
	            return;
	        }
	        def sContentType = attachment.getContentType()
	        message.setProperty("MailContentType", sContentType);
	        sContentType = sContentType.substring(0, sContentType.lastIndexOf(";")).toLowerCase();
	        if(sContentType.contains("pdf")){
	            sContentType="application/pdf";
	            bTypeSupported = true;
	        }
	        else if ( sContentType.contains("png")){
	            sContentType="image/png";
	            bTypeSupported = true;
	           
	        }
	         else if (sContentType.contains("jpg")){
	            sContentType="image/jpg";
	            bTypeSupported = true;
	        }
	         else if (sContentType.contains("jpeg")){
				sContentType="image/jpeg";
	            bTypeSupported = true;
			}
			
			else if (sContentType.startsWith("application/octet-stream")){
			    //get type by filename 
			    def sFilename = attachment.getName();
			    sFilename = sFilename.toLowerCase();
			    def lastIndex = sFilename.lastIndexOf('.')
                def sFileExt = lastIndex >=0 ? sFilename.substring(lastIndex+1) : "";
                if (sFileExt=="jpg" || sFileExt =="png" ||sFileExt =="jpeg"){
                    bTypeSupported = true;
                    sContentType = "image/"+sFileExt;
                }
			}
			
			
			
			if (bTypeSupported == true){
			    message.setProperty("MailContentTypeSupported", "True");
			    byte[] attachment_body = readInputStream(attachment.getInputStream());
			    message.setProperty("attachment_body",attachment_body);
			    message.setProperty("attachment_fileName",attachment.getName());
			    message.setProperty("attachment_type",sContentType);
			}
	    }
	}

    return message;
}

public byte[] readInputStream(InputStream inputStream) {
	if (inputStream==null) return null;
	  def result = new ByteArrayOutputStream();
	  def buffer = new byte[1024];
	  def length;
	  while ((length = inputStream.read(buffer)) != -1) {
		result.write(buffer, 0, length);
	  }
	  def byteArray = result.toByteArray();
	  
	  return byteArray;
}

