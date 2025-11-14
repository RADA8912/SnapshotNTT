import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    
import java.text.SimpleDateFormat   

// functions
def setZeroIfEmpty (value) {        
        if (value =="") return "0"
        else return value        
        }    

def replaceChar (String value, String sourceChar, String targetChar) {
       returnValue = ""       
       if (value !="") { 
        return returnValue = value.replaceAll(sourceChar,targetChar)                
       }      
       else return "0"
}

// main 
def Message processData(Message message) {

    Reader reader = message.getBody(Reader)
    def input = new XmlSlurper().parse(reader)
    Writer writer = new StringWriter()
    def indentPrinter = new IndentPrinter(writer, '    ')
    def builder = new MarkupBuilder(indentPrinter)
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'")
    LocalDateTime now = LocalDateTime.now()
    String time = dtf.format(now)  
    def rows = input.SB
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy")
    SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd")    
    // get filename from headers 
	def map = message.getHeaders();
	def filename = map.get("CamelFileName")   

    // add fields
    builder.'ns0:MT_Settlement'('xmlns:ns0': 'http://saps4.companyname.net/settlement')  {
        MessageHeader {
        SenderBusinessSystemID (filename)
        CreationDateTime (time)
        }

        Report_Header {
            Datum (input.RH.Datum)
            Feld (replaceChar(input.RH.Feld.toString(),"\"",""))
            Feld1 (input.RH.Feld1)
        }

        File_Header {           
            Feld (input.FH.Feld1)           
        }

        Section_Header {
            Datum (input.SH.Datum)
            Datum1 (input.SH.Datum1)
            Feld (input.SH.Feld)
        }

        rows.each { item -> 
        'Row_Data' {
          Transaktionscode (item.Transaktionscode)  
          Rechnungsnummer (item.Rechnungsnummer)
          PayPalReferenznummer (item.PayPalReferenznummer)
          PayPalReferenznummerTyp (item.PayPalReferenznummerTyp)
          Transaktionsereigniscode (item.Transaktionsereigniscode)
          Transaktionseinleitungsdatum (item.Transaktionseinleitungsdatum)
          Transaktionsabschlussdatum (item.Transaktionsabschlussdatum)
          TransaktionGutschriftOderBelastung (item.TransaktionGutschriftOderBelastung)
          Bruttotransaktionsbetrag (item.Bruttotransaktionsbetrag)
          WaehrungDerTransaktion (item.WaehrungDerTransaktion)
          GebuehrSollOderHaben (item.GebuehrSollOderHaben)
          Gebuehrenbetrag (item.Gebuehrenbetrag)
          WaehrungDerGebuehr (item.WaehrungDerGebuehr)
          Transaktionsstatus (item.Transaktionsstatus)
          Versicherungsbetrag (item.Versicherungsbetrag)
          Umsatzsteuerbetrag (item.Umsatzsteuerbetrag)
          Versandkosten (item.Versandkosten)
          Transaktionsgegenstand (item.Transaktionsgegenstand)
          Transaktionshinweis (item.Transaktionshinweis)
          KontokennungDesKaeufers (item.KontokennungDesKaeufers)
          AdressenstatusDesKäufers (item.AdressenstatusDesKäufers)
          Artikelbezeichnung (item.Artikelbezeichnung)
          Artikelnummer (item.Artikelnummer)
          NameOption1 (item.NameOption1)
          WertOption1 (item.WertOption1)
          NameOption2 (item.NameOption2)
          WertOption2 (item.WertOption2)
          AuktionsSite (item.AuktionsSite)
          Auktionsende (item.Auktionsende)
          KaeuferID (item.KaeuferID)
          VersandadresseZeile1 (item.VersandadresseZeile1)
          LieferadresseZeile2 (item.LieferadresseZeile2)
          VersandadresseOrt (item.VersandadresseOrt)
          LieferadresseBundesland (item.LieferadresseBundesland)
          LieferadressePLZ (item.LieferadressePLZ)
          VersandadresseLand (item.VersandadresseLand)
          Versandmethode (item.Versandmethode)
          BenutzerdefiniertesFeld (item.BenutzerdefiniertesFeld)
          RechnungsadresseZeile1 (item.RechnungsadresseZeile1)
          RechnungsadresseZeile2 (item.RechnungsadresseZeile2)
          RechnungsadresseOrt (item.RechnungsadresseOrt)
          RechnungsadresseStaat (item.RechnungsadresseStaat)
          RechnungsadressePLZ (item.RechnungsadressePLZ)
          RechnungsadresseLand (item.RechnungsadresseLand)
          Kundennummer (item.Kundennummer)
          Vorname (item.Vorname)
          Nachname (item.Nachname)
          Firmenname (item.Firmenname)
          Kartentyp (item.Kartentyp)
          Zahlungsquelle (item.Zahlungsquelle)
          Versandname (item.Versandname)
          Autorisierungsstatus (item.Autorisierungsstatus)
          AnspruchAufVerkaeuferschutz (item.AnspruchAufVerkaeuferschutz)
          ZahlungsverfolgungsID (item.ZahlungsverfolgungsID)
          Filiale (item.Filiale)
          Kasse (item.Kasse)
          Gutscheine (item.Gutscheine)
          Sonderangebote (item.Sonderangebote)
          Kundenkartennummer (item.Kundenkartennummer)
          Zahlungstyp (item.Zahlungstyp)
          AlternativeLieferadresseZeile1 (item.AlternativeLieferadresseZeile1)
          AlternativeLieferadresseZeile2 (item.AlternativeLieferadresseZeile2)
          AlternativeLieferadresseOrt (item.AlternativeLieferadresseOrt)
          AlternativeLieferadresseBundesland (item.AlternativeLieferadresseBundesland)
          AlternativeLieferadresseLand (item.AlternativeLieferadresseLand)
          AlternativeLieferadressePLZ (item.AlternativeLieferadressePLZ)
          PLReferenznummer (item.PLReferenznummer)
          Geschenkkartennummer (item.Geschenkkartennummer)
        }
        }
        Footer
        {
        Section_Footer {
            Feld (input.SF.Wert)}
        
        Section_Record_Count {
            Feld (input.SC.Wert)
        }
        Report_Footer {
            Feld (input.RF.Feld)
        }
        Report_Record_Count {
            Feld (input.RC.Feld)
        }
        File_Footer {
            Feld (input.FF.Feld)
        }
        }        
    }    
    message.setBody(writer.toString())
    return message
}