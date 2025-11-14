import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.slurpersupport.GPathResult
import groovy.xml.XmlUtil

//IDOCFlatConvertor
//Version 1.0.3

def Message processData(Message message)
{
    String body = message.getBody(String);
    Map<String, Object> props = message.getProperties();
    String conversionType = props.get("idoc.conversionType") as String;

    XMLSchemaNode xmlSchema;
    String resPayload;
    switch(conversionType) {
        case "XML2Flat":
            XMLSchemaNode.NEWLINE_NORM = body.count(XMLSchemaNode.NEWLINE_PATERN) > 0;
            InputStream inputStream = new ByteArrayInputStream(body.getBytes('UTF-8'));
            GPathResult root = new XmlSlurper().parse(inputStream);
            String rfcPayload = props.get("idoc.rfcPayload") as String;
            xmlSchema = Transform.parseSchemaRFC(rfcPayload);
            resPayload = TransformXML2Flat.convertIDOC(root, xmlSchema);
            break;
        case "Flat2XML":
            String[] lines = body.tokenize("\n");
            def RenumberSegmentsTemp = props.get("idoc.RenumberSegments");
            String RenumberSegmentsSTemp = RenumberSegmentsTemp != null ? RenumberSegmentsTemp as String : 'false';
            Boolean RenumberSegments = RenumberSegmentsSTemp == 'true';
            String rfcPayload = props.get("idoc.rfcPayload") as String;
            def trimFieldsTemp = props.get("idoc.TrimFields");
            String sTrimFieldsTemp = trimFieldsTemp != null ? trimFieldsTemp as String : 'false';
            Boolean trimFields = sTrimFieldsTemp == 'true';
            xmlSchema = Transform.parseSchemaRFC(rfcPayload);
            resPayload = TransformFlat2XML.convertIDOC(lines, xmlSchema, RenumberSegments, trimFields);
            break;
        default:
            throw new Exception("Error! idoc.conversionType \'" + conversionType + "\' is not supported.");
    }

    message.setBody(resPayload);
    return message;
}

class Transform {

    static XMLSchemaNode parseSchemaRFC(String rfc_payload){
        XMLSchemaNode xs = new XMLSchemaNode(null, "root", -2);
        InputStream inputStream = new ByteArrayInputStream(rfc_payload.getBytes('UTF-8'));
        GPathResult rfcData = new XmlSlurper().parse(inputStream);
        String idocName = rfcData.PE_HEADER.IDOCTYP.text();
        String cimType = rfcData.PE_HEADER.CIMTYP.text();
        //add INVOIC
        XMLSchemaNode invoicNode = new XMLSchemaNode(xs, idocName, -1);
        invoicNode.label = cimType;
        xs.subNodes.add(invoicNode);
        //add IDOC
        XMLSchemaNode idocNode = new XMLSchemaNode(invoicNode, "IDOC", 0);
        invoicNode.subNodes.add(idocNode);
        XMLSchemaNode edidc40Node = new XMLSchemaNode(idocNode, "EDI_DC40", 1);
        idocNode.subNodes.add(edidc40Node);
        handleRfcEDI_DC40Fields(edidc40Node);

        XMLSchemaNode lastNode = idocNode;
        rfcData.PT_SEGMENTS.item.each{ item ->
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
            rfcData.PT_FIELDS.item.each{ field ->
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

    //outputs string with fixed size
    static String fixedLengthString(String inputString, Integer targetLength) {
        inputString = inputString != null ? inputString : "";
        if (targetLength == 0){
            return inputString;
        }
        else if (inputString.length() > targetLength) {
            return inputString.substring(0, targetLength);
            //throw new Exception("Error! String '$inputString' length (" + inputString.length() + ") is bigger than expected ($targetLength).");
        }
        else {
            return inputString.padRight(targetLength);
        }
    }

    static String xmlToStringClean(Node root){
        def xmlString = XmlUtil.serialize(root);
        xmlString = xmlString.replaceFirst('<\\?xml version="1.0" encoding="UTF-8"\\?>', '');
        xmlString = xmlString.replaceAll(/\r\n.*?\r\n/, "\n").trim();
        return xmlString;
    }

}

class TransformXML2Flat extends Transform {

    static String convertIDOC(GPathResult root, XMLSchemaNode xmlSchema){
        String resPayload = matchAndCreate(root, xmlSchema, 0);
        return resPayload;
    }

    static private String matchAndCreate(GPathResult payloadNode, XMLSchemaNode xsParent, Integer lineNumber){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < payloadNode.children().size(); i++){
            GPathResult rowNode = payloadNode.children()[i];
            String nodeName = rowNode.name();
            XMLSchemaNode xsNode = xsParent.subNodes.find { node -> node.name == nodeName };
            lineNumber = handleSegments(sb, rowNode, xsNode, lineNumber);
        }
        return sb.toString();
    }

    static private Integer handleSegments(StringBuilder sb, GPathResult rowNode, XMLSchemaNode xsNode, Integer lineNumber){

        List<XMLSchemaNode> leafNodes = xsNode.subNodes.findAll { it.subNodes.size() == 0 };
        if (leafNodes.size() > 0){
            StringBuilder sbLine = new StringBuilder();
            if (xsNode.name == 'EDI_DC40') {
                xsNode.MANDT = rowNode.MANDT.text();
                xsNode.DOCNUM = rowNode.DOCNUM.text();
                lineNumber = 0;
                writeLine(sbLine, leafNodes, rowNode);
            }
            else {
                //add tag
                sbLine.append(fixedLengthString(xsNode.label, xsNode.headerLength));
                //add MANDT
                sbLine.append(xsNode.MANDT);
                //add DOCNUM
                sbLine.append(xsNode.DOCNUM);
                //add lineNumber
                sbLine.append((lineNumber as String).padLeft(6, "0"));
                //add parent lineNumber and hLevel
                sbLine.append(getParentLineNumberAndHLevel(xsNode, xsNode.hLevel));

                writeLine(sbLine, leafNodes, rowNode);
            }
            xsNode.lineNumber = lineNumber;
            lineNumber++
            sb.append(sbLine.toString());
            sb.append("\n");
        }

        List<XMLSchemaNode> branchNodes = xsNode.subNodes.findAll { it.subNodes.size() > 0 };
        if (branchNodes.size() > 0){
            for (int iBranch = 0; iBranch < branchNodes.size(); iBranch++){
                XMLSchemaNode xsBranch = branchNodes[iBranch];
                //def childList = rowNode.'*'.findAll { it.name() == xsBranch.name  };
                def childList = rowNode.'*'.findAll { it.name() == xsBranch.getParsedName() };
                for (int iCount = 0; iCount < childList.size(); iCount++){
                    GPathResult xmlBranch = childList[iCount];
                    lineNumber = handleSegments(sb, xmlBranch, xsBranch, lineNumber);
                }
            }
        }
        return lineNumber;
    }

    static private void writeLine(StringBuilder sbLine, List<XMLSchemaNode> leafNodes, GPathResult rowNode){
        for (int iLeaf = 0; iLeaf < leafNodes.size(); iLeaf++) {
            XMLSchemaNode leaf = leafNodes[iLeaf];
            String value = rowNode."$leaf.name";
            if (XMLSchemaNode.NEWLINE_NORM) {
                value = value.replaceAll("\n", XMLSchemaNode.NEWLINE_PATERN);
            }
            String finalValue = fixedLengthString(value, leaf.maxLength);
            sbLine.append(finalValue);
        }
    }

    static private String getParentLineNumberAndHLevel(XMLSchemaNode xsNode, Integer hLevelChild){
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

    static String convertIDOC(String[] lines, XMLSchemaNode xmlSchema, Boolean RenumberSegments, Boolean trimFields){
        Node xml = matchAndCreate(lines, xmlSchema, RenumberSegments, trimFields);
        String resPayload = xmlToStringClean(xml);
        return resPayload;
    }

    static private Node matchAndCreate(String[] lines, XMLSchemaNode xmlSchema, Boolean RenumberSegments, Boolean trimFields){
        String idocName = xmlSchema.label != "" ? xmlSchema.getParsedLabel() : xmlSchema.getParsedName();
        Node xml = new Node(null, idocName);
        XMLSchemaNode xsIDOC = xmlSchema.subNodes[0];
        Node idocNode;
        ArrayList auxNodeArray = [];
        int iSegment = 0;
        for (int iLine = 0; iLine < lines.size(); iLine++){
            String line = lines[iLine];
            if (line.startsWith("EDI_DC40")){
                idocNode = new Node(xml, "IDOC", [BEGIN: '1']);
                Integer offset = 0;
                XMLSchemaNode EDI_DC40 = xsIDOC.subNodes[0];
                Node ediNode = new Node(idocNode, EDI_DC40.name, [SEGMENT: '1']);
                EDI_DC40.isSegment = true;
                EDI_DC40.lineNumber = 0;
                auxNodeArray = [];
                auxNodeArray.add([xsIDOC, idocNode]);
                //auxNodeArray.add([EDI_DC40, ediNode]);
                createNodes(EDI_DC40, offset, line, ediNode, trimFields);
            }
            else {
                Integer offset = 0;
                //30 -> segmentName
                String lineLabel = line.substring(offset, 30).trim();
                //read header fields
                //19 ->  ignore idoc number
                offset += 30 + 19;
                Integer lineNumber = -1;
                if (!RenumberSegments)
                    lineNumber = line.substring(offset, offset + 6).trim() as Integer;
                offset += 6;
                Integer lineNumberParent = -1;
                if (!RenumberSegments)
                    lineNumberParent = line.substring(offset, offset + 6).trim() as Integer;
                offset += 6;
                //Integer hLevel = line.substring(offset, offset + 2).trim() as Integer;
                offset += 2;

                if (!RenumberSegments) {
                    if (lineNumberParent == 0) {
                        handleSegments(iSegment, xsIDOC, idocNode, line,
                                lineLabel, offset, lineNumber, auxNodeArray, trimFields);
                    } else {
                        XMLSchemaNode segmentTemp = auxNodeArray[lineNumberParent][0] as XMLSchemaNode;
                        Node segmentNode = auxNodeArray[lineNumberParent][1] as Node;
                        handleSegments(iSegment, segmentTemp, segmentNode, line,
                                lineLabel, offset, lineNumber, auxNodeArray, trimFields);
                    }
                }
                else {
                    for (int i = auxNodeArray.size() - 1; i > -1; i--){
                        boolean found = false;
                        XMLSchemaNode segmentTemp = auxNodeArray[i][0] as XMLSchemaNode;
                        Node segmentNode = auxNodeArray[i][1] as Node;
                        //confirm if current node belongs to these nodes, if not, continue
                        for (int iSeg = 0; iSeg < segmentTemp.subNodes.size(); iSeg++) {
                            XMLSchemaNode segment = segmentTemp.subNodes[iSeg];
                            if (identifyNode(segment, lineLabel)) {
                                found = true;
                                handleSegments(iSegment, segmentTemp, segmentNode,
                                        line, lineLabel, offset, lineNumber, auxNodeArray, trimFields);
                                break;
                            }
                        }
                        if (found)
                            break;
                    }
                }
            }
        }

        //remove empty segments
        cleanEmptySegments(auxNodeArray);
        return xml;
    }

    static private void handleSegments(int iSegment, XMLSchemaNode xsParent, Node xmlParent, String line, String label,
                                       int offset, int lineNumber, def auxNodeArray, Boolean trimFields){
        boolean found = false;
        for (iSegment; iSegment < xsParent.subNodes.size(); iSegment++) {
            XMLSchemaNode segment = xsParent.subNodes[iSegment];
            //segment.isSegment = true;
            if (identifyNode(segment, label)) {
                found = true;
                segment.lineNumber = lineNumber;
                Node newNode = new Node(xmlParent, segment.getParsedName(), [SEGMENT: '1']);
                auxNodeArray.add([segment, newNode]);
                createNodes(segment, offset, line, newNode, trimFields);
                break;
            }
        }
        if (!found) {
            throw new Exception("Error! The node " + label + " was not found in the schema!");
        }
    }

    private static void createNodes(XMLSchemaNode segment, int offset, String line, Node newNode, Boolean trimFields){
        for (int iField = 0; iField < segment.subNodes.size(); iField++) {
            XMLSchemaNode field = segment.subNodes[iField];
            String value = "";
            Integer ffl = offset + field.maxLength;
            if (ffl <= line.size()){
                value = line.substring(offset, ffl);
                if (trimFields)
                    value = value.trim();
            }

            offset += field.maxLength;
            String valTemp = value.trim();
            if (valTemp != ""){
                new Node(newNode, field.getParsedName(), value);
            }
        }
    }

    static private boolean identifyNode(XMLSchemaNode xsParent, String lineLabel){
        if (xsParent.name == lineLabel || xsParent.label == lineLabel)
            return true;
        else {
            //TODO VALIDATE
            //convert label to expected format, to handle different SAPRelease
            Integer size = lineLabel.size() > xsParent.name.size() ? xsParent.name.size() : lineLabel.size();
            String labelMaster = lineLabel.substring(0, size);
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

class XMLSchemaNode {
    XMLSchemaNode parent;
    String name;
    String label;
    Integer maxLength = 0;
    static Integer headerLength = 30;
    ArrayList<XMLSchemaNode> subNodes = [];
    Integer hLevel = 0;
    //defined during 'matchAndCreate'
    static String MANDT = "";
    static String DOCNUM = "";
    Integer lineNumber = -1;
    //F2I
    Boolean isSegment = false;
    static String NEWLINE_PATERN = "\r\n";
    static Boolean NEWLINE_NORM;

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
        this.hLevel = hLevel;
    }

    public String getParsedName(){
        String parsed = this.name.replaceAll('/', '_-');
        return parsed;
    }

    public String getParsedLabel(){
        String parsed = this.label.replaceAll('/', '_-');
        return parsed;
    }

}
