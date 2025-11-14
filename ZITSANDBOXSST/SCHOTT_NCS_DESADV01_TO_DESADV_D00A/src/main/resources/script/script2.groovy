/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/en-GB/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/en-GB/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.xml.*
class IdocSegmentTracer {
    def segments

    IdocSegmentTracer(def xmlRoot) {
        // Collect all segments with HLNUM and PARENT
        segments = xmlRoot.'**'.findAll { it.VENUM && it.PARENT }
    }

    String findRootVENUM(String startVENUM) {
        return traceToRoot(startVENUM)
    }

    def findSegmentByVENUM(String VENUM) {
        return segments.find { it.VENUM.text() == VENUM }
    }

    private String traceToRoot(String VENUM) {
        def segment = findSegmentByVENUM(VENUM)
        if (!segment || !segment.PARENT.text()?.trim()) {
            return VENUM
        } else {
            return traceToRoot(segment.PARENT.text())
        }
    }
}


def Message processData(Message message) {
    //get encoded original message 
    def originalEncodeMessage = message.getProperty('Original_Encoded_Message');
    
    //decode original message
    def originalDecodeMessage = Base64.decoder.decode(originalEncodeMessage)
    def originalMessage = new String(originalDecodeMessage , "UTF-8")
    
    //get xml format
    def xml = new XMLSlurper().parseText(originalMessage)
    
    //trace to pallet
    def segTracer = new IdocSegmentTracer(xml)
    
    //get segments that do not have a PARENT segment
    def segParent = xml.**.E1EDP08.findAll { it.PARENT.size() > 0 && it.PARENT.text().trim() } 
    
    //pallet counts
    def palletSet = [:]
    
    segParent.each { segment ->
        def exidv = segment.EXIDV?.text()?.trim()
        def pckar = segment.PCKAR?.text()?.trim()

        if (exidv && !palletSet.containsKey(exidv)) {
            palletSet[exidv] = pckar
        }
    }
    
    //get unique batches
    def segBatch = xml.**.E1EDP09.findAll { it.CHARG.size() > 0 && it.CHARG.text().trim() }
    

    // Traverse E1EDP08 segments with ANZAR != "00000"
    def filteredSegments = xml.'**'.findAll { 
        it.name() == 'E1EDP08' && it.ANZAR?.text()?.trim() != 0
    }
    
    //material segments
    def matSegments = xml.'**'.findAll { 
        it.name() == 'E1EDP09'
    }
    
    def arrBoxes = [:]
    
    filteredSegments.each { segment ->
        def venum = segment.VENUM?.text()?.trim()
        def pckar = segment.PCKAR?.text()?.trim()
        def anzar = segment.ANZAR?.text()?.trim()
        def vemeh = segment.VEMEH?.text()?.trim()
        def posnr = segment.POSNR?.text()?.trim()
        def serial = segment.EXIDV?.text()?.trim()
        def matSeg = matSegments.find { it.POSNR.text()?.trim() == posnr }
        
        def charg = matSeg.CHARG?.text()?.trim()
        def kdmat = matSeg.KDMAT?.text()?.trim()
        def matnr = matSeg.MATNR?.text()?.trim()
        
        def vgbel = xml.'**'.find { it.name() == 'E1EDP07' && it.POSNR.text() == matSeg.POSNR?.text()?.trim() }.VGBEL.text().trim()
        def pallet = segParent.find { it.VENUM.text() == segTracer.findRootVENUM(venum) }.EXIDV?.text()?.trim()

        def key = "${venum}|${pckar}|${anzar}|${vemeh}|${charg}|${kdmat}|${matnr}|${vgbel}"
        comboCounter[key] += 1
        
        arrBoxes[serial] = pallet +  charg
        
    }

    // Convert map to array of structured objects
    def resultArray = comboCounter.collect { key, count ->
        def (venum, pckar, anzar, vemeh, charg, kdmat, matnr, vgbel) = key.split("\\|")
        [VENUM: venum, 
         PCKAR: pckar, 
         ANZAR: anzar, 
         VEMEH: vemeh,
         CHARG: charg,
         KDMAT: kdmat,
         MATNR: matnr,
         VGBEL: vgbel,
         COUNT: count]
    }
    
    //build CPS segment xml
    def writer = new StringWriter()
    def xmlCPS = new MarkUpBuilder(writer)
    
    
    palletCounts.each { pallet, count ->
        
        xmlCPS.G_SG10{
            
            S_CPS{
                D_7164(2)
                D_7075(3)
            }
            G_SG11{
                S_PAC{
                    D_7224(count)
                    C_C202{
                        D_7065(pallet)
                    }
                }
            }
            G_SG13{
                S_PCI{
                    C_C210{
                        D_7102(segParent.find { it.VENUM.text().trim() == pallet  }.EXIDV.text().trim())
                    }
                    C_C287{
                        D_7511("M")
                    }
                }
            }
        }
        
        
        xmlCPS.G_SG10 {
    
            S_CPS{
                D_7164(1)
                D_7075(1)
            }
            
            resultArray.each { box ->
                G_SG11{
                    S_PAC{
                        D_7224(box.COUNT)
                        C_C202{
                            D_7065(box.PCKAR)
                            
                        }
                    }
                    S_QTY{
                        C_C186{
                            D_6063(52)
                            D_6060(box.ANZAR)
                            D_6411(box.VEMEH)
                        }
                    }
                }
                G_SG17{
                    S_LIN{
                        D_1082(matSegments.find { it.KDMAT.text()?.trim() == box.KDMAT }.POSNR?.text()?.trim())
                        C_C212{
                            D_7140(box.KDMAT)
                            D_7143("IN")
                        }
                    }
                    S_PIA{
                        D_4347(1)
                        C_C212{
                            D_7140(box.MATNR)
                            D_7143("EC")
                        }
                    }
                    S_QTY{
                        C_C186{
                            D_6063(12)
                            def qtyDepatch = box.ANZAR * box.COUNT
                            D_6060(qtyDespatch)
                            D_6411(box.VEMEH)
                        }
                    }
                    S_GIR{
                        D_7297(1)
                        C_C206{
                            D_7402(box.CHARG)
                            D_7405("BX")
                        }
                    }
                    G_SG18{
                        S_RFF{
                            C_C506{
                                D_1153("ON")
                                D_1154(box.VGBEL)
                            }
                        }
                    }
                    def keySerial = box.PALLET + box.CHARG
                    arrBoxes.findAll { it.value == keySerial }.keySet().each { key ->
                        G_SG22{
                            S_PCI{
                                C_C210{
                                    D_7102(key)
                                }
                                C_C827{
                                    D_7511("S")
                                }
                            }
                        }
                    }
                }
                
            }

        }
        
        
    }
    
    def xmlCPSFragment = writer.toString()
    
    def body = message.getBody(String)
    def parser = new XmlParser()
    def origBody = parser.parseText(body)
    def cpsNode = parser.parseText(xmlCPSFragment)

    def children = origBody.children()
    def indexToInsert = children.findIndexOf { it.name() == 'G_SG6' }
    
    if (indexToInsert != -1) {
        root.children().add(indexToInsert, cpsNode )
    }
    
    def finalBody = XmlUtil.serialize(origBody)
    message.setBody(finalBody)
    
    //remove encoded message to reduce memory consumption
    message.remove('Original_Encoded_Message')
    
}