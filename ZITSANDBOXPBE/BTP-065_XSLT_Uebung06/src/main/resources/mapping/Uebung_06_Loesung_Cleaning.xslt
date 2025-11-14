<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<root>
			<xsl:for-each select="root/row">
				<row>
					<xsl:copy-of select="node()"/>
				</row>
			</xsl:for-each>
		</root>
	</xsl:template>
</xsl:stylesheet>