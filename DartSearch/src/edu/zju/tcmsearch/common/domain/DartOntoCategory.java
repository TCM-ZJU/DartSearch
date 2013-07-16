/*
 * Created on 2005-10-8
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.common.domain;

import java.util.ArrayList;
import java.util.List;

public class DartOntoCategory {
    private String categoryName;
    private String categoryText;
    private List<DartOntology> ontoCategoryList=new ArrayList<DartOntology>();
    /**
     * @return Returns the categoryName.
     */
    public String getCategoryName() {
        return categoryName;
    }
    /**
     * @param categoryName The categoryName to set.
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    /**
     * @return Returns the categoryText.
     */
    public String getCategoryText() {
        return categoryText;
    }
    /**
     * @param categoryText The categoryText to set.
     */
    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }
    /**
     * @return Returns the ontoCategoryList.
     */
    public List<DartOntology> getOntoCategoryList() {
        return ontoCategoryList;
    }

    public void addOntology(DartOntology dartOntology){
        getOntoCategoryList().add(dartOntology);
    }
    
    


}
