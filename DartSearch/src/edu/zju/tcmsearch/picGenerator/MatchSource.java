
package edu.zju.tcmsearch.picGenerator;
import java.io.*;
import java.net.*;

/**
 * @author zhm
 *
 */
public  class MatchSource {

	/**
	 * @param args
	 */
	
	public String getDocumentAtURL(String urlString) {
		StringBuffer document = new StringBuffer();
		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null)			
			document.append(line+"\r\n");
			reader.close();
		}
		catch (MalformedURLException e) {
			System.out.println("Unable to connect to URL: " + urlString);
			} 
		catch (IOException e) {
			System.out.println("IOException when connecting to URL: " + urlString);
			}
			
			return document.toString();
		} 
	
	public String getSourseFile(String fileName)
    {
    	
    	byte[] buff = new byte[20480*5];     	
    	FileInputStream infile = null; 
    	fileName =fileName;
    	try 
    	{ 
    		infile = new FileInputStream(fileName); 
    	} 
    	catch (FileNotFoundException e) 
    	{ 
    		System.err.println(" 找不到文件："+fileName); 
    		System.exit(1); 
    	}
    	
    	try 
    	{ 
    		int n = infile.read(buff);
    		return new String(buff) ;  	
    	} 
    	catch (Exception e) 
    	{
    
    	}
		return "";    	
		
    }

}