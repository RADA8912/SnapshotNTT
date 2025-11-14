<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="xml" indent="yes"/>

  <!-- Gruppierungsschlüssel nach MATYP -->
  <xsl:key name="matypKey" match="row" use="MATYP"/>

  <!-- Einstiegspunkt ohne Namespace -->
  <xsl:template match="/Messages">
    <MT_TopSpareparts_Grouped_Baan>
      <xsl:for-each select="Message1/MT_TopSpareparts_Baan/row[generate-id() = generate-id(key('matypKey', MATYP)[1])]">
        <MACHINE>
        <MATYP>
          <xsl:value-of select="MATYP"/>
        </MATYP>
        <xsl:for-each select="key('matypKey', MATYP)">
          <row>
            <Details>
              <ID><xsl:value-of select="ID"/></ID>
              <ARTICLE><xsl:value-of select="ARTICLE"/></ARTICLE>
              <ROWID><xsl:value-of select="ROWID"/></ROWID>
              <NAME><xsl:value-of select="NAME"/></NAME>
              <STOCK><xsl:value-of select="STOCK"/></STOCK>
              <VKTEXTDE><xsl:value-of select="VKTEXTDE"/></VKTEXTDE>
              <VKTEXTEN><xsl:value-of select="VKTEXTEN"/></VKTEXTEN>
              <ACTION><xsl:value-of select="ACTION"/></ACTION>
              <TIMESTAMP><xsl:value-of select="TIMESTAMP"/></TIMESTAMP>
              <PIPOTIMESTAMP><xsl:value-of select="PIPOTIMESTAMP"/></PIPOTIMESTAMP>
              <STATUS><xsl:value-of select="STATUS"/></STATUS>
              <ERRORMESSAGE><xsl:value-of select="ERRORMESSAGE"/></ERRORMESSAGE>
            </Details>
          </row>
        </xsl:for-each>
        </MACHINE>
      </xsl:for-each>
    </MT_TopSpareparts_Grouped_Baan>
  </xsl:template>
</xsl:stylesheet>