import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.xml.*;
import java.io.*;

/**
 * This script removes the first two lines of the CSV input as these only are descpriptive within the
 * file. Furthermore, the invoice year is read before the drop to have that present for the 
 * message mapping.
 * @param message
 * @return message
 */

def Message processData(Message message)
{
    def body = message.getBody(String)

    //Retrieve first row and store the timestamp from column M
    firstRow = body.split('\n')[0]
    yearTimestamp = firstRow.split(';')[12]
    
    yearForMapping = yearTimestamp.substring(0, 4)
    //Set the year accordingly
    message.setProperty("Z_InvoiceYear", yearForMapping)
    message.setBody(body.split('\n').drop(2).join('\n'))
    return message



}

