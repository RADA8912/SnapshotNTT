<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:cpi="http://sap.com/it/" exclude-result-prefixes="cpi">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:param name="exchange"/>
	<xsl:template match="/">
		<root>
			<xsl:value-of select="cpi:setHeader($exchange, 'Z_Header_XSLT', 'Header Value')"/>
			<xsl:value-of select="cpi:setProperty($exchange, 'Z_Property_XSLT', 'Property Value')"/>
		</root>
	</xsl:template>
</xsl:stylesheet>