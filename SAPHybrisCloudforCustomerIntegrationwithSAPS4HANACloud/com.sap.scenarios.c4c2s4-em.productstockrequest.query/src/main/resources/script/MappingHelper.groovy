import com.sap.it.api.mapping.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import com.sap.gateway.ip.core.customdev.util.Message;

def String convertDecimal(String decimalIn, MappingContext context) {
  try {
    return new BigDecimal(decimalIn).toPlainString() // get rid of exponential representation
  } catch (NumberFormatException e) {
    return decimalIn; // map through
  }
}

def Message encodeParameters(Message message) {

  def map = message.getProperties()

  // encode material
  def materialNumber = map.get("p_matno")
  String encodedMaterialNumber = URLEncoder.encode(materialNumber, "UTF-8")
  message.setProperty("p_matno", encodedMaterialNumber)

  // encode stock type
  def stockType = map.get("p_stocktype")
  String encodedStockType = URLEncoder.encode(stockType, "UTF-8")
  message.setProperty("p_stocktype", encodedStockType)

  // encode stock type
  def specialStockType = map.get("p_specialStocktype")
  if (specialStockType != null) {
    String encodedSpecialStockType = URLEncoder.encode(specialStockType, "UTF-8")
    message.setProperty("p_specialStocktype", encodedSpecialStockType)
  }

  return message
}