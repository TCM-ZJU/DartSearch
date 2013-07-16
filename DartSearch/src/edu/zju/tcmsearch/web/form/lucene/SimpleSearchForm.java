/*
 * Created on 2005-12-19
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.web.form.lucene;
import edu.zju.tcmsearch.util.web.ParameterValueDecoder;

public class SimpleSearchForm {
    private String queryExperssion;
    private String ontologyName;


    /**
     * @return Returns the ontologyName.
     */
    public String getOntologyName() {
        return ontologyName;
    }

    /**
     * @param ontologyName The ontologyName to set.
     */
    public void setOntologyName(String ontologyName) {
        this.ontologyName = ontologyName;
    }

    /**
     * @return Returns the queryExperssion.
     */
    public String getQueryExperssion() {
        return queryExperssion;
    }

    /**
     * @param queryExperssion The queryExperssion to set.
     */
    public void setQueryExperssion(String queryExperssion) {
        this.queryExperssion = queryExperssion;
    }
    
    
}
