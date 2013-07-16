package edu.zju.tcmsearch.cnki;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpInfomation {
	public Map<String,String> cookie = new HashMap<String,String>();
	public StringBuilder content;
	public String contentType;
	public byte[] binaryContent;
	public int contentLength;
	public int responseCode=0;

	public InputStream is;
	public boolean isRedirect(){
		return responseCode>=300&&responseCode<400;
	}
	
	public void addCookie(String name,String value){
		cookie.put(name,value);
	}
	
	public String getRedirectUrl(){
		if(isRedirect()&&null!=content){
			int iStart = content.indexOf("a href='")+"a href='".length();
	        int iEnd = content.indexOf("'",iStart);
	        return iEnd-iStart>0 ? content.substring(iStart,iEnd).replace("&amp;","&") : null;
		}
		return null;
	}
	
	public boolean hasCookie(String[] cookieNames){
		if(null!=cookieNames){
			boolean ok = true;
			for(String ckn:cookieNames){
				ok = ok && cookie.containsKey(ckn);
			}
		}
		return true;
	}
	public boolean isHtml(){
		return contentType!=null && contentType.lastIndexOf("text/html")!=-1;
	}
	public final static String POST = "POST";
	public final static String GET  = "GET";

}
