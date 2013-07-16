/*
 * Created on 2005-12-30
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.splitter;

import java.util.Set;

import edu.zju.tcmsearch.lucene.splitter.data.DictNode;
import edu.zju.tcmsearch.lucene.splitter.data.DictValue;

public interface DictService {

    public DictNode saveNode(String strPrefix);
    public DictNode readNode(String strPrefix);
    public void saveAllNodeToFile();
}
