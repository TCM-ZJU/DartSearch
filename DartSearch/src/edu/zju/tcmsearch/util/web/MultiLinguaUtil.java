package edu.zju.tcmsearch.util.web;

import javax.servlet.http.HttpServletRequest;

public class MultiLinguaUtil {
	public static String MULTILINGUA_PREFIX_KEY ="MULTILINGUA_PREFIX_KEY"; 
	public static String default_prefix="/default/";
	public static String getPrefixInSession(HttpServletRequest request){
		String prefix =(String)request.getSession().getAttribute(MULTILINGUA_PREFIX_KEY);
		return null==prefix ? default_prefix:prefix;
	}
	
	public static void setPrefixInSession(String prefix,HttpServletRequest request){
		request.getSession().setAttribute(MULTILINGUA_PREFIX_KEY,prefix);
	}
}
