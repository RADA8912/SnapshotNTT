<?xml version="1.0" encoding="utf-8"?>
<!--
|Document:		test_template_to-lower-case_xslt_1.0_xpath_1.0.xsl
|Last Modified:	15.07.2012
|Editor:		SGR
|Description:	Test of template 'bsp:to-lower-case'
|Version:		1.0.0
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:bsp="http://itelligence.de/basic-string-pool" exclude-result-prefixes="bsp">
	<xsl:output method="xml" indent="yes"/>
	<xsl:include href="basic-string-pool.xslt"/>
	<!-- Test template -->
	<xsl:template match="/">
		<values>
			<xsl:for-each select="Message/Values">
				<valuelist>
					<input>
						<xsl:value-of select="Input"/>
					</input>
					<output>
						<xsl:call-template name="bsp:to-lower-case">
							<xsl:with-param name="value">
								<xsl:value-of select="Input"/>
							</xsl:with-param>
						</xsl:call-template>
					</output>
				</valuelist>
			</xsl:for-each>
		</values>
	</xsl:template>
</xsl:stylesheet>