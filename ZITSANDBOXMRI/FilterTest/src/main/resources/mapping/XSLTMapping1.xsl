<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="root">
    <xsl:copy>
      <xsl:apply-templates select="entry"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="entry">
    <xsl:variable name="id" select="id"/>
    <xsl:element name="entry">
      <xsl:copy-of select="id"/>
      <indicator><xsl:value-of select="name(.)"/></indicator>
      <entry><xsl:value-of select="name"/></entry>
    </xsl:element>
    <xsl:element name="entry">
      <xsl:copy-of select="id"/>
      <indicator><xsl:value-of select="name1"/></indicator>
      <entry><xsl:value-of select="name1"/></entry>
    </xsl:element>
  </xsl:template>

</xsl:stylesheet>






