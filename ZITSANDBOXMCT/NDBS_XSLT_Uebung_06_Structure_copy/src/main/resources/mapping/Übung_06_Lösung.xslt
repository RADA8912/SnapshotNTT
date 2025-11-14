<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<root>
			<row>
				<CompanyCode>
					<xsl:value-of select="'Company Code'"/>
				</CompanyCode>
				<OrderNumber>
					<xsl:value-of select="'Order Number'"/>
				</OrderNumber>
				<GoodsReceipt>
					<xsl:value-of select="'Goods Receipt'"/>
				</GoodsReceipt>
			</row>
			<xsl:for-each select="A_MaterialDocumentHeader/A_MaterialDocumentHeaderType/to_MaterialDocumentItem/A_MaterialDocumentItemType[GoodsMovementType='101']">
				<row>
					<CompanyCode>
						<xsl:value-of select="'1000'"/>
					</CompanyCode>
					<OrderNumber>
						<xsl:value-of select="fn:concat(PurchaseOrder,'-',PurchaseOrderItem)"/>
					</OrderNumber>
					<GoodsReceipt>
						<xsl:value-of select="MaterialDocument"/>
					</GoodsReceipt>
				</row>
				<value_filled>
					<xsl:value-of select="'X'"/>
				</value_filled>
			</xsl:for-each>
		</root>
	</xsl:template>
</xsl:stylesheet>