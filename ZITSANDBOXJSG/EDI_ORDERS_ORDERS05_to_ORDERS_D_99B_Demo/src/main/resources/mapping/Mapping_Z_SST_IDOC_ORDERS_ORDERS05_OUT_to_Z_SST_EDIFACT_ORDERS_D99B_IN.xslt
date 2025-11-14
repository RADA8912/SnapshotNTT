<?xml version="1.0" encoding="UTF-8"?><!-- Copyright statement for Type System ASC X12: Copyright (c) 2017, Accredited Standards Committee X12 Incorporated, Format (c) 2017 Washington Publishing Company. Exclusively published by the Washington Publishing Company. No part of this publication maybe distributed, posted, reproduced, stored in a retrieval system, or transmitted in any form or by any means without the prior written permission of the copyright owner. See also: http://members.x12.org/policies-procedures/adp06-intellectual-property-rights-policy-statement.pdf --><!-- Copyright statement for Type System UN/EDIFACT: Copyright (c) United Nations 2000-2008. All rights reserved. None of the materials provided on this web site may be used, reproduced or transmitted, in whole or in part, in any form or by any means, electronic or mechanical, including photocopying, recording or the use of any information storage and retrieval system, except as provided for in the Terms and Conditions of Use of United Nations Web Sites, without permission in writing from the publisher. To request such permission and for further enquiries, contact the Secretary of the Publications Board, United Nations, New York, NY, 10017, USA (pubboard@un.org; Telephone: (+1) 212-963-4664; Facsimile: (+1) 212-963-0077). See also: http://www.unece.org/legal_notice/copyrightnotice.html --><!-- Copyright statement for Type System SAP IDoc: Copyright © SAP SE 2017. All Rights Reserved. See also: https://www.sap.com/corporate/en/legal/copyright/use-of-copyrighted-material.html --><!-- Copyright statement for ISO Codelists: Copyright (c) 2017, ISO All ISO content is copyright protected. The copyright is owned by ISO. Any use of the content, including copying of it in whole or in part, for example to another Internet site, is prohibited and would require written permission from ISO. All ISO publications are also protected by copyright. The copyright ownership of ISO is clearly indicated on every ISO publication. Any unauthorized use such as copying, scanning or distribution is prohibited. Requests for permission should be addressed to the ISO Central Secretariat or directly through the ISO member in your country. See more: https://www.iso.org/privacy-and-copyright.html --><!-- Copyright statement for UN/CEFACT Codelists: Copyright (c) United Nations 2000-2008. All rights reserved. None of the materials provided on this web site may be used, reproduced or transmitted, in whole or in part, in any form or by any means, electronic or mechanical, including photocopying, recording or the use of any information storage and retrieval system, except as provided for in the Terms and Conditions of Use of United Nations Web Sites, without permission in writing from the publisher. To request such permission and for further enquiries, contact the Secretary of the Publications Board, United Nations, New York, NY, 10017, USA (pubboard@un.org; Telephone: (+1) 212-963-4664; Facsimile: (+1) 212-963-0077). See also: http://www.unece.org/legal_notice/copyrightnotice.html --><xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:a="urn:sap.com:ica:mag" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:global="urn:sap.com/ica/mag/internal-use/global" xmlns:ica="http://www.sap.com/ica/mag/" xmlns:ica_env="urn:sap:ica:env" xmlns:ica_env_options="urn:sap:ica:env:options" xmlns:ica_fn="http://www.sap.com/ica/mag/function" xmlns:ica_gen="urn:sap:ica:gen" xmlns:sap="http://www.ttools.org/sap/ns/structure" xmlns:sapaqua="http://www.sap.com/ns/saat/aqua" xmlns:util="http://www.sap.com/ns/ica/xpath-functions/util" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:z="http://www.xsdplus.org/ns/structure" exclude-result-prefixes="#all" version="2.0"><xsl:output encoding="UTF-8" method="xml"/><xsl:template match="/"><xsl:variable name="c" select="/*"/><xsl:variable as="xs:integer*" name="ica_gen:iteration" select="()"/><M_ORDERS><xsl:variable name="c" select="$c/(/ORDERS05)"/><xsl:variable name="ica_env:t1" select="$c/(ica_fn:f100(E1EDP01/E1EDPT1_gq_F05/E1EDPT2/TDLINE))"/><S_UNH><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f1($c/(.))"/><D_0062><xsl:value-of select="$src"/></D_0062></xsl:if><C_S009><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f2($c/(.))"/><D_0065><xsl:value-of select="$src"/></D_0065></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f3($c/(.))"/><D_0052><xsl:value-of select="$src"/></D_0052></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f6($c/(.))"/><D_0054><xsl:value-of select="$src"/></D_0054></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f4($c/(.))"/><D_0051><xsl:value-of select="$src"/></D_0051></xsl:if></C_S009></S_UNH><S_BGM><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/(E1EDK01)"><C_C002><xsl:variable name="c" select="$c/(E1EDK01)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f7($c/(.))"/><D_1001><xsl:value-of select="$src"/></D_1001></xsl:if></C_C002></xsl:if><xsl:if test="$c/(E1EDK02_gq_001/BELNR)"><C_C106><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/(./E1EDK02_gq_001/BELNR)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f19($c/(.))"/><D_1004><xsl:value-of select="$src"/></D_1004></xsl:if></xsl:if></C_C106></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f8($c/(.))"/><D_1225><xsl:value-of select="$src"/></D_1225></xsl:if></S_BGM><xsl:if test="$c/(E1EDK03_gq_002/DATUM)"><S_DTM_gq_2><xsl:variable name="c" select="$c/(.)"/><C_C507><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f20($c/(.))"/><D_2005><xsl:value-of select="$src"/></D_2005></xsl:if><xsl:if test="$c/(./E1EDK03_gq_002/DATUM)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f23($c/(.))"/><D_2380><xsl:value-of select="$src"/></D_2380></xsl:if></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f26($c/(.))"/><D_2379><xsl:value-of select="$src"/></D_2379></xsl:if></C_C507></S_DTM_gq_2></xsl:if><xsl:if test="$c/(E1EDK03_gq_012/DATUM)"><S_DTM_gq_4><xsl:variable name="c" select="$c/(.)"/><C_C507><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f21($c/(.))"/><D_2005><xsl:value-of select="$src"/></D_2005></xsl:if><xsl:if test="$c/(./E1EDK03_gq_012/DATUM)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f24($c/(.))"/><D_2380><xsl:value-of select="$src"/></D_2380></xsl:if></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f27($c/(.))"/><D_2379><xsl:value-of select="$src"/></D_2379></xsl:if></C_C507></S_DTM_gq_4></xsl:if><xsl:if test="$c/()"><S_DTM_gq_97><xsl:variable name="c" select="$c/(.)"/><C_C507><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2005><xsl:value-of select="$src"/></D_2005></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2380><xsl:value-of select="$src"/></D_2380></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2379><xsl:value-of select="$src"/></D_2379></xsl:if></xsl:if></C_C507></S_DTM_gq_97></xsl:if><xsl:if test="$c/(E1EDK03_gq_011/DATUM)"><S_DTM_gq_137><xsl:variable name="c" select="$c/(.)"/><C_C507><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f22($c/(.))"/><D_2005><xsl:value-of select="$src"/></D_2005></xsl:if><xsl:if test="$c/(./E1EDK03_gq_011/DATUM)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f25($c/(.))"/><D_2380><xsl:value-of select="$src"/></D_2380></xsl:if></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f28($c/(.))"/><D_2379><xsl:value-of select="$src"/></D_2379></xsl:if></C_C507></S_DTM_gq_137></xsl:if><xsl:if test="$c/()"><S_DTM_gq_171><xsl:variable name="c" select="$c/(.)"/><C_C507><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2005><xsl:value-of select="$src"/></D_2005></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2380><xsl:value-of select="$src"/></D_2380></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2379><xsl:value-of select="$src"/></D_2379></xsl:if></xsl:if></C_C507></S_DTM_gq_171></xsl:if><xsl:for-each select="1 to (ica_fn:sp-iterations($ica_env:t1, 512, 0, 1, 0))"><xsl:variable name="ica_gen:iteration" select="($ica_gen:iteration, .)"/><xsl:if test="$c/(E1EDP01/E1EDPT1_gq_F05/E1EDPT2/TDLINE | E1EDP01/E1EDPT1_gq_F05/E1EDPT2)"><S_FTX_gq_AAB><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f98($c/(.))"/><D_4451><xsl:value-of select="$src"/></D_4451></xsl:if><xsl:for-each select="1 to (ica_fn:sp-iterations($ica_env:t1, 512, 0, 1, ($ica_gen:iteration[1]-1)*512))"><xsl:variable name="ica_gen:iteration" select="($ica_gen:iteration, .)"/><xsl:if test="$c/(E1EDP01/E1EDPT1_gq_F05/E1EDPT2/TDLINE | E1EDP01/E1EDPT1_gq_F05/E1EDPT2)"><C_C108><xsl:variable name="c" select="$c/(.)"/><xsl:for-each select="1 to (ica_fn:sp-iterations($ica_env:t1, 512, 1, 1, sum((($ica_gen:iteration[1]-1)*512, ($ica_gen:iteration[2]-1)*512))))"><xsl:variable name="ica_gen:iteration" select="($ica_gen:iteration, .)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:sp-src-value($ica_env:t1, 512, sum((($ica_gen:iteration[1]-1)*512, ($ica_gen:iteration[2]-1)*512, ($ica_gen:iteration[3]-1)*512)))"/><D_4440><xsl:value-of select="$src"/></D_4440></xsl:if></xsl:for-each><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_4440_2><xsl:value-of select="$src"/></D_4440_2></xsl:if></xsl:if></C_C108></xsl:if></xsl:for-each><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f99($c/(.))"/><D_3453><xsl:value-of select="$src"/></D_3453></xsl:if></S_FTX_gq_AAB></xsl:if></xsl:for-each><xsl:if test="$c/(E1EDK02_gq_001/BELNR | E1EDK02_gq_001/DATUM)"><G_SG1_gq_ON><xsl:variable name="c" select="$c/(.)"/><S_RFF><xsl:variable name="c" select="$c/(.)"/><C_C506><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f29($c/(.))"/><D_1153><xsl:value-of select="$src"/></D_1153></xsl:if><xsl:if test="$c/(./E1EDK02_gq_001/BELNR)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f30($c/(.))"/><D_1154><xsl:value-of select="$src"/></D_1154></xsl:if></xsl:if></C_C506></S_RFF><xsl:if test="$c/(E1EDK02_gq_001/DATUM)"><S_DTM_gq_4><xsl:variable name="c" select="$c/(.)"/><C_C507><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f31($c/(.))"/><D_2005><xsl:value-of select="$src"/></D_2005></xsl:if><xsl:if test="$c/(./E1EDK02_gq_001/DATUM)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f32($c/(.))"/><D_2380><xsl:value-of select="$src"/></D_2380></xsl:if></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f33($c/(.))"/><D_2379><xsl:value-of select="$src"/></D_2379></xsl:if></C_C507></S_DTM_gq_4></xsl:if><xsl:if test="$c/()"><S_DTM_gq_137><xsl:variable name="c" select="$c/(.)"/><C_C507><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2005><xsl:value-of select="$src"/></D_2005></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2380><xsl:value-of select="$src"/></D_2380></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2379><xsl:value-of select="$src"/></D_2379></xsl:if></xsl:if></C_C507></S_DTM_gq_137></xsl:if></G_SG1_gq_ON></xsl:if><xsl:if test="$c/(E1EDK02_gq_005/BELNR | E1EDK02_gq_005/DATUM)"><G_SG1_gq_CT><xsl:variable name="c" select="$c/(.)"/><S_RFF><xsl:variable name="c" select="$c/(.)"/><C_C506><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f34($c/(.))"/><D_1153><xsl:value-of select="$src"/></D_1153></xsl:if><xsl:if test="$c/(./E1EDK02_gq_005/BELNR)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f35($c/(.))"/><D_1154><xsl:value-of select="$src"/></D_1154></xsl:if></xsl:if></C_C506></S_RFF><xsl:if test="$c/(E1EDK02_gq_005/DATUM)"><S_DTM_gq_4><xsl:variable name="c" select="$c/(.)"/><C_C507><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f36($c/(.))"/><D_2005><xsl:value-of select="$src"/></D_2005></xsl:if><xsl:if test="$c/(./E1EDK02_gq_005/DATUM)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f84($c/(.))"/><D_2380><xsl:value-of select="$src"/></D_2380></xsl:if></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f37($c/(.))"/><D_2379><xsl:value-of select="$src"/></D_2379></xsl:if></C_C507></S_DTM_gq_4></xsl:if><xsl:if test="$c/()"><S_DTM_gq_137><xsl:variable name="c" select="$c/(.)"/><C_C507><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2005><xsl:value-of select="$src"/></D_2005></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2380><xsl:value-of select="$src"/></D_2380></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_2379><xsl:value-of select="$src"/></D_2379></xsl:if></xsl:if></C_C507></S_DTM_gq_137></xsl:if></G_SG1_gq_CT></xsl:if><xsl:if test="$c/(E1EDKA1_gq_AG/E1EDKA1/PARTN | E1EDKA1_gq_AG/E1EDKA1/ORGTX | E1EDKA1_gq_AG/E1EDKA1/TELF1 | E1EDKA1_gq_AG/E1EDKA1/TELFX | E1EDKA1_gq_AG)"><G_SG2_gq_BY><xsl:variable name="c" select="$c/(.)"/><S_NAD><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f38($c/(.))"/><D_3035><xsl:value-of select="$src"/></D_3035></xsl:if><xsl:if test="$c/(E1EDKA1_gq_AG/E1EDKA1/PARTN)"><C_C082><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f9($c/(.))"/><D_3039><xsl:value-of select="$src"/></D_3039></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f39($c/(.))"/><D_3055><xsl:value-of select="$src"/></D_3055></xsl:if></C_C082></xsl:if></S_NAD><xsl:if test="$c/(E1EDKA1_gq_AG/E1EDKA1/ORGTX | E1EDKA1_gq_AG/E1EDKA1/TELF1 | E1EDKA1_gq_AG/E1EDKA1/TELFX | E1EDKA1_gq_AG)"><xsl:for-each select="$c/((.)[position() &lt;= 5])"><xsl:variable name="c" select="."/><G_SG5><S_CTA><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f40($c/(.))"/><D_3139><xsl:value-of select="$src"/></D_3139></xsl:if><xsl:if test="$c/(E1EDKA1_gq_AG/E1EDKA1/ORGTX)"><C_C056><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3413><xsl:value-of select="$src"/></D_3413></xsl:if></xsl:if><xsl:if test="$c/(./E1EDKA1_gq_AG/E1EDKA1/ORGTX)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f41($c/(.))"/><D_3412><xsl:value-of select="$src"/></D_3412></xsl:if></xsl:if></C_C056></xsl:if></S_CTA><xsl:if test="$c/(E1EDKA1_gq_AG/E1EDKA1/TELF1)"><S_COM_gq_TE><xsl:variable name="c" select="$c/(.)"/><xsl:for-each select="$c/((.)[position() &lt;= 3])"><xsl:variable name="c" select="."/><C_C076><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f43($c/(.))"/><D_3148><xsl:value-of select="$src"/></D_3148></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f42($c/(.))"/><D_3155><xsl:value-of select="$src"/></D_3155></xsl:if></C_C076></xsl:for-each></S_COM_gq_TE></xsl:if><xsl:if test="$c/(E1EDKA1_gq_AG/E1EDKA1/TELFX)"><S_COM_gq_FX><xsl:variable name="c" select="$c/(.)"/><xsl:for-each select="$c/((.)[position() &lt;= 3])"><xsl:variable name="c" select="."/><C_C076><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f45($c/(.))"/><D_3148><xsl:value-of select="$src"/></D_3148></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f44($c/(.))"/><D_3155><xsl:value-of select="$src"/></D_3155></xsl:if></C_C076></xsl:for-each></S_COM_gq_FX></xsl:if><xsl:if test="$c/(E1EDKA1_gq_AG)"><S_COM_gq_EM><xsl:variable name="c" select="$c/(E1EDKA1_gq_AG)"/><xsl:for-each select="$c/((.)[position() &lt;= 3])"><xsl:variable name="c" select="."/><C_C076><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f47($c/(.))"/><D_3148><xsl:value-of select="$src"/></D_3148></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f46($c/(.))"/><D_3155><xsl:value-of select="$src"/></D_3155></xsl:if></C_C076></xsl:for-each></S_COM_gq_EM></xsl:if></G_SG5></xsl:for-each></xsl:if></G_SG2_gq_BY></xsl:if><xsl:if test="$c/(E1EDKA1_gq_LF/E1EDKA1/PARTN)"><G_SG2_gq_SU><xsl:variable name="c" select="$c/(.)"/><S_NAD><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f48($c/(.))"/><D_3035><xsl:value-of select="$src"/></D_3035></xsl:if><xsl:if test="$c/(E1EDKA1_gq_LF/E1EDKA1/PARTN)"><C_C082><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f50($c/(.))"/><D_3039><xsl:value-of select="$src"/></D_3039></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f49($c/(.))"/><D_3055><xsl:value-of select="$src"/></D_3055></xsl:if></C_C082></xsl:if></S_NAD></G_SG2_gq_SU></xsl:if><xsl:if test="$c/(E1EDP01/E1EDP01/WERKS | E1EDP01/E1EDP01/LGORT | E1EDKA1_gq_WE/E1EDKA1/NAME1 | E1EDKA1_gq_WE/E1EDKA1/NAME2 | E1EDKA1_gq_WE/E1EDKA1/NAME3 | E1EDKA1_gq_WE/E1EDKA1/STRAS | E1EDKA1_gq_WE/E1EDKA1/ORT01 | E1EDKA1_gq_WE/E1EDKA1/PSTLZ | E1EDKA1_gq_WE/E1EDKA1/LAND1)"><G_SG2_gq_DP><xsl:variable name="c" select="$c/(.)"/><S_NAD><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f51($c/(.))"/><D_3035><xsl:value-of select="$src"/></D_3035></xsl:if><xsl:if test="$c/(E1EDP01/E1EDP01/WERKS | E1EDP01/E1EDP01/LGORT)"><C_C082><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f82($c/(.))"/><D_3039><xsl:value-of select="$src"/></D_3039></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3055><xsl:value-of select="$src"/></D_3055></xsl:if></xsl:if></C_C082></xsl:if><xsl:if test="$c/(E1EDKA1_gq_WE/E1EDKA1/NAME1 | E1EDKA1_gq_WE/E1EDKA1/NAME2 | E1EDKA1_gq_WE/E1EDKA1/NAME3)"><C_C080><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f52($c/(.))"/><D_3036><xsl:value-of select="$src"/></D_3036></xsl:if><xsl:if test="$c/(./E1EDKA1_gq_WE/E1EDKA1/NAME2)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f89($c/(.))"/><D_3036_2><xsl:value-of select="$src"/></D_3036_2></xsl:if></xsl:if><xsl:if test="$c/(./E1EDKA1_gq_WE/E1EDKA1/NAME3)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f90($c/(.))"/><D_3036_3><xsl:value-of select="$src"/></D_3036_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3036_4><xsl:value-of select="$src"/></D_3036_4></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3036_5><xsl:value-of select="$src"/></D_3036_5></xsl:if></xsl:if></C_C080></xsl:if><xsl:if test="$c/(E1EDKA1_gq_WE/E1EDKA1/STRAS)"><C_C059><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f53($c/(.))"/><D_3042><xsl:value-of select="$src"/></D_3042></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3042_2><xsl:value-of select="$src"/></D_3042_2></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3042_3><xsl:value-of select="$src"/></D_3042_3></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_3042_4><xsl:value-of select="$src"/></D_3042_4></xsl:if></xsl:if></C_C059></xsl:if><xsl:if test="$c/(./E1EDKA1_gq_WE/E1EDKA1/ORT01)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f54($c/(.))"/><D_3164><xsl:value-of select="$src"/></D_3164></xsl:if></xsl:if><xsl:if test="$c/(./E1EDKA1_gq_WE/E1EDKA1/PSTLZ)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f55($c/(.))"/><D_3251><xsl:value-of select="$src"/></D_3251></xsl:if></xsl:if><xsl:if test="$c/(./E1EDKA1_gq_WE/E1EDKA1/LAND1)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f56($c/(.))"/><D_3207><xsl:value-of select="$src"/></D_3207></xsl:if></xsl:if></S_NAD></G_SG2_gq_DP></xsl:if><xsl:if test="$c/(E1EDK01/CURCY)"><xsl:for-each select="$c/((.)[position() &lt;= 5])"><xsl:variable name="c" select="."/><G_SG7><S_CUX><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/(E1EDK01/CURCY)"><C_C504><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f10($c/(.))"/><D_6347><xsl:value-of select="$src"/></D_6347></xsl:if><xsl:if test="$c/(./E1EDK01/CURCY)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f57($c/(.))"/><D_6345><xsl:value-of select="$src"/></D_6345></xsl:if></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f11($c/(.))"/><D_6343><xsl:value-of select="$src"/></D_6343></xsl:if></C_C504></xsl:if></S_CUX></G_SG7></xsl:for-each></xsl:if><xsl:for-each select="$c/((E1EDP01)[position() &lt;= 200000])"><xsl:variable name="c" select="."/><G_SG28><S_LIN><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/(./E1EDP01/POSEX)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f16($c/(.))"/><D_1082><xsl:value-of select="$src"/></D_1082></xsl:if></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f17($c/(.))"/><D_1229><xsl:value-of select="$src"/></D_1229></xsl:if><xsl:if test="$c/(E1EDP19_gq_001/IDTNR)"><C_C212><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/(./E1EDP19_gq_001/IDTNR)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f18($c/(.))"/><D_7140><xsl:value-of select="$src"/></D_7140></xsl:if></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f12($c/(.))"/><D_7143><xsl:value-of select="$src"/></D_7143></xsl:if></C_C212></xsl:if></S_LIN><xsl:if test="$c/(E1EDP19_gq_002/IDTNR)"><xsl:for-each select="$c/((.)[position() &lt;= 25])"><xsl:variable name="c" select="."/><S_PIA><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f58($c/(.))"/><D_4347><xsl:value-of select="$src"/></D_4347></xsl:if><C_C212><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/(./E1EDP19_gq_002/IDTNR)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f59($c/(.))"/><D_7140><xsl:value-of select="$src"/></D_7140></xsl:if></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f60($c/(.))"/><D_7143><xsl:value-of select="$src"/></D_7143></xsl:if></C_C212></S_PIA></xsl:for-each></xsl:if><xsl:if test="$c/(E1EDP19_gq_001/KTEXT)"><xsl:for-each select="$c/((.)[position() &lt;= 99])"><xsl:variable name="c" select="."/><S_IMD><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f61($c/(.))"/><D_7077><xsl:value-of select="$src"/></D_7077></xsl:if><xsl:if test="$c/(E1EDP19_gq_001/KTEXT)"><C_C272><xsl:variable name="c" select="$c/(E1EDP19_gq_001/KTEXT)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f62($c/(.))"/><D_7081><xsl:value-of select="$src"/></D_7081></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f63($c/(.))"/><D_3055><xsl:value-of select="$src"/></D_3055></xsl:if></C_C272></xsl:if><xsl:if test="$c/(E1EDP19_gq_001/KTEXT)"><C_C273><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/(./E1EDP19_gq_001/KTEXT)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f64($c/(.))"/><D_7008><xsl:value-of select="$src"/></D_7008></xsl:if></xsl:if></C_C273></xsl:if></S_IMD></xsl:for-each></xsl:if><xsl:if test="$c/(E1EDP01/MENGE | E1EDP01/MENEE)"><xsl:for-each select="$c/((.)[position() &lt;= 99])"><xsl:variable name="c" select="."/><S_QTY><C_C186><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f65($c/(.))"/><D_6063><xsl:value-of select="$src"/></D_6063></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f66($c/(.))"/><D_6060><xsl:value-of select="$src"/></D_6060></xsl:if><xsl:if test="$c/(./E1EDP01/MENEE)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f67($c/(.))"/><D_6411><xsl:value-of select="$src"/></D_6411></xsl:if></xsl:if></C_C186></S_QTY></xsl:for-each></xsl:if><xsl:for-each select="$c/((E1EDP20)[position() &lt;= 35])"><xsl:variable name="c" select="."/><S_DTM><C_C507><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f68($c/(.))"/><D_2005><xsl:value-of select="$src"/></D_2005></xsl:if><xsl:if test="$c/(./EDATU)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f69($c/(.))"/><D_2380><xsl:value-of select="$src"/></D_2380></xsl:if></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f70($c/(.))"/><D_2379><xsl:value-of select="$src"/></D_2379></xsl:if></C_C507></S_DTM></xsl:for-each><xsl:if test="$c/(../E1EDK01/EMPST)"><S_FTX_gq_COI><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f95($c/(.))"/><D_4451><xsl:value-of select="$src"/></D_4451></xsl:if><xsl:if test="$c/(../E1EDK01/EMPST)"><C_C108><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f97($c/(.))"/><D_4440><xsl:value-of select="$src"/></D_4440></xsl:if></C_C108></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f96($c/(.))"/><D_3453><xsl:value-of select="$src"/></D_3453></xsl:if></S_FTX_gq_COI></xsl:if><xsl:if test="$c/(E1EDP01/VPREI | E1EDP01/PEINH | E1EDP01/PMENE)"><xsl:for-each select="$c/((.)[position() &lt;= 25])"><xsl:variable name="c" select="."/><G_SG32><S_PRI><xsl:variable name="c" select="$c/(.)"/><xsl:if test="$c/(E1EDP01/VPREI | E1EDP01/PEINH | E1EDP01/PMENE)"><C_C509><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f71($c/(.))"/><D_5125><xsl:value-of select="$src"/></D_5125></xsl:if><xsl:if test="$c/(./E1EDP01/VPREI)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f72($c/(.))"/><D_5118><xsl:value-of select="$src"/></D_5118></xsl:if></xsl:if><xsl:if test="$c/()"><xsl:if test="true()"><xsl:variable name="src" select="$c/()"/><D_5387><xsl:value-of select="$src"/></D_5387></xsl:if></xsl:if><xsl:if test="$c/(./E1EDP01/PEINH)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f73($c/(.))"/><D_5284><xsl:value-of select="$src"/></D_5284></xsl:if></xsl:if><xsl:if test="$c/(./E1EDP01/PMENE)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f74($c/(.))"/><D_6411><xsl:value-of select="$src"/></D_6411></xsl:if></xsl:if></C_C509></xsl:if></S_PRI></G_SG32></xsl:for-each></xsl:if><xsl:for-each select="$c/((.)[position() &lt;= 999])"><xsl:variable name="c" select="."/><G_SG39><S_NAD><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f75($c/(.))"/><D_3035><xsl:value-of select="$src"/></D_3035></xsl:if><xsl:if test="$c/(.)"><C_C082><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f76($c/(.))"/><D_3039><xsl:value-of select="$src"/></D_3039></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f77($c/(.))"/><D_3055><xsl:value-of select="$src"/></D_3055></xsl:if></C_C082></xsl:if><xsl:if test="$c/(.)"><C_C058><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f78($c/(.))"/><D_3124><xsl:value-of select="$src"/></D_3124></xsl:if></C_C058></xsl:if></S_NAD></G_SG39></xsl:for-each><xsl:if test="$c/(E1EDP05_gq_RG01/E1EDP05/ALCKZ | E1EDP05_gq_RG01/E1EDP05/KPERC)"><xsl:for-each select="$c/((.)[position() &lt;= 99])"><xsl:variable name="c" select="."/><G_SG43><S_ALC><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f79($c/(.))"/><D_5463><xsl:value-of select="$src"/></D_5463></xsl:if></S_ALC><xsl:if test="$c/(E1EDP05_gq_RG01/E1EDP05/KPERC)"><G_SG45><xsl:variable name="c" select="$c/(.)"/><S_PCD><xsl:variable name="c" select="$c/(.)"/><C_C501><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f80($c/(.))"/><D_5245><xsl:value-of select="$src"/></D_5245></xsl:if><xsl:if test="$c/(./E1EDP05_gq_RG01/E1EDP05/KPERC)"><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f81($c/(.))"/><D_5482><xsl:value-of select="$src"/></D_5482></xsl:if></xsl:if></C_C501></S_PCD></G_SG45></xsl:if></G_SG43></xsl:for-each></xsl:if></G_SG28></xsl:for-each><S_UNS><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f13($c/(.))"/><D_0081><xsl:value-of select="$src"/></D_0081></xsl:if></S_UNS><S_UNT><xsl:variable name="c" select="$c/(.)"/><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f14($c/(.))"/><D_0074><xsl:value-of select="$src"/></D_0074></xsl:if><xsl:if test="true()"><xsl:variable name="src" select="ica_fn:f15($c/(.))"/><D_0062><xsl:value-of select="$src"/></D_0062></xsl:if></S_UNT></M_ORDERS></xsl:template><xsl:function name="ica_fn:f1"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(14)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>1</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f10"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>2</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f100"><xsl:param name="nodes_in"/><xsl:variable name="target_tag_min_length" select="()"/><xsl:variable name="target_tag_max_length" select="()"/><xsl:variable name="target_tag_max_cardinality" select="()"/><xsl:variable name="target_tag_min_cardinality" select="()"/><!--
              begin of snippet code
          --><xsl:sequence select="string-join($nodes_in, ' ')"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f11"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>9</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f12"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>BP</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f13"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(1)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
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
          --></xsl:function><xsl:function name="ica_fn:f14"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(6)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>0</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f15"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(14)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>1</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f16"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05/E1EDP01 -> /ORDERS05/E1EDP01/E1EDP01/POSEX--><xsl:for-each select="E1EDP01/POSEX"><POSEX><xsl:value-of select="."/></POSEX></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(6)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f17"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>1</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f18"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05/E1EDP01 -> /ORDERS05/E1EDP01/E1EDP19_gq_001/IDTNR--><xsl:for-each select="E1EDP19_gq_001/IDTNR"><IDTNR><xsl:value-of select="."/></IDTNR></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="format-number($nodes_in/*,'#')"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f19"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDK02_gq_001/BELNR--><xsl:for-each select="E1EDK02_gq_001/BELNR"><BELNR><xsl:value-of select="."/></BELNR></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f2"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(6)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>ORDERS</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f20"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>2</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f21"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>4</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f22"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>137</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f23"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDK03_gq_002/DATUM--><xsl:for-each select="E1EDK03_gq_002/DATUM"><DATUM><xsl:value-of select="."/></DATUM></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f24"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDK03_gq_012/DATUM--><xsl:for-each select="E1EDK03_gq_012/DATUM"><DATUM><xsl:value-of select="."/></DATUM></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f25"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDK03_gq_011/DATUM--><xsl:for-each select="E1EDK03_gq_011/DATUM"><DATUM><xsl:value-of select="."/></DATUM></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f26"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>102</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f27"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>102</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f28"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>102</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f29"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>ON</value>
                    
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
                  
                      <value>D</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f30"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDK02_gq_001/BELNR--><xsl:for-each select="E1EDK02_gq_001/BELNR"><BELNR><xsl:value-of select="."/></BELNR></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f31"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>4</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f32"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDK02_gq_001/DATUM--><xsl:for-each select="E1EDK02_gq_001/DATUM"><DATUM><xsl:value-of select="."/></DATUM></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f33"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>102</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f34"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>CT</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f35"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDK02_gq_005/BELNR--><xsl:for-each select="E1EDK02_gq_005/BELNR"><BELNR><xsl:value-of select="."/></BELNR></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f36"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>4</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f37"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>102</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f38"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>BY</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f39"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>91</value>
                    
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
          --></xsl:function><xsl:function name="ica_fn:f40"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>PD</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f41"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDKA1_gq_AG/E1EDKA1/ORGTX--><xsl:for-each select="E1EDKA1_gq_AG/E1EDKA1/ORGTX"><ORGTX><xsl:value-of select="."/></ORGTX></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f42"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>TE</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f43"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDKA1_gq_AG/E1EDKA1/TELF1--><xsl:for-each select="E1EDKA1_gq_AG/E1EDKA1/TELF1"><TELF1><xsl:value-of select="."/></TELF1></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(512)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f44"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>FX</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f45"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDKA1_gq_AG/E1EDKA1/TELFX--><xsl:for-each select="E1EDKA1_gq_AG/E1EDKA1/TELFX"><TELFX><xsl:value-of select="."/></TELFX></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(512)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f46"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>EM</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f47"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(512)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>confirmation@company.de</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f48"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>SU</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f49"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>92</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f50"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDKA1_gq_LF/E1EDKA1/PARTN--><xsl:for-each select="E1EDKA1_gq_LF/E1EDKA1/PARTN"><PARTN><xsl:value-of select="."/></PARTN></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f51"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>DP</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f52"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDKA1_gq_WE/E1EDKA1/NAME1--><xsl:for-each select="E1EDKA1_gq_WE/E1EDKA1/NAME1"><NAME1><xsl:value-of select="."/></NAME1></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f53"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDKA1_gq_WE/E1EDKA1/STRAS--><xsl:for-each select="E1EDKA1_gq_WE/E1EDKA1/STRAS"><STRAS><xsl:value-of select="."/></STRAS></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f54"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDKA1_gq_WE/E1EDKA1/ORT01--><xsl:for-each select="E1EDKA1_gq_WE/E1EDKA1/ORT01"><ORT01><xsl:value-of select="."/></ORT01></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f55"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDKA1_gq_WE/E1EDKA1/PSTLZ--><xsl:for-each select="E1EDKA1_gq_WE/E1EDKA1/PSTLZ"><PSTLZ><xsl:value-of select="."/></PSTLZ></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(17)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f56"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDKA1_gq_WE/E1EDKA1/LAND1--><xsl:for-each select="E1EDKA1_gq_WE/E1EDKA1/LAND1"><nodes_in_1><xsl:value-of select="."/></nodes_in_1></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><xsl:variable name="target_tag_name" select="('nodes_out_1')"/><xsl:variable name="target_tag_max_lengths" select="(3)"/><xsl:variable name="target_tag_max_cardinalities" select="(1)"/><xsl:variable name="target_tag_min_cardinalities" select="(0)"/><xsl:variable name="target_tag_names" select="('nodes_out_1')"/><!--
              begin of snippet code
          --><xsl:sequence select="ica_fn:concat_with_space_to_sequence($nodes_in/*, $target_tag_names, $target_tag_max_lengths, $target_tag_min_cardinalities, $target_tag_max_cardinalities)"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f57"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDK01/CURCY--><xsl:for-each select="E1EDK01/CURCY"><nodes_in_1><xsl:value-of select="."/></nodes_in_1></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><xsl:variable name="target_tag_name" select="('nodes_out_1')"/><xsl:variable name="target_tag_max_lengths" select="(3)"/><xsl:variable name="target_tag_max_cardinalities" select="(1)"/><xsl:variable name="target_tag_min_cardinalities" select="(0)"/><xsl:variable name="target_tag_names" select="('nodes_out_1')"/><!--
              begin of snippet code
          --><xsl:sequence select="ica_fn:concat_with_space_to_sequence($nodes_in/*, $target_tag_names, $target_tag_max_lengths, $target_tag_min_cardinalities, $target_tag_max_cardinalities)"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f58"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>5</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f59"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05/E1EDP01 -> /ORDERS05/E1EDP01/E1EDP19_gq_002/IDTNR--><xsl:for-each select="E1EDP19_gq_002/IDTNR"><IDTNR><xsl:value-of select="."/></IDTNR></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f6"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>99B</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f60"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>SA</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f61"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>F</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f62"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>8</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f63"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>92</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f64"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05/E1EDP01 -> /ORDERS05/E1EDP01/E1EDP19_gq_001/KTEXT--><xsl:for-each select="E1EDP19_gq_001/KTEXT"><KTEXT><xsl:value-of select="."/></KTEXT></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(256)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f65"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>21</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f66"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05/E1EDP01 -> /ORDERS05/E1EDP01/E1EDP01/MENGE--><xsl:for-each select="E1EDP01/MENGE"><MENGE><xsl:value-of select="."/></MENGE></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f67"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05/E1EDP01 -> /ORDERS05/E1EDP01/E1EDP01/MENEE--><xsl:for-each select="E1EDP01/MENEE"><MENEE><xsl:value-of select="."/></MENEE></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f68"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>2</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f69"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05/E1EDP01/E1EDP20 -> /ORDERS05/E1EDP01/E1EDP20/EDATU--><xsl:for-each select="EDATU"><EDATU><xsl:value-of select="."/></EDATU></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f7"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>220</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f70"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>102</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f71"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>AAA</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f72"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05/E1EDP01 -> /ORDERS05/E1EDP01/E1EDP01/VPREI--><xsl:for-each select="E1EDP01/VPREI"><VPREI><xsl:value-of select="."/></VPREI></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(15)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f73"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05/E1EDP01 -> /ORDERS05/E1EDP01/E1EDP01/PEINH--><xsl:for-each select="E1EDP01/PEINH"><PEINH><xsl:value-of select="."/></PEINH></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(9)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f74"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05/E1EDP01 -> /ORDERS05/E1EDP01/E1EDP01/PMENE--><xsl:for-each select="E1EDP01/PMENE"><PMENE><xsl:value-of select="."/></PMENE></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f75"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>YE</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f76"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>EDC</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f77"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>91</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f78"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>Company DPU</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f79"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05/E1EDP01 -> /ORDERS05/E1EDP01/E1EDP05_gq_RG01/E1EDP05/ALCKZ--><xsl:for-each select="E1EDP05_gq_RG01/E1EDP05/ALCKZ"><nodes_in_1><xsl:value-of select="."/></nodes_in_1></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          --><xsl:sequence select="ica_fn:m_s_2b087eaae2df30ca8a2236237ade7d8c_t_2ca84a71b71f3693babca65986c004a9($nodes_in/nodes_in_1)"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f8"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>9</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f80"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>1</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f81"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05/E1EDP01 -> /ORDERS05/E1EDP01/E1EDP05_gq_RG01/E1EDP05/KPERC--><xsl:for-each select="E1EDP05_gq_RG01/E1EDP05/KPERC"><KPERC><xsl:value-of select="."/></KPERC></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(10)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="replace($nodes_in/*,'-','')"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f82"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDP01/E1EDP01/WERKS--><xsl:for-each select="E1EDP01/E1EDP01/WERKS"><WERKS><xsl:value-of select="."/></WERKS></xsl:for-each><!--/ORDERS05 -> /ORDERS05/E1EDP01/E1EDP01/LGORT--><xsl:for-each select="E1EDP01/E1EDP01/LGORT"><LGORT><xsl:value-of select="."/></LGORT></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          --><xsl:sequence select="concat($nodes_in/WERKS[1],'-',$nodes_in/LGORT[1])"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f84"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDK02_gq_005/DATUM--><xsl:for-each select="E1EDK02_gq_005/DATUM"><DATUM><xsl:value-of select="."/></DATUM></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f89"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDKA1_gq_WE/E1EDKA1/NAME2--><xsl:for-each select="E1EDKA1_gq_WE/E1EDKA1/NAME2"><NAME2><xsl:value-of select="."/></NAME2></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f9"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDKA1_gq_AG/E1EDKA1/PARTN--><xsl:for-each select="E1EDKA1_gq_AG/E1EDKA1/PARTN"><PARTN><xsl:value-of select="."/></PARTN></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f90"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05 -> /ORDERS05/E1EDKA1_gq_WE/E1EDKA1/NAME3--><xsl:for-each select="E1EDKA1_gq_WE/E1EDKA1/NAME3"><NAME3><xsl:value-of select="."/></NAME3></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(35)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(0)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f95"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>COI</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f96"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>de</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f97"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><!--/ORDERS05/E1EDP01 -> /ORDERS05/E1EDK01/EMPST--><xsl:for-each select="../E1EDK01/EMPST"><EMPST><xsl:value-of select="."/></EMPST></xsl:for-each></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(512)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          --><xsl:sequence select="$nodes_in/*"/><!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f98"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>AAB</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:f99"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in/></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="(1)"/><xsl:variable name="target_tag_max_length" select="(3)"/><xsl:variable name="target_tag_max_cardinality" select="(1)"/><xsl:variable name="target_tag_min_cardinality" select="(1)"/><!--
              begin of snippet code
          -->
            <xsl:variable a:constant="true" as="element(nodes_in)*" name="nodes_in">
              <xsl:for-each select="$nodes_in">
                <xsl:copy>
                  <xsl:sequence select="*"/>
                  
                      <value>de</value>
                    
                </xsl:copy>
              </xsl:for-each>
            </xsl:variable>
            <xsl:value-of select="$nodes_in/value"/>
            <!--
              end of snippet code
          --></xsl:function><xsl:function name="ica_fn:m_s_2b087eaae2df30ca8a2236237ade7d8c_t_2ca84a71b71f3693babca65986c004a9"><xsl:param name="ica_fn:ctxt"/><xsl:variable as="element(nodes_in)*" name="nodes_in"><xsl:for-each select="$ica_fn:ctxt"><nodes_in><nodes_in_1><xsl:value-of select="."/></nodes_in_1></nodes_in></xsl:for-each></xsl:variable><xsl:variable name="target_tag_min_length" select="()"/><xsl:variable name="target_tag_max_length" select="()"/><xsl:variable name="target_tag_max_cardinality" select="()"/><xsl:variable name="target_tag_min_cardinality" select="()"/><!--
              begin of snippet code
          --><xsl:variable name="value" select="$nodes_in/nodes_in_1"/><xsl:choose><xsl:when test="$value = '+'"><xsl:sequence select="'C'"/></xsl:when><xsl:when test="$value = '-'"><xsl:sequence select="'A'"/></xsl:when><xsl:otherwise><xsl:sequence select="$value               "/></xsl:otherwise></xsl:choose><!--
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