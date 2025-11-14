<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:nm="http://sap.com/xi/XI/SplitAndMerge">
	<xsl:output exclude-result-prefixes="nm"/>
	<xsl:template match="/">
		<xsl:copy-of copy-namespaces="no" select="//*[parent::nm:Message1]"/>
	</xsl:template>
</xsl:stylesheet>
