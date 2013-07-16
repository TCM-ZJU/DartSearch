package edu.zju.tcmsearch.web.controller.account;

import javax.servlet.http.HttpServletRequest;

import net.sf.acegisecurity.GrantedAuthority;
import net.sf.acegisecurity.GrantedAuthorityImpl;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller; 

import java.util.List;

import edu.zju.tcmsearch.secure.domain.account.Account;
import edu.zju.tcmsearch.secure.domain.account.UserInfo;
import edu.zju.tcmsearch.secure.service.account.IAccountManager;
import edu.zju.tcmsearch.secure.service.account.UserInfoManager;
import edu.zju.tcmsearch.util.web.AccountMessageMapper;
import edu.zju.tcmsearch.util.web.MultiLinguaUtil;

public class UserInfoDetailController implements Controller{
	
	private String successView;
	private String submitView;
	private IAccountManager accountManager;
	private UserInfoManager userInfoManager;
	
	private static final String ROLE_USER = "ROLE_USER";
	private static final String ROLE_TECH_ADMIN = "ROLE_TECH_ADMIN";
	private static final String ROLE_CUSTOMER_ADMIN = "ROLE_CUSTOMER_ADMIN";
	
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

	public String getSubmitView(){
		return submitView;
	}

	public void setSubmitView(String view){
		this.submitView = view;
	}
	
	public UserInfoManager getUserInfoManager() {
		return userInfoManager;
	}

	public void setUserInfoManager(UserInfoManager userInfoManager) {
		this.userInfoManager = userInfoManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String cmd = request.getParameter("CMD_WORD");
		if(null==cmd){
			return showForm(request);
		}else {
			return submitForm(request);
		}
	}

	private ModelAndView showForm(HttpServletRequest request) {
		String idString =request.getParameter("userid");
		Integer idInteger = new Integer(idString);
		int idInt = idInteger.intValue();
		
		Account accountSave = accountManager.getAccountById(idInt);	 
		UserInfo userInfo = userInfoManager.userInfoLoadById(idInt);
		request.getSession().setAttribute(this.ACCOUNT_SAVE_IN_SESSION_KEY,accountSave);
		
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("userInfo",userInfo);
		model.put("account",accountSave);
		model.put("MessageMapper",new AccountMessageMapper());
		
		String ViewName = getSuccessView();
		return new ModelAndView(ViewName,model);
	}
	
//	private ModelAndView showForm(HttpServletRequest request) {
//		String username = request.getParameter("username");
//		String idString =request.getParameter("userid");
//		//Account account = request.getParameter("account");
//		logger.debug("idString"+idString );
//		List<Account> accounts = accountManager.getAllAccounts();
//	   
//		//logger.debug("account_name"+account.getUsername());
//		Integer idInteger = new Integer(idString);
//		int idInt = idInteger.intValue();
//		Account accountWant = new Account();
//		 for(int i=0;i<accounts.size();i++){
//			    Account account = accounts.get(i);
//		    	if(idInt == account.getId())
//		    		accountWant = account;
//		 }
//		 
//		accountSave = accountWant;		 
//		UserInfo userInfo = userInfoManager.userInfoLoadById(idInt);
//		
//		logger.debug("username"+username);
//		Map model = new HashMap();
//		model.put("username",username);
//		model.put("userInfo",userInfo);
//		model.put("account",accountWant);
//		model.put("MessageMapper",new AccountMessageMapper());
//		
//		return new ModelAndView(getSuccessView(),model);
//	}

	private ModelAndView submitForm(HttpServletRequest request){
		String[] authorities = request.getParameterValues("J_GRANT_AUTHORITES");
		if(authorities!=null && authorities.length >0 ){
			GrantedAuthority[] GA = new GrantedAuthority[authorities.length];
			for(int i=0;i<authorities.length;i++){
				GA[i] = new GrantedAuthorityImpl(authorities[i]);
			}
			Account accountSave = (Account)request.getSession().getAttribute(this.ACCOUNT_SAVE_IN_SESSION_KEY);
			accountSave.setGrantedAuthority(GA);
			accountSave.setStatus(Account.ACCOUNT_STATUS_NORAML);
			accountManager.update(accountSave);
			accountManager.updateAuthority(accountSave);
		}
		return new ModelAndView("redirect:"+getSubmitView());
	}

	protected static final String ACCOUNT_SAVE_IN_SESSION_KEY = "ACCOUNT_SAVE_IN_SESSION_KEY";
}
