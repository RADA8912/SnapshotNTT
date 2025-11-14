<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                              xmlns:n0="http://sap.com/xi/SAPGlobal20/Global" 
                              xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
                              xmlns:prx="urn:sap.com:proxy:QE6:/1SAI/TAS9B28D7884F2C0D1E09D6:751">
    
    <xsl:strip-space elements="*"/>
    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="/Log">
        <n0:BusinessPartnerRelationshipSUITEBulkReplicateConfirmation>
            <xsl:apply-templates select="@*|node()"/>
        </n0:BusinessPartnerRelationshipSUITEBulkReplicateConfirmation>
    </xsl:template>
    
</xsl:stylesheet>