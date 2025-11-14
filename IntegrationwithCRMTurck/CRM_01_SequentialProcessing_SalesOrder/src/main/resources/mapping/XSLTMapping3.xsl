<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions" version="2.0">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
    <xsl:template match="/">
        <Positions>
            <xsl:for-each select="//E1EDP01[ACTION='001']">
                <Position>
                    <xsl:copy-of select="node()"/>
                </Position>
            </xsl:for-each>
        </Positions>
    </xsl:template>
</xsl:stylesheet>