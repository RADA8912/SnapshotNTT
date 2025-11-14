<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:m="http://schemas.microsoft.com/ado/2007/08/dataservices/metadata"
                xmlns:d="http://schemas.microsoft.com/ado/2007/08/dataservices">
  
  <!-- Leere Vorlage, um alle anderen Elemente zu ignorieren -->
  <xsl:template match="*|@*">
    <xsl:apply-templates select="entry"/>
  </xsl:template>

  <!-- Vorlage für das <entry>-Element -->
  <xsl:template match="entry">
    <xsl:copy>
      <xsl:apply-templates select="m:properties/d:*"/>
    </xsl:copy>
    <xsl:text>&#10;</xsl:text> <!-- Zeilenumbruch nach jedem entry -->
  </xsl:template>

  <!-- Vorlage für die untergeordneten Elemente in <entry> -->
  <xsl:template match="m:properties/d:*">
    <xsl:value-of select="."/>
  </xsl:template>

</xsl:stylesheet>
