package edu.zju.tcmsearch.web.controller.account;

import java.util.Date;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.acegisecurity.GrantedAuthority;
import net.sf.acegisecurity.GrantedAuthorityImpl;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import edu.zju.tcmsearch.secure.domain.account.Account;
import edu.zju.tcmsearch.secure.service.account.IAccountManager;
import edu.zju.tcmsearch.util.web.AccountMessageMapper;

/**
 * @author 倪亦柯 et ming
 *
 */
public class RegisterController extends SimpleFormController{

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		// TODO Auto-generated method stub
		/*
		Account newAccount = accountManager.createAccountObject();
		Account account = (Account)command;
		account.setStatus("ACCOUNT_STATUS_INACTIVATED");
		account.setId(newAccount.getId());
		request.getSession().setAttribute("account",account);
		*/
		Account newAccount = accountManager.createAccountObject();
		Account account = (Account)command;
		account.setStatus(Account.ACCOUNT_STATUS_NORAML);
		account.setMaxConcurrence(1);
		account.setGrantedAuthority(new GrantedAuthority[]{new GrantedAuthorityImpl("ROLE_USER")});
		account.setId(newAccount.getId());
		account.setNewUser();
		account.setNewOntology();
		account.setNewTable();
		request.getSession().setAttribute("account",account);		

		Map<String,Object> model = new HashMap<String,Object>();
		model.put("account",account);
		model.put("MessageMapper",new AccountMessageMapper());
		return new ModelAndView(getSuccessView(),model);
	};

	protected  Map referenceData(HttpServletRequest request, Object command, Errors errors){
		Date today = new Date();		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("today",today.toLocaleString().split(" ")[0]);
		model.put("ACCOUNT_ID_CHECKER",null==request.getParameter("ACCOUNT_ID_CHECKER")? "":request.getParameter("ACCOUNT_ID_CHECKER"));
		model.put("ACCOUNT_ID",null==request.getParameter("ACCOUNT_ID")? "":request.getParameter("ACCOUNT_ID"));
		return model;
	}
	
	private IAccountManager accountManager;
	
	public void setAccountManager(IAccountManager accountManager){
		this.accountManager = accountManager;
	}
	

}
