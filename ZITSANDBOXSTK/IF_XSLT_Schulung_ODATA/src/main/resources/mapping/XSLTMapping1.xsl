<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
<!-- define params -->
<xsl:param name="DeliveryDate"/>
<xsl:param name="ShipperName"/>
<xsl:param name="ShipperID"/>
<xsl:template match="/">
    <SalesOrder>
        <xsl:for-each select="Voucher">
        <Header>
            <SalesOrder>
                <xsl:value-of select="Ordernumber"/>
            </SalesOrder>
            <SalesOrderType>
                <xsl:value-of select="'ZGA'"/>
            </SalesOrderType>
            <SalesOffice>
                <xsl:value-of select="Center"/>
            </SalesOffice>
            <Customer>
                <xsl:value-of select="CustomerName1"/>
            </Customer>
            <DeliveryDate>
                <xsl:value-of select="$DeliveryDate"/>
            </DeliveryDate>
            <ShipperName>
                <xsl:value-of select="$ShipperName"/>
            </ShipperName>
            <ShipperID>
                <xsl:value-of select="$ShipperID"/>
            </ShipperID>
        </Header>
        </xsl:for-each>
    </SalesOrder>
</xsl:template>
</xsl:stylesheet>