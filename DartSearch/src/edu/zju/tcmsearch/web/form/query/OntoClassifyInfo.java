/*
 * Created on 2005-11-25
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.web.form.query;

import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.util.dartcore.DataConvertor;

public class OntoClassifyInfo {
    DartOntology dartOntology;
    String ontoSelect="false";
    
    int position;
    
    public OntoClassifyInfo(int position){
        this.position=position;
    }
  
    /**
     * @return Returns the dartOntology.
     */
    public DartOntology getDartOntology() {
        return dartOntology;
    }
    
    
    /**
     * @return Returns the position.
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position The position to set.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @param dartOntology The dartOntology to set.
     */
    public void setDartOntology(DartOntology dartOntology) {
        this.dartOntology = dartOntology;
    }
    /**
     * @return Returns the ontoSelect.
     */
    public String getOntoSelect() {
        return ontoSelect;
    }
    /**
     * @param ontoSelect The ontoSelect to set.
     */
    public void setOntoSelect(String ontoSelect) {
        this.ontoSelect = ontoSelect;
    }
    
    public boolean isSelect(){
        return DataConvertor.toBoolean(this.ontoSelect);
    }
 


}
