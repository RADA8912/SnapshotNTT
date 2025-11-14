<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" indent="yes"/>
	<xsl:template match="ZGSVERF03/IDOC/E1EDK01">
		<E1EDK01>
			<xsl:apply-templates>
				<xsl:sort select="INVOICE_PDF_COUNTER" data-type="number"/>
			</xsl:apply-templates>
		</E1EDK01>
	</xsl:template>
	<xsl:template match="*">
		<xsl:copy>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>