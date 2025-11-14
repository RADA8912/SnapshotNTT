package src.main.resources.script
class ReaderWrapper {

    BufferedReader br;
    String line="";
    ReaderWrapper(Reader reader)
    {
        try{
            br=new BufferedReader(reader);
            line=br.readLine();
        }catch(Exception ex)
        {
            System.out.println("Error reading the impex "+ex.getMessage());
        }
    }

    String readLine()
    {
        try{
            line=br.readLine();
            return line;
        }catch(Exception ex)
        {
            System.out.println("Error reading the impex "+ex.getMessage())
        }
    }

    String peek()
    {

        return line;
    }
}
