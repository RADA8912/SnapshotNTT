<?xml version="1.0" encoding="UTF-8"?>
<!-- 
	Remove Empty Fields
	Generates an XML output without empty fields
	
	@author nttdata-solutions.com
	@version 1.0.0 
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="*[not(child::node())]"/>
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>