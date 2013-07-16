package edu.zju.tcmsearch.secure.domain.account;

public class AccountDetial {
    private int id;
    private String uniqueName;
    
	public int getAccountId(){
		return id;
	}
	
	public String getUniqueName(){
		return uniqueName;
	}
	
	public AccountDetial(int id, String uniqueName){
		this.id = id;
		this.uniqueName = uniqueName;
	}
}
