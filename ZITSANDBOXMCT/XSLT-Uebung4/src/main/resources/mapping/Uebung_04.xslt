<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />

	<xsl:param name="RCVPRN" />
	<xsl:param name="RCVPOR" />
	<xsl:param name="SNDPOR" />
	<xsl:param name="SNDPRN" />

	<xsl:template match="/">
		<ACC_DOCUMENT>
			<IDOC>
				<EDI_DC40>
					<TABNAM>
					</TABNAM>
					<DIRECT>
					</DIRECT>
					<RCVPRN>
						<xsl:value-of select="$RCVPRN" />
					</RCVPRN>
					<SNDPOR>
						<xsl:value-of select="$SNDPOR" />
					</SNDPOR>
					<RCVPOR>
						<xsl:value-of select="$RCVPOR" />
					</RCVPOR>
					<SNDPRN>
						<xsl:value-of select="$SNDPRN" />
					</SNDPRN>
				</EDI_DC40>
				<E1BPACHE09>
					<xsl:copy-of select="ACC_DOCUMENT05/IDOC/E1BPACHE09/*" />
				</E1BPACHE09>
			</IDOC>
		</ACC_DOCUMENT>
	</xsl:template>
</xsl:stylesheet>