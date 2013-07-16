/*
 * Created on 2005-12-14
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index;


public interface IndexBuilder {
    public void buildIndex();
    public void buildIndex(IndexBuildCallBack indexBuildCallBack);
}
