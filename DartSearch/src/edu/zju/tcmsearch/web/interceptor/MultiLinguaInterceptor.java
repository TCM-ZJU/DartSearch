package edu.zju.tcmsearch.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import org.apache.log4j.Logger;

public class MultiLinguaInterceptor implements HandlerInterceptor {

	public static Logger logger = Logger.getLogger(MultiLinguaInterceptor.class);
	
	public static String MULTILINGUA_PREFIX_KEY ="MULTILINGUA_PREFIX_KEY"; 
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse reponse,
			Object handler) throws Exception {
        if(null==request.getSession().getAttribute(MULTILINGUA_PREFIX_KEY)){
        	Locale locale =localeResolver.resolveLocale(request);
        	request.getSession().setAttribute(MULTILINGUA_PREFIX_KEY,getPrefix(locale));
        }
        if(null!=realResolver){
        	String prefix =(String)request.getSession().getAttribute(MULTILINGUA_PREFIX_KEY);
        	if(!prefix.equals(currentPrefix)){
        		realResolver.clearCache();
        		realResolver.setPrefix(prefix+old_resolver_prefix);
        		currentPrefix = prefix;
        		logger.debug("[multilingua] reset prefix="+currentPrefix);
        	}
        	
        }		
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse reponse,
			Object handler, ModelAndView model) throws Exception {

	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exec)
			throws Exception {

	}
	
	public String getPrefix(Locale locale){
		if(locale==null)
			return defaultPrefix;
		String prefix = Locale2Url.get(locale.getLanguage()+"_"+locale.getCountry());
		if(null==prefix)
			   prefix = Locale2Url.get(locale.getLanguage());		
		return null==prefix ? defaultPrefix:prefix;
	}
	
	public String getPrefix(String country){
		return Locale2Url.containsKey(country)? Locale2Url.get(country):defaultPrefix;
	}
	
	public MultiLinguaInterceptor(){
		Locale2Url.put("zh","/zh");
		Locale2Url.put("en","/en");		
		Locale2Url.put("zht","/zht/");
		Locale2Url.put("zh_TW","/zht/");
	}
	
	public void setRealResolver(VelocityViewResolver resolver){
		this.realResolver = resolver;
	}

	public void setResolverPrefix(String prefix){
		this.old_resolver_prefix = prefix;
	}
	
	private String currentPrefix="";	
	private String defaultPrefix="/default";
	private String old_resolver_prefix="";
	private Map<String,String> Locale2Url = new HashMap<String,String>();	
	private AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
	private VelocityViewResolver realResolver;
	
	private String XMLContext="";
	public void setXMLContext(String xc){
		this.XMLContext = xc;
	}


}
