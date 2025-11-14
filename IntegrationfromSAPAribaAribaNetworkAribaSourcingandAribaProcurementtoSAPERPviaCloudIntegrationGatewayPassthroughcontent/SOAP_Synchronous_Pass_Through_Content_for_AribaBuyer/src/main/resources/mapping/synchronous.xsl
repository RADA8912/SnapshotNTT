<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hci="http://sap.com/it/" 
xmlns:n0="urn:sap-com:document:sap:rfc:functions" 
version="2.0" exclude-result-prefixes="hci n0">
   <xsl:param name="exchange" />
   <xsl:output method="xml" encoding="UTF-8" omit-xml-declaration="yes" />
   <xsl:template match="@* | *">
      <xsl:copy>
         <xsl:apply-templates select="@* | *" />
      </xsl:copy>
   </xsl:template>
   <xsl:template match="/">

      <xsl:param name="soapRealmId" select="normalize-space(//VARIANT)" />
      
     
      <xsl:variable name="docType">
         <xsl:choose>
       
        <xsl:when test="n0:ARBCIG_BAPI_PO_CLOSE">PurchaseOrderCloseStatusExportRequest</xsl:when>
		<xsl:when test="n0:ARBCIG_PURREQ">RequisitionExportRequest</xsl:when>
		<xsl:when test="n0:ARBCIG_DERIVATION">DeriveAccountingExport</xsl:when>
		<xsl:when test="n0:ARBCIG_PURREQ_DELETE">RequisitionRevertExportRequest</xsl:when>
         
         </xsl:choose>
      </xsl:variable> 
      
      <xsl:variable name="realmId">
         <xsl:choose>
            <xsl:when test="$soapRealmId != ''">
               <xsl:value-of select="$soapRealmId" />
            </xsl:when>
           
         </xsl:choose>
      </xsl:variable>   
          
     
      <xsl:copy>
         <xsl:apply-templates select="@*" />
         <xsl:copy-of select="." />
      </xsl:copy>
      <!-- Output Variables -->
      <xsl:value-of select="hci:setHeader($exchange, 'docType', $docType)" />
      <xsl:value-of select="hci:setHeader($exchange, 'realmId', $realmId)" />
   </xsl:template>
</xsl:stylesheet>