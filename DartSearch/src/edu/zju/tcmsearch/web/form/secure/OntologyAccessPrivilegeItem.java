package edu.zju.tcmsearch.web.form.secure;
import edu.zju.tcmsearch.secure.domain.access.OntologyAccessPrivilege;

public class OntologyAccessPrivilegeItem {
	public static String TRUE =",true";
	public static String FALSE = "";

	private int  accountId =0;
	private String ontologyURI ="";
	private String readPriv = FALSE;
	private String wantReadPriv = FALSE;
	private String isCategory = FALSE;
	
	public String getOntologyURI(){
		return this.ontologyURI;
	}
	
	public String getReadPriv(){
		return this.readPriv;
	}
	
	public void setReadPriv(String priv){
		this.readPriv = priv;
	}
	
	public boolean noneReadPrivilege(){
		return this.readPriv.equals(TRUE)==false;
	}
	
	public String getWantReadPriv(){
		return this.wantReadPriv;
	}
	
	public void setWantReadPriv(String priv){
		this.wantReadPriv = priv;
	}
	
	public boolean noneWantReadPrivilege(){
		return this.wantReadPriv.equals(TRUE)==false;
	}	
	
	public int getAccountId(){
		return this.accountId;
	}
	
	public OntologyAccessPrivilegeItem(OntologyAccessPrivilege priv){
		this.accountId = priv.getAccountId();
		this.readPriv = priv.isReadPriv()? TRUE:FALSE;
		this.isCategory = priv.isCategory() ? TRUE:FALSE;
		this.wantReadPriv = priv.isWantReadPriv() ? TRUE:FALSE;
		this.ontologyURI = priv.getOntoloygURI();
	}
	
	public OntologyAccessPrivilegeItem(String ontoURI,int accountId){
		this.ontologyURI = ontoURI;
		this.accountId = accountId;
	}
	
	public OntologyAccessPrivilege getOntologyAccessPrivilege(){
		OntologyAccessPrivilege priv = new OntologyAccessPrivilege();
		priv.setAccountId(this.accountId);
		priv.setOntoloygURI(this.ontologyURI);
		priv.setCategory(this.isCategory.equals(TRUE));
		priv.setReadPriv(this.readPriv.equals(TRUE));
		priv.setWantReadPriv(this.wantReadPriv.equals(TRUE));
		return priv;
	}

	public boolean equals(Object obj){
		OntologyAccessPrivilegeItem item = (OntologyAccessPrivilegeItem) obj;
		return this.accountId==item.getAccountId() && this.ontologyURI.equals(item.getOntologyURI());
	}
	
	public void setOntologyURI(String id){
		this.ontologyURI = id;
	}
	
	public void setAccountId(int id){
		this.accountId = id;
	}
	

}
