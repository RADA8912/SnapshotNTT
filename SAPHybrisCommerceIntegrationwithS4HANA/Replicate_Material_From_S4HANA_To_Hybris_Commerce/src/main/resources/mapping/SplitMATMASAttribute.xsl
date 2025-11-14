<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="2.0">

<xsl:output indent="yes"/>
<xsl:strip-space elements="*"/>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    
<xsl:template match="/">
  <CUSTOMIZE>
  <xsl:for-each-group select="MATMAS05/IDOC/E1MARAM/E1CUCFG/E1CUVAL" group-by="CHARC">
        <xsl:apply-templates select="/*"/>
   </xsl:for-each-group>
   </CUSTOMIZE> 
</xsl:template>

<xsl:template match="MATMAS05/IDOC/E1MARAM/E1CUCFG/E1CUVAL">
  <xsl:if test=". intersect current-group()">
            <xsl:next-match/>
        </xsl:if>
</xsl:template>

</xsl:stylesheet>
