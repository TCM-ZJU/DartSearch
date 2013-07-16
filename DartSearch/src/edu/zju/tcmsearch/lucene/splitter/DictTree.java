/*
 * Created on 2005-12-30
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.splitter;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.zju.tcmsearch.lucene.splitter.data.DictNode;
import edu.zju.tcmsearch.lucene.splitter.data.DictValue;

public interface DictTree {

    public abstract DictNode buildSubNodes(String strPrefix);
    
    public DictNode getRootNode(String strPrefix);

    public abstract DictNode buildOrGetSubNodes(String strPrefix,
            DictNode parentNode);
    
    public void saveDictMap();
    
    public Map<String,DictNode> loadDictMap();
    
    public List<DictValue> getDictValues(String strPrefix);
    
    public Set<String> getAllPrefixes();
 
}