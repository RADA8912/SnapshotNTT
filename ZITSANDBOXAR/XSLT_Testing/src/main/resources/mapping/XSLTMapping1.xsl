<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:param name="z_name"/>
	
	<xsl:template match="/">
		<!-- TODO: Auto-generated template -->

		
		<Mein_Name>
		    <xsl:value-of select="$z_name"/>
		</Mein_Name>
		
	</xsl:template>
	
</xsl:stylesheet>
