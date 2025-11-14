import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*;
import com.sap.it.api.pd.PartnerDirectoryService;
import com.sap.it.api.ITApiFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.osgi.framework.FrameworkUtil;
import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.it.op.b2b.monitor.api.*;
import com.sap.it.op.b2b.monitor.api.events.*;
import java.nio.charset.StandardCharsets;

def Message processData(Message message) {
    def service = ITApiFactory.getApi(PartnerDirectoryService.class, null);
    if(service == null){
        throw new IllegalStateException("Partner Directory Service not found");
    }

    def headers                      =   message.getHeaders();

	// Test values ------------------------------------------------------------------
	String sndDocumentStandard = 'EDIFACT'
	String sndMessageType = 'ORDERS96A'
	String sndInterchangeControlNr = '000022'
	String sndMessageControlNr = '470000011'

	String sndSenderId = '4388591131997'
	String sndSenderIdQualifier = 'UNEDIFACT'
	String tpaSenderTpId = ''
	String tpaSenderTpName = 'FG GmbH 413'
	
	String sndReceiverId = 'NDBS'
	String sndReceiverIdQualifier = 'UNEDIFACT'
	String tpaReceiverTpId = ''
	String tpaReceiverTpName = 'NTT Data Business Solutions AG'

    String tpaAgreementName = ''
    String tpaBTName = ''
    String tpaBTTypeName = ''
    String tpaBTActivityName = ''
	String tpaBTActivityDirection = ''

	String sndAdapterType = 'AS2'
	// End of Test values ------------------------------------------------------------------


    // set general trading partner agreement properties for monitoring.
    message.setProperty("SAP_TPA_Name", tpaAgreementName);
    message.setProperty("SAP_TPA_BT_Name", tpaBTName);
    message.setProperty("SAP_TPA_BT_Type", tpaBTTypeName);
    message.setProperty("SAP_TPA_BTA_Name", tpaBTActivityName);
    message.setProperty("SAP_TPA_BTA_Direction", tpaBTActivityDirection);
    message.setProperty("SAP_TPA_SND_Trading_Partner_ID", tpaSenderTpId);
    message.setProperty("SAP_TPA_SND_Trading_Partner_Name", tpaSenderTpName);
    message.setProperty("SAP_TPA_REC_Trading_Partner_ID", tpaReceiverTpId);
    message.setProperty("SAP_TPA_REC_Trading_Partner_Name", tpaReceiverTpName);

    // Create event entry in monitoring queue

    def bundleContext = FrameworkUtil.getBundle(Class.forName("com.sap.gateway.ip.core.customdev.util.Message")).getBundleContext();
    def serviceRef = bundleContext.getServiceReference(Class.forName("com.sap.it.op.b2b.monitor.api.B2BMonitoringApi"));
    B2BMonitoringApi api = (B2BMonitoringApi) bundleContext.getService(serviceRef);

    BusinessDocumentCreateEvent documentCreateEvent = api.createBusinessDocumentCreateEvent();
    // no builder or "with..." pattern
    // for manually written mappings, the attributes are then nicely aligned underneath each other
    documentCreateEvent.setMonitoringReference(headers.get("SAP_MessageProcessingLogID"));
    documentCreateEvent.setMonitoringReferenceType(MonitoringReferenceType.MPL);



    BusinessDocument document = documentCreateEvent.createBusinessDocument();
    // for TPM we would expect a rather generic transfer of the exchange headers
    // this could be achieved by maintaining a map of function pointers and property or header
    // names and then iterating over the entries and call something like
    // key.accept(exchange.getProperty(value)

    def documentId = document.getId();
    message.setProperty("Document_ID", documentId);

    document.setSenderDocumentStandard(sndDocumentStandard);
    document.setSenderMessageType(sndMessageType);
    document.setSenderAdapterType(sndAdapterType);
    document.setSenderInterchangeControlNumber(sndInterchangeControlNr);
    document.setSenderMessageNumber(sndMessageControlNr)
    document.setSenderTradingPartnerName(tpaSenderTpName);
    document.setReceiverTradingPartnerName(tpaReceiverTpName);
    document.setAgreedSenderIdAtSender(sndSenderId);
    document.setAgreedSenderIdQualifierAtSender(sndSenderIdQualifier);
    document.setAgreedReceiverIdAtSender(sndReceiverId);
    document.setAgreedReceiverIdQualifierAtSender(sndReceiverIdQualifier);
    document.setTransactionDocumentType(BusinessTransactionDocumentType.REQUEST);
    document.setProcessingStatus(ProcessingStatus.PROCESSING);
    document.setSenderFunctionalAckStatus(FunctionalAcknowledgementStatus.EXPECTED);
    document.setReceiverFunctionalAckStatus(FunctionalAcknowledgementStatus.NOT_EXPECTED);

    documentCreateEvent.submit();

    return message;
}
