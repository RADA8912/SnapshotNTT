<?xml version="1.0" encoding="utf-8"?>
<!--
|Document:		basic-string-pool.xsl
|Last Modified:	15.07.2012
|Editor:		SGR
|Description:	Basic string pool templates for XSLT 1.0 and XPath 1.0
|Version:		1.0.0
-->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:bsp="http://itelligence.de/basic-string-pool"
	extension-element-prefixes="bsp">

	<!-- Template 'bsp:to-lower-case' -->
	<!-- Convert all characters in string to lower case. -->
	<xsl:template name="bsp:to-lower-case">
		<!-- Input Parameter -->
		<!-- Value as string -->
		<xsl:param name="value"/>
		<!-- Template -->
		<xsl:choose>
			<!-- Convert all characters in string to lower case -->
			<xsl:when test="$value!=''">
				<xsl:value-of select="translate($value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<!-- Template 'bsp:to-upper-case' -->
	<!-- Convert all characters in string to upper case. -->
	<xsl:template name="bsp:to-upper-case">
		<!-- Input Parameter -->
		<!-- Value as string -->
		<xsl:param name="value"/>
		<!-- Template -->
		<xsl:choose>
			<!-- Convert all characters in string to upper case -->
			<xsl:when test="$value!=''">
				<xsl:value-of select="translate($value,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>