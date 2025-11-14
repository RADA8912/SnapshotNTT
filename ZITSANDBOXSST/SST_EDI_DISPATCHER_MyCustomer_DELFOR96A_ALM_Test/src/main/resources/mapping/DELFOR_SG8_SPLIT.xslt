<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<envelope>
			<xsl:for-each select="M_DELFOR/G_SG4/G_SG8">
				<xsl:variable name="pos_envelope" select="fn:position()"/>
				<M_DELFOR>
					<xsl:for-each select="../../*">
						<xsl:choose>
							<xsl:when test="name() = 'G_SG4'">
								<xsl:for-each select="S_NAD">
									<xsl:copy-of select="fn:current()"/>
								</xsl:for-each>
								<xsl:for-each select="S_LOC">
									<xsl:copy-of select="fn:current()"/>
								</xsl:for-each>
								<xsl:for-each select="S_FTX">
									<xsl:copy-of select="fn:current()"/>
								</xsl:for-each>
								<xsl:for-each select="G_SG5">
									<xsl:copy-of select="fn:current()"/>
								</xsl:for-each>
								<xsl:for-each select="G_SG6">
									<xsl:copy-of select="fn:current()"/>
								</xsl:for-each>
								<xsl:for-each select="G_SG7">
									<xsl:copy-of select="fn:current()"/>
								</xsl:for-each>
								<xsl:for-each select="G_SG8[$pos_envelope]">
									<xsl:copy-of select="fn:current()"/>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>
								<xsl:copy-of select="fn:current()"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
				</M_DELFOR>
			</xsl:for-each>
		</envelope>
	</xsl:template>
</xsl:stylesheet>