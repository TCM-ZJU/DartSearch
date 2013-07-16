package edu.zju.tcmsearch.web.controller.lucene;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import edu.zju.tcmsearch.lucene.search.QueryStorer;
import edu.zju.tcmsearch.lucene.search.SearchResults;
import edu.zju.tcmsearch.lucene.search.Searcher;
import edu.zju.tcmsearch.lucene.search.impl.SimpleQueryStorer;
import edu.zju.tcmsearch.secure.TableNameLocalization;
import edu.zju.tcmsearch.util.GBK2Big5.GB2Big5;
import edu.zju.tcmsearch.util.web.SearchResultUtil;

import org.apache.log4j.Logger;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class SecondLinkSearchController implements Controller{
	
	private static Logger logger = Logger.getLogger(SecondLinkSearchController.class);
    private String successView;
    private Searcher searcher;
    

    /**
     * @return Returns the searcher.
     */
    public Searcher getSearcher() {
        return searcher;
    }



    /**
     * @param searcher The searcher to set.
     */
    public void setSearcher(Searcher searcher) {
        this.searcher = searcher;
    }



    /**
     * @return Returns the successView.
     */
    public String getSuccessView() {
        return successView;
    }



    /**
     * @param successView The successView to set.
     */
    public void setSuccessView(String successView) {
        this.successView = successView;
    }



    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String ontoName=request.getParameter("ontoName");
        QueryStorer queryStorer=getQueryStorer(request.getSession());
        logger.debug("ontoName ="+ontoName);
        logger.debug("queryExpression ="+queryStorer.getQueryExpression());
        if (null==ontoName || null==queryStorer.getQueryExpression()){
        	//有待处理
            return new ModelAndView(getSuccessView());
        }
        SearchResults searchResults=getSearcher().search(queryStorer.getQueryExpression(),ontoName,queryStorer);
        Map model=new HashMap();
        searchResults.setCurPageId(1);
        model.put("searchResults",searchResults);
        model.put("helper",new SearchResultUtil());
        model.put("GB2Big5",GB2Big5.getInstance());
        model.put("chineseTableName",tableNameLocal);
        return new ModelAndView(getSuccessView(),model);         
    }

    private QueryStorer getQueryStorer(HttpSession session){
        QueryStorer queryStorer=(QueryStorer)session.getAttribute("queryStorer");
        if (null==queryStorer){
           queryStorer=new SimpleQueryStorer();
           session.setAttribute("queryStorer",queryStorer);
        }
        return queryStorer;
    }
    
	private TableNameLocalization tableNameLocal;
	
	public void setTableNameLocalization(TableNameLocalization tableNameLocal){
		this.tableNameLocal = tableNameLocal;
	}    
}
