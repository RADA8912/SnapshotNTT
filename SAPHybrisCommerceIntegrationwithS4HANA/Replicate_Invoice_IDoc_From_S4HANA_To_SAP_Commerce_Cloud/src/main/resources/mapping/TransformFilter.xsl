<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
	<xsl:output method="xml" indent="yes" />
	<xsl:strip-space elements="*" />
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
	<xsl:template match="INVOIC02/IDOC">
		<xsl:variable name="documentNumber" select="E1EDK01/BELNR" />
		<xsl:variable name="methodName" select="'POST'"/>
		<xsl:variable name="amount" select="E1EDS01[SUMID='010']/SUMME" />
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
			<xsl:for-each select="E1EDS01">
				<xsl:choose>
					<xsl:when test="SUMID = '010'">
						<amount>
							<xsl:apply-templates select="@*|node()" />
						</amount>
					</xsl:when>
					<xsl:when test="SUMID = '005'">
						<overAllTax>
							<xsl:apply-templates select="@*|node()" />
						</overAllTax>
					</xsl:when>
					<xsl:when test="SUMID = '011'">
						<grandTotal>
							<xsl:apply-templates select="@*|node()" />
						</grandTotal>
					</xsl:when>
					<xsl:otherwise></xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<xsl:for-each select="E1EDKA1">
				<xsl:choose>
					<xsl:when test="PARVW = 'AG'">
						<b2bunit>
							<xsl:apply-templates select="@*|node()" />
						</b2bunit>
					</xsl:when>
					<xsl:otherwise></xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<xsl:for-each select="E1EDK14">
				<xsl:choose>
					<xsl:when test="QUALF = '008'">
						<salesOrg>
							<xsl:apply-templates select="@*|node()" />
						</salesOrg>
					</xsl:when>
					<xsl:when test="QUALF = '007'">
						<distChannel>
							<xsl:apply-templates select="@*|node()" />
						</distChannel>
					</xsl:when>
					<xsl:when test="QUALF = '006'">
						<division>
							<xsl:apply-templates select="@*|node()" />
						</division>
					</xsl:when>
					<xsl:otherwise></xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<xsl:for-each select="E1EDK02">
				<xsl:choose>
					<xsl:when test="QUALF = '009'">
						<documentDate>
							<xsl:apply-templates select="@*|node()" />
						</documentDate>
					</xsl:when>
					<xsl:when test="QUALF = '002'">
						<orderNumber>
							<xsl:apply-templates select="@*|node()" />
						</orderNumber>
					</xsl:when>
					<xsl:when test="QUALF = '012'">
						<deliveryNumber>
							<xsl:apply-templates select="@*|node()" />
						</deliveryNumber>
					</xsl:when>
					<xsl:otherwise></xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<xsl:for-each select="E1EDK18">
				<xsl:choose>
					<xsl:when test="QUALF = '001'">
						<termsOfPayment>
							<xsl:apply-templates select="@*|node()" />
						</termsOfPayment>
					</xsl:when>
					<xsl:otherwise></xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<xsl:for-each select="E1EDK17">
				<xsl:choose>
					<xsl:when test="QUALF = '002'">
						<termsOfDelivery1>
							<xsl:apply-templates select="@*|node()" />
						</termsOfDelivery1>
					</xsl:when>
					<xsl:otherwise></xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<MaterialData>
				<xsl:for-each select="E1EDP01">
					<batchChangeSetPart>
						<method><xsl:value-of select="$methodName" /></method>
						<Materials>
							<Material>
								<integrationKey></integrationKey>
								<matPosNo>
									<xsl:value-of select="POSEX" />
								</matPosNo>
								<quantity>
									<xsl:value-of select="MENGE" />
								</quantity>
								<unit>
									<xsl:value-of select="MENEE" />
								</unit>
								<xsl:if test="E1EDP19[QUALF='002']/IDTNR">
									<matNo>
										<xsl:value-of select="E1EDP19[QUALF='002']/IDTNR" />
									</matNo>
								</xsl:if>
								<xsl:if test="E1EDP19[QUALF='002']/KTEXT">
									<matDesc>
										<xsl:value-of select="E1EDP19[QUALF='002']/KTEXT" />
									</matDesc>
								</xsl:if>
								<xsl:if test="E1EDP26[QUALF='001']/BETRG">
									<grossPrice>
										<xsl:value-of select="E1EDP26[QUALF='001']/BETRG" />
									</grossPrice>
								</xsl:if>
								<xsl:if test="E1EDP26[QUALF='002']/BETRG">
									<grossValue>
										<xsl:value-of select="E1EDP26[QUALF='002']/BETRG" />
									</grossValue>
								</xsl:if>
								<xsl:if test="E1EDP26[QUALF='003']/BETRG">
									<netValue>
										<xsl:value-of select="E1EDP26[QUALF='003']/BETRG" />
									</netValue>
								</xsl:if>
								<xsl:if test="E1EDP26[QUALF='004']/BETRG">
									<qualifyCashDisc>
										<xsl:value-of select="E1EDP26[QUALF='004']/BETRG" />
									</qualifyCashDisc>
								</xsl:if>
								<xsl:if test="E1EDP26[QUALF='005']/BETRG">
									<abstNetValue>
										<xsl:value-of select="E1EDP26[QUALF='005']/BETRG" />
									</abstNetValue>
								</xsl:if>
								<xsl:if test="E1EDP26[QUALF='006']/BETRG">
									<netPrice>
										<xsl:value-of select="E1EDP26[QUALF='006']/BETRG" />
									</netPrice>
								</xsl:if>
								<xsl:if test="E1EDP26[QUALF='007']/BETRG">
									<cashDisc>
										<xsl:value-of select="E1EDP26[QUALF='007']/BETRG" />
									</cashDisc>
								</xsl:if>
								<xsl:if test="E1EDP26[QUALF='009']/BETRG">
									<statValue>
										<xsl:value-of select="E1EDP26[QUALF='009']/BETRG" />
									</statValue>
								</xsl:if>
								<xsl:if test="E1EDP26[QUALF='010']/BETRG">
									<subTotal1>
										<xsl:value-of select="E1EDP26[QUALF='010']/BETRG" />
									</subTotal1>
								</xsl:if>
								<xsl:if test="E1EDP26[QUALF='011']/BETRG">
									<subTotal2>
										<xsl:value-of select="E1EDP26[QUALF='011']/BETRG" />
									</subTotal2>
								</xsl:if>
								<xsl:if test="E1EDP26[QUALF='012']/BETRG">
									<subTotal3>
										<xsl:value-of select="E1EDP26[QUALF='012']/BETRG" />
									</subTotal3>
								</xsl:if>
								<xsl:if test="E1EDP26[QUALF='013']/BETRG">
									<subTotal4>
										<xsl:value-of select="E1EDP26[QUALF='013']/BETRG" />
									</subTotal4>
								</xsl:if>
								<xsl:if test="E1EDP26[QUALF='014']/BETRG">
									<subTotal5>
										<xsl:value-of select="E1EDP26[QUALF='014']/BETRG" />
									</subTotal5>
								</xsl:if>
								<xsl:if test="E1EDP26[QUALF='015']/BETRG">
									<subTotal6>
										<xsl:value-of select="E1EDP26[QUALF='015']/BETRG" />
									</subTotal6>
								</xsl:if>
								<sapB2BDocument>
									<SapB2BDocument>
										<integrationKey></integrationKey>
										<documentNumber>
											<xsl:value-of select="$documentNumber" />
										</documentNumber>
										<documentType>
											<B2BDocumentType>
												<code>Invoice</code>
												<integrationKey></integrationKey>
											</B2BDocumentType>
										</documentType>
									</SapB2BDocument>
								</sapB2BDocument>
							</Material>
						</Materials>
					</batchChangeSetPart>
				</xsl:for-each>
			</MaterialData>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
