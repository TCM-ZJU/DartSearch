package edu.zju.tcmsearch.web.form.secure;
import java.util.List;
import java.util.ArrayList;


import cn.edu.zju.dart.core.utils.DartCoreSettings;
import cn.edu.zju.dart.core.semanticregistry.IRelation2Ontology;

import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.common.service.OntoService;
import edu.zju.tcmsearch.secure.domain.access.OntologyAccessPrivilege;

public class OntologyAccessAssigmentForm {

	private OntoService ontoService;
	
	private OntologyAccessPrivilegeItem[] privItems;
	
	private int accountId;
	
	public OntologyAccessAssigmentForm(OntoService service){
		this.ontoService = service;
	}
	
	public void initialize(List<OntologyAccessPrivilege> privileges,int accountId){
		List<OntologyAccessPrivilegeItem> hasPriv = new ArrayList<OntologyAccessPrivilegeItem>();
		for(OntologyAccessPrivilege pItem:privileges){
			hasPriv.add(new OntologyAccessPrivilegeItem(pItem));
		}
		
		OntologyAccessPrivilegeItem item = null;
		for(DartOntology ontology:ontoService.getBaseOntologies()){
			if(item==null){
				item = new OntologyAccessPrivilegeItem(ontology.getIdentity(),accountId);
			}else{
				item.setAccountId(accountId);
				item.setOntologyURI(ontology.getIdentity());
			}
			if(!hasPriv.contains(item)){
				hasPriv.add(item);
				item = null;
			}
		}
		privItems = new OntologyAccessPrivilegeItem[hasPriv.size()];
		hasPriv.toArray(privItems);
		this.accountId = accountId;
	}
	
	public int getAccountId(){
		return accountId;
	}
	
	public OntologyAccessPrivilegeItem[] getPrivItems(){
		return privItems;
	}
	
	public int getItemCount(){
		return null==privItems ? 0:privItems.length;
	}
	
	public List<OntologyAccessPrivilege> getPrivilege(){
		List<OntologyAccessPrivilege> privSet = new ArrayList<OntologyAccessPrivilege>();
		for(OntologyAccessPrivilegeItem item:privItems){
			if(!item.noneReadPrivilege()||!item.noneWantReadPrivilege()){
				privSet.add(item.getOntologyAccessPrivilege());
			}
		}
		return privSet;
	}
}
