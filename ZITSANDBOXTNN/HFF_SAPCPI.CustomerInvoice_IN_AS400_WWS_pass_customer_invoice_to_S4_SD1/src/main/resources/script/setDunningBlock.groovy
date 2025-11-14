import com.sap.it.api.mapping.*;


def String setDunningBlock(String sUMSP, String sURGART) {
    
   String sDunningBlock = ""
   
   def aValues = [sUMSP, sURGART]
   
   switch (aValues){
       
       case {it == ["1", ""]}:
           
           sDunningBlock = ""
           break
       
       case {it == ["1", "G"]}:
           
           sDunningBlock = "R"
           break
       
        case {it == ["0",""]}:
           
           sDunningBlock = "R"
           break
       
       case {it == ["0", "G"]}:
       
          sDunningBlock = "R"
          break
   
        default:
        
            sDunningBlock = "Mahnsperrenmapping fehlgeschlagen"
   }
    return sDunningBlock
}
