package edu.zju.tcmsearch.picGenerator;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExMatch {
	
	public RegExMatch() {
		
	}

	public Matcher GetMatcherFromFile(String PatternStr,String SourceFile )
	{   	
			
		//Pattern p = Pattern.compile("<(\\w+)\\s*(\\w+(=('|\").*?\\4)?\\s*)*>.*?</\\1>"); 
		/*Pattern p = Pattern.compile("<tr.*>" +
				".*(\r\n)*<td.*>.*(\r\n)*</td.*>" +
				".*(\r\n)*<td.*>.*(\r\n)*</td.*>" +
				".*(\r\n)*<td.*>.*(\r\n)*</td.*>" +
				".*(\r\n)*<td.*>.*(\r\n)*</td.*>" +
				".*(\r\n)*<td.*>.*(\r\n)*</td.*>" +
				".*(\r\n)*</tr.*>");
				*/
		
		Pattern p = Pattern.compile(PatternStr);
		MatchSource ms=new MatchSource();
		String SourceData =ms.getSourseFile(SourceFile);		
		Matcher m = p.matcher(SourceData);	
		return m;
	}
	
	public Matcher GetMatcherFromURL(String PatternStr,String SourceURL )
	{ 		
		Pattern p = Pattern.compile(PatternStr);
		MatchSource ms=new MatchSource();
		String SourceData =ms.getDocumentAtURL(SourceURL);		
		Matcher m = p.matcher(SourceData);	
		return m;
	}
	
	public Matcher GetMatcherFromStr(String PatternStr,String SourceStr )
	{
		Pattern p = Pattern.compile(PatternStr);				
		Matcher m = p.matcher(SourceStr);	
		return m;
	}
	

}