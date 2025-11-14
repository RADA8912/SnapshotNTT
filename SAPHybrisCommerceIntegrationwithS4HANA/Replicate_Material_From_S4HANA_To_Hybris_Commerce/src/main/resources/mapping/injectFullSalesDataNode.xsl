<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="fullSalesDataNode" />

<!-- Identity transform -->
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>
	<xsl:template match="/MATMAS05/IDOC/E1MARAM">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
			<xsl:value-of select="$fullSalesDataNode" disable-output-escaping="yes"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>