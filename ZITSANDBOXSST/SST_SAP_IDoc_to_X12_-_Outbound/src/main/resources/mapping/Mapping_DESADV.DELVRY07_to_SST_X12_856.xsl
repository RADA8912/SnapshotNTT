<?xml version="1.0" encoding="UTF-8"?><!-- Copyright statement for Type System ASC X12: Copyright (c) 2017, Accredited Standards Committee X12 Incorporated, Format (c) 2017 Washington Publishing Company. Exclusively published by the Washington Publishing Company. No part of this publication maybe distributed, posted, reproduced, stored in a retrieval system, or transmitted in any form or by any means without the prior written permission of the copyright owner. See also: http://members.x12.org/policies-procedures/adp06-intellectual-property-rights-policy-statement.pdf --><!-- Copyright statement for Type System UN/EDIFACT: Copyright (c) United Nations 2000-2008. All rights reserved. None of the materials provided on this web site may be used, reproduced or transmitted, in whole or in part, in any form or by any means, electronic or mechanical, including photocopying, recording or the use of any information storage and retrieval system, except as provided for in the Terms and Conditions of Use of United Nations Web Sites, without permission in writing from the publisher. To request such permission and for further enquiries, contact the Secretary of the Publications Board, United Nations, New York, NY, 10017, USA (pubboard@un.org; Telephone: (+1) 212-963-4664; Facsimile: (+1) 212-963-0077). See also: http://www.unece.org/legal_notice/copyrightnotice.html --><!-- Copyright statement for Type System SAP IDoc: Copyright © SAP SE 2017. All Rights Reserved. See also: https://www.sap.com/corporate/en/legal/copyright/use-of-copyrighted-material.html --><!-- Copyright statement for ISO Codelists: Copyright (c) 2017, ISO All ISO content is copyright protected. The copyright is owned by ISO. Any use of the content, including copying of it in whole or in part, for example to another Internet site, is prohibited and would require written permission from ISO. All ISO publications are also protected by copyright. The copyright ownership of ISO is clearly indicated on every ISO publication. Any unauthorized use such as copying, scanning or distribution is prohibited. Requests for permission should be addressed to the ISO Central Secretariat or directly through the ISO member in your country. See more: https://www.iso.org/privacy-and-copyright.html --><!-- Copyright statement for UN/CEFACT Codelists: Copyright (c) United Nations 2000-2008. All rights reserved. None of the materials provided on this web site may be used, reproduced or transmitted, in whole or in part, in any form or by any means, electronic or mechanical, including photocopying, recording or the use of any information storage and retrieval system, except as provided for in the Terms and Conditions of Use of United Nations Web Sites, without permission in writing from the publisher. To request such permission and for further enquiries, contact the Secretary of the Publications Board, United Nations, New York, NY, 10017, USA (pubboard@un.org; Telephone: (+1) 212-963-4664; Facsimile: (+1) 212-963-0077). See also: http://www.unece.org/legal_notice/copyrightnotice.html --><xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:a="urn:sap.com:ica:mag" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:global="urn:sap.com/ica/mag/internal-use/global" xmlns:ica="http://www.sap.com/ica/mag/" xmlns:ica_debug="http://www.sap.com/ns/saat/debug" xmlns:ica_env="urn:sap:ica:env" xmlns:ica_env_options="urn:sap:ica:env:options" xmlns:ica_fn="http://www.sap.com/ica/mag/function" xmlns:ica_gen="urn:sap:ica:gen" xmlns:sap="http://www.ttools.org/sap/ns/structure" xmlns:sapaqua="http://www.sap.com/ns/saat/aqua" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:z="http://www.xsdplus.org/ns/structure" exclude-result-prefixes="#all" version="2.0"><xsl:output encoding="UTF-8" method="xml"/><xsl:template match="/"><xsl:variable name="c" select="/*"/><xsl:variable as="xs:integer*" name="ica_gen:iteration" select="()"/><M_856><xsl:variable name="c" select="$c/(/DELVRY07)"/><S_ST><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f1($c/(.))"/><D_143><xsl:value-of select="$src"/></D_143></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f2($c/(.))"/><D_329><xsl:value-of select="$src"/></D_329></xsl:if></S_ST><S_BSN><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f3($c/(.))"/><D_353><xsl:value-of select="$src"/></D_353></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_396><xsl:value-of select="$src"/></D_396></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_373><xsl:value-of select="$src"/></D_373></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_337><xsl:value-of select="$src"/></D_337></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1005><xsl:value-of select="$src"/></D_1005></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_640><xsl:value-of select="$src"/></D_640></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_641><xsl:value-of select="$src"/></D_641></xsl:if></xsl:if></S_BSN><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_DTM><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f4($c/(.))"/><D_374><xsl:value-of select="$src"/></D_374></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f5($c/(.))"/><D_373><xsl:value-of select="$src"/></D_373></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_337><xsl:value-of select="$src"/></D_337></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_623><xsl:value-of select="$src"/></D_623></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1250><xsl:value-of select="$src"/></D_1250></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1251><xsl:value-of select="$src"/></D_1251></xsl:if></xsl:if></S_DTM></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 10]"/></xsl:if><xsl:variable name="src"><xsl:for-each select="$c/(EDI_DC40)"><xsl:variable name="c" select="."/><G_HL><S_HL><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f6($c/(.))"/><D_628><xsl:value-of select="$src"/></D_628></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_734><xsl:value-of select="$src"/></D_734></xsl:if></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_735><xsl:value-of select="$src"/></D_735></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_736><xsl:value-of select="$src"/></D_736></xsl:if></xsl:if></S_HL><xsl:if test="$c/()"><S_LIN><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_350><xsl:value-of select="$src"/></D_350></xsl:if></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235><xsl:value-of select="$src"/></D_235></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234><xsl:value-of select="$src"/></D_234></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_2><xsl:value-of select="$src"/></D_235_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_2><xsl:value-of select="$src"/></D_234_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_3><xsl:value-of select="$src"/></D_235_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_3><xsl:value-of select="$src"/></D_234_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_4><xsl:value-of select="$src"/></D_235_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_4><xsl:value-of select="$src"/></D_234_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_5><xsl:value-of select="$src"/></D_235_5></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_5><xsl:value-of select="$src"/></D_234_5></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_6><xsl:value-of select="$src"/></D_235_6></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_6><xsl:value-of select="$src"/></D_234_6></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_7><xsl:value-of select="$src"/></D_235_7></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_7><xsl:value-of select="$src"/></D_234_7></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_8><xsl:value-of select="$src"/></D_235_8></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_8><xsl:value-of select="$src"/></D_234_8></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_9><xsl:value-of select="$src"/></D_235_9></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_9><xsl:value-of select="$src"/></D_234_9></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_10><xsl:value-of select="$src"/></D_235_10></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_10><xsl:value-of select="$src"/></D_234_10></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_11><xsl:value-of select="$src"/></D_235_11></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_11><xsl:value-of select="$src"/></D_234_11></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_12><xsl:value-of select="$src"/></D_235_12></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_12><xsl:value-of select="$src"/></D_234_12></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_13><xsl:value-of select="$src"/></D_235_13></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_13><xsl:value-of select="$src"/></D_234_13></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_14><xsl:value-of select="$src"/></D_235_14></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_14><xsl:value-of select="$src"/></D_234_14></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_15><xsl:value-of select="$src"/></D_235_15></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_15><xsl:value-of select="$src"/></D_234_15></xsl:if></xsl:if></S_LIN></xsl:if><xsl:if test="$c/()"><S_SN1><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_350><xsl:value-of select="$src"/></D_350></xsl:if></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_382><xsl:value-of select="$src"/></D_382></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355><xsl:value-of select="$src"/></D_355></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_646><xsl:value-of select="$src"/></D_646></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_330><xsl:value-of select="$src"/></D_330></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_2><xsl:value-of select="$src"/></D_355_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_728><xsl:value-of select="$src"/></D_728></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_668><xsl:value-of select="$src"/></D_668></xsl:if></xsl:if></S_SN1></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_SLN><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_350><xsl:value-of select="$src"/></D_350></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_350_2><xsl:value-of select="$src"/></D_350_2></xsl:if></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_662><xsl:value-of select="$src"/></D_662></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_380><xsl:value-of select="$src"/></D_380></xsl:if></xsl:if><xsl:if test="$c/()"><C_C001><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355><xsl:value-of select="$src"/></D_355></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1018><xsl:value-of select="$src"/></D_1018></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_649><xsl:value-of select="$src"/></D_649></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_2><xsl:value-of select="$src"/></D_355_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1018_2><xsl:value-of select="$src"/></D_1018_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_649_2><xsl:value-of select="$src"/></D_649_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_3><xsl:value-of select="$src"/></D_355_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1018_3><xsl:value-of select="$src"/></D_1018_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_649_3><xsl:value-of select="$src"/></D_649_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_4><xsl:value-of select="$src"/></D_355_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1018_4><xsl:value-of select="$src"/></D_1018_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_649_4><xsl:value-of select="$src"/></D_649_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_5><xsl:value-of select="$src"/></D_355_5></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1018_5><xsl:value-of select="$src"/></D_1018_5></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_649_5><xsl:value-of select="$src"/></D_649_5></xsl:if></xsl:if></C_C001></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_212><xsl:value-of select="$src"/></D_212></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_639><xsl:value-of select="$src"/></D_639></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_662_2><xsl:value-of select="$src"/></D_662_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235><xsl:value-of select="$src"/></D_235></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234><xsl:value-of select="$src"/></D_234></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_2><xsl:value-of select="$src"/></D_235_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_2><xsl:value-of select="$src"/></D_234_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_3><xsl:value-of select="$src"/></D_235_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_3><xsl:value-of select="$src"/></D_234_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_4><xsl:value-of select="$src"/></D_235_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_4><xsl:value-of select="$src"/></D_234_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_5><xsl:value-of select="$src"/></D_235_5></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_5><xsl:value-of select="$src"/></D_234_5></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_6><xsl:value-of select="$src"/></D_235_6></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_6><xsl:value-of select="$src"/></D_234_6></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_7><xsl:value-of select="$src"/></D_235_7></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_7><xsl:value-of select="$src"/></D_234_7></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_8><xsl:value-of select="$src"/></D_235_8></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_8><xsl:value-of select="$src"/></D_234_8></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_9><xsl:value-of select="$src"/></D_235_9></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_9><xsl:value-of select="$src"/></D_234_9></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_235_10><xsl:value-of select="$src"/></D_235_10></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_234_10><xsl:value-of select="$src"/></D_234_10></xsl:if></xsl:if></S_SLN></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 1000]"/></xsl:if><xsl:if test="$c/()"><S_PRF><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_324><xsl:value-of select="$src"/></D_324></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_328><xsl:value-of select="$src"/></D_328></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_327><xsl:value-of select="$src"/></D_327></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_373><xsl:value-of select="$src"/></D_373></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_350><xsl:value-of select="$src"/></D_350></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_367><xsl:value-of select="$src"/></D_367></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_92><xsl:value-of select="$src"/></D_92></xsl:if></xsl:if></S_PRF></xsl:if><xsl:if test="$c/()"><S_PO4><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_356><xsl:value-of select="$src"/></D_356></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_357><xsl:value-of select="$src"/></D_357></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355><xsl:value-of select="$src"/></D_355></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_103><xsl:value-of select="$src"/></D_103></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_187><xsl:value-of select="$src"/></D_187></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_384><xsl:value-of select="$src"/></D_384></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_2><xsl:value-of select="$src"/></D_355_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_385><xsl:value-of select="$src"/></D_385></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_3><xsl:value-of select="$src"/></D_355_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_82><xsl:value-of select="$src"/></D_82></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_189><xsl:value-of select="$src"/></D_189></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_65><xsl:value-of select="$src"/></D_65></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_4><xsl:value-of select="$src"/></D_355_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_810><xsl:value-of select="$src"/></D_810></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_752><xsl:value-of select="$src"/></D_752></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_350><xsl:value-of select="$src"/></D_350></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_350_2><xsl:value-of select="$src"/></D_350_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1470><xsl:value-of select="$src"/></D_1470></xsl:if></xsl:if></S_PO4></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_PID><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_349><xsl:value-of select="$src"/></D_349></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_750><xsl:value-of select="$src"/></D_750></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_559><xsl:value-of select="$src"/></D_559></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_751><xsl:value-of select="$src"/></D_751></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_352><xsl:value-of select="$src"/></D_352></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_752><xsl:value-of select="$src"/></D_752></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_822><xsl:value-of select="$src"/></D_822></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1073><xsl:value-of select="$src"/></D_1073></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_819><xsl:value-of select="$src"/></D_819></xsl:if></xsl:if></S_PID></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 200]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_MEA><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_737><xsl:value-of select="$src"/></D_737></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_738><xsl:value-of select="$src"/></D_738></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_739><xsl:value-of select="$src"/></D_739></xsl:if></xsl:if><xsl:if test="$c/()"><C_C001><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355><xsl:value-of select="$src"/></D_355></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1018><xsl:value-of select="$src"/></D_1018></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_649><xsl:value-of select="$src"/></D_649></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_2><xsl:value-of select="$src"/></D_355_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1018_2><xsl:value-of select="$src"/></D_1018_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_649_2><xsl:value-of select="$src"/></D_649_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_3><xsl:value-of select="$src"/></D_355_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1018_3><xsl:value-of select="$src"/></D_1018_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_649_3><xsl:value-of select="$src"/></D_649_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_4><xsl:value-of select="$src"/></D_355_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1018_4><xsl:value-of select="$src"/></D_1018_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_649_4><xsl:value-of select="$src"/></D_649_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_5><xsl:value-of select="$src"/></D_355_5></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1018_5><xsl:value-of select="$src"/></D_1018_5></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_649_5><xsl:value-of select="$src"/></D_649_5></xsl:if></xsl:if></C_C001></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_740><xsl:value-of select="$src"/></D_740></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_741><xsl:value-of select="$src"/></D_741></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_935><xsl:value-of select="$src"/></D_935></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_936><xsl:value-of select="$src"/></D_936></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_752><xsl:value-of select="$src"/></D_752></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1373><xsl:value-of select="$src"/></D_1373></xsl:if></xsl:if></S_MEA></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 40]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_PWK><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_755><xsl:value-of select="$src"/></D_755></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_756><xsl:value-of select="$src"/></D_756></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_757><xsl:value-of select="$src"/></D_757></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_98><xsl:value-of select="$src"/></D_98></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_66><xsl:value-of select="$src"/></D_66></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_67><xsl:value-of select="$src"/></D_67></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_352><xsl:value-of select="$src"/></D_352></xsl:if></xsl:if><xsl:if test="$c/()"><C_C002><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_704><xsl:value-of select="$src"/></D_704></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_704_2><xsl:value-of select="$src"/></D_704_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_704_3><xsl:value-of select="$src"/></D_704_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_704_4><xsl:value-of select="$src"/></D_704_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_704_5><xsl:value-of select="$src"/></D_704_5></xsl:if></xsl:if></C_C002></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1525><xsl:value-of select="$src"/></D_1525></xsl:if></xsl:if></S_PWK></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 25]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_PKG><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_349><xsl:value-of select="$src"/></D_349></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_753><xsl:value-of select="$src"/></D_753></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_559><xsl:value-of select="$src"/></D_559></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_754><xsl:value-of select="$src"/></D_754></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_352><xsl:value-of select="$src"/></D_352></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_400><xsl:value-of select="$src"/></D_400></xsl:if></xsl:if></S_PKG></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 25]"/></xsl:if><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_TD1><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_103><xsl:value-of select="$src"/></D_103></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_80><xsl:value-of select="$src"/></D_80></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_23><xsl:value-of select="$src"/></D_23></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_22><xsl:value-of select="$src"/></D_22></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_79><xsl:value-of select="$src"/></D_79></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_187><xsl:value-of select="$src"/></D_187></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_81><xsl:value-of select="$src"/></D_81></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355><xsl:value-of select="$src"/></D_355></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_183><xsl:value-of select="$src"/></D_183></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_2><xsl:value-of select="$src"/></D_355_2></xsl:if></xsl:if></S_TD1></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 20]"/><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_TD5><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f7($c/(.))"/><D_133><xsl:value-of select="$src"/></D_133></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_66><xsl:value-of select="$src"/></D_66></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_67><xsl:value-of select="$src"/></D_67></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_91><xsl:value-of select="$src"/></D_91></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_387><xsl:value-of select="$src"/></D_387></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_368><xsl:value-of select="$src"/></D_368></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_309><xsl:value-of select="$src"/></D_309></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_310><xsl:value-of select="$src"/></D_310></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_731><xsl:value-of select="$src"/></D_731></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_732><xsl:value-of select="$src"/></D_732></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_733><xsl:value-of select="$src"/></D_733></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_284><xsl:value-of select="$src"/></D_284></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_284_2><xsl:value-of select="$src"/></D_284_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_284_3><xsl:value-of select="$src"/></D_284_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_26><xsl:value-of select="$src"/></D_26></xsl:if></xsl:if></S_TD5></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 12]"/><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_TD3><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_40><xsl:value-of select="$src"/></D_40></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_206><xsl:value-of select="$src"/></D_206></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_207><xsl:value-of select="$src"/></D_207></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_187><xsl:value-of select="$src"/></D_187></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_81><xsl:value-of select="$src"/></D_81></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355><xsl:value-of select="$src"/></D_355></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_102><xsl:value-of select="$src"/></D_102></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_407><xsl:value-of select="$src"/></D_407></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_225><xsl:value-of select="$src"/></D_225></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_24><xsl:value-of select="$src"/></D_24></xsl:if></xsl:if></S_TD3></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 12]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_TD4><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_152><xsl:value-of select="$src"/></D_152></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_208><xsl:value-of select="$src"/></D_208></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_209><xsl:value-of select="$src"/></D_209></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_352><xsl:value-of select="$src"/></D_352></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1073><xsl:value-of select="$src"/></D_1073></xsl:if></xsl:if></S_TD4></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 5]"/></xsl:if><xsl:if test="$c/()"><S_TSD><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_350><xsl:value-of select="$src"/></D_350></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_219><xsl:value-of select="$src"/></D_219></xsl:if></xsl:if></S_TSD></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_REF><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128><xsl:value-of select="$src"/></D_128></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127><xsl:value-of select="$src"/></D_127></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_352><xsl:value-of select="$src"/></D_352></xsl:if></xsl:if><xsl:if test="$c/()"><C_C040><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128><xsl:value-of select="$src"/></D_128></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127><xsl:value-of select="$src"/></D_127></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128_2><xsl:value-of select="$src"/></D_128_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127_2><xsl:value-of select="$src"/></D_127_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128_3><xsl:value-of select="$src"/></D_128_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127_3><xsl:value-of select="$src"/></D_127_3></xsl:if></xsl:if></C_C040></xsl:if></S_REF></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_PER><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_366><xsl:value-of select="$src"/></D_366></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_93><xsl:value-of select="$src"/></D_93></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_365><xsl:value-of select="$src"/></D_365></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_364><xsl:value-of select="$src"/></D_364></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_365_2><xsl:value-of select="$src"/></D_365_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_364_2><xsl:value-of select="$src"/></D_364_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_365_3><xsl:value-of select="$src"/></D_365_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_364_3><xsl:value-of select="$src"/></D_364_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_443><xsl:value-of select="$src"/></D_443></xsl:if></xsl:if></S_PER></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 3]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><G_LH1><S_LH1><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355><xsl:value-of select="$src"/></D_355></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_80><xsl:value-of select="$src"/></D_80></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_277><xsl:value-of select="$src"/></D_277></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_200><xsl:value-of select="$src"/></D_200></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_22><xsl:value-of select="$src"/></D_22></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_2><xsl:value-of select="$src"/></D_355_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_380><xsl:value-of select="$src"/></D_380></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_595><xsl:value-of select="$src"/></D_595></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_665><xsl:value-of select="$src"/></D_665></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_254><xsl:value-of select="$src"/></D_254></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1375><xsl:value-of select="$src"/></D_1375></xsl:if></xsl:if></S_LH1><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_LH2><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_215><xsl:value-of select="$src"/></D_215></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_983><xsl:value-of select="$src"/></D_983></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_218><xsl:value-of select="$src"/></D_218></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_222><xsl:value-of select="$src"/></D_222></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_759><xsl:value-of select="$src"/></D_759></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355><xsl:value-of select="$src"/></D_355></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_408><xsl:value-of select="$src"/></D_408></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_2><xsl:value-of select="$src"/></D_355_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_408_2><xsl:value-of select="$src"/></D_408_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_3><xsl:value-of select="$src"/></D_355_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_408_3><xsl:value-of select="$src"/></D_408_3></xsl:if></xsl:if></S_LH2></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 4]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_LH3><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_224><xsl:value-of select="$src"/></D_224></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_984><xsl:value-of select="$src"/></D_984></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_985><xsl:value-of select="$src"/></D_985></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1073><xsl:value-of select="$src"/></D_1073></xsl:if></xsl:if></S_LH3></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 12]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_LFH><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_808><xsl:value-of select="$src"/></D_808></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_809><xsl:value-of select="$src"/></D_809></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_809_2><xsl:value-of select="$src"/></D_809_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1023><xsl:value-of select="$src"/></D_1023></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355><xsl:value-of select="$src"/></D_355></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_380><xsl:value-of select="$src"/></D_380></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_380_2><xsl:value-of select="$src"/></D_380_2></xsl:if></xsl:if></S_LFH></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 20]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_LEP><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_806><xsl:value-of select="$src"/></D_806></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_807><xsl:value-of select="$src"/></D_807></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_156><xsl:value-of select="$src"/></D_156></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127><xsl:value-of select="$src"/></D_127></xsl:if></xsl:if></S_LEP></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*"/></xsl:if><xsl:if test="$c/()"><S_LH4><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_238><xsl:value-of select="$src"/></D_238></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_364><xsl:value-of select="$src"/></D_364></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_254><xsl:value-of select="$src"/></D_254></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_230><xsl:value-of select="$src"/></D_230></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_230_2><xsl:value-of select="$src"/></D_230_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_230_3><xsl:value-of select="$src"/></D_230_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_271><xsl:value-of select="$src"/></D_271></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_267><xsl:value-of select="$src"/></D_267></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_805><xsl:value-of select="$src"/></D_805></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_986><xsl:value-of select="$src"/></D_986></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_364_2><xsl:value-of select="$src"/></D_364_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355><xsl:value-of select="$src"/></D_355></xsl:if></xsl:if></S_LH4></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_LHT><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_215><xsl:value-of select="$src"/></D_215></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_218><xsl:value-of select="$src"/></D_218></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_222><xsl:value-of select="$src"/></D_222></xsl:if></xsl:if></S_LHT></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 3]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_LHR><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128><xsl:value-of select="$src"/></D_128></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127><xsl:value-of select="$src"/></D_127></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_373><xsl:value-of select="$src"/></D_373></xsl:if></xsl:if></S_LHR></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 10]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_PER><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_366><xsl:value-of select="$src"/></D_366></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_93><xsl:value-of select="$src"/></D_93></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_365><xsl:value-of select="$src"/></D_365></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_364><xsl:value-of select="$src"/></D_364></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_365_2><xsl:value-of select="$src"/></D_365_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_364_2><xsl:value-of select="$src"/></D_364_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_365_3><xsl:value-of select="$src"/></D_365_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_364_3><xsl:value-of select="$src"/></D_364_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_443><xsl:value-of select="$src"/></D_443></xsl:if></xsl:if></S_PER></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 5]"/></xsl:if><xsl:if test="$c/()"><S_LHE><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_224><xsl:value-of select="$src"/></D_224></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_218><xsl:value-of select="$src"/></D_218></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128><xsl:value-of select="$src"/></D_128></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127><xsl:value-of select="$src"/></D_127></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_759><xsl:value-of select="$src"/></D_759></xsl:if></xsl:if></S_LHE></xsl:if></G_LH1></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 100]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><G_CLD><S_CLD><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_622><xsl:value-of select="$src"/></D_622></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_382><xsl:value-of select="$src"/></D_382></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_103><xsl:value-of select="$src"/></D_103></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_357><xsl:value-of select="$src"/></D_357></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355><xsl:value-of select="$src"/></D_355></xsl:if></xsl:if></S_CLD><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_REF><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128><xsl:value-of select="$src"/></D_128></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127><xsl:value-of select="$src"/></D_127></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_352><xsl:value-of select="$src"/></D_352></xsl:if></xsl:if><xsl:if test="$c/()"><C_C040><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128><xsl:value-of select="$src"/></D_128></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127><xsl:value-of select="$src"/></D_127></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128_2><xsl:value-of select="$src"/></D_128_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127_2><xsl:value-of select="$src"/></D_127_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128_3><xsl:value-of select="$src"/></D_128_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127_3><xsl:value-of select="$src"/></D_127_3></xsl:if></xsl:if></C_C040></xsl:if></S_REF></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 200]"/></xsl:if><xsl:if test="$c/()"><S_DTP><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_374><xsl:value-of select="$src"/></D_374></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1250><xsl:value-of select="$src"/></D_1250></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1251><xsl:value-of select="$src"/></D_1251></xsl:if></S_DTP></xsl:if></G_CLD></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 200]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_MAN><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_88><xsl:value-of select="$src"/></D_88></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_87><xsl:value-of select="$src"/></D_87></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_87_2><xsl:value-of select="$src"/></D_87_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_88_2><xsl:value-of select="$src"/></D_88_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_87_3><xsl:value-of select="$src"/></D_87_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_87_4><xsl:value-of select="$src"/></D_87_4></xsl:if></xsl:if></S_MAN></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_DTM><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_374><xsl:value-of select="$src"/></D_374></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_373><xsl:value-of select="$src"/></D_373></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_337><xsl:value-of select="$src"/></D_337></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_623><xsl:value-of select="$src"/></D_623></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1250><xsl:value-of select="$src"/></D_1250></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1251><xsl:value-of select="$src"/></D_1251></xsl:if></xsl:if></S_DTM></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 10]"/></xsl:if><xsl:if test="$c/()"><S_FOB><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_146><xsl:value-of select="$src"/></D_146></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_309><xsl:value-of select="$src"/></D_309></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_352><xsl:value-of select="$src"/></D_352></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_334><xsl:value-of select="$src"/></D_334></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_335><xsl:value-of select="$src"/></D_335></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_309_2><xsl:value-of select="$src"/></D_309_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_352_2><xsl:value-of select="$src"/></D_352_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_54><xsl:value-of select="$src"/></D_54></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_352_3><xsl:value-of select="$src"/></D_352_3></xsl:if></xsl:if></S_FOB></xsl:if><xsl:if test="$c/()"><S_PAL><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_883><xsl:value-of select="$src"/></D_883></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_884><xsl:value-of select="$src"/></D_884></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_885><xsl:value-of select="$src"/></D_885></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_356><xsl:value-of select="$src"/></D_356></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_395><xsl:value-of select="$src"/></D_395></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355><xsl:value-of select="$src"/></D_355></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_82><xsl:value-of select="$src"/></D_82></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_189><xsl:value-of select="$src"/></D_189></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_65><xsl:value-of select="$src"/></D_65></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_2><xsl:value-of select="$src"/></D_355_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_384><xsl:value-of select="$src"/></D_384></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_3><xsl:value-of select="$src"/></D_355_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_385><xsl:value-of select="$src"/></D_385></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_4><xsl:value-of select="$src"/></D_355_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_399><xsl:value-of select="$src"/></D_399></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_810><xsl:value-of select="$src"/></D_810></xsl:if></xsl:if></S_PAL></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><G_N1><S_N1><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_98><xsl:value-of select="$src"/></D_98></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_93><xsl:value-of select="$src"/></D_93></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_66><xsl:value-of select="$src"/></D_66></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_67><xsl:value-of select="$src"/></D_67></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_706><xsl:value-of select="$src"/></D_706></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_98_2><xsl:value-of select="$src"/></D_98_2></xsl:if></xsl:if></S_N1><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_N2><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_93><xsl:value-of select="$src"/></D_93></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_93_2><xsl:value-of select="$src"/></D_93_2></xsl:if></xsl:if></S_N2></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 2]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_N3><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_166><xsl:value-of select="$src"/></D_166></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_166_2><xsl:value-of select="$src"/></D_166_2></xsl:if></xsl:if></S_N3></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 2]"/></xsl:if><xsl:if test="$c/()"><S_N4><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_19><xsl:value-of select="$src"/></D_19></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_156><xsl:value-of select="$src"/></D_156></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_116><xsl:value-of select="$src"/></D_116></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_26><xsl:value-of select="$src"/></D_26></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_309><xsl:value-of select="$src"/></D_309></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_310><xsl:value-of select="$src"/></D_310></xsl:if></xsl:if></S_N4></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_REF><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128><xsl:value-of select="$src"/></D_128></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127><xsl:value-of select="$src"/></D_127></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_352><xsl:value-of select="$src"/></D_352></xsl:if></xsl:if><xsl:if test="$c/()"><C_C040><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128><xsl:value-of select="$src"/></D_128></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127><xsl:value-of select="$src"/></D_127></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128_2><xsl:value-of select="$src"/></D_128_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127_2><xsl:value-of select="$src"/></D_127_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128_3><xsl:value-of select="$src"/></D_128_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127_3><xsl:value-of select="$src"/></D_127_3></xsl:if></xsl:if></C_C040></xsl:if></S_REF></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 12]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_PER><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_366><xsl:value-of select="$src"/></D_366></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_93><xsl:value-of select="$src"/></D_93></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_365><xsl:value-of select="$src"/></D_365></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_364><xsl:value-of select="$src"/></D_364></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_365_2><xsl:value-of select="$src"/></D_365_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_364_2><xsl:value-of select="$src"/></D_364_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_365_3><xsl:value-of select="$src"/></D_365_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_364_3><xsl:value-of select="$src"/></D_364_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_443><xsl:value-of select="$src"/></D_443></xsl:if></xsl:if></S_PER></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 3]"/></xsl:if><xsl:if test="$c/()"><S_FOB><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_146><xsl:value-of select="$src"/></D_146></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_309><xsl:value-of select="$src"/></D_309></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_352><xsl:value-of select="$src"/></D_352></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_334><xsl:value-of select="$src"/></D_334></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_335><xsl:value-of select="$src"/></D_335></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_309_2><xsl:value-of select="$src"/></D_309_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_352_2><xsl:value-of select="$src"/></D_352_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_54><xsl:value-of select="$src"/></D_54></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_352_3><xsl:value-of select="$src"/></D_352_3></xsl:if></xsl:if></S_FOB></xsl:if></G_N1></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 200]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_SDQ><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355><xsl:value-of select="$src"/></D_355></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_66><xsl:value-of select="$src"/></D_66></xsl:if></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_67><xsl:value-of select="$src"/></D_67></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_380><xsl:value-of select="$src"/></D_380></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_67_2><xsl:value-of select="$src"/></D_67_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_380_2><xsl:value-of select="$src"/></D_380_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_67_3><xsl:value-of select="$src"/></D_67_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_380_3><xsl:value-of select="$src"/></D_380_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_67_4><xsl:value-of select="$src"/></D_67_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_380_4><xsl:value-of select="$src"/></D_380_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_67_5><xsl:value-of select="$src"/></D_67_5></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_380_5><xsl:value-of select="$src"/></D_380_5></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_67_6><xsl:value-of select="$src"/></D_67_6></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_380_6><xsl:value-of select="$src"/></D_380_6></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_67_7><xsl:value-of select="$src"/></D_67_7></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_380_7><xsl:value-of select="$src"/></D_380_7></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_67_8><xsl:value-of select="$src"/></D_67_8></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_380_8><xsl:value-of select="$src"/></D_380_8></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_67_9><xsl:value-of select="$src"/></D_67_9></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_380_9><xsl:value-of select="$src"/></D_380_9></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_67_10><xsl:value-of select="$src"/></D_67_10></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_380_10><xsl:value-of select="$src"/></D_380_10></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_310><xsl:value-of select="$src"/></D_310></xsl:if></xsl:if></S_SDQ></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 50]"/></xsl:if><xsl:if test="$c/()"><S_ETD><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_626><xsl:value-of select="$src"/></D_626></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_627><xsl:value-of select="$src"/></D_627></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128><xsl:value-of select="$src"/></D_128></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127><xsl:value-of select="$src"/></D_127></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_743><xsl:value-of select="$src"/></D_743></xsl:if></xsl:if></S_ETD></xsl:if><xsl:if test="$c/()"><S_CUR><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_98><xsl:value-of select="$src"/></D_98></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_100><xsl:value-of select="$src"/></D_100></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_280><xsl:value-of select="$src"/></D_280></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_98_2><xsl:value-of select="$src"/></D_98_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_100_2><xsl:value-of select="$src"/></D_100_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_669><xsl:value-of select="$src"/></D_669></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_374><xsl:value-of select="$src"/></D_374></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_373><xsl:value-of select="$src"/></D_373></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_337><xsl:value-of select="$src"/></D_337></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_374_2><xsl:value-of select="$src"/></D_374_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_373_2><xsl:value-of select="$src"/></D_373_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_337_2><xsl:value-of select="$src"/></D_337_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_374_3><xsl:value-of select="$src"/></D_374_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_373_3><xsl:value-of select="$src"/></D_373_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_337_3><xsl:value-of select="$src"/></D_337_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_374_4><xsl:value-of select="$src"/></D_374_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_373_4><xsl:value-of select="$src"/></D_373_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_337_4><xsl:value-of select="$src"/></D_337_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_374_5><xsl:value-of select="$src"/></D_374_5></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_373_5><xsl:value-of select="$src"/></D_373_5></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_337_5><xsl:value-of select="$src"/></D_337_5></xsl:if></xsl:if></S_CUR></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><G_SAC><S_SAC><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_248><xsl:value-of select="$src"/></D_248></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1300><xsl:value-of select="$src"/></D_1300></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_559><xsl:value-of select="$src"/></D_559></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1301><xsl:value-of select="$src"/></D_1301></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_610><xsl:value-of select="$src"/></D_610></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_378><xsl:value-of select="$src"/></D_378></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_332><xsl:value-of select="$src"/></D_332></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_118><xsl:value-of select="$src"/></D_118></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355><xsl:value-of select="$src"/></D_355></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_380><xsl:value-of select="$src"/></D_380></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_380_2><xsl:value-of select="$src"/></D_380_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_331><xsl:value-of select="$src"/></D_331></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127><xsl:value-of select="$src"/></D_127></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_770><xsl:value-of select="$src"/></D_770></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_352><xsl:value-of select="$src"/></D_352></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_819><xsl:value-of select="$src"/></D_819></xsl:if></xsl:if></S_SAC><xsl:if test="$c/()"><S_CUR><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_98><xsl:value-of select="$src"/></D_98></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_100><xsl:value-of select="$src"/></D_100></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_280><xsl:value-of select="$src"/></D_280></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_98_2><xsl:value-of select="$src"/></D_98_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_100_2><xsl:value-of select="$src"/></D_100_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_669><xsl:value-of select="$src"/></D_669></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_374><xsl:value-of select="$src"/></D_374></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_373><xsl:value-of select="$src"/></D_373></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_337><xsl:value-of select="$src"/></D_337></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_374_2><xsl:value-of select="$src"/></D_374_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_373_2><xsl:value-of select="$src"/></D_373_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_337_2><xsl:value-of select="$src"/></D_337_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_374_3><xsl:value-of select="$src"/></D_374_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_373_3><xsl:value-of select="$src"/></D_373_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_337_3><xsl:value-of select="$src"/></D_337_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_374_4><xsl:value-of select="$src"/></D_374_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_373_4><xsl:value-of select="$src"/></D_373_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_337_4><xsl:value-of select="$src"/></D_337_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_374_5><xsl:value-of select="$src"/></D_374_5></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_373_5><xsl:value-of select="$src"/></D_373_5></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_337_5><xsl:value-of select="$src"/></D_337_5></xsl:if></xsl:if></S_CUR></xsl:if></G_SAC></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*"/></xsl:if><xsl:if test="$c/()"><S_GF><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128><xsl:value-of select="$src"/></D_128></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127><xsl:value-of select="$src"/></D_127></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_367><xsl:value-of select="$src"/></D_367></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_782><xsl:value-of select="$src"/></D_782></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128_2><xsl:value-of select="$src"/></D_128_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127_2><xsl:value-of select="$src"/></D_127_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_328><xsl:value-of select="$src"/></D_328></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_128_3><xsl:value-of select="$src"/></D_128_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_127_3><xsl:value-of select="$src"/></D_127_3></xsl:if></xsl:if></S_GF></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_YNQ><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1321><xsl:value-of select="$src"/></D_1321></xsl:if></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1073><xsl:value-of select="$src"/></D_1073></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1250><xsl:value-of select="$src"/></D_1250></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1251><xsl:value-of select="$src"/></D_1251></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_933><xsl:value-of select="$src"/></D_933></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_933_2><xsl:value-of select="$src"/></D_933_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_933_3><xsl:value-of select="$src"/></D_933_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1270><xsl:value-of select="$src"/></D_1270></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1271><xsl:value-of select="$src"/></D_1271></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_933_4><xsl:value-of select="$src"/></D_933_4></xsl:if></xsl:if></S_YNQ></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 10]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><G_LM><S_LM><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_559><xsl:value-of select="$src"/></D_559></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_822><xsl:value-of select="$src"/></D_822></xsl:if></xsl:if></S_LM><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_LQ><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1270><xsl:value-of select="$src"/></D_1270></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1271><xsl:value-of select="$src"/></D_1271></xsl:if></xsl:if></S_LQ></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 100]"/></G_LM></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 10]"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><G_V1><S_V1><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_597><xsl:value-of select="$src"/></D_597></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_182><xsl:value-of select="$src"/></D_182></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_26><xsl:value-of select="$src"/></D_26></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_55><xsl:value-of select="$src"/></D_55></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_140><xsl:value-of select="$src"/></D_140></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_249><xsl:value-of select="$src"/></D_249></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_854><xsl:value-of select="$src"/></D_854></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_897><xsl:value-of select="$src"/></D_897></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_91><xsl:value-of select="$src"/></D_91></xsl:if></xsl:if></S_V1><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_R4><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_115><xsl:value-of select="$src"/></D_115></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_309><xsl:value-of select="$src"/></D_309></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_310><xsl:value-of select="$src"/></D_310></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_114><xsl:value-of select="$src"/></D_114></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_26><xsl:value-of select="$src"/></D_26></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_174><xsl:value-of select="$src"/></D_174></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_113><xsl:value-of select="$src"/></D_113></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_156><xsl:value-of select="$src"/></D_156></xsl:if></xsl:if></S_R4></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*"/></xsl:if><xsl:if test="$c/()"><xsl:variable name="src"><xsl:for-each select="$c/(.)"><xsl:variable name="c" select="."/><S_DTM><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_374><xsl:value-of select="$src"/></D_374></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_373><xsl:value-of select="$src"/></D_373></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_337><xsl:value-of select="$src"/></D_337></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_623><xsl:value-of select="$src"/></D_623></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1250><xsl:value-of select="$src"/></D_1250></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1251><xsl:value-of select="$src"/></D_1251></xsl:if></xsl:if></S_DTM></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*"/></xsl:if></G_V1></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*"/></xsl:if></G_HL></xsl:for-each></xsl:variable><xsl:copy-of select="$src/*[position() &lt;= 200000]"/><xsl:if test="$c/()"><S_CTT><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_354><xsl:value-of select="$src"/></D_354></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_347><xsl:value-of select="$src"/></D_347></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_81><xsl:value-of select="$src"/></D_81></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355><xsl:value-of select="$src"/></D_355></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_183><xsl:value-of select="$src"/></D_183></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_355_2><xsl:value-of select="$src"/></D_355_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_352><xsl:value-of select="$src"/></D_352></xsl:if></xsl:if></S_CTT></xsl:if><S_SE><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_96><xsl:value-of select="$src"/></D_96></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_329><xsl:value-of select="$src"/></D_329></xsl:if></S_SE></M_856></xsl:template><xsl:function name="ica_fn:f1"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(3)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->

            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in[1]">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>856</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f2"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(4)"/><xsl:variable name="target_tag_max_length" select="(9)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->

            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in[1]">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>2</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f3"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(2)"/><xsl:variable name="target_tag_max_length" select="(2)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->

            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in[1]">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>3</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f4"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(3)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->

            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in[1]">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>011</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f5"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(8)"/><xsl:variable name="target_tag_max_length" select="(8)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->

            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in[1]">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>20240826</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f6"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(12)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->

            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in[1]">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>1</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f7"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(2)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->

            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in[1]">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>12</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:param name="defaultChar" select="'#'"/><xsl:function as="xs:string" name="ica_fn:length-constrains"><xsl:param name="text"/><xsl:param as="xs:integer" name="minLen"/><xsl:param as="xs:integer" name="maxLen"/><xsl:sequence select="ica_fn:length-constrains($text, $minLen, $maxLen, $defaultChar)"/></xsl:function><xsl:function as="xs:string" name="ica_fn:length-constrains"><xsl:param name="text"/><xsl:param as="xs:integer" name="minLen"/><xsl:param as="xs:integer" name="maxLen"/><xsl:param as="xs:string" name="defaultChar"/><xsl:variable name="defaultChar" select="substring($defaultChar, 1, 1)"/><xsl:variable as="xs:integer" name="textLen" select="string-length($text)"/><xsl:variable name="maxLen" select="             if ($maxLen = -1) then               ($textLen)             else               ($maxLen)"/><xsl:sequence select="             if ($textLen gt $maxLen) then               substring($text, 1, $maxLen)             else               if ($textLen lt $minLen) then                 string-join(($text,                 (for $c in $textLen + 1 to $minLen                 return                   $defaultChar)), '')               else                 $text             "/></xsl:function><!--

        Public Function Library N-to-1 Date Time Conversion - Handle missing date parts
      --><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:boolean" name="USE-UTC-FOR-CURRENT-DATE-TIME" select="true()"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="CURRENT-DATE-TIME-PICTURE-STRING" select="'CCYY''-''MM''-''DD''T''hh'':''mm'':''ss[''.''ff*][Z|(+|-)hh'':''mm]'"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="element(currentDateTime)" name="PRECALCULATED-CURRENT-DATE-TIME">
    <xsl:element name="currentDateTime">
      <xsl:attribute name="CC" select="ica_fn:calculateCurrentDateTimeForPictureString('CC', $USE-UTC-FOR-CURRENT-DATE-TIME)"/>
      <xsl:attribute name="YY" select="ica_fn:calculateCurrentDateTimeForPictureString('YY', $USE-UTC-FOR-CURRENT-DATE-TIME)"/>
      <xsl:attribute name="MM" select="ica_fn:calculateCurrentDateTimeForPictureString('MM', $USE-UTC-FOR-CURRENT-DATE-TIME)"/>
      <xsl:attribute name="DD" select="ica_fn:calculateCurrentDateTimeForPictureString('DD', $USE-UTC-FOR-CURRENT-DATE-TIME)"/>
      <xsl:attribute name="hh" select="ica_fn:calculateCurrentDateTimeForPictureString('hh', $USE-UTC-FOR-CURRENT-DATE-TIME)"/>
      <xsl:attribute name="mm" select="ica_fn:calculateCurrentDateTimeForPictureString('mm', $USE-UTC-FOR-CURRENT-DATE-TIME)"/>
      <xsl:attribute name="ss" select="ica_fn:calculateCurrentDateTimeForPictureString('ss', $USE-UTC-FOR-CURRENT-DATE-TIME)"/>
    </xsl:element>
  </xsl:variable><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="ica_fn:transform-dateTime-multipleSources-missingDateParts">
    <xsl:param as="xs:string*" name="dateTimeLikes"/>
    <xsl:param as="xs:string*" name="source-picture-strings"/>
    <xsl:param as="xs:string*" name="picture-string-conflict-resolutions"/>
    <xsl:param as="xs:string*" name="generic-picture-strings"/>
    <xsl:param as="xs:string*" name="generic-types"/>
    <xsl:param as="xs:string*" name="generic-values"/>
    <xsl:param as="xs:string" name="target-picture-string"/>
    
    <!-- As $dateTimeLikes may not only contain date/time information but also  -->
    <!-- all the other parameters (such as the source-picture-strings, etc.),   -->
    <!-- the $dateTimeLikes sequence is truncated to the number of source       -->
    <!-- picture strings that represent the number of the input source domains. -->
    <xsl:variable name="numOfSourcePictureStrings" select="count($source-picture-strings)"/>
    <xsl:variable name="dateTimeLikes" select="subsequence($dateTimeLikes, 1, $numOfSourcePictureStrings)"/>

    <xsl:variable as="element(e)*" name="rowVectors" select="ica_fn:colToRowVectorTransformation($generic-picture-strings, $generic-types, $generic-values)"/>
    <xsl:for-each select="$rowVectors">
      <xsl:message select="concat('pic=', ./@pic, ' type=', ./@type, ' value=', ./@value)"/>
    </xsl:for-each>
    
    <xsl:variable as="element(e)*" name="rowVectorsWithTypeFixed" select="ica_fn:filterTypeFixed($rowVectors)"/>
    <xsl:variable as="element(e)*" name="rowVectorsWithTypeCurrentDateTime" select="ica_fn:filterTypeCurrentDateTime($rowVectors)"/>

    <xsl:variable as="xs:string*" name="dateTimeLikes">
      <xsl:sequence select="$dateTimeLikes"/>
      <xsl:if test="exists($rowVectorsWithTypeFixed)">
        <xsl:sequence select="ica_fn:selectValue($rowVectorsWithTypeFixed)"/>
      </xsl:if>
      <xsl:if test="exists($rowVectorsWithTypeCurrentDateTime)">
        <xsl:variable as="xs:string*" name="currentDateTimePics" select="ica_fn:selectPic($rowVectorsWithTypeCurrentDateTime)"/>
        <xsl:variable as="xs:string*" name="currentDateTimeValues">
          <xsl:for-each select="$currentDateTimePics">
            <xsl:variable as="xs:string" name="pic" select="."/>
            <xsl:sequence select="ica_fn:getCurrentDateTimeForPictureString($pic)"/>
          </xsl:for-each>
        </xsl:variable>
        <xsl:sequence select="$currentDateTimeValues"/>
      </xsl:if>
    </xsl:variable>

    <xsl:variable as="xs:string*" name="source-picture-strings">
      <xsl:sequence select="$source-picture-strings"/>
      <xsl:if test="exists($rowVectorsWithTypeFixed)">
        <xsl:sequence select="ica_fn:selectPic($rowVectorsWithTypeFixed)"/>
      </xsl:if>
      <xsl:if test="exists($rowVectorsWithTypeCurrentDateTime)">
        <xsl:sequence select="ica_fn:selectPic($rowVectorsWithTypeCurrentDateTime)"/>
      </xsl:if>
    </xsl:variable>

    <xsl:variable as="xs:string*" name="picture-string-conflict-resolutions">
      <xsl:sequence select="$picture-string-conflict-resolutions"/>
      <xsl:if test="exists($rowVectorsWithTypeFixed)">
        <xsl:sequence select="ica_fn:selectPic($rowVectorsWithTypeFixed)"/>
      </xsl:if>
      <xsl:if test="exists($rowVectorsWithTypeCurrentDateTime)">
        <xsl:sequence select="ica_fn:selectPic($rowVectorsWithTypeCurrentDateTime)"/>
      </xsl:if>
    </xsl:variable>

    <xsl:variable as="xs:string" name="result">
      <!-- Regardless of the existence of conflict resolution information, call transformation for conflicting case. -->
      <xsl:variable name="result" select="ica_fn:transform-dateTime-multipleSources-conflicting($dateTimeLikes, $source-picture-strings, $picture-string-conflict-resolutions, $target-picture-string)"/>
      <xsl:sequence select="$result"/>
    </xsl:variable>
    
    <xsl:sequence select="$result"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="element(e)*" name="ica_fn:colToRowVectorTransformation">
    <xsl:param as="xs:string*" name="generic-picture-strings"/>
    <xsl:param as="xs:string*" name="generic-types"/>
    <xsl:param as="xs:string*" name="generic-values"/>
    
    <xsl:variable as="element(e)*" name="result">
      <xsl:for-each select="$generic-picture-strings">
        <xsl:variable as="xs:integer" name="pos" select="position()"/>
        
        <xsl:variable as="xs:string" name="pic" select="$generic-picture-strings[$pos]"/>
        <xsl:variable as="xs:string" name="type" select="$generic-types[$pos]"/>
        <xsl:variable as="xs:string" name="value" select="$generic-values[$pos]"/>
        
        <xsl:variable as="element(e)" name="item">
          <e pic="{$pic}" type="{$type}" value="{$value}"/>
        </xsl:variable>
        
        <xsl:sequence select="$item"/>
      </xsl:for-each>
    </xsl:variable>
    
    <xsl:sequence select="$result"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string*" name="ica_fn:selectPic">
    <xsl:param as="element(e)*" name="rowVectors"/>
    <xsl:sequence select="ica_fn:selectGeneric($rowVectors, 'pic')"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string*" name="ica_fn:selectType">
    <xsl:param as="element(e)*" name="rowVectors"/>
    <xsl:sequence select="ica_fn:selectGeneric($rowVectors, 'type')"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string*" name="ica_fn:selectValue">
    <xsl:param as="element(e)*" name="rowVectors"/>
    <xsl:sequence select="ica_fn:selectGeneric($rowVectors, 'value')"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string*" name="ica_fn:selectGeneric">
    <xsl:param as="element(e)*" name="rowVectors"/>
    <xsl:param as="xs:string" name="colName"/>
    
    <xsl:variable as="xs:string*" name="result">
      <xsl:for-each select="$rowVectors">
        <xsl:sequence select="./@*[name() eq $colName]"/>
      </xsl:for-each>
    </xsl:variable>
    
    <xsl:sequence select="$result"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="element(e)*" name="ica_fn:filterTypeFixed">
    <xsl:param as="element(e)*" name="rowVectors"/>
    <xsl:sequence select="ica_fn:filterGeneric($rowVectors, 'type', 'FIXED')"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="element(e)*" name="ica_fn:filterTypeCurrentDateTime">
    <xsl:param as="element(e)*" name="rowVectors"/>
    <xsl:sequence select="ica_fn:filterGeneric($rowVectors, 'type', 'CURRENT_DATE_TIME')"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="element(e)*" name="ica_fn:filterGeneric">
    <xsl:param as="element(e)*" name="rowVectors"/>
    <xsl:param as="xs:string" name="colName"/>
    <xsl:param as="xs:string" name="colValue"/>
    
    <xsl:variable as="element(e)*" name="result">
      <xsl:for-each select="$rowVectors[@*[name() eq $colName] eq $colValue]">
        <xsl:sequence select="."/>
      </xsl:for-each>
    </xsl:variable>
    
    <xsl:sequence select="$result"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="ica_fn:getCurrentDateTimeForPictureString">
    <xsl:param as="xs:string" name="pic"/>
    <xsl:sequence select="$PRECALCULATED-CURRENT-DATE-TIME/@*[name() eq $pic]"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="ica_fn:calculateCurrentDateTimeForPictureString">
    <xsl:param as="xs:string" name="pic"/>
    <xsl:param as="xs:boolean" name="useUTC"/>
    <xsl:choose>
      <xsl:when test="$useUTC">
        <xsl:sequence select="ica_fn:transform-dateTime(string(adjust-dateTime-to-timezone(current-dateTime(), xs:dayTimeDuration('PT0H'))), $CURRENT-DATE-TIME-PICTURE-STRING, $pic)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:sequence select="ica_fn:transform-dateTime(string(current-dateTime()), $CURRENT-DATE-TIME-PICTURE-STRING, $pic)"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:function><!--

        Public Function Library N-to-1 Date Time Conversion
      --><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" name="error:conflicting-ps" select="'Conflicting picture strings:'"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="TIME-ZONE-PLUS-MINUS" select="'(+|-)'"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="TIME-ZONE-PLUS-MINUS-ESCAPED-FOR-REGEX" select="'\(\+\|\-\)'"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="TIME-ZONE-HOURS" select="'th'"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="TIME-ZONE-MINUTES" select="'tm'"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="HOURS" select="'hh'"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="MINUTES" select="'mm'"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="DOLLAR" select="'$'"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="QUOTED-DOLLAR" select="'''$'''"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="QUOTED-AT" select="'''@'''"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="FS" select="'fs'"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="TZHHMM" select="'tzhhmm'"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="F-ASTERISK-ESCAPED-FOR-REGEX" select="'f\*'"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="F" select="'f'"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string*" name="PLACEHOLDERS-WITHOUT-FS-AND-TZHHMM" select="('CC','YY','MM','DD','hh','mm','ss','Z')"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string*" name="PLACEHOLDERS-WITH-FS-AND-TZHHMM" select="('CC','YY','MM','DD','hh','mm','ss','Z','fs','tzhhmm')"/><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="ica_fn:transform-dateTime-multipleSources">
    <xsl:param as="xs:string*" name="dateTimeLikes"/>
    <xsl:param as="xs:string*" name="source-picture-strings"/>
    <xsl:param as="xs:string*" name="picture-string-conflict-resolutions"/>
    <xsl:param as="xs:string" name="target-picture-string"/>
    
    <!-- As $dateTimeLikes may not only contain date/time information but also  -->
    <!-- all the other parameters (such as the source-picture-strings, etc.),   -->
    <!-- the $dateTimeLikes sequence is truncated to the number of source       -->
    <!-- picture strings that represent the number of the input source domains. -->
    <xsl:variable name="numOfSourcePictureStrings" select="count($source-picture-strings)"/>
    <xsl:variable name="dateTimeLikes" select="subsequence($dateTimeLikes, 1, $numOfSourcePictureStrings)"/>
    
    <xsl:variable name="result">
      <xsl:choose>
        <!-- If there is only one date/time input domain, treat the -->
        <!-- date/time conversion mapping element as in the 1-to-1 case. -->
        <xsl:when test="count($dateTimeLikes) eq 1">
          <xsl:variable as="xs:string" name="dateTimeLike" select="$dateTimeLikes[1]"/>
          <xsl:variable as="xs:string" name="source-picture-string" select="$source-picture-strings[1]"/>
          <xsl:variable name="result" select="ica_fn:transform-dateTime($dateTimeLike, $source-picture-string, $target-picture-string)"/>
          <xsl:sequence select="$result"/>
        </xsl:when>
        <xsl:otherwise>
          <!-- Regardless of the existence of conflict resolution information, call transformation for conflicting case. -->
          <xsl:variable name="result" select="ica_fn:transform-dateTime-multipleSources-conflicting($dateTimeLikes, $source-picture-strings, $picture-string-conflict-resolutions, $target-picture-string)"/>
          <xsl:sequence select="$result"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    
    <xsl:sequence select="$result"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="ica_fn:transform-dateTime-multipleSources-nonConflicting">
    <xsl:param as="xs:string*" name="dateTimeLikes"/>
    <xsl:param as="xs:string*" name="source-picture-strings"/>
    <xsl:param as="xs:string" name="target-picture-string"/>
    
    <xsl:variable as="xs:integer*" name="indices-of-input-picture-strings-containing-time-zone" select="ica_fn:indices-of-picture-strings-containing-time-zone($source-picture-strings)"/>

    <xsl:variable as="xs:integer" name="pos-with-time-zone">
      <xsl:choose>
        <xsl:when test="count($indices-of-input-picture-strings-containing-time-zone) gt 1">
	      <xsl:sequence select="error:throw(concat('More than one time zone particle found in picture strings ', string-join($source-picture-strings, ', ')), $error:conflicting-ps)"/>
        </xsl:when>
        <xsl:when test="count($indices-of-input-picture-strings-containing-time-zone) eq 1">
          <xsl:sequence select="$indices-of-input-picture-strings-containing-time-zone[1]"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:sequence select="count($source-picture-strings)"/>
        </xsl:otherwise>  
      </xsl:choose>
    </xsl:variable>

    <xsl:variable as="xs:integer" name="number-of-picture-strings" select="count($source-picture-strings)"/>

    <!-- Swap the elements at the positions $pos-with-time-zone and $number-of-picture-strings -->
    <!-- in the sequences $dateTimeLikes and $source-picture-strings. If those positions are -->
    <!-- same, then ica_fn:swap-elements-in-sequence will return the unmodified sequence. -->
    <!-- Therefore, if $pos-with-time-zone is 0, i.e., no position exists with time zone information, -->
    <!-- then set $pos-with-time-zone to the value of $number-of-picture-strings, i.e., the -->
    <!-- replacement will not have any effect. --> 
    <xsl:variable as="xs:integer" name="pos-with-time-zone" select="if($pos-with-time-zone ne 0) then $pos-with-time-zone else $number-of-picture-strings"/>
    <xsl:variable as="xs:string*" name="dateTimeLikes" select="ica_fn:swap-elements-in-sequence($dateTimeLikes, $pos-with-time-zone, $number-of-picture-strings)"/>
    <xsl:variable as="xs:string*" name="source-picture-strings" select="ica_fn:swap-elements-in-sequence($source-picture-strings, $pos-with-time-zone, $number-of-picture-strings)"/>

    <xsl:variable name="combinedSourceDateTimeLike" select="string-join($dateTimeLikes, $DOLLAR)"/>
    <xsl:variable name="combinedSourcePictureString" select="string-join($source-picture-strings, $QUOTED-DOLLAR)"/>

    <xsl:variable name="result" select="ica_fn:transform-dateTime($combinedSourceDateTimeLike, $combinedSourcePictureString, $target-picture-string)"/>
    
    <xsl:sequence select="$result"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string*" name="ica_fn:swap-elements-in-sequence">
    <xsl:param as="xs:string*" name="seq"/>
    <xsl:param as="xs:integer" name="pos1"/>
    <xsl:param as="xs:integer" name="pos2"/>
  
	<xsl:sequence select="       for $pos in 1 to count($seq) return         $seq[              if ($pos eq $pos1) then                  $pos2              else if ($pos eq $pos2) then                  $pos1              else                  $pos             ]       "/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:integer*" name="ica_fn:indices-of-picture-strings-containing-time-zone">
    <xsl:param as="xs:string*" name="picture-strings"/>
    <xsl:for-each select="$picture-strings">
      <xsl:variable as="xs:integer" name="pos" select="position()"/>
      <xsl:if test="contains($picture-strings[$pos], $TIME-ZONE-PLUS-MINUS)">
        <xsl:sequence select="$pos"/>
      </xsl:if>
    </xsl:for-each>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="ica_fn:transform-dateTime-multipleSources-conflicting">
    <xsl:param as="xs:string*" name="dateTimeLikes"/>
    <xsl:param as="xs:string*" name="source-picture-strings"/>
    <xsl:param as="xs:string*" name="picture-string-conflict-resolutions"/>
    <xsl:param as="xs:string" name="target-picture-string"/>

    <xsl:variable name="source-picture-strings-reduced" select="ica_fn:reduceSourcePictureStrings($source-picture-strings, $picture-string-conflict-resolutions)"/>
    <xsl:variable name="dateTimeLikes-reduced" select="ica_fn:reduceDateTimeLikes($dateTimeLikes, $source-picture-strings, $source-picture-strings-reduced)"/>
    <xsl:variable name="result" select="ica_fn:transform-dateTime-multipleSources-nonConflicting($dateTimeLikes-reduced, $source-picture-strings-reduced, $target-picture-string)"/>
    
    <xsl:sequence select="$result"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string*" name="ica_fn:reduceSourcePictureStrings">
    <xsl:param as="xs:string*" name="source-picture-strings"/>
    <xsl:param as="xs:string*" name="picture-string-conflict-resolutions"/>

    <xsl:variable as="xs:string*" name="result">
      <xsl:for-each select="$source-picture-strings">
        <xsl:variable name="pos" select="position()"/>
        <xsl:variable name="source-picture-string" select="$source-picture-strings[$pos]"/>
        <xsl:variable name="inbound-conflicting-placeholders" select="ica_fn:conflicting-placeholders-in-picture-strings($source-picture-strings)"/>
        <xsl:variable name="picture-string-conflict-resolution" select="$picture-string-conflict-resolutions[$pos]"/>

        <xsl:sequence select="ica_fn:reduce($source-picture-string, $inbound-conflicting-placeholders, $picture-string-conflict-resolution)"/>
      </xsl:for-each>
    </xsl:variable>

    <xsl:sequence select="$result"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string*" name="ica_fn:reduceDateTimeLikes">
    <xsl:param as="xs:string*" name="dateTimeLikes"/>
    <xsl:param as="xs:string*" name="source-picture-strings"/>
    <xsl:param as="xs:string*" name="source-picture-strings-reduced"/>

    <xsl:variable as="xs:string*" name="result">
      <xsl:for-each select="$dateTimeLikes">
        <xsl:variable name="pos" select="position()"/>
        <xsl:variable name="dateTimeLike" select="$dateTimeLikes[$pos]"/>
        <xsl:variable name="source-picture-string" select="$source-picture-strings[$pos]"/>
        <xsl:variable name="source-picture-string-reduced" select="$source-picture-strings-reduced[$pos]"/>

        <xsl:sequence select="ica_fn:transform-dateTime($dateTimeLike, $source-picture-string, $source-picture-string-reduced)"/>
      </xsl:for-each>
    </xsl:variable>
    
    <xsl:sequence select="$result"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="ica_fn:reduce">
    <xsl:param as="xs:string" name="picture-string"/>
    <xsl:param as="xs:string*" name="inbound-conflicting-placeholders"/>
    <xsl:param as="xs:string" name="picture-string-conflict-resolution"/>
    
    <xsl:variable as="xs:string" name="result">
      <xsl:choose>
        <xsl:when test="$picture-string ne ''">
          <xsl:variable as="xs:string*" name="picture-string-conflict-resolution-as-placeholder-sequence" select="ica_fn:convert-conflict-resolution-vector-to-placeholder-sequence($picture-string-conflict-resolution)"/>
          <xsl:variable as="xs:string*" name="placeholders-to-remove" select="$inbound-conflicting-placeholders[not(. = $picture-string-conflict-resolution-as-placeholder-sequence)]"/>
          
          <!-- Optimization: Ensure that the placeholders-to-remove-regex does not contain the special placeholders 'tzhhmm' and 'fs' even if they do not harm! -->
          <xsl:variable as="xs:string" name="placeholders-to-remove-regex" select="string-join($placeholders-to-remove, '|')"/>

          <xsl:variable as="xs:string" name="picture-string-reduced" select="ica_fn:mark-time-zone-placeholders($picture-string)"/>
          <xsl:variable as="xs:string" name="picture-string-reduced" select="             if($placeholders-to-remove-regex ne '')             then replace($picture-string-reduced, $placeholders-to-remove-regex, $QUOTED-AT)             else $picture-string-reduced           "/>

          <xsl:variable as="xs:boolean" name="reduce-all-fractional-seconds-related-placeholders" select="$placeholders-to-remove[. = $FS]              and not($picture-string-conflict-resolution-as-placeholder-sequence[. = $FS])"/>

          <xsl:variable as="xs:string" name="picture-string-reduced" select="             if($reduce-all-fractional-seconds-related-placeholders)             (: Since placeholder 'f' is infix of placeholder 'f*', the replacement of 'f*' must occur before 'f'. :)             then replace(replace($picture-string-reduced, $F-ASTERISK-ESCAPED-FOR-REGEX, $QUOTED-AT), $F, $QUOTED-AT)             else $picture-string-reduced           "/>

          <xsl:variable as="xs:boolean" name="reduce-all-time-zone-related-placeholders" select="$placeholders-to-remove[. = $TZHHMM]              and not($picture-string-conflict-resolution-as-placeholder-sequence[. = $TZHHMM])"/>

          <xsl:variable as="xs:string" name="picture-string-reduced" select="             if($reduce-all-time-zone-related-placeholders)             then replace(replace(replace($picture-string-reduced, $TIME-ZONE-PLUS-MINUS-ESCAPED-FOR-REGEX, $QUOTED-AT), $TIME-ZONE-HOURS, $QUOTED-AT), $TIME-ZONE-MINUTES, $QUOTED-AT)             else $picture-string-reduced           "/>

          <xsl:variable as="xs:string" name="picture-string-reduced" select="ica_fn:unmark-time-zone-placeholders($picture-string-reduced)"/>

          <xsl:sequence select="$picture-string-reduced"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:sequence select="''"/>
        </xsl:otherwise>
      </xsl:choose>    
    </xsl:variable>

    <xsl:sequence select="$result"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string*" name="ica_fn:convert-conflict-resolution-vector-to-placeholder-sequence">
    <xsl:param as="xs:string*" name="conflict-resolutions"/>
    
    <xsl:variable as="xs:string*" name="result">
      <xsl:for-each select="$conflict-resolutions">
        <xsl:variable name="pos" select="position()"/> 
        <xsl:variable name="conflict-resolution" select="$conflict-resolutions[$pos]"/> 
        <xsl:if test="$conflict-resolution ne ''">
          <xsl:sequence select="tokenize($conflict-resolution, ' ')"/>
        </xsl:if>
      </xsl:for-each>
    </xsl:variable>

    <xsl:sequence select="$result"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="ica_fn:mark-time-zone-placeholders">
    <xsl:param as="xs:string" name="picture-string"/>

    <xsl:variable as="xs:string" name="result">
      <xsl:choose>
        <xsl:when test="contains($picture-string, $TIME-ZONE-PLUS-MINUS)">
        <xsl:variable as="xs:string" name="before" select="substring-before($picture-string, $TIME-ZONE-PLUS-MINUS)"/>
        <xsl:variable as="xs:string" name="after" select="substring-after($picture-string, $TIME-ZONE-PLUS-MINUS)"/>
        <xsl:variable as="xs:string" name="after" select="replace($after, $HOURS, $TIME-ZONE-HOURS)"/>
        <xsl:variable as="xs:string" name="after" select="replace($after, $MINUTES, $TIME-ZONE-MINUTES)"/>
        <xsl:sequence select="concat($before, $TIME-ZONE-PLUS-MINUS, $after)"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:sequence select="$picture-string"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:sequence select="$result"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string" name="ica_fn:unmark-time-zone-placeholders">
    <xsl:param as="xs:string" name="picture-string"/>
    
    <xsl:variable as="xs:string" name="result" select="$picture-string"/>
    <xsl:variable as="xs:string" name="result" select="replace($result, $TIME-ZONE-HOURS, $HOURS)"/>
    <xsl:variable as="xs:string" name="result" select="replace($result, $TIME-ZONE-MINUTES, $MINUTES)"/>

    <xsl:sequence select="$result"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" as="xs:string*" name="ica_fn:conflicting-placeholders-in-picture-strings">
    <xsl:param as="xs:string*" name="picture-strings"/>
    
    <xsl:variable as="xs:string*" name="placeholders-in-picture-strings">
        
        <xsl:for-each select="$picture-strings">
          <xsl:variable name="picture-string" select="."/>
          <xsl:variable as="xs:string" name="marked-picture-string" select="ica_fn:mark-time-zone-placeholders($picture-string)"/>

          <xsl:for-each select="$PLACEHOLDERS-WITHOUT-FS-AND-TZHHMM">
            <xsl:variable name="placeholder" select="."/>
              <xsl:if test="contains($marked-picture-string, $placeholder)">
                <xsl:sequence select="$placeholder"/>
              </xsl:if>
          </xsl:for-each>

          <xsl:if test="contains($marked-picture-string, $F)">
            <xsl:sequence select="$FS"/>
          </xsl:if>
          <xsl:if test="contains($marked-picture-string, $TIME-ZONE-HOURS) or contains($marked-picture-string, $TIME-ZONE-MINUTES)">
            <xsl:sequence select="$TZHHMM"/>
          </xsl:if>
        </xsl:for-each>
        
    </xsl:variable>

    <xsl:variable as="xs:string*" name="result">
      <xsl:for-each select="$PLACEHOLDERS-WITH-FS-AND-TZHHMM">
        <xsl:variable name="placeholder" select="."/>

          <xsl:if test="count($placeholders-in-picture-strings[. = $placeholder]) gt 1">
            <xsl:sequence select="$placeholder"/>
          </xsl:if>
                  
      </xsl:for-each>
    </xsl:variable>

    <xsl:sequence select="$result"/>
  </xsl:function><!--
        Public Function Library
      --><xsl:function as="item()*" name="ica_fn:concat_with_space_to_sequence">
    <xsl:param name="in"/>
    <xsl:param name="target_tag_names"/>
    <xsl:param name="target_tag_max_lengths"/>
    <xsl:param name="target_tag_min_cardinalities"/>
    <xsl:param name="target_tag_max_cardinalities"/>
    <xsl:value-of select="$in"/>
  </xsl:function><xsl:function as="xs:string" name="ica_fn:transform-dateTime">
    <xsl:param as="xs:string" name="dateTimeLike"/>
    <xsl:param as="xs:string" name="source-picture-string"/>
    <xsl:param as="xs:string" name="target-picture-string"/>
    <xsl:variable name="src-ipr" select="ica_fn:parse-from-picture($source-picture-string)"/>
    <xsl:variable name="trg-ipr" select="ica_fn:parse-from-picture($target-picture-string)"/>
    <xsl:variable name="ifr" select="ica_fn:canonicalize-ifr(ica_fn:parse-ifr($dateTimeLike, $src-ipr))"/>
    <xsl:variable name="ifr" select="ica_fn:ensure-valid-ifr($ifr)"/>
    <xsl:variable name="result" select="ica_fn:format-ifr($trg-ipr, $ifr)"/>
    <xsl:sequence select="$result"/>
  </xsl:function><xsl:function as="xs:dateTime" name="ica_fn:parse-dateTime">
    <xsl:param as="xs:string" name="dateTime"/>
    <xsl:param as="xs:string" name="picture-string"/>
    <xsl:variable name="ipr" select="ica_fn:parse-from-picture($picture-string)"/>
    <xsl:variable name="ifr" select="ica_fn:canonicalize-ifr(ica_fn:parse-ifr($dateTime, $ipr))"/>
    <xsl:variable name="ifr" select="ica_fn:ensure-valid-ifr($ifr)"/>
    <xsl:sequence select="ica_fn:ifr-to-datetime($ifr)"/>
  </xsl:function><xsl:function as="xs:date" name="ica_fn:parse-date">
    <xsl:param as="xs:string" name="date"/>
    <xsl:param as="xs:string" name="picture-string"/>
    <xsl:variable name="ipr" select="ica_fn:parse-from-picture($picture-string)"/>
    <xsl:variable name="ifr" select="ica_fn:canonicalize-ifr(ica_fn:parse-ifr($date, $ipr))"/>
    <xsl:variable name="ifr" select="ica_fn:ensure-valid-ifr($ifr)"/>
    <xsl:sequence select="ica_fn:ifr-to-date($ifr)"/>
  </xsl:function><xsl:function as="xs:time" name="ica_fn:parse-time">
    <xsl:param as="xs:string" name="time"/>
    <xsl:param as="xs:string" name="picture-string"/>
    <xsl:variable name="ipr" select="ica_fn:parse-from-picture($picture-string)"/>
    <xsl:variable name="ifr" select="ica_fn:canonicalize-ifr(ica_fn:parse-ifr($time, $ipr))"/>
    <xsl:variable name="ifr" select="ica_fn:ensure-valid-ifr($ifr)"/>
    <xsl:sequence select="ica_fn:ifr-to-time($ifr)"/>
  </xsl:function><xsl:function as="xs:string" name="ica_fn:format-dateTime">
    <xsl:param as="xs:dateTime" name="dateTime"/>
    <xsl:param as="xs:string" name="picture-string"/>
    <xsl:variable name="ipr" select="ica_fn:parse-from-picture($picture-string)"/>
    <xsl:variable name="ifr" select="ica_fn:datetime-to-ifr($dateTime)"/>
    <xsl:sequence select="ica_fn:format-ifr($ipr, $ifr)"/>
  </xsl:function><xsl:function as="xs:string" name="ica_fn:format-date">
    <xsl:param as="xs:date" name="date"/>
    <xsl:param as="xs:string" name="picture-string"/>
    <xsl:variable name="ipr" select="ica_fn:parse-from-picture($picture-string)"/>
    <xsl:variable name="ifr" select="ica_fn:date-to-ifr($date)"/>
    <xsl:sequence select="ica_fn:format-ifr($ipr, $ifr)"/>
    
  </xsl:function><xsl:function as="xs:string" name="ica_fn:format-time">
    <xsl:param as="xs:time" name="time"/>
    <xsl:param as="xs:string" name="picture-string"/>
    <xsl:variable name="ipr" select="ica_fn:parse-from-picture($picture-string)"/>
    <xsl:variable name="ifr" select="ica_fn:time-to-ifr($time)"/>
    <xsl:sequence select="ica_fn:format-ifr($ipr, $ifr)"/>
  </xsl:function><xsl:function name="ica_fn:replaceInSequence">
    <xsl:param as="item()*" name="sequence"/>
    <xsl:param as="item()" name="search"/>
    <xsl:param as="item()" name="replace"/>
    <xsl:sequence select="         for $s in $sequence         return           if ($s = $search) then             ($replace)           else             ($s)"/>
  </xsl:function><xsl:function as="xs:numeric?" name="ica_fn:number-within-limits">
    <xsl:param as="xs:numeric?" name="min"/>
    <xsl:param as="xs:numeric?" name="max"/>
    <xsl:param as="xs:numeric?" name="value"/>
    <xsl:sequence select="if ($min gt $value) then $min       else if ($max lt $value) then $max       else $value"/>
  </xsl:function><xsl:function as="xs:integer" name="ica_fn:sp-iterations">
    <xsl:param as="xs:string?" name="text"/>
    <xsl:param as="xs:numeric?" name="instanceCapacity"/>
    <xsl:param as="xs:numeric?" name="min"/>
    <xsl:param as="xs:numeric?" name="max"/>
    <xsl:param as="xs:numeric" name="offset"/>    
        
    <xsl:sequence select="(       if ($instanceCapacity eq xs:double('INF')) then         ica_fn:number-within-limits($min, $max,         if (string-length($text) gt $offset) then 1 else 0)       else if (exists($offset)) then         ica_fn:number-within-limits($min, $max,          xs:integer(ceiling((string-length($text) - $offset) div $instanceCapacity)))       else         ica_fn:number-within-limits($min, $max,          xs:integer(ceiling((string-length($text) div $instanceCapacity))))                , 0)[1]"/>
  </xsl:function><xsl:function as="xs:string" name="ica_fn:sp-src-value">
    <xsl:param as="xs:string?" name="text"/>
    <xsl:param as="xs:numeric?" name="maxLength"/>
    <xsl:param as="xs:numeric?" name="offset"/>    
    
    <xsl:variable name="effectiveOffset" select="($offset, 0)[1]"/>
    <xsl:sequence select="       if (exists($maxLength)) then substring($text, 1 + $effectiveOffset, $maxLength)       else substring($text, 1 + $effectiveOffset)"/>

  </xsl:function><!--
        Internal Function Library
      --><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(p)" name="ica_fn:parse-from-picture">
    <xsl:param as="xs:string" name="picture-string"/>
    <xsl:variable name="tokens" select="ica_fn:picture-string-tokenizer($picture-string)"/>
    <xsl:variable name="hierarchy" select="ica_fn:picture-string-hierarchy($tokens)/*"/>
    <xsl:variable name="translated" select="$hierarchy/ica_fn:picture-string-toktranslate(.)"/>
    
    <xsl:if test="$translated/descendant-or-self::t">
      <xsl:sequence select="error:throw(concat('Unexpected character ', $translated//t[1]/@value), $error:invalid-ps)"/>
    </xsl:if>

    <p org="{$picture-string}">
      <xsl:copy-of select="$translated"/>
    </p>

  </xsl:function><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="error:invalid-ps" select="'Invalid picture string:'"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="error:format-ifr" select="'A datetime cannot be converted into the target format:'"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="error:invalid-ifr" select="'The input data does not represent a valid date/time for the Source DateTime Format:'"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="error:impl" select="'Implementation error:'"/><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="node()*" name="ica_fn:node-seq-to-siblings">
    <xsl:param as="node()*" name="nodes"/>
    <xsl:variable as="document-node()" name="doc">
      <xsl:document>
        <xsl:for-each select="$nodes">
          <xsl:copy-of select="."/>
        </xsl:for-each>
      </xsl:document>
    </xsl:variable>
    <xsl:sequence select="$doc/node()"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="document-node()" name="ica_fn:picture-string-hierarchy">
    <xsl:param as="element(t)*" name="tokens"/>
    
    <xsl:variable name="tokens" select="ica_fn:node-seq-to-siblings($tokens)"/>
    
    <xsl:variable name="first-alt-start" select="$tokens[@type = 'alt'][@value = ('(', '[')][1]"/>

    <xsl:document>
      <xsl:choose>
        <xsl:when test="$first-alt-start">


          <xsl:variable name="leadings" select="$tokens[. &lt;&lt; $first-alt-start]"/>

          <xsl:variable name="tokens" select="ica_fn:node-seq-to-siblings($tokens except $leadings)"/>
          <xsl:variable name="first-tok" select="$tokens[1]"/>


          <xsl:variable name="start-val" select="$first-tok/@value"/>
          <xsl:variable name="end-val" select="translate($start-val, '([', ')]')"/>
          <xsl:variable name="alts" select="$tokens[@type = 'alt']"/>


          <xsl:variable name="start-ends" select="$alts[@value = ($start-val, $end-val)] except $first-tok"/>


          <!--        
          first end which has as much as preceding starts as preceding ends 
          -->
          <xsl:variable name="end" select="$start-ends[@value = $end-val][count(preceding::t[@value = $start-val]) = count(preceding::t[@value = $end-val]) + 1][1]"/>
          
          
          <xsl:if test="count($end) eq 0">
            <xsl:sequence select="error:throw(concat('A bracket ', $start-val, ' was not closed!'), $error:invalid-ps)"/>
          </xsl:if>

          <xsl:variable name="ingroup" select="$end/preceding::t intersect $first-tok/following::t"/>
          <xsl:variable name="rest" select="$end/following::t"/>

          <xsl:copy-of select="$leadings"/>

          <xsl:variable name="prop-block" select="               $ingroup[@type = 'prop-def']               [not(preceding-sibling::t[@type != 'prop-def'] intersect $ingroup)]               "/>

          <xsl:variable name="ingroup" select="$ingroup except $prop-block"/>

          <xsl:variable name="pb-org" select="               if ($prop-block) then                 (concat('{', string-join($prop-block/@org, ','), '}'))               else                 ''               "/>

          <xsl:variable name="org" select="string-join(($first-tok/@org, $pb-org, $ingroup/@org, $end/@org), '')"/>



          <a org="{$org}">
            <xsl:variable name="branches" select="ica_fn:picture-string-hierarchy($ingroup)/*"/>

            <xsl:sequence select="$prop-block"/>

            <xsl:for-each-group group-starting-with="t[@type = 'alt'][@value = '|']" select="$branches">

              <xsl:variable name="firstPipe" select="self::t[@type = 'alt'][@value = '|']"/>

              <xsl:if test="position() = 1 and ($firstPipe or $start-val = '[')">
                <p org=""/>
              </xsl:if>

              <xsl:variable name="content" select="current-group() except $firstPipe"/>
              <p org="{string-join($content/@org, '')}">
                <xsl:copy-of select="$content"/>
              </p>

            </xsl:for-each-group>
          </a>

          <xsl:copy-of select="ica_fn:picture-string-hierarchy($rest)"/>

        </xsl:when>
        <xsl:otherwise>
          <xsl:copy-of select="$tokens"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:document>

  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element()*" name="ica_fn:picture-string-toktranslate">
    <xsl:param as="element()" name="token"/>

    <xsl:apply-templates mode="ica_fn:ps-toktranslate" select="$token"/>
  </xsl:function><xsl:template xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" match="a[t[@type = 'prop-def']]" mode="ica_fn:ps-toktranslate">
    <xsl:variable name="props" select="t[@type = 'prop-def']"/>
    <xsl:variable name="fieldProp" select="$props[tokenize(@value, ':')[1] = 'field']"/>
    
    <xsl:if test="count($fieldProp) ne 1">
      <xsl:sequence select="         error:throw('There should be exactly one field property for field matcher', $error:invalid-ps)       "/>
    </xsl:if>
    
    <xsl:variable name="prop-names" select="$props/tokenize(@value, ':')[1]"/>
    <xsl:variable name="unknownProps" select="$prop-names[not(. = ('field', 'exponent'))]"/>
    
    <xsl:if test="count($unknownProps) gt 0">
      <xsl:sequence select="         error:throw(concat('Unknown property ', $unknownProps[1], ' for field matcher.'), $error:invalid-ps)         "/>
    </xsl:if>
    
    <xsl:variable name="noValProp" select="$props[not(contains(@value, ':'))]"/>
    <xsl:if test="count($noValProp) gt 0">
      <xsl:sequence select="         error:throw(concat('No value for field matcher property ', $noValProp[1]), $error:invalid-ps)         "/>
    </xsl:if>
    
    <xsl:variable name="field" select="       $fieldProp         /@value/tokenize(., ':')[2]"/>
    <xsl:variable name="exp" select="$props[tokenize(@value, ':')[1] = 'exponent']"/>
    <f name="{$field}">
      <xsl:sequence select="@org"/>
      <xsl:attribute name="char-supply" select="p/t[@type = 'quoted']/@value" separator=""/>
      <xsl:if test="$exp">
        <xsl:attribute name="exponent" select="$exp/@value/tokenize(., ':')[2]"/>
      </xsl:if>
    </f>
  </xsl:template><xsl:template xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" match="t[@type = 'quoted']" mode="ica_fn:ps-toktranslate">
    <l>
      <xsl:sequence select="@org"/>
      <xsl:attribute name="char-sequence" select="@value"/>
    </l>
  </xsl:template><xsl:template xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" match="t[@type = 'short'][@value = $ica_fn:short-dict/@value]" mode="ica_fn:ps-toktranslate">
    <xsl:variable name="val" select="@value"/>
    <xsl:copy-of select="$ica_fn:short-dict[@value = $val][1]/*"/>
  </xsl:template><xsl:template xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" match="t[@type = 'short'][@value = ('f*', 'f')]" mode="ica_fn:ps-toktranslate" priority="10">

    <xsl:variable name="val" select="@value"/>
    <xsl:variable name="repl" select="$ica_fn:short-dict[@value = $val][1]/*"/>

    <xsl:variable name="prec_fm" select="ica_fn:prec-field-matcher(.)"/>
    <xsl:variable as="element(f)*" name="prec_fm_translated">
      <xsl:apply-templates mode="ica_fn:ps-toktranslate" select="$prec_fm"/>
    </xsl:variable>
    <xsl:variable name="last_prec_fm" select="$prec_fm_translated[last()]"/>

    <xsl:variable name="exponent" select="         if ($last_prec_fm/@name = 'fractional-second') then           ($last_prec_fm/@exponent - 1)         else           (-1)"/>

    <xsl:for-each select="$repl">
      <xsl:copy>
        <xsl:sequence select="@*"/>
        <xsl:attribute name="exponent" select="$exponent"/>
        <xsl:sequence select="node()"/>
      </xsl:copy>

    </xsl:for-each>

  </xsl:template><xsl:template xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" match="t[@type = 'short'][@value = ('hh', 'mm')]" mode="ica_fn:ps-toktranslate" priority="10">
    <xsl:variable name="val" select="@value"/>
    <xsl:variable name="repl" select="$ica_fn:short-dict[@value = $val][1]/*"/>
    <xsl:variable name="repl-alt" select="$ica_fn:short-dict[@value = $val][2]/*"/>

    <xsl:variable name="prec_fm" select="ica_fn:prec-field-matcher(.)"/>

    <xsl:variable as="element(f)*" name="prec_fm_translated">
      <xsl:apply-templates mode="ica_fn:ps-toktranslate" select="$prec_fm"/>
    </xsl:variable>

    <xsl:variable name="prec-alt-field" select="         if ($val = 'hh') then           ('offset-sign')         else           ('offset-sign', 'offset-hour')"/>


    <xsl:copy-of select="         if ($prec_fm_translated[last()][@name = $prec-alt-field]) then           ($repl-alt)         else           ($repl)         "/>

  </xsl:template><xsl:template xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" match="node() | @*" mode="ica_fn:ps-toktranslate">
    <xsl:copy>
      <xsl:apply-templates mode="#current" select="@*"/>
      <xsl:apply-templates mode="#current" select="node()"/>
    </xsl:copy>
  </xsl:template><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:boolean" name="ica_fn:is-field-matcher">
    <xsl:param as="element()" name="t"/>

    <xsl:sequence select="$t/@type = 'short' or $t/self::a/*[1][@type = 'prop-def']"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element()?" name="ica_fn:prec-field-matcher">
    <xsl:param as="element()" name="fm"/>

    <xsl:variable name="precsibl" select="$fm/preceding-sibling::*[ica_fn:is-field-matcher(.) or .//*[ica_fn:is-field-matcher(.)]][1]"/>
    <xsl:sequence select="         if ($precsibl[ica_fn:is-field-matcher(.)]) then           ($precsibl)         else           if ($precsibl) then             $precsibl//*[ica_fn:is-field-matcher(.)][last()]           else             $fm/parent::p/parent::a/ica_fn:prec-field-matcher(.)         "/>

  </xsl:function><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(t)+" name="ica_fn:short-dict">
    <t value="CC">
      <f char-supply="0123456789" exponent="3" name="year" org="C"/>
      <f char-supply="0123456789" exponent="2" name="year" org="C"/>
    </t>
    <t value="YY">
      <f char-supply="0123456789" exponent="1" name="year" org="Y"/>
      <f char-supply="0123456789" exponent="0" name="year" org="Y"/>
    </t>
    <t value="MM">
      <f char-supply="01" exponent="1" name="month" org="M"/>
      <f char-supply="0123456789" exponent="0" name="month" org="M"/>
    </t>
    <t value="DD">
      <f char-supply="0123" exponent="1" name="day" org="D"/>
      <f char-supply="0123456789" exponent="0" name="day" org="D"/>
    </t>
    <t value="hh">
      <f char-supply="012" exponent="1" name="hour" org="h"/>
      <f char-supply="0123456789" exponent="0" name="hour" org="h"/>
    </t>
    <t value="mm">
      <f char-supply="012345" exponent="1" name="minute" org="m"/>
      <f char-supply="0123456789" exponent="0" name="minute" org="m"/>
    </t>
    <t value="ss">
      <f char-supply="0123456" exponent="1" name="second" org="s"/>
      <f char-supply="0123456789" exponent="0" name="second" org="s"/>
    </t>
    <t value="f">
      <f char-supply="0123456789" exponent="#" name="fractional-second" org="f"/>
    </t>
    <t value="f*">
      <f char-supply="0123456789" exponent="#" exponent-min="-INF" name="fractional-second" org="f*"/>
    </t>
    <t value="Z">
      <f char-supply="Z" name="offset-z" org="Z"/>
    </t>
    <t value="(+|-)">
      <f char-supply="+-" name="offset-sign" org="(+|-)"/>
    </t>
    <t value="hh">
      <f char-supply="01" exponent="1" name="offset-hour" org="h"/>
      <f char-supply="0123456789" exponent="0" name="offset-hour" org="h"/>
    </t>
    <t value="mm">
      <f char-supply="012345" exponent="1" name="offset-minute" org="m"/>
      <f char-supply="0123456789" exponent="0" name="offset-minute" org="m"/>
    </t>
  </xsl:variable><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(t)*" name="ica_fn:picture-string-tokenizer">
    <xsl:param as="xs:string" name="picture-string"/>

    <!--    
    handle quotes
    -->
    <xsl:variable name="shortsRegex" select="'CC|YY|MM|DD|hh|mm|ss|f\*?|Z|\(\+\|-\)'"/>
    <xsl:choose>
      <xsl:when test="$picture-string = ''"/>
      <xsl:when test="contains($picture-string, '''')">
        <xsl:variable name="before" select="substring-before($picture-string, '''')"/>
        <xsl:variable name="after" select="substring-after($picture-string, '''')"/>
        
        
        <xsl:variable name="quoted" select="ica_fn:parse-quotes($after)"/>
        <xsl:variable name="tail" select="substring-after($after, $quoted)"/>
        
        
        <xsl:variable name="quoted" select="replace($quoted, '''$', '')"/>
        
        <xsl:if test="$quoted = ''">
          <xsl:sequence select="error:throw('An empty literal matcher is not allowed!', $error:invalid-ps)"/>
        </xsl:if>
        <xsl:if test="contains(replace($quoted, '\\\\|\\''', ''), '\')">
          <xsl:sequence select="             error:throw(             'Only '' or \ are allowed to be escaped by \',             $error:invalid-ps)             "/></xsl:if>


        <xsl:sequence select="ica_fn:picture-string-tokenizer(string($before))"/>
        <t org="'{$quoted}'" type="quoted" value="{replace($quoted, '\\(.)', '$1')}"/>
        <xsl:sequence select="ica_fn:picture-string-tokenizer(string($tail))"/>

      </xsl:when>
      <xsl:when test="contains($picture-string, '{')">
        <xsl:variable name="before" select="substring-before($picture-string, '{')"/>
        <xsl:variable name="after" select="substring-after($picture-string, '{')"/>

        <xsl:if test="not(contains($after, '}'))">
          <xsl:sequence select="             error:throw('A property block was not closed!',             $error:invalid-ps)             "/>
        </xsl:if>

        <xsl:variable name="property-block" select="substring-before($after, '}')"/>
        <xsl:variable name="after" select="substring-after($after, '}')"/>

        <xsl:sequence select="ica_fn:picture-string-tokenizer(string($before))"/>

        <xsl:for-each select="tokenize($property-block, ',')">
          <t org="{.}" type="prop-def" value="{.}"/>
        </xsl:for-each>

        <xsl:sequence select="ica_fn:picture-string-tokenizer($after)"/>

      </xsl:when>
      <xsl:when test="matches($picture-string, $shortsRegex)">
        <xsl:analyze-string regex="{$shortsRegex}" select="$picture-string">
          <xsl:matching-substring>
            <t org="{.}" type="short" value="{.}"/>
          </xsl:matching-substring>
          <xsl:non-matching-substring>
            <xsl:sequence select="ica_fn:picture-string-tokenizer(.)"/>
          </xsl:non-matching-substring>
        </xsl:analyze-string>
      </xsl:when>
      <xsl:otherwise>

        <xsl:variable name="alt-chars" select="'[', ']', '|', '(', ')'"/>

        <xsl:analyze-string regex="." select="$picture-string">
          <xsl:matching-substring>
            <xsl:variable name="c" select="."/>
            <xsl:variable as="element(t)" name="token">
              <t>
                <xsl:attribute name="type" select="                     if ($alt-chars = $c) then                       ('alt')                     else                       ('#UNKNOWN')"/>
                <xsl:attribute name="value" select="$c"/>
                <xsl:attribute name="org" select="$c"/>
              </t>
            </xsl:variable>
            
            <xsl:if test="$token/@type = '#UNKNOWN'">
              <xsl:sequence select="error:throw(concat('Unknown character ', $c), $error:invalid-ps)"/>
            </xsl:if>
            <xsl:sequence select="$token"/>

          </xsl:matching-substring>
        </xsl:analyze-string>
      </xsl:otherwise>
    </xsl:choose>

  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:string" name="ica_fn:parse-quotes">
    <xsl:param as="xs:string" name="picture-string"/>
    <xsl:variable name="length" select="         if (starts-with($picture-string, '\')) then           (2)         else           (1)"/>
    <xsl:variable name="c" select="substring($picture-string, 1, $length)"/>
    <xsl:variable name="rest" select="substring($picture-string, $length + 1)"/>
    
    <xsl:if test="$rest = '' and $c != ''''">
      <xsl:sequence select="error:throw('A literal matcher was never closed.', $error:invalid-ps)"/>
    </xsl:if>
    
    <xsl:variable name="rest" select="         if ($c != '''') then           ica_fn:parse-quotes($rest)         else           ('')"/>
    
    
    <xsl:sequence select="concat($c, $rest)"/>

  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="error:throw">
    <xsl:param as="xs:string" name="message"/>
    <xsl:param as="xs:string" name="context"/>
    <xsl:sequence select="error:throw($message, $context, ())"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="error:throw">
    <xsl:param as="xs:string" name="message"/>
    <xsl:param as="xs:string" name="context"/>
    <xsl:param as="xs:string?" name="code"/>
    
    <xsl:variable name="code" select=" if ($code) then concat('[', $code, ']') else ('')"/>
    
    <xsl:message select="string-join(($code, $context, $message), ' ')" terminate="yes"/>
    
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:string" name="ica_fn:format-ifr">
    <xsl:param as="element(p)" name="ipr"/>
    <xsl:param as="element(ifr)" name="ifr"/>
    <xsl:variable as="xs:string*" name="formatted">
      <xsl:apply-templates mode="ica_fn:format-ifr" select="$ipr">
        <xsl:with-param name="ifr" select="ica_fn:get-effective-ifr($ifr)" tunnel="yes"/>
      </xsl:apply-templates>
    </xsl:variable>
    <xsl:sequence select="string-join($formatted, '')"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(ifr)" name="ica_fn:get-effective-ifr">
    <xsl:param as="element(ifr)" name="ifr"/>
    <xsl:variable name="fields" select="$ifr/f"/>
    <xsl:variable name="fields" select="($fields, $ica_fn:default-ifr-naiv/f[not(@name = $fields/@name)])"/>

    <xsl:variable name="hasOffsets" select="$fields[starts-with(@name, 'offset-')]"/>
    <xsl:variable name="fields" select="       if ($hasOffsets) then           ($fields, $ica_fn:default-offset/f[not(@name = $fields/@name)])         else           $fields         "/>

    <xsl:variable name="fields" select="         if (         $hasOffsets and         not($fields[@name = ('offset-hour', 'offset-minute')]/@value/number(.) != 0)         ) then           ($fields, $ica_fn:default-z/f[not(@name = $fields/@name)])         else           ($fields)         "/>

    <ifr>
      <xsl:sequence select="$fields"/>
    </ifr>

  </xsl:function><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(ifr)" name="ica_fn:default-ifr-naiv">
    <ifr>
      <f exponent-max="-1" exponent-min="0" name="year" value="9996"/>
      <f exponent-max="-1" exponent-min="0" name="month" value="1"/>
      <f exponent-max="-1" exponent-min="0" name="day" value="1"/>
      <f exponent-max="-1" exponent-min="0" name="hour" value="0"/>
      <f exponent-max="-1" exponent-min="0" name="minute" value="0"/>
      <f exponent-max="-1" exponent-min="0" name="second" value="0"/>
      <f exponent-max="-1" exponent-min="0" name="fractional-second" value="0"/>
    </ifr>
  </xsl:variable><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(ifr)" name="ica_fn:default-offset">
    <ifr>
      <f default="true" name="offset-sign" value="+"/>
      <f exponent-max="-1" exponent-min="0" name="offset-hour" value="0"/>
      <f exponent-max="-1" exponent-min="0" name="offset-minute" value="0"/>
    </ifr>
  </xsl:variable><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(ifr)" name="ica_fn:default-z">
    <ifr>
      <f default="true" name="offset-z" value="Z"/>
    </ifr>
  </xsl:variable><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="attribute(value)?" name="ica_fn:get-ifr-field">
    <xsl:param as="element(ifr)" name="ifr"/>
    <xsl:param as="xs:string" name="name"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:integer" name="ica_fn:get-digit">
    <xsl:param as="xs:decimal" name="value"/>
    <xsl:param as="xs:integer" name="exp"/>

    <xsl:sequence select="xs:integer(floor($value * ica_fn:pow($exp * -1)) mod 10)"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:decimal" name="ica_fn:pow">
    <xsl:param as="xs:integer" name="exp"/>
    <xsl:sequence select="ica_fn:pow($exp, 10)"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:decimal" name="ica_fn:pow">
    <xsl:param as="xs:integer" name="exp"/>
    <xsl:param as="xs:integer" name="base"/>
    <xsl:sequence select="         if      ($exp gt 0) then $base * ica_fn:pow($exp - 1, $base)         else if ($exp lt 0) then 1 div ica_fn:pow(abs($exp), $base)         else                     1         "/>

  </xsl:function><xsl:param xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:date" name="ica_fn:currentDate" select="current-date()"/><xsl:template xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" match="f" mode="ica_fn:format-ifr">
    <xsl:param as="element(ifr)" name="ifr" required="yes" tunnel="yes"/>
    <xsl:variable name="name" select="@name"/>
    <xsl:variable name="field" select="$ifr/f[@name = $name]"/>
    
    
    <xsl:if test="count($field) ne 1">
      <xsl:variable name="xsdatetime" select="ica_fn:ifr-to-datetime($ifr)"/>
      <xsl:variable name="isoffset" select="$name = ($ica_fn:default-offset, $ica_fn:default-z)/f/@name"/>
      
      <xsl:variable name="reason" select=" if ($isoffset) then ('as no offset information is available.') else ()"/>
      
      <xsl:sequence select="error:throw(         string-join(('The field', $name, 'is not formattable for the datetime', $xsdatetime, $reason), ' '),          $error:format-ifr)         "/>
    </xsl:if>
    
    <xsl:sequence select="$field/@value/string(.)"/>
  </xsl:template><xsl:template xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" match="f[@exponent]" mode="ica_fn:format-ifr">
    <xsl:param as="element(ifr)" name="ifr" required="yes" tunnel="yes"/>
    <xsl:variable name="name" select="@name"/>
    <xsl:variable name="field" select="$ifr/f[@name = $name]"/>
    
    <xsl:if test="count($field) ne 1">
      <xsl:variable name="xsdatetime" select="ica_fn:ifr-to-datetime($ifr)"/>
      <xsl:sequence select="error:throw(         concat('The field ', $name, ' is not formattable for the datetime ', $xsdatetime),          $error:format-ifr)         "/>
    </xsl:if>
    
    <xsl:variable name="value" select="$field/@value/xs:decimal(.)"/>

    <xsl:sequence select="         string(ica_fn:get-digit($value, @exponent/xs:integer(.)))         "/>
  </xsl:template><xsl:template xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" match="f[@name = 'fractional-second'][@exponent-min = '-INF']" mode="ica_fn:format-ifr" priority="10">
    <xsl:param as="element(ifr)" name="ifr" required="yes" tunnel="yes"/>
    <xsl:variable name="name" select="@name"/>
    <xsl:variable name="ifr-field" select="$ifr/f[@name = $name]"/>
    <xsl:variable name="ifr-exp-min" select="$ifr-field/@exponent-min/xs:integer(.)"/>
    <xsl:variable name="exp" select="@exponent/xs:integer(.)"/>

    <xsl:for-each select="reverse($ifr-exp-min to $exp)">
      <xsl:variable as="element(f)" name="new-ipr">
        <f char-supply="0123456789" exponent="{.}" name="fractional-second" org="f*"/>
      </xsl:variable>
      <xsl:apply-templates mode="#current" select="$new-ipr"/>
    </xsl:for-each>

  </xsl:template><xsl:template xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" match="l" mode="ica_fn:format-ifr">
    <xsl:value-of select="@char-sequence"/>
  </xsl:template><xsl:template xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" match="a" mode="ica_fn:format-ifr">
    <xsl:param as="element(ifr)" name="ifr" required="yes" tunnel="yes"/>

    <xsl:variable name="formatables" select="p[ica_fn:is-formatable($ifr, .)]"/>
    
    <xsl:if test="count($formatables) eq 0">
      <xsl:variable name="xsdatetime" select="ica_fn:ifr-to-datetime($ifr)"/>
      <xsl:sequence select="error:throw(         concat('The alternative ', @org, ' can not be formatted for the date/time ', $xsdatetime),          $error:format-ifr)"/>
    </xsl:if>


    <xsl:variable as="element(p)*" name="formatables-ordered">
      <xsl:for-each select="$formatables">
        <xsl:sort data-type="number" order="descending" select="ica_fn:format-rank($ifr, .)"/>
        <xsl:sequence select="."/>
      </xsl:for-each>
    </xsl:variable>
    <xsl:apply-templates mode="#current" select="$formatables-ordered[1]"/>
  </xsl:template><xsl:template xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" match="text()" mode="ica_fn:format-ifr"/><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:boolean" name="ica_fn:is-formatable">
    <xsl:param as="element(ifr)" name="ifr"/>
    <xsl:param as="element()" name="ipr-el"/>
    
    <xsl:variable name="ifr" select="ica_fn:get-effective-ifr($ifr)"/>

    <xsl:variable name="name" select="$ipr-el/local-name()"/>

    <xsl:choose>
      <xsl:when test="$name = 'p' and not($ipr-el/node())">
        <xsl:sequence select="true()"/>
      </xsl:when>
      <xsl:when test="$name = 'p'">
        <xsl:sequence select="             every $c in $ipr-el/*               satisfies ica_fn:is-formatable($ifr, $c)"/>
      </xsl:when>
      <xsl:when test="$name = 'a'">
        <xsl:sequence select="             some $p in $ipr-el/p               satisfies ica_fn:is-formatable($ifr, $p)"/>
      </xsl:when>
      <xsl:when test="$name = 'f'">
        <xsl:variable name="fieldName" select="$ipr-el/@name"/>
        <xsl:sequence select="exists($ifr/f[@name = $fieldName])"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:sequence select="true()"/>
      </xsl:otherwise>
    </xsl:choose>

  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:integer" name="ica_fn:format-rank">
    <xsl:param as="element(ifr)" name="ifr"/>
    <xsl:param as="element()" name="el"/>

    <xsl:variable name="fieldName" select="$el/self::f/@name"/>
    <xsl:variable name="ifrField" select="$ifr/f[@name = $fieldName]"/>
    <xsl:variable name="exp" select="$el/self::f/@exponent/xs:integer(.)"/>

    <xsl:variable name="rank" select="         if ($fieldName = 'offset-z') then           (2)         else           if ($fieldName = ('offset-hour', 'offset-minute', 'offset-sign')) then             (1)           else             (0)         "/>

    <xsl:sequence select="         if ($el/self::p)         then           max(($el/*/ica_fn:format-rank($ifr, .), -1))         else           if ($el/self::a) then             max($el/p[ica_fn:is-formatable($ifr, .)]/ica_fn:format-rank($ifr, .))           else             if ($el/self::l) then               -1             else               if ($ifrField/@default = 'true') then                 -1               else                 if ($exp &lt; $ifrField/@exponent-min or $exp &gt; $ifrField/@exponent-max) then                   -1                 else                   $rank         "/>

  </xsl:function><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="ica_fn:xsdDateTimePicture">CCYY'-'MM'-'DD'T'hh':'mm':'ss['.'f*][(Z|(+|-)hh':'mm)]</xsl:variable><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="ica_fn:xsdDatePicture">CCYY'-'MM'-'DD[(Z|(+|-)hh':'mm)]</xsl:variable><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="ica_fn:xsdTimePicture">hh':'mm':'ss['.'f*][(Z|(+|-)hh':'mm)]</xsl:variable><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="ica_fn:xsdDateTimeIPR" select="ica_fn:parse-from-picture($ica_fn:xsdDateTimePicture)"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="ica_fn:xsdDateIPR" select="ica_fn:parse-from-picture($ica_fn:xsdDatePicture)"/><xsl:variable xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="ica_fn:xsdTimeIPR" select="ica_fn:parse-from-picture($ica_fn:xsdTimePicture)"/><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:dateTime" name="ica_fn:ifr-to-datetime">
    <xsl:param as="element(ifr)" name="ifr"/>

    <xsl:sequence select="xs:dateTime(ica_fn:format-ifr($ica_fn:xsdDateTimeIPR, $ifr))"/>

  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:time" name="ica_fn:ifr-to-time">
    <xsl:param as="element(ifr)" name="ifr"/>

    <xsl:sequence select="xs:time(ica_fn:format-ifr($ica_fn:xsdTimeIPR, $ifr))"/>

  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:date" name="ica_fn:ifr-to-date">
    <xsl:param as="element(ifr)" name="ifr"/>

    <xsl:sequence select="xs:date(ica_fn:format-ifr($ica_fn:xsdDateIPR, $ifr))"/>

  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(ifr)" name="ica_fn:datetime-to-ifr">
    <xsl:param as="xs:dateTime" name="dateTime"/>

    <xsl:variable name="ifr_raw" select="ica_fn:parse-ifr(string($dateTime), $ica_fn:xsdDateTimeIPR)"/>

    <xsl:sequence select="ica_fn:canonicalize-ifr($ifr_raw)"/>

  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(ifr)" name="ica_fn:date-to-ifr">
    <xsl:param as="xs:date" name="date"/>

    <xsl:variable name="ifr_raw" select="ica_fn:parse-ifr(string($date), $ica_fn:xsdDateIPR)"/>

    <xsl:sequence select="ica_fn:canonicalize-ifr($ifr_raw)"/>

  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(ifr)" name="ica_fn:time-to-ifr">
    <xsl:param as="xs:time" name="time"/>

    <xsl:variable name="ifr_raw" select="ica_fn:parse-ifr(string($time), $ica_fn:xsdTimeIPR)"/>

    <xsl:sequence select="ica_fn:canonicalize-ifr($ifr_raw)"/>

  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element()*" name="ica_fn:finalize-fields">
    <xsl:param as="element(f)*" name="fields"/>
    <!-- TODO replace this shortcut to find the names of numeric fields with something more official -->
    <xsl:variable name="numeric-fields" select="distinct-values($ica_fn:short-dict//f[@exponent]/@name)"/>
    <xsl:variable name="decimal-fields" select="'fractional-second'"/>
    <xsl:variable name="integer-fields" select="$numeric-fields[not(. = $decimal-fields)]"/>
    
    <xsl:copy-of select="$fields[not(self::f)] | $fields[self::f][not(@name = $numeric-fields)]"/>
    <xsl:for-each select="$integer-fields">
      <xsl:variable name="name" select="."/>
      <xsl:if test="$fields[self::f][@name = $name]">
        <xsl:variable name="co-conspirators" select="$fields[self::f][@name = $name]"/>
        <f exponent-max="{max($co-conspirators/xs:integer(@exponent))}" exponent-min="{min($co-conspirators/xs:integer(@exponent))}" name="{.}" value="{sum($co-conspirators/xs:integer(@value))}"/>
      </xsl:if>
    </xsl:for-each>
    <xsl:for-each select="$decimal-fields">
      <xsl:variable name="name" select="."/>
      <xsl:if test="$fields[self::f][@name = $name]">
      <xsl:variable name="co-conspirators" select="$fields[self::f][@name = $name]"/>
        <f exponent-max="{max($co-conspirators/xs:integer(@exponent))}" exponent-min="{min(($co-conspirators/xs:integer(@exponent), $co-conspirators/xs:integer(@exponent-min)))}" name="{.}" value="{sum($co-conspirators/xs:decimal(@value))}"/>
      </xsl:if>
    </xsl:for-each>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(ifr)" name="ica_fn:ensure-valid-ifr">
    <xsl:param as="element(ifr)" name="ifr"/>
    <xsl:variable name="errormsg" select="ica_fn:validate-ifr($ifr)"/>
    <xsl:if test="exists($errormsg)">
      <xsl:sequence select="error:throw($errormsg, $error:invalid-ifr)"/>
    </xsl:if>
    <xsl:sequence select="$ifr"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:string?" name="ica_fn:validate-ifr">
    <xsl:param as="element(ifr)" name="ifr"/>
    
    <xsl:variable name="format_as_dateTime" select="ica_fn:format-ifr($ica_fn:xsdDateTimeIPR, $ifr)"/>
    <xsl:variable name="castable_as_dateTime" select="$format_as_dateTime castable as xs:dateTime"/>
    <xsl:sequence select="       if ($castable_as_dateTime) then () else concat('The input data would represent in the Source DateTime Format the xs:dateTime ', $format_as_dateTime, ' which is not valid.')"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="ica_fn:validate-ipr">
    <xsl:param as="element(p)" name="ifr"/>
    <!-- TODO implement -->
    <xsl:sequence select="true()"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(ifr)" name="ica_fn:parse-ifr">
    <xsl:param as="xs:string" name="value"/>
    <xsl:param as="element(p)" name="ipr"/>
    <xsl:variable name="parse-result" select="ica_fn:parse-ifr-intern($value, $ipr, true())"/>
    <xsl:if test="$parse-result/@fail">
      <xsl:sequence select="error:throw($parse-result/@fail, concat(         'The input data (', ica_pr:quoted($value), ') ',         'does not conform to the source date/time format (', ica_pr:quoted($ipr/@org), '):'       ))"/>
    </xsl:if>
    <ifr>
      <xsl:sequence select="ica_fn:finalize-fields($parse-result/*)"/>
    </ifr>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(parse-result)" name="ica_fn:parse-ifr-intern">
    <xsl:param as="xs:string" name="value"/>
    <xsl:param as="element()*" name="ipr"/>
    <xsl:param as="xs:boolean" name="top-level"/>
    <xsl:choose>
      <xsl:when test="empty($ipr)">
        <parse-result consumed="0">
          <xsl:if test="$value != '' and $top-level">
            <xsl:attribute name="fail" select="concat('Not consumed by picture string: ', ica_pr:quoted($value))"/>/&gt;
          </xsl:if>
        </parse-result>
      </xsl:when>
      <xsl:otherwise>
        <!-- sequence of pps's, parse in order -->
        <xsl:variable name="first-pattern" select="ica_fn:parse-pps-intern($value, $ipr[1])"/>
        <xsl:variable name="next-patterns" select="ica_fn:parse-ifr-intern(           substring($value, $first-pattern/@consumed + 1),            $ipr[position() gt 1],           $top-level)"/>
        <xsl:variable as="xs:integer" name="consumed" select="sum(($first-pattern, $next-patterns)/xs:integer(@consumed))"/>

        <parse-result consumed="{ $consumed }">
          <xsl:if test="($first-pattern, $next-patterns)/@fail">
            <xsl:attribute name="fail" select="(($first-pattern, $next-patterns)/@fail)[1]"/>
          </xsl:if>
          <xsl:sequence select="$first-pattern/*"/>
          <xsl:sequence select="$next-patterns/*"/>
        </parse-result>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="ica_fn:parse-pps-intern">
    <xsl:param as="xs:string" name="value"/>
    <xsl:param as="element()" name="ipr"/>

    <xsl:choose>
      <xsl:when test="$ipr/self::p">
        <xsl:sequence select="ica_fn:parse-ifr-intern($value, $ipr/*, false())"/>
      </xsl:when>
      
      <xsl:when test="$ipr/self::l">
        <xsl:variable name="length" select="string-length($ipr/@char-sequence)"/>
        <parse-result consumed="{min((string-length($ipr//@char-sequence), string-length($value)))}">
          <xsl:if test="substring($value, 1, $length) ne $ipr/@char-sequence">
            <xsl:attribute name="fail" select="concat(               'Expected ', ica_pr:quoted($ipr/@char-sequence),               ', got ' , ica_pr:quoted(substring($value, 1, $length)))"/>
          </xsl:if>
        </parse-result>
      </xsl:when>

      <xsl:when test="$ipr/self::a">
        <xsl:variable name="selected" select="($ipr/*[contains(ica_pr:parse-indicator(.), substring($value, 1, 1))])[1]"/>
        <xsl:choose>
          <xsl:when test="exists($selected)">
            <xsl:sequence select="ica_fn:parse-ifr-intern($value, $selected, false())"/>
          </xsl:when>
          <xsl:when test="$ipr/*/ica_pr:parse-indicator(.) = ''">
            <parse-result consumed="0"/>
          </xsl:when>
          <xsl:otherwise>
            <parse-result consumed="0" fail="{concat(               'no alternative matched ', ica_pr:quoted($value)               )}"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      
      <xsl:when test="$ipr/self::f">
        <xsl:choose>

          <!-- non-numeric one-character fields -->
          <xsl:when test="$ipr/@name = ('offset-sign', 'offset-z')">
            <xsl:variable name="length" select="1"/>
            <xsl:variable name="field" select="substring($value, 1, $length)"/>
            <parse-result consumed="{ string-length($field) }">
              <xsl:choose>
                <xsl:when test="contains($ipr/@char-supply, $field) and string-length($field) = $length">
                  <f name="{$ipr/@name}" value="{$field}"/> 
                </xsl:when>
                <xsl:otherwise>
                  <xsl:attribute name="fail" select="concat(                     'Expected ', ica_pr:string-join(for $c in string-to-codepoints($ipr/@char-supply) return ica_pr:quoted(codepoints-to-string($c)), ', ', ' or '),                     ', got ', ica_pr:quoted($field)                     )"/>
                </xsl:otherwise>
              </xsl:choose>
            </parse-result>
          </xsl:when>

          <xsl:when test="$ipr/@name = 'fractional-second'">
            <xsl:variable as="xs:integer?" name="length" select="ica_pr:field-length($ipr)"/>
            <xsl:variable as="xs:string" name="field" select="               if (empty($length))                then replace($value, '[^0-9].*', '')               else substring($value, 1, $length)               "/>
            <parse-result consumed="{ ($length, string-length($field))[1] }">
              <xsl:choose>
                <xsl:when test="$field = '' and not (0 != $length)"/>
                <xsl:when test="matches($field, '^[0-9]*$') and not(string-length($field) != $length)">
                  <f exponent="{$ipr/@exponent}" name="{$ipr/@name}" value="{                     if ($field = '') then 0                     else xs:decimal($field) * ica_fn:pow(xs:integer($ipr/@exponent) - string-length($field) + 1)                     }">
                    <xsl:if test="empty($length)">
                      <xsl:attribute name="exponent-min" select="xs:integer($ipr/@exponent) - string-length($field) + 1"/>
                    </xsl:if>
                  </f>
                </xsl:when>
                <xsl:otherwise>
                  <!-- length is never empty here ! -->
                  <xsl:attribute name="fail" select="concat('Expected ', $length ,' digit(s), got ', ica_pr:quoted($field))"/>
                </xsl:otherwise>
              </xsl:choose>
            </parse-result>
          </xsl:when>

          <xsl:otherwise>
            <!-- default field handling: integer -->
            <xsl:variable name="length" select="1"/>
            <xsl:variable name="field" select="substring($value, 1, $length)"/>
            <parse-result consumed="{ $length }">
              <xsl:choose>
                <xsl:when test="matches($field, '^[0-9]*$') and string-length($field) = $length">
                  <f exponent="{$ipr/@exponent}" name="{$ipr/@name}" value="{ xs:integer($field) * ica_fn:pow(xs:integer($ipr/@exponent)) }"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:attribute name="fail" select="concat('Expected ', $length, ' digit(s), got ', ica_pr:quoted($field))"/>
                </xsl:otherwise>
              </xsl:choose>
            </parse-result>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <xsl:otherwise>
        <parse-result consumed="0" fail="Illegal pattern: {name($ipr)}"/>
      </xsl:otherwise>

    </xsl:choose>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="ica_pr:quoted">
    <xsl:param as="xs:string" name="value"/>
    <xsl:sequence select="concat('&#34;', replace($value, '&#34;', '&#34;&#34;'), '&#34;')"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="ica_pr:string-join">
    <xsl:param as="xs:string*" name="strings"/>
    <xsl:param as="xs:string" name="sep1"/>
    <xsl:param as="xs:string" name="sep2"/>
    <xsl:variable name="last-2" select="string-join($strings[position() ge count($strings) - 1], $sep2)"/>
    <xsl:sequence select="string-join(($strings[position() lt count($strings) - 1], $last-2), $sep1)"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(ifr)" name="ica_fn:canonicalize-ifr">
      <xsl:param as="element(ifr)" name="ifr_raw"/>
      <xsl:sequence select="ica_fn:canonicalize-ifr($ifr_raw, $ica_fn:currentDate)"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(ifr)" name="ica_fn:canonicalize-ifr">
    <xsl:param as="element(ifr)" name="ifr_raw"/>
    <xsl:param as="xs:date" name="refDate"/>
    
    <xsl:apply-templates mode="ica_fn:canonicalize-ifr" select="$ifr_raw">
      <xsl:with-param name="refDate" select="$refDate" tunnel="yes"/>
    </xsl:apply-templates>
    
  </xsl:function><xsl:template xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" match="f[@name = 'year'][@exponent-max/number(.) &lt; 2][@value &lt; 100]" mode="ica_fn:canonicalize-ifr">
    <xsl:param as="xs:date" name="refDate" tunnel="yes"/>
    <xsl:variable name="formated-ifr" select="ica_fn:format-ifr(ica_fn:parse-from-picture('YY''-''MM''-''DD'), parent::ifr)"/>
    
    <xsl:variable name="rYear" select="year-from-date($refDate)"/>
    <xsl:variable name="rCentury" select="$rYear idiv 100"/>
    <xsl:variable as="xs:date+" name="possible-dates" select="       for $c in $rCentury - 1 to $rCentury + 1       return         xs:date(concat($c, $formated-ifr))       "/>
    
    <xsl:variable as="xs:date" name="date" select="       $possible-dates[$refDate - xs:yearMonthDuration('P80Y') le .]                      [. lt $refDate + xs:yearMonthDuration('P20Y')]       "/>
    
    <xsl:copy>
      <xsl:sequence select="@*"/>
      <xsl:attribute name="value" select="year-from-date($date)"/>
      <xsl:sequence select="node()"/>
    </xsl:copy>
    
    
  </xsl:template><xsl:template xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" match="node() | @*" mode="ica_fn:canonicalize-ifr">
    <xsl:copy>
      <xsl:apply-templates mode="#current" select="@*"/>
      <xsl:apply-templates mode="#current" select="node()"/>
    </xsl:copy>
  </xsl:template><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:string" name="ica_pr:parse-indicator">
    <xsl:param name="ipr"/>
    <xsl:variable name="first-pps" select="$ipr/*[1]"/>
    <xsl:choose>
      <xsl:when test="empty($first-pps)"><xsl:sequence select="''"/></xsl:when>
      <!-- assumption: l/@char-sequence is never empty -->
      <xsl:when test="$first-pps[self::l]"><xsl:sequence select="substring($first-pps/@char-sequence, 1, 1)"/></xsl:when>
      <xsl:when test="$first-pps[self::f]"><xsl:sequence select="$first-pps/@char-supply"/></xsl:when>
      <xsl:when test="$first-pps[self::a]"><xsl:sequence select="string-join($first-pps/*/ica_pr:parse-indicator(.), '')"/></xsl:when>
      <xsl:otherwise>
        <xsl:sequence select="error:throw(concat('IPR contains an element named ', local-name($first-pps)), $error:impl)"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:integer?" name="ica_pr:field-length">
    <xsl:param as="element(f)" name="field"/>
    <xsl:for-each select="$field">
      <xsl:choose>
        <xsl:when test="@exponent">
          <xsl:variable name="exp" select="xs:integer(@exponent)"/>
          <xsl:variable as="xs:integer?" name="exp-min" select="if (@exponent-min = '-INF') then () else xs:integer((@exponent-min, @exponent)[1])"/>
          <xsl:sequence select="for $m in $exp-min return ($exp - $m + 1)"/>
        </xsl:when>
        <xsl:otherwise>1</xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:boolean" name="ica_pr:is-ifrs-mergeable">
    <xsl:param as="element(ifr)" name="ifr1"/>
    <xsl:param as="element(ifr)" name="ifr2"/>
    <xsl:sequence select="         every $f in distinct-values(($ifr1, $ifr2)/f/@name)[. != 'offset-sign']         satisfies ica_pr:is-field-mergeable($ifr1/f[@name = $f], $ifr2/f[@name = $f])           and           ica_pr:is-offset-sign-mergeable($ifr1/f[starts-with(@name, 'offset-')], $ifr2/f[starts-with(@name, 'offset-')])           "/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:boolean" name="ica_pr:is-offset-sign-mergeable">
    <xsl:param as="element(f)*" name="f1"/>
    <xsl:param as="element(f)*" name="f2"/>
    
    <xsl:variable name="sign1" select="$f1[@name = 'offset-sign']/@value"/>
    <xsl:variable name="sign2" select="$f2[@name = 'offset-sign']/@value"/>
    <xsl:variable name="isutc" select="        every $f in ($f1, $f2)[@name != 'offset-sign']         satisfies         ($f/@value = 'Z' or $f/@value = 0)       "/>
    
    <xsl:sequence select="       $isutc       or       $sign1 = $sign2       or       count(($sign1, $sign2)) lt 2       "/>
    
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:boolean" name="ica_pr:is-field-mergeable">
    <xsl:param as="element(f)?" name="f1"/>
    <xsl:param as="element(f)?" name="f2"/>

    <xsl:sequence select="         (not($f1) or not($f2))         or         $f1/@name != $f2/@name         or         $f1/@name = 'offset-sign'         or         (if ($f1/@exponent-min) then           ica_pr:is-decimal-mergeable(           $f1/@value/xs:decimal(.),            $f2/@value/xs:decimal(.),           $f1/@exponent-min/xs:integer(.),           $f1/@exponent-max/xs:integer(.),           $f2/@exponent-min/xs:integer(.),           $f2/@exponent-max/xs:integer(.)           )         else           $f1/@value = $f2/@value           )         "/>

  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:boolean" name="ica_pr:is-decimal-mergeable">
    <xsl:param as="xs:decimal" name="v1"/>
    <xsl:param as="xs:decimal" name="v2"/>
    <xsl:param as="xs:integer" name="exp-min-1"/>
    <xsl:param as="xs:integer" name="exp-max-1"/>
    <xsl:param as="xs:integer" name="exp-min-2"/>
    <xsl:param as="xs:integer" name="exp-max-2"/>

    <xsl:variable name="exp-min" select="min(($exp-min-1, $exp-min-2))"/>
    <xsl:variable name="exp-max" select="max(($exp-max-1, $exp-max-2))"/>

    <xsl:variable name="exponents1" select="$exp-min-1 to $exp-max-1"/>
    <xsl:variable name="exponents2" select="$exp-min-2 to $exp-max-2"/>
    
    
    <xsl:sequence select="         every $e in ($exp-min to $exp-max)         satisfies ica_pr:is-decimal-mergeable($v1, $v2, $e[. = $exponents1], $e[. = $exponents2])         "/>

  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:boolean" name="ica_pr:is-decimal-mergeable">
    <xsl:param as="xs:decimal" name="v1"/>
    <xsl:param as="xs:decimal" name="v2"/>
    <xsl:param as="xs:integer?" name="exp1"/>
    <xsl:param as="xs:integer?" name="exp2"/>
    

    <xsl:sequence select="         count($exp1) = 0         or         count($exp2) = 0         or         not($exp1 = $exp2)         or         ica_fn:get-digit($v1, $exp1) = ica_fn:get-digit($v2, $exp2)         "/>

  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(mf)" name="ica_fn:createJsonMfStruct">
    <xsl:param as="node()*" name="nodes_in"/>
    <mf>
      <params>
        <xsl:sequence select="ica_fn:createYaml($nodes_in)"/>
      </params>
    </mf>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="element(cond)" name="ica_fn:createJsonCondStruct">
    <xsl:param as="node()*" name="nodes_in"/>
    <xsl:param as="xs:boolean" name="cond_result"/>
    
    <cond>
      <params>
        <xsl:sequence select="ica_fn:createYaml($nodes_in)"/>
      </params>
      <result as="boolean">
        <xsl:value-of select="$cond_result"/>
      </result>
    </cond>
    
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:string" name="ica_fn:createJsonObject">
    <xsl:param as="element()*" name="xml"/>
    
    <xsl:variable name="tokens">
      <xsl:text>{ </xsl:text>
      <xsl:for-each select="$xml">
        <xsl:sequence select="ica_fn:json-string(name())"/>
        <xsl:text> : </xsl:text>
        <xsl:sequence select="             if (@as)            then (ica_fn:json-as(.))        else if (*)            then ica_fn:createJsonObject(*)           else ica_fn:json-string(.)           "/>
        <xsl:if test="position() != last()">
          <xsl:text>, </xsl:text>
        </xsl:if>
      </xsl:for-each>
      <xsl:text> }</xsl:text>
    </xsl:variable>
    
    <xsl:sequence select="string-join($tokens, '')"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:string" name="ica_fn:json-as">
    <xsl:param as="element()" name="xml"/>
    <xsl:sequence select="ica_fn:json-as(string($xml), $xml/@as)"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:string" name="ica_fn:json-as">
    <xsl:param as="xs:string" name="content"/>
    <xsl:param as="xs:string?" name="as"/>
    <xsl:variable name="as" select="($as, 'string')[1]"/>
    <xsl:sequence select="         if ($as = 'boolean')        then ica_fn:json-boolean($content = 'true')    else if ($as = 'string')        then ica_fn:json-string($content)       else ica_fn:json-string($content)       "/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:string" name="ica_fn:json-boolean">
    <xsl:param as="xs:boolean" name="value"/>
    <xsl:sequence select="string($value)"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:string" name="ica_fn:json-string">
    <xsl:param as="xs:string" name="value"/>
    <xsl:variable name="escaped" select="replace(replace($value, &#34;(\\|&#34;&#34;)&#34;, &#34;\\$1&#34;), &#34;\n&#34;, &#34;\\n&#34;)"/>
    <xsl:sequence select="concat(&#34;&#34;&#34;&#34;, $escaped, &#34;&#34;&#34;&#34;)"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:string" name="ica_fn:createYaml">
    <xsl:param as="node()*" name="nodes_in"/>
    <xsl:sequence select="string-join($nodes_in/ica_fn:createYaml(., 0), '&#xA;')"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:string" name="ica_fn:createYaml">
    <xsl:param as="node()" name="nodes_in"/>
    <xsl:param as="xs:integer" name="indent"/>
    <xsl:variable name="indent-space" select="       string-join(for $s in 1 to ($indent * 2)       return       ' ', '')"/>
    <xsl:variable name="firstLine" select="       $indent-space,       '- ',       $nodes_in/name(),       ':',       ica_fn:createYamlValue($nodes_in, $indent-space)       "/>
    <xsl:variable name="nextLines" select="$nodes_in/*/ica_fn:createYaml(., $indent + 1)"/>
    
    <xsl:sequence select="string-join((string-join($firstLine, ''), $nextLines), '&#xA;')"/>
    
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:string" name="ica_fn:createYamlValue">
    <xsl:param as="node()" name="node"/>
    <xsl:param as="xs:string" name="indent-space"/>


    <xsl:variable name="escaped" select="replace($node, '(\\|'')', '\\$1')"/>
    
    
    <xsl:sequence select="              if ($node/*)        then ''       else         if ($node/@*)       then ica_fn:createYamlValueWithAttributes($node, $indent-space)       else concat('''', ica_fn:escapeAndSkip($node), '''')              "/>
    
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:string" name="ica_fn:createYamlValueWithAttributes">
    <xsl:param as="node()" name="node"/>
    <xsl:param as="xs:string" name="indent-space"/>

    <xsl:variable name="indent-space" select="concat($indent-space, '  ')"/>

    <xsl:variable name="attributesAndSelf" select="($node/@*, $node[. != ''])"/>

    <xsl:variable name="lines" select="         for $v in $attributesAndSelf         return           concat($indent-space, ica_fn:yamlMappingKeyFromNode($v), ': ', ica_fn:yamlMappingValueFromNode($v))"/>

    <xsl:sequence select="         concat('&#xA;', string-join($lines, '&#xA;')         )"/>

  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="ica_fn:yamlMappingKeyFromNode">
    <xsl:param as="node()" name="node"/>
    <xsl:sequence select="         if ($node instance of attribute()) then           concat('''@', name($node), '''')         else           ('.')         "/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" name="ica_fn:yamlMappingValueFromNode">
    <xsl:param as="node()" name="node"/>
    
    <xsl:sequence select="         if ($node instance of attribute() and $node castable as xs:double)        then $node       else concat('''', ica_fn:escapeAndSkip($node), '''')         "/>
    
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:string" name="ica_fn:escapeAndSkip">
    <xsl:param as="xs:string" name="value"/>
    <xsl:sequence select="ica_fn:escapeAndSkip($value, 20)"/>
  </xsl:function><xsl:function xmlns:error="http://www.sap.com/ica/mag/function/private/error" xmlns:ica_pr="http://www.sap.com/ica/mag/function/private" as="xs:string" name="ica_fn:escapeAndSkip">
    <xsl:param as="xs:string" name="value"/>
    <xsl:param as="xs:integer" name="length"/>

    <xsl:variable name="escaped" select="replace($value, '(\\|'')', '\\$1')"/>

    <xsl:sequence select="                if (string-length($escaped) gt $length)        then concat(substring($escaped, 1, $length), '...')       else $escaped              "/>
    
  </xsl:function></xsl:stylesheet>