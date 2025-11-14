<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:strip-space elements="*"/>
    <xsl:output method="xml" indent="yes"/>

    <xsl:variable name="created" select="//BusinessPartnerRelationshipSUITEReplicateConfirmationMessage/Log[Item/TypeID = 'S030(MDG_BS_BP_DATAREPL)']"/>

    <xsl:template match="@*|node()">

        <xsl:choose>
            <xsl:when test="$created != 'null'">
                <xsl:for-each select="$created">
                    <xsl:copy>
                        <xsl:copy-of select="../."/>
                    </xsl:copy>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
              	<xsl:copy>
	               	<xsl:apply-templates select="node()|@*" />
	            </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

</xsl:stylesheet>