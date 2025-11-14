<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:strip-space elements="*"/>
	<xsl:param name="Z_LAST_CHANGE_DATE" select="'2025-06-24T18:56:11.000Z'"/>
	<xsl:template match="/">
		<root>
			<xsl:copy-of select="node()"/>
			<xsl:for-each select="//job_information/last_modified_on">
				<xsl:variable name="LastModifiedDate_converted">
					<xsl:call-template name="string-to-date-time-converter">
						<xsl:with-param name="value" select="."/>
						<xsl:with-param name="format" select="'YYYYMMDDhhmm'"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="LastRun_converted">
					<xsl:call-template name="string-to-date-time-converter">
						<xsl:with-param name="value" select="$Z_LAST_CHANGE_DATE"/>
						<xsl:with-param name="format" select="'YYYYMMDDhhmm'"/>
					</xsl:call-template>
				</xsl:variable>
				<checkLastModifiedOn>
					<xsl:choose>
						<xsl:when test="$LastModifiedDate_converted > $LastRun_converted">
							<xsl:value-of select="'true'"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="'false'"/>
						</xsl:otherwise>
					</xsl:choose>
				</checkLastModifiedOn>
			</xsl:for-each>
		</root>
	</xsl:template>
	<xsl:template name="string-to-date-time-converter">
		<xsl:param name="value"/>
		<xsl:param name="format"/>
		<xsl:variable name="dateValue" select="fn:concat($value,'000000000000000000000000000000000000')"/>
		<xsl:variable name="year" select="fn:substring($dateValue,1,4)"/>
		<xsl:variable name="month" select="fn:substring($dateValue,6,2)"/>
		<xsl:variable name="day" select="fn:substring($dateValue,9,2)"/>
		<xsl:variable name="hour" select="fn:substring($dateValue,12,2)"/>
		<xsl:variable name="minute" select="fn:substring($dateValue,15,2)"/>
		<xsl:variable name="secound" select="fn:substring($dateValue,18,2)"/>
		<xsl:choose>
			<xsl:when test="$format = 'YYYYMMDDhhmm'">
				<xsl:value-of select="fn:concat($year,$month,$day,$hour,$minute)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="fn:concat($year,'-',$month,'-',$day,'T',$hour,':',$minute,':',$secound,'.0Z')"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>