<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output encoding="UTF-8" indent="no" method="xml" omit-xml-declaration="yes" version="1.0"/>
	<xsl:template match="/">
		<MATMAS05>
		    <E1MARAM>
		        <xsl:copy-of select="/MATMAS05/IDOC/E1MARAM/(MATNR)"/>
		       <E1MVKEM>
		           <VKORG>Default</VKORG>
		           <VTWEG></VTWEG>
				</E1MVKEM>
				<xsl:for-each select="/MATMAS05/IDOC/E1MARAM/E1MVKEM">
				    <E1MVKEM>
				    <VKORG><xsl:value-of select="VKORG" /></VKORG>
				    <VTWEG><xsl:value-of select="VTWEG" /></VTWEG>
				    </E1MVKEM>
			   </xsl:for-each>
			</E1MARAM>
		</MATMAS05>
	</xsl:template>
</xsl:stylesheet>