<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:ns3="http://www.sap.com/eDocument/Italy/ReceiveInvoice/v1.0" xmlns:typ="http://www.fatturapa.gov.it/sdi/ws/ricezione/v1.0/types">

    <xsl:template match="@*|node()"></xsl:template>
    
	<xsl:template match="/">
		<xsl:apply-templates select="*">
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template match="messages">
	   <ns3:PullInvoiceResponse>
        <xsl:apply-templates select="message">
        </xsl:apply-templates>
	   </ns3:PullInvoiceResponse>
	</xsl:template>

    <xsl:template match="message">
        <xsl:apply-templates select="*">
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="invoice">
     <InvoiceEnvelope>
        <xsl:copy-of select="*"></xsl:copy-of>
      </InvoiceEnvelope>
    </xsl:template>

</xsl:stylesheet>

<!-- 
INPUT
      <messages>
         <message id="IT01234567890_11111_NS_001">
            <typ:notificaScarto112 xmlns:typ="http://www.fatturapa.gov.it/sdi/ws/trasmissione/v1.0/types">
               <SdiIdentifier>123456</SdiIdentifier>
               <FileName>IT01234567890_11111_NS_001</FileName>
               <File>asdadadas112</File>
            </typ:notificaScarto112>
         </message>
         <message id="IT01234567890_11111_NS_002">
            <typ:notificaScarto xmlns:typ="http://www.fatturapa.gov.it/sdi/ws/trasmissione/v1.0/types">
               <SdiIdentifier>123456</SdiIdentifier>
               <FileName>IT01234567890_11111_NS_002</FileName>
               <File>asdadadas</File>
            </typ:notificaScarto>
         </message>
      </messages>


OUTPUT
      <ns3:InvoiceCollectionElement xmlns:ns3="http://www.sap.com/eDocument/Italy/PullInvoice/v1.0" xmlns:typ="http://www.fatturapa.gov.it/sdi/ws/trasmissione/v1.0/types">
         <ns3:InvoiceEnvelope>
            <SdiIdentifier>123456</SdiIdentifier>
            <FileName>IT01234567890_11111_NS_001</FileName>
            <File>asdadadas112</File>
         </ns3:InvoiceEnvelope>
         <ns3:InvoiceEnvelope>
            <SdiIdentifier>123456</SdiIdentifier>
            <FileName>IT01234567890_11111_NS_002</FileName>
            <File>asdadadas</File>
         </ns3:InvoiceEnvelope>
      </ns3:InvoiceCollectionElement>

 -->
