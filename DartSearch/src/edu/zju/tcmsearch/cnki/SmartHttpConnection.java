package edu.zju.tcmsearch.cnki;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class SmartHttpConnection {
	static Logger logger = Logger.getLogger(SmartHttpConnection.class);
	
	private String cookie="";
	
	public String getResultContent(String value) throws IOException{
		HttpInfomation info = new HttpInfomation();
		HttpURLConnection.setFollowRedirects(false);
		/**
		 * 连接首页，获取ASP.NET_SessionId
		 */
		URL url = new URL(ServerInfomation.HostUrl+"/kns50/index.aspx");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        HttpInfomation info1 = doGet(con,true);
        info.cookie.putAll(info1.cookie);
        logger.debug("第一次连接  取session－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
        //logger.debug(info1.content);
        con.disconnect();

        String _VIEWSTATE = subString(info1.content,"name=\"__VIEWSTATE\" value=\"","\"");        
        /**
         * 登陆
         */
        URL url2 = new URL(ServerInfomation.HostUrl+"/kns50/chkd_index.aspx");
        HttpURLConnection con2 =(HttpURLConnection) url2.openConnection();
        info.addCookie("RecordID","cnki%3ACHKD%u671F%u520A%u5168%u6587%u6570%u636E%u5E93%2CCHKD%u535A%u7855%u58EB%u5B66%u4F4D%u8BBA%u6587%u5168%u6587%u6570%u636E%u5E93%2CCHKD%u4F1A%u8BAE%u8BBA%u6587%u5168%u6587%u6570%u636E%u5E93%2CCHKD%u62A5%u7EB8%u5168%u6587%u6570%u636E%u5E93");
        setCookie(con2,info.cookie,null);
        Map<String,String> vnp = new HashMap<String,String>();
        vnp.put("username",ServerInfomation.Username);
        vnp.put("password",ServerInfomation.Password);
        vnp.put("__VIEWSTATE",_VIEWSTATE);
        vnp.put("loginsubmit.x","32");
        vnp.put("loginsubmit.y","4");
        HttpInfomation info2=doPost(con2,vnp,null,false,true);
        info.cookie.putAll(info2.cookie);
        con2.disconnect();
        logger.debug("第二次连接 登陆 －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
        //logger.debug(info2.content);
        
        HttpInfomation info22 = info2;
        while(info22.isRedirect()){
            URL url22 = new URL(ServerInfomation.HostUrl+info22.getRedirectUrl());
            HttpURLConnection con22 =(HttpURLConnection) url22.openConnection();
            setCookie(con22,info.cookie,null);
            info22=doGet(con22,true);
            info.cookie.putAll(info22.cookie);
            con22.disconnect();
            logger.debug("第二次连接 ◎ 跳转－"+url22+"－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
            //logger.debug(info22.content);        	
        }
        /**
         * 查询
         *
         */
        URL url3 = new URL(ServerInfomation.HostUrl+"/kns50/scdbsearch/cdbSearch.aspx");
        HttpURLConnection con3 =(HttpURLConnection) url3.openConnection();
        info.addCookie("kuakuSearchSelectionID","0");
        setCookie(con3,info.cookie,null);
        Map<String,String> vnpq = utils.parseNameValueString(ServerInfomation.getQueryPattern(value));
        HttpInfomation info3= doPost(con3,vnpq,null,true,true);
        info.cookie.putAll(info3.cookie);
        con3.disconnect();
        logger.debug("第三次连接 查询－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
        logger.debug(info3.content);
        HttpInfomation info33 = info3;
        while(info33.isRedirect()){
        	URL url33 = new URL(ServerInfomation.HostUrl+info33.getRedirectUrl());
        	HttpURLConnection con33=(HttpURLConnection) url33.openConnection();
        	setCookie(con33,info.cookie,null);
        	info33 = doGet(con33,true);
        	info.cookie.putAll(info33.cookie);
        	con33.disconnect();
        	logger.debug("第三次连接 ◎ 跳转－"+url33+"－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
        }
        logger.debug(info33.content);
        return info33.content.toString();
	}	
	
	public void close() throws IOException{
		URL url = new URL(ServerInfomation.HostUrl+"/kns50/logout.aspx?q=1");
		HttpURLConnection con =(HttpURLConnection) url.openConnection();
		doGet(con,true);
	}
	
	public HttpInfomation doGet(HttpURLConnection con,boolean retrieveDataInArray) throws IOException{
		HttpInfomation info = new HttpInfomation();
		con.setRequestMethod(HttpInfomation.GET);
		con.connect();
		info.contentType = con.getContentType();
		info.contentLength = con.getContentLength();
		if(info.isHtml()){
			fetchContent(con,info);
		}else if(retrieveDataInArray){
			getBinaryContent(con, info);
		}else{
			info.is = con.getInputStream();
		}
		
		reserveCookie(con, info);
		info.responseCode = con.getResponseCode();
		return info;
	}

	protected void getBinaryContent(HttpURLConnection con, HttpInfomation info) throws IOException {
		byte[] buf = new byte[info.contentLength];
		InputStream is = con.getInputStream();
		int ip = 0;
		int cnt = 0;
		do{
			cnt=is.read(buf,ip,info.contentLength-ip);
			ip+=cnt;
		}while(cnt>=0);
		info.binaryContent = buf;
		is.close();
	}
	
	public HttpInfomation doPost(HttpURLConnection con, Map<String,String> vnPair,String text,boolean isTextEncoded,boolean retrieveDataInArray) throws IOException{
		HttpInfomation info = new HttpInfomation();
		con.setRequestMethod(HttpInfomation.POST);
		con.setDoOutput(true);
		con.connect();
		
		java.io.PrintWriter out = new PrintWriter(con.getOutputStream());
		if(null!=vnPair){
			boolean first=true;
			for(Map.Entry<String,String> pair:vnPair.entrySet()){
				if(first) first=false;
				else out.print("&");
				String name = pair.getKey();
				String value = pair.getValue();
				out.print(name);
				out.print("=");
				out.print(URLEncoder.encode(value,"UTF-8"));
			}			
		}
		if(null!=text){
			out.print(isTextEncoded ? URLEncoder.encode(text,"UTF-8"):text);
		}

		out.close();
		
		info.contentType = con.getContentType();
		info.contentLength = con.getContentLength();
		if(info.isHtml()){
			fetchContent(con,info);
		}else if(retrieveDataInArray){
			getBinaryContent(con, info);
		}else{
			info.is = con.getInputStream();
		}
		
		reserveCookie(con, info);		
		info.responseCode = con.getResponseCode();
		return info;
	}

	protected void fetchContent(HttpURLConnection con,HttpInfomation info) throws IOException {
		Scanner in;
		StringBuilder response = new StringBuilder();
		try{
			in = new Scanner(con.getInputStream(),"UTF-8");
		}catch(IOException e){
			InputStream err = con.getErrorStream();
			if(err == null) throw e;
			in = new Scanner(err);
		}
		while(in.hasNextLine()){
			response.append(in.nextLine());
			response.append("\n");
		}
		in.close();
		info.content = response;
	}

	protected void reserveCookie(HttpURLConnection con, HttpInfomation info) {
		Map<String,List<String>> map = con.getHeaderFields();
	    if(map.containsKey(ServerInfomation.Set_Cookie)){
	    	for(String t: map.get(ServerInfomation.Set_Cookie)){
	  		  for(String s:t.split("; ")){
	  			logger.debug("Set-Cookie:"+s);
	  			String[] pair = s.split("=");
	  			info.cookie.put(pair[0],pair.length>1 ? pair[1]:null);
	  		  }
	  		}		
	    }
	}
	
	protected boolean setCookie(HttpURLConnection con,Map<String,String> cookieValue,String[] cookieNames){
		boolean setEveryCookie = true;
		StringBuilder sb= null;
		if(null!=cookieNames){
			boolean first = true;
			for(String ckn:cookieNames){
				if(!cookieValue.containsKey(ckn)){
					setEveryCookie = false;
					continue;
				}
				if(first){
					sb = new StringBuilder();
					sb.append(ckn).append("=").append(cookieValue.get(ckn));
					first = false;
				}else{
					sb.append("; ").append(ckn).append("=").append(cookieValue.get(ckn));
				}
			}
		}else{
			boolean first = true;
			for(Map.Entry entry:cookieValue.entrySet()){
				if(first){
					sb = new StringBuilder();
					sb.append(entry.getKey()).append("=").append(entry.getValue());
					first = false;
				}else{
					sb.append("; ").append(entry.getKey()).append("=").append(entry.getValue());
				}
			}
		}
		
		if(null!=sb){
			con.setRequestProperty(ServerInfomation.Cookie,sb.toString());
		}
		return setEveryCookie;
	}
	

	
	public String subString(StringBuilder sb,String prefix,String postfix){
		int iStart = sb.indexOf(prefix)+prefix.length();
		if(iStart>=0){
			int iEnd = sb.indexOf(postfix,iStart);
			return iStart<iEnd? sb.substring(iStart,iEnd):null;
		}
		return null;
	}
	
}
