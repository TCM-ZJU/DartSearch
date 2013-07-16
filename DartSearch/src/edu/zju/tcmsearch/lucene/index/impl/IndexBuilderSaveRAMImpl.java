/*
 * Created on 2005-12-26
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.impl;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.util.StopWatch;

import edu.zju.tcmsearch.exception.lucene.TcmLuceneException;
import edu.zju.tcmsearch.lucene.config.LuceneConfig;
import edu.zju.tcmsearch.lucene.index.DbContentRetriever;
import edu.zju.tcmsearch.lucene.index.DbInfoRetriever;
import edu.zju.tcmsearch.lucene.index.IndexBuildCallBack;
import edu.zju.tcmsearch.lucene.index.IndexBuilder;
import edu.zju.tcmsearch.lucene.index.data.DbRecData;
import edu.zju.tcmsearch.lucene.index.data.TableInfo;

/**
 * 该类主要设置构建索引所需要的所有信息，并通过回调类indexBuildCallBack执行建立索引的工作
 * luceneConfig:设置index的参数属性，如indexDir,minMergeDocs,maxMergeDocs,mergeFactor等
 * analyzer:设置采用的分词起，这里使用的是lib中的myAnalyzer.jar包中的IK_CAnalyzer分词算法
 * dbInfoRetriever:设置数据源信息获取类
 * dbContentRetriever:设置数据源的数据获取类，其中对取出的每条数据建立索引
 * indexBuildCallBack:构建索引的回调类
 * @author ddream
 *
 */
public class IndexBuilderSaveRAMImpl implements IndexBuilder {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(IndexBuilderSaveRAMImpl.class);

    
    private LuceneConfig luceneConfig;

    private Analyzer analyzer;

    private DbInfoRetriever dbInfoRetriever;

    private DbContentRetriever dbContentRetriever;
    
    private IndexBuildCallBack indexBuildCallBack;

    /**
     * @return Returns the dbInfoRetriever.
     */
    public DbInfoRetriever getDbInfoRetriever() {
        return dbInfoRetriever;
    }

    /**
     * @param dbInfoRetriever
     *            The dbInfoRetriever to set.
     */
    public void setDbInfoRetriever(DbInfoRetriever dbInfoRetriever) {
        this.dbInfoRetriever = dbInfoRetriever;
    }

    public void buildIndex() {
        buildIndex(getIndexBuildCallBack());
    }

   /**
    * 构建索引方法
    */
    public void buildIndex(IndexBuildCallBack indexBuildCallBack) {
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("lucene indexing");
            String buildIndexDir = luceneConfig.getIndexDir().getIndexBuildDirectory();
            Directory dir = FSDirectory.getDirectory(buildIndexDir, true);
            IndexWriter writer = new IndexWriter(dir, analyzer, true);
         /*   writer.maxFieldLength = this.luceneConfig.getMaxFieldLength();
            writer.mergeFactor = this.luceneConfig.getMergeFactor();
            writer.minMergeDocs = this.luceneConfig.getMinMergeDocs();
            writer.maxMergeDocs = this.luceneConfig.getMaxMergeDocs();*/
            
            //设置索引合并的参数
            writer.setMaxFieldLength(this.luceneConfig.getMaxFieldLength());
            writer.setMergeFactor(this.luceneConfig.getMergeFactor());
            writer.setMaxMergeDocs(this.luceneConfig.getMaxMergeDocs());
            writer.setMaxBufferedDocs(this.luceneConfig.getMinMergeDocs());
            
            
            int counter=0;
            //dbInfoRetriever.getAllTableInfos将根据数据库的连接信息，得到数据库中的表信息
            for (TableInfo tableInfo : dbInfoRetriever.getAllTableInfos()) {
                logger.debug(tableInfo);
                if (tableInfo.isRelationTable()){
                    continue;
                }
                if (!tableInfo.isInFullIndex()){
                    continue;
                }
                //在这一步通过回调类indexBuildCallBack对每一个TableInfo构建索引，
                //并返回构建索引的记录（数据表中的每一行）的数
                counter+=dbContentRetriever.buildRecIndex(writer, tableInfo,
                        indexBuildCallBack);
            }
            writer.close();
            stopWatch.stop();
            luceneConfig.getIndexDir().setIndexDirectory(buildIndexDir);
            logger.info(stopWatch.prettyPrint());
            logger.info("======== total index record count is "+counter+" ========");
            System.out.println("finish index building!");
        } catch (IOException e) {
            throw new TcmLuceneException("建索引时文件读写出错！ 索引文件夹:"+ luceneConfig.getIndexDir().getIndexBuildDirectory(), e);
        }
    }

    /**
     * @return Returns the analyzer.
     */
    public Analyzer getAnalyzer() {
        return analyzer;
    }

    /**
     * @param analyzer
     *            The analyzer to set.
     */
    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * @return Returns the luceneConfig.
     */
    public LuceneConfig getLuceneConfig() {
        return luceneConfig;
    }

    /**
     * @param luceneConfig
     *            The luceneConfig to set.
     */
    public void setLuceneConfig(LuceneConfig luceneConfig) {
        this.luceneConfig = luceneConfig;
    }

    /**
     * @return Returns the dbContentRetriever.
     */
    public DbContentRetriever getDbContentRetriever() {
        return dbContentRetriever;
    }

    /**
     * @param dbContentRetriever
     *            The dbContentRetriever to set.
     */
    public void setDbContentRetriever(DbContentRetriever dbContentRetriever) {
        this.dbContentRetriever = dbContentRetriever;
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

}
