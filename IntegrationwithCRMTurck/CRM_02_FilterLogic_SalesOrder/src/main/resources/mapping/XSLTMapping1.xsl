<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions"
                version="2.0">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
    <xsl:template match="/">
        <xsl:for-each select="ORDERS05/IDOC">
            <xsl:if test="EDI_DC40/STATUS='30' and EDI_DC40/MESTYP='ORDERS'">
                <Order>
                    <OrderNumber>
                        <xsl:value-of select="replace(EDI_DC40/DOCNUM, '^0+', '')"/>
                    </OrderNumber>
                    <OrderDate>
                        <xsl:value-of select="EDI_DC40/CREDAT"/>
                    </OrderDate>
                    <Positions>
                        <xsl:for-each select="E1EDP01">
                            <xsl:if test="ACTION = '001' and /ORDERS05/IDOC/E1EDS01">
                                <Position>
                                    <xsl:copy-of select="node()"/>
                                </Position>
                            </xsl:if>
                        </xsl:for-each>
                    </Positions>
                    <xsl:for-each select="E1EDS01">
                        <xsl:if test="@SEGMENT">
                            <xsl:copy-of select="node()"/>
                        </xsl:if>
                    </xsl:for-each>
                </Order>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>