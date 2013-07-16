/*
 * Created on 2005-11-26
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.common.service;

import edu.zju.tcmsearch.web.form.query.OntoClassifyInfo;

public interface OntoCategoryService {
    public void updateOntoClassify(OntoClassifyInfo[] ontoClassifyInfos,Integer level,Integer metaCategoryId);
}
