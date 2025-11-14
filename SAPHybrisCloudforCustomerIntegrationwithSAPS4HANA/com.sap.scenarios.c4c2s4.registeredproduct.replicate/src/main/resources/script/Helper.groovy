import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.MarkupBuilder;
import groovy.json.JsonSlurper

/**
    * If a partner function in C4C exists and not in S/4 then it is considered as a create (POST) case
    * If a partner function exists in both C4C and S/4 but the partner no is different, then it is considered as an update (PATCH) case
    * MarkupBuilder is used to construct the batch output. 
    * Each partner function to be added or updated are added to batch as a separate batchChangeSetPart under batchParts/batchChangeSet
    * URI for PATCH Method is of format:
    *       PATCH EquipmentPartner(Equipment='10006851',PartnerFunction='RE',EquipmentPartnerObjectNmbr='2') HTTP/1.1
    * URI for POST Method is of format:
    *       POST EquipmentPartner HTTP/1.1
*/

def Message processPartnerFunction(Message message) {

  //def body = message.getBody(String)
  //def s4Party = new XmlSlurper().parseText(body)
  
  def body = message.getBody(java.io.Reader)
  def s4Party = new XmlSlurper().parse(body)

  def map = message.getProperties()
  def equipmentNo = map.get("P_EquipmentNum")
  //def c4cParty = new XmlSlurper().parseText(map.get("P_C4C_InvolvedParties"))
  def c4cParty = new XmlSlurper().parse(map.get("P_C4C_InvolvedParties"))

  def patchlist = []
  def postlist = []

  for (party in c4cParty.Party) {

    def partnerRole = party.PartnerFunction.toString()
    def partnerNo = party.Partner.toString()
    def existingParty = false

    for (sparty in s4Party.EquipmentType.to_Partner.EquipmentPartnerType) {

      def s4PartnerRole = sparty.PartnerFunction.toString()
      def s4PartnerNo = sparty.Partner.toString()

      if (s4PartnerRole.equals(partnerRole) && s4PartnerNo.equals(partnerNo)) existingParty = true

      if (s4PartnerRole.equals(partnerRole) && !s4PartnerNo.equals(partnerNo)) {
        patchlist.add(party.PartnerFunction + sparty.EquipmentPartnerObjectNmbr + party.Partner)
        existingParty = true
      }

    }
    if (!existingParty) postlist.add(party.PartnerFunction + party.Partner)
  }

  def xmlWriter = new StringWriter()
  def xmlMarkup = new MarkupBuilder(xmlWriter)

  xmlMarkup.batchParts {
    batchChangeSet {
      for (e in patchlist) {
        batchChangeSetPart {
          method('PATCH')
          uri("EquipmentPartner(Equipment='" + equipmentNo + "',PartnerFunction='" + e[0] +"',EquipmentPartnerObjectNmbr='" + e[1] +"')")
          EquipmentPartner {
            EquipmentPartnerType {
              Partner(e[2])
            }
          }
        }

      }
      for (e in postlist) {
        batchChangeSetPart {
          method('POST')
          uri('EquipmentPartner')
          EquipmentPartner {
            EquipmentPartnerType {
              Equipment(equipmentNo)
              PartnerFunction(e[0])
              Partner(e[1])
            }
          }
        }
      }
    }
  }

  String result = xmlWriter.toString()
  message.setBody(result)
  return message

}

/**
 * Creates Equipment long text batch payload
 * Format : PATCH EquipmentLongText(Equipment='10006844') HTTP/1.1
 * 
*/

def Message processTextCollection(Message message) {
    
  def body = message.getBody(java.io.Reader)
  def content = new XmlSlurper().parse(body)
  
  def textContent = content.Text.ContentText.toString()

  def map = message.getProperties()
  def equipmentNo = map.get("P_EquipmentNum")

  def xmlWriter = new StringWriter()
  def xmlMarkup = new MarkupBuilder(xmlWriter)

  xmlMarkup.batchParts {
    batchChangeSet {
      if (textContent.size() > 0) batchChangeSetPart {
        method('PATCH')
        uri("EquipmentLongText(Equipment='" + equipmentNo + "')")
        EquipmentLongText {
          EquipmentLongTextType {
            EquipmentLongText(textContent)
          }
        }
      }
    }
  }

  String result = xmlWriter.toString()
  message.setBody(result)
  return message

}

/**
 * Equipment Hierarchy Processing 
 * If C4C ReceiverUpperInstallationPointID exists and S/4 SuperordinateEquipment is initial - Do an Installation
 * If S/4 SuperordinateEquipment exists and C4C ReceiverUpperInstallationPointID is initial - Do a Dismantle
 * If both exists and the values are not equal, first Dismantle (existing SuperordinateEquipment) and then install (C4C ReceiverUpperInstallationPointID)
*/

def Message processHierachyRelationship(Message message) {

  def map = message.getProperties()
  def c4c_parentequipment = map.get("P_ReceiverUpperInstallationPointID")
  def s4_parentequipment = map.get("P_SuperordinateEquipment")
  def s4_equipmentNo = map.get("P_EquipmentNum")
  def s4_functionalLocation = map.get("P_FunctionalLocation")
  def equipInstallationPositionNmbr = map.get("P_InstallationPointPosition")
  def InstallationPointTypeCode = map.get("P_InstallationPointTypeCode")
  def etag = map.get("P_batch_etag")

  if (equipInstallationPositionNmbr.isEmpty())
    equipInstallationPositionNmbr = "&EquipInstallationPositionNmbr='" + "0000" + "'"
  else
    equipInstallationPositionNmbr = "&EquipInstallationPositionNmbr='" + equipInstallationPositionNmbr + "'"

  def xmlWriter = new StringWriter()
  def xmlMarkup = new MarkupBuilder(xmlWriter)

  xmlMarkup.batchParts {
    if (c4c_parentequipment.size() > 0) {
      if (!c4c_parentequipment.equalsIgnoreCase(s4_parentequipment) &&
        InstallationPointTypeCode.equalsIgnoreCase("2") &&
        (s4_parentequipment.size() > 0 || (s4_parentequipment.size() == 0 && s4_functionalLocation.size() > 0))) {

        batchChangeSet {
          batchChangeSetPart {
            method('POST')
            uri("DismantleEquipment?Equipment='" + s4_equipmentNo + "'&ValidityEndDate=datetime%279999-12-31T00%3A00%3A00%27")
            headers {
              header {
                headerName("If-Match")
                headerValue(etag)
              }
            }
          }
        }

        batchChangeSet {
          batchChangeSetPart {
            method('POST')
            uri("InstallEquipment?Equipment='" +
              s4_equipmentNo +
              "'&ValidityEndDate=datetime%279999-12-31T00%3A00%3A00%27&SuperordinateEquipment='" +
              c4c_parentequipment +
              "'" +
              equipInstallationPositionNmbr)
            headers {
              header {
                headerName("If-Match")
                headerValue("*")
              }
            }
          }
        }
      } else if (!c4c_parentequipment.equalsIgnoreCase(s4_functionalLocation) &&
        InstallationPointTypeCode.equalsIgnoreCase("6") &&
        (s4_functionalLocation.size() > 0 || (s4_functionalLocation.size() == 0 && s4_parentequipment.size() > 0))) {

        batchChangeSet {
          batchChangeSetPart {
            method('POST')
            uri("DismantleEquipment?Equipment='" + s4_equipmentNo + "'&ValidityEndDate=datetime%279999-12-31T00%3A00%3A00%27")
            headers {
              header {
                headerName("If-Match")
                headerValue(etag)
              }
            }
          }
        }

        batchChangeSet {
          batchChangeSetPart {
            method('POST')
            uri("InstallEquipment?Equipment='" +
              s4_equipmentNo +
              "'&ValidityEndDate=datetime%279999-12-31T00%3A00%3A00%27&FunctionalLocation='" +
              c4c_parentequipment +
              "'" +
              equipInstallationPositionNmbr)
            headers {
              header {
                headerName("If-Match")
                headerValue("*")
              }
            }
          }
        }

      }
    }

    if (c4c_parentequipment.size() > 0) {
      if (s4_parentequipment.size() == 0 && InstallationPointTypeCode.equalsIgnoreCase("2") && s4_functionalLocation.size() == 0) {
        batchChangeSet {
          batchChangeSetPart {
            method('POST')
            uri("InstallEquipment?Equipment='" +
              s4_equipmentNo +
              "'&ValidityEndDate=datetime%279999-12-31T00%3A00%3A00%27&SuperordinateEquipment='" +
              c4c_parentequipment +
              "'" +
              equipInstallationPositionNmbr)
            headers {
              header {
                headerName("If-Match")
                headerValue(etag)
              }
            }
          }
        }

      } else if (s4_functionalLocation.size() == 0 && InstallationPointTypeCode.equalsIgnoreCase("6") && s4_parentequipment.size() == 0) {
        batchChangeSet {
          batchChangeSetPart {
            method('POST')
            uri("InstallEquipment?Equipment='" +
              s4_equipmentNo +
              "'&ValidityEndDate=datetime%279999-12-31T00%3A00%3A00%27&FunctionalLocation='" +
              c4c_parentequipment +
              "'" +
              equipInstallationPositionNmbr)
            headers {
              header {
                headerName("If-Match")
                headerValue(etag)
              }
            }
          }
        }

      }
    }

    if ((c4c_parentequipment.size() == 0 && s4_parentequipment.size() > 0) || (c4c_parentequipment.size() == 0 && s4_functionalLocation.size() > 0)) {
      batchChangeSet {
        batchChangeSetPart {
          method('POST')
          uri("DismantleEquipment?Equipment='" + s4_equipmentNo + "'&ValidityEndDate=datetime%279999-12-31T00%3A00%3A00%27")
          headers {
            header {
              headerName("If-Match")
              headerValue(etag)
            }
          }
        }
      }
    }
  }

  String result = xmlWriter.toString()
  message.setBody(result)
  return message

}


/**
 * Parse the ODATA batch response to extract the message
 * Note the batch response is XML inside XML
 * batchPartResponse is the outer XML root node and body is the inner XML root node
 * Message text is available inside the error/message/text of inner XML
*/

def Message throwBatchProcessingException(Message message) {

  //def body = message.getBody(String)
  def body = message.getBody(java.io.Reader)
  def errorBody = null

  try {
    //def batchPartResponse = new XmlSlurper().parseText(body)
    def batchPartResponse = new XmlSlurper().parse(body)
    errorBody = new XmlSlurper().parseText(batchPartResponse.batchChangeSetResponse.batchChangeSetPartResponse.body.text())
  }
  catch(Exception exception) {
    throw new Exception('Batch Processing Exception' + exception)
  }
  
   if (errorBody != null) throw new Exception(errorBody.message.text())

  return message
}

/**
 * For ODATA post error get the error message text from the ODATA response body and populate it in MPL
 * For Other exceptions return as is
*/

def Message throwODATACreateException(Message message) {

  def body = message.getBody(java.io.Reader)
  def map = message.getProperties();
  def ex = map.get("CamelExceptionCaught")
  def errorBody = null

  if (ex != null) {

    if (ex.getClass().getCanonicalName().equals("com.sap.gateway.core.ip.component.odata.exception.OsciException")) {

      try {
        errorBody = new XmlSlurper().parse(body)
      } catch (Exception exception) {
        throw new Exception(exception)
      }

      if (errorBody.message != null) throw new Exception(errorBody.message.text())
    } else throw new Exception(ex)
  }

  return message
}

/**
 * Check for Application sequencing 
 * If the sender sequence no in the incoming message is older than the sequence no in data store end message processing with status error.
 * Also set custom status in MPL as Obsolete to make it more transparent
 * If the ReceiverInstallationPointID is not available in the incoming message (can happen if message was restarted after a failure), check if a datastore entry for the C4C equipment exists,
 * if yes, then populate the ReceiverInstallationPointID for the PATCH call.
 * The check for sequence no is only required if data store entry is found.
*/

def Message checkAppSequencing(Message message) {

  def dataStoreEntryFound = message.getHeader("SAP_DataStoreEntryFound", String)

  if (dataStoreEntryFound == 'true') {

    def body = message.getBody(String)
    def sequencingData = new JsonSlurper().parseText(body)

    def map = message.getProperties()
    def currentSequenceNo = map.get("P_SenderSequenceNo")
    def oldSequenceNo = sequencingData.SenderSequenceNumberValue

    def df = "yyyyMMddHHmmssSSS"

    def oldTimeStamp = new Date().parse(df, oldSequenceNo)
    def newTimeStamp = new Date().parse(df, currentSequenceNo)

    if (newTimeStamp.compareTo(oldTimeStamp) < 0) {
      def messageLog = messageLogFactory.getMessageLog(message)
      if (messageLog != null) {
        messageLog.addCustomHeaderProperty("Status", "Obsolete")
      }
      throw new Exception('A newer message has already been processed for the Equipment ' + sequencingData.InstallationPointID)
    }

    def s4EquipmentNo = map.get("P_EquipmentNum")
    if (s4EquipmentNo.isEmpty()) message.setProperty("P_EquipmentNum", sequencingData.ReceiverInstallationPointID)

  }

  return message
}