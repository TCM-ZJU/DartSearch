package edu.zju.tcmsearch.util.web;

import javax.servlet.http.HttpServletRequest;

public class GB2Big5Checker {
	public static boolean check(HttpServletRequest request){
		String key =(String) request.getSession().getAttribute(MULTILINGUA_PREFIX_KEY);
		if(null!=key && "/zht".equals(key))
			return true;
		return false;
	}
	public static String MULTILINGUA_PREFIX_KEY ="MULTILINGUA_PREFIX_KEY";

}
