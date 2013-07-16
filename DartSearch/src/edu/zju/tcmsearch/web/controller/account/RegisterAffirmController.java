/**
 * 
 */
package edu.zju.tcmsearch.web.controller.account;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import edu.zju.tcmsearch.secure.domain.account.Account;
import edu.zju.tcmsearch.secure.domain.account.UserInfo;
import edu.zju.tcmsearch.secure.service.account.IAccountManager;
import edu.zju.tcmsearch.secure.service.account.UserInfoManager;

/**
 * @author 倪亦柯
 *
 */
public class RegisterAffirmController extends SimpleFormController{
	
	private IAccountManager accountManager;
	private UserInfoManager userInfoManager;
	
	 private static final Logger logger = Logger
     .getLogger(RegisterAffirmController.class);

	public IAccountManager getAccountManager() {
		return accountManager;
	}


	public void setAccountManager(IAccountManager accountManager) {
		this.accountManager = accountManager;
	}


	public UserInfoManager getUserInfoManager() {
		return userInfoManager;
	}


	public void setUserInfoManager(UserInfoManager userInfoManager) {
		this.userInfoManager = userInfoManager;
	}


	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		// TODO Auto-generated method stub
		Account account = (Account)request.getSession().getAttribute("account");
		Map<String,String> model = new HashMap<String,String>();
		String msg = account.validate();
		if(msg.equals("ACCOUNT_VALID")){
		   if(null!=accountManager.createInDB(account))
		   {
			  accountManager.updateAuthority(account);
			  UserInfo userInfo = account.getUserInfo();
			  userInfo.setId(account.getId());
			  userInfoManager.userInfoInsert(userInfo);
			  model.put("ErrMsg","ERR_NONE");
			  
			  model.put("AccountId",account.getId()+"");
			  //this.login(account,request);
		   }else{
			  model.put("ErrMsg","ERR_DUPLICATED_ACCOUNT");
		   }
		}else{
		   model.put("ErrMsg",msg);
		}
		return new ModelAndView(getSuccessView(),model);
	}
	
	public void login(Account account,HttpServletRequest request){
		accountManager.login(account,request.getSession());
		request.getSession().setAttribute(ACCOUNTEDITOR_SAVE_ACCOUNT_KEY,account);
	}
	
	protected final String ACCOUNTEDITOR_SAVE_ACCOUNT_KEY = "ACCOUNTEDITOR_SAVE_ACCOUNT_KEY";
	
}

