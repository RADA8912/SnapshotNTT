<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" omit-xml-declaration="yes" version="1.0" encoding="UTF-8" indent="yes"/>
        <xsl:param name="data"/>
         <xsl:param name="klart"/>
    <xsl:template match="@* | node()">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:variable name="clfmas" select="//CLFMAS02[IDOC/E1OCLFM/KLART != $klart]"/>
    <xsl:template match="/">
        <xsl:copy-of select="$clfmas"/>
    </xsl:template>
</xsl:stylesheet>

