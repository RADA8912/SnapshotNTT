<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


    <xsl:output method="xml" encoding="utf-8" indent="no"/>

    <!-- Copy all XML nodes in payload -->   
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>

    <!-- If element == E1WYT3M; omit element from copying -->
    <xsl:template match="E1WYT3M" />

</xsl:stylesheet>