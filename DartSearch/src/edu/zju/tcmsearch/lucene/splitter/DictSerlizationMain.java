/*
 * Created on 2005-12-31
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.splitter;

import org.springframework.util.StopWatch;

import edu.zju.tcmsearch.common.AppContextFactory;

public class DictSerlizationMain {
    private static DictService getDictService(){
        DictService dictService= (DictService)AppContextFactory.getLuceneContext().getBean("dictService");
        
        return dictService;
    }
    
    private static  DictTree getDictTree(){
        DictTree dictTree= (DictTree)AppContextFactory.getLuceneContext().getBean("dictTree");
        return dictTree;
    }
    

    /**
     * @param args
     */
    public static void main(String[] args) {
//        System.out.println("start serialize Dict!");
//        StopWatch stopWatch=new StopWatch("Save Dict ");
//        stopWatch.start("serialize Dict to individual files!");
//        getDictService().saveAllNodeToFile();
//        stopWatch.stop();
//        System.out.println("finish serialize Dict!");
//        System.out.println(stopWatch.prettyPrint());
        
        System.out.println("start serialize Dict!");
//        StopWatch stopWatch=new StopWatch("Save Dict to one file!");
//        stopWatch.start("serialize Dict to individual files!");        
        getDictTree().saveDictMap();
 //       stopWatch.stop();
        System.out.println("finish serialize Dict!");
 //       System.out.println(stopWatch.prettyPrint());        
    }

}
