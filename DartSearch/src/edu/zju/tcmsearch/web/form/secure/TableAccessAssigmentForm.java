package edu.zju.tcmsearch.web.form.secure;

import java.util.List;
import java.util.ArrayList;

import cn.edu.zju.dart.core.utils.DartCoreSettings;
import cn.edu.zju.dart.core.semanticregistry.IRelation2Ontology;

import edu.zju.tcmsearch.common.service.OntoService;
import edu.zju.tcmsearch.secure.domain.access.TableAccessPrivilege;

public class TableAccessAssigmentForm {

	private TableAccessPrivilegeItem[] privItems ;
	
	private OntoService ontoService;
	
	private int accountId;
	
	public TableAccessAssigmentForm(OntoService ontoService){
		this.ontoService = ontoService;
	}
	/**
	 * generate privilege items of all the tables 
	 * @param privileges - privileges read from database store
	 * @param accountId
	 */
	public void initialize(List<TableAccessPrivilege> privileges,int accountId){
		List<TableAccessPrivilegeItem> hasPrivItems = new ArrayList<TableAccessPrivilegeItem>();
		
		for(TableAccessPrivilege pri:privileges){
			hasPrivItems.add(new TableAccessPrivilegeItem(pri));
		}
		
		DartCoreSettings dcSetting = ontoService.getDartCoreSettings();
		IRelation2Ontology[] allSemReg = dcSetting.getDartSemanticRegistry().getAllSemReg();
		TableAccessPrivilegeItem privItemReuse = null;
		for(int i=0;i<allSemReg.length;i++){
			IRelation2Ontology r2o = allSemReg[i];
			String tableName = r2o.getRelation().getRelationName();
			String tableSpace = r2o.getRelation().getRelationSpace().getRelationSpaceName();
			if(null==privItemReuse)
			{
				privItemReuse = new TableAccessPrivilegeItem(accountId,tableSpace+"."+tableName);
			}
			else
			{
				privItemReuse.setTableIdentity(tableSpace+"."+tableName);
				privItemReuse.setAccountId(accountId);
			}
			if(!hasPrivItems.contains(privItemReuse)){
				hasPrivItems.add(privItemReuse);
				privItemReuse = null;
			}
		}

		privItems = new TableAccessPrivilegeItem[hasPrivItems.size()];
		hasPrivItems.toArray(privItems);
		this.accountId = accountId;
	}
	
	/**
	 * get none-empty privileges
	 * @return
	 */
	public List<TableAccessPrivilege> getTableAccessPrivilege(){
		List<TableAccessPrivilege> privileges = new ArrayList<TableAccessPrivilege>();
		for(TableAccessPrivilegeItem item:privItems){
			if(!item.noneReadPrivilege()||!item.noneWantReadPrivilege())
				privileges.add(item.getPrivilege());
		}
		return privileges;
	}
	
	public TableAccessPrivilegeItem[] getPrivItems(){
		return privItems;
	}
	
	public int getAccountId(){
		return accountId;
	}
	
	public int getItemCount(){
		return null==privItems ? 0:privItems.length;
	}	
}
