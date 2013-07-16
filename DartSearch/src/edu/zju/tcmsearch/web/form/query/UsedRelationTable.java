package edu.zju.tcmsearch.web.form.query;

import cn.edu.zju.dart.core.resregistry.model.IRelation;
/*
 * 每次查询时由dartcore返回，记录能够查到数据的表
 */
public class UsedRelationTable {

	private IRelation[] usedRelation;
	
	public IRelation[] getUsedRelation(){
		return this.usedRelation;
	}

	public void setUsedRelation(IRelation[] usedRelation){
		this.usedRelation=usedRelation;
	}

	public static String getRelationDBSource(IRelation relation){
		if(null==relation)
			return null;
		return relation.getDBResourceID().getLocalPart();
	}
	
	public static String getRelationID(IRelation relation){
		return relation.getRelationSpace().getRelationSpaceName()+"."+relation.getRelationName();
	}	
}
