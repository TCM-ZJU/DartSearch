package edu.zju.tcmsearch.web.ajax;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

import edu.zju.tcmsearch.web.filter.ChineseEncodingFilter;

import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;
/*
 * Get access to HttpSession through Ajax , mainly used in JavaScript 
 */
public class Session{
	private static Set<String> allowedNames; 
	static {
		allowedNames = new HashSet<String>();
		allowedNames.add("Main_ShowCategory");
		allowedNames.add("SearchResult_RowNo");
		allowedNames.add("SearchResult_PageNo");
		allowedNames.add(ChineseEncodingFilter.CHINESE_ENCODING_KEY);
	}
	

	public static String putString(String name,String value)
	{
		if(!allowedNames.contains(name))
			return "null";
		
		WebContext ctx = WebContextFactory.get();
		HttpSession session = ctx.getHttpServletRequest().getSession();
		session.setAttribute(name,value);
		return value;
	}

	public static String getString(String name)
	{
		if(!allowedNames.contains(name))
			return "null";
		
		WebContext ctx = WebContextFactory.get();
		HttpSession session = ctx.getHttpServletRequest().getSession();
		Object value = session.getAttribute(name);
		if(value!=null && value instanceof String)
		   return (String)value;
		return "null";
	}

}