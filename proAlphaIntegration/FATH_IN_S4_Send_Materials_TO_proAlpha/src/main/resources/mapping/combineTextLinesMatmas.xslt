<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes"/>

    <!-- Identity template to copy everything while stripping namespaces -->
    <xsl:template match="*">
        <xsl:element name="{local-name()}">
            <xsl:apply-templates select="@* | node()"/>
        </xsl:element>
    </xsl:template>

    <!-- Copy attributes -->
    <xsl:template match="@*">
        <xsl:attribute name="{local-name()}">
            <xsl:value-of select="."/>
        </xsl:attribute>
    </xsl:template>

    <!-- Group E1MTXLM elements by E1MTXHM/TDSPRAS -->
    <xsl:template match="E1MTXHM">
        <E1MTXHM>
            <xsl:apply-templates select="@* | node()[not(self::E1MTXLM)]"/>
            
            <xsl:for-each select="E1MTXLM[1]">
                <E1MTXLM>
                    <xsl:apply-templates select="@*"/>
                    <xsl:apply-templates select="../TDSPRAS"/>
                    <TDLINE>
                        <xsl:for-each select="../E1MTXLM/TDLINE">
                            <xsl:value-of select="normalize-space(.)"/>
                            <xsl:if test="position() != last()"> </xsl:if>
                        </xsl:for-each>
                    </TDLINE>
                </E1MTXLM>
            </xsl:for-each>

        </E1MTXHM>
    </xsl:template>

    <!-- Suppress extra E1MTXLM elements -->
    <xsl:template match="E1MTXLM"/>

</xsl:stylesheet>
