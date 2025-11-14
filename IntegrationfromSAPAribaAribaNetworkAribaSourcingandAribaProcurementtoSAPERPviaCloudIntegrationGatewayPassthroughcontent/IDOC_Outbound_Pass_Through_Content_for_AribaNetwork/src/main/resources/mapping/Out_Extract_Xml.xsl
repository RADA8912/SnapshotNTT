<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hci="http://sap.com/it/" 
    version="2.0">
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
                <xsl:when test="ARBCIGR_CHRMAS">AppsIDOC</xsl:when>
                <xsl:when test="ARBCIGR_CLSMAS">AppsIDOC</xsl:when>
                <xsl:when test="ARBCIGR_WMERCAT">AppsIDOC</xsl:when>
                <xsl:when test="ARBCIGR_BOMMAT">AppsIDOC</xsl:when>
                <xsl:when test="ARBCIGR_ARTMAS">AppsIDOC</xsl:when>
                <xsl:when test="ARBCIGR_ALEAUD">AppsIDOC</xsl:when>
                <xsl:otherwise>IDOC</xsl:otherwise>
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