import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.util.Map;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.mapping.ValueMappingApi;

def Message processData(Message message) { 

  def a = ITApiFactory.getService(ValueMappingApi.class, null);
  def headers = message.getHeaders();
  def headervalue = headers.get('HeaderValue');
  def mappedValue = a.getMappedValue('Frucht', 'US', headervalue, 'Frucht', 'DE');
  def messageLog = messageLogFactory.getMessageLog(message);
  messageLog.setStringProperty('MappedValue', mappedValue);
  message.setHeader('ValueMapped', mappedValue);
  return message;
  }