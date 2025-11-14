<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<ACC_DOCUMENT05>
			<IDOC BEGIN="1">
				<EDI_DC40 SEGMENT="1">
					<TABNAM/>
					<DIRECT/>
					<IDOCTYP/>
					<MESTYP/>
					<SNDPOR/>
					<SNDPRT/>
					<SNDPRN/>
					<RCVPOR/>
					<RCVPRT/>
					<RCVPRN/>
				</EDI_DC40>
				<!-- weitere Segmente -->
			</IDOC>
		</ACC_DOCUMENT05>
	</xsl:template>
</xsl:stylesheet>