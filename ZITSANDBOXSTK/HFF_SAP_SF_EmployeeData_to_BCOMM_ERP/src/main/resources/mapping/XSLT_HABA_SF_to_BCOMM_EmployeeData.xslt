<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:param name="Z_NUMBER_RANGE"/>
	<xsl:param name="Z_SENDER_SYSTEM"/>
	<xsl:template match="/">
		<HRCC1DNPERSO01>
			<IDOC BEGIN="1">
				<EDI_DC40 SEGMENT="1">
					<TABNAM>
						<xsl:value-of select="'EDI_DC40'"/>
					</TABNAM>
					<DOCNUM>
						<xsl:value-of select="$Z_NUMBER_RANGE"/>
					</DOCNUM>
					<DIRECT>
						<xsl:value-of select="'1'"/>
					</DIRECT>
					<IDOCTYP>
						<xsl:value-of select="'HRCC1DNPERSO01'"/>
					</IDOCTYP>
					<MESTYP>
						<xsl:value-of select="'HRCC1DNPERSO'"/>
					</MESTYP>
					<SNDPOR>
						<xsl:value-of select="'SAPHP1'"/>
					</SNDPOR>
					<SNDPRT>
						<xsl:value-of select="'LS'"/>
					</SNDPRT>
					<SNDPRN>
						<xsl:value-of select="$Z_SENDER_SYSTEM"/>
					</SNDPRN>
					<RCVPOR>
						<xsl:value-of select="'SAPCIIDOC'"/>
					</RCVPOR>
					<RCVPRT>
						<xsl:value-of select="'LS'"/>
					</RCVPRT>
					<RCVPRN>
						<xsl:value-of select="'BC_ERP_HR'"/>
					</RCVPRN>
				</EDI_DC40>
				<xsl:for-each select="//queryCompoundEmployeeResponse/CompoundEmployee/person">
					<xsl:for-each select="employment_information/job_information">
						<xsl:variable name="currentDate">
							<xsl:call-template name="DateConverter">
								<xsl:with-param name="value" select="fn:current-date()"/>
								<xsl:with-param name="input_format" select="'YYYY-MM-DD'"/>
								<xsl:with-param name="output_format" select="'YYYYMMDD'"/>
							</xsl:call-template>
						</xsl:variable>
						<xsl:variable name="startDate">
							<xsl:call-template name="DateConverter">
								<xsl:with-param name="value" select="start_date"/>
								<xsl:with-param name="input_format" select="'YYYY-MM-DD'"/>
								<xsl:with-param name="output_format" select="'YYYYMMDD'"/>
							</xsl:call-template>
						</xsl:variable>
						<xsl:variable name="endDate">
							<xsl:call-template name="DateConverter">
								<xsl:with-param name="value" select="end_date"/>
								<xsl:with-param name="input_format" select="'YYYY-MM-DD'"/>
								<xsl:with-param name="output_format" select="'YYYYMMDD'"/>
							</xsl:call-template>
						</xsl:variable>
						<xsl:if test="$currentDate &lt;= $endDate and $currentDate &gt;= $startDate">
							<xsl:if test="custom_string22 = '1'">
								<E1BPCC1DNPERSO SEGMENT="1">
									<SOURCE_SYS>
										<xsl:value-of select="$Z_SENDER_SYSTEM"/>
									</SOURCE_SYS>
									<TIMEID_NO>
										<xsl:variable name="TIMEID_NO_VALUE">
											<xsl:call-template name="addLeadingZeros">
												<xsl:with-param name="input">
													<xsl:value-of select="../../personal_information[end_date='9999-12-31']/custom_string5"/>
												</xsl:with-param>
												<xsl:with-param name="length">
													<xsl:value-of select="'8'"/>
												</xsl:with-param>
											</xsl:call-template>
										</xsl:variable>
										<xsl:value-of select="$TIMEID_NO_VALUE"/>
									</TIMEID_NO>
									<FROM_DATE>
										<xsl:call-template name="DateConverter">
											<xsl:with-param name="value" select="fn:current-date()"/>
											<xsl:with-param name="input_format" select="'YYYY-MM-DD'"/>
											<xsl:with-param name="output_format" select="'YYYYMMDD'"/>
										</xsl:call-template>
									</FROM_DATE>
									<TO_DATE>
										<xsl:choose>
											<xsl:when test="../end_date != ''">
												<xsl:call-template name="DateConverter">
													<xsl:with-param name="value" select="../end_date"/>
													<xsl:with-param name="input_format" select="'YYYY-MM-DD'"/>
													<xsl:with-param name="output_format" select="'YYYYMMDD'"/>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="'99991231'"/>
											</xsl:otherwise>
										</xsl:choose>
									</TO_DATE>
									<PERNO>
										<xsl:variable name="PERNO_VALUE">
											<xsl:call-template name="addLeadingZeros">
												<xsl:with-param name="input">
													<xsl:value-of select="../../person_id_external"/>
												</xsl:with-param>
												<xsl:with-param name="length">
													<xsl:value-of select="'8'"/>
												</xsl:with-param>
											</xsl:call-template>
										</xsl:variable>
										<xsl:value-of select="$PERNO_VALUE"/>
									</PERNO>
									<xsl:variable name="FirstName">
										<xsl:value-of select="../../personal_information[end_date='9999-12-31']/first_name"/>
									</xsl:variable>
									<xsl:variable name="LastName">
										<xsl:value-of select="../../personal_information[end_date='9999-12-31']/last_name"/>
									</xsl:variable>
									<EDIT_NAME>
										<xsl:value-of select="fn:concat($LastName,' ',$FirstName)"/>
									</EDIT_NAME>
									<SORT_NAME>
										<xsl:value-of select="fn:concat($LastName,' ',$FirstName)"/>
									</SORT_NAME>
									<LANGU>
										<xsl:value-of select="'D'"/>
									</LANGU>
									<LANGU_ISO>
										<xsl:value-of select="'DE'"/>
									</LANGU_ISO>
									<PS_GRPG_ATT_ABS_TYPE>
										<xsl:value-of select="''"/>
									</PS_GRPG_ATT_ABS_TYPE>
									<COUNTRY_GROUPING>
										<xsl:value-of select="'01'"/>
									</COUNTRY_GROUPING>
									<SUBSYSTEM_GROUPING>
										<xsl:value-of select="'001'"/>
									</SUBSYSTEM_GROUPING>
									<ES_GRPG_WORK_SCHED>
										<xsl:value-of select="''"/>
									</ES_GRPG_WORK_SCHED>
									<ACCESS_CONTROL_GROUP>
										<xsl:value-of select="''"/>
									</ACCESS_CONTROL_GROUP>
									<ATT_ABS_REASON_GRPG>
										<xsl:value-of select="''"/>
									</ATT_ABS_REASON_GRPG>
									<EXT_WAGETYPE_GRPG>
										<xsl:value-of select="''"/>
									</EXT_WAGETYPE_GRPG>
									<TIME_EVENT_TYPE_GROUP>
										<xsl:value-of select="'01'"/>
									</TIME_EVENT_TYPE_GROUP>
									<COMP_CODE>
										<xsl:value-of select="''"/>
									</COMP_CODE>
									<COSTCENTER>
										<xsl:variable name="COSTCENTER_VALUE">
											<xsl:value-of select="cost_center"/>
										</xsl:variable>
										<xsl:variable name="COSTCENTER_START_POSITION">
											<xsl:value-of select="fn:string-length($COSTCENTER_VALUE) - 7"/>
										</xsl:variable>
										<xsl:value-of select="fn:substring($COSTCENTER_VALUE, $COSTCENTER_START_POSITION, 8)"/>
									</COSTCENTER>
								</E1BPCC1DNPERSO>
							</xsl:if>
						</xsl:if>
					</xsl:for-each>
				</xsl:for-each>
			</IDOC>
		</HRCC1DNPERSO01>
	</xsl:template>
	<!-- Method to handle the input format -->
	<xsl:template name="DateConverter">
		<xsl:param name="value"/>
		<xsl:param name="input_format"/>
		<xsl:param name="output_format"/>
		<xsl:variable name="dateValue" select="fn:concat($value,'00000000000000000000')"/>
		<xsl:choose>
			<xsl:when test="$input_format='YYYY-MM-DD'">
				<xsl:variable name="year" select="fn:substring($dateValue,1,4)"/>
				<xsl:variable name="month" select="fn:substring($dateValue,6,2)"/>
				<xsl:variable name="day" select="fn:substring($dateValue,9,2)"/>
				<xsl:call-template name="DateConverter_Output">
					<xsl:with-param name="year" select="$year"/>
					<xsl:with-param name="month" select="$month"/>
					<xsl:with-param name="day" select="$day"/>
					<xsl:with-param name="output_format" select="$output_format"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$value"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Method to generate the output format -->
	<xsl:template name="DateConverter_Output">
		<xsl:param name="year"/>
		<xsl:param name="month"/>
		<xsl:param name="day"/>
		<xsl:param name="output_format"/>
		<xsl:choose>
			<xsl:when test="$output_format='YYYYMMDD'">
				<xsl:value-of select="fn:concat($year,$month,$day)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="fn:concat($year,'-',$month,'-',$day)"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Method for adding leading zeros -->
	<xsl:template name="addLeadingZeros">
		<xsl:param name="input"/>
		<xsl:param name="length"/>
		<xsl:choose>
			<xsl:when test="fn:string-length($input) &lt; $length">
				<xsl:call-template name="addLeadingZeros">
					<xsl:with-param name="input">
						<xsl:value-of select="fn:concat('0', $input)"/>
					</xsl:with-param>
					<xsl:with-param name="length">
						<xsl:value-of select="$length"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$input"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>