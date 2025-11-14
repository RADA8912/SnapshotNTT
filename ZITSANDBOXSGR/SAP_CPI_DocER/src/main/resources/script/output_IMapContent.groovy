import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.gateway.ip.core.customdev.util.Message;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.codec.binary.Base64;



public class docHeader {

	private String sourceFields ="";
	private String constantValues ="";
	private String functionS ="";
	private String targetField ="";

	public void setsourceFields(String name) {
		this.sourceFields = this.sourceFields.concat(name);
	}

	public void setconstantValues(String name) {
		this.constantValues = this.constantValues.concat(name);
	}

	public void setfunctionS(String name) {
		this.functionS = this.functionS.concat(name);
	}

	public void settargetField(String name) {
		this.targetField = this.targetField.concat(name);
	}
}

def Message processData(Message message) {

	//Get Response Payload
	def body = message.getBody(String.class);

	InputStream inpStream = new ByteArrayInputStream(body.getBytes());

	Document document = processxml(inpStream);

	// Normalize the XML Structure; It's just too important !!
	document.getDocumentElement().normalize();

	// Here comes the root node
	Element root = document.getDocumentElement();


	Element content = (Element) root.getElementsByTagNameNS("urn:sap-com:xi", "content").item(0);
	Element metaData = (Element) content.getElementsByTagNameNS("urn:sap-com:xi:mapping:xitrafo","MetaData").item(0);

	String mapData;

	if(metaData.getElementsByTagName("mappingtool").getLength() > 0 ) {
		Element transformation = getTransformationTag(metaData);
		mapData = getMapData(transformation);
	}
	else if(metaData.getElementsByTagNameNS("urn:sap-com:xi:mapping:xitrafo", "blob").getLength()>0) {

		byte[] buffer = new byte[1024];
		String b64data = metaData.getElementsByTagNameNS("urn:sap-com:xi:mapping:xitrafo", "blob").item(0).getTextContent();
		b64data = b64data.replace("!zip!", "");
		byte[] decCon = Base64.decodeBase64(b64data);

		ByteArrayOutputStream oStream = getunzipData(decCon);

		ByteArrayOutputStream oStream1 = getunzipData(oStream.toByteArray());

		InputStream newinpStream = new ByteArrayInputStream(oStream1.toByteArray());

		Document doc1 = processxml(newinpStream);
		doc1.getDocumentElement().normalize();
		Element root1 = doc1.getDocumentElement();
		Element transformation1 = getTransformationTag(root1);
		mapData = getMapData(transformation1);
	}

	message.setHeader("Content-Type", "application/json");
	message.setBody("{\"mappingResult\":" + mapData + "}");

	return message;

}

def Document processxml(InputStream inpStream) throws Exception {

	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	factory.setNamespaceAware(true);
	DocumentBuilder builder = factory.newDocumentBuilder();

	Document document = builder.parse(inpStream);

	return document;
}

def Element getTransformationTag(Element root) {

	NodeList transf = root.getElementsByTagName("transformation");
	Element transformation = (Element) transf.item(0);
	return transformation;
}

def String getMapData(Element transformation) {

	NodeList nList1 = transformation.getChildNodes();


	def dh = [];

	int resar = 0;

	for (int temp = 0; temp < nList1.getLength(); temp++) {

		int sf = 0;
		int fr = 0;
		int cn = 0;


		Node node = nList1.item(temp);

		if(node.getNodeType() == Node.ELEMENT_NODE) {

			dh[resar] = new docHeader();
			Element eElement = (Element) node;

			NodeList nList2 = eElement.getElementsByTagName("brick");

			for (int temp1 = 0; temp1 < nList2.getLength(); temp1++) {

				Node node1 = nList2.item(temp1);
				Element eElement1 = (Element) node1;

				if(eElement1.getAttribute("type").equals("Src")) {
					if(sf!=0)
						dh[resar].setsourceFields("\n");
					dh[resar].setsourceFields(eElement1.getAttribute("path"));
					sf++;
				}
				else if(eElement1.getAttribute("type").equals("Func") && eElement1.getAttribute("fname").equals("const")) {
					if(cn!=0)
						dh[resar].setconstantValues("\n");
					dh[resar].setconstantValues("Constant -" + eElement.getElementsByTagName("value").item(0).getTextContent());
					cn++;
				}
				else {
					String value=eElement1.getAttribute("fname");
					if(fr!=0 && !value.isEmpty())
						dh[resar].setfunctionS("\n");
					dh[resar].setfunctionS(value);
					fr++;
				}

			}
			dh[resar].settargetField(eElement.getAttribute("path"));
			resar++;

		}

	}

	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	return gson.toJson(dh);

}

def ByteArrayOutputStream getunzipData(byte[] inp) throws Exception {

	byte[] buffer = new byte[1024];

	InputStream newinpStream = new ByteArrayInputStream(inp);

	ByteArrayOutputStream oStream = new ByteArrayOutputStream();

	ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream("zip", newinpStream);
	ais.getNextEntry();
	int len;
	while ((len = ais.read(buffer)) > 0) {
		oStream.write(buffer, 0, len);
	}

	return oStream;
}