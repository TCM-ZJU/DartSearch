/*
 * Created on 2005-12-28
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.splitter.dao;

import java.util.List;

import edu.zju.tcmsearch.lucene.splitter.data.DictValue;

public interface DictDAO {
  public List<DictValue> getDictValues(String strPrefix);
  public List<String> getAllPrefixes();
}
