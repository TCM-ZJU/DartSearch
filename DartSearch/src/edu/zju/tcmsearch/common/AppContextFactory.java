/*
 * Created on 2005-12-21
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.common;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppContextFactory {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(AppContextFactory.class);

    private static final String[] luceneConfigFiles = {
            "applicationContext.xml",
            "dataAccessContext-local.xml",
            "luceneApplicationContext.xml", 
            "ontologyThemeContext.xml",
       };
    
    private static final String[] luceneScheduleFiles = {
        "applicationContext.xml",
        "dataAccessContext-local.xml",
        "luceneApplicationContext.xml", 
        "luceneScheduleContext.xml",
        "ontologyThemeContext.xml",
    };
    
    private static ApplicationContext applicationContext= new ClassPathXmlApplicationContext(
            luceneConfigFiles);
    
    private static ApplicationContext luceneScheduleContext=new ClassPathXmlApplicationContext(
            luceneScheduleFiles);
    
    public static final ApplicationContext getLuceneContext() {        
        return applicationContext;
    }
    
    public static final ApplicationContext getLuceneScheduleContext() {
        return luceneScheduleContext;
    }    
}
