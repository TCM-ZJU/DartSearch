/*
 * Created on 2005-12-19
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.search.impl;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

import edu.zju.tcmsearch.exception.lucene.TcmLuceneException;
import edu.zju.tcmsearch.lucene.search.MultiFieldQueryParser;


public class MultiFieldQueryParserImpl extends QueryParser implements MultiFieldQueryParser {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(MultiFieldQueryParserImpl.class);

    private Analyzer requiredAnalyzer;

    
    /**
     * @return Returns the requiredAnalyzer.
     */
    public Analyzer getRequiredAnalyzer() {
        return requiredAnalyzer;
    }



    /**
     * @param requiredAnalyzer The requiredAnalyzer to set.
     */
    public void setRequiredAnalyzer(Analyzer requiredAnalyzer) {
        this.requiredAnalyzer = requiredAnalyzer;
    }



    public MultiFieldQueryParserImpl(String field,Analyzer analyzer) {
        super(field, analyzer);
    }



    final protected Query getWildcardQuery(String field, String term)
            throws ParseException {
        throw new ParseException("Wildcard Query not allowed.");
    }

    final protected Query getFuzzyQuery(String field, String term)
            throws ParseException {
        throw new ParseException("Fuzzy Query not allowed.");
    }

    public Query parse(String query, String[] fields) {
    	    	
        BooleanQuery bQuery = new BooleanQuery();
        try {
            for (int i = 0; i < fields.length; i++) {
                QueryParser parser = new MultiFieldQueryParserImpl(fields[i], getAnalyzer());
                Query q = parser.parse(query);
//                bQuery.add(q, false, false);
                bQuery.add(q,BooleanClause.Occur.SHOULD);
            }
        } catch (ParseException e) {
            throw new TcmLuceneException("Query parse failure in multiFieldQueryParser",e);
        }
        return bQuery;
    }

    public Query testParse(String query, String[] fields) {
        BooleanQuery bQuery = new BooleanQuery();
        try {
            for (int i = 0; i < fields.length; i++) {
            	
                QueryParser parser = new MultiFieldQueryParserImpl(fields[i], getAnalyzer());
                String[] exor = query.split("or");
        	    BooleanQuery orQuery = new BooleanQuery();
        		for(int k=0;k<exor.length;k++){
        			String[] exand = exor[k].split("and");
        			BooleanQuery andQuery = new BooleanQuery();
        			for(int j=0;j<exand.length;j++){
        				//System.out.println(exand[j]);
        				Query q = parser.parse(exand[j]);
        				andQuery.add(q,BooleanClause.Occur.MUST);
        			}
        		    orQuery.add(andQuery,BooleanClause.Occur.SHOULD);
        		}
        		bQuery.add(orQuery,BooleanClause.Occur.SHOULD);
            }
        } catch (ParseException e) {
            throw new TcmLuceneException("Query parse failure in multiFieldQueryParser",e);
        }
        return bQuery;
    }
    /**
     * query:普通的query关键字序列
     * fields: 普通的fields搜索域
     * requiredQueries:必须搜索的关键字序列
     * requiredFields:对应于requiredQueries的搜索域
     * 该方法的处理逻辑为首先生成query和fields构成的普通baseQuery，再判断requiredQueries和requiredFields是否为空，若不为空构造
     * 对应的requiredQuery,最后baseQuery和requiredQuery做and组合。
     */
    public Query parse(String query, String[] fields, String[] requiredQueries, String[] requiredFields) {
        Query baseQuery=parse(query,fields);
        if (null==requiredFields || requiredFields.length==0){
            return baseQuery;
        }
        if (emptyRequiredQueries(requiredQueries) ){
            return baseQuery;
        }
        BooleanQuery bQuery=new BooleanQuery();
//        bQuery.add(baseQuery,true,false);
        bQuery.add(baseQuery,BooleanClause.Occur.MUST);
        try {
            for (int i = 0; i < requiredFields.length; i++) {
            	if (null==requiredQueries[i] || "".equals(requiredQueries[i]) || "empty".equals(requiredQueries[i])){
            		continue;
            	}
        		String[] rqs = requiredQueries[i].split(",");
    			BooleanQuery bq =new BooleanQuery();
        		for(String rq : rqs){
                    QueryParser parser = new MultiFieldQueryParserImpl(requiredFields[i], getAnalyzer());
                    logger.debug("required field is " + requiredFields[i]);
                    logger.debug("required sub query is " + rq);
                    Query q = parser.parse(rq);
//        			bq.add(q,false,false);
        			bq.add(q,BooleanClause.Occur.SHOULD);
        		}
//              bQuery.add(q, true, false);
        		bQuery.add(bq,BooleanClause.Occur.MUST);
            }
        } catch (ParseException e) {
            throw new TcmLuceneException("Query parse failure in multiFieldQueryParser",e);
        }
        return bQuery;
    }



	private boolean emptyRequiredQueries(String[] requiredQueries) {
		if (null==requiredQueries || 0==requiredQueries.length)
			return true;
		for (int i = 0; i < requiredQueries.length; i++){
			if (null!=requiredQueries[i] && !"".equals(requiredQueries[i]) && !"empty".equals(requiredQueries[i]))
				return false;
		}
		return true;
	}

}
