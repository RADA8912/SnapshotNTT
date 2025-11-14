<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" />
	<xsl:template match="/OrderCancelNotification">
INSERT_UPDATE saporder;<xsl:value-of select="code/@header" />;<xsl:value-of select="translatorEventCall/@header" />
;<xsl:value-of select="code" />;<xsl:value-of select="translatorEventCall" />
	</xsl:template>
</xsl:stylesheet>
