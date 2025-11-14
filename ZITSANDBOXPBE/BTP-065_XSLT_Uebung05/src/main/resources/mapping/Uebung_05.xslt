<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />
	<xsl:param name="Z_ShippedDate" />
	<xsl:param name="Z_ShipName" />
	<xsl:param name="Z_CustomerID" />
	<xsl:template match="/">
		<SalesOrder>
			<Header>
				<SalesOrder>
					<xsl:value-of select="Voucher/Ordernumber" />
				</SalesOrder>
				<SalesOrderType>
					<xsl:value-of select="'ZGA'" />
				</SalesOrderType>
				<SalesOrganization>
					<xsl:value-of select="Voucher/Area" />
				</SalesOrganization>
				<SalesOffice>
					<xsl:value-of select="Voucher/Center" />
				</SalesOffice>
				<Customer>
					<xsl:value-of select="Voucher/CustomerName1" />
				</Customer>
				<DeliveryDate><xsl:value-of select="$Z_ShippedDate" /></DeliveryDate>
				<ShipperName><xsl:value-of select="$Z_ShipName" /></ShipperName>
				<ShipperID><xsl:value-of select="$Z_CustomerID" /></ShipperID>
			</Header>
		</SalesOrder>
	</xsl:template>
</xsl:stylesheet>