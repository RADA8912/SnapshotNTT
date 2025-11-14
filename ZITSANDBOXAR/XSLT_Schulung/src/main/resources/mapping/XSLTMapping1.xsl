<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:param name="SNDPOR"/>
	<xsl:param name="SNDPRN"/>
	<xsl:param name="RCVPRN"/>
	<xsl:template match="/">
	    <EDI_DC40>
	        <TABAM>
	            <xsl:value-of select="ACC_DOCUMENT05/IDOC/EDI_DC40/TABNAM"/>   
	        </TABAM>
	        <DIRECT>
	            <xsl:value-of select="ACC_DOCUMENT05/IDOC/EDI_DC40/DIRECT"/>  
	        </DIRECT>
	        <IDOCTYP>
	            <xsl:value-of select="ACC_DOCUMENT05/IDOC/EDI_DC40/IDOCTYP"/>  
	        </IDOCTYP>
	        <MESTYP>
	            <xsl:value-of select="ACC_DOCUMENT05/IDOC/EDI_DC40/MESTYP"/>  
	        </MESTYP>
	        <SNDPOR>
	            <xsl:value-of select="$SNDPOR"/>
	        </SNDPOR>
	        <SNDPRT>
	            <xsl:value-of select="ACC_DOCUMENT05/IDOC/EDI_DC40/SNDPRT"/>  
	        </SNDPRT>
	        <SNDPRN>
	            <xsl:value-of select="$SNDPRN"/>
	        </SNDPRN>
	        <RCVPOR>
	            <xsl:value-of select="ACC_DOCUMENT05/IDOC/EDI_DC40/RCVPOR"/>  
	        </RCVPOR>
	        <RCVPRT>
	            <xsl:value-of select="ACC_DOCUMENT05/IDOC/EDI_DC40/RCVPRT"/>  
	        </RCVPRT>
	        <RCVPRN>
	           <xsl:value-of select="$RCVPRN"/> 
	        </RCVPRN>
	    </EDI_DC40>
	    
	    <xsl:for-each select="ACC_DOCUMENT05/IDOC/E1BPACCR09">
    	    <E1BPACCR09>
    	        <xsl:value-of select="*"/>
    	    </E1BPACCR09>
	    </xsl:for-each>
	    
	    
	</xsl:template>
</xsl:stylesheet>
