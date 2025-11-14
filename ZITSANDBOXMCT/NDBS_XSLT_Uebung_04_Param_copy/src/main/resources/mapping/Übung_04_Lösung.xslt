<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:param name="Z_SNDPRO"/>
	<xsl:param name="Z_SNDPRN"/>
	<xsl:param name="Z_RCVPRN"/>
	<xsl:template match="/">
		<xsl:for-each select="ACC_DOCUMENT05">
			<ACC_DOCUMENT05>
				<IDOC BEGIN="1">
					<EDI_DC40 SEGMENT="1">
						<TABNAM>
							<xsl:value-of select="'EDI_DC40'"/>
						</TABNAM>
						<DIRECT>
							<xsl:value-of select="'2'"/>
						</DIRECT>
						<IDOCTYP>
							<xsl:value-of select="'ACC_DOCUMENT05'"/>
						</IDOCTYP>
						<MESTYP>
							<xsl:value-of select="'ACC_DOCUMENT'"/>
						</MESTYP>
						<SNDPOR>
							<xsl:value-of select="$Z_SNDPRO"/>
						</SNDPOR>
						<SNDPRT>
							<xsl:value-of select="'LS'"/>
						</SNDPRT>
						<SNDPRN>
							<xsl:value-of select="$Z_SNDPRN"/>
						</SNDPRN>
						<RCVPOR>
							<xsl:value-of select="''"/>
						</RCVPOR>
						<RCVPRT>
							<xsl:value-of select="'LS'"/>
						</RCVPRT>
						<RCVPRN>
							<xsl:value-of select="$Z_RCVPRN"/>
						</RCVPRN>
					</EDI_DC40>
					<xsl:for-each select="IDOC/E1BPACCR09">
						<E1BPACCR09 SEGMENT="1">
							<xsl:copy-of select="node()"/>
						</E1BPACCR09>
					</xsl:for-each>
				</IDOC>
			</ACC_DOCUMENT05>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>