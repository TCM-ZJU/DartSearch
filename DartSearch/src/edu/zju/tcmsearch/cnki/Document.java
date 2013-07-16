package edu.zju.tcmsearch.cnki;

import java.io.InputStream;

public class Document {
	public String title;
	public String fileUrl;		
	public String absUrl;
	public String contentType;
	public String html;
    public byte[] content;
    public InputStream is;
    public int contentLength;
    
    public String getNormalTitle(){
    	StringBuilder sb= new StringBuilder();
    	int ip = 0;
    	int iStart = 0;
    	int iEnd = 0;
    	do{
    		iStart = title.indexOf('<',ip);
    		iEnd = title.indexOf('>',ip);
    		if(iStart>ip){
    			sb.append(title.substring(ip,iStart));
    		}else if(iStart==-1){
    			sb.append(title.substring(ip));
    		}
    		ip = iEnd==-1 ? -1:iEnd+1;
    	}while(ip>=0);
    	return sb.toString();
    }
    
    public String getAbsUrl(String host){
    	if(null==absUrl)
    		return null;
    	return absUrl.replace("..",host);    	
    }
    
    public String getTitle(){
    	return title;
    }
    
    public String getFileUrl(String host){
    	if(null==fileUrl)
    		return null;
    	return fileUrl.replace("..",host);
    }
    
	public String toString(){
		return "标题："+title+"  文件下载："+fileUrl+"   摘要："+absUrl;
	}
}
