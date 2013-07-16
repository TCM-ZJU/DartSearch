/*
 * Created on 2005-12-21
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import edu.zju.tcmsearch.common.AppContextFactory;
import edu.zju.tcmsearch.lucene.index.IndexBuilder;

public class IndexBuildJob implements Job{
    /**
     * @return Returns the indexBuilder.
     */
    public IndexBuilder getIndexBuilder() {
        IndexBuilder indexBuilder= (IndexBuilder)AppContextFactory.getLuceneScheduleContext().getBean("indexBuilder");
        return indexBuilder;
    }


    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        System.out.print("start building Index!");
        getIndexBuilder().buildIndex();
        System.out.print("finish building Index!");
    }


}
