/*
 * Created on 2005-11-25
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.web.form.query;

import edu.zju.tcmsearch.common.domain.DartOntology;

public class OntoClassifyForm {
    private int arrayBase=0;
    private int length;
    private OntoClassifyInfo[] ontoClassifyInfos;
    /**
     * @return Returns the arrayBase.
     */
    public int getArrayBase() {
        return arrayBase;
    }
    /**
     * @param arrayBase The arrayBase to set.
     */
    public void setArrayBase(int arrayBase) {
        this.arrayBase = arrayBase;
    }
    /**
     * @return Returns the length.
     */
    public int getLength() {
        return length;
    }
    /**
     * @param length The length to set.
     */
    public void setLength(int length) {
        this.length = length;
        ontoClassifyInfos=new OntoClassifyInfo[length];
    }
    /**
     * @return Returns the ontoClassifyInfo.
     */
    public OntoClassifyInfo[] getOntoClassifyInfos() {
        return ontoClassifyInfos;
    }
    /**
     * @param ontoClassifyInfo The ontoClassifyInfo to set.
     */
    public void setOntoClassifyInfos(OntoClassifyInfo[] ontoClassifyInfo) {
        this.ontoClassifyInfos = ontoClassifyInfo;
    }
    
    public void setOntology(DartOntology dartOntology, int position){
        if (null==ontoClassifyInfos[position]){
            ontoClassifyInfos[position]=new OntoClassifyInfo(position);
        }
        this.ontoClassifyInfos[position].setDartOntology(dartOntology);
    }
    
    public void setSelect(boolean select, int position){
        if (select){
            this.ontoClassifyInfos[position].setOntoSelect("true");
        }
        else{
            this.ontoClassifyInfos[position].setOntoSelect("false");
        }
    }
    
    
}
