<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:cpi="http://sap.com/it/" exclude-result-prefixes="cpi">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:param name="Z_SNDPOR"/>
	<xsl:param name="Z_SNDPRN"/>
	<xsl:param name="Z_RCVPRN"/>
	<xsl:param name="exchange"/>
	<xsl:template match="/ACC_DOCUMENT05/IDOC">
		<ACC_DOCUMENT05>
		<xsl:value-of select="cpi:setHeader($exchange, 'Content-Type', 'text/xml')"/>
			<IDOC BEGIN="1">
				<EDI_DC40 SEGMENT="1">
					<TABNAM>
						<xsl:value-of select="EDI_DC40/TABNAM"/>
					</TABNAM>
					<DIRECT>
						<xsl:value-of select="EDI_DC40/DIRECT"/>
					</DIRECT>
					<IDOCTYP>
						<xsl:value-of select="EDI_DC40/IDOCTYP"/>
					</IDOCTYP>
					<MESTYP>
						<xsl:value-of select="EDI_DC40/MESTYP"/>
					</MESTYP>
					<SNDPOR>
						<xsl:value-of select="$Z_SNDPOR"/>
					</SNDPOR>
					<SNDPRT>
						<xsl:value-of select="EDI_DC40/SNDPRT"/>
					</SNDPRT>
					<SNDPRN>
						<xsl:value-of select="$Z_SNDPRN"/>
					</SNDPRN>
					<RCVPOR>
						<xsl:value-of select="EDI_DC40/RCVPOR"/>
					</RCVPOR>
					<RCVPRT>
						<xsl:value-of select="EDI_DC40/RCVPRT"/>
					</RCVPRT>
					<RCVPRN>
						<xsl:value-of select="$Z_RCVPRN"/>
					</RCVPRN>
				</EDI_DC40>
				<xsl:for-each select="E1BPACCR09">
					<E1BPACCR09 SEGMENT="1">
						<xsl:copy-of select="node()"/>
					</E1BPACCR09>
				</xsl:for-each>
			</IDOC>
		</ACC_DOCUMENT05>
	</xsl:template>
</xsl:stylesheet>