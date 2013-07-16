/*
 * Created on 2005-12-25
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.search.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.search.Query;

import edu.zju.tcmsearch.lucene.search.QueryStorer;

public class SimpleQueryStorer implements QueryStorer{
    Query query;
    Query highlightQuery;
    String queryExpression;
    Map<String,Object> orignalQueryInfo= new HashMap<String,Object>();

    /**
     * Logger for this class
     */
    protected static final Logger logger = Logger.getLogger(QueryStorer.class);
    
    public void storeQuery(Query query) {
        this.query=query;
    }

    public Query getQuery() {        
        return query;
    }

    public void storeHighlightQuery(Query query) {
        this.highlightQuery=query;
    }

    public Query getHighlightQuery() {
        return this.highlightQuery;
    }

    public void storeQueryExpression(String expr){
    	this.queryExpression = expr;
    }
    public String  getQueryExpression(){
    	return this.queryExpression;
    }
    
    public Object getQueryInfo(String name){
    	return orignalQueryInfo.get(name);
    }
    public void   storeQueryInfo(String name,Object info){
    	orignalQueryInfo.put(name,info);
    }
    
    public void freeVolatile(){
    	orignalQueryInfo.clear();
    	logger.debug("free volatile infomation");
    }
}
