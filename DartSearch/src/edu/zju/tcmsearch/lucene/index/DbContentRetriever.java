/*
 * Created on 2005-12-16
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index;

import java.util.List;

import org.apache.lucene.index.IndexWriter;

import edu.zju.tcmsearch.lucene.index.data.DbRecData;
import edu.zju.tcmsearch.lucene.index.data.TableInfo;

public interface DbContentRetriever {
  public List<DbRecData> getTableContent(TableInfo tableInfo);
  public Integer buildRecIndex(IndexWriter writer,TableInfo tableInfo,IndexBuildCallBack indexBuildCallBack);
  public DbRecData getTableContentByKey(String tableIdentity,String primaryKey);
}
