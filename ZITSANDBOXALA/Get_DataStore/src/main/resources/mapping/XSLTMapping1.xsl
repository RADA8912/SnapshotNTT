<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- Output settings: keep XML declaration -->
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" omit-xml-declaration="no"/>

    <!-- Remove the <messages> wrapper -->
    <xsl:template match="/messages">
        <xsl:apply-templates select="message/*" />
    </xsl:template>

    <!-- Remove the <message> wrapper -->
    <xsl:template match="message">
        <xsl:apply-templates select="*" />
    </xsl:template>

    <!-- Default copy template for all other elements -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
