<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hci="http://sap.com/it/"  xmlns:n0="http://sap.com/xi/ARBCIG1"  xmlns:n1="urn:sap-com:document:sap:rfc:functions" xmlns:sapg20="http://sap.com/xi/SAPGlobal20/Global" xmlns:n2="http://sap.com/xi/ARBCIGR" xmlns:ns0="http://ariba.com/s4/dms/schema/pir"  xmlns:n3="http://ariba.com/s4/dms/schema/bom" xmlns:n4="http://sap.com/xi/APPL/BNSFINMDR" xmlns:n5="http://sap.com/xi/APPL/LogMDR" xmlns:n6="http://sap.com/xi/FNDEI" xmlns:n7="http://sap.com/xi/PS" xmlns:n8="http://sap.com/xi/SAPGlobal20/Global" xmlns:n9="http://ariba.com/s4/dms/schema/changemaster" xmlns:n10="http://sap.com/xi/Procurement" xmlns:typens="urn:Ariba:Buyer:vsap" version="2.0" exclude-result-prefixes="hci n0 n1 n2 ns0 sapg20 n3 n4 n5 n6 n7 n8 n9 n10 typens">
    <xsl:param name="exchange" />
    <xsl:output method="xml" encoding="UTF-8" omit-xml-declaration="yes" />
    <xsl:template match="@* | *">
        <xsl:copy>
            <xsl:apply-templates select="@* | *" />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="/">
        
        <xsl:variable name="docType">
            <xsl:choose>
                
                <xsl:when test="n0:MasterDataReplReqMsg">MasterData</xsl:when>
                <xsl:when test="sapg20:BusinessPartnerSUITEBulkReplicateRequest">MasterData</xsl:when>
                <xsl:when test="sapg20:BusinessPartnerSUITEBulkReplicateConfirmation">MasterData</xsl:when>
                <xsl:when test="sapg20:KeyMappingBulkReplicateRequest">MasterData</xsl:when>
                <xsl:when test="sapg20:KeyMappingBulkReplicateConfirmation">MasterData</xsl:when>
                <xsl:when test="sapg20:BusinessPartnerRelationshipSUITEBulkReplicateRequest">MasterData</xsl:when>
                <xsl:when test="sapg20:BusinessPartnerRelationshipSUITEBulkReplicateConfirmation">MasterData</xsl:when>
                <!-- Sourcing/Retail -->
                <xsl:when test="n2:MasterDataDeleteRequest">MasterData</xsl:when>
                <xsl:when test="ns0:PIRAcknowledgementRequest">MasterData</xsl:when>
                <xsl:when test="n3:BOMReplicateRequest">MasterData</xsl:when>
                <xsl:when test="n9:ChangeMasterRequest">MasterData</xsl:when> 
                <!-- MDNI -->
				<xsl:when test="n4:BNSCompanyCodeMasterDataReplicationBundleRequest">MasterData</xsl:when>
				<xsl:when test="n4:BNSCostCentreMasterDataReplicationBulkRequest">MasterData</xsl:when>
				<xsl:when test="n4:BNSFixedAssetMasterDataReplicationBulkRequest">MasterData</xsl:when>
				<xsl:when test="n4:BNSGLAccountMasterDataReplicationBulkRequest">MasterData</xsl:when>
				<xsl:when test="n4:BNSInternalOrderMasterDataReplicationBulkRequest">MasterData</xsl:when>
				<xsl:when test="n4:BNSPaymentMethodMasterDataReplicationBundleRequest">MasterData</xsl:when>
				<xsl:when test="n4:BNSPaymentTermsMasterDataReplicationBundleRequest">MasterData</xsl:when>
				<xsl:when test="n4:BNSTaxCodeMasterDataReplicationBulkRequest">MasterData</xsl:when>
				<xsl:when test="n5:AccountAssignmentCategoryMasterDataReplicationBundleRequest">MasterData</xsl:when>
				<xsl:when test="n5:PlantMasterDataReplicationBundleRequest">MasterData</xsl:when>
				<xsl:when test="n5:PlantPurchasingOrgMasterDataReplicationBundleRequest">MasterData</xsl:when>
				<xsl:when test="n5:PurchasingGroupMasterDataReplicationBundleRequest">MasterData</xsl:when>
				<xsl:when test="n5:PurchasingOrganisationMasterDataReplicationBundleRequest">MasterData</xsl:when>
				<xsl:when test="n6:CurrencyMasterDataReplicationBundleRequest">MasterData</xsl:when>
				<xsl:when test="n6:ExchangeRateMasterDataReplicationBundleRequest">MasterData</xsl:when>
				<xsl:when test="n6:IncotermsMasterDataReplicationBundleRequest">MasterData</xsl:when>
				<xsl:when test="n6:MaterialGroupMasterDataReplicationBundleRequest">MasterData</xsl:when>
				<xsl:when test="n6:PurchaseDocumentItemCategoryMasterDataReplicationBundleRequest">MasterData</xsl:when>
				<xsl:when test="n6:UnitOfMeasurementMasterDataReplicationBundleRequest">MasterData</xsl:when>
				<xsl:when test="n7:WBSElementMasterDataReplicationBulkRequest">MasterData</xsl:when>
				<xsl:when test="n8:ProductMDMBulkReplicateRequestMessage">MasterData</xsl:when>
				<xsl:when test="typens:UserMasterDataReplicationBulkRequest">MasterData</xsl:when>
				
				<!--                Guided Buying Status-->
                <xsl:when test="n10:PurchaseOrderItemHistoryNotification">Guided Buying</xsl:when>
                <xsl:when test="n10:PurchaseRequisitionReplicationNotification">Guided Buying</xsl:when>
                <xsl:when test="n10:PurchaseRequesitionStatusNotificationMsg">Guided Buying</xsl:when>
                <xsl:when test="n10:PurchaseRequisitionReservationNotification">Guided Buying</xsl:when>
				
                <xsl:otherwise>TransactionalData</xsl:otherwise>
            </xsl:choose>
            
        </xsl:variable>
        <xsl:copy>
            <xsl:apply-templates select="@*" />
            <xsl:copy-of select="." />
        </xsl:copy>

        <!-- Output Variables -->
        <xsl:value-of select="hci:setHeader($exchange, 'docType', $docType)" />
    </xsl:template>
</xsl:stylesheet>