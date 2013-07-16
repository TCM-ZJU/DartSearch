package edu.zju.tcmsearch.cnki;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class DownloadFileConnection extends SmartHttpConnection{
	static final Logger logger = Logger.getLogger(DownloadFileConnection.class);
	private Document doc;
	private InputStream is;
	private boolean isContentFetched = false;
	
	public DownloadFileConnection(Document doc){
		this.doc = doc;
	}


	public byte[] getFileRawData() throws IOException{
		if(!isContentFetched){
			isContentFetched = true;
			return doc.content=getFileRawDataInternal(true);
		}
		else{
			return doc.content;
		}
	}
	
	public InputStream getFileRawDataAsStream() throws IOException{
		if(null==is){
			getFileRawDataInternal(false);
			return is;
		}
		throw new IOException("InputStream already retrieved !");
	}

	protected byte[] getFileRawDataInternal(boolean retrieveDataInArray) throws IOException{
		HttpInfomation info = new HttpInfomation();
		HttpURLConnection.setFollowRedirects(false);
		login(info);
        
		String downloadUrl = doc.getFileUrl(ServerInfomation.HostUrl+"/kns50/");
		URL url1 = new URL(downloadUrl);
		HttpURLConnection con1 =(HttpURLConnection) url1.openConnection();
		setCookie(con1,info.cookie,null);
		HttpInfomation info1 = doGet(con1,retrieveDataInArray);
		
		HttpInfomation info11 = info1;
		while(info11.isRedirect()){
			String reUrl = info11.getRedirectUrl();
			if(!reUrl.startsWith("http://")){
				reUrl = ServerInfomation.HostUrl + reUrl;
			}
			URL url11 = new URL(reUrl);
			HttpURLConnection con11 =(HttpURLConnection) url11.openConnection();
            setCookie(con11,info.cookie,null);
            info11=doGet(con11,retrieveDataInArray);
            info.cookie.putAll(info11.cookie);
            con11.disconnect();
            logger.debug("第二次连接 ◎ 跳转－"+url11+"－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");			
		}

		doc.contentType = info11.contentType;
        doc.content = info11.binaryContent;
        doc.contentLength = info11.contentLength;
        is= doc.is = info11.is;
		return doc.content;
	}

	public void writeToConnection(HttpServletResponse response) throws IOException{
		byte[] data = getFileRawData();
		String subfix = "."+doc.contentType.substring(doc.contentType.lastIndexOf("/")+1);
		String filename = URLEncoder.encode(doc.getNormalTitle()+subfix,"utf-8");
		response.addHeader("Content-Disposition","attachment; filename="+filename);
		response.addHeader("Content-LENGTH",""+data.length);
		response.addHeader("Content-Type",doc.contentType);
		OutputStream out = response.getOutputStream();
		out.write(data);
		out.close();
	}
	
	public void flowToConnection(HttpServletResponse response) throws IOException{
		getFileRawDataAsStream();
		
		String subfix = "."+doc.contentType.substring(doc.contentType.lastIndexOf("/")+1);
		String filename = URLEncoder.encode(doc.getNormalTitle()+subfix,"utf-8");
		response.addHeader("Content-Disposition","attachment; filename="+filename);
		response.addHeader("Content-LENGTH",""+doc.contentLength);
		response.addHeader("Content-Type",doc.contentType);
		OutputStream out = response.getOutputStream();
		byte[] buf= new byte[1024000];
		int cnt;
		do{
			cnt=is.read(buf);
			if(cnt>=0){
				out.write(buf,0,cnt);
			}
		}while(cnt>=0);
		is.close();
		out.close();
	}
	protected HttpInfomation login(HttpInfomation info) throws MalformedURLException, IOException {
		/**
		 * 连接首页，获取ASP.NET_SessionId
		 */
		URL url = new URL(ServerInfomation.HostUrl+"/kns50/index.aspx");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        HttpInfomation info1 = doGet(con,true);
        info.cookie.putAll(info1.cookie);
        logger.debug("第一次连接 －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
        con.disconnect();

        String _VIEWSTATE = subString(info1.content,"name=\"__VIEWSTATE\" value=\"","\"");        
        /**
         * 登陆
         */
        URL url2 = new URL(ServerInfomation.HostUrl+"/kns50/chkd_index.aspx");
        HttpURLConnection con2 =(HttpURLConnection) url2.openConnection();
        info.addCookie("RememberUserSelect","NOW");
        setCookie(con2,info.cookie,null);
        Map<String,String> vnp = new HashMap<String,String>();
        vnp.put("username",ServerInfomation.Username);
        vnp.put("password",ServerInfomation.Password);
        vnp.put("__VIEWSTATE",_VIEWSTATE);
        vnp.put("loginsubmit.x","36");
        vnp.put("loginsubmit.y","6");
        HttpInfomation info2=doPost(con2,vnp,null,false,true);
        info.cookie.putAll(info2.cookie);
        con2.disconnect();
        logger.debug("第二次连接 －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
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
		return info22;
	}
	
	final static String HostUrl  = ServerInfomation.HostUrl; 
	final static String Username = ServerInfomation.Username;
	final static String Password = ServerInfomation.Password;


	
}
