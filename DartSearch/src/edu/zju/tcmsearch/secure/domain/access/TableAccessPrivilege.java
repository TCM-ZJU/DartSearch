package edu.zju.tcmsearch.secure.domain.access;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class TableAccessPrivilege {
	private int  accountId;

	private String tableOwner;

	private String tableName;

	private boolean readPriv;

	private boolean wantReadPriv;

	public boolean isWantReadPriv(){
		return wantReadPriv;
	}

	public void setWantReadPriv(boolean wantReadPriv){
		this.wantReadPriv = wantReadPriv;
	}
	
	public boolean isReadPriv() {
		return readPriv;
	}

	public void setReadPriv(boolean readPriv) {
		this.readPriv = readPriv;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int id) {
		this.accountId = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableOwner() {
		return tableOwner;
	}

	public void setTableOwner(String tableOwner) {
		this.tableOwner = tableOwner;
	}


	public String getTableIdentity(){
		return getTableOwner()+"."+getTableName();
	}
	
	public boolean equals(Object obj){
		if(obj instanceof TableAccessPrivilege){
			TableAccessPrivilege tobj = (TableAccessPrivilege) obj;
			return tobj.accountId==this.accountId && 
			       tobj.readPriv==this.readPriv &&
			       tobj.tableName.equals(this.tableName) &&
			       tobj.tableOwner.equals(this.tableOwner);
		}
		return false;
	}
}
