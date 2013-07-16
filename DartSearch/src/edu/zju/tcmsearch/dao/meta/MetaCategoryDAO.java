/*
 * Created on 2005-11-17
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.dao.meta;

import java.util.List;

import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.query.meta.MetaCategoryData;

public interface MetaCategoryDAO {    
    public List<MetaCategoryData> getCategory1();
    public List<MetaCategoryData> getCategory2(int pid);
    public List<MetaCategoryData> getCategoryByLevel(Integer level,Integer pid);    
    
    public List<String> getClassifiedOntoURI(int level, int id);
    
    public MetaCategoryData getCategoryById(int level, int id);   
    
    public int addClassification(MetaCategoryData metaCategoryData, DartOntology dartOntology);
    public int removeClassification(MetaCategoryData metaCategoryData,DartOntology dartOntology);
    public boolean existClassification(MetaCategoryData metaCategoryData, DartOntology dartOntology);
    
    public boolean addCategory(int level,int pid,String dname);
    public boolean removeCategory(int level,int id);
    public boolean renameCategory(int level,int id,String dname);
    /*
     * 添加Observer接收onMetaCategoryChange事件
     */
    public void addObserver(IMetaCategoryObserver mcObserver);
}
