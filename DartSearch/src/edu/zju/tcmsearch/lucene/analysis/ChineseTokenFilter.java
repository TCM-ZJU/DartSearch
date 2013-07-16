/*
 * Created on 2005-12-28
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

import edu.zju.tcmsearch.lucene.splitter.Splitter;

public class ChineseTokenFilter extends TokenFilter {
    
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(ChineseTokenFilter.class);

    private boolean tagFiltered;

    private List<String> filterTagList = new ArrayList<String>();
    
    private Splitter splitter;
  //分词时,将一个句子的分完后保存在queue中,下一个next只在这个queue中取,而不用input.next
    private Queue<Token> tokenQueue=new LinkedBlockingQueue<Token>();
    /**
     * @return Returns the tagFiltered.
     */
    public boolean isTagFiltered() {
        return tagFiltered;
    }

    /**
     * @param tagFiltered
     *            The tagFiltered to set.
     */
    public void setTagFiltered(boolean tagFiltered) {
        this.tagFiltered = tagFiltered;
    }

    public ChineseTokenFilter(TokenStream in,Splitter splitter,List<String> filterTagList) {
        super(in); 
        this.filterTagList=filterTagList;
        this.splitter=splitter;
    }

    private boolean isFilterTag(Token t) {
        if (null == t) {
            return false;
        }
        return filterTagList.contains(t.termText());
    }

    @Override
    public Token next() throws IOException {
        Token t=this.tokenQueue.poll();
        //logger.debug(" tokenQueue size is: "+this.tokenQueue.size());
        if (null!=t){
            //logger.debug(" chinese token is: "+t.termText()+" "+t.startOffset()+"->"+t.endOffset());
            return t;
        }        
        
        while (true){
          t = getNextAvailableToken();
          if (t==null){
              return null;
          }
          this.tokenQueue=this.splitter.split(t);
          if (this.tokenQueue.size()>0){
              break;
          }
        }
        
        return this.tokenQueue.poll();
    }

    private Token getNextAvailableToken() throws IOException {        
        Token t = input.next();
        if (null == t) {
            return null;
        }

        int times = 0;
        if (isFilterTag(t)) {
            do {
                t = input.next();
                times++;
            } while (!isFilterTag(t) && times < 3);// 滤掉<span></span>中间的内容,直到遇到下一个span,否则一直滤掉
            return getNextAvailableToken();
        }
        else{
            return t;
        }
    }


}
