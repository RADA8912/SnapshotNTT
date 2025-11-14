<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dp="http://www.datapower.com/extensions" xmlns:dpconfig="http://www.datapower.com/param/config" extension-element-prefixes="dp" exclude-result-prefixes="dp dpconfig">

<xsl:output method="xml" indent="yes"/>

<xsl:strip-space elements="*"/>
<xsl:template match="/">

<xsl:copy-of select="."/>
</xsl:template>
</xsl:stylesheet>