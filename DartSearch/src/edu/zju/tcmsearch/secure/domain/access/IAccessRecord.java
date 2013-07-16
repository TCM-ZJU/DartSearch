package edu.zju.tcmsearch.secure.domain.access;

import java.util.Date;

public interface IAccessRecord {
	public Date getAccessDate();
	
	public int getAccountId();
	
	public String getAccessUrl();
	
	public String getContent();

}
