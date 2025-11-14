import com.sap.it.api.mapping.*;

def String getMessageID(String arg1,MappingContext context){
    String messageID = java.util.UUID.randomUUID().toString()
	//context.setHeader("SapMessageId", messageID.toUpperCase().replaceAll("-",""))
	//context.setHeader("SapMessageIdEx", messageID)	
	return messageID.toUpperCase().replaceAll("-","") 
}

def String formatUUID(String uuid){	
	return (uuid.substring(0,8)+"-"+uuid.substring(8, 12)+"-"+uuid.substring(12,16)+"-"+uuid.substring(16,20)+"-"+uuid.substring(20));
}

def String SystemID(String systemID, MappingContext context){
	return context.getProperty(systemID);
}

def String determineStatus(String EquipmentIsMarkedForDeletion,String EquipmentIsAtCustomer,String EquipmentIsAvailable,String EquipmentIsInWarehouse,String EquipmentIsAssignedToDelivery,String EquipmentIsInstalled,String EquipIsAllocToSuperiorEquip,String EquipmentIsInactive, MappingContext context){
   
        if(EquipmentIsMarkedForDeletion == "true")
         return "4";
         
        else if (EquipmentIsAtCustomer == "true" || EquipmentIsAvailable == "true" || EquipmentIsInWarehouse == "true" || EquipmentIsAssignedToDelivery == "true" || EquipmentIsInstalled == "true" || EquipIsAllocToSuperiorEquip == "true" || EquipmentIsInactive == "false") 
         return "2";
        
        else 
         return "1";
}

def String getSenderSequenceNo(String lastChangeDateTime,MappingContext context){
    //The etag from S/4 is of format W/"'SADL-020201207134153C~20201207134153'" and we extract the timestamp 20201207134153 using substring
   def _etag = context.getHeader('etag')
    if (_etag != null)
        return _etag.substring(26,40)
    else
        return lastChangeDateTime
}