
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" indent="yes"/>
    
    <xsl:template match="/RawMaterial">
        <RawMaterial>
            <xsl:for-each select="RawMaterialType">
                <RawMaterialType>
                    <InspectionLot>
                        <xsl:value-of select="InspectionLot"/>
                    </InspectionLot>				
                    <Material>
                        <xsl:value-of select="Material"/>
                    </Material>
					<MaterialText>
						<xsl:value-of select="MaterialText"/>
					</MaterialText>
                    <Batch>
                        <xsl:value-of select="Batch"/>
                    </Batch>
                    <NetWeightTotal>
                        <xsl:value-of select="NetWeightTotal"/>
                    </NetWeightTotal>
                    <WeightUnit>
                        <xsl:value-of select="WeightUnit"/>
                    </WeightUnit>
                    <CreatedOnTimestamp>
                        <xsl:value-of select="CreatedOnTimestamp"/>
                    </CreatedOnTimestamp>
                    <CreatedOnYearMonth>
                        <xsl:value-of select="CreatedOnYearMonth"/>
                    </CreatedOnYearMonth>
                </RawMaterialType>
            </xsl:for-each>
        </RawMaterial>
    </xsl:template>
</xsl:stylesheet>
