/*
 * Created on 2005-12-30
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.splitter.impl;

import java.util.Queue;

import org.apache.lucene.analysis.Token;

import edu.zju.tcmsearch.lucene.splitter.DictTree;
import edu.zju.tcmsearch.lucene.splitter.Splitter;

public class SplitterImpl implements Splitter{
    private DictTree dictTree;

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

    public Queue<Token> split(Token token) {
        getDictTree().loadDictMap();
        ChineseSplitter cnSplitter=new ChineseSplitter(token);
        cnSplitter.setDictTree(dictTree);
        return cnSplitter.split();
    }

}
