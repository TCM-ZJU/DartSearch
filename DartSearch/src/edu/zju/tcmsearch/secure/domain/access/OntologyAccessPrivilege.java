package edu.zju.tcmsearch.secure.domain.access;

public class OntologyAccessPrivilege {

	private boolean isCategory=false;
	private String ontolgoyURI="";
	private boolean readPriv=false;
	private boolean wantReadPriv=false;
	private int accountId;
	
	public OntologyAccessPrivilege(){
		
	}
	
	public OntologyAccessPrivilege(int id,String uri,boolean read,boolean cate){
		this.accountId   = id;
		this.ontolgoyURI = uri;
		this.readPriv    = read;
		this.isCategory  = cate;
	}
	
	public void setOntoloygURI(String uri){
		this.ontolgoyURI =  uri;
	}
	
	public void setWantReadPriv(boolean priv){
		this.wantReadPriv = priv;
	}
	
	public void setReadPriv(boolean priv){
		this.readPriv = priv;
	}
	
	public void setCategory(boolean isCate){
		this.isCategory = isCate;
	}
	
	public void setAccountId(int id){
		this.accountId = id;
	}
	
	public String getOntoloygURI(){
		return this.ontolgoyURI;
	}
	
	public boolean isWantReadPriv(){
		return this.wantReadPriv;
	}
	
	public boolean isCategory(){
		return this.isCategory;
	}
	
	public boolean isReadPriv(){
		return this.readPriv;
	}
	
	public int getAccountId(){
		return this.accountId;
	}
	
	public boolean equals(Object obj){
		if(obj instanceof OntologyAccessPrivilege){
			OntologyAccessPrivilege priv = (OntologyAccessPrivilege) obj;
			return priv.accountId == this.accountId  && priv.ontolgoyURI.equals(this.ontolgoyURI);
		}else{
			return false;
		}
	}
}
