import com.sap.it.api.mapping.*;
import java.util.regex.Pattern;

def String formatCHRMASDescription(String language_iso,MappingContext context)
{
    if(language_iso==null || language_iso.trim().size()<=0)
        return '';
    def fullDescription = context.getProperty("CHRMAS_FULL_DESCRIPTION");
    
   if(fullDescription != null && fullDescription.size()>0){
       def description  = fullDescription.get(language_iso.toUpperCase());
       
       if(description !=null){
            def List fullText  = description.tokenize("==");
           
           removeConsecutiveDuplicates(fullText);
           def String result = appendItfLine(fullText);
            return result.trim().length()>0?result:'';
       }
      return ''
   }
   return ''; 
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
