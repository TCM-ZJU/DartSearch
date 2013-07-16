package dartie.ontology.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class ModelImporter {
	
    private static final Log log = LogFactory.getLog(ModelImporter.class);
    private static final String OLD_RDFS_NS = "http://www.w3.org/TR/1999/PR-rdf-schema-19990303#";

    private static final String NEW_RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";

    private static final String DEFAULT_JVM_ENCODING = "ISO-8859-1";
    
    private static int readSize = 1024;
    
    private static InputStream getFromFile(String fileName)
    {
    	InputStream in = null;
        try {
            in = new FileInputStream(fileName);
        } catch (FileNotFoundException fexp) {
            log.error("File: '" + fileName + "' not found!",fexp);
        } catch (Exception e) {
            log.error("Exception happens during reading the file " + fileName + "!",e);
        }
        InputStream input = null;
        try {
        	String encoding = getEncoding(in);
        	in.close();
        	try {
                in = new FileInputStream(fileName);
            } catch (FileNotFoundException fexp) {
                log.error("File: '" + fileName + "' not found!",fexp);
            } catch (Exception e) {
                log.error("Exception happens during reading the file " + fileName + "!",e);
            }
        	
            String str = new String(getInputStreamAsBytes(new PushbackInputStream(in)),encoding);
            str = replace(str, OLD_RDFS_NS, NEW_RDFS_NS);
            str = replace(str, encoding, "UTF-8");
            input = new ByteArrayInputStream(str.getBytes("UTF-8"));
        } catch (IOException e1) {
            log.error("Exceptions happens during converting the file to utf-8 encoding!",e1);
        }
        return input;
    }
    
    public static Model getFromRdfFile(String fileName) {
        
    	InputStream in = getFromFile(fileName);

    	//create a jena model
        Model model = ModelFactory.createDefaultModel();
        try {
        	//model.begin();
            model.read(in, "");
            //model.close();
        } catch (Exception e) {
            log.error("Fail to import file: " + fileName + "!",e);
        }
        return model;
    }
    
    public static OntModel getFromOwlFile(String fileName) {
    	
    	InputStream in = getFromFile(fileName);

        //create a jena model
        OntModel model =ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null );
        try {
        	//model.begin();
            model.read(in, "");
            //model.close();
        } catch (Exception e) {
            log.error("Fail to import file: " + fileName + "!",e);
        }
        return model;
    }

    private static String replace(String strSource, String strFrom, String strTo) {
        StringBuffer strDest = new StringBuffer();
        int intFromLen = strFrom.length();
        int intPos;

        while ((intPos = strSource.indexOf(strFrom)) != -1) {
            strDest.append(strSource.substring(0, intPos))
            	   .append(strTo);
            strSource = strSource.substring(intPos + intFromLen);
            break;
        }
        strDest.append(strSource);
        return strDest.toString();
    }
    
    private static byte[] getInputStreamAsBytes(PushbackInputStream urlInput) throws IOException {
    	byte[] bytes = new byte[urlInput.available()];
        byte[] buffer = new byte[readSize];
        int bytesRead;
        int cursor = 0;
        while ((bytesRead = urlInput.read(buffer)) != -1) {
        	for(int i=0;i < bytesRead;i++)
        	bytes[cursor++] = buffer[i];
        }
        return bytes;
    }
    
/*    private static String getInputStreamAsString(PushbackInputStream urlInput,String encoding) throws IOException {
        byte[] buffer = new byte[readSize];
        int bytesRead;
        StringBuffer urlContent = new StringBuffer(readSize);
        while ((bytesRead = urlInput.read(buffer)) != -1) {
        	String thestr = new String(buffer, 0, bytesRead,encoding);
        	
        	int count = 0;
        	for(int index = bytesRead - 1;index >=0;index--,count++){
        		if(buffer[index] >= 0)
        			break;
        	}
        	if(count%2 != 0){
        		thestr = thestr.substring(0,thestr.length() - 1);
        		urlInput.unread(buffer[bytesRead - 1]);
        	}
            urlContent.append(thestr);
        }
        String templateText = urlContent.toString();

        return templateText;
    }
*/
	private static String getEncoding(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		return getEncoding(br);
	}
	
/*	private static String getEncoding(String str) throws IOException {
		BufferedReader br = new BufferedReader(new StringReader(str));
		return getEncoding(br);
	}*/
	
	private static String getEncoding(BufferedReader br) throws IOException {
		String headLine	= br.readLine();
		if(headLine != null && headLine.indexOf("encoding=")>=0){
			int startIndex = headLine.indexOf("encoding=") + "encoding='".length();
			int endIndex = headLine.lastIndexOf('?') - 1;
			return headLine.substring(startIndex,endIndex);
		}else{
			return DEFAULT_JVM_ENCODING;
		}
	}
}
