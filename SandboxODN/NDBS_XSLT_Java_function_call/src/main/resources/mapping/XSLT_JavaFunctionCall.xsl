<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/" xmlns:date="java:java.util.Date">
		<root>
			<xsl:value-of select="date:new()"/>
		</root>
	</xsl:template>
</xsl:stylesheet>