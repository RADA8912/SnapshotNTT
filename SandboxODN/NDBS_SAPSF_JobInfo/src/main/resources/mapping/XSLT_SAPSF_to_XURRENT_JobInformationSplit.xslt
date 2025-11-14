<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:strip-space elements="*"/>
	<xsl:template match="/">
		<queryCompoundEmployeeResponse>
			<CompoundEmployee>
				<xsl:for-each select="//queryCompoundEmployeeResponse/CompoundEmployee/person/employment_information/job_information">
					<xsl:variable name="count">
						<xsl:value-of select="position()"/>
					</xsl:variable>
					<xsl:for-each select="../../../person">
						<person>
							<xsl:for-each select="*">
								<xsl:choose>
									<xsl:when test="local-name() = 'employment_information'">
										<employment_information>
											<xsl:for-each select="*[local-name() != 'job_information']">
												<xsl:element name="{local-name()}">
													<xsl:copy-of select="node()"/>
												</xsl:element>
											</xsl:for-each>
											<xsl:for-each select="*[local-name() = 'job_information']">
												<xsl:if test="$count = position()">
													<xsl:element name="{local-name()}">
														<xsl:copy-of select="node()"/>
													</xsl:element>
												</xsl:if>
											</xsl:for-each>
										</employment_information>
									</xsl:when>
									<xsl:otherwise>
										<xsl:element name="{local-name()}">
											<xsl:copy-of select="node()"/>
										</xsl:element>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:for-each>
						</person>
					</xsl:for-each>
				</xsl:for-each>
			</CompoundEmployee>
		</queryCompoundEmployeeResponse>
	</xsl:template>
</xsl:stylesheet>