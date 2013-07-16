/**
 * 
 */
package edu.zju.tcmsearch.web.controller.account;

import java.util.*;



import org.springframework.web.servlet.mvc.Controller; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller; 

import edu.zju.tcmsearch.util.web.SimplePager; 
import edu.zju.tcmsearch.dao.Expression;
import edu.zju.tcmsearch.dao.GroupExpression;
import edu.zju.tcmsearch.dao.Operator;
import edu.zju.tcmsearch.secure.domain.access.GrantedAuthorityConstant;
import edu.zju.tcmsearch.secure.domain.account.Account;
import edu.zju.tcmsearch.secure.service.account.IAccountManager;
import edu.zju.tcmsearch.util.web.AccountMessageMapper;
import edu.zju.tcmsearch.util.web.MultiLinguaUtil;
import edu.zju.tcmsearch.util.web.ParameterValueDecoder;

public class AccountResultsController implements Controller{
	
	private String successView;
	private IAccountManager accountManager;
	private int pageSize=5;
	
	
	 private static final Logger logger = Logger
     .getLogger(RegisterAffirmController.class);

	public IAccountManager getAccountManager() {
		return accountManager;
	}



	public void setAccountManager(IAccountManager accountManager) {
		this.accountManager = accountManager;
	}



	public String getSuccessView() {
		return successView;
	}



	public void setSuccessView(String successView) {
		this.successView = successView;
	}



	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Account> accountResults = null;
		String cmd = request.getParameter("cmd");
		if("query".equals(cmd)){
			accountResults = query(request);
		}else if(/*"next".equals(cmd)&&*/null!=request.getSession().getAttribute(ACCOUNT_RESULT_KEY_IN_SESSION)){
			accountResults =(List<Account>) request.getSession().getAttribute(ACCOUNT_RESULT_KEY_IN_SESSION);
		}else{
			accountResults = Collections.EMPTY_LIST;
		}
		
		SimplePager pager = getPager(accountResults);
		int pageID;
		try{
		    String pid = request.getParameter("pageID");
		    pageID = Integer.parseInt(pid);
		}catch(Exception e){
			pageID = pager.firstPage();
		}
		Map model=new HashMap();
		model.put("accountResults",pager.getPage(pageID));
		model.put("nextpage",pager.nextPage(pageID));
		model.put("priorpage",pager.priorPage(pageID));
		model.put("curpage",pageID);
		model.put("MessageMapper",new AccountMessageMapper());
		model.put("manager",accountManager);
		putAccountData(model,request);
		
		String ViewName = getSuccessView();
		return new ModelAndView(ViewName,model);
	}



	protected List<Account> query(HttpServletRequest request) {
		List<Account> accountResults;
		List<String> orderby = new ArrayList<String>();
		Account accountOnLine = (Account)request.getSession().getAttribute(Account.ACCOUNT_SAVE_IN_SESSION_1986_KEY);
		if(accountOnLine.isInGlobalGroup()){
			Expression expr = getExpression(request,orderby);
			accountResults = accountManager.getAccounts(expr,orderby);
		}else{
			Expression expr = getExpression(request,orderby);
			((GroupExpression)expr).addExpression(new Expression("groupid",Operator.Equal,accountOnLine.getGroupId()));
			accountResults = accountManager.getAccounts(expr,orderby);
		}
		request.getSession().removeAttribute(ACCOUNT_RESULT_KEY_IN_SESSION);
		request.getSession().setAttribute(ACCOUNT_RESULT_KEY_IN_SESSION,accountResults);
		return accountResults;
	}
        
	protected SimplePager getPager(List<Account> accounts){
		SimplePager pager = new SimplePager<Account>(accounts,this.pageSize,1);
		return pager;
	}
	
    protected void putAccountData(Map model,HttpServletRequest request){
    	Account account = getOnlineAccount(request);
    	if(null==account){
    		model.put("isLogin","false");
    	}else{
    	    model.put("id",account.getId());
    	    if(account.isIPLogin()){
    		   model.put("username",request.getRemoteAddr());
    	    }else{
    		   model.put("username",account.getUsername());
    	    }
    	    
    	    if(account.hasAuthority(GrantedAuthorityConstant.AUTHORITY_ADMINISTRATOR.toString())){
    	    	model.put("isLogin","admin");
    	    }else if(account.hasAuthority(GrantedAuthorityConstant.AUTHORITY_SERVENT.toString())){
    	    	model.put("isLogin","servent");
    	    }else{
    	    	model.put("isLogin","user");
    	    }
    	}
    	
    }	
	
	protected Account getOnlineAccount(HttpServletRequest request){
		return (Account)request.getSession().getAttribute(Account.ACCOUNT_SAVE_IN_SESSION_1986_KEY);
	}	
	
	protected Expression getExpression(HttpServletRequest request,List<String> orderBy){
		  orderBy.clear();
		  String[] order = request.getParameterValues("orderBy");
		  if(null!=order){
			  for(String o:order){
				  orderBy.add(o);
			  }
		  }
		  
		  GroupExpression g = new GroupExpression();
		  String value;
		  g.setOperator(Operator.And);
		  if((value =(String) request.getParameter("loginType"))!=null && !"".equals(value)){
			  g.addExpression(new Expression("loginType",Operator.Like,value));}
	      if((value =(String) request.getParameter("username"))!=null && !"".equals(value)){
	    	  g.addExpression(new Expression("username",Operator.Like,value));}
	      if((value =(String) request.getParameter("password"))!=null && !"".equals(value)){
	    	  g.addExpression(new Expression("password",Operator.Like,value));}
	     
	      if((value =(String) request.getParameter("status"))!=null && !"".equals(value)){
	    	  g.addExpression(new Expression("status",Operator.Like,value));}
	      if((value =(String) request.getParameter("ipaddress"))!=null && !"".equals(value)){
	    	  g.addExpression(new Expression("ipaddress",Operator.Like,value));}

	      
	      if((value =(String) request.getParameter("chargetype"))!=null && !"".equals(value)){
	    	  g.addExpression(new Expression("chargetype",Operator.Like,value));}
	      if((value =(String) request.getParameter("startdate"))!=null && !"".equals(value)){
	    	  g.addExpression(new Expression("startdate",Operator.Like,value));}
	      if((value =(String) request.getParameter("totaldate"))!=null && !"".equals(value)){
	    	  g.addExpression(new Expression("totaldate",Operator.Like,value));}
	      if((value =(String) request.getParameter("totalflow"))!=null && !"".equals(value)){
	    	  g.addExpression(new Expression("totalflow",Operator.Like,value));}
	      if((value =(String) request.getParameter("usedflow"))!=null && !"".equals(value)){
	    	  g.addExpression(new Expression("usedflow",Operator.Like,value));}
	      
	      if((value =(String) request.getParameter("groupid"))!=null && !"".equals(value)){
	    	  g.addExpression(new Expression("groupid",Operator.Like,value));}
	      
	      value =(String) request.getParameter("contact_name");
//	      value = null==value||"".equals(value) ? null:ParameterValueDecoder.decode(value);
	      if(null!=value && !"".equals(value)){
	    	  g.addExpression(new Expression("contact_name",Operator.Like,value));
	      }
	      
	      String[] vArray =request.getParameterValues("event");
	      if(vArray!=null ){
	    	  for(String v:vArray){
	    		  g.addExpression(new Expression("event",Operator.Like,v));
	    	  }
	      }
	      return g;
	}
	
	final static String ACCOUNT_RESULT_KEY_IN_SESSION = "ACCOUNT_RESULT_KEY_IN_SESSION";

}
