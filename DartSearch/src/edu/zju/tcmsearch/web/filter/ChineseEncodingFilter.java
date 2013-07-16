package edu.zju.tcmsearch.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class ChineseEncodingFilter implements Filter  {

	      private static final Logger logger = Logger.getLogger(ChineseEncodingFilter.class);
	      
		  private FilterConfig filterConfig= null;
		  
		  private List<String> patterns = new ArrayList<String>();
		  
		  public static final String CHINESE_ENCODING_KEY = "J_CHARACTER_TYPE";
	
		  public void init(FilterConfig filterConfig)   throws ServletException {
		    this.filterConfig= filterConfig;
		  }
		  
		  public void destroy() {
		    this.filterConfig = null; 		      
		   }
		  
		  public void doFilter(ServletRequest request,   ServletResponse response, FilterChain chain)
		    throws IOException, ServletException {
		   
			  HttpServletRequest httpRequest = (HttpServletRequest) request;
			  HttpServletResponse httpResponse = (HttpServletResponse) response;
			  
			  boolean isValid = true;
			  
			  String urlStr = httpRequest.getRequestURI();
			  
			  
			  
			  //J_CHARACTER_TYPE的值为空 或不等于"TRADITIONAL_CHINESE"都按照简体来处理;	 

			    if(httpRequest.getSession().getAttribute(CHINESE_ENCODING_KEY)!=null)			 			 
				    if( httpRequest.getSession().getAttribute(CHINESE_ENCODING_KEY).toString().compareTo("TRADITIONAL_CHINESE")==0 )
				  	  {
					    isValid=false;
				  	  }			 
			 
			    if(urlStr.indexOf("www.cintcm.com/cgi-bin/bigate.cgi")!=-1) 
			       {
				  	  isValid=true;
			       }

			  
			  if(isValid)
			  {
				  chain.doFilter(request, response);
			  }
			  else
			  {
				  urlStr="http://www.cintcm.com/cgi-bin/bigate.cgi/b/k/k/http@"+httpRequest.getServerName()+":"+httpRequest.getServerPort()+urlStr;
				  logger.debug("Redirect URL:"+urlStr);
				  httpResponse.sendRedirect(urlStr);
			  } 		   
		  }		
		  
		  public void setFilterPatterns(List<String> patterns){
			     this.patterns = patterns;
		  }
		  
		  protected boolean isFilterURL(String url){
			     PathMatcher matcher = new AntPathMatcher();
			     for(String pattern: patterns){
			    	 if(matcher.match(pattern,url))
			    		 return true;
			     }
			     return false;
		  }
		  
}
