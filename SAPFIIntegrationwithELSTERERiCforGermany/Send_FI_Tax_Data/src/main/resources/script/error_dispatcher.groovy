import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    //Body 
       def props = message.getProperties();
       def stepId = props.get("SAP_ErrorModelStepID");
       
       def handler = "default";
       
       switch (stepId) {
         case '': handler = 'default'; break;
         
         case 'CallActivity_35': handler = 'default'; break;
         
         case 'MessageFlow_443': handler = 'elster'; break;
         case 'MessageFlow_393': handler = 'elster'; break;
         case 'MessageFlow_392': handler = 'elster'; break;
         
         case 'ServiceTask_20': handler = 'elster'; break;
         case 'ServiceTask_40': handler = 'elster'; break;
         case 'ServiceTask_441': handler = 'elster'; break;
         
         default: handler = 'default';
       }
       props.put('ErrorHandler', handler);
    
       return message;
}