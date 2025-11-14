<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	
	<!--
		In diesem XSLT wird das iDoc mit allen Segmenten kopiert. 
		Lediglich das E1AUSPM Segment wird um ein Feld Z_CLASS erweitert, welches
		die entsprechende Klasse des Merkmals darstellt. Dieses muss an proAlpha übergeben werden. 
	-->
	
	
	<xsl:template match="/">
	<CLFMAS02>
		<IDOC>
		<!--  Kopieren der Segmente EDI_DC40, E1KSSKM und E1DATEM, da an diesen nichts manipuliert wird -->
			<xsl:copy-of select ="//IDOC/EDI_DC40"/>
			<xsl:for-each select="//E1OCLFM[position()=1]">    
		        <E1OCLFM>
		            <MSGFN><xsl:value-of select="MSGFN"/></MSGFN>
	             	<OBTAB><xsl:value-of select="OBTAB"/></OBTAB>
			      	<OBJEK><xsl:value-of select="OBJEK"/></OBJEK>
			      	<KLART><xsl:value-of select="KLART"/></KLART>
			      	<MAFID><xsl:value-of select="MAFID"/></MAFID>
			      	<OBJECT_TABLE><xsl:value-of select="OBJECT_TABLE"/></OBJECT_TABLE>
			      	<OBJEK_LONG><xsl:value-of select="OBJEK_LONG"/></OBJEK_LONG>
			      			
			      	
			      	<xsl:for-each select="E1KSSKM">
			      		<xsl:copy-of select="."/>
			      	</xsl:for-each>
			      	
			      	<!--  Dynamischer Aufbau pro E1AUSPM Segment mit der Manipuluation -->
			      	<xsl:for-each select="E1AUSPM">
			      	<E1AUSPM>
		      		 	<MSGFN><xsl:value-of select="MSGFN"/></MSGFN>
				        <ATNAM><xsl:value-of select="ATNAM"/></ATNAM>
				        <DATUV><xsl:value-of select="DATUV"/></DATUV>
				        <ATWRT><xsl:value-of select="ATWRT"/></ATWRT>
				        <ATFLV><xsl:value-of select="ATFLV"/></ATFLV>
				        <ATFLB><xsl:value-of select="ATFLB"/></ATFLB>
				        <ATCOD><xsl:value-of select="ATCOD"/></ATCOD>
				        <ATTLV><xsl:value-of select="ATTLV"/></ATTLV>
				        <ATTLB><xsl:value-of select="ATTLB"/></ATTLB>
				        <ATINC><xsl:value-of select="ATINC"/></ATINC>
				        <ATIMB><xsl:value-of select="ATIMB"/></ATIMB>
				        <ATZIS><xsl:value-of select="ATZIS"/></ATZIS>
				        
				        
				        <!--  Speichern einer Variable für Zugriff auf Klasse -->
				        <xsl:variable name="Z_ATNAM">
							 <xsl:value-of select="ATNAM"/>
						</xsl:variable>
				        <!-- 
				        In dem Fall wo der Klassennamen im Merkmal enthalten ist, 
				        befüllen wir das zusätzliche Feld Z_CLASS mit der entsprechenden
				        Klasse aus dem E1SSKM Segment hinzu welches wir 
				        im Mapping nach proAlpha nutzen können -->
				        <ATWRT_LONG><xsl:value-of select="ATWRT_LONG"/></ATWRT_LONG>
				        <Z_CLASS>
				        <xsl:for-each select="../E1KSSKM[contains($Z_ATNAM, CLASS)]/CLASS">
					        <xsl:value-of select="."/>
					        
				        </xsl:for-each>
				        </Z_CLASS>
				        
		     		</E1AUSPM>
			      	</xsl:for-each>
			      	
			      	
			      	<xsl:for-each select="E1DATEM">
			      		<xsl:copy-of select="."/>
			      	</xsl:for-each>
			      	
		        </E1OCLFM>
		        
    		</xsl:for-each>
			
			
			
			
			
			
		</IDOC>
	</CLFMAS02>
	</xsl:template>



</xsl:stylesheet>
