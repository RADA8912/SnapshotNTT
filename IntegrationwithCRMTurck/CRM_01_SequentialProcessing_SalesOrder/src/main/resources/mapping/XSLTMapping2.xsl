<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions"
                version="2.0">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
    <xsl:template match="/">
            <xsl:for-each select="ORDERS05/IDOC">
                <xsl:copy-of select="EDI_DC40"/>
            </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>