/*
 * Created on 2005-12-21
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index;

import edu.zju.tcmsearch.common.AppContextFactory;

/**
 * 构建索引的main类
 * @author ddream
 *
 */
public class IndexBuilderMain {
    
    private static IndexBuilder getIndexBuilder(){
        IndexBuilder indexBuilder= (IndexBuilder)AppContextFactory.getLuceneContext().getBean("indexBuilder");
        return indexBuilder;
    }
    

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.print("start building Index!");
        getIndexBuilder().buildIndex();
        System.out.print("finish building Index!");
    }

}
