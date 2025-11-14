import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message){

	//SetProperties
	message.setProperty("converterClass","com.equalize.converter.core.XML2DeepPlainConverter");
	message.setProperty("documentName","Root");
	message.setProperty("encoding","UTF-8");
	message.setProperty("recordsetStructure","EINS,ZWEI,DREI,VIER");
	
	
/*	message.setProperty("documentNamespace","http://saps4.fielmann.net/kasse_euronet");
	message.setProperty("recordsetStructure","HS,AG,SE,F1,F2,F3,F4,F5,S1,S2,S3,S4,S5,S6,S7,S8,S9,S0,GV,GZ,AV,AE,SA,SZ,WG,KK,RB,D1,D2,D3,D4,D5,DG,DS,DW,DZ,ZV,EZ,ZZ,PS,UNBEKANNT");
	message.setProperty("keyFieldName","SATZART");
	message.setProperty("HS.fieldNames","BUKRS,KASSDAT,JOURNAL,SATZART,VORTAG,TUMSATZ,ZIELVER,ZIELEIN,GUTVERK,GUTZAHL,GUTSCHRAUS,GUTSCHRZAH,ANZERHA,ANZVERR,SONEIN,AUSGABE,ELV,AMEXCO,DINERS,EURO,VISA,FREMD1,FREMD2,FREMD3,FREMD4,FREMD5,BAR,SCHECK,SOZAMI1,SOZAMI2,SOZAMI3,SOZAMI4,SOZAMI5,SOZAMI6,SOZAMI7,SOZAMI8,SOZAMI9,SOZAMI0CHIPKNIP,TBIST");
	message.setProperty("PS.parent","Root");
	message.setProperty("UNBEKANNT.parent","Root");
	message.setProperty("HS.missingLastFields","ignore");
*/
	return message;
}