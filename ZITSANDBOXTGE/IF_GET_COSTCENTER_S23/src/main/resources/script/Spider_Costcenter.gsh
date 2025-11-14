import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.*


def Message processData(Message message) {
    
    // XML-Input einlesen
    def body = message.getBody(String)
    def xml = new XmlSlurper().parseText(body)
    
    // Initialisiere Ergebnis
    def result = new StringBuilder()
    
    xml.A_CostCenterType.each { c ->
        def costCenter = c.CostCenter.text()
        def controllingArea = c.ControllingArea.text()
        def companyCode = c.CompanyCode.text()
        def validityStartDate = c.ValidityStartDate.text()
        def validityEndDate = c.ValidityEndDate.text()
        def costCtrResponsibleUser = c.CostCtrResponsibleUser.text()
        def department = c.Department.text()
        def blockedPrimaryCosts = c.IsBlockedForPlanPrimaryCosts.text()
        def blockedPlanRevenue = c.IsBlockedForPlanRevenues.text()
        result.append("$costCenter;$controllingArea;$validityStartDate;$validityEndDate;$costCtrResponsibleUser;$department;$blockedPrimaryCosts;$blockedPlanRevenue\n")
    }
    
    // TXT-Output setzen
    message.setBody(result.toString())
    return message
}
