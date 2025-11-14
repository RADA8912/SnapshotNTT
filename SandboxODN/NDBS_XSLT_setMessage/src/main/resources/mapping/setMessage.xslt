<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<root>
			<xsl:for-each select="A_MaterialDocumentHeader/A_MaterialDocumentHeaderType/to_MaterialDocumentItem/A_MaterialDocumentItemType">
				<value>
					<xsl:choose>
						<xsl:when test="GoodsMovementType = '101'">
							<xsl:value-of select="'101'"/>
							<xsl:message select="'GoodsMovementType ist 101.'"/>
						</xsl:when>
						<xsl:when test="GoodsMovementType = '201'">
							<xsl:value-of select="'201'"/>
							<xsl:message select="'GoodsMovementType ist 201.'"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="'other'"/>
							<xsl:message select="'GoodsMovementType ist ein anderer.'"/>
						</xsl:otherwise>
					</xsl:choose>
				</value>
			</xsl:for-each>
		</root>
	</xsl:template>
</xsl:stylesheet>