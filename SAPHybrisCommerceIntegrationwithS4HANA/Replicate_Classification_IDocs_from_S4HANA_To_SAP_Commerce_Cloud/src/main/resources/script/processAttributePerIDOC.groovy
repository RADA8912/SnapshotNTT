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
import java.util.regex.Pattern;
import groovy.transform.Field;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;

@Field def boolean isRawTextMode = false;

def Message processData(Message message) {
    def chrmas_full_xml = message.getBody(java.lang.String) as String;

    if( chrmas_full_xml == null || chrmas_full_xml.contains("NOTFOUND") || chrmas_full_xml == '' ){
        message.setBody("<NOTFOUND/>");
        return message;
    }
    def cls_catalog_type = message.getProperty("CLS_KLART") as String;
    def chr_root = new XmlSlurper(false,false).parseText(chrmas_full_xml);
  
    def attributePayload=   processClassificationAttribute( chr_root ,cls_catalog_type);
    message.setBody(XmlUtil.serialize('<batchParts xmlns="">'+attributePayload+ '</batchParts>'));
    return message;
}

def processClassificationAttribute(groovy.util.slurpersupport.GPathResult chr_root, String cls_catalog_type){
    //def cls_catalog_id = cls_root.IDOC.E1KLAHM.KLART.text();
    def attr_code = chr_root.IDOC.E1CABNM.ATNAM.text();
    
  def batchAttributeValueList = chr_root.IDOC.E1CABNM.E1CAWNM.collect{it->(it.ATWRT!=''?it.ATWRT.text():null)};
  batchAttributeValueList.removeAll([null])
    
  def batchAttributeLangList = [:]
  chr_root.IDOC.E1CABNM.E1CABTM.each{it-> batchAttributeLangList.put(it.SPRAS_ISO.text(),it.ATBEZ.text() )};
  def builder = new StreamingMarkupBuilder();
  
  def mappedClassificationCatalogId = loadMappedSuperCategoryCatalogId("ERP_CLASSIFICATION");
  def mappedClassificationCatalogVersion = loadMappedSuperCategoryCatalogId("ERP_IMPORT");
    
  def descrption_lang=[:]
  if(chr_root.IDOC.E1CABNM.E1TEXTL!=''){
      descrption_lang = processCHRMASDescription(chr_root);
  }
    
  def attributeBatchParts = builder.bind {build->
        batchChangeSet {
          batchChangeSetPart{
            method('POST')
        
            ClassificationAttributes{
              ClassificationAttribute{
                integrationKey('')
                code(attr_code)
                
                localizedAttributes{
                    batchAttributeLangList.each {key,value->
                        Localized___ClassificationAttribute{
                            language(key.toLowerCase())
                            if(descrption_lang.size()>0){
                                formatCHRMASDescription build,key,descrption_lang
                            }
                            name(value)
                        }
                    }
                }
                
                if(batchAttributeValueList.size()>0){
                defaultAttributeValues{
                  batchAttributeValueList.each {v->
                    ClassificationAttributeValue{
                      integrationKey('')
                      code(attr_code+"_"+v)
                      systemVersion{
                        ClassificationSystemVersion{
                          integrationKey('')
                          version(mappedClassificationCatalogVersion)
                          catalog{
                            Catalog{
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
                systemVersion{
                  ClassificationSystemVersion{
                    integrationKey('')
                    version(mappedClassificationCatalogVersion)
                    catalog{
                      Catalog{
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
  return attributeBatchParts;
}

def processCHRMASDescription (groovy.util.slurpersupport.GPathResult root_chrmas) {
     
    def description = [:];
  
  root_chrmas.IDOC.E1CABNM.E1TEXTL.each{it->
    description.containsKey(it.LANGUAGE_ISO.text()) ? !description.get(it.LANGUAGE_ISO.text()).contains(it.TDLINE.text()+"|"+it.TDFORMAT.text())? description.put( it.LANGUAGE_ISO.text(),description.get(it.LANGUAGE_ISO.text())+"=="+it.TDLINE.text()+"|"+it.TDFORMAT.text()):"": description.put( it.LANGUAGE_ISO.text(), it.TDLINE.text()+"|"+it.TDFORMAT.text());
    };
  return description;
     
}

def formatCHRMASDescription(build, String language_iso, Map description_Map )
{
  if(language_iso!=null || language_iso.trim().size()>0){
       
      def fullDescription = description_Map ;//context.getProperty("CHRMAS_FULL_DESCRIPTION");
   
      if(fullDescription != null && fullDescription.size()>0){
        def description  = fullDescription.get(language_iso.toUpperCase());
      
        if(description !=null){
          def List fullText  = description.tokenize("==");
        
          removeConsecutiveDuplicates(fullText);
          def String result = appendItfLine(fullText);
          build.sapERPCharacteristicLongText(result.trim().length()>0?result:'');
        }
      }
    }
}
   
def String getDescriptionLanguageType(String language_iso)
{
  return language_iso.toLowerCase();
}
   
def String appendItfLine(List textLines) {
   def Set<String> TDFORMAT_COMMENT_COMMAND = new HashSet<>(Arrays.asList("/*", "/:"));
   def Set<String> TDFORMAT_LONG_RAW_BROKEN_LINE = new HashSet<>(Arrays.asList("=", "(", ""));
   def Set<String> TDFORMAT_RAW_BROKEN_LINE = new HashSet<>(Arrays.asList("(", ""));
   def Set<String> TDFORMAT_RAW_RAW_LINE = new HashSet<>(Arrays.asList("(", "/("));
   def StringBuilder rawText = new StringBuilder();
     
   for(String textLine: textLines) {
     def (line, format) = textLine.tokenize("|");
 
     // skip comment "/*" or command "/:"
     if (TDFORMAT_COMMENT_COMMAND.contains(format))
     {
       continue;
     }
   
     // if oldText == null means we call this function the first time and want to avoid an empty line (or empty space) at the beginning of the text.
   
     if (rawText.length() != 0)
     {
       // append only a newline-character if the format is not long line or raw line or broken line
       if (!TDFORMAT_LONG_RAW_BROKEN_LINE.contains(format))
       {
         rawText.append('\n');
       }
   
       // in case of raw line, a space must be appended (as shown by tests, but in opposite of SAP's format description, which says to append without space)
       // If it is a broken line, we also have to append a space
       if (TDFORMAT_RAW_BROKEN_LINE.contains(format))
       {
         rawText.append(' ');
       }
     }
   
    // convert line content only if it is not a raw line, that means a line in SAP's ITF format that contains control and format characters
    if (TDFORMAT_RAW_RAW_LINE.contains(format))
    {
       rawText.append(line);
    }
    else
    {
         // if we don't have a long or broken line, raw text markers inside ITF-text are reset.
      if (!"=".equals(format) && !"/=".equals(format) && !"".equals(format))
      {
         isRawTextMode = false;
      }
   
      rawText.append(convertItfLineToRaw(line));
      }
    }
    return rawText.toString();
}
   
 def  removeConsecutiveDuplicates(List list) {
     Iterator iterator = list.iterator();
     Object old = iterator.next();
     while (iterator.hasNext()) {
       Object next = iterator.next();
       if (next.equals(old)) {
         iterator.remove();
       }
       old = next;
     }
     return null;
 }
   
   def StringBuilder convertItfLineToRaw(String line)
   {
       def Pattern PATTERN_FONT = Pattern.compile("<(\\)|/|[a-zA-Z][a-zA-Z\\d]?)>");
       def Pattern PATTERN_REFERENCE = Pattern.compile("&[^\\s&+]+&");
       def Pattern PATTERN_SYMBOL = Pattern.compile("<\\d+>");
    
       def StringBuilder newText = new StringBuilder();
       int start = 0;
       while (start < line.length())
       {
         // find next token
         String nextToken = isRawTextMode ? "<)>" : "<(>";
         int end = line.indexOf(nextToken, start);
         if (end == -1)
         {
           end = line.length();
         }
   
         // get text up to the next token or end
         String part = line.substring(start, end);
         if (!isRawTextMode)
         {
           // convert from SAP's ITF format to string
           // special formatting sequences needs to be deleted: begin="<" + n + ">" and end="</>", where n is "H" (bold text) or "U" (underlined text).
           // String n can also be any user-defined format, consisting of up to 2 letters. The second letter can also be a digit.
           // also the end-of-raw-text-sequence "<)>" needs to be deleted.
           part = PATTERN_FONT.matcher(part).replaceAll("");
   
           // special characters are coded "<" + n + ">", where n is a number. Decoding them can be done as an advanced task, but here we just delete them.
           part = PATTERN_SYMBOL.matcher(part).replaceAll("");
   
           // variable-replacers needs to be deleted. Format: "&replacer&", where replacer should not be a space or plus and consist of minimum 1 character
           part = PATTERN_REFERENCE.matcher(part).replaceAll("");
         }
         newText.append(part);
   
         // toggle mode if we found a token
         if (end != line.length())
         {
           isRawTextMode = !isRawTextMode;
         }
   
         // continue parsing after the last token
         start = end + nextToken.length();
       }
       return newText;
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