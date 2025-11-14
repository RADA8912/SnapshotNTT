<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output indent="no" omit-xml-declaration="yes" />
    <xsl:template match="/">
        <CLSMAS04>
                <xsl:for-each select="CLSMAS04/IDOC/E1KLAHM/E1KSMLM">
                    <E1KSMLM>
                        <xsl:copy-of select="/CLSMAS04/IDOC/E1KLAHM/KLART"/>
                        <xsl:copy-of select="ATNAM"/>
                    </E1KSMLM>
                </xsl:for-each>
        </CLSMAS04>
    </xsl:template>
</xsl:stylesheet>