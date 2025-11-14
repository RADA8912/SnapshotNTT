package src.main.resources.script
public class Data {

    Map<String, Map<String,List<String>>> mapOfImpex = new HashMap<>();
    Map<String,List<String>> integrationObjectLinesMap = new HashMap<>();
    Map<String,List<String>> integrationObjectItemLinesMap = new HashMap<>();
    Map<String,List<String>> integrationObjectItemAttributeLinesMap = new HashMap<>();
    String errorMessages="";
     Data(Reader reader) {
        mapOfImpex.put("IO", integrationObjectLinesMap);
        mapOfImpex.put("IOI", integrationObjectItemLinesMap);
        mapOfImpex.put("IOIA", integrationObjectItemAttributeLinesMap);
        parseFile(reader);

    }

    private void parseFile(Reader reader)
    {
        try{
            ReaderWrapper wrapper = new ReaderWrapper(reader);
            String line="";
            while((line=wrapper.peek())!=null &&  (line=removeComments(line))!='-1')
            {
                if(line.matches("(?i)\\s*(INSERT_UPDATE|INSERT|UPDATE)\\s+IntegrationObject\\s*;.+"))
                {
                    while((line=wrapper.readLine())!=null &&  (line=removeComments(line))!='-1' && (!line.matches("(?i)\\s*(INSERT_UPDATE|INSERT|UPDATE)\\s+.+") ))
                    {
                        if(line.length() > 0)
                        {
                            try{
                                String code=line.split(";")[1].trim();
                                if(integrationObjectLinesMap.get(code)==null)
                                {
                                    List<String> integrationObjectLines=new ArrayList<String>();
                                    integrationObjectLines.add(line);
                                    integrationObjectLinesMap.put(code,integrationObjectLines);
                                }
                            }
                            catch(Exception ex)
                            {
                                errorMessages+="Exception while reading the IO line \n"+ line +"\n";
                            }
                            
                        }

                    }
                }
                else if(line!=null && line.matches("(?i)\\s*(INSERT_UPDATE|INSERT|UPDATE)\\s+IntegrationObjectItem\\s*;.+"))
                {
                    while((line=wrapper.readLine())!=null &&  (line=removeComments(line))!='-1' && (!line.matches("(?i)\\s*(INSERT_UPDATE|INSERT|UPDATE)\\s+.+") ))
                    {
                        if(line.length() > 0)
                        {
                            try{
                                String code=line.split(";")[1].trim();
                                if(integrationObjectItemLinesMap.get(code)==null)
                                {
                                    List<String> integrationObjectItemLines=new ArrayList<String>();
                                    integrationObjectItemLinesMap.put(code,integrationObjectItemLines);
                                }
                                integrationObjectItemLinesMap.get(code).add(line);
                            }
                            catch(Exception ex)
                            {
                                errorMessages+="Exception while reading the IOI line \n"+ line +"\n";
                            }
                        }
                    }
                }

                else if(line!=null && line.matches("(?i)\\s*(INSERT_UPDATE|INSERT|UPDATE)\\s+IntegrationObjectItemAttribute\\s*;.+"))
                {
                    while((line=wrapper.readLine())!=null &&  (line=removeComments(line))!='-1' && (!line.matches("(?i)\\s*(INSERT_UPDATE|INSERT|UPDATE)\\s+.+") ))
                    {
                        if(line.length() > 0)
                        {
                            try{
                                String code=line.split(";")[1].trim().split(":")[0].trim();
                                if(integrationObjectItemAttributeLinesMap.get(code)==null)
                                {
                                    List<String> integrationObjectItemAttributeLines=new ArrayList<String>();
                                    integrationObjectItemAttributeLinesMap.put(code,integrationObjectItemAttributeLines);
                                }
                                integrationObjectItemAttributeLinesMap.get(code).add(line);
                            }
                            catch(Exception ex)
                            {
                                errorMessages+="Exception while reading the IOIA line \n"+ line +"\n";
                            }
                        }

                    }
                }
                else
                {
                    wrapper.readLine();
                }

            }
        }catch (IOException ex) {
            System.out.println("Error reading the file");
            System.out.println(ex.getMessage());
        }
    }


     Map<String, List<String>> getMapOfImpex()
    {
        return  mapOfImpex;
    }


    private String removeComments(String line)
    {
        int offset = line.indexOf("#");
        if(offset!=-1)
        {
            line=line.substring(0,offset);
        }

        return line.trim();

    }


    String getErrorMessages()
    {
        return errorMessages;
    }


}




