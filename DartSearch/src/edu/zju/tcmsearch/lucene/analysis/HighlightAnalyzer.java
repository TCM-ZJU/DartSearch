/*
 * Created on 2005-12-31
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.analysis;

import java.io.Reader;

import junit.framework.Assert;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.search.Query;

public class HighlightAnalyzer  extends Analyzer{
    Query highlightQuery;
    
    public HighlightAnalyzer(Query query) {
        super();
        // TODO Auto-generated constructor stub
        this.highlightQuery = query;
    }

    public TokenStream tokenStream(String fieldName, Reader reader){
        return new HighlightTokenFilter(new LetterTokenizer(reader),highlightQuery);
      }
}
