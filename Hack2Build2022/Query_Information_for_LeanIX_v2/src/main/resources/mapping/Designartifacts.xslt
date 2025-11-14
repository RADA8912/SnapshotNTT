<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
  
  <xsl:template match="/">
    <IntegrationRuntimeArtifact>
      <Status>
        <xsl:value-of select="//Status"/>
      </Status>
      <Type>
        <xsl:value-of select="//Type"/>
      </Type>
      <Version></Version>
      <DeployedBy></DeployedBy>
      <Id>
        <xsl:value-of select="//Id"/>
      </Id>
      <Sender/>
      <ModifiedAt>
        <xsl:value-of select="//ModifiedAt"/>
      </ModifiedAt>
      <Description>
        <xsl:value-of select="//Description"/>
      </Description>
      <CreatedBy>
        <xsl:value-of select="//CreatedBy"/>
      </CreatedBy>
      <Version>
        <xsl:value-of select="//Version"/>
      </Version>
      <CreatedAt>
        <xsl:value-of select="//CreatedAt"/>
      </CreatedAt>
      <Receiver/>
      <PackageId>
        <xsl:value-of select="//PackageId"/>
      </PackageId>
      <ModifiedBy>
        <xsl:value-of select="//ModifiedBy"/>
      </ModifiedBy>
      <Name>
        <xsl:value-of select="//Name"/>
      </Name>
      <DeployedOn>
        <xsl:value-of select="//DeployedOn"/>
      </DeployedOn>
    </IntegrationRuntimeArtifact>
  </xsl:template>
</xsl:stylesheet>
