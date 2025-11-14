<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://ns.hr-xml.org/2004-08-02"  xmlns:ns2="http://ns.epredix.com/2004-12-01" xmlns:ns1="http://ns.epredix.com/2004-12-01">
	 <xsl:template match="node()|@*">
     <xsl:copy>
       <xsl:apply-templates select="node()|@*"/>
     </xsl:copy>
 </xsl:template>
  <xsl:template match="ns1:*">
   <xsl:element name="{local-name()}">
    <xsl:copy-of select="namespace::*[not(. = namespace-uri(..))]"/>
    <xsl:apply-templates select="node()|@*"/>
  </xsl:element>
  </xsl:template>
</xsl:stylesheet>