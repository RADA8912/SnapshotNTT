<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hci="http://sap.com/it/"
    xmlns:n0="http://sap.com/xi/EDI" xmlns:n1="http://sap.com/xi/Procurement" xmlns:pp="http://sap.com/xi/EDI/Creditor" xmlns:sapg20="http://sap.com/xi/SAPGlobal20/Global"
    version="2.0" exclude-result-prefixes="hci n0 n1 pp sapg20">
    <xsl:param name="exchange"/>
    <xsl:output method="xml" encoding="UTF-8" omit-xml-declaration="yes"/>
    <xsl:template match="@* | *">
        <xsl:copy>
            <xsl:apply-templates select="@* | *"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="/">
        <!-- TODO: Auto-generated template -->
        <!--Use RecipientBusinessSystemID for IES 42K Inbound Documents-->
        <xsl:param name="orderconfirmationrequest" select="normalize-space((//n0:OrderConfRequest/MessageHeader/RecipientBusinessSystemID))"/>
        <xsl:param name="deliveryrequest" select="normalize-space((//n0:DeliveryRequest/MessageHeader/RecipientBusinessSystemID))"/>
        <xsl:param name="InvoiceRequest" select="normalize-space((//n0:InvoiceRequest/MessageHeader/RecipientBusinessSystemID))"/>
        <xsl:param name="serviceentrysheetrequest" select="normalize-space(//sapg20:ServiceEntrySheetRequestMsg/MessageHeader/RecipientBusinessSystemID)"/>
        <xsl:param name="supplierquotations4request" select="normalize-space(//n1:SupplierQuotationS4Request/MessageHeader/RecipientBusinessSystemID)"/>
        <xsl:param name="ProofofDeliveryRequest" select="normalize-space(//n0:ProofOfDeliveryRequest/MessageHeader/RecipientBusinessSystemID)"/>
        <xsl:param name="centralsupplierquotations4requ" select="normalize-space((//n1:CentralSupplierQuotationS4Request/MessageHeader/RecipientBusinessSystemID))"/>
        <!-- 4BL/4QN/4AZ/4B0 Scenarios -->
        <xsl:param name="rfqconfirmations4request" select="normalize-space(//n1:RFQConfirmationS4Request/MessageHeader/RecipientBusinessSystemID)"/>
<!--        SupplierQuotation/CentralSupplierQuotation Already covered in 42K-->
        <xsl:param name="PurchaseContractRequest"
            select="normalize-space(//n0:PurchaseContractRequest/MessageHeader/RecipientBusinessSystemID)"/>
        <xsl:param name="BusinessPartnerSUITEBulkReplicateRequest"
            select="normalize-space(//sapg20:BusinessPartnerSUITEBulkReplicateRequest/MessageHeader/RecipientBusinessSystemID)"/>    
        <xsl:variable name="docType">
            <xsl:choose>
                <xsl:when test="n0:OrderConfRequest">orderconfirmationrequest</xsl:when>
                <xsl:when test="n0:DeliveryRequest">deliveryrequest</xsl:when>
                <xsl:when test="n0:InvoiceRequest">InvoiceRequest</xsl:when>
                <xsl:when test="sapg20:ServiceEntrySheetRequestMsg">serviceentrysheetrequest</xsl:when>
                <xsl:when test="n1:SupplierQuotationS4Request"
                    >supplierquotations4request</xsl:when>
                <xsl:when test="n0:ProofOfDeliveryRequest">ProofofDeliveryRequest</xsl:when>
                <xsl:when test="n1:CentralSupplierQuotationS4Request">centralsupplierquotations4requ</xsl:when>
                <xsl:when test="n1:RFQConfirmationS4Request">rfqconfirmations4request</xsl:when>
                <xsl:when test="n0:PurchaseContractRequest">PurchaseContractRequest</xsl:when>
                <xsl:when test="sapg20:BusinessPartnerSUITEBulkReplicateRequest">BusinessPartnerSUITEReplicateRequest</xsl:when>
              </xsl:choose>
        </xsl:variable>

        <xsl:variable name="systemId">
            <xsl:choose>
                <xsl:when test="$orderconfirmationrequest != ''">
                    <xsl:value-of select="$orderconfirmationrequest"/>
                </xsl:when>
                <xsl:when test="$deliveryrequest != ''">
                    <xsl:value-of select="$deliveryrequest"/>
                </xsl:when>
                <xsl:when test="$serviceentrysheetrequest != ''">
                    <xsl:value-of select="$serviceentrysheetrequest"/>
                </xsl:when>
                <xsl:when test="$InvoiceRequest != ''">
                    <xsl:value-of select="$InvoiceRequest"/>
                </xsl:when>
                <xsl:when test="$supplierquotations4request != ''">
                    <xsl:value-of select="$supplierquotations4request"/>
                </xsl:when>
                <xsl:when test="$ProofofDeliveryRequest != ''">
                    <xsl:value-of select="$ProofofDeliveryRequest"/>
                </xsl:when>
                <xsl:when test="$centralsupplierquotations4requ != ''">
                    <xsl:value-of select="$centralsupplierquotations4requ"/>
                </xsl:when>
                <xsl:when test="$rfqconfirmations4request != ''">
                    <xsl:value-of select="$rfqconfirmations4request"/>
                </xsl:when>
                <xsl:when test="$PurchaseContractRequest != ''">
                    <xsl:value-of select="$PurchaseContractRequest"/>
                </xsl:when>
                <xsl:when test="$BusinessPartnerSUITEBulkReplicateRequest != ''">
                    <xsl:value-of select="$BusinessPartnerSUITEBulkReplicateRequest"/>
                </xsl:when>
            </xsl:choose>
        </xsl:variable>

        <xsl:variable name="docStd">
            <xsl:value-of select="'SOAP'"/>
        </xsl:variable>

        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:copy-of select="."/>
        </xsl:copy>

        <!-- Output Variables -->
        <xsl:value-of select="hci:setHeader($exchange, 'docType', $docType)"/>
        <xsl:value-of select="hci:setHeader($exchange, 'systemId', $systemId)"/>
        <xsl:value-of select="hci:setHeader($exchange, 'documentStandard', $docStd)"/>

    </xsl:template>
</xsl:stylesheet>
