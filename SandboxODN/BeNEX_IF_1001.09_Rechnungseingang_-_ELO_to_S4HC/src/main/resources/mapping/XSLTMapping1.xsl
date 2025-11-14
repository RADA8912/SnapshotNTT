<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>
  
  <!-- Identity template - copies all nodes as-is -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
  
  <!-- Template to ignore the <d> node and copy its children -->
  <xsl:template match="d">
    <xsl:apply-templates/>
  </xsl:template>
</xsl:stylesheet>