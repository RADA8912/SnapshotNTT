import com.sap.aii.mapping.api.*;
import com.sap.it.api.mapping.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;
public void valuePairResult (String[] contextValues, String[] searchValues, String[] resultValues, Output result) {
boolean somethingAdded;
if (contextValues != null && contextValues.length > 0) {
for (int i = 0; i < contextValues.length; i++) {
String id = contextValues[i];
somethingAdded = false;
for (int j = 0; j < searchValues.length; j++) {
String ref = searchValues[j];
if(id.equals(ref)){
result.addValue(resultValues[j]);
somethingAdded = true;
break
}
}
if (somethingAdded==false){
result.addValue("false");
}
}
}
}
public void valuePairBasedGeneric(String[] contextValues,String[] searchValues, Output result) {
boolean somethingAdded;
if (contextValues != null && contextValues.length > 0) {
for (int i = 0; i < contextValues.length; i++) {
String id = contextValues[i];
somethingAdded = false;
for (int j = 0; j < searchValues.length; j++) {
String ref = searchValues[j];
if(id.equals(ref)){
result.addValue("true");
somethingAdded = true;
break
}
}
if (somethingAdded==false){
result.addValue("false");
}
}
}
}