package edu.zju.tcmsearch.web.controller.account;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import edu.zju.tcmsearch.secure.service.account.IAccountManager;

public class AccountExistContoller implements Controller {

	public final String ACCOUNT_ID_NEW ="ACCOUNT_ID_NEW";
	public final String ACCOUNT_ID_EXIST = "ACCOUNT_ID_EXIST";
	public final String ACCOUNT_ID_INVAILD ="ACCOUNT_ID_INVAILD";
	public final String ACCOUNT_ID_TYPE_IP ="ACCOUNT_ID_TYPE_IP";
	public final String ACCOUNT_ID_TYPE_USER ="ACCOUNT_ID_TYPE_USER";
	public final String ACCOUNT_ID_CHECKER = "ACCOUNT_ID_CHECKER";
	public final String ACCOUNT_ID ="ACCOUNT_ID";
	private String rdView;
	private String paramName = ACCOUNT_ID_CHECKER;
	private IAccountManager manager;
		
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse arg1) throws Exception {
		String type = request.getParameter("AccountType");
		String id = request.getParameter("AccountID");
		String url = request.getContextPath()+"/"+rdView+"?"+this.ACCOUNT_ID_CHECKER+"=";
		if(null==type || null==id){
			url = url + this.ACCOUNT_ID_INVAILD;
		}else if(ACCOUNT_ID_TYPE_IP.compareTo(type)==0){
			url = url + (manager.isExistIP(id.toUpperCase()) ? this.ACCOUNT_ID_EXIST : this.ACCOUNT_ID_NEW);
		}else if(ACCOUNT_ID_TYPE_USER.compareTo(type)==0){
			url = url + (manager.isExistUser(id.toUpperCase()) ? this.ACCOUNT_ID_EXIST : this.ACCOUNT_ID_NEW);
		}else 
			url = url + this.ACCOUNT_ID_INVAILD;
		
		url = url + "&" + this.ACCOUNT_ID+"="+(null==id ? "":id);
		RedirectView rdv = new RedirectView(url);
		return new ModelAndView(rdv);
		/*
		Map<String,String> model = new HashMap<String,String>();
		model.put("ACCOUNT_ID_CHECKER",url);
		return new ModelAndView(rdView,model);
		*/
	}


	public void setParameterName(String name){
		this.paramName = name;
	}
	public void setRedirectView(String view){
		this.rdView = view;
	}
	public void setAccountManager(IAccountManager manager){
		this.manager = manager;
	}	
}
