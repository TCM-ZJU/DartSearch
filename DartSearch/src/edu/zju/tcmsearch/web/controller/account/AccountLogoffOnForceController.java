package edu.zju.tcmsearch.web.controller.account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import edu.zju.tcmsearch.secure.domain.account.Account;
import edu.zju.tcmsearch.secure.service.account.IAccountManager;

public class AccountLogoffOnForceController implements Controller {

	private IAccountManager manager;
	
	public void setAccountManager(IAccountManager manager){
		this.manager = manager;
	}
	public ModelAndView handleRequest(HttpServletRequest request,HttpServletResponse reponse) throws Exception {
		// TODO Auto-generated method stub
		String accIdStr =(String)request.getParameter("accountId");
		int id = Integer.parseInt(accIdStr);
		Account account = manager.getAccountById(id);
		manager.logOff(account);
		View backView=new RedirectView(request.getContextPath()+"/secure/accountResults.luc");
		return new ModelAndView(backView);		
	}

}
