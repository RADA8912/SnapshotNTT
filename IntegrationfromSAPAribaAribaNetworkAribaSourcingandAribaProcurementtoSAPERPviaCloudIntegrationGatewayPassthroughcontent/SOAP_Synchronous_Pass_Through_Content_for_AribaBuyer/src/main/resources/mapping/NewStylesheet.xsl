<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hci="http://sap.com/it/" xmlns:n0="http://sap.com/xi/ARBCIG1" 
version="2.0" exclude-result-prefixes="hci n0">
   <xsl:param name="exchange" />
   <xsl:output method="xml" encoding="UTF-8" omit-xml-declaration="yes" />
   <xsl:template match="@* | *">
      <xsl:copy>
         <xsl:apply-templates select="@* | *" />
      </xsl:copy>
   </xsl:template>
   <xsl:template match="/">
      <!-- TODO: Auto-generated template -->
      <xsl:param name="idocType" select="normalize-space(ORDERS05/IDOC/EDI_DC40/IDOCTYP)" />
      <xsl:param name="idocSysId" select="normalize-space(//IDOC/EDI_DC40/SNDPRN)" />
      <xsl:param name="soapSysId" select="normalize-space(//RecipientParty/InternalID)" />
      
     
      <xsl:variable name="docType">
         <xsl:choose>
         <xsl:when test="n0:DsptchdDelivNotifMsg">DsptchdDelivNotifMsg</xsl:when>
         
		<xsl:when test="n0:ComponentAcknowledgement">ComponentAcknowledgement</xsl:when>
		<xsl:when test="n0:ComponentConsumption">ComponentConsumption</xsl:when>
		<xsl:when test="n0:CreditMemoMsg">CreditMemoMsg</xsl:when>
		<xsl:when test="n0:DocumentStatusUpdateRequest">DocumentStatusUpdateRequest</xsl:when>
		<xsl:when test="n0:LiabilityTransfer">LiabilityTransfer</xsl:when>
		<xsl:when test="n0:ProductReplenishment">ProductReplenishment</xsl:when>
		<xsl:when test="n0:RemittanceAdvice">RemittanceAdvice</xsl:when>
		<xsl:when test="n0:ServiceEntrySheetRequest">ServiceEntrySheetRequest</xsl:when>
		<xsl:when test="n0:QualityIssueNotificationMessage">QualityIssueNotificationMessage</xsl:when>
		<xsl:when test="n0:PurchaseOrderExportRequest">PurchaseOrderExportRequest</xsl:when>
		<xsl:when test="n0:PurchaseOrderChangeExportRequest">PurchaseOrderChangeExportRequest</xsl:when>
		<xsl:when test="n0:PurchaseOrderCancelExportRequest">PurchaseOrderCancelExportRequest</xsl:when>
		<xsl:when test="n0:PurchaseOrderCloseStatusExportRequest">PurchaseOrderCloseStatusExportRequest</xsl:when>
		<xsl:when test="n0:RequisitionExportRequest">RequisitionExportRequest</xsl:when>
		<xsl:when test="n0:DeriveAccountingExport">DeriveAccountingExport</xsl:when>
		<xsl:when test="n0:ReceiptExportRequest">ReceiptExportRequest</xsl:when>
		<xsl:when test="n0:ServiceEntrySheetRequest">ServiceEntrySheetRequest</xsl:when>
		<xsl:when test="n0:AdvancePaymentExportRequest">AdvancePaymentExportRequest</xsl:when>
		<xsl:when test="n0:CancelAdvancePaymentExportRequest">CancelAdvancePaymentExportRequest</xsl:when>
		<xsl:when test="n0:PaymentExportRequest">PaymentExportRequest</xsl:when>
		<xsl:when test="n0:RequisitionRevertExportRequest">RequisitionRevertExportRequest</xsl:when>
		<xsl:when test="n0:PurchasingContractERPCreateRequest">PurchasingContractERPCreateRequest</xsl:when>
		<xsl:when test="n0:ARBCIG_QUOTE">ARBCIG_QUOTE</xsl:when>
		<xsl:when test="n0:PurchasingContractERPRequest_V1">PurchasingContractERPRequest_V1</xsl:when>
		<xsl:when test="n0:PurchaseInfoRecord">PurchaseInfoRecord</xsl:when>

        
        
         
         </xsl:choose>
      </xsl:variable> 
      
      <xsl:variable name="systemId">
         <xsl:choose>
            <xsl:when test="$idocSysId != ''">
               <xsl:value-of select="$idocSysId" />
            </xsl:when>
            <xsl:otherwise>
               <xsl:value-of select="$soapSysId" />
            </xsl:otherwise>
         </xsl:choose>
      </xsl:variable>   
      
      
      <xsl:variable name="docStd">
         <xsl:choose>
            <xsl:when test="$idocSysId != ''">IDoc</xsl:when>
         </xsl:choose>
      </xsl:variable>
      <xsl:copy>
         <xsl:apply-templates select="@*" />
         <xsl:copy-of select="." />
      </xsl:copy>
      <!-- Output Variables -->
      <xsl:value-of select="hci:setHeader($exchange, 'docType', $docType)" />
      <xsl:value-of select="hci:setHeader($exchange, 'systemId', $systemId)" />
      <xsl:value-of select="hci:setHeader($exchange, 'documentStandard', $docStd)" />
   </xsl:template>
</xsl:stylesheet>