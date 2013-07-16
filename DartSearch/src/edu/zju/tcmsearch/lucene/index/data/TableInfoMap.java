/*
 * Created on 2005-12-20
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.data;

import java.util.HashMap;
import java.util.Map;

import edu.zju.tcmsearch.lucene.index.DbInfoRetriever;

public class TableInfoMap {
    private DbInfoRetriever dbInfoRetriever;

    Map<String,TableInfo> tiMap;
    
    /**
     * @return Returns the dbInfoRetriever.
     */
    public DbInfoRetriever getDbInfoRetriever() {
        return dbInfoRetriever;
    }

    /**
     * @param dbInfoRetriever The dbInfoRetriever to set.
     */
    public void setDbInfoRetriever(DbInfoRetriever dbInfoRetriever) {
        this.dbInfoRetriever = dbInfoRetriever;
    }

    public TableInfo getTableInfo(String tableIdentity){
        if (null==tiMap){
            tiMap=new HashMap<String,TableInfo>();
            for (TableInfo tableInfo:dbInfoRetriever.getAllTableInfos()){
               tiMap.put(tableInfo.getTableIdentity(),tableInfo);
            }           
        }
        return tiMap.get(tableIdentity);
    }

}
