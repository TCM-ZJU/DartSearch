/*
 * Created on 2005-12-19
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.web.controller.lucene;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import edu.zju.tcmsearch.lucene.index.DbInfoRetriever;
import edu.zju.tcmsearch.lucene.search.QueryStorer;
import edu.zju.tcmsearch.lucene.search.SearchResults;
import edu.zju.tcmsearch.lucene.search.Searcher;
import edu.zju.tcmsearch.lucene.search.impl.SimpleQueryStorer;
import edu.zju.tcmsearch.secure.TableNameLocalization;
import edu.zju.tcmsearch.util.GBK2Big5.GB2Big5;
import edu.zju.tcmsearch.util.dartcore.OntoUriParseUtil;
import edu.zju.tcmsearch.util.web.SearchResultUtil;
import edu.zju.tcmsearch.web.form.lucene.SimpleSearchForm;

public abstract class AbstractSearchController extends SimpleFormController{
    private Searcher searcher;
    private DbInfoRetriever dbInfoRetriever;
    Map ontoOptions;
   
    protected Map getOntoOptions(){
        if (null!=ontoOptions){
            return ontoOptions;
        }
        TreeMap temp = new TreeMap(); 
        List<String> ontoUriList=dbInfoRetriever.getAllOntoUris();
       // ontoOptions.put("empty","不限定");
        for (String ontoUri:ontoUriList){
        	temp.put(OntoUriParseUtil.getOntoName(ontoUri),OntoUriParseUtil.getOntoName(ontoUri));
        }
        ontoOptions = temp;//??
        return ontoOptions;
    }


    /**
     * @return Returns the dbInfoRetriever.
     */
    public DbInfoRetriever getDbInfoRetriever() {
        return dbInfoRetriever;
    }

    /**
     * @param dbInfoRetriever The dbInfoRetriever to set.
     */
    public void setDbInfoRetriever(DbInfoRetriever dbInfoRetriever) {
        this.dbInfoRetriever = dbInfoRetriever;
    }

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

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map model=new HashMap();
        putRefData(model); 
        putRefData(model,request);
        return model;
    }


    protected void putRefData(Map model,HttpServletRequest request){
    	
    }


    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
    	
        getQueryStorer(request.getSession()).freeVolatile();
        SearchResults searchResults = doSearch(request, command);
        if (null==searchResults){
        	return new ModelAndView("emptyQuery");
        	
        }
        request.getSession().setAttribute("searchResults",searchResults);
        Map model=new HashMap();
        searchResults.setCurPageId(1);
        model.put("searchResults",searchResults);
        model.put("helper",new SearchResultUtil());
        model.put("GB2Big5",GB2Big5.getInstance());
        model.put("chineseTableName",tableNameLocal);
        //model.put("pageId",1);
        return new ModelAndView(getSuccessView(),model);
    }



    
    protected QueryStorer getQueryStorer(HttpSession session){
        QueryStorer queryStorer=(QueryStorer)session.getAttribute("queryStorer");
        if (null==queryStorer){
           queryStorer=new SimpleQueryStorer();
           session.setAttribute("queryStorer",queryStorer);
        }
        return queryStorer;
    }
    
    protected abstract SearchResults doSearch(HttpServletRequest request, Object command);
    
    protected abstract  void putRefData(Map model);

	private TableNameLocalization tableNameLocal;
	
	public void setTableNameLocalization(TableNameLocalization tableNameLocal){
		this.tableNameLocal = tableNameLocal;
	}    
}
