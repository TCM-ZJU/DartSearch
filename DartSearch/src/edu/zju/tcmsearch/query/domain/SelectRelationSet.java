package edu.zju.tcmsearch.query.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

import edu.zju.tcmsearch.web.form.query.SelectRelationTable;
import cn.edu.zju.dart.core.semanticregistry.IRelation2Ontology;
import cn.edu.zju.dart.core.query.util.ISemregSelector;
/*
 * 记录用户在一次查询过程中选择的所有数据来源表
 * 即所有本体的数据来源
 */

public class SelectRelationSet implements ISemregSelector{
	
    private List<IRelation2Ontology> selectRelation=new ArrayList<IRelation2Ontology>();
    /*
     * 添加一个hashtable，用于检测重复的数据表名
     */
    private HashSet<String>  hashSelectRelation=new HashSet<String>();
    
    
	public void addRelation(SelectRelationTable relation){
		
		if(relation==null)	
			return;
		
		List<IRelation2Ontology> addonRelation=relation.getSelectRelationResult();
        for( IRelation2Ontology r2o:addonRelation){
        	if(true== hashSelectRelation.add(r2o.getRelation().getRelationName())){
        		selectRelation.add(r2o);
        	}
        }
	}
	
	
	public List<IRelation2Ontology> getSelectRelation(){	
		return this.selectRelation;
	}

	public List<IRelation2Ontology> getSemReg(){
		return this.getSelectRelation();
	}
	
	public String toString(){
		String str="Table Selected ("+selectRelation.size()+") : ";
		for(IRelation2Ontology r2o:selectRelation){
			str= str + r2o.getRelation().getRelationName()+" ";
		}
		return str;
	}
	
	public void clear(){
		selectRelation.clear();
		hashSelectRelation.clear();
	}
	
	
}
