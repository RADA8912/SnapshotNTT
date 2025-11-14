<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="node()|@*">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="//S_UNT/D_0074/text()[.='0']">
		<xsl:value-of select="count(//*[starts-with(name(), 'S_')][not(name() = 'S_UNA')][not(name() = 'S_UNB')][not(name() = 'S_UNG')][not(name() = 'S_UNE')][not(name() = 'S_UNZ')])"/>
	</xsl:template>
	<xsl:template match="//S_SE/D_96/text()[.='0']">
		<xsl:value-of select="count(//*[starts-with(name(), 'S_')][not(name() = 'S_ISA')][not(name() = 'S_GS')][not(name() = 'S_GE')][not(name() = 'S_IEA')])"/>
	</xsl:template>
</xsl:stylesheet>