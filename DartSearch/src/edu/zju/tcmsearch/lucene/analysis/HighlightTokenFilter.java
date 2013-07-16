/*
 * Created on 2005-12-31
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.search.Query;

public class HighlightTokenFilter  extends TokenFilter {
    Query highlightQuery;
    
    public HighlightTokenFilter(TokenStream in,Query query){
        super(in); 
        this.highlightQuery=query;
    }

    @Override
    public Token next() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

}
