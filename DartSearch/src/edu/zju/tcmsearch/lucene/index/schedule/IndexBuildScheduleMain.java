/*
 * Created on 2005-12-21
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.schedule;

import org.quartz.CronTrigger;

import edu.zju.tcmsearch.common.AppContextFactory;

public class IndexBuildScheduleMain {

    /**
     * @param args
     */
    public static void main(String[] args) {
        CronTrigger trigger=(CronTrigger) AppContextFactory.getLuceneScheduleContext().getBean("cronTrigger");
        System.out.println(" the index job will start as follows "+trigger.getCronExpression()); 
    }

}
