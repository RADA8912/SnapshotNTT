import com.sap.it.api.mapping.*;
import java.util.UUID; 
import java.util.Arrays;
import java.util.Set;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;

def String getUUID(String header, MappingContext context){
    return UUID.randomUUID().toString();
}   

def String getAddressUUID(String header, MappingContext context){
    return context.getProperty("addressUUID");
} 

def String getBPGroupCode(String header, MappingContext context){
    return context.getProperty("bpGroupCode");
} 

def String removeLeadingZeros(String header, MappingContext context){
    header = header.replaceAll("^0+", "");
    return  header;
} 
