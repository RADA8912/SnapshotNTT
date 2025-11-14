<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" indent="yes" omit-xml-declaration="yes" />
<xsl:param name="TaxCountry"/>
  <xsl:template match="/">
      <xsl:apply-templates select="/MATMAS05/IDOC/E1MARAM/E1MLANM[ALAND=$TaxCountry]"/>
  </xsl:template>

  <xsl:template match="E1MLANM">
    <xsl:copy-of select="."/>
  </xsl:template>

</xsl:stylesheet>

