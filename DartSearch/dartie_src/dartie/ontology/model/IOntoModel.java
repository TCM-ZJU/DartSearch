package dartie.ontology.model;

import com.hp.hpl.jena.rdf.model.Resource;

public interface IOntoModel {

	public Resource getResource();
	
	public void setResource(Resource resource);

}
