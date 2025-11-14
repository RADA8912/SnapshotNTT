import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.XmlUtil;

//MessageTransformBean
//Version 1.0.0

def Message processData(Message message)
{
    Map<String, Object> props = message.getProperties();
    String conversionType = props.get("xml.conversionType") as String;
    //Config config = new Config(message, props);
    String resPayload;
    String body = message.getBody(String) as String;

    switch(conversionType) {
        case "SimplePlain2XML":
            //def reader = message.getBody(java.io.Reader);
            ConfigSimple cSimple = new ConfigSimple(message, props);
            def simplePlain2XML = new SimplePlain2XML(cSimple);
            String[] lines = body.tokenize(cSimple.endSeparator);
            simplePlain2XML.handleFieldNames(lines[0]);
            resPayload = simplePlain2XML.buildPayload(lines);
            break;
        case "SimpleXML2Plain":
            ConfigSimple cSimple = new ConfigSimple(message, props);
            def simpleXML2Plain = new SimpleXML2Plain(cSimple);
            Node root = new XmlParser().parseText(body);
            simpleXML2Plain.handleFieldNames(root);
            resPayload = simpleXML2Plain.buildPayload(root);
            break;
        case "StructPlain2XML":
            ConfigStruct cStruct = new ConfigStruct(message, props);
            def structurePlain2XML = new StructurePlain2XML(cStruct);
            String[] lines = body.tokenize(cStruct.endSeparator);
            List<String> headerLines = [];
            //StructPlain files have multiple header lines (1 for each object)
            if (cStruct.processFieldNames == "fromFile" ||
                    cStruct.processFieldNames == "fromFileWithBlankLine"){
                for (int i = 0; i < structurePlain2XML.configStruct.recordsetStructureList.size(); i++){
                    headerLines.add(lines[i].trim());
                }
            }
            structurePlain2XML.handleFieldNames(headerLines);
            resPayload = structurePlain2XML.buildPayload(lines);
            break;
        case "StructXML2Plain":
            ConfigStruct cStruct = new ConfigStruct(message, props);
            def structureXML2Plain = new StructureXML2Plain(cStruct);
            Node root = new XmlParser().parseText(body);
            structureXML2Plain.handleFieldNames(root);
            resPayload = structureXML2Plain.buildPayload(root);
            break;
        default:
            throw new Exception("Error! xml.conversionType \'" + conversionType + "\' is not supported.");
    }

    message.setBody(resPayload);
    return message;
}

class Transformation {

    Transformation() {

    }

    static String fixedLengthString(String inputString, int targetLength, String fixedLengthHandling) {
        if (inputString.length() > targetLength) {
            if (fixedLengthHandling == "IGNORE") {
                return inputString;
            } else if (fixedLengthHandling == "CUT"){
                return inputString.substring(0, targetLength);
            }
            else {
                throw new Exception("Error! fixedLengthTooShortHandling - String '$inputString' length (" + inputString.length() + ") is bigger than expected ($targetLength).");
            }
        }
        else {
            return inputString.padRight(targetLength);
        }
    }

    static String xmlToStringClean(Config config, Node root){
        def xmlString = XmlUtil.serialize(root);
        if (!config.addXMLDeclaration){
            xmlString = xmlString.replaceFirst('<\\?xml version="1.0" encoding="UTF-8"\\?>', '')
        }
        else{
            xmlString = xmlString.replaceFirst('<\\?xml version="1.0" encoding="UTF-8"\\?>', '<\\?xml version="1.0" encoding="UTF-8"\\?>\n')
        }
        xmlString = xmlString.replaceAll(/\r\n.*?\r\n/, "\n").trim();
        return xmlString;
    }

}

class SimplePlain2XML extends Transformation {
    ConfigSimple configSimple;

    SimplePlain2XML(ConfigSimple configs) {
        this.configSimple = configs;
        configs.validateFieldLimiter();
    }

    void handleFieldNames(String headerLine) {
        configSimple.fieldNames = [];
        if (configSimple.processFieldNames == "fromConfiguration") {
            configSimple.fieldNames =
                    configSimple.getLineValuesCleaned(configSimple.props.get("xml.fieldNames"), ",");
        }
        else if (configSimple.processFieldNames == "fromFile" ||
                configSimple.processFieldNames == "fromFileWithBlankLine") {
            //fromFile or fromFileWithBlankLine
            configSimple.skip = configSimple.processFieldNames == "fromFile" ? 1 : 2;

            if (configSimple.fieldSeparator != '') {
                configSimple.fieldNames =
                        configSimple.getLineValuesCleaned(headerLine, configSimple.fieldSeparator);
            }
            else {
                int offset = 0;
                ArrayList tempList = [];
                for (int k = 0; k < configSimple.fieldFixedLengths.length; k++)
                {
                    int ffl = configSimple.fieldFixedLengths[k];
                    int finalSize = (headerLine.length() < offset + ffl) &
                            (k == configSimple.fieldFixedLengths.length - 1) ? headerLine.length() : offset + ffl;
                    String headerValue = headerLine.substring(offset, finalSize).trim();
                    //String headerValue = headerLine.substring(offset, offset + ffl).trim();
                    offset += ffl;
                    tempList.add(headerValue);
                }
                configSimple.fieldNames = tempList.toArray(new String[0]);
            }
        }
        else {
            //notAvailable - dummy names like 'Column1', 'Column2';
            ArrayList tempList = [];
            String dummyColumnName = "Column";
            int size = configSimple.fieldSeparator != '' ?
                    configSimple.getLineValuesCleaned(headerLine, configSimple.fieldSeparator).size() :
                    configSimple.fieldFixedLengths.size();

            for (int i = 1; i <= size; i++) {
                tempList.add(dummyColumnName + i);
            }
            configSimple.fieldNames = tempList.toArray(new String[0]);
        }
    }

    public String buildPayload(String[] lines) {
        Node xml;
        if (configSimple.documentNamespace != null && configSimple.documentNamespace != '')
            xml = new Node(null,"ns1:$configSimple.documentName",["xmlns:ns1":configSimple.documentNamespace])
        else
            xml = new Node(null,configSimple.documentName);

        if (configSimple.fieldSeparator != '') {
            //Build by Separator
            for (int i = configSimple.skip; i < lines.length; i++)
            {
                String line = lines[i];
                def lineValues = configSimple.getLineValues(line, configSimple.fieldSeparator);
                Node record = new Node(xml, configSimple.structTitle);
                for (int k = 0; k < configSimple.fieldNames.length; k++)
                {
                    String nodeName = configSimple.fieldNames[k].trim();
                    String nodeValue = configSimple.cleanLineValue(lineValues[k]);
                    new Node(record, nodeName, nodeValue);
                }
            }

        }
        else {
            //Build by fieldFixedLengths
            for (int i = configSimple.skip; i < lines.length; i++)
            {
                String line = lines[i];
                Node record = new Node(xml, configSimple.structTitle);
                int offset = 0;
                for (int k = 0; k < configSimple.fieldNames.length; k++)
                {
                    Integer ffl = configSimple.fieldFixedLengths[k];
                    String nodeValue = line.substring(offset, offset + ffl).trim();
                    offset += ffl;
                    String nodeName = configSimple.fieldNames[k];
                    new Node(record, nodeName, nodeValue);
                }
            }
        }

        return xmlToStringClean(configSimple, xml);
    }
}

class SimpleXML2Plain extends Transformation {
    ConfigSimple configSimple;

    SimpleXML2Plain(ConfigSimple configSimple) {
        this.configSimple = configSimple;
        configSimple.validateFieldLimiter();
    }

    void handleFieldNames(Node root) {
        configSimple.fieldNames = [];
        configSimple.skip = (configSimple.addHeaderLine == "2" || configSimple.addHeaderLine == "4") ? 1 : 0;

        if (configSimple.addHeaderLine == "3" || configSimple.addHeaderLine == "4") {
            //3 - Header line is stored as NameA.headerLine
            //4 – As for 3, followed by a blank line
            configSimple.fieldNames = configSimple.props.get("xml.headerLine").tokenize(",") as String[];
        }
        else if (configSimple.addHeaderLine == "1" || configSimple.addHeaderLine == "2") {
            //1 – Header line with column names from the XML document;
            //2 – As for 1, followed by a blank line
            ArrayList tempList = [];

            root.children().first().children().each { field ->
                String nodeValue = field.name();
                tempList.add(nodeValue);
            }
            configSimple.fieldNames = tempList.toArray(new String[0]);
        }
    }

    def String buildPayload(Node root) {
        def plainText = new StringBuilder();

        // Write header row
        if (configSimple.addHeaderLine != "0") {
            if (configSimple.fieldSeparator != '') {
                //Build by Separator
                plainText.append(configSimple.fieldNames.join(configSimple.fieldSeparator));
            }
            else {
                //Build by fieldFixedLengths
                int index = 0;
                configSimple.fieldNames.each { field ->
                    int ffl = Integer.valueOf(configSimple.fieldFixedLengths[index]);
                    String nodeValue = fixedLengthString(field, ffl, configSimple.fixedLengthTooShortHandling);
                    plainText.append(nodeValue);
                    index++;
                }
            }
            plainText.append(configSimple.endSeparator);
        }

        for (int i = 0; i < configSimple.skip; i++){
            plainText.append(configSimple.endSeparator);
        }

        // Write data rows
        if (configSimple.fieldSeparator != '') {
            //Build by Separator
            root.children().each { row ->
                row.children().each { field ->
                    String nodeValue = field.text();
                    plainText.append(nodeValue);
                    if (field != row.children().last()) {
                        plainText.append(configSimple.fieldSeparator);
                    }
                }
                plainText.append(configSimple.endSeparator);
            }
        }
        else {
            //Build by fieldFixedLengths
            root.children().each { row ->
                int index = 0;
                row.children().each { field ->
                    int ffl = Integer.valueOf(configSimple.fieldFixedLengths[index]);
                    String nodeValue = fixedLengthString(field.text(), ffl, configSimple.fixedLengthTooShortHandling);
                    plainText.append(nodeValue);
                    index++;
                }
                plainText.append(configSimple.endSeparator);
            }
        }
        return plainText.toString();
    }

}

class StructurePlain2XML extends Transformation {
    ConfigStruct configStruct;

    StructurePlain2XML(ConfigStruct configStruct) {
        this.configStruct = configStruct;
    }

    void handleFieldNames(List<String> headerLines) {
        if (configStruct.processFieldNames == "fromConfiguration") {
            for (int i = 0; i < configStruct.recordsetStructureList.size(); i++) {
                RecordSetStructure recordSS = configStruct.recordsetStructureList[i];
                def temp = configStruct.props.get("xml." + recordSS.name + ".fieldNames");
                if (temp != null) {
                    recordSS.setFieldNames(configStruct.getLineValuesCleaned(temp, ","));
                }
                else
                    throw new Exception("Error! xml.$recordSS.name .fieldNames must be provided.");

                if (configStruct.keyFieldRequired) {
                    recordSS.setKeyFieldValue(configStruct.props.get("xml." + recordSS.name + ".keyFieldValue") as String);
                    recordSS.setKeyFieldIndex(recordSS.fieldNames.findIndexOf { it.contains(configStruct.keyFieldName) });
                    recordSS.setAddKeyField(configStruct.props.get("xml." + recordSS.name + ".keyFieldInStructure") != "ignore");

                    int tempOffset = 0;
                    if (recordSS.fieldSeparator == '') {
                        for (int k = 0; k < recordSS.keyFieldIndex; k++)
                            tempOffset += recordSS.fieldFixedLengths[k];
                        recordSS.setKeyFieldOffset(tempOffset);
                    }
                }
            }
        }
        else if (configStruct.processFieldNames == "fromFile" ||
                configStruct.processFieldNames == "fromFileWithBlankLine") {
            //fromFile or fromFileWithBlankLine
            configStruct.skip = configStruct.processFieldNames == "fromFile" ? headerLines.size() : headerLines.size() + 1;

            for (int i = 0; i < configStruct.recordsetStructureList.size(); i++) {
                RecordSetStructure recordSS = configStruct.recordsetStructureList[i];
                String headerLine = headerLines[i];
                if (recordSS.fieldSeparator != '') {
                    recordSS.setFieldNames(configStruct.getLineValuesCleaned(headerLine, recordSS.fieldSeparator));
                } else {
                    int offset = 0;
                    ArrayList tempList = [];

                    for (int k = 0; k < recordSS.fieldFixedLengths.length; k++) {
                        int ffl = recordSS.fieldFixedLengths[k];
                        int finalSize = (headerLine.length() < offset + ffl) &
                                (k == recordSS.fieldFixedLengths.length - 1) ? headerLine.length() : offset + ffl;
                        String headerValue = headerLine.substring(offset, finalSize).trim();
                        //String headerValue = headerLine.substring(offset, offset + ffl).trim();
                        offset += ffl;
                        tempList.add(headerValue);
                    }
                    recordSS.fieldNames = tempList.toArray(new String[0]);
                }

                if (configStruct.keyFieldRequired) {
                    recordSS.setKeyFieldValue(configStruct.props.get("xml." + recordSS.name + ".keyFieldValue") as String);
                    recordSS.setKeyFieldIndex(recordSS.fieldNames.findIndexOf { it.contains(configStruct.keyFieldName) });
                    recordSS.setAddKeyField(configStruct.props.get("xml." + recordSS.name + ".keyFieldInStructure") != "ignore");

                    int tempOffset = 0;
                    if (recordSS.fieldSeparator == '') {
                        for (int k = 0; k < recordSS.keyFieldIndex; k++)
                            tempOffset += recordSS.fieldFixedLengths[k];
                        recordSS.setKeyFieldOffset(tempOffset);
                    }
                }
            }
        }
        else {
            throw new Exception("Error! xml.processFieldNames = notAvailable is not yet supported fro StructurePlain2XML!");
            //notAvailable - dummy names like 'Column1', 'Column2';
        }
    }

    public String buildPayload(String[] lines) {
        Node xml;
        if (configStruct.documentNamespace != null && configStruct.documentNamespace != '')
            xml = new Node(null,"ns1:$configStruct.documentName",["xmlns:ns1":configStruct.documentNamespace])
        else
            xml = new Node(null, configStruct.documentName);

        int previousLineIndex = configStruct.skip;
        Node previousRecordSet;
        Node recordSet;
        Node record;
        int previousRecordAddedIndex = -1;

        while (previousLineIndex < lines.length) {
            int previousKeyIndex = 0;   //to help identify when a new recordsetName starts
            int rssCounter;
            previousRecordSet = recordSet;
            boolean recordSetCreated = false;
            if (recordSet == null || recordSet.children().size() > 0) {
                recordSet = xml;
                if (!configStruct.ignoreRecordSetName) {
                    recordSetCreated = true;
                    if (configStruct.recordsetNamespace != null && configStruct.recordsetNamespace != '')
                        recordSet = new Node(xml, "ns2:$configStruct.recordsetName", ["xmlns:ns2": configStruct.recordsetNamespace])
                    else
                        recordSet = new Node(xml, configStruct.recordsetName)
                }
            }
            for (int indexLine = previousLineIndex; indexLine < lines.length; indexLine++) {
                previousLineIndex = indexLine;  //to not start reading the file from the beginning again
                String line = lines[indexLine];
                boolean match = false;
                rssCounter = 0;
                for (int indexRec = previousKeyIndex; indexRec < configStruct.recordsetStructureList.size(); indexRec++) {
                    previousKeyIndex = indexRec;
                    RecordSetStructure recordSS = configStruct.recordsetStructureList[indexRec];
                    rssCounter++;
                    if (recordSS.fieldSeparator != '') {
                        //Build by Separator
                        String[] lineValues = configStruct.getLineValues(line, recordSS.fieldSeparator);
                        String tempKeyLine = lineValues[recordSS.keyFieldIndex].trim();
                        if (!configStruct.keyFieldRequired || tempKeyLine == recordSS.keyFieldValue) {
                            if (!configStruct.ignoreRecordSetName &&
                                    previousRecordSet != null &&
                                    recordSetCreated &&
                                    previousRecordAddedIndex != -1 &&
                                    previousRecordAddedIndex <= recordSS.index &&
                                    configStruct.recordsetStructureList[previousRecordAddedIndex].occur != '1'){
                                xml.remove(recordSet);
                                recordSet = previousRecordSet;
                            }
                            recordSetCreated = false;
                            previousRecordAddedIndex = recordSS.index;

                            record = new Node(recordSet, recordSS.name);
                            for (int k = 0; k < recordSS.fieldNames.length; k++) {
                                String nodeValue = "";
                                if (k < lineValues.length) {
                                    nodeValue = configStruct.cleanLineValue(lineValues[k]);
                                }
                                else if(recordSS.missingLastFields == "ERROR"){
                                    throw new Exception("Error! Atleast one record for $recordSS.name does not contain all expected fields!");
                                }
                                else if(recordSS.missingLastFields == "IGNORE"){
                                    continue;
                                }
                                //adds if k < lineValues.length or missingLastFields = "ADD"
                                String nodeName = recordSS.fieldNames[k];
                                if (nodeName != configStruct.keyFieldName || recordSS.addKeyField)
                                    new Node(record, nodeName, nodeValue)
                            }
                            if (recordSS.occur == '1')
                                previousKeyIndex++;
                            match = true;
                            break;
                        }
                    }
                    else{
                        //Build by fieldFixedLengths
                        String tempKeyLine = line.substring(recordSS.keyFieldOffset);
                        if (!configStruct.keyFieldRequired || tempKeyLine.startsWith(recordSS.keyFieldValue)) {
                            if (!configStruct.ignoreRecordSetName &&
                                    previousRecordSet != null &&
                                    recordSetCreated &&
                                    previousRecordAddedIndex != -1 &&
                                    previousRecordAddedIndex <= recordSS.index &&
                                    configStruct.recordsetStructureList[previousRecordAddedIndex].occur != '1'){
                                xml.remove(recordSet);
                                recordSet = previousRecordSet;
                            }
                            recordSetCreated = false;
                            previousRecordAddedIndex = recordSS.index;

                            record = new Node(recordSet, recordSS.name)
                            int offset = 0;
                            for (int k = 0; k < recordSS.fieldNames.length; k++) {
                                Integer ffl = recordSS.fieldFixedLengths[k];
                                int finalSize = (line.length() < offset + ffl) ? line.length() : offset + ffl;
                                String nodeValue;
                                if (offset < line.length()) {
                                    nodeValue = line.substring(offset, finalSize).trim();
                                    offset += ffl;
                                }
                                else if (recordSS.missingLastFields == "ADD"){
                                    nodeValue = ' ' * ffl;
                                }
                                else if(recordSS.missingLastFields == "ERROR"){
                                    throw new Exception("Error! Atleast one record for $recordSS.name does not contain all expected fields!");
                                }
                                else{
                                    continue;
                                }

                                String nodeName = recordSS.fieldNames[k];
                                if (nodeName != configStruct.keyFieldName || recordSS.addKeyField)
                                    new Node(record, nodeName, nodeValue)
                            }
                            if (recordSS.occur == '1')
                                previousKeyIndex++;
                            match = true;
                            break;
                        }
                    }
                }
                if (rssCounter ==  configStruct.recordsetStructureList.size() && !match) {
                    previousLineIndex++;
                }
                if (!match)
                    break;
                else
                    previousLineIndex++;
            }
        }

        if (recordSet != null && recordSet.children().size() == 0)
            xml.remove(recordSet);

        if (configStruct.recordsetsPerMessage != 0){
            //split the xml into X different ones and save in different properties
            Node xmlTemp;
            int docCounter = 1;
            for(int i = 0; i < xml.children().size(); i++){
                if (xmlTemp == null){
                    if (configStruct.documentNamespace != null && configStruct.documentNamespace != '')
                        xmlTemp = new Node(null,"ns1:$configStruct.documentName",["xmlns:ns1":configStruct.documentNamespace])
                    else
                        xmlTemp = new Node(null,configStruct.documentName);
                }
                Node recTemp = xml.children()[i];
                xmlTemp.append(recTemp);
                if (xmlTemp.children().size() == configStruct.recordsetsPerMessage){
                    String xmlString = xmlToStringClean(configStruct, xmlTemp);
                    configStruct.message.setProperty(configStruct.propDefaultName + docCounter, xmlString);
                    docCounter++;
                    xmlTemp = null;
                }
            }
            if (xmlTemp != null){
                String xmlString = xmlToStringClean(configStruct, xmlTemp);
                configStruct.message.setProperty(configStruct.propDefaultName + docCounter, xmlString);
            }
        }

        return xmlToStringClean(configStruct, xml);
    }

}

class StructureXML2Plain extends Transformation{
    ConfigStruct configStruct;

    StructureXML2Plain(ConfigStruct configStruct) {
        this.configStruct = configStruct;
        //configs.validateFieldLimiter();
    }

    void handleFieldNames(Node root){
        configStruct.skip = (configStruct.addHeaderLine == "2" || configStruct.addHeaderLine == "4") ? 1 : 0;

        if (configStruct.addHeaderLine == "3" || configStruct.addHeaderLine == "4") {
            //3 - Header line is stored as NameA.headerLine
            //4 – As for 3, followed by a blank line
            for (int i = 0; i < configStruct.recordsetStructureList.size(); i++) {
                RecordSetStructure recordSS = configStruct.recordsetStructureList[i];
                def temp = configStruct.props.get("xml." + recordSS.name + ".fieldNames");
                if (temp != null)
                    recordSS.setFieldNames(temp.tokenize(",") as String[]);
                else
                    throw new Exception("Error! xml.$recordSS.name .fieldNames must be provided.");
            }
        }
        else if (configStruct.addHeaderLine == "1" || configStruct.addHeaderLine == "2") {
            //1 – Header line with column names from the XML document;
            //2 – As for 1, followed by a blank line

            for (int iRss = 0; iRss < configStruct.recordsetStructureList.size(); iRss++){
                RecordSetStructure rss = configStruct.recordsetStructureList[iRss];
                if (root.children().first().name() == configStruct.recordsetName){
                    //a recordSet exists, need to loop from them
                    for (int iRecord = 0; iRecord < root.children().size(); iRecord++){
                        Node recordSet = root.children()[iRecord];
                        if (collectFieldNamesFromFile(rss, recordSet))
                            break;//iRecord
                    }
                }
                else{
                    //a recordSet does not exist, the field are at this level
                    collectFieldNamesFromFile(rss, root);
                }
            }
        }
    }

    def String buildPayload(Node root){
        StringBuilder plainText = new StringBuilder();

        // Write header row
        if (configStruct.addHeaderLine != "0") {
            for (int i = 0; i < configStruct.recordsetStructureList.size(); i++) {
                RecordSetStructure rss = configStruct.recordsetStructureList[i];
                if (rss.fieldSeparator != '') {
                    //Build by Separator
                    plainText.append(rss.fieldNames.join(rss.fieldSeparator));
                } else {
                    //Build by fieldFixedLengths
                    int index = 0;
                    rss.fieldNames.each { field ->
                        int ffl = Integer.valueOf(rss.fieldFixedLengths[index]);
                        String nodeValue = fixedLengthString(field, ffl, rss.fixedLengthTooShortHandling);
                        plainText.append(nodeValue);
                        index++;
                    }
                }
                plainText.append(configStruct.endSeparator);
            }
        }

        for (int i = 0; i < configStruct.skip; i++){
            plainText.append(configStruct.endSeparator);
        }

        // Write data rows
        root.children().each { recordSetNode ->
            if (recordSetNode.name() == configStruct.recordsetName){
                recordSetNode.children().each { record ->
                    //match target RSS
                    writePlainText(plainText, record as Node);
                }
            }
            else{
                writePlainText(plainText, recordSetNode as Node);
            }
        }

        return plainText.toString();
    }

    private void writePlainText(StringBuilder plainText, Node record){
        def rssResult = configStruct.recordsetStructureList.findAll { it.name == record.name() };
        if (rssResult.size() > 0) {
            RecordSetStructure rss = rssResult[0];
            if (rss.fieldSeparator != '') {
                record.children().each { field ->
                    String nodeValue = field.text();
                    plainText.append(nodeValue);
                    if (field != record.children().last()) {
                        plainText.append(rss.fieldSeparator);
                    }
                }
            }
            else{
                int index = 0;
                record.children().each { field ->
                    int ffl = rss.fieldFixedLengths[index];
                    String nodeValue = fixedLengthString(field.text() as String, ffl, rss.fixedLengthTooShortHandling);
                    plainText.append(nodeValue);
                    index++
                }
            }
            plainText.append(configStruct.endSeparator);
        }
    }

    //return true if found
    private static Boolean collectFieldNamesFromFile(RecordSetStructure rss, Node recordSet){
        for (int iEntry = 0; iEntry < recordSet.children().size(); iEntry++){
            Node entry = recordSet.children()[iEntry];
            if (entry.name() == rss.name){
                List<String> tempFields = [];
                for (int iField = 0; iField < entry.children().size(); iField++) {
                    Node field = entry.children()[iField];
                    tempFields.add(field.name() as String);
                }
                rss.fieldNames = tempFields.toArray();
                return true;
            }
        }
        return false;
    }

}

class Config {
    Message message;
    HashMap props;
    String conversionType;
    String processFieldNames;
    String endSeparator;
    String documentName;
    String documentNamespace;
    Integer skip;
    //XML2Plain
    String addHeaderLine;
    Boolean addXMLDeclaration;
    String enclosureSign;
    Boolean enclosureConversion;

    Config(Message message, HashMap props){
        this.message = message;
        this.props = props;

        conversionType = props.get("xml.conversionType") as String;
        processFieldNames = props.get("xml.processFieldNames") as String;
        //processFieldNames can be (required):
        //fromFile - at header line;
        //fromFileWithBlankLine - at header line and ignore next line;
        //fromConfiguration - from property 'xml.fieldNames';
        //notAvailable - dummy names like 'Column1', 'Column2';

        def tempOffset = props.get("xml.documentOffset") as Integer;
        skip = tempOffset != null ? tempOffset : 0;
        def endSeparatorTemp = props.get("xml.endSeparator") as String;
        endSeparator = (endSeparatorTemp != '' && endSeparatorTemp != null) ? endSeparatorTemp : '\n';
        def documentNameTemp = props.get("xml.documentName") as String;
        documentName = (documentNameTemp != '' && documentNameTemp != null) ? documentNameTemp : "root";
        documentNamespace = props.get("xml.documentNamespace") as String;
        def addHeaderLineTemp = props.get("xml.addHeaderLine") as String;
        addHeaderLine = (addHeaderLineTemp != null && addHeaderLineTemp != '') ? addHeaderLineTemp : "0";
        def addXMLDeclarationTemp = props.get("xml.addXMLDeclaration") as String;
        addXMLDeclaration = addXMLDeclarationTemp == 'true';
        enclosureSign = props.get("xml.enclosureSign") as String;
        def enclosureConversionTemp = props.get("xml.enclosureConversion") as String;
        enclosureConversion = enclosureConversionTemp == 'yes';
        validateConversionType();
        //validateProcessFieldNames();
    }

    String[] getLineValuesCleaned(String line, String fieldSeparator){
        String[] lineValues;
        if (enclosureConversion) {
            def tempList = extractValues(line, fieldSeparator);
            tempList = tempList.collect { cleanLineValue(it); };
            lineValues = tempList.toArray(new String[0]);
        }
        else{
            //lineValues = line.split(fieldSeparator) as String[];
            lineValues = extractValues(line, fieldSeparator).toArray(new String[0]);
        }
        return lineValues;
    }

    String[] getLineValues(String line, String fieldSeparator){
        String[] lineValues;
        if (enclosureConversion) {
            def tempList = extractValues(line, fieldSeparator);
            lineValues = tempList.toArray(new String[0]);
        }
        else{
            //lineValues = line.split(fieldSeparator) as String[];
            lineValues = extractValues(line, fieldSeparator).toArray(new String[0]);
        }
        return lineValues;
    }

    String cleanLineValue(String lineValue){
        String nodeValue;
        if (enclosureConversion) {
            if (lineValue.startsWith(enclosureSign) && lineValue.endsWith(enclosureSign)) {
                lineValue = lineValue.substring(enclosureSign.length());
                lineValue = lineValue.substring(0, lineValue.length() - enclosureSign.length());
            }
        }
        nodeValue = lineValue.trim();
        return nodeValue;
    }

    private def extractValues(String line, String fieldSeparator){
        def parts = []  // This array will store the parsed parts of the input string
        String tempPart = ''  // This variable will store a part that may contain multiple semicolons

        // Loop through the input string character by character
        boolean insideQuote = false  // Flag to track if the current character is inside double quotes
        for(int i = 0; i < line.length(); i++) {
            char c = line[i];
            if (c == fieldSeparator && !insideQuote) {
                // If the current character is a semicolon and we are not inside double quotes, add the current temp part to the parts array
                parts.add(tempPart)
                tempPart = ''  // Reset the temp part
            } else {
                tempPart += c  // Add the current character to the temp part
                if (c == enclosureSign && enclosureConversion) {
                    insideQuote = !insideQuote  // Toggle the insideQuote flag when encountering double quotes
                }
            }
        }
        // Add the last temp part to the parts array
        parts.add(tempPart)
        return parts;
    }

    private void validateConversionType() {
        if (conversionType != "SimplePlain2XML" &&
                conversionType != "SimpleXML2Plain" &&
                conversionType != "StructPlain2XML" &&
                conversionType != "StructXML2Plain") {
            throw new Exception("Error! xml.conversionType \'" + conversionType + "\' is not supported.");
        }
    }

    private void validateProcessFieldNames() {
        if (processFieldNames != "fromFile" &&
                processFieldNames != "fromFileWithBlankLine" &&
                processFieldNames != "fromConfiguration" &&
                processFieldNames != "notAvailable") {
            throw new Exception("Error! xml.processFieldNames \'" + processFieldNames + "\' is not supported.");
        }
    }

}

class ConfigSimple extends Config {
    String structTitle;
    String fieldSeparator;
    Integer[] fieldFixedLengths;
    String[] fieldNames;
    String fixedLengthTooShortHandling;

    ConfigSimple(Message message, HashMap props) {
        super(message, props);
        fieldNames = []; // populated in handleFieldNames()
        def tempFieldFixedLengths = props.get("xml.fieldFixedLengths");
        fieldFixedLengths = tempFieldFixedLengths != null ? tempFieldFixedLengths.tokenize(",") as Integer[] : [] as Integer[];
        def fieldSeparatorTemp = props.get("xml.fieldSeparator");
        fieldSeparator = fieldSeparatorTemp != null ? fieldSeparatorTemp : '';
        //fieldSeparator = fieldSeparatorTemp != null ? Pattern.quote(fieldSeparatorTemp) : '';
        def structTitleTemp = props.get("xml.structureTitle") as String;
        structTitle = (structTitleTemp != '' && structTitleTemp != null) ? structTitleTemp : "row";
        setFixedLengthTooShortHandling(props.get("xml.fixedLengthTooShortHandling") as String);

    }

    void validateFieldLimiter(){
        if (fieldFixedLengths == [''] &&
                (fieldSeparator == '' || fieldSeparator == null)) {
            throw new Exception("Error! xml.fieldFixedLengths or xml.fieldSeparator must be provided.");
        }
    }

    void setFixedLengthTooShortHandling(String fixedLengthTooShortHandling){
        if (fixedLengthTooShortHandling != null) {
            fixedLengthTooShortHandling = fixedLengthTooShortHandling.toUpperCase();
            if (fixedLengthTooShortHandling == "CUT" || fixedLengthTooShortHandling == "IGNORE")
                this.fixedLengthTooShortHandling = fixedLengthTooShortHandling;
            else
                this.fixedLengthTooShortHandling = "ERROR";
        }
        else{
            this.fixedLengthTooShortHandling = "ERROR";
        }
    }
}

class ConfigStruct extends Config {
    String recordsetName;
    String recordsetNamespace;
    Boolean ignoreRecordSetName;
    List<RecordSetStructure> recordsetStructureList;
    String keyFieldName;
    String keyFieldType;
    Boolean keyFieldRequired = false;
    int recordsetsPerMessage;
    String propDefaultName = "output_xml_node_";

    ConfigStruct(Message message, HashMap props) {
        super(message, props);

        def recordsetNameTemp = props.get("xml.recordsetName") as String;
        recordsetName = (recordsetNameTemp != '' && recordsetNameTemp != null) ? recordsetNameTemp : 'Recordset';

        def recordsetNamespaceTemp = props.get("xml.recordsetNamespace") as String;
        recordsetNamespace = (recordsetNamespaceTemp != '' && recordsetNamespaceTemp != null) ? recordsetNamespaceTemp : '';

        def dummyRSS = props.get("xml.recordsetStructure") as String;
        def recordsetStructureTemp = dummyRSS != null ? dummyRSS.tokenize(",") as String[] : [''] ;

        keyFieldName = props.get("xml.keyFieldName") as String;
        keyFieldType = props.get("xml.keyFieldType") as String;
        def ignoreRecordSetNameTemp = props.get("xml.ignoreRecordSetName") as String;
        ignoreRecordSetName = ignoreRecordSetNameTemp == 'true';
        def recordsetsPerMessagetemp = props.get("xml.recordsetsPerMessage");
        recordsetsPerMessage = recordsetsPerMessagetemp != null ? recordsetsPerMessagetemp as Integer : 0;

        if (recordsetStructureTemp == ['']){
            throw new Exception("Error! xml.recordsetStructure must be provided!");
        }
        else {
            recordsetStructureList = [];
            for (int i = 0; i < recordsetStructureTemp.size(); i += 2) {
                String value = recordsetStructureTemp[i];
                String occur = recordsetStructureTemp[i + 1];
                if (occur == "*") keyFieldRequired = true;
                def recordSS = new RecordSetStructure(value, occur, (i / 2) as Integer);
                recordSS.setFieldSeparator(props.get("xml." + recordSS.name  + ".fieldSeparator") as String);
                def temp = props.get("xml." + recordSS.name  + ".fieldFixedLengths");
                if (temp != null)
                    recordSS.setFieldFixedLengths(temp.tokenize(",") as Integer[]);
                recordSS.setFixedLengthTooShortHandling(props.get("xml." + recordSS.name  + ".fixedLengthTooShortHandling"));
                recordSS.setMissingLastFields(props.get("xml." + recordSS.name + ".missingLastFields"));
                recordsetStructureList.add(recordSS);
            }
            //keyFieldRequired = recordsetStructureList.size() == 1 ? false : keyFieldRequired;
            //keyFieldRequired = keyFieldRequired || keyFieldName != '';
            keyFieldRequired = recordsetStructureList.size() == 1 &&
                    (keyFieldName == '' || keyFieldName == null) ? false : keyFieldRequired;
            //recordsetStructure [NameA, 1 ; NameB, * ; NameC, 1]
        }

    }
}

class RecordSetStructure {
    String name;
    String fieldSeparator;
    Integer[] fieldFixedLengths;
    String[] fieldNames;
    String occur;
    int index;
    String keyFieldValue;
    int keyFieldIndex;      //keyField may not be in the beginning
    int keyFieldOffset;     //keyField may not be in the beginning
    Boolean addKeyField;    //optional keyField
    String missingLastFields;
    String fixedLengthTooShortHandling;

    RecordSetStructure(String recordName, String occur, int index){
        this.name = recordName; //NameA
        this.occur = occur;     //1 or *
        this.index = index;
    }

    void setFieldNames(String[] fieldNames){
        this.fieldNames = fieldNames;
    }

    void setFieldFixedLengths(Integer[] fieldFixedLengths){
        this.fieldFixedLengths = fieldFixedLengths;
    }

    void setKeyFieldValue(String keyFieldValue){
        this.keyFieldValue = keyFieldValue;
    }

    void setKeyFieldIndex(int keyFieldIndex){
        this.keyFieldIndex = keyFieldIndex;
    }

    void setKeyFieldOffset(int keyFieldOffset){
        this.keyFieldOffset = keyFieldOffset;
    }

    void setAddKeyField(Boolean addKeyField){
        this.addKeyField = addKeyField;
    }

    void setFieldSeparator(String fieldSeparator){
        this.fieldSeparator = fieldSeparator != null ? fieldSeparator : '';
    }

    void setMissingLastFields(String missingLastFields){
        if (missingLastFields != null) {
            missingLastFields = missingLastFields.toUpperCase();
            if (missingLastFields == "ADD" || missingLastFields == "IGNORE" || missingLastFields == "ERROR")
                this.missingLastFields = missingLastFields;
            else
                this.missingLastFields = "IGNORE";
        }
        else {
            this.missingLastFields = "IGNORE";
        }
    }

    void setFixedLengthTooShortHandling(String fixedLengthTooShortHandling){
        if (fixedLengthTooShortHandling != null) {
            fixedLengthTooShortHandling = fixedLengthTooShortHandling.toUpperCase();
            if (fixedLengthTooShortHandling == "CUT" || fixedLengthTooShortHandling == "IGNORE")
                this.fixedLengthTooShortHandling = fixedLengthTooShortHandling;
            else
                this.fixedLengthTooShortHandling = "ERROR";
        }
        else{
            this.fixedLengthTooShortHandling = "ERROR";
        }
    }

}
