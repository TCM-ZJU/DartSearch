/*
 * Created on 2005-11-26
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.common.service.impl;

import edu.zju.tcmsearch.common.service.OntoCategoryService;
import edu.zju.tcmsearch.dao.meta.MetaCategoryDAO;
import edu.zju.tcmsearch.query.meta.MetaCategoryData;
import edu.zju.tcmsearch.util.web.CateNodeUtil;
import edu.zju.tcmsearch.web.form.query.OntoClassifyInfo;

public class OntoCategoryServiceImpl implements OntoCategoryService{
    private MetaCategoryDAO metaCategoryDAO;
    
    /**
     * @return Returns the metaCategoryDAO.
     */
    public MetaCategoryDAO getMetaCategoryDAO() {
        return metaCategoryDAO;
    }

    /**
     * @param metaCategoryDAO The metaCategoryDAO to set.
     */
    public void setMetaCategoryDAO(MetaCategoryDAO metaCategoryDAO) {
        this.metaCategoryDAO = metaCategoryDAO;
    }

    public void updateOntoClassify(OntoClassifyInfo[] ontoClassifyInfos,Integer level,Integer metaCategoryId) {

        for (int i=0;i<ontoClassifyInfos.length;i++){
            if (null==ontoClassifyInfos[i].getDartOntology()){
                continue;
            }
            MetaCategoryData metaCategoryData=metaCategoryDAO.getCategoryById(level,metaCategoryId);
            if (metaCategoryDAO.existClassification(metaCategoryData,ontoClassifyInfos[i].getDartOntology())){
                if (!ontoClassifyInfos[i].isSelect()){
                    metaCategoryDAO.removeClassification(metaCategoryData,ontoClassifyInfos[i].getDartOntology());
                }    
            }
            else{
                if (ontoClassifyInfos[i].isSelect()){
                    metaCategoryDAO.addClassification(metaCategoryData,ontoClassifyInfos[i].getDartOntology());
                } 
            }            
        }
    }


}
