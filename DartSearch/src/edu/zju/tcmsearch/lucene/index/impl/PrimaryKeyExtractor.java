/*
 * Created on 2005-12-26
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import edu.zju.tcmsearch.lucene.index.IndexBuildCallBack;
import edu.zju.tcmsearch.lucene.index.IndexBuildUtil;
import edu.zju.tcmsearch.lucene.index.data.DbRecData;
import edu.zju.tcmsearch.lucene.index.data.TableInfo;

public class PrimaryKeyExtractor implements ResultSetExtractor{
	private Set<Object> dataID;
    private TableInfo tableInfo;
    private IndexBuildUtil indexBuildUtil;
    private IndexBuildCallBack indexBuildCallBack;


    /**
     * @return Returns the indexBuildCallBack.
     */
    public IndexBuildCallBack getIndexBuildCallBack() {
        return indexBuildCallBack;
    }


    /**
     * @param indexBuildCallBack The indexBuildCallBack to set.
     */
    public void setIndexBuildCallBack(IndexBuildCallBack indexBuildCallBack) {
        this.indexBuildCallBack = indexBuildCallBack;
    }




    public PrimaryKeyExtractor(Set<Object> dataID,TableInfo tableInfo,IndexBuildCallBack indexBuildCallBack,IndexBuildUtil indexBuildUtil) {
        super();
        this.dataID = dataID;
        this.tableInfo=tableInfo;
        this.indexBuildCallBack=indexBuildCallBack;
        this.indexBuildUtil=indexBuildUtil;
    }


    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
        int counter=0;
        while (rs.next()){
            DbRecData dbRecData=indexBuildUtil.retrieveRecPrimaryKey(rs,tableInfo);
            dataID.add(dbRecData.getPrimaryKey());
            counter++;
            if (null!=getIndexBuildCallBack()){
               getIndexBuildCallBack().execute(counter,dbRecData);
            }
            dbRecData=null;
        }
        return counter;
    }

}
