package edu.zju.tcmsearch.web.form.secure;

import edu.zju.tcmsearch.secure.domain.access.TableAccessPrivilege;

public class TableAccessPrivilegeItem {
	
	public static String TRUE =",true";
	public static String FALSE = "";

	private int  accountId =0;
	private String tableIdentity ="";
	private String readPriv = FALSE;
	private String wantReadPriv = FALSE;
	
	public TableAccessPrivilegeItem(TableAccessPrivilege pri){
		accountId = pri.getAccountId();
		tableIdentity = pri.getTableIdentity();
		readPriv = pri.isReadPriv() ? TRUE:FALSE;
		wantReadPriv = pri.isWantReadPriv() ? TRUE:FALSE;
	}

	public TableAccessPrivilegeItem(int accountId,String tableId){
		this.accountId = accountId; 
		this.tableIdentity = tableId;
	}
	
	public TableAccessPrivilege getPrivilege(){
		TableAccessPrivilege pri = new TableAccessPrivilege();
		pri.setAccountId(accountId);
		pri.setReadPriv(readPriv.equals(TRUE));
		pri.setWantReadPriv(wantReadPriv.equals(TRUE));
		String[] parts = tableIdentity.split("\\."); 
		if(parts.length==1){
			pri.setTableName(parts[0]);
			pri.setTableOwner("");
		}else{
			pri.setTableName(parts[1]);
			pri.setTableOwner(parts[0]);
		}
		return pri;
	}
	
	public boolean equals(Object obj){
		if(obj instanceof TableAccessPrivilegeItem){
			TableAccessPrivilegeItem tobj = (TableAccessPrivilegeItem) obj;
			return tobj.accountId==this.accountId && 
			       tobj.tableIdentity.equals(this.tableIdentity);
		}
		return false;
	}	
	
	public boolean noneReadPrivilege(){
		return 	!readPriv.equals(TRUE);
	}
	
	public String getReadPriv(){
		return this.readPriv;
	}
	
	public void setReadPriv(String priv){
		this.readPriv = priv;
	}
	
	public boolean noneWantReadPrivilege(){
		return 	!wantReadPriv.equals(TRUE);
	}
	
	public String getWantReadPriv(){
		return this.wantReadPriv;
	}
	
	public void setWantReadPriv(String priv){
		this.wantReadPriv = priv;
	}
	
	public String getTableIdentity(){
		return tableIdentity ;
	}
	
	public void setTableIdentity(String tid){
		this.tableIdentity = tid;
	}
	
	public void setAccountId(int id){
		this.accountId = id;
	}
}
