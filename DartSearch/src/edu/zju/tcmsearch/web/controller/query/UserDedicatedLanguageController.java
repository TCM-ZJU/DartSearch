package edu.zju.tcmsearch.web.controller.query;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class UserDedicatedLanguageController implements Controller {

	public static String MULTILINGUA_PREFIX_KEY ="MULTILINGUA_PREFIX_KEY"; 	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String language = request.getParameter("language");
		if(null!=language && Locale2Url.containsKey(language)){
			request.getSession().setAttribute(MULTILINGUA_PREFIX_KEY,Locale2Url.get(language));
		}
        RedirectView rv = new RedirectView(request.getContextPath()+"/"+redirectView);
		return new ModelAndView(rv);
	}
	
	public UserDedicatedLanguageController(){
		Locale2Url.put("zh","/zh");
		Locale2Url.put("zht","/zht");
		Locale2Url.put("en","/en");	
	}

	public void setRedirectView(String rv){
		this.redirectView = rv;
	}
	private String redirectView="";
	private Map<String,String> Locale2Url = new HashMap<String,String>();

}
