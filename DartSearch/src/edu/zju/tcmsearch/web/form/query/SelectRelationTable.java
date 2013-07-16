package edu.zju.tcmsearch.web.form.query;

import java.util.List;
import java.util.ArrayList;
import cn.edu.zju.dart.core.semanticregistry.IRelation2Ontology;

/*
 * 保存用户选择的数据来源，在SearchconditonForm中做为交互界面
 * 一个本体对应一张SelectRelationTable
 */

public class SelectRelationTable {
	private IRelation2Ontology[]  selectRelation;
	/*
	 * selectSet布尔集用于标记选中的表
	 */
	private String[] selectSet; 
	private List<String> relationName=new ArrayList<String>();
	
	
	public IRelation2Ontology[] getSelectRelation(){
		return  selectRelation;
	}

	public void  setSelectRelation(IRelation2Ontology[]  selectRelation){
		this.selectRelation=selectRelation;
		selectSet=new String[this.selectRelation.length];
		for(int i=0;i<selectSet.length;i++){
			selectSet[i]=",true";
			relationName.add(this.selectRelation[i].getRelation().getRelationName());
		}
	}
	
	public List<IRelation2Ontology> getSelectRelationResult(){
        List<IRelation2Ontology> result= new ArrayList<IRelation2Ontology>();
		for(int i=0;i<selectSet.length;i++)
			if(null!=selectSet[i] && ",true".compareTo(selectSet[i])==0){
				result.add(selectRelation[i]);
			}
		return result;
	}

	
	public String[] getSelectSet(){
		return selectSet;
	}
	
	public List<String> getRelationName(){
		return relationName;
	}
	
}
