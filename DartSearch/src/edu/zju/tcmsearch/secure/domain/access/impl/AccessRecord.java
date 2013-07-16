package edu.zju.tcmsearch.secure.domain.access.impl;
import java.util.Date;

import edu.zju.tcmsearch.secure.domain.access.IAccessRecord;
public class AccessRecord implements IAccessRecord{
	
	private  Date accessDate;
	private  int accountId;
	private  String accessUrl;
	private  String content;

	public AccessRecord(){}
	
	public Date getAccessDate(){
		return this.accessDate;
	}
	
	public int getAccountId(){
		return this.accountId;
	}
	
	public String getAccessUrl(){
		return this.accessUrl;
	}
	
	public String getContent(){
		return this.content;
	}
	

	public void setAccessDate(Date date){
		this.accessDate=date;
	}
	
	public void setAccountId(int id){
		this.accountId=id;
	}
	
	public void setAccessUrl(String url){
		this.accessUrl=url;
	}
	
	public void setContent(String content){
		this.content=content;
	}
	
}
