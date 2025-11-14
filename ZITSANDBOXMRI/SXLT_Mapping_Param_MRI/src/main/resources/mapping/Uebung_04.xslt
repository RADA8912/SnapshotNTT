<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:param name="Z_SNDPOR"/>
	<xsl:param name="Z_SNDPRN"/>
	<xsl:param name="Z_RCVPRN"/>
	<xsl:template match="/">
		<ACC_DOCUMENT05>
			<IDOC BEGIN="1">
				<EDI_DC40 SEGMENT="1">
					<TABNAM>
						<xsl:value-of select="ACC_DOCUMENT05/IDOC/EDI_DC40/TABNAM"/>
					</TABNAM>
					<DIRECT>
						<xsl:value-of select="ACC_DOCUMENT05/IDOC/EDI_DC40/DIRECT"/>
					</DIRECT>
					<IDOCTYP>
						<xsl:value-of select="ACC_DOCUMENT05/IDOC/EDI_DC40/IDOCTYP"/>
					</IDOCTYP>
					<MESTYP>
						<xsl:value-of select="ACC_DOCUMENT05/IDOC/EDI_DC40/MESTYP"/>
					</MESTYP>
					<SNDPOR>
						<xsl:value-of select="$Z_SNDPOR"/>
					</SNDPOR>
					<SNDPRT>
						<xsl:value-of select="ACC_DOCUMENT05/IDOC/EDI_DC40/SNDPRT"/>
					</SNDPRT>
					<SNDPRN>
						<xsl:value-of select="$Z_SNDPRN"/>
					</SNDPRN>
					<RCVPOR>
						<xsl:value-of select="ACC_DOCUMENT05/IDOC/EDI_DC40/RCVPOR"/>
					</RCVPOR>
					<RCVPRT>
						<xsl:value-of select="ACC_DOCUMENT05/IDOC/EDI_DC40/RCVPRT"/>
					</RCVPRT>
					<RCVPRN>
						<xsl:value-of select="$Z_RCVPRN"/>
					</RCVPRN>
				</EDI_DC40>
				<xsl:copy-of select ="ACC_DOCUMENT05/IDOC/E1BPACCR09" copy-namespaces="no" />
			</IDOC>
		</ACC_DOCUMENT05>
	</xsl:template>
</xsl:stylesheet>