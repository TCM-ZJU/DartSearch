package edu.zju.tcmsearch.common.service;

import java.util.List;
import java.util.Map;

import cn.edu.zju.dart.core.ontology.IOntologySchema;
import cn.edu.zju.dart.core.utils.DartCoreSettings;

import edu.zju.tcmsearch.common.domain.DartOntoCategory;
import edu.zju.tcmsearch.common.domain.DartOntology;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public interface OntoService {
  public DartOntology getInstanceOntology(String identity);
  public List<DartOntology> getBaseOntologies();
  public Map<String,DartOntology> getOntologyMap();
  public DartOntology getValueOntology(String identity);  
  public IOntologySchema getOntologySchema();
  
  public List<DartOntology> getCateOntologies(int level, int cid);
  public DartCoreSettings getDartCoreSettings();
}
