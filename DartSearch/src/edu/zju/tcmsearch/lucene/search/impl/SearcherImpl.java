/*
 * Created on 2005-12-19
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.search.impl;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.springframework.util.StopWatch;

import edu.zju.tcmsearch.exception.lucene.TcmLuceneException;
import edu.zju.tcmsearch.lucene.config.LuceneConfig;
import edu.zju.tcmsearch.lucene.search.MultiFieldQueryParser;
import edu.zju.tcmsearch.lucene.search.QueryStorer;
import edu.zju.tcmsearch.lucene.search.SearchResults;
import edu.zju.tcmsearch.lucene.search.Searcher;

public class SearcherImpl implements Searcher {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(SearcherImpl.class);

    private MultiFieldQueryParser multiFieldQueryParser;

    private LuceneConfig luceneConfig;

    private String[] searchFields = { "showContent", "clobContent" };// {"fieldContent","clobContent"};
    
    private String[] requiredFields={"ontologyName"};

    /**
     * @return Returns the luceneConfig.
     */
    public LuceneConfig getLuceneConfig() {
        return luceneConfig;
    }

    /**
     * @param luceneConfig
     *            The luceneConfig to set.
     */
    public void setLuceneConfig(LuceneConfig luceneConfig) {
        this.luceneConfig = luceneConfig;
    }

    /**
     * @return Returns the multiFieldQueryParser.
     */
    public MultiFieldQueryParser getMultiFieldQueryParser() {
        return multiFieldQueryParser;
    }

    /**
     * @param multiFieldQueryParser
     *            The multiFieldQueryParser to set.
     */
    public void setMultiFieldQueryParser(
            MultiFieldQueryParser multiFieldQueryParser) {
        this.multiFieldQueryParser = multiFieldQueryParser;
    }

    public SearchResults search(String queryExpression, String[] fields,QueryStorer queryStorer) {
        return search(queryExpression, fields, null, null,queryStorer);
    }

    public SearchResults search(String queryExpression,QueryStorer queryStorer) {
        return search(queryExpression, searchFields,queryStorer);
    }
    
    public SearchResults search(String queryExpression, String[] fields, String[] requiredQueries, String[] requiredFields, QueryStorer queryStorer) {
        return search(queryExpression, searchFields, requiredQueries,requiredFields,queryStorer,null);
    }  
    
    /**
     * 选项最多的API,是唯一的一个完整的查询实现,其它接口都是调用该API实现部分功能 
     */
    public SearchResults search(String queryExpression, String[] fields,
    		String[] requiredQueries, String[] requiredFields,QueryStorer queryStorer,Query originalQuery) {
        try {
            StopWatch stopWatch = new StopWatch(); //StopWatch is used to get a time_segment. 
            stopWatch.start("lucene searching");
            FSDirectory directory = FSDirectory.getDirectory(luceneConfig.getIndexDir().getIndexDirectory(), false);
            /** 存储查询的原始信息 */
            queryStorer.storeQueryExpression(queryExpression);
            if(null!=requiredFields && null!=requiredQueries)
            {
            	 for(int i=0;i<requiredFields.length && i<requiredQueries.length;i++)
            	 {
            	  queryStorer.storeQueryInfo(requiredFields[i],requiredQueries[i]);
            	  logger.debug("recording orignal query info ---〉"+requiredFields[i]+" : "+requiredQueries[i]);
            	 }
            }
            
            Query query = parseQuery(queryExpression, fields, requiredQueries, requiredFields, originalQuery);
         
            logger.debug("the query : " + query.toString());
            //logger.debug("the query showContent: " + query.toString("showContent"));
            storeQuery(queryExpression, fields, queryStorer, originalQuery, query);            
            IndexSearcher indexSearcher = new IndexSearcher(directory);
            Hits hits = indexSearcher.search(query);
            SearchResults searchResults = new SearchResults(hits,queryStorer);
            stopWatch.stop();
            logger.debug(stopWatch.prettyPrint());
            return searchResults;
        } catch (IOException e) {
            throw new TcmLuceneException("error in open index dir for search!",
                    e);
        }
    }

    private void storeQuery(String queryExpression, String[] fields, QueryStorer queryStorer, Query originalQuery, Query query) {
        if (null!=queryStorer){
            queryStorer.storeQuery(query);
            Query highlightQuery;
            
            if (null==originalQuery){
                highlightQuery=parseQuery(queryExpression, fields,null,null,null);//highlightQuery不用包含限定的本体
            }
            else{
                highlightQuery=parseQuery(queryExpression, fields,null,null,queryStorer.getHighlightQuery());//highlightQuery不用包含限定的本体
            }
            queryStorer.storeHighlightQuery(highlightQuery);
        }
    }

    private Query parseQuery(String queryExpression, String[] fields, String[] requiredQueries, String[] requiredFields, Query originalQuery) {
        if (null==queryExpression || "".equals(queryExpression)){
            return originalQuery;
        }
        Query query = multiFieldQueryParser.parse(queryExpression, fields,
                requiredQueries, requiredFields);       
        if (null==originalQuery){
            return query; 
        }

        BooleanQuery bQuery=new BooleanQuery();
        bQuery.add(query,BooleanClause.Occur.MUST);
        bQuery.add(originalQuery,BooleanClause.Occur.MUST);
//        bQuery.add(query,true,false);
//        bQuery.add(originalQuery,true,false);
        return bQuery;
       
    }

    public SearchResults search(String queryExpression, String ontologyName,QueryStorer queryStorer) {
        String[] searchFields = { "showContent", "clobContent" };
        String[] requiredFields = { "ontologyName" };
        String[] requiredQueries={ontologyName};

        return search(queryExpression, searchFields, requiredQueries,
                requiredFields,queryStorer);
    }

    public SearchResults searchInResult(String queryExpression,QueryStorer queryStorer, Query originalQuery) {
        return search(queryExpression, searchFields, null,null,queryStorer,originalQuery);
    }



}
