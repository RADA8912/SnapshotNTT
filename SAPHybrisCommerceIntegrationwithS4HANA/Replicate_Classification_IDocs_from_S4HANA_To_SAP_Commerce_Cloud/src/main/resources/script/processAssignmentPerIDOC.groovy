/*
 The integration developer needs to create the method processData 
 This method takes Message object of package com.sap.gateway.ip.core.customdev.util 
which includes helper methods useful for the content developer:
The methods available are:
    public java.lang.Object getBody()
  public void setBody(java.lang.Object exchangeBody)
    public java.util.Map<java.lang.String,java.lang.Object> getHeaders()
    public void setHeaders(java.util.Map<java.lang.String,java.lang.Object> exchangeHeaders)
    public void setHeader(java.lang.String name, java.lang.Object value)
    public java.util.Map<java.lang.String,java.lang.Object> getProperties()
    public void setProperties(java.util.Map<java.lang.String,java.lang.Object> exchangeProperties) 
       public void setProperty(java.lang.String name, java.lang.Object value)
 */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import groovy.time.*
import com.sap.it.api.mapping.*;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;

def Message processData(Message message) {
    def chrmas_full_xml = message.getBody(java.lang.String) as String;
    if(chrmas_full_xml == null|| chrmas_full_xml.contains("NOTFOUND") || chrmas_full_xml ==''){
        message.setBody("<NOTFOUND/>");
        return message
    }
    
    def cls_catalog_type = message.getProperty("CLS_KLART") as String;
    def cls_class = message.getProperty("CLS_CLASS") as String;
    def chr_root = new XmlSlurper(false,false).parseText(chrmas_full_xml);
  
    def classAssignmentUnitPersistenceHook = message.getProperty("classAssignmentUnitPersistenceHook");
    classAssignmentUnitPersistenceHook = classAssignmentUnitPersistenceHook != null? classAssignmentUnitPersistenceHook : "sapCpiClassAssignmentUnitPersistenceHook";
    
    def attributeAssignmentPayload = processClassAttributeAssignment(chr_root, cls_catalog_type, cls_class, classAssignmentUnitPersistenceHook);
    
    message.setBody(XmlUtil.serialize('<batchParts xmlns="">'+ attributeAssignmentPayload +'</batchParts>') );
    
    return message;
}
def processClassAttributeAssignment(groovy.util.slurpersupport.GPathResult chr_root, String cls_catalog_type, String cls_class, String hook){
    
    def builder = new StreamingMarkupBuilder()
    def batchAttributeValueList = chr_root.IDOC.E1CABNM.E1CAWNM.collect{it->(it.ATWRT!=''?it.ATWRT.text():null)};
	batchAttributeValueList.removeAll([null])
	
	def mappedClassificationCatalogId = loadMappedSuperCategoryCatalogId("ERP_CLASSIFICATION");
    def mappedClassificationCatalogVersion = loadMappedSuperCategoryCatalogId("ERP_IMPORT");
    
    def attr_code = chr_root.IDOC.E1CABNM.ATNAM.text();
     
    def attributeTyoe = getChrmasAttributeType(attr_code,batchAttributeValueList.toString(), chr_root.IDOC.E1CABNM.ATFOR.text())
    def formatType = formatTypeDefination(chr_root.IDOC.E1CABNM.ATFOR.text(), chr_root.IDOC.E1CABNM.ATDEX.text(),chr_root.IDOC.E1CABNM.ANZST.text(),chr_root.IDOC.E1CABNM.ANZDZ.text(),chr_root.IDOC.E1CABNM.ATSCH.text());
    formatType = formatType.contains('-')?(formatType.replace('-','')+';'+formatType):formatType;
    def isRangeType = chr_root.IDOC.E1CABNM.ATINT.text() =='X'?'true':'false';
    def isMultipleValue =  chr_root.IDOC.E1CABNM.ATEIN.text() !='X' || chr_root.IDOC.E1CABNM.ATINT.text() =='X'?'true':'false';
    def unitCode = chr_root.IDOC.E1CABNM.MSEHI.text();
    def assignmentBatchParts = builder.bind {
      batchChangeSet {
        batchChangeSetPart{
          method('POST')
          if(unitCode!=''){
            headers{
				header{
					headerName('Pre-Persist-Hook')
					headerValue(hook)
				}
			}
          }
          ClassAttributeAssignments{
            ClassAttributeAssignment{
              integrationKey('')
              formatDefinition(formatType)
              multiValued(isMultipleValue)
              range(isRangeType)
              if(unitCode!=''){
                  sapCpiAssignmentUnitCode(unitCode)
              }
              attributeType{
                ClassificationAttributeTypeEnum{
                  integrationKey('')
                  code(attributeTyoe)
                }
              }
              classificationAttribute{
                ClassificationAttribute{
                  integrationKey('')
                  code(attr_code)
                  systemVersion{
                    ClassificationSystemVersion{
                      integrationKey('')
                      version(mappedClassificationCatalogVersion)
                      catalog{
                        ClassificationSystem{
                          integrationKey('')
                          id(mappedClassificationCatalogId+'_'+cls_catalog_type)
                        }
                      }
                    }
                  }
                }
              }
              classificationClass{
                ClassificationClass{
                  integrationKey('')
                  code(cls_class)
                  catalogVersion{
                    ClassificationSystemVersion{
                      integrationKey('')
                      version(mappedClassificationCatalogVersion)
                      catalog{
                        ClassificationSystem{
                          integrationKey('')
                          id(mappedClassificationCatalogId+'_'+cls_catalog_type)
                        }
                      }
                    }
                  }
                }
              }
              systemVersion{
                ClassificationSystemVersion{
                  integrationKey('')
                  version(mappedClassificationCatalogVersion)
                  catalog{
                    ClassificationSystem{
                      integrationKey('')
                      id(mappedClassificationCatalogId+'_'+cls_catalog_type)
                    }
                  }
                }
              }
            }
          }
        }
      }
  }
  return assignmentBatchParts;
}
def String  getChrmasAttributeType(String attribute,String attributeValue,String attributeType){

    if (attribute == null || attribute == '')
        return "string";
    else if (attributeValue!='[]' && attributeValue !='')
        return "enum";
    return attributeType == 'DATE'?'date':(attributeType =='NUM'?'number':'string');
}

def String formatTypeDefination(String attributeType, String displayFormat,String charNumber, String decimalPlace,String template){
    if("NUM"!=attributeType || displayFormat!= '0')
    return "";
     
  int char_number = charNumber.toInteger();
     
  int decimal = decimalPlace.toInteger(); 
  if(template.count("_") != char_number)
    return "";
  template = template.replace("_","0");
    
  for(int i=0;i<(char_number-(decimal+1));i++ )
    template = template.replaceFirst("0", "#");
  return template;
}

def private  String loadMappedSuperCategoryCatalogId(String catalogVersion){
    return dynamicValueMap("CATALOG","CLASSIFICATIONSYSTEMVERSION","EXTERNAL","PARAMETERIZATION",catalogVersion);
}

def private String dynamicValueMap(String  sAgency, String sIdentifier, String tAgency, String tIdentifier, String key){
	def service = ITApiFactory.getApi(ValueMappingApi.class, null);
	if( service != null) {
		String value= service.getMappedValue(sAgency, sIdentifier,key, tAgency, tIdentifier);
		if ( value == null )	{
    		return key;
		}
		else
    		return value;
	}
	return key;
}
 