/*
 * Created on 2005-12-14
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.search;

import org.apache.lucene.search.Query;

public interface Searcher {
    public SearchResults search(String queryExpression, String[] fields,
            QueryStorer queryStorer);

    public SearchResults search(String queryExpression, String[] fields,
            String[] requiredQueries, String[] requiredFields,
            QueryStorer queryStorer);

    /**
     * 选项最多的API,是唯一的一个完整的查询实现,其它接口都是调用该API实现部分功能 一般最终用户不需要如此复杂的API
     * 
     * @param queryExpression
     * @param fields
     * @param requiredQuery
     * @param requiredFields
     * @param queryStorer
     *            存放query,目前主要将query存在HttpSession中用于highlight和结果中搜索
     * @param orignialQuery
     *            原有的query,用来实现结果中搜索
     * @return
     */
    public SearchResults search(String queryExpression, String[] fields,
            String[] requiredQueries, String[] requiredFields,
            QueryStorer queryStorer, Query originalQuery);

    /**
     * 常用的查询,限定了查询的fields 和ontologyName
     * 
     * @param queryExpression
     * @param ontologyName
     * @return
     */
    public SearchResults search(String queryExpression, String ontologyName,
            QueryStorer queryStorer);
    
    public SearchResults search(String queryExpression,QueryStorer queryStorer);    

    /**
     * 在结果中搜索数据的API
     * @param queryExpression
     * @param originalQuery
     * @return
     */
    public SearchResults searchInResult(String queryExpression,QueryStorer queryStorer,
            Query originalQuery);
}
