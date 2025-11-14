<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" indent="yes" encoding="UTF-8"/>
 <!--  ISO-8859-1 US-ASCII UTF-8 encoding="Shift_JIS"  -->
 <!--

   http://lists.xml.org/archives/xml-dev/201212/msg00059.html
 
-->
 <!--  Identity transform  -->
<xsl:template match="@* | node()">
<xsl:copy>
<xsl:apply-templates select="@* | node()"/>
</xsl:copy>
</xsl:template>
</xsl:stylesheet>