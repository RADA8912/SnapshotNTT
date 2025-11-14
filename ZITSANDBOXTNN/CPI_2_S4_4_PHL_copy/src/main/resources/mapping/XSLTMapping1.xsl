<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="CONF_ACTIVITY1"/>
	<xsl:template match="CONF_ACTI_UNIT1"/>
	<xsl:template match="CONF_ACTIVITY2"/>
	<xsl:template match="CONF_ACTI_UNIT2"/>
</xsl:stylesheet>
