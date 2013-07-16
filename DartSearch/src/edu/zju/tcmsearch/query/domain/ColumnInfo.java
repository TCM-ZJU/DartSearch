/*
 * Created on 2005-7-25
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.query.domain;

import cn.edu.zju.dart.core.exceptions.DartCoreResultInvalidException;
import cn.edu.zju.dart.core.query.ValueNode;
//import cn.edu.zju.dart.core.query.IValueNode;
import edu.zju.tcmsearch.exception.query.TcmRuntimeException;

public class ColumnInfo {
    private String columnName;
   
    private QueryResult queryResult;
    
    private DartQuery dartQuery;
    

    

    /**
     * @return Returns the dartQuery.
     */
    public DartQuery getDartQuery() {
        return dartQuery;
    }

    /**
     * @param dartQuery The dartQuery to set.
     */
    public void setDartQuery(DartQuery dartQuery) {
        this.dartQuery = dartQuery;
    }

    /**
     * @return Returns the queryResult.
     */
    public QueryResult getQueryResult() {
        return queryResult;
    }

    /**
     * @param queryResult The queryResult to set.
     */
    public void setQueryResult(QueryResult queryResult) {
        this.queryResult = queryResult;
    }

    /**
     * @return Returns the columnName.
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * @param columnName
     *            The columnName to set.
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    
    public ValueNode getValueNode(){
        try {
            return queryResult.getQueryResult().getMetaData().getValueNode(columnName);
        } catch (DartCoreResultInvalidException e) {
            throw new TcmRuntimeException("取查询结果的ValueNode时出错啦!",e);
        }
    }
    
    public ValueOntology getValueOntology(){
        return getDartQuery().getValue(getValueNode().toString());
    }
    
    public String getOntoName(){
        return getValueOntology().getName();
    }  

    public int getSqlType()throws TcmRuntimeException {
    	return queryResult.getSqlType(this);
    }
}
