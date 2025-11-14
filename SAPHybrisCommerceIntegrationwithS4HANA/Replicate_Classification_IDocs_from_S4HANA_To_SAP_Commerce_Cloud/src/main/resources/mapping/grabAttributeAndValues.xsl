<xsl:stylesheet version="1.0"
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:x="http://invia.fujitsu.com/RetailDATACenter/rdc.xsd">
    <xsl:output omit-xml-declaration="yes" indent="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:param name="attributeValues" select=
    "'|CHRMAS05|IDOC|E1CABNM|E1CAWNM|ATWRT|'"/>
    
    <xsl:template match="*" >
    <xsl:copy-of select="ATNAM"/>
     <xsl:if test="contains($attributeValues, concat('|',local-name(), '|'))">
        <xsl:element name="{name()}">
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="node()"/>
        </xsl:element>
     </xsl:if>
    </xsl:template>

    <xsl:template match="node()[not(self::*)]">
        <xsl:copy-of select="."/>
    </xsl:template>
</xsl:stylesheet>