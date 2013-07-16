/*
 * Created on 2005-7-25
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.query.domain;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import cn.edu.zju.dart.core.exceptions.DartCoreResultInvalidException;
import cn.edu.zju.dart.core.query.sqlplan.result.IQueryResultPageManager;
import cn.edu.zju.dart.core.query.sqlplan.result.IResultDataItem;
import edu.zju.tcmsearch.exception.query.TcmRuntimeException;

/**
 * 一个分页显示结果的类
 * 目前只采用最简单的方式:将所有的查询结果读到一个List,然后去取其中某一页的内容
 * 对于数据量太大的分页,所有结果读到一个List可能会有问题
 * @author xcc
 *
 */
public class PageList {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(PageList.class);

    private QueryResult queryResult;
    private IQueryResultPageManager pageManager;
    private int listSize;
    private int pageSize=30;
    
    
    /**
     * @return Returns the pageSize.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize The pageSize to set.
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public PageList(QueryResult queryResult){
        this.queryResult=queryResult;
        try {
            this.pageManager=queryResult.getQueryResultPageManager();
        } catch (DartCoreResultInvalidException e) {
            throw new TcmRuntimeException("取分页管理器出错!",e);
        }
        this.listSize=queryResult.getResultCount();
    }
    
    public int getMinPage(){
        return 1;
    }
    
    public int getNextPageNo(int pageNo){
        if (isLastPage(pageNo)){
            return pageNo;
        }
        else{
            return pageNo+1;
        }
    }
    
    public int getPrevPageNo(int pageNo){
        if (isFirstPage(pageNo)){
            return pageNo;
        }
        else{
            return pageNo-1;
        }
    }
    
    public int getMaxPage(){
        if (this.listSize % this.pageSize==0){
            return this.listSize / this.pageSize;
        }
        else{
            return this.listSize / this.pageSize+1;
        }
    }
    
    public boolean isFirstPage(int pageNo){
        return pageNo==getMinPage();
    }
    
    public boolean isLastPage(int pageNo){
        return pageNo==getMaxPage();
    }
    
    public boolean isMultiPage(){
        return getMaxPage()>1;
    }
    
    public int getFirstRecordPos(int pageNo){
        return (pageNo-1)*pageSize;
    }
    
    public int getLastRecordPos(int pageNo){
        if ((pageNo*pageSize)>this.listSize){
            return this.listSize-1;
        }
        else{
            return pageNo*pageSize-1;
        }
    }
    
    public int getCurPageSize(int pageNo){    	
    	return getLastRecordPos(pageNo)-getFirstRecordPos(pageNo)+1;
    }
    
    
    
    /**
     * 
     * @param pageNo
     * @return
     */
    public List<IResultDataItem> getPage(int pageNo){
      //  List<IResultDataItem> resultPageList=new ArrayList<IResultDataItem>();
        //logger.debug("pagesize is "+listSize);
        if (this.pageSize==0){
            return Collections.EMPTY_LIST;
        }
        try {
            return pageManager.getPage(getFirstRecordPos(pageNo)+1,pageSize);
        } catch (DartCoreResultInvalidException e) {
            throw new TcmRuntimeException("取分页的列表数据出错!",e);
        }
        
//        for (int i=getFirstRecordPos(pageNo);i<=getLastRecordPos(pageNo);i++){
//            resultPageList.add(queryResult.getResultDataList().get(i));
//        }
      //  return resultPageList;
    }
}
