package edu.zju.tcmsearch.web.controller.account;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.acegisecurity.AuthenticationException;
import net.sf.acegisecurity.ui.AbstractProcessingFilter;
import net.sf.acegisecurity.ui.webapp.AuthenticationProcessingFilter;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import edu.zju.tcmsearch.util.web.MultiLinguaUtil;


public class AcegiLoginController implements Controller{
	private String successView;

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session=request.getSession();
		String login_error=request.getParameter("login_error");
		Map model=new HashMap();
		if (null!=login_error){
		   String errorMessage=((AuthenticationException) session.getAttribute(AbstractProcessingFilter.ACEGI_SECURITY_LAST_EXCEPTION_KEY)).getMessage();
		   String lastUserName=(String) session.getAttribute(AuthenticationProcessingFilter.ACEGI_SECURITY_LAST_USERNAME_KEY);
		   model.put("login_error",login_error);
		   model.put("errorMessage",errorMessage);
		   model.put("lastUserName",lastUserName);
		}			
		
		String ViewName = getSuccessView();
		return new ModelAndView(ViewName,model);
	}

}
