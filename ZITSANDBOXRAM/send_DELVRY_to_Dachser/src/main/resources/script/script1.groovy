import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap;
import com.sun.corba.se.spi.ior.IdentifiableFactory;
import groovy.xml.StreamingMarkupBuilder;
import groovy.xml.XmlUtil;



def Message processData(Message message) {
    def properties = message.getProperties();
    def creationDate = properties.get("CreationDate");
   


    Reader reader = message.getBody(Reader)
    def sourceXml = new XmlSlurper().parse(reader)


    def resultXml = new StreamingMarkupBuilder().bind {

          DeliveryOrder {
            DocumentHeader {

                EDISender {
                    PartnerInformation{
                        PartnerID('47063344')
                    }
                }

                EDIReceiver {
                    PartnerInformation{
                        PartnerGLN('4023083000008')
                    }
                }

                def docID = sourceXml.'**'.find { it.name() == 'DOCNUM' }.text()
                def idWithoutZeros = docID.toInteger().toString()
                DocumentID(idWithoutZeros)

                DocumentDate {

                    Date(creationDate)

                }
                TestFlag('1')
            }

            DeliveryOrderHeader {
                DeliveryOrderHeaderContent {
                    DeliveryOrderNumber(sourceXml.'**'.find { it.name() == 'VBELN' }.text())
                    Warehouse {
                        PartnerInformation {
                            PartnerGLN('4022128000003')
                        }
                    }

                    Principal {
                        PartnerInformation {
                            PartnerID('47063344')
                        }
                    }

                    GoodsReceiver {
                        PartnerInformation {

                            def e1adrm1Elements = sourceXml.'**'.findAll { it.name() == 'E1ADRM1' }
                            e1adrm1Elements.each { e1adrm1Element ->
                                def qualiValue = e1adrm1Element.PARTNER_Q.text()

                                if (qualiValue == 'WE') {
                                    def partnerID = e1adrm1Element?.PARTNER_ID?.text()
                                    PartnerID(partnerID)
                                    def inputString = e1adrm1Element?.NAME1?.text()
                                    def maxLength = 30
                                    def shortenedString = inputString.substring(0, Math.min(inputString.length(), maxLength))
                                    PartnerName(shortenedString)
                                    AddressInformation {
                                        Street(e1adrm1Element?.STREET1?.text())
                                        City(e1adrm1Element?.CITY1?.text())
                                        PostalCode(e1adrm1Element?.POSTL_COD1?.text())
                                        CountryCode(e1adrm1Element?.COUNTRY1?.text())
                                    }
                                    ContactInformation {
                                        ContactName()
                                        ContactPhoneNumber(e1adrm1Element?.TELEPHONE1?.text())
                                        ContactEmail(e1adrm1Element?.E_MAIL?.text())
                                    }
                                }
                            }
                        }
                    }

                    DeliveryDate(DeliveryDateType: 'FROM') {

                        def e1edt13Elements = sourceXml.'**'.findAll { it.name() == 'E1EDT13' }
                        e1edt13Elements.each { e1edt13Element ->
                            def iddatValue = e1edt13Element?.QUALF?.text()

                            if (iddatValue == '001') {
                                def datumValue = e1edt13Element?.NTANF?.text()
                                Date date = Date.parse('yyyymmdd', datumValue)
                                def newDate = date.format('yyyy-mm-dd') + 'T00:00:00Z'
                                Date(newDate)
                            }
                        }
                    }
                    def getLFART = sourceXml.'**'.find { it.name() == 'LFART' }.text()

                    if (getLFART == 'LF') {
                        VoucherType('320')
                    }
                    if (getLFART == 'NL') {
                        VoucherType('432')
                    }

                    def e1adrm1Elements = sourceXml.'**'.findAll { it.name() == 'E1ADRM1' }
                    e1adrm1Elements.each { e1adrm1Element ->
                        def qualiValue = e1adrm1Element.PARTNER_Q.text()

                        if(qualiValue == 'SP'){
                            def getName = e1adrm1Element?.NAME1?.text()
                            if(getName == 'Dachser SE'){
                                ShippingRoute('SPE')
                            }
                            else{
                                ShippingRoute('ABH')
                            }

                        }
                    }


                    DeliveryTerms('031')

                    def delivTerms = sourceXml.'**'.find { it.name() == 'VSBED' }.text()
                    def result = ""
                    switch(delivTerms){
                        case"TX":
                            result = "R"
                            break

                        case"TS":
                            result = "Z"
                            break

                        case"TF":
                            result = "Y"
                            break
                    }
                    Dispatch(result)


                    AdditionalReference(Code: 'CR2') {
                        ReferenceNo(sourceXml.'**'.find { it.name() == 'BSTNR' }.text())
                    }

                    AdditionalReference(Code: 'CR3') {
                        ReferenceNo(sourceXml.'**'.find { it.name() == 'VGBEL' }.text())
                    }

                }

                sourceXml.IDOC.E1EDL20.E1EDL24.each { e1edl24 ->


                    DeliveryOrderPosition {
                        def posNr = e1edl24.POSNR.text()
                        def posNrOhneNull = posNr.toInteger().toString()
                        PosNo(posNrOhneNull)

                        def marNr = e1edl24.MATNR.text()
                        def marNrOhneNull = marNr.toInteger().toString()
                        Article(marNrOhneNull)
                        Quantity(e1edl24.LFIMG.text())
                        QuantityUnit(e1edl24.VRKME.text())
                        PositionText(TextType: "PT") {
                            Text(e1edl24.E1TXTH9.E1TXTP9.TDLINE.text())
                        }

                    }
                }

            }
        }
    }

    def writer = new StringWriter()
    XmlUtil.serialize(resultXml, writer)


    message.setBody(writer.toString())

    return message
}
