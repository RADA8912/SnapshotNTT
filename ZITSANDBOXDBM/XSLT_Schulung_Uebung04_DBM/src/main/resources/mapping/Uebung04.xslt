<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />

	<xsl:param name="SNDPOR" />
	<xsl:param name="SNDPRN" />
	<xsl:param name="RCVPRN" />

	<xsl:template match="/">
		<ACC_DOCUMENT05>
			<IDOC BEGIN="1">
				<xsl:for-each select="ACC_DOCUMENT05/IDOC/EDI_DC40">
					<EDI_DC40 SEGMENT="1">
						<TABNAM>
							<xsl:value-of select="TABNAM" />
						</TABNAM>
						<DIRECT>
							<xsl:value-of select="'2'" />
						</DIRECT>
						<IDOCTYP>
							<xsl:value-of select="IDOCTYP" />
						</IDOCTYP>
						<MESTYP>
							<xsl:value-of select="MESTYP" />
						</MESTYP>

						<SNDPOR>
							<xsl:value-of select="$SNDPOR" />
						</SNDPOR>

						<SNDPRT>
							<xsl:value-of select="'LS'" />
						</SNDPRT>

						<SNDPRN>
							<xsl:value-of select="$SNDPRN" />
						</SNDPRN>

						<RCVPOR>
						</RCVPOR>

						<RCVPRT>
							<xsl:value-of select="'LS'" />
						</RCVPRT>

						<RCVPRN>
							<xsl:value-of select="RCVPRN" />
						</RCVPRN>
					</EDI_DC40>
				</xsl:for-each>
				<!-- weitere Segmente -->
			</IDOC>
		</ACC_DOCUMENT05>
	</xsl:template>
</xsl:stylesheet>