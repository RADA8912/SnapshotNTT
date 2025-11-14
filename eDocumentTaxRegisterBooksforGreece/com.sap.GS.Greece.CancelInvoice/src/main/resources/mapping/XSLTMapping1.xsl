<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"/>
	
	<xsl:template match="@*">
    	<xsl:copy copy-namespaces="no">
      		<xsl:apply-templates select="@*"/>
    	</xsl:copy>
 	</xsl:template>
 	
	<!-- Copy elements -->
	<xsl:template match="*" priority="-1">
		<xsl:element name="{local-name()}">
			<xsl:apply-templates select="node()|@*" />
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
