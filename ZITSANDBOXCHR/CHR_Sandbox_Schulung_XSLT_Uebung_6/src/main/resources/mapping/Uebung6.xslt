<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
    <xsl:template match="/">
        <root>
            <row>
                <CompanyCode>Company Code</CompanyCode>
                <OrderNumber>Order Number</OrderNumber>
                <GoodsReceipt>Goods Receipt</GoodsReceipt>
            </row>
            <xsl:for-each select="A_MaterialDocumentHeader/A_MaterialDocumentHeaderType/to_MaterialDocumentItem/A_MaterialDocumentItemType[GoodsMovementType ='101']">
            <row>
                <CompanyCode><xsl:value-of select="'1000'"/></CompanyCode>
                <OrderNumber><xsl:value-of select="fn:concat(PurchaseOrder, '-', PurchaseOrderItem)"/></OrderNumber>
                <GoodsReceipt><xsl:value-of select="MaterialDocument"/></GoodsReceipt>
            </row>
            </xsl:for-each>
            <value_filled/>
        </root>
    </xsl:template>
</xsl:stylesheet>