package edu.zju.tcmsearch.web.controller.account;

import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import edu.zju.tcmsearch.secure.domain.access.GrantedAuthorityConstant;
import edu.zju.tcmsearch.secure.domain.account.Account;
import edu.zju.tcmsearch.secure.domain.account.UserInfo;
import edu.zju.tcmsearch.secure.service.account.IAccountManager;
import edu.zju.tcmsearch.secure.service.account.UserInfoManager;
import edu.zju.tcmsearch.util.web.MultiLinguaUtil;
import edu.zju.tcmsearch.web.form.secure.AccountEditable;

public class AccountEditOnSelfController extends SimpleFormController {
	
	protected static Logger logger = Logger.getLogger(AccountEditController.class);
	
	private IAccountManager accountManager;
	
	private UserInfoManager userInfoManager;
	
	public void setAccountManager(IAccountManager manager){
		this.accountManager = manager;
	}

	public void setUserInfoManager(UserInfoManager userInfoManager){
		this.userInfoManager = userInfoManager;
	}
	
	protected  Map referenceData(HttpServletRequest request){
		Account account = accountManager.getAccountById(getAccountId(request));
		UserInfo userInfo = userInfoManager.userInfoLoadById(getAccountId(request));
		if(userInfo==null)
			userInfo = new UserInfo();
		
		account.setUserInfo(userInfo);
		request.getSession().setAttribute(ACCOUNTEDITOR_SAVE_ACCOUNT_KEY,account);
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("accountPre",account);
	    model.put("accEdit",getAccountEditable(request));
		return model;
	}
	
	protected int getAccountId(HttpServletRequest request){
		Account acc=(Account) request.getSession().getAttribute(Account.ACCOUNT_SAVE_IN_SESSION_1986_KEY);
		return acc.getId();
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> model = new HashMap<String,Object>();
				
		Account account = (Account)request.getSession().getAttribute(ACCOUNTEDITOR_SAVE_ACCOUNT_KEY);
		Account accountParameter = (Account)command;
		if(null!=account){
			request.getSession().removeAttribute(ACCOUNTEDITOR_SAVE_ACCOUNT_KEY);
			getAccountEditable(request).copy(accountParameter,account);
		    String msg = account.validate();
		    if(msg.equals("ACCOUNT_VALID")){
			  UserInfo userInfo = account.getUserInfo();
			  userInfoManager.delete(userInfo);
			  userInfoManager.userInfoInsert(userInfo);
			  account.checkFlow();
			  account.checkTimeOut();
			  accountManager.update(account);
			  model.put("ErrMsg","ERR_NONE");
		   }else{
		      model.put("ErrMsg",msg);
		   }
		   model.put("accountId",account.getId());
		}else{
		   model.put("ErrMsg","ERR_SESSION_TIME_OUT");
		}
		
		String ViewName = getSuccessView();
		return new ModelAndView(ViewName,model);
	}	
	
	private AccountEditable getAccountEditable(HttpServletRequest request){
		Account accountOnLine = (Account)request.getSession().getAttribute(Account.ACCOUNT_SAVE_IN_SESSION_1986_KEY);		
        if(null!=accountOnLine&&accountOnLine.getId()==getAccountId(request)){
			return AccountEditable.Customer;
        }else{
        	return AccountEditable.Stranger;
        }

	}
	protected final String ACCOUNTEDITOR_SAVE_ACCOUNT_KEY = "ACCOUNTEDITOR_SAVE_ACCOUNT_KEY";
}
