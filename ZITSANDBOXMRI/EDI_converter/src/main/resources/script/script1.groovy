import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
    // Get message body
    def body = message.getBody(String);

    Reader reader = message.getBody(Reader)
        XmlSlurper slurper = new XmlSlurper()
        // Keep whitespaces
        slurper.keepIgnorableWhitespace = true
        def root = slurper.parse(reader)

        // tbd.
        String UNA1 = ":"
        // tbd.
        String UNA2 = "+"
        // tbd.
        String UNA3 = "."
        // tbd.
        String UNA4 = "?"
        // tbd.
        String UNA5 = " "
        // tbd.
        String UNA6 = "'"
        // tbd.
        String resultBody = ""

        resultBody += "UNA" + UNA1 + UNA2 + UNA3 + UNA4 + UNA5

        // UNB tbd.

        resultBody += UNA6 + "UNB" + UNA2

        String previousSegementIsD = "false"

        root.'**'.each {
        println(it.name() + "+" + it.getClass())

        segmentName = it.name()
            nodeType = checkForNodeType(it.name())

            

            switch (nodeType) {
                //There is case statement defined for 4 cases
                // Each case statement section has a break condition to exit the loop

            case "S":
                resultBody += UNA6+getEDISegmentName(segmentName) + UNA2
                previousSegementIsD = "false"
                break;
            case "G":
                previousSegementIsD = "false"
                break;
            case "C":
                resultBody += UNA2
                previousSegementIsD = "false"
                break;
            case "D":

                println(previousSegementIsD)
                if (previousSegementIsD == "false") {
                    resultBody += it

                } else if(previousSegementIsD == "true") {
                    
                    resultBody += ":"+it
                }
                previousSegementIsD = "true"

            break;
    default:
        println("The value is unknown");
        break;
    }
}


message.setBody(resultBody)

return message;
}

private String checkForNodeType(String value) {
    if (value) {
        leadingString = value.split('_')[0]
    } else {
        //tbd. Exception
        println("fail")
    }

}

private String getEDISegmentName(String value) {
    if (value) {
        leadingString = value.split('_')[1]
    } else {
        //tbd. Exception
        println("fail")
    }

}
