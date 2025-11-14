<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ser-root1="http://host/ws/createContract"
	xmlns:ns1="urn:sap-com:document:sap:rfc:functions"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:output omit-xml-declaration="yes" indent="yes"/>
	<xsl:template match="/MaterialData">
		<xsl:copy-of select="node()" />
	</xsl:template>
</xsl:stylesheet>