/*
 * Created on 2005-12-28
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.splitter.impl;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Token;

import edu.zju.tcmsearch.lucene.splitter.DictTree;
import edu.zju.tcmsearch.lucene.splitter.data.DictNode;

public class ChineseSplitter {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(ChineseSplitter.class);

    private Token token;
    private Queue<Token> tokenQueue = new LinkedBlockingQueue<Token>();
    private Set<Integer> hasTokenedSet=new HashSet<Integer>();//动态规划法,将已经token过的起始位置放入到Set里,下次见到就不用再计算了
    private DictTree dictTree;


    /**
     * @return Returns the token.
     */
    public Token getToken() {
        return token;
    }

    /**
     * @param token The token to set.
     */
    public void setToken(Token token) {
        this.token = token;
    }

    /**
     * @return Returns the tokenQueue.
     */
    public Queue<Token> getTokenQueue() {
        return tokenQueue;
    }
    
    private boolean isTokened(int pos){
        return this.hasTokenedSet.contains(pos);        
    }
    
    private void setTokened(int pos){
        hasTokenedSet.add(pos);
    }




    /**
     * @return Returns the dictTree.
     */
    public DictTree getDictTree() {
        return dictTree;
    }

    /**
     * @param dictTree The dictTree to set.
     */
    public void setDictTree(DictTree dictTree) {
        this.dictTree = dictTree;
    }

    public ChineseSplitter(Token token) {
        super();
        this.token=token;
    }

    public Queue<Token> split() {
        if (null==token){
            return tokenQueue;
        }        
        
        if ((token.endOffset() - token.startOffset()) <= 1) {
            tokenQueue.offer(token);
            return tokenQueue;
        }
        enqueueToken(0,DictNode.EMPTY_NODE);
        return tokenQueue;
    }
    
    private void enqueueToken(int pos,DictNode parentNode){
       // logger.debug(" start enqueue token! pos is "+pos+" , parent node is "+parentNode);
        if (pos>=token.termText().length()){
            return;
        }        
        if (isTokened(pos) && parentNode.getLevel()==0){
            return;
        }        
        if(parentNode.getLevel()==0){
          setTokened(pos);   
        }
        String strPrefix=token.termText().substring(pos, pos+1);
        //logger.debug(" prefix is "+strPrefix);
        DictNode dictNode=getDictTree().buildOrGetSubNodes(strPrefix,parentNode);
        //logger.debug(" pos is "+pos+"; dictnode is "+dictNode);
        
        if (null==dictNode){
            enqueueToken(pos+1,DictNode.EMPTY_NODE);//当前字无法组成任何词,往后移一位,继续跑
            return;
        }
        if (dictNode.hasEnd()){
//          enqueue the current node;
            dictNode.incUseCount();
            Token curToken=new Token(dictNode.getTokenValue(),token.startOffset()+pos+1-dictNode.getLevel(),token.startOffset()+pos+1);
            tokenQueue.offer(curToken);
            enqueueToken(pos+1,DictNode.EMPTY_NODE);//截完上一个词,开始截下一词
        }       
        enqueueToken(pos+1,dictNode);//继续往下跑,看看能不能组成更大的词
    }   


}
