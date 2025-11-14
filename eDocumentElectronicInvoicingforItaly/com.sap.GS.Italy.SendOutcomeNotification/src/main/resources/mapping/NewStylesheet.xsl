<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:typ="http://www.fatturapa.gov.it/sdi/messaggi/v1.0">

	<xsl:output method="xml" indent="yes"/>
	
	<xsl:template match="@*">
    	<xsl:copy copy-namespaces="no">
      		<xsl:apply-templates select="@*"/>
    	</xsl:copy>
 	</xsl:template>
 	 

 	<xsl:template match="/*">
    <xsl:element name="typ:{local-name()}" namespace="http://www.fatturapa.gov.it/sdi/messaggi/v1.0">
    <xsl:namespace name="xsi" select="'http://www.w3.org/2001/XMLSchema-instance'"/>
      <xsl:apply-templates select="@* | node()" />
    </xsl:element>
  </xsl:template>

  <xsl:template match="*/*">
    <xsl:element name="{local-name()}">
      <xsl:apply-templates select="@* | node()" />
    </xsl:element>
  </xsl:template>
  
</xsl:stylesheet>