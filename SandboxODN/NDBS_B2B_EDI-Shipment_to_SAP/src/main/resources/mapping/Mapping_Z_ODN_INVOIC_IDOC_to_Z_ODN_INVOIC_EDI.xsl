<?xml version="1.0" encoding="UTF-8"?><!-- Copyright statement for Type System ASC X12: Copyright (c) 2017, Accredited Standards Committee X12 Incorporated, Format (c) 2017 Washington Publishing Company. Exclusively published by the Washington Publishing Company. No part of this publication maybe distributed, posted, reproduced, stored in a retrieval system, or transmitted in any form or by any means without the prior written permission of the copyright owner. See also: http://members.x12.org/policies-procedures/adp06-intellectual-property-rights-policy-statement.pdf --><!-- Copyright statement for Type System UN/EDIFACT: Copyright (c) United Nations 2000-2008. All rights reserved. None of the materials provided on this web site may be used, reproduced or transmitted, in whole or in part, in any form or by any means, electronic or mechanical, including photocopying, recording or the use of any information storage and retrieval system, except as provided for in the Terms and Conditions of Use of United Nations Web Sites, without permission in writing from the publisher. To request such permission and for further enquiries, contact the Secretary of the Publications Board, United Nations, New York, NY, 10017, USA (pubboard@un.org; Telephone: (+1) 212-963-4664; Facsimile: (+1) 212-963-0077). See also: http://www.unece.org/legal_notice/copyrightnotice.html --><!-- Copyright statement for Type System SAP IDoc: Copyright © SAP SE 2017. All Rights Reserved. See also: https://www.sap.com/corporate/en/legal/copyright/use-of-copyrighted-material.html --><!-- Copyright statement for ISO Codelists: Copyright (c) 2017, ISO All ISO content is copyright protected. The copyright is owned by ISO. Any use of the content, including copying of it in whole or in part, for example to another Internet site, is prohibited and would require written permission from ISO. All ISO publications are also protected by copyright. The copyright ownership of ISO is clearly indicated on every ISO publication. Any unauthorized use such as copying, scanning or distribution is prohibited. Requests for permission should be addressed to the ISO Central Secretariat or directly through the ISO member in your country. See more: https://www.iso.org/privacy-and-copyright.html --><!-- Copyright statement for UN/CEFACT Codelists: Copyright (c) United Nations 2000-2008. All rights reserved. None of the materials provided on this web site may be used, reproduced or transmitted, in whole or in part, in any form or by any means, electronic or mechanical, including photocopying, recording or the use of any information storage and retrieval system, except as provided for in the Terms and Conditions of Use of United Nations Web Sites, without permission in writing from the publisher. To request such permission and for further enquiries, contact the Secretary of the Publications Board, United Nations, New York, NY, 10017, USA (pubboard@un.org; Telephone: (+1) 212-963-4664; Facsimile: (+1) 212-963-0077). See also: http://www.unece.org/legal_notice/copyrightnotice.html --><xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:a="urn:sap.com:ica:mag" xmlns:global="urn:sap.com/ica/mag/internal-use/global" xmlns:ica="http://www.sap.com/ica/mag/" xmlns:ica_env="urn:sap:ica:env" xmlns:ica_env_options="urn:sap:ica:env:options" xmlns:ica_fn="http://www.sap.com/ica/mag/function" xmlns:ica_gen="urn:sap:ica:gen" xmlns:sap="http://www.ttools.org/sap/ns/structure" xmlns:sapaqua="http://www.sap.com/ns/saat/aqua" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:z="http://www.xsdplus.org/ns/structure" exclude-result-prefixes="#all" version="2.0"><xsl:output encoding="UTF-8" method="xml"/><xsl:template match="/"><xsl:variable name="c" select="/*"/><xsl:variable as="xs:integer*" name="ica_gen:iteration" select="()"/><M_INVOIC><xsl:variable name="c" select="$c/(/INVOIC02)"/><S_UNH><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_0062><xsl:value-of select="$src"/></D_0062></xsl:if><C_S009><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count(ica_fn:f1($c/(.))) &gt; 0) then (ica_fn:f1($c/(.))) else ()"/><D_0065><xsl:value-of select="$src"/></D_0065></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="if (count(ica_fn:f2($c/(.))) &gt; 0) then (ica_fn:f2($c/(.))) else ()"/><D_0052><xsl:value-of select="$src"/></D_0052></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="if (count(ica_fn:f3($c/(.))) &gt; 0) then (ica_fn:f3($c/(.))) else ()"/><D_0054><xsl:value-of select="$src"/></D_0054></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="if (count(ica_fn:f4($c/(.))) &gt; 0) then (ica_fn:f4($c/(.))) else ()"/><D_0051><xsl:value-of select="$src"/></D_0051></xsl:if></C_S009></S_UNH><S_BGM><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><C_C002><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1001><xsl:value-of select="$src"/></D_1001></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1131><xsl:value-of select="$src"/></D_1131></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3055><xsl:value-of select="$src"/></D_3055></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1000><xsl:value-of select="$src"/></D_1000></xsl:if></xsl:if></C_C002></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1004><xsl:value-of select="$src"/></D_1004></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1225><xsl:value-of select="$src"/></D_1225></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4343><xsl:value-of select="$src"/></D_4343></xsl:if></xsl:if></S_BGM><xsl:for-each select="$c/((.)[position() &lt;= 35])"><xsl:variable name="c" select="."/><S_DTM><C_C507><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_2005><xsl:value-of select="$src"/></D_2005></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2380><xsl:value-of select="$src"/></D_2380></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2379><xsl:value-of select="$src"/></D_2379></xsl:if></xsl:if></C_C507></S_DTM></xsl:for-each><xsl:if test="$c/()"><S_PAI><xsl:variable name="c" select="$c/(.)"/><C_C534><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4439><xsl:value-of select="$src"/></D_4439></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4431><xsl:value-of select="$src"/></D_4431></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4461><xsl:value-of select="$src"/></D_4461></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1131><xsl:value-of select="$src"/></D_1131></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3055><xsl:value-of select="$src"/></D_3055></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4435><xsl:value-of select="$src"/></D_4435></xsl:if></xsl:if></C_C534></S_PAI></xsl:if><xsl:if test="$c/()"><xsl:for-each select="$c/((.)[position() &lt;= 5])"><xsl:variable name="c" select="."/><S_ALI><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3239><xsl:value-of select="$src"/></D_3239></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_9213><xsl:value-of select="$src"/></D_9213></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4183><xsl:value-of select="$src"/></D_4183></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4183_2><xsl:value-of select="$src"/></D_4183_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4183_3><xsl:value-of select="$src"/></D_4183_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4183_4><xsl:value-of select="$src"/></D_4183_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4183_5><xsl:value-of select="$src"/></D_4183_5></xsl:if></xsl:if></S_ALI></xsl:for-each></xsl:if><xsl:if test="$c/()"><S_IMD><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_7077><xsl:value-of select="$src"/></D_7077></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_7081><xsl:value-of select="$src"/></D_7081></xsl:if></xsl:if><xsl:if test="$c/()"><C_C273><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_7009><xsl:value-of select="$src"/></D_7009></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1131><xsl:value-of select="$src"/></D_1131></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3055><xsl:value-of select="$src"/></D_3055></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_7008><xsl:value-of select="$src"/></D_7008></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_7008_2><xsl:value-of select="$src"/></D_7008_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3453><xsl:value-of select="$src"/></D_3453></xsl:if></xsl:if></C_C273></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_7383><xsl:value-of select="$src"/></D_7383></xsl:if></xsl:if></S_IMD></xsl:if><xsl:if test="$c/()"><xsl:for-each select="$c/((.)[position() &lt;= 10])"><xsl:variable name="c" select="."/><S_FTX><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_4451><xsl:value-of select="$src"/></D_4451></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4453><xsl:value-of select="$src"/></D_4453></xsl:if></xsl:if><xsl:if test="$c/()"><C_C107><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_4441><xsl:value-of select="$src"/></D_4441></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1131><xsl:value-of select="$src"/></D_1131></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3055><xsl:value-of select="$src"/></D_3055></xsl:if></xsl:if></C_C107></xsl:if><xsl:if test="$c/()"><C_C108><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_4440><xsl:value-of select="$src"/></D_4440></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4440_2><xsl:value-of select="$src"/></D_4440_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4440_3><xsl:value-of select="$src"/></D_4440_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4440_4><xsl:value-of select="$src"/></D_4440_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4440_5><xsl:value-of select="$src"/></D_4440_5></xsl:if></xsl:if></C_C108></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3453><xsl:value-of select="$src"/></D_3453></xsl:if></xsl:if></S_FTX></xsl:for-each></xsl:if><xsl:if test="$c/()"><xsl:for-each select="$c/((.)[position() &lt;= 99])"><xsl:variable name="c" select="."/><G_SG1><S_RFF><xsl:variable name="c" select="$c/(.)"/><C_C506><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_1153><xsl:value-of select="$src"/></D_1153></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1154><xsl:value-of select="$src"/></D_1154></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1156><xsl:value-of select="$src"/></D_1156></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4000><xsl:value-of select="$src"/></D_4000></xsl:if></xsl:if></C_C506></S_RFF><xsl:if test="$c/()"><xsl:for-each select="$c/((.)[position() &lt;= 5])"><xsl:variable name="c" select="."/><S_DTM><C_C507><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_2005><xsl:value-of select="$src"/></D_2005></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2380><xsl:value-of select="$src"/></D_2380></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2379><xsl:value-of select="$src"/></D_2379></xsl:if></xsl:if></C_C507></S_DTM></xsl:for-each></xsl:if></G_SG1></xsl:for-each></xsl:if><xsl:if test="$c/()"><xsl:for-each select="$c/((.)[position() &lt;= 99])"><xsl:variable name="c" select="."/><G_SG2><S_NAD><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_3035><xsl:value-of select="$src"/></D_3035></xsl:if><xsl:if test="$c/()"><C_C082><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_3039><xsl:value-of select="$src"/></D_3039></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1131><xsl:value-of select="$src"/></D_1131></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3055><xsl:value-of select="$src"/></D_3055></xsl:if></xsl:if></C_C082></xsl:if><xsl:if test="$c/()"><C_C058><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_3124><xsl:value-of select="$src"/></D_3124></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3124_2><xsl:value-of select="$src"/></D_3124_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3124_3><xsl:value-of select="$src"/></D_3124_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3124_4><xsl:value-of select="$src"/></D_3124_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3124_5><xsl:value-of select="$src"/></D_3124_5></xsl:if></xsl:if></C_C058></xsl:if><xsl:if test="$c/()"><C_C080><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_3036><xsl:value-of select="$src"/></D_3036></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3036_2><xsl:value-of select="$src"/></D_3036_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3036_3><xsl:value-of select="$src"/></D_3036_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3036_4><xsl:value-of select="$src"/></D_3036_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3036_5><xsl:value-of select="$src"/></D_3036_5></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3045><xsl:value-of select="$src"/></D_3045></xsl:if></xsl:if></C_C080></xsl:if><xsl:if test="$c/()"><C_C059><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_3042><xsl:value-of select="$src"/></D_3042></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3042_2><xsl:value-of select="$src"/></D_3042_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3042_3><xsl:value-of select="$src"/></D_3042_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3042_4><xsl:value-of select="$src"/></D_3042_4></xsl:if></xsl:if></C_C059></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3164><xsl:value-of select="$src"/></D_3164></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3229><xsl:value-of select="$src"/></D_3229></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3251><xsl:value-of select="$src"/></D_3251></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3207><xsl:value-of select="$src"/></D_3207></xsl:if></xsl:if></S_NAD><xsl:if test="$c/()"><xsl:for-each select="$c/((.)[position() &lt;= 25])"><xsl:variable name="c" select="."/><S_LOC><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_3227><xsl:value-of select="$src"/></D_3227></xsl:if><xsl:if test="$c/()"><C_C517><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3225><xsl:value-of select="$src"/></D_3225></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1131><xsl:value-of select="$src"/></D_1131></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3055><xsl:value-of select="$src"/></D_3055></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3224><xsl:value-of select="$src"/></D_3224></xsl:if></xsl:if></C_C517></xsl:if><xsl:if test="$c/()"><C_C519><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3223><xsl:value-of select="$src"/></D_3223></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1131><xsl:value-of select="$src"/></D_1131></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3055><xsl:value-of select="$src"/></D_3055></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3222><xsl:value-of select="$src"/></D_3222></xsl:if></xsl:if></C_C519></xsl:if><xsl:if test="$c/()"><C_C553><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3233><xsl:value-of select="$src"/></D_3233></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1131><xsl:value-of select="$src"/></D_1131></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3055><xsl:value-of select="$src"/></D_3055></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3232><xsl:value-of select="$src"/></D_3232></xsl:if></xsl:if></C_C553></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_5479><xsl:value-of select="$src"/></D_5479></xsl:if></xsl:if></S_LOC></xsl:for-each></xsl:if><xsl:if test="$c/()"><xsl:for-each select="$c/((.)[position() &lt;= 5])"><xsl:variable name="c" select="."/><S_FII><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_3035><xsl:value-of select="$src"/></D_3035></xsl:if><xsl:if test="$c/()"><C_C078><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3194><xsl:value-of select="$src"/></D_3194></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3192><xsl:value-of select="$src"/></D_3192></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3192_2><xsl:value-of select="$src"/></D_3192_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_6345><xsl:value-of select="$src"/></D_6345></xsl:if></xsl:if></C_C078></xsl:if><xsl:if test="$c/()"><C_C088><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3433><xsl:value-of select="$src"/></D_3433></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1131><xsl:value-of select="$src"/></D_1131></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3055><xsl:value-of select="$src"/></D_3055></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3434><xsl:value-of select="$src"/></D_3434></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1131_2><xsl:value-of select="$src"/></D_1131_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3055_2><xsl:value-of select="$src"/></D_3055_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3432><xsl:value-of select="$src"/></D_3432></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3436><xsl:value-of select="$src"/></D_3436></xsl:if></xsl:if></C_C088></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3207><xsl:value-of select="$src"/></D_3207></xsl:if></xsl:if></S_FII></xsl:for-each></xsl:if><xsl:if test="$c/()"><xsl:for-each select="$c/((.)[position() &lt;= 9999])"><xsl:variable name="c" select="."/><G_SG3><S_RFF><xsl:variable name="c" select="$c/(.)"/><C_C506><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_1153><xsl:value-of select="$src"/></D_1153></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1154><xsl:value-of select="$src"/></D_1154></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1156><xsl:value-of select="$src"/></D_1156></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4000><xsl:value-of select="$src"/></D_4000></xsl:if></xsl:if></C_C506></S_RFF><xsl:if test="$c/()"><xsl:for-each select="$c/((.)[position() &lt;= 5])"><xsl:variable name="c" select="."/><S_DTM><C_C507><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_2005><xsl:value-of select="$src"/></D_2005></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2380><xsl:value-of select="$src"/></D_2380></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2379><xsl:value-of select="$src"/></D_2379></xsl:if></xsl:if></C_C507></S_DTM></xsl:for-each></xsl:if></G_SG3></xsl:for-each></xsl:if><xsl:if test="$c/()"><xsl:for-each select="$c/((.)[position() &lt;= 5])"><xsl:variable name="c" select="."/><G_SG4><S_DOC><xsl:variable name="c" select="$c/(.)"/><C_C002><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1001><xsl:value-of select="$src"/></D_1001></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1131><xsl:value-of select="$src"/></D_1131></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3055><xsl:value-of select="$src"/></D_3055></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1000><xsl:value-of select="$src"/></D_1000></xsl:if></xsl:if></C_C002><xsl:if test="$c/()"><C_C503><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1004><xsl:value-of select="$src"/></D_1004></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1373><xsl:value-of select="$src"/></D_1373></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1366><xsl:value-of select="$src"/></D_1366></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3453><xsl:value-of select="$src"/></D_3453></xsl:if></xsl:if></C_C503></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3153><xsl:value-of select="$src"/></D_3153></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1220><xsl:value-of select="$src"/></D_1220></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_1218><xsl:value-of select="$src"/></D_1218></xsl:if></xsl:if></S_DOC><xsl:if test="$c/()"><xsl:for-each select="$c/((.)[position() &lt;= 5])"><xsl:variable name="c" select="."/><S_DTM><C_C507><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_2005><xsl:value-of select="$src"/></D_2005></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2380><xsl:value-of select="$src"/></D_2380></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2379><xsl:value-of select="$src"/></D_2379></xsl:if></xsl:if></C_C507></S_DTM></xsl:for-each></xsl:if></G_SG4></xsl:for-each></xsl:if><xsl:if test="$c/()"><xsl:for-each select="$c/((.)[position() &lt;= 5])"><xsl:variable name="c" select="."/><G_SG5><S_CTA><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3139><xsl:value-of select="$src"/></D_3139></xsl:if></xsl:if><xsl:if test="$c/()"><C_C056><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3413><xsl:value-of select="$src"/></D_3413></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3412><xsl:value-of select="$src"/></D_3412></xsl:if></xsl:if></C_C056></xsl:if></S_CTA><xsl:if test="$c/()"><xsl:for-each select="$c/((.)[position() &lt;= 5])"><xsl:variable name="c" select="."/><S_COM><C_C076><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_3148><xsl:value-of select="$src"/></D_3148></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_3155><xsl:value-of select="$src"/></D_3155></xsl:if></C_C076></S_COM></xsl:for-each></xsl:if></G_SG5></xsl:for-each></xsl:if></G_SG2></xsl:for-each></xsl:if><S_UNS><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count(ica_fn:f5($c/(.))) &gt; 0) then (ica_fn:f5($c/(.))) else ()"/><D_0081><xsl:value-of select="$src"/></D_0081></xsl:if></S_UNS><xsl:for-each select="$c/((.)[position() &lt;= 100])"><xsl:variable name="c" select="."/><G_SG48><S_MOA><xsl:variable name="c" select="$c/(.)"/><C_C516><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_5025><xsl:value-of select="$src"/></D_5025></xsl:if></C_C516></S_MOA></G_SG48></xsl:for-each><S_UNT><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="if (count(ica_fn:f6($c/(.))) &gt; 0) then (ica_fn:f6($c/(.))) else ()"/><D_0074><xsl:value-of select="$src"/></D_0074></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="if (count($c/()) &gt; 0) then ($c/()) else ()"/><D_0062><xsl:value-of select="$src"/></D_0062></xsl:if></S_UNT></M_INVOIC></xsl:template><xsl:function name="ica_fn:f1"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(6)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
          <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
            <xsl:for-each select="$nodes_in">
              <xsl:copy>
                <xsl:sequence select="*"/>
                
                    <value>INVOIC</value>
                  
              </xsl:copy>
            </xsl:for-each>
          </xsl:variable>
          <xsl:value-of select="$nodes_in/value"/>
          <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f2"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
          <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
            <xsl:for-each select="$nodes_in">
              <xsl:copy>
                <xsl:sequence select="*"/>
                
                    <value>D</value>
                  
              </xsl:copy>
            </xsl:for-each>
          </xsl:variable>
          <xsl:value-of select="$nodes_in/value"/>
          <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f3"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
          <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
            <xsl:for-each select="$nodes_in">
              <xsl:copy>
                <xsl:sequence select="*"/>
                
                    <value>96A</value>
                  
              </xsl:copy>
            </xsl:for-each>
          </xsl:variable>
          <xsl:value-of select="$nodes_in/value"/>
          <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f4"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(2)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
          <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
            <xsl:for-each select="$nodes_in">
              <xsl:copy>
                <xsl:sequence select="*"/>
                
                    <value>UN</value>
                  
              </xsl:copy>
            </xsl:for-each>
          </xsl:variable>
          <xsl:value-of select="$nodes_in/value"/>
          <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f5"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(1)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
          <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
            <xsl:for-each select="$nodes_in">
              <xsl:copy>
                <xsl:sequence select="*"/>
                
                    <value>S</value>
                  
              </xsl:copy>
            </xsl:for-each>
          </xsl:variable>
          <xsl:value-of select="$nodes_in/value"/>
          <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f6"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(6)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
          <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
            <xsl:for-each select="$nodes_in">
              <xsl:copy>
                <xsl:sequence select="*"/>
                
                    <value>$B2B_SEG_COUNTER</value>
                  
              </xsl:copy>
            </xsl:for-each>
          </xsl:variable>
          <xsl:value-of select="$nodes_in/value"/>
          <!--
              end of snippet code
          --></xsl:function><xsl:param name="defaultChar" select="'#'"/><xsl:function as="xs:string" name="ica_fn:length-constrains"><xsl:param name="text"/><xsl:param as="xs:integer" name="minLen"/><xsl:param as="xs:integer" name="maxLen"/><xsl:sequence select="ica_fn:length-constrains($text, $minLen, $maxLen, $defaultChar)"/></xsl:function><xsl:function as="xs:string" name="ica_fn:length-constrains"><xsl:param name="text"/><xsl:param as="xs:integer" name="minLen"/><xsl:param as="xs:integer" name="maxLen"/><xsl:param as="xs:string" name="defaultChar"/><xsl:variable name="defaultChar" select="substring($defaultChar, 1, 1)"/><xsl:variable as="xs:integer" name="textLen" select="string-length($text)"/><xsl:variable name="maxLen" select="             if ($maxLen = -1) then               ($textLen)             else               ($maxLen)"/><xsl:sequence select="             if ($textLen gt $maxLen) then               substring($text, 1, $maxLen)             else               if ($textLen lt $minLen) then                 string-join(($text,                 (for $c in $textLen + 1 to $minLen                 return                   $defaultChar)), '')               else                 $text             "/></xsl:function><!--
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
  </xsl:function><xsl:function as="xs:string" name="ica_fn:createYaml">
    <xsl:param as="node()*" name="nodes_in"/>
    <xsl:sequence select="string-join($nodes_in/ica_fn:createYaml(., 0), '&#xA;')"/>
  </xsl:function><xsl:function as="xs:string" name="ica_fn:createYaml">
    <xsl:param as="node()" name="nodes_in"/>
    <xsl:param as="xs:integer" name="indent"/>
    <xsl:variable name="indent-space" select="         string-join(for $s in 1 to ($indent * 2)         return           ' ', '')"/>
    <xsl:variable name="firstLine" select="         $indent-space,         '- ',         $nodes_in/name(),         ':',         ica_fn:createYamlValue($nodes_in)         "/>
    <xsl:variable name="nextLines" select="$nodes_in/*/ica_fn:createYaml(., $indent + 1)"/>

    <xsl:sequence select="string-join((string-join($firstLine, ''), $nextLines), '&#xA;')"/>

  </xsl:function><xsl:function as="xs:string" name="ica_fn:createYamlValue">
    <xsl:param as="node()" name="node"/>
    <xsl:variable name="escaped" select="replace($node, '(\\|'')', '\\$1')"/>
    
    <xsl:variable name="skipped" select="         if (string-length($escaped) gt 20) then           concat(substring($escaped, 1, 20), '...')         else           $escaped"/>
    
    <xsl:sequence select="         if ($node/*) then           ('')         else           concat('''', $skipped, '''')"/>
    
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

  </xsl:function></xsl:stylesheet>