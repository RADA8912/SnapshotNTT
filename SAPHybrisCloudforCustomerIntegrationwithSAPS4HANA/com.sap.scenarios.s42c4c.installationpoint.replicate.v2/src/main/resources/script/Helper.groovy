import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap

def Message processData(Message message) {

  //Properties 
  def map = message.getProperties()
  def createdByUser = map.get("p_CreatedByUser")
  def eventType = map.get("p_eventType")
  def commUser = map.get("P_CommUser")

  if (commUser.length() > 0 && eventType.equals('sap.s4.beh.functionallocation.v1.FunctionalLocation.Created.v1') && createdByUser.equals(commUser)) {
    message.setProperty("p_ReplicateToC4C", "N")
  }

  return message
}