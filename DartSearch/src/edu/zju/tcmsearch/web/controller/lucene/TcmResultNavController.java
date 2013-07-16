/*
 * Created on 2005-12-20
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.web.controller.lucene;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import edu.zju.tcmsearch.lucene.search.SearchResults;
import edu.zju.tcmsearch.secure.TableNameLocalization;
import edu.zju.tcmsearch.util.GBK2Big5.GB2Big5;
import edu.zju.tcmsearch.util.web.SearchResultUtil;

public class TcmResultNavController extends AbstractController{
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(TcmResultNavController.class);

    private String successView;
    
    
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


    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SearchResults searchResults=(SearchResults)request.getSession().getAttribute("searchResults");
        if (null==searchResults){
            logger.info("Can not retrieve searchResults from HttpSession. thus return to first page!");
            return new ModelAndView("simpleSearch");
        }
        String sPageId=request.getParameter("pageId");
        Integer pageId=Integer.parseInt(sPageId);
        searchResults.setCurPageId(pageId);
        Map model=new HashMap();
        model.put("searchResults",searchResults);
        model.put("helper",new SearchResultUtil());
        model.put("GB2Big5",GB2Big5.getInstance());
        model.put("chineseTableName",tableNameLocal);
        //model.put("pageId",pageId);
        return new ModelAndView(getSuccessView(),model);
    }

	private TableNameLocalization tableNameLocal;
	
	public void setTableNameLocalization(TableNameLocalization tableNameLocal){
		this.tableNameLocal = tableNameLocal;
	}


}
