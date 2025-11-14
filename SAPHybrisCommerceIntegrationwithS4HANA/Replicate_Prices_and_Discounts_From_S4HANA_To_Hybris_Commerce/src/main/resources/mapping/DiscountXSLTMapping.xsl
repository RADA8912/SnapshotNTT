<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" />
	<xsl:template match="/DiscountRow">
INSERT_UPDATE DiscountRow;;<xsl:value-of select="productId/@header" />;<xsl:value-of select="amount/@header" />;<xsl:value-of select="conditionType/@header" />;<xsl:value-of select="catalogVersion/@header" />;<xsl:value-of select="userGroup/@header" />;<xsl:value-of select="productGroup/@header" />;<xsl:value-of select="conditionId/@header" />;<xsl:value-of select="startDate/@header" />;<xsl:value-of select="endDate/@header" />;<xsl:value-of select="currency/@header" />
;;<xsl:value-of select="productId" />;<xsl:value-of select="amount" />;<xsl:value-of select="conditionType" />;<xsl:value-of select="catalogVersion" />;<xsl:value-of select="userGroup" />;<xsl:value-of select="productGroup" />;<xsl:value-of select="conditionId" />;<xsl:value-of select="startDate" />;<xsl:value-of select="endDate" />;<xsl:value-of select="currency" />
	</xsl:template>
</xsl:stylesheet>