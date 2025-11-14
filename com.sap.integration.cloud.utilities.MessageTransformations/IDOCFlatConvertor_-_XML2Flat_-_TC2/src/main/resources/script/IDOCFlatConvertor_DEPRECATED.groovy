import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.slurpersupport.GPathResult
import groovy.xml.XmlUtil
import com.sap.conn.jco.*
import com.sap.esb.datastore.DataStore
import com.sap.esb.datastore.Data
import org.osgi.framework.*

//IDOCFlatConvertor
//Version 0.2.1

def Message processData(Message message)
{
    String body = message.getBody(String);
    Map<String, Object> props = message.getProperties();
    String conversionType = props.get("idoc.conversionType") as String;
    String schemaFrom = props.get("idoc.schemaFrom") as String;
    //XMLSchemaNode.RFC = schemaFrom != 'xsd';

    String sapRelease = props.get("idoc.SAPRelease") as String;
    String destinationName = props.get("idoc.destination") as String;
    if (XMLSchemaNode.RFC &&
            (destinationName == null || destinationName == '')){
        throw new Exception("Error! idoc.destination property is mandatory!");
    }

    XMLSchemaNode xmlSchema;
    String resPayload;
    switch(conversionType) {
        case "XML2Flat":
            GPathResult root = new XmlSlurper().parseText(body);
            if (XMLSchemaNode.RFC){
                String idocType = root.IDOC.EDI_DC40.IDOCTYP.text().trim();
                //String idocRelease = root.IDOC.EDI_DC40.DOCREL.text().trim();
                String rfcPayload = Transform.getRFCPayload(message, destinationName, idocType, sapRelease);
                xmlSchema = Transform.parseSchemaRFC(rfcPayload);
            }
            else {
                String schemaFilename = props.get("idoc.schemaName") as String;
                xmlSchema = parseSchema(schemaFilename);
            }
            resPayload = TransformXML2Flat.convertIDOC(root, xmlSchema);
            break;
        case "Flat2XML":
            String[] lines = body.tokenize("\n");
            if (XMLSchemaNode.RFC){
                //parse first line to extract type and release
                String headerLine = lines[0];
                String idocType = Transform.getIDOCTypeFromHeader(headerLine);
                //String idocRelease = Transform.getIDOCReleaseFromHeader(headerLine);

                String rfcPayload = Transform.getRFCPayload(message, destinationName, idocType, sapRelease);
                xmlSchema = Transform.parseSchemaRFC(rfcPayload);
            }
            else {
                String schemaFilename = props.get("idoc.schemaName") as String;
                xmlSchema = parseSchema(schemaFilename);
            }

            resPayload = TransformFlat2XML.convertIDOC(lines, xmlSchema);

            break;
        default:
            throw new Exception("Error! idoc.conversionType \'" + conversionType + "\' is not supported.");
    }

    message.setBody(resPayload);
    return message;
}

XMLSchemaNode parseSchema(String schemaFilename){
    String schemaText = getSchemaXSDAsString(schemaFilename);
    GPathResult root = new XmlSlurper().parseText(schemaText);
    XMLSchemaNode xs = new XMLSchemaNode(null, "root", -2);
    readSchemaNodes_XSD(xs, root);
    xs = xs.subNodes[0];
    Transform.initHeaderLengths(xs);
    return xs;
}

//get XSD from resources and return it as string
String getSchemaXSDAsString(String schemaFilename){
    URL schemaFile = this.getClass().getResource('/xsd/' + schemaFilename);
    if (schemaFile == null) {
        throw new Exception("File not found: " + schemaFilename);
    }
    // Convert schemaFile to String
    String schemaText = schemaFile.text;
    return schemaText;
}

void readSchemaNodes_XSD(XMLSchemaNode xsParent, GPathResult node) {
    String name = node.name();
    if (name == 'element') {
        def nodeValue = node.@name as String;
        XMLSchemaNode xsNew = new XMLSchemaNode(xsParent, nodeValue, xsParent.hLevel + 1);
        xsParent.subNodes.add(xsNew);
        xsParent = xsNew;
        if (node.@fixed != null){
            xsNew.maxLength = (node.@fixed as String).length();
        }
    }
    else if (name == 'maxLength' || name == 'enumeration') {
        xsParent.maxLength = (node.@value as String) as Integer;
    }

    if (node.children() != null && node.children().size() > 0) {
        node.children().each { row ->
            readSchemaNodes_XSD(xsParent, row);
        }
    }
}

class Transform {

    static String getIDOCTypeFromHeader(String headerLine){
        Integer ffl = XMLSchemaNode.masterHeader["IDOCTYP"];
        Integer offset = XMLSchemaNode.masterHeader["TABNAM"] + XMLSchemaNode.masterHeader["MANDT"] +
                XMLSchemaNode.masterHeader["DOCNUM"] + XMLSchemaNode.masterHeader["DOCREL"] +
                XMLSchemaNode.masterHeader["STATUS"] + XMLSchemaNode.masterHeader["DIRECT"] +
                XMLSchemaNode.masterHeader["OUTMOD"] + XMLSchemaNode.masterHeader["EXPRSS"] +
                XMLSchemaNode.masterHeader["TEST"];
        String idocType = headerLine.substring(offset, offset + ffl).trim();
        return idocType;
    }

    static String getIDOCReleaseFromHeader(String headerLine){
        Integer ffl = XMLSchemaNode.masterHeader["DOCREL"];
        Integer offset = XMLSchemaNode.masterHeader["TABNAM"] + XMLSchemaNode.masterHeader["MANDT"] +
                XMLSchemaNode.masterHeader["DOCNUM"];
        String idocRelease = headerLine.substring(offset, offset + ffl).trim();
        return idocRelease;
    }

    static String getRFCPayload(Message message, String destinationName, String idocType, String idocRelease) {
        DataStoreOperations dso = new DataStoreOperations(message, idocType, idocRelease, destinationName);
        String rfcPayload = dso.readFromDataStore();
        if (rfcPayload == null) {
            rfcPayload = callRFC(destinationName, idocType, idocRelease);
            dso.writeToDataStore(rfcPayload);
        }
        if (rfcPayload == null)
            throw new Exception("Error! RFC Payload not available!");

        return rfcPayload;
    }

    static XMLSchemaNode parseSchemaRFC(String rfc_payload){
        XMLSchemaNode xs = new XMLSchemaNode(null, "root", -2);
        GPathResult rfcData = new XmlSlurper().parseText(rfc_payload);
        String idocName = rfcData.OUTPUT.PE_HEADER.IDOCTYP.text();
        //add INVOIC
        XMLSchemaNode invoicNode = new XMLSchemaNode(xs, idocName, -1);
        xs.subNodes.add(invoicNode);
        //add IDOC
        XMLSchemaNode idocNode = new XMLSchemaNode(invoicNode, "IDOC", 0);
        invoicNode.subNodes.add(idocNode);
        XMLSchemaNode edidc40Node = new XMLSchemaNode(idocNode, "EDI_DC40", 1);
        idocNode.subNodes.add(edidc40Node);
        handleRfcEDI_DC40Fields(edidc40Node);

        XMLSchemaNode lastNode = idocNode;
        rfcData.TABLES.PT_SEGMENTS.item.each{ item ->
            Integer hLevel = item.HLEVEL.text() as Integer;
            String itemName = item.SEGMENTTYP.text();
            String tempParentNo = item.PARPNO.text();

            XMLSchemaNode parent = calcParent(idocNode, lastNode, hLevel, tempParentNo);

            XMLSchemaNode xsSegment = new XMLSchemaNode(parent, itemName, hLevel);
            parent.subNodes.add(xsSegment);
            xsSegment.label = item.SEGMENTDEF.text();
            xsSegment.isSegment = true;
            lastNode = xsSegment;
            //loop all PT_FIELDS for current segment
            rfcData.TABLES.PT_FIELDS.item.each{ field ->
                String fieldParent = field.SEGMENTTYP.text();
                if (fieldParent == itemName){
                    String fieldName = field.FIELDNAME.text();
                    Integer fieldLen = field.EXTLEN.text() as Integer;
                    XMLSchemaNode xsField = new XMLSchemaNode(xsSegment, fieldName, hLevel + 1);
                    xsSegment.subNodes.add(xsField);
                    xsField.maxLength = fieldLen;
                }
            }
        }

        xs = xs.subNodes[0];
        return xs;
    }

    static private XMLSchemaNode calcParent(XMLSchemaNode idocNode, XMLSchemaNode lastNode,
                                            Integer hLevel, String tempParentNo){
        if (tempParentNo == "0000"){
            return idocNode;
        }
        else if(lastNode.hLevel == hLevel){
            return lastNode.parent;
        }
        else if(lastNode.hLevel < hLevel){
            return lastNode;
        }
        else if (lastNode.hLevel > hLevel){
            while (lastNode.hLevel > hLevel){
                lastNode = lastNode.parent;
            }
            return lastNode.parent;
        }
        return null;
    }

    static private void handleRfcEDI_DC40Fields(XMLSchemaNode edidc40Node) {
        edidc40Node.isSegment = true;
        XMLSchemaNode.masterHeader.each { key, value ->
            XMLSchemaNode field = new XMLSchemaNode(edidc40Node, key, 2);
            edidc40Node.subNodes.add(field);
            field.maxLength = value;
        }
    }

    static void initHeaderLengths(XMLSchemaNode xsParent){
        XMLSchemaNode idocNode = xsParent.subNodes[0];
        XMLSchemaNode ediDc40Node = idocNode.subNodes[0];
        for (int i = 0; i < ediDc40Node.subNodes.size(); i++) {
            XMLSchemaNode xsChild = ediDc40Node.subNodes[i];
            if (XMLSchemaNode.masterHeader.containsKey(xsChild.name)) {
                xsChild.maxLength = XMLSchemaNode.masterHeader[xsChild.name];
            }
        }
    }

    static String callRFC(String destinationName, String idocType, String idocRelease) {
        String bapiName = "IDOCTYPE_READ_COMPLETE";
        try {
            JCoDestination jcoDestination = JCoDestinationManager.getDestination(destinationName)
            JCoFunction function = jcoDestination.getRepository().getFunction(bapiName)

            // Check if the function is retrieved successfully
            if (function != null) {
                // Get the import parameter list of the function (RFC Request Fields)
                def parameterList = function.getImportParameterList()
                parameterList.setValue("PI_IDOCTYP", idocType);
                if (idocRelease != null && idocRelease != '')
                    parameterList.setValue("PI_RELEASE", idocRelease);

                function.execute(jcoDestination)

                // Get the response XML from the executed RFC function.
                def result = function.toXML()
                return result;
            } else {
                // Throw an exception if the RFC function is not found
                throw new Exception("ERROR: RFC function '${bapiName}' not found")
            }

        } catch (JCoException e) {
            // Throw JCo exceptions
            throw new Exception("ERROR executing RFC function: ${e.getMessage()}")
        } catch (Exception e) {
            // Throw other exceptions
            throw new Exception("Unexpected error: ${e.getMessage()}")
        }

    }

    //outputs string with fixed size
    static String fixedLengthString(String inputString, Integer targetLength) {
        inputString = inputString != null ? inputString : "";
        if (targetLength == 0){
            return inputString;
        }
        else if (inputString.length() > targetLength) {
            return inputString.substring(0, targetLength);;
            //throw new Exception("Error! String '$inputString' length (" + inputString.length() + ") is bigger than expected ($targetLength).");
        }
        else {
            return inputString.padRight(targetLength);
        }
    }

    static String xmlToStringClean(Node root){
        def xmlString = XmlUtil.serialize(root);
        xmlString = xmlString.replaceFirst('<\\?xml version="1.0" encoding="UTF-8"\\?>', '')
        xmlString = xmlString.replaceAll(/\r\n.*?\r\n/, "\n").trim();
        return xmlString;
    }

}

class TransformXML2Flat extends Transform {

    static String convertIDOC(GPathResult root, XMLSchemaNode xmlSchema){
        StringBuilder sb = new StringBuilder();
        matchValuesToSchema(root, xmlSchema);
        createOutput(sb, root, xmlSchema, 0);
        sb.append("\n");
        String resPayload = sb.replaceFirst("\n", "");

        return resPayload;
    }

    //groups nodes and repeated nodes
    static private void matchValuesToSchema(GPathResult payloadNode, XMLSchemaNode xsParent){
        for (int i = 0; i < payloadNode.children().size(); i++){
            GPathResult rowNode = payloadNode.children()[i];
            String nodeName = rowNode.name();
            XMLSchemaNode xsChild = xsParent.subNodes.find { node -> node.name == nodeName };
            if (xsChild != null) {
                xsParent.hasValues = true;
                xsChild.hasValues = true;
            }
            if (rowNode.children().size() != 0) {
                matchValuesToSchema(rowNode, xsChild);
                xsChild.counter = payloadNode."$nodeName".size();
            }
            else{
                if (xsChild != null) {
                    xsParent.hasValues = true;
                    xsChild.outputValues.add(rowNode.text() as String);
                    if (xsParent.name == 'EDI_DC40') {
                        if (xsChild.name == 'MANDT') {
                            xsChild.MANDT = rowNode.text();
                        } else if (xsChild.name == 'DOCNUM') {
                            xsChild.DOCNUM = rowNode.text();
                        }
                    }
                }
            }
        }
        //guarantees that the outputValues always have the same size to avoid shifting values when empty
        xsParent.handleOutputValuesSize();
    }

    //creates output - result in sb variable
    static private Integer createOutput(StringBuilder sb, GPathResult payloadNode, XMLSchemaNode xsParent, int lineNumber){
        for (int iNodeOccur = 0; iNodeOccur < xsParent.counter; iNodeOccur++) {
            xsParent.lineNumber = lineNumber + 1;
            for (int iFields = 0; iFields < xsParent.subNodes.size(); iFields++) {
                XMLSchemaNode xsChild = xsParent.subNodes[iFields];
                String nodeName = xsChild.name;
                GPathResult rowNode = payloadNode.children().find { node -> node.name() == nodeName }
                if (xsChild.subNodes.size() == 0 && xsParent.hasValues) {
                    if (iFields == 0){
                        sb.append("\n");
                        if(xsParent.name != 'EDI_DC40') {
                            lineNumber++;
                            //add tag
                            sb.append(fixedLengthString(xsParent.getOutputTag(), xsParent.headerLength));
                            //add MANDT
                            sb.append(xsParent.MANDT);
                            //add DOCNUM
                            sb.append(xsParent.DOCNUM);
                            //add lineNumber
                            sb.append((lineNumber as String).padLeft(6, "0"));
                            //add hLevel
                            sb.append(buildHLevel(xsParent, xsParent.hLevel));
                        }
                    }
                    String value = iNodeOccur < xsChild.outputValues.size() ? xsChild.outputValues[iNodeOccur] : "";
                    String finalValue = fixedLengthString(value, xsChild.maxLength);
                    sb.append(finalValue);
                } else {
                    lineNumber = createOutput(sb, rowNode, xsChild, lineNumber);
                }
            }
        }
        return lineNumber;
    }

    static private String buildHLevel(XMLSchemaNode xsNode, Integer hLevelChild){
        String lineNumber = xsNode.parent.lineNumber as String;
        String hLevel = hLevelChild as String;
        hLevel = hLevel.padLeft(2, "0");
        if (xsNode.parent.hLevel == 0)
            lineNumber = "0";

        lineNumber = lineNumber.padLeft(6, "0");

        return lineNumber + hLevel;
    }

}

class TransformFlat2XML extends Transform {

    static String convertIDOC(String[] lines, XMLSchemaNode xmlSchema){
        Node xml = new Node(null, xmlSchema.name);
        XMLSchemaNode idocSchemaNode = xmlSchema.subNodes[0];
        Node idocNode = new Node(xml, "IDOC", [BEGIN: '1']);

        matchAndCreate(lines, idocSchemaNode, idocNode);

        String resPayload = xmlToStringClean(xml);
        return resPayload;
    }

    static private void matchAndCreate(String[] lines, XMLSchemaNode xsParent, Node idocNode){
        //first cycle xsParent & = IDOC
        ArrayList auxNodeArray = [];
        int iSegment = 0;
        for (int iLine = 0; iLine < lines.size(); iLine++){
            String line = lines[iLine];
            if (iLine == 0){
                Integer offset = 0;
                XMLSchemaNode EDI_DC40 = xsParent.subNodes[0];
                Node ediNode = new Node(idocNode, EDI_DC40.name, [SEGMENT: '1']);
                EDI_DC40.isSegment = true;
                EDI_DC40.lineNumber = 0;
                auxNodeArray.add([EDI_DC40, ediNode]);
                for (int iField = 0; iField < EDI_DC40.subNodes.size(); iField++) {
                    XMLSchemaNode field = EDI_DC40.subNodes[iField];
                    String value = line.substring(offset, offset + field.maxLength).trim();
                    offset += field.maxLength;
                    field.outputValues.add(value);
                    if (value != "")
                        new Node(ediNode, field.name, value);
                }
            }
            else {
                Integer offset = 0;
                //30 -> segmentName
                String label = line.substring(offset, 30).trim();
                //read header fields
                //19 ->  ignore idoc number
                offset += 30 + 19;
                Integer lineNumber = line.substring(offset, offset + 6).trim() as Integer;
                offset += 6;
                Integer lineNumberParent = line.substring(offset, offset + 6).trim() as Integer;
                offset += 6;
                Integer hLevel = line.substring(offset, offset + 2).trim() as Integer;
                offset += 2;

                if (lineNumberParent == 0) {
                    handleSegments(iSegment, xsParent, idocNode, line, label, offset, lineNumber, auxNodeArray);
                }
                else {
                    XMLSchemaNode segmentTemp = auxNodeArray[lineNumberParent][0];
                    Node segmentNode = auxNodeArray[lineNumberParent][1];
                    handleSegments(iSegment, segmentTemp, segmentNode, line, label, offset, lineNumber, auxNodeArray);
                }
            }
        }

        //remove empty segments
        cleanEmptySegments(auxNodeArray);
    }

    static private void handleSegments(int iSegment, XMLSchemaNode xsParent, Node xmlParent, String line, String label,
                                       int offset, int lineNumber, def auxNodeArray){
        boolean found = false;
        for (iSegment; iSegment < xsParent.subNodes.size(); iSegment++) {
            XMLSchemaNode segment = xsParent.subNodes[iSegment];
            //segment.isSegment = true;
            if (identifyNode(segment, label)) {
                found = true;
                segment.lineNumber = lineNumber;
                Node newNode = new Node(xmlParent, segment.name, [SEGMENT: '1']);
                auxNodeArray.add([segment, newNode]);

                for (int iField = 0; iField < segment.subNodes.size(); iField++) {
                    XMLSchemaNode field = segment.subNodes[iField];
                    //String value = line.substring(offset, offset + field.maxLength).trim();
                    String value = "";
                    Integer ffl = offset + field.maxLength;
                    if (ffl <= line.size() )
                        value = line.substring(offset, ffl).trim();

                    offset += field.maxLength;
                    field.outputValues.add(value);
                    if (value != ""){
                        new Node(newNode, field.name, value);
                    }
                }
                break;
            }
        }
        if (!found) {
            throw new Exception("Error! The node " + label + " was not found in the schema!");
        }
    }

    static private boolean identifyNode(XMLSchemaNode xsParent, String label){
        if (xsParent.name == label || xsParent.label == label)
            return true;
        else {
            //TODO VALIDATE
            //convert label to expected format, to handle different SAPRelease
            String labelMaster = label.substring(0, 7);
            labelMaster = replaceSecondChar(labelMaster);
            if (xsParent.name == labelMaster)
                return true;
        }
        return false;
    }

    static String replaceSecondChar(String input) {
        if (input.length() > 1 && input[1] == '2') {
            return input[0] + '1' + input[2..-1]
        }
        return input
    }

    static private void cleanEmptySegments(ArrayList auxNodeArray) {
        for (int i = auxNodeArray.size() - 1; i > -1; i--) {
            Node nodeToRemove = auxNodeArray[i][1];
            if (nodeToRemove.children().size() == 0) {
                nodeToRemove.parent().remove(nodeToRemove);
            }
        }
    }

}

class DataStoreOperations {
    private def camelContext;
    private String datastoreName;
    private String idocKey;
    private String scope;
    //1000ms - 60s - 60min - 24h - 25days
    //TODO validate values
    private static long dayMilli = 86400000;
    private static long dueAtInMs = 25 * dayMilli;
    private static long retainUntilInMs = 30 * dayMilli;

    DataStoreOperations(Message message, String idocType, String idocRelease, String destinationName) {
        idocKey = idocType;
        if (idocRelease != null && idocRelease != '')
            idocKey += "_" + idocRelease;
        this.datastoreName = "IDOCFlatConvertor";
        this.scope = destinationName;
        this.camelContext = message.exchange.getContext();
    }

    void writeToDataStore(String payload) {
        DataStore dataStore = (DataStore)camelContext.getRegistry().lookupByName(DataStore.class.getName());
        byte[] payloadData = payload.getBytes("UTF-8");

        //Create datastore payload/data with the following parameters
        //params => (DatastoreName, ContextName, EntryId, Body, Headers, MessageId, Version)
        //Note: Setting ContextName to null, will create a global Datastore
        Data dsData = new Data(datastoreName, scope, idocKey,
                payloadData, null, null, 0);

        //Write dsData element to the data store
        //params => (DataInstance, overwriteEntry, encrypt, dueAtInMs, retainUntilInMs)
        dataStore.put(dsData, true, false, dueAtInMs, retainUntilInMs);

    }

    String readFromDataStore() {
        DataStore dataStore = (DataStore)camelContext.getRegistry().lookupByName(DataStore.class.getName());
        try
        {
            def dsEntry = dataStore.get(datastoreName, scope, idocKey);
            //dsEntry null if DATASTORE_NAME or key does not exist
            if (dsEntry != null) {
                String result = new String(dsEntry.getDataAsArray());
                return result;
            }
        }
        catch(Exception ex){
            //ignore for now
        }
        return null;
    }

}

class XMLSchemaNode {
    //true if parsing RFC, false if parsing XSD
    static Boolean RFC = true;
    XMLSchemaNode parent;
    String name;
    String label;
    Integer maxLength = 0;
    static Integer headerLength = 30;
    ArrayList<XMLSchemaNode> subNodes = [];
    Integer hLevel = 0;
    //defined during Match (read payload)
    static String MANDT = "";
    static String DOCNUM = "";
    ArrayList outputValues = [];
    Integer counter = 1;
    Boolean hasValues = false;
    //defined during Build (output)
    Integer lineNumber = -1;
    //F2I
    Boolean isSegment = false;

    static final Map masterHeader = [
            "TABNAM" : 10,
            "MANDT"  : 3,
            "DOCNUM" : 16,
            "DOCREL" : 4,
            "STATUS" : 2,
            "DIRECT" : 1,
            "OUTMOD" : 1,
            "EXPRSS" : 1,
            "TEST"   : 1,
            "IDOCTYP": 30,
            "CIMTYP" : 30,
            "MESTYP" : 30,
            "MESCOD" : 3,
            "MESFCT" : 3,
            "STD"    : 1,
            "STDVRS" : 6,
            "STDMES" : 6,
            "SNDPOR" : 10,
            "SNDPRT" : 2,
            "SNDPFC" : 2,
            "SNDPRN" : 10,
            "SNDSAD" : 21,
            "SNDLAD" : 70,
            "RCVPOR" : 10,
            "RCVPRT" : 2,
            "RCVPFC" : 2,
            "RCVPRN" : 10,
            "RCVSAD" : 21,
            "RCVLAD" : 70,
            "CREDAT" : 8,
            "CRETIM" : 6,
            "REFINT" : 14,
            "REFGRP" : 14,
            "REFMES" : 14,
            "ARCKEY" : 70,
            "SERIAL" : 20
    ];

    XMLSchemaNode(XMLSchemaNode parent, String name, Integer hLevel){
        this.parent = parent;
        this.name = name;
        this.hLevel = _calcHLevel(hLevel);
    }

    //return label if exists, else return name
    String getOutputTag(){
        if (label != null && label != ''){
            return label;
        }
        else{
            return name;
        }
    }

    //hLevel setup
    private int _calcHLevel(Integer hLevel){
        if (!RFC && this.parent != null && this.parent.name == "IDOC" &&
                (this.parent.subNodes.size() > 1 && this.name != "E1IDOCENHANCEMENT"))
            return hLevel + 1;
        else
            return hLevel;
    }

    private int _getMaxSizeSubNodesOutput(){
        int max = 0;
        for (int i = 0; i < this.subNodes.size(); i++) {
            int size = this.subNodes[i].outputValues.size();
            max = size > max ? size : max;
        }
        return max;
    }

    void handleOutputValuesSize(){
        for (int i = 0; i < this.subNodes.size(); i++){
            XMLSchemaNode xsChild = this.subNodes[i];
            if (xsChild.outputValues.size() < this._getMaxSizeSubNodesOutput()){
                xsChild.outputValues.add("");
            }
        }
    }

}
