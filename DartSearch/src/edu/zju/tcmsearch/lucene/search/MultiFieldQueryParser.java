/*
 * Created on 2005-12-19
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.search;

import org.apache.lucene.search.Query;

public interface MultiFieldQueryParser {
    public Query parse(String query, String[] fields);
    public Query parse(String query, String[] fields, String[] requiredQueries,String[] requiredFields);
}
