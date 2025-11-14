<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<ROOT>
			<xsl:for-each select="//field">
				<xsl:if test=". != ''">
					<SEGMENT>
						<FELD1>
							<xsl:value-of select="fn:translate(fn:substring(.,1,2), ' ', '')"/>
						</FELD1>
						<FELD2>
							<xsl:value-of select="fn:translate(fn:substring(.,3,20), ' ', '')"/>
						</FELD2>
						<FELD3>
							<xsl:value-of select="fn:translate(fn:substring(.,23,20), ' ', '')"/>
						</FELD3>
						<FELD4>
							<xsl:value-of select="fn:translate(fn:substring(.,43,15), ' ', '')"/>
						</FELD4>
						<FELD5>
							<xsl:value-of select="fn:translate(fn:substring(.,58,8), ' ', '')"/>
						</FELD5>
						<FELD6>
							<xsl:value-of select="fn:translate(fn:substring(.,66,6), ' ', '')"/>
						</FELD6>
						<FELD7>
							<xsl:value-of select="fn:translate(fn:substring(.,72,20), ' ', '')"/>
						</FELD7>
						<FELD8>
							<xsl:value-of select="fn:translate(fn:substring(.,92,20), ' ', '')"/>
						</FELD8>
						<FELD9>
							<xsl:value-of select="fn:translate(fn:substring(.,112,7), ' ', '')"/>
						</FELD9>
						<FELD10>
							<xsl:value-of select="fn:translate(fn:substring(.,119,7), ' ', '')"/>
						</FELD10>
						<FELD11>
							<xsl:value-of select="fn:translate(fn:substring(.,126,5), ' ', '')"/>
						</FELD11>
						<FELD12>
							<xsl:value-of select="fn:translate(fn:substring(.,131,2), ' ', '')"/>
						</FELD12>
						<FELD13>
							<xsl:value-of select="fn:translate(fn:substring(.,133,80), ' ', '')"/>
						</FELD13>
					</SEGMENT>
				</xsl:if>
			</xsl:for-each>
		</ROOT>
	</xsl:template>
</xsl:stylesheet>