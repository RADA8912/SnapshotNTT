import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.xml.QName;
import groovy.xml.XmlUtil;

//XMLAnonymizer
//Version 1.0.0

Message processData(Message message) {

    message.setBody(XMLAnonymizer.execute(message));

    return message;
}

class XMLAnonymizer{

    private static String unquote(String value) {
        int vlen = value.length();
        if (vlen >= 2) {
            char f = value.charAt(0);
            char e = value.charAt(vlen-1);
            if ((f == '"' && e == '"') || (f == '\'' && e == '\'')) return value.substring(1,vlen-1);
        }
        return value;
    }

    static String execute(Message message){
        XmlParser parser = new XmlParser();

            // Get parameters from exchange properties
            def acceptNamespaces         = message.getProperty("anonymizer.acceptNamespaces");
            def quoteCharacter           = message.getProperty("anonymizer.quote");     // Default quote character is single quote
            def encoding                 = message.getProperty("anonymizer.encoding"); // Default encoding is UTF-8
            //def keepCDATA                = message.getProperty("anonymizer.keepCDATA");
        //def escapeOptionalCharacters = message.getProperty("anonymizer.escapeOptionalCharacters");
        //def escapeEncoding           = message.getProperty("anonymizer.escapeEncoding");

        quoteCharacter = (quoteCharacter) ? quoteCharacter : '"';
        encoding       = (encoding) ? encoding : "UTF-8";

        def namespacePairs = acceptNamespaces.trim().split(/\s+/);
        def namespaceReplacements = [:];
        def namespaceReplaced = [];

        if (!acceptNamespaces)
            throw new Exception("At least one namespace is mandatory");

        if (!(quoteCharacter in ["'", '"']))
            throw new Exception("Quote Character is invalid");


        namespacePairs.eachWithIndex { pair, idx ->
            if ((idx % 2) == 1) return true; //Skip odd index

            namespaceReplacements[unquote(pair)] = unquote(namespacePairs[idx+1]);
        }

        println "Namespace Replacements: $namespaceReplacements"

        // Recursive closure to replace nodes with prefix
        def replaceNodePrefix;
        replaceNodePrefix = { Node node ->
            String namespaceURI = "";
            String localPart    = "";
            String prefix       = "";
            String simpleName   = "";

            def nodeName = node.name();
            if (node.name() instanceof String){
                simpleName = node.name();
            }else{
                prefix       = node.name().getPrefix();
                namespaceURI = nodeName.namespaceURI;
                localPart    = simpleName = nodeName.localPart;
            }

            println "Prefix: $prefix | Namespace: $namespaceURI | Element: $simpleName";

            Node outputNode;
            if (namespaceURI in namespaceReplacements.keySet()) {
                if (namespaceReplacements[namespaceURI] == "''"){ //if '' specified, create a new node with namespace without prefix
                    def newQName = new QName(namespaceURI, localPart, '');
                    outputNode = parser.createNode(null, newQName, node.attributes());

                }else{
                    def newQName = new QName(namespaceURI, localPart, namespaceReplacements[namespaceURI]);
                    outputNode = parser.createNode(null, newQName, node.attributes());
                }

                namespaceReplaced.push(namespaceURI);
            }else{ //If namespace is not on the specified list, create a new node without namespace
                def localNodeName = (nodeName instanceof QName) ? nodeName.getLocalPart() : nodeName;
                outputNode = parser.createNode(null, localNodeName, node.attributes());
            }

            // read all the children elements, and append/setValue to the newly created outputNode recursively
            node.children().each { child ->
                switch (child){
                    case Node:
                        outputNode.append(replaceNodePrefix.trampoline(child).call());
                        break;
                    case String:
                        outputNode.setValue(child);

                        //Improvement can be done regarding 'escaped characters' function existing on XMLAnonymizer
                        break;
                }
            }

            return outputNode;
        }.trampoline();

        String payload = message.getBody(java.lang.String) as String;

        Node root = parser.parseText(payload);

        def resultXML = replaceNodePrefix(root);

        def xmlResultString = XmlUtil.serialize(resultXML);

        //Remove the original XML Declaration
        xmlResultString = xmlResultString.replaceAll(/^<\?xml.*?\?>/, "");

        //Add new XML Declaration
        xmlResultString = "<?xml version=${quoteCharacter}1.0${quoteCharacter} encoding=${quoteCharacter}${encoding}${quoteCharacter}?>" + xmlResultString;


        //re-add in root element the namespaces that were unused but declared on properties
        namespaceReplaced = namespaceReplaced.unique();

        def nsDeclarations = '';
        namespaceReplacements.each { uri, prefix ->
            if (!namespaceReplaced.find{ it == uri} && payload.contains(uri)){
                nsDeclarations += " xmlns:${prefix}=${quoteCharacter}${uri}${quoteCharacter}";
            }
        }

        xmlResultString = xmlResultString.replaceFirst(/<(?!\/)([^?\s>]+)([^>]*?)>/, { match, tagName, attrs ->
            "<$tagName$nsDeclarations$attrs>"
        });

        //Handle the quote character according to what is received from the property.
        if (quoteCharacter == "'"){
            xmlResultString = xmlResultString.replaceAll(/="([^"]*)"/) { match ->
                match[0].replaceAll('"', "'");
            }
        }else if (quoteCharacter == '"'){
            xmlResultString = xmlResultString.replaceAll(/='([^'']*)'/) { match ->
                match[0].replaceAll("'", '"');
            }
        }

        return xmlResultString.replaceAll(/\n\s*/,'');
    }
}