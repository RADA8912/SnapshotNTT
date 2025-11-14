/*
Copyright 2018 - Ariel Bravo Ayala - ariel.bravo[a]gmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.mapping.ValueMappingApi;
import groovy.util.XmlSlurper;

def Message processIDOC(Message msg) {
    //Runtime
    def runtimeAgency = 'ENVIRONMENT_IDOC';
    def srcAg = 'IDOC';
    def tgtAg = 'PROCESSDIRECT';
    def fieldSeparator = '|';
    def helperValMap = ITApiFactory.getApi(ValueMappingApi.class, null);
    
    //Get runtime parameters - IDOC
    def controlSegmentFieldsStr = helperValMap.getMappedValue(runtimeAgency, 'PARAMETER', 'controlSegmentFields' , runtimeAgency, 'VALUE');
    if (controlSegmentFieldsStr == null){ throw new Exception("NF: Control Segment Fields") }
    def controlSegmentFieldsArr = controlSegmentFieldsStr.tokenize(fieldSeparator);

    //Analyse Idoc Control Segment
    def body = msg.getBody(java.lang.String);
    def xml  = new XmlSlurper().parseText(body);
    
    def keySender = [];
    controlSegmentFieldsArr.each{
        keySender.add(xml.IDOC.EDI_DC40."${it}".text());
    }
    keySender = keySender.join(fieldSeparator);
    
    //Get Receiver Address
    def keyReceiver = helperValMap.getMappedValue(srcAg, 'SENDER', keySender , tgtAg, 'RECEIVER');

    //Set Propertie - Receiver Address 
    def map = msg.getProperties();
    msg.setProperty("receiverAddr", keyReceiver);
    return msg;
}

