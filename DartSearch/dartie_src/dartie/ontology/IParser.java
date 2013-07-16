package dartie.ontology;

import java.io.InputStream;

public interface IParser {
	
	// Parser Initialize 
	public void init();	
	// Print Parser Hierarchy Structure
	public void showHierarchy();	
	// Get Class Stream
	public InputStream getClassStream();
	// Get Instance Stream
	public InputStream getInstanceStream();
	// Get Property Stream
	public InputStream getPropertyStream();
	
	public void saveClassAsXMLFile(String filePath);
	
	public void saveInstanceAsXMLFile(String filePath);
	
	public void savePropertyAsXMLFile(String filePath);
	
	public String getInputFileName();
	
	public void setInputFileName(String inputFileName);
	
	public String getUri();
	
	public void setUri(String uri);
}
