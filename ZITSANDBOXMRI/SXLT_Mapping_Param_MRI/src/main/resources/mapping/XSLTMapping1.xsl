<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:param name="$Z_Filte"/>
<xsl:template match="/">

	<xsl:choose>
		<xsl:when test="$Z_Filter= 'Order'">
			<xsl:copy-of select ="//E1BPACCR09" copy-namespaces="no" />
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="'Fehler'"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
</xsl:stylesheet>