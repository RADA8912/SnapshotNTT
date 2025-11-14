import com.sap.it.api.mapping.*;

def String getMessageID(String arg1,MappingContext context){
    String messageID = java.util.UUID.randomUUID().toString()
	//context.setHeader("SapMessageId", messageID.toUpperCase().replaceAll("-",""))
	//context.setHeader("SapMessageIdEx", messageID)	
	return messageID.toUpperCase().replaceAll("-","") 
}

def String formatUUID(String uuid){	
	return (uuid.substring(0,8)+"-"+uuid.substring(8, 12)+"-"+uuid.substring(12,16)+"-"+uuid.substring(16,20)+"-"+uuid.substring(20))
}

def String SystemID(String systemID, MappingContext context){
	return context.getProperty(systemID)
}

def String getWarrantyType(String inarg, MappingContext context){
	return context.getProperty("P_WarrantyType")
}

// Save the S/4 Equipment no into Property P_EquipmentNum and read the C4C equipmentNo from Property P_InstallationPointID
def String InstallationPointID(String equipmentNo, MappingContext context){
    context.setProperty("P_EquipmentNum",equipmentNo)
	return context.getProperty("P_InstallationPointID")
}

def String determineStatus(String EquipmentIsMarkedForDeletion,String EquipmentIsAtCustomer,String EquipmentIsAvailable,String EquipmentIsInWarehouse,String EquipmentIsAssignedToDelivery,String EquipmentIsInstalled,String EquipIsAllocToSuperiorEquip,String EquipmentIsInactive, MappingContext context){
   
        if(EquipmentIsMarkedForDeletion == "true")
         return "4"
         
        else if (EquipmentIsAtCustomer == "true" || EquipmentIsAvailable == "true" || EquipmentIsInWarehouse == "true" || EquipmentIsAssignedToDelivery == "true" || EquipmentIsInstalled == "true" || EquipIsAllocToSuperiorEquip == "true" || EquipmentIsInactive == "false") 
         return "2"
        
        else 
         return "1"
}      

def String getEquipmentURI(String arg1,MappingContext context){
    //PUT Equipment(Equipment='10180054',ValidityEndDate=datetime'9999-12-31T00%3A00%3A00') HTTP/1.1
    def uri = "Equipment(Equipment='" + context.getProperty('P_EquipmentNum') + "',ValidityEndDate=datetime'9999-12-31T00%3A00%3A00')"
	return uri 
}

def String checkIfInstallationRequired(String ID,String Type, MappingContext context){
    
    def receiverUpperInstallationPointID = context.getProperty("P_ReceiverUpperInstallationPointID")
    def functionalLocation = context.getProperty("P_FunctionalLocation")
    def superordinateEquipment = context.getProperty("P_SuperordinateEquipment")
    
    if(receiverUpperInstallationPointID.isEmpty() && functionalLocation.size().isEmpty() &&  superordinateEquipment.size().isEmpty() ){
        context.setProperty("P_InstalltionRequired",'false')
         return ""
    }
    
    if(Type.equalsIgnoreCase("2")){
        if(receiverUpperInstallationPointID.equalsIgnoreCase(superordinateEquipment)){
            context.setProperty("P_InstalltionRequired",'false')
             return ""
        }
    }
    
    if(Type.equalsIgnoreCase("6")){
        if(receiverUpperInstallationPointID.equalsIgnoreCase(functionalLocation)){
            context.setProperty("P_InstalltionRequired",'false')
             return ""
        }
    }
    
    context.setProperty("P_InstalltionRequired",'true')
    return ""
}

def String getSenderSequenceNo(String dummy,MappingContext context){
    //The etag from S/4 is of format W/"'SADL-020201207134153C~20201207134153'" and we extract the timestamp 20201207134153 using substring
    def _etag = context.getHeader('etag')
    return _etag.substring(26,40)
}

def String getEtag(String sequenceno,MappingContext context){
   //return "W/\"'SADL-0" +  sequenceno + "C~"+sequenceno + "'\""
   return context.getHeader('etag')
}

def String getEquipmentLongTextURI(String arg1,MappingContext context){
    //PATCH EquipmentLongText(Equipment='10180054') HTTP/1.1
    def uri = "EquipmentLongText(Equipment='" + context.getProperty('P_EquipmentNum') + "')"
	return uri 
}