package dartie.ontology.model;

import java.util.Map;

public interface IOntoClass extends IOntoModel{
		
	public void addSubClass(String key, IOntoClass ontoClass);
		
	public void removeSubClass(String key, IOntoClass ontoClass);
	
	public String getLabel();

	public void setLabel(String label);
	
	public Map getSubClasses();
	
	public void setSubClasses(Map subClasses);
	
	public void setUri(String uri);
	
	public String getUri();
}
