<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" />
	<xsl:template match="/SAPInboundProduct">
################CleanBaseProductAttributes####################
INSERT_UPDATE SAPInboundProduct;<xsl:value-of select="code/@header" />;<xsl:value-of select="catalogVersion/@header" />;<xsl:value-of select="translatorEventCall/@header" />
;<xsl:value-of select="code" />;<xsl:value-of select="catalogVersion" />;<xsl:value-of select="translatorEventCall" />
################CleanBaseProductAttributes####################
	</xsl:template>
</xsl:stylesheet>
