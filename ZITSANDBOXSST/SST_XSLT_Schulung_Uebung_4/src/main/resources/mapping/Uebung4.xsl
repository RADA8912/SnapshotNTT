<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	
	<xsl:param name="SNDPOR"/>
	<xsl:param name="SNDPRN"/>
	<xsl:param name="RCVPRN"/>
	
	<xsl:template match="/">
		<ACC_DOCUMENT05>
			<IDOC BEGIN="1">
				<EDI_DC40 SEGMENT="1">
					<TABNAM><xsl:value-of select="'EDI_DC40'"/></TABNAM>
					<DIRECT><xsl:value-of select="'2'"/></DIRECT>
					<IDOCTYP><xsl:value-of select="'ACC_DOCUMENT05'"/></IDOCTYP>
					<MESTYP><xsl:value-of select="'ACC_DOCUMENT'"/></MESTYP>
					<SNDPOR><xsl:value-of select="$SNDPOR"/></SNDPOR>
					<SNDPRT><xsl:value-of select="'LS'"/></SNDPRT>
					<SNDPRN><xsl:value-of select="$SNDPRN"/></SNDPRN>
					<RCVPOR><xsl:value-of select="''"/></RCVPOR>
					<RCVPRT><xsl:value-of select="'LS'"/></RCVPRT>
					<RCVPRN><xsl:value-of select="$RCVPRN"/></RCVPRN>
				</EDI_DC40>
				<E1BPACHE09><xsl:copy-of select="ACC_DOCUMENT05/IDOC/E1BPACHE09/*"/></E1BPACHE09>
				<E1BPACGL09><xsl:copy-of select="ACC_DOCUMENT05/IDOC/E1BPACGL09/*"/></E1BPACGL09>
				<E1BPACAP09><xsl:copy-of select="ACC_DOCUMENT05/IDOC/E1BPACAP09/*"/></E1BPACAP09>
				<E1BPACTX09><xsl:copy-of select="ACC_DOCUMENT05/IDOC/E1BPACTX09/*"/></E1BPACTX09>
				<xsl:for-each select="ACC_DOCUMENT05/IDOC/E1BPACCR09">
					<E1BPACCR09><xsl:copy-of select="*"/></E1BPACCR09>
				</xsl:for-each>
			</IDOC>
		</ACC_DOCUMENT05>
	</xsl:template>
</xsl:stylesheet>