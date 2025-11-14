<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />
		<xsl:param name="SNDPOR"/>
		<xsl:param name="SNDPRN"/>
		<xsl:param name="RCVPRN"/>
		
		<xsl:template match="/ACC_DOCUMENT05">

			<ACC_DOCUMENT05>
			<IDOC BEGIN="1">
				<xsl:for-each select="IDOC/EDI_DC40">
					<EDI_DC40 SEGMENT="1">
						<xsl:for-each select="*">
							<xsl:choose>
								<xsl:when test="name(.) = 'SNDPOR'">
									<SNDPOR><xsl:value-of select="$SNDPOR"/></SNDPOR>
								</xsl:when>
								<xsl:when test="name(.) = 'SNDPRN'">
									<SNDPRN><xsl:value-of select="$SNDPRN"/></SNDPRN>
								</xsl:when>
								<xsl:when test="name(.) = 'RCVPRN'">
									<RCVPRN><xsl:value-of select="$RCVPRN"/></RCVPRN>
								</xsl:when>
								<xsl:otherwise>
									<xsl:copy-of select="." copy-namespaces="no" />
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</EDI_DC40>
				</xsl:for-each>
				
				<xsl:copy-of select="IDOC/E1BPACCR09" copy-namespaces="no"/>
				
			</IDOC>
		</ACC_DOCUMENT05>
	</xsl:template>
</xsl:stylesheet>