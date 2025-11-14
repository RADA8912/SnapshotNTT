import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    
    //def properties = message.getProperties()
    
    def headers = message.getHeaders();
    
    def idocnr = headers.get("IDOCNUM")
    def rcvprt = headers.get("SAP_SenderType")
    def rcvprn = headers.get("SAP_Sender")
    def sndprn = headers.get("SAP_Receiver")
    def sndprt = headers.get("SAP_ReceiverType")

    def out = '''<?xml version="1.0" encoding="UTF-8"?>
    <ALEAUD01>
        <IDOC BEGIN="1">
            <EDI_DC40 SEGMENT="1">
                <TABNAM>EDI_DC40</TABNAM>                
                <DIRECT>2</DIRECT>
                <IDOCTYP>ALEAUD01</IDOCTYP>
                <MESTYP>ALEAUD</MESTYP>
                <SNDPOR>CPI</SNDPOR>
                <SNDPRT>''' + sndprt + '''</SNDPRT>
                <SNDPRN>''' + sndprn + '''</SNDPRN>
                <RCVPOR>SOAP</RCVPOR>
                <RCVPRT>''' + rcvprt + '''</RCVPRT>
                <RCVPRN>''' + rcvprn + '''</RCVPRN>
            </EDI_DC40>
            <E1ADHDR SEGMENT="1">
                <MESTYP>ALEAUD</MESTYP>
                <E1STATE SEGMENT="1">
                <DOCNUM>''' + idocnr + '''</DOCNUM>
                <STATUS>50</STATUS>
            </E1STATE>
         </E1ADHDR>
      </IDOC>
   </ALEAUD01>'''
  
    message.setBody(out)
    return message
}
