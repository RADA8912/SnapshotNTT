
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" indent="yes"/>
    
    <xsl:template match="/Production">
        <Production>
            <xsl:for-each select="ProductionType">
                <ProductionType>
                    <InspectionLot>
                        <xsl:value-of select="InspectionLot"/>
                    </InspectionLot>
                    <HandlingUnit>
                        <xsl:value-of select="HandlingUnit"/>
                    </HandlingUnit>
                    <Machine>
                        <xsl:value-of select="Machine"/>
                    </Machine>
                    <Material>
                        <xsl:value-of select="Material"/>
                    </Material>
                    <Batch>
                        <xsl:value-of select="Batch"/>
                    </Batch>
                    <ManufacturingOrder>
                        <xsl:value-of select="ManufacturingOrder"/>
                    </ManufacturingOrder>
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
                </ProductionType>
            </xsl:for-each>
        </Production>
    </xsl:template>
</xsl:stylesheet>
