package edu.zju.tcmsearch.web.form.secure;
import java.util.List;

import cn.edu.zju.dart.core.semanticregistry.IRelation2Ontology;
import cn.edu.zju.dart.core.utils.DartCoreSettings;

import edu.zju.tcmsearch.common.service.OntoService;
import edu.zju.tcmsearch.secure.domain.fee.TablePrice;

public class TablePriceForm {
	private OntoService ontoService;
	
	private TablePrice[] priceSet;
	public TablePriceForm(OntoService ontoService){
		this.ontoService = ontoService;
	}
	
	/*
	 * note that parameter tpSet are modified after invoking 
	 */
	public void initialize(List<TablePrice> tpSet){
		DartCoreSettings dcSetting = ontoService.getDartCoreSettings();
		IRelation2Ontology[] allSemReg = dcSetting.getDartSemanticRegistry().getAllSemReg();
		for(int i=0;i<allSemReg.length;i++){
			IRelation2Ontology r2o = allSemReg[i];
			String tableName = r2o.getRelation().getRelationName();
			String tableSpace = r2o.getRelation().getRelationSpace().getRelationSpaceName();
			TablePrice tp = new TablePrice(tableSpace+"."+tableName,"false",1);
			if(!tpSet.contains(tp))
				tpSet.add(tp);
		}
		this.priceSet = tpSet.toArray(new TablePrice[0]);
	}
	
	public TablePrice[] getPriceSet(){
		return this.priceSet;
	}
	
	public void setPriceSet(TablePrice[] set){
		this.priceSet = set;
	}
	
}
