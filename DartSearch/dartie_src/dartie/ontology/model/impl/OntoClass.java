package dartie.ontology.model.impl;

import java.util.HashMap;
import java.util.Map;


import com.hp.hpl.jena.rdf.model.Resource;

import dartie.ontology.model.IOntoClass;

public class OntoClass implements IOntoClass{

	private String label;
	private String uri;
	private Resource resource;
	private Map subClasses = new HashMap();
	
	public void addSubClass(String key, IOntoClass ontoClass) {
		if(!subClasses.containsKey(key)) {
			subClasses.put(key, ontoClass);
		}
	}
	
	public void removeSubClass(String key,IOntoClass ontoClass) {
		if(subClasses.containsKey(key)) {
			subClasses.remove(key);
		}
	}

	/**
	 * @return Returns the label.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return Returns the resource.
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * @param resource The resource to set.
	 */
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	/**
	 * @return Returns the subClasses.
	 */
	public Map getSubClasses() {
		return subClasses;
	}

	/**
	 * @param subClasses The subClasses to set.
	 */
	public void setSubClasses(Map subClasses) {
		this.subClasses = subClasses;
	}

	/**
	 * @return Returns the uri.
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri The uri to set.
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

}
