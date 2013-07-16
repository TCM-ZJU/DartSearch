/*
 * Created on 2005-12-14
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.impl;

import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.util.StopWatch;

import edu.zju.tcmsearch.exception.lucene.TcmLuceneException;
import edu.zju.tcmsearch.lucene.config.LuceneConfig;
import edu.zju.tcmsearch.lucene.index.IndexBuildCallBack;
import edu.zju.tcmsearch.lucene.index.IndexBuildUtil;
import edu.zju.tcmsearch.lucene.index.IndexBuilder;
import edu.zju.tcmsearch.lucene.index.IndexSource;
import edu.zju.tcmsearch.lucene.index.data.DbRecData;

public class IndexBuilderImpl implements IndexBuilder{
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(IndexBuilderImpl.class);
    //luceneConfig 存放从luceneConfig文件中的配置信息
    private LuceneConfig luceneConfig;
 
    private IndexSource indexSource;
    
    
    private Analyzer analyzer;
    private IndexBuildCallBack indexBuildCallBack;
    private IndexBuildUtil indexBuildUtil;



    /**
     * @return Returns the indexBuildUtil.
     */
    public IndexBuildUtil getIndexBuildUtil() {
        return indexBuildUtil;
    }

    /**
     * @param indexBuildUtil The indexBuildUtil to set.
     */
    public void setIndexBuildUtil(IndexBuildUtil indexBuildUtil) {
        this.indexBuildUtil = indexBuildUtil;
    }

    /**
     * @return Returns the indexBuildCallBack.
     */
    public IndexBuildCallBack getIndexBuildCallBack() {
        return indexBuildCallBack;
    }

    /**
     * @param indexBuildCallBack The indexBuildCallBack to set.
     */
    public void setIndexBuildCallBack(IndexBuildCallBack indexBuildCallBack) {
        this.indexBuildCallBack = indexBuildCallBack;
    }

    /**
     * @return Returns the analyzer.
     */
    public Analyzer getAnalyzer() {
        return analyzer;
    }

    /**
     * @param analyzer The analyzer to set.
     */
    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * @return Returns the indexSource.
     */
    public IndexSource getIndexSource() {
        return indexSource;
    }

    /**
     * @param indexSource The indexSource to set.
     */
    public void setIndexSource(IndexSource indexSource) {
        this.indexSource = indexSource;
    }

    /**
     * @return Returns the luceneConfig.
     */
    public LuceneConfig getLuceneConfig() {
        return luceneConfig;
    }

    /**
     * @param luceneConfig The luceneConfig to set.
     */
    public void setLuceneConfig(LuceneConfig luceneConfig) {
        this.luceneConfig = luceneConfig;
    }

    public void buildIndex() {
        buildIndex(getIndexBuildCallBack());        
    }

    /**
     * @param indexBuildCallBack  A callback to IndexBuildImpl
     */
    public void buildIndex(IndexBuildCallBack indexBuildCallBack) {
        try {
            StopWatch stopWatch=new StopWatch();
            stopWatch.start("lucene indexing");
            //indexBuildDir 获得索引存放目录
            String indexBuildDir = luceneConfig.getIndexDir().getIndexBuildDirectory();
            Directory dir = FSDirectory.getDirectory(indexBuildDir,true);
            IndexWriter writer = new IndexWriter(dir, analyzer, true);
            /*writer.maxFieldLength=this.luceneConfig.getMaxFieldLength();
            writer.mergeFactor=this.luceneConfig.getMergeFactor();
            writer.minMergeDocs=this.luceneConfig.getMinMergeDocs();
            writer.maxMergeDocs=this.luceneConfig.getMaxMergeDocs();*/
            
            writer.setMaxFieldLength(this.luceneConfig.getMaxFieldLength());
            writer.setMergeFactor(this.luceneConfig.getMergeFactor());
            writer.setMaxMergeDocs(this.luceneConfig.getMaxMergeDocs());
            writer.setMaxBufferedDocs(this.luceneConfig.getMinMergeDocs());
            
            
            int i=0;
            for (Iterator<DbRecData> dbRecIterator=indexSource.createIterator();dbRecIterator.hasNext();){
                DbRecData curRecData=dbRecIterator.next();
                i++;
                indexBuildCallBack.execute(i,curRecData);
                indexBuildUtil.writeRecIndex(writer,curRecData);
            }
            writer.close();
            stopWatch.stop();
            luceneConfig.getIndexDir().setIndexDirectory(indexBuildDir);
            logger.info(stopWatch.prettyPrint());
        } catch (IOException e) {
            throw new TcmLuceneException("建索引时文件读写出错！ 索引文件夹:"+luceneConfig.getIndexDir().getIndexBuildDirectory(),e);
        }       
    }
    
  
}
