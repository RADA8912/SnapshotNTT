<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" />
    <xsl:template match="/">
        <root>
            <xsl:for-each select="multimap:Messages/multimap:Message1">
                <xsl:copy-of select="node()" />
            </xsl:for-each>
        </root>
    </xsl:template>
</xsl:stylesheet>