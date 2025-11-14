import com.sap.it.api.mapping.*;
import java.math.BigDecimal;

def String convertDecimal(String decimalIn, MappingContext context){
    try {
        return new BigDecimal(decimalIn).toPlainString(); // get rid of exponential representation
    } catch(NumberFormatException e) {
        return decimalIn; // map through
    }
}