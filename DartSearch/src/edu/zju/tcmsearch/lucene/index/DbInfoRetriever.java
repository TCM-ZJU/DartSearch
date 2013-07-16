/*
 * Created on 2005-12-16
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index;

import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import edu.zju.tcmsearch.lucene.index.data.DbResInfo;
import edu.zju.tcmsearch.lucene.index.data.TableInfo;

public interface DbInfoRetriever {
    public List<QName> getDbQNames();
    public List<TableInfo> getTableInfoList(QName dbQName);
    public DbResInfo getDbRes(QName dbQName);
    public Set<String> getOntoUris(QName dbQName);
    public List<TableInfo> getAllTableInfos();
    public List<String> getAllOntoUris();
}
