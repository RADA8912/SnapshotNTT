<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<SalesOrder>
			<Header>
				<xsl:for-each select="Payload/Voucher">
					<SalesOrder>
						<xsl:value-of select="Ordernumber"/>
					</SalesOrder>
					<SalesOrderType>
						<xsl:value-of select="'ZGA'"/>
					</SalesOrderType>
					<SalesOrganization>
						<xsl:value-of select="Area"/>
					</SalesOrganization>
					<SalesOffice>
						<xsl:value-of select="Center"/>
					</SalesOffice>
					<Customer>
						<xsl:value-of select="CustomerName1"/>
					</Customer>
					<DeliveryDate>
						<xsl:value-of select="../Orders/Order/ShippedDate"/>
					</DeliveryDate>
					<ShipperName>
						<xsl:value-of select="../Orders/Order/ShipName"/>
					</ShipperName>
					<ShipperID>
						<xsl:value-of select="../Orders/Order/CustomerID"/>
					</ShipperID>
				</xsl:for-each>
			</Header>
		</SalesOrder>
	</xsl:template>
</xsl:stylesheet>