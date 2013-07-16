package edu.zju.tcmsearch.web.controller.lucene;

import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import edu.zju.tcmsearch.lucene.search.QueryStorer;
import edu.zju.tcmsearch.lucene.search.SearchResults;
import edu.zju.tcmsearch.picGenerator.PicGenerator;
import edu.zju.tcmsearch.picGenerator.PictureInfo;
import edu.zju.tcmsearch.secure.domain.access.GrantedAuthorityConstant;
import edu.zju.tcmsearch.secure.domain.account.Account;
import edu.zju.tcmsearch.web.form.lucene.SimpleSearchForm;

import javax.servlet.http.HttpServletRequest;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 
 * modified by zhm
 * 2008-04-30
 * 增加对相关图片的搜索
 */
public class SimpleSearchController extends AbstractSearchController{
	protected SearchResults doSearch(HttpServletRequest request, Object command) {
		SimpleSearchForm simpleSearchForm=(SimpleSearchForm)command;
        if (null==simpleSearchForm.getQueryExperssion() || "".equals(simpleSearchForm.getQueryExperssion())){
            return null;
        }
        QueryStorer queryStorer=getQueryStorer(request.getSession());
        SearchResults searchResults=getSearcher().search(simpleSearchForm.getQueryExperssion(),simpleSearchForm.getOntologyName(),queryStorer);
		
      
        searchResults.setPictureList(new PicGenerator().picRetrival(simpleSearchForm.getQueryExperssion()));
        
        
        return searchResults;
	}
	
	protected void putRefData(Map model) {
		model.put("ontoOptions",getOntoOptions());
	}
	  protected void putRefData(Map model,HttpServletRequest request){
    	Account account = getOnlineAccount(request);
    	if(null==account){
    		model.put("isLogin","false");
    	}else{
    	    model.put("id",account.getId());
    	    model.put("runOutFee",account.isToRunOutFee()?"yes":"no");
    	    if(account.isIPLogin()){
    		   model.put("username",request.getRemoteAddr());
    	    }else{
    		   model.put("username",account.getUsername());
    	    }
    	    if(account.hasAuthority(GrantedAuthorityConstant.AUTHORITY_ADMINISTRATOR.toString())||
    	       account.hasAuthority(GrantedAuthorityConstant.AUTHORITY_SERVENT.toString())){
    	    	model.put("isLogin","admin");
    	    }else{
    	    	model.put("isLogin","user");
    	    }
    	}    	
    }	
	
	protected Account getOnlineAccount(HttpServletRequest request){
		return (Account)request.getSession().getAttribute(Account.ACCOUNT_SAVE_IN_SESSION_1986_KEY);
	}
}
