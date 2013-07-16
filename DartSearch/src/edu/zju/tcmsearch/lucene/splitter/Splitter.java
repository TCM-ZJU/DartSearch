/*
 * Created on 2005-12-28
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.splitter;

import java.util.Queue;

import org.apache.lucene.analysis.Token;

public interface Splitter {
    public Queue<Token> split(Token token);
    public DictTree getDictTree();
}
