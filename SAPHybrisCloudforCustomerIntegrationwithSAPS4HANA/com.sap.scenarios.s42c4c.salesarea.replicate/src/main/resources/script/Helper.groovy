import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.*;
import groovy.util.*;

def Message processData(Message message) {
  //Body
  def body = message.getBody(String)
  def xml = new XmlSlurper().parseText(body)

  def orgHierarchyMap = [: ]

  xml.
  '**'.findAll {
    it.name().equals('OrganizationHierarchyReplicationRequest')
  }.each {
    orgHierarchyNode ->

      // Get the OrganizationCentreID
      def orgCentreID = orgHierarchyNode.OrganizationHierarchy.OrganizationCentreID.text()
    if (orgCentreID) {

      def distributionChannelNodes = orgHierarchyNode.OrganizationHierarchy.DistributionChannelAndDivision

      if (orgHierarchyMap.containsKey(orgCentreID)) {

        def getNode = orgHierarchyMap[orgCentreID]

        getNode.OrganizationHierarchy.appendNode(distributionChannelNodes)

        orgHierarchyMap[orgCentreID] = getNode

      } else {

        orgHierarchyMap[orgCentreID] = orgHierarchyNode
      }

    }
  }

  def modifiedXml = new XmlSlurper(false, false).parseText('<ns6:OrganizationHierarchyReplicationRequest xmlns:ns6="http://sap.com/xi/SAPGlobal20/Global"></ns6:OrganizationHierarchyReplicationRequest>')

  modifiedXml.appendNode(xml.MessageHeader)
  orgHierarchyMap.each {
    key,
    value ->
    modifiedXml.appendNode(value)

  }

  message.setBody(XmlUtil.serialize(modifiedXml))
  return message
}