<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:edoc="http://www.sap.com/eDocument/Italy/ReceiveNotification/v1.0"
	xmlns:typ="http://www.fatturapa.gov.it/sdi/ws/trasmissione/v1.1/types">

	<xsl:template match="@*|node()"></xsl:template>

	<xsl:template match="/">
		<xsl:apply-templates select="*">
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template match="messages">
		<edoc:PullNotificationResponse>
			<xsl:apply-templates select="message">
			</xsl:apply-templates>
		</edoc:PullNotificationResponse>
	</xsl:template>

	<xsl:template match="message">
		<xsl:apply-templates select="*">
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template match="notification">
		<NotificationEnvelope>
			<xsl:copy-of select="*"></xsl:copy-of>
		</NotificationEnvelope>
	</xsl:template>

</xsl:stylesheet>

<!-- 
INPUT
<messages>
	<message id="IT01234567890_11111_NS_001">
		<typ:notificaScarto112
			xmlns:typ="http://www.fatturapa.gov.it/sdi/ws/trasmissione/v1.0/types">
			<IdentificativoSdI>123456</IdentificativoSdI>
			<NomeFile>IT01234567890_11111_NS_001</NomeFile>
			<File>asdadadas112</File>
		</typ:notificaScarto112>
	</message>
	<message id="IT01234567890_11111_NS_002">
		<typ:notificaScarto
			xmlns:typ="http://www.fatturapa.gov.it/sdi/ws/trasmissione/v1.0/types">
			<IdentificativoSdI>123456</IdentificativoSdI>
			<NomeFile>IT01234567890_11111_NS_002</NomeFile>
			<File>asdadadas</File>
		</typ:notificaScarto>
	</message>
</messages>

OUTPUT
<edoc:PullNotificationResponse
	xmlns:edoc="http://www.sap.com/eDocument/Italy/PullNotification/v1.0"
	xmlns:typ="http://www.fatturapa.gov.it/sdi/ws/trasmissione/v1.0/types">
	<edoc:NotificationEnvelope>
		<IdentificativoSdI>123456</IdentificativoSdI>
		<NomeFile>IT01234567890_11111_NS_001</NomeFile>
		<File>asdadadas112</File>
	</edoc:NotificationEnvelope>
	<edoc:NotificationEnvelope>
		<IdentificativoSdI>123456</IdentificativoSdI>
		<NomeFile>IT01234567890_11111_NS_002</NomeFile>
		<File>asdadadas</File>
	</edoc:NotificationEnvelope>
</edoc:PullNotificationResponse>
-->
