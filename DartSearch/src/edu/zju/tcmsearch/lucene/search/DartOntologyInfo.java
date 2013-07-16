/*
 * Created on 2005-12-22
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.search;

import java.util.Collections;
import java.util.List;

import edu.zju.tcmsearch.common.OntologyMaps;
import edu.zju.tcmsearch.common.domain.DartOntology;

public class DartOntologyInfo {
    private String ontoIdentity;
    private String ontoName;
    private String ontoUri;

    /**
     * @return Returns the dartOntology.
     */
    public DartOntology getDartOntology() {
        return OntologyMaps.getInstance(getOntoIdentity());
    }

    /**
     * @return Returns the ontoIdentity.
     */
    public String getOntoIdentity() {
        return ontoIdentity;
    }
    /**
     * @param ontoIdentity The ontoIdentity to set.
     */
    public void setOntoIdentity(String ontoIdentity) {
        this.ontoIdentity = ontoIdentity;
    }
    /**
     * @return Returns the ontoName.
     */
    public String getOntoName() {
        return ontoName;
    }
    /**
     * @param ontoName The ontoName to set.
     */
    public void setOntoName(String ontoName) {
        this.ontoName = ontoName;
    }
    /**
     * @return Returns the ontoUri.
     */
    public String getOntoUri() {
        return ontoUri;
    }
    /**
     * @param ontoUri The ontoUri to set.
     */
    public void setOntoUri(String ontoUri) {
        this.ontoUri = ontoUri;
    }
   
    public List<DartOntology> getChildOntologies(){
        DartOntology dartOntology=getDartOntology();
        if (null==dartOntology){
            return Collections.EMPTY_LIST;
        }
        return dartOntology.getChildOntologies();
    }
}
