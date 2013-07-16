package edu.zju.tcmsearch.query.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import cn.edu.zju.dart.core.exceptions.DartCoreResultInvalidException;
import cn.edu.zju.dart.core.query.sqlplan.result.IQueryResult;
import cn.edu.zju.dart.core.query.sqlplan.result.IQueryResultMetaData;
import cn.edu.zju.dart.core.query.sqlplan.result.IQueryResultPageManager;
import cn.edu.zju.dart.core.query.sqlplan.result.IResultDataItem;
import edu.zju.tcmsearch.exception.query.TcmRuntimeException;
import cn.edu.zju.dart.core.resregistry.model.IRelation;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */

/**
 * 由于dartcore提供的queryResult里没有iterator,以及资源关闭等问题，在这里包一层
 */
public class QueryResult {
    private IQueryResult queryResult;
    
    private IQueryResultMetaData metaData;

    private ColumnInfo[] columnInfos;

    private List<IResultDataItem> resultDataList = new ArrayList<IResultDataItem>();

    private DartQuery dartQuery;

    private PageList pageList;
    
    private Map<ColumnInfo,Integer> column2index= new HashMap<ColumnInfo,Integer>();
    
    /*
     * 返回数据来源，即可查到数据的表@ming
     */
    private IRelation[] usedRelation;
    
    public void setUsedRelation(IRelation[] usedRelation){
    	this.usedRelation=usedRelation;
    }
    
    public IRelation[] getUsedRelation(){
    	return this.usedRelation;
    }

    /* (non-Javadoc)
     * @see cn.edu.zju.dart.core.query.sqlplan.result.IQueryResult#getQueryResultPageManager()
     */
    public IQueryResultPageManager getQueryResultPageManager() throws DartCoreResultInvalidException {
        return queryResult.getQueryResultPageManager();
    }

    public PageList getPageList() {
        if (null != pageList)
            return pageList;
        pageList = new PageList(this);
        return pageList;
    }

    public boolean isMultiPage() {
        return getPageList().isMultiPage();
    }

    public int getColumnCount() {
        try {
            return this.metaData.getColumnCount();
        } catch (DartCoreResultInvalidException e) {
            throw new TcmRuntimeException("取查询结果的列数时出错啦!", e);
        } 
    }

    public void setColumnNames(String[] columnNames) {

    }

    /**
     * @param pageSize
     *            The pageSize to set.
     */
    public void setPageSize(int pageSize) {
        getPageList().setPageSize(pageSize);
    }

    public DartQuery getDartQuery() {
        return dartQuery;
    }

    public void setDartQuery(DartQuery dartQuery) {
        this.dartQuery = dartQuery;        

    }

    public IQueryResult getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(IQueryResult queryResult) {
        this.queryResult = queryResult;
        try {
            this.metaData=this.queryResult.getMetaData();
        } catch (DartCoreResultInvalidException e) {
            throw new TcmRuntimeException("取queryResult的源数据时出错!",e);
        }
        String[] columnNames;
        try {
            columnNames = metaData.getColumnNames();
        } catch (DartCoreResultInvalidException e) {
            throw new TcmRuntimeException("取查询结果的列名时出错啦!", e);
        }
        column2index.clear();
        columnInfos = new ColumnInfo[columnNames.length];
        for (int i = 0; i < columnNames.length; i++) {
            columnInfos[i] = new ColumnInfo();
            columnInfos[i].setColumnName(columnNames[i]);
            columnInfos[i].setDartQuery(getDartQuery());
            columnInfos[i].setQueryResult(this);
            column2index.put(columnInfos[i],new Integer(i+1));
        }
    }

    public List<IResultDataItem> getResultDataList() {
        return resultDataList;
    }

    public void setResultDataList(List<IResultDataItem> resultDataList) {
        this.resultDataList = resultDataList;
    }

    public ColumnInfo[] getColumnInfos() {
        return columnInfos;
    }

    public void addResultDataItem(IResultDataItem resultDataItem) {
        resultDataList.add(resultDataItem);
    }

    public int getResultCount() {
        try {   
            return this.queryResult.getDataItemCount();
        } catch (DartCoreResultInvalidException e) {
            throw new TcmRuntimeException("取queryResult的总条数时出错!",e);
        }
//        return resultDataList.size();
    }

    public int getSqlType(int index) throws TcmRuntimeException {
    	if(index<0 || index >getColumnCount()){
    	   throw new TcmRuntimeException("Column Index 下标越界");
    	} 
    	return this.metaData.getSqlType(index);
    }
    
   public int getSqlType(ColumnInfo col) throws TcmRuntimeException{
	   Integer index=column2index.get(col);
	   if(null==index)
		   throw new TcmRuntimeException("Column 对象在结果表中 ");
	   return this.getSqlType(index.intValue());
   }
}
