<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:atom="http://www.w3.org/2005/Atom"
    xmlns:m="http://schemas.microsoft.com/ado/2007/08/dataservices/metadata"
    xmlns:d="http://schemas.microsoft.com/ado/2007/08/dataservices"
    exclude-result-prefixes="atom m d">

    <!-- Output the XML declaration and root element -->
    <xsl:output method="xml" indent="yes"/>

    <!-- Template to match the root <feed> element -->
    <xsl:template match="atom:feed">
        <!-- Iterate through each <entry> element and its <m:properties> child -->
        <xsl:for-each select="atom:entry">
            <split-segment>
                <!-- Copy the entry excluding other parts except <m:properties> -->
                <xsl:copy-of select="atom:content/m:m:properties"/>
            </split-segment>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
