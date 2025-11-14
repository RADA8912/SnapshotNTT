import org.osgi.framework.FrameworkUtil;
import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.it.op.b2b.monitor.api.*;
import com.sap.it.op.b2b.monitor.api.events.*;
import java.nio.charset.StandardCharsets;

def Message processData(Message message) {

    def headers = message.getHeaders();
    def sndAdapterType = headers.get("SAP_COM_SND_Adapter_Type");
    def sndDocumentStandard = headers.get("SAP_EDI_Document_Standard");
    def sndInterchangeControlNr = headers.get("SAP_EDI_Interchange_Control_Number");
    def sndMessageControlNr = headers.get("SAP_EDI_Message_Number");
    def sndMessageType = headers.get("SAP_EDI_Message_Type");
    def sndReceiverId = headers.get("SAP_EDI_Receiver_ID");
    def sndReceiverIdQualifier = headers.get("SAP_EDI_Receiver_ID_Qualifier");
    def sndSenderId = headers.get("SAP_EDI_Sender_ID");
    def sndSenderIdQualifier = headers.get("SAP_EDI_Sender_ID_Qualifier");

    if (sndAdapterType == null) {
        throw new IllegalStateException("Mandatory value sndAdapterType not provided.");
    } else {
        sndAdapterType = sndAdapterType.trim();
    }

    if (sndDocumentStandard == null) {
        throw new IllegalStateException("Mandatory value sndDocumentStandard not provided.");
    } else {
        sndDocumentStandard = sndDocumentStandard.trim();
    }

    if (sndMessageType == null) {
        throw new IllegalStateException("Mandatory value sndMessageType not provided.");
    } else {
        sndMessageType = sndMessageType.trim();
    }

    if (sndReceiverId == null) {
        throw new IllegalStateException("Mandatory value sndReceiverId not provided.");
    } else {
        sndReceiverId = sndReceiverId.trim();
    }

    if (sndReceiverIdQualifier == null) {
        sndReceiverIdQualifier = "";
    } else {
        sndReceiverIdQualifier = sndReceiverIdQualifier.trim();
    }

    if (sndSenderId == null) {
        throw new IllegalStateException("Mandatory value sndSenderId not provided.");
    } else {
        sndSenderId = sndSenderId.trim();
    }

    if (sndSenderIdQualifier == null) {
        sndSenderIdQualifier = "";
    } else {
        sndSenderIdQualifier = sndSenderIdQualifier.trim();
    }

    // get general trading partner agreement parameters for monitoring.
    def tpaBTTypeName              = message.getProperty("SAP_TPA_BT_Type");
    def tpaSenderTpName            = message.getProperty("SAP_TPA_SND_Trading_Partner_Name");
    def tpaReceiverTpName          = message.getProperty("SAP_TPA_REC_Trading_Partner_Name");

    def recDocumentStandard = message.getProperty("SAP_EDI_REC_Document_Standard");
    def recMsgType = message.getProperty("SAP_EDI_REC_Message_Type");
    def recAdapterType = message.getProperty("SAP_COM_REC_Adapter_Type");
    def recInterchangeControlNumber = message.getProperty("SAP_EDI_REC_Interchange_Control_Number");
    def recMsgNumber = message.getProperty("SAP_EDI_REC_Message_Number");
    def recSndReferenceID = message.getProperty("SAP_EDI_REC_Sender_ID");
    def recSndReferenceIDScheme = message.getProperty("SAP_EDI_REC_Sender_ID_Qualifier");
    def recRecReferenceID = message.getProperty("SAP_EDI_REC_Receiver_ID");
    def recRecReferenceIDScheme = message.getProperty("SAP_EDI_REC_Receiver_ID_Qualifier");

    // Create event entry in monitoring queue

    def bundleContext = FrameworkUtil.getBundle(Class.forName("com.sap.gateway.ip.core.customdev.util.Message")).getBundleContext();
    def serviceRef = bundleContext.getServiceReference(Class.forName("com.sap.it.op.b2b.monitor.api.B2BMonitoringApi"));
    B2BMonitoringApi api = (B2BMonitoringApi) bundleContext.getService(serviceRef);

    BusinessDocumentMappedEvent documentMappedEvent = api.createBusinessDocumentMappedEvent();
    // no builder or "with..." pattern
    // for manually written mappings, the attributes are then nicely aligned underneath each other
    documentMappedEvent.setMonitoringReference(headers.get("SAP_MessageProcessingLogID"));
    documentMappedEvent.setMonitoringReferenceType(MonitoringReferenceType.MPL);



    BusinessDocument document = documentMappedEvent.createUpdateBusinessDocument(message.getProperty("Document_ID"));
    document.setSenderDocumentStandard(sndDocumentStandard);
    document.setReceiverDocumentStandard(recDocumentStandard);
    document.setSenderMessageType(sndMessageType);
    document.setReceiverMessageType(recMsgType);
    document.setSenderAdapterType(sndAdapterType);
    document.setReceiverAdapterType(recAdapterType);
    document.setSenderInterchangeControlNumber(sndInterchangeControlNr);
    document.setReceiverInterchangeControlNumber(recInterchangeControlNumber);
    document.setSenderMessageNumber(sndMessageControlNr);
    document.setReceiverMessageNumber(recMsgNumber);
    document.setSenderTradingPartnerName(tpaSenderTpName);
    document.setReceiverTradingPartnerName(tpaReceiverTpName);
    document.setAgreedSenderIdAtSender(sndSenderId);
    document.setAgreedSenderIdQualifierAtSender(sndSenderIdQualifier);
    document.setAgreedReceiverIdAtSender(sndReceiverId);
    document.setAgreedReceiverIdQualifierAtSender(sndReceiverIdQualifier);
    document.setAgreedSenderIdAtReceiver(recSndReferenceID);
    document.setAgreedSenderIdQualifierAtReceiver(recSndReferenceIDScheme);
    document.setAgreedReceiverIdAtReceiver(recRecReferenceID);
    document.setAgreedReceiverIdQualifierAtReceiver(recRecReferenceIDScheme);
    document.setTransactionDocumentType(BusinessTransactionDocumentType.REQUEST);
    document.setProcessingStatus(ProcessingStatus.PROCESSING);
    document.setSenderFunctionalAckStatus(FunctionalAcknowledgementStatus.EXPECTED);
    document.setReceiverFunctionalAckStatus(FunctionalAcknowledgementStatus.NOT_EXPECTED);

    // hierarchy is created bottom to top, which might be counter intuitive,
    // however the document is the main object for the BusinessDocument related events
    // for Beta, there is no transaction entry to be made, so we would only use the
    // name as part of the document, but then we could keep the API stable for GA

    BusinessTransaction transaction = document.createUpdateBusinessTransaction(message.getProperty("Transaction_ID"));
    transaction.setAgreementSequence(1);
    transaction.setTypeName(tpaBTTypeName);
    transaction.setInitiatorTradingPartnerName(tpaSenderTpName);
    transaction.setResponderTradingPartnerName(tpaReceiverTpName);
    transaction.setStatus(BusinessTransactionStatus.OPEN);

    TradingPartnerAgreement agreement = transaction.createUpdateAgreement(message.getProperty("Agreement_ID"));
    agreement.setInitiatorTradingPartnerName(tpaSenderTpName);
    agreement.setResponderTradingPartnerName(tpaReceiverTpName);
    agreement.setStatus(TradingPartnerAgreementStatus.OPEN);

    BusinessDocumentPayload inboundPayload = document.createPayload();
    inboundPayload.setDirection(message.getProperty("SAP_TPA_BTA_Direction"));

    inboundPayload.setProcessingPhase(ProcessingPhase.PAYLOAD_PLAIN);
    def body = message.getBody(java.lang.String) as String;
    inboundPayload.setPayload(body.getBytes(StandardCharsets.UTF_8));

    documentMappedEvent.submit();

    return message;
}