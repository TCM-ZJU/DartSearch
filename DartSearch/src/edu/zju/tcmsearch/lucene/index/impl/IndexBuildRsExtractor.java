/*
 * Created on 2005-12-26
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.index.IndexWriter;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import edu.zju.tcmsearch.lucene.index.IndexBuildCallBack;
import edu.zju.tcmsearch.lucene.index.IndexBuildUtil;
import edu.zju.tcmsearch.lucene.index.data.DbRecData;
import edu.zju.tcmsearch.lucene.index.data.TableInfo;

public class IndexBuildRsExtractor implements ResultSetExtractor{
    private IndexWriter writer;
    private TableInfo tableInfo;
    private IndexBuildUtil indexBuildUtil;
    private IndexBuildCallBack indexBuildCallBack;
    private Map<String,Set<Object>> constraintOnto2DataID = null;

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




    public IndexBuildRsExtractor(IndexWriter writer,TableInfo tableInfo,IndexBuildCallBack indexBuildCallBack,IndexBuildUtil indexBuildUtil) {
        super();
        this.writer = writer;
        this.tableInfo=tableInfo;
        this.indexBuildCallBack=indexBuildCallBack;
        this.indexBuildUtil=indexBuildUtil;
    }
    /**
     * 接DbContentRetrieverImpl的buildRecIndex方法中的jdbcTemplate.query(queryStr,resultSetExtractor)后执行
     * 对queryStr查询出来的结果集中的每一行数据建立索引
     */
    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
        int counter=0;
        while (rs.next()){
        	//得到结果集中的当前行数据信息
            DbRecData dbRecData=indexBuildUtil.retrieveRecData(rs,tableInfo);
            if(constraintOnto2DataID != null){
            	for(String onto : constraintOnto2DataID.keySet()){
            		Set<Object> dataID = constraintOnto2DataID.get(onto);
            		if(dataID.contains(dbRecData.getPrimaryKey())){
            			dbRecData.addDataSpecifiedOnto(onto);
            		}
            	}
            }
            //这一步将执行实际的索引建立操作
            indexBuildUtil.writeRecIndex(writer,dbRecData);            
            counter++;
            if (null!=getIndexBuildCallBack()){
               getIndexBuildCallBack().execute(counter,dbRecData);
            }
            dbRecData=null;
        }
        return counter;
    }


	public Map<String, Set<Object>> getConstraintOnto2Data() {
		return constraintOnto2DataID;
	}


	public void setConstraintOnto2Data(
			Map<String, Set<Object>> constraintOnto2DataID) {
		this.constraintOnto2DataID = constraintOnto2DataID;
	}

}
