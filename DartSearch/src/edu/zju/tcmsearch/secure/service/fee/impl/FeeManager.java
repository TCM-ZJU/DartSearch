package edu.zju.tcmsearch.secure.service.fee.impl;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import cn.edu.zju.dart.core.semanticregistry.IDartSemanticRegistry;
import cn.edu.zju.dart.core.semanticregistry.IRelation2Ontology;
import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.common.service.OntoService;
import edu.zju.tcmsearch.dao.secure.IFeeDao;
import edu.zju.tcmsearch.dao.secure.ITableFeeChangedObserver;
import edu.zju.tcmsearch.dao.secure.TablePriceDao;
import edu.zju.tcmsearch.secure.domain.fee.FeeRecord;
import edu.zju.tcmsearch.secure.domain.fee.TablePrice;
import edu.zju.tcmsearch.secure.service.fee.IFeeManager;
import edu.zju.tcmsearch.util.dartcore.OntoUriParseUtil;
import edu.zju.tcmsearch.web.form.query.TreeNode;

public class FeeManager implements IFeeManager,ITableFeeChangedObserver {
	private IFeeDao feeDao;
	private TablePriceDao tablePriceDao;
	private OntoService ontoService;
	
	private List<FeeRecord> cache = new ArrayList<FeeRecord>();
	private int Cache_Threshold = 512;
	private Map<String,Integer> onto2price=null;
	private Map<String,Integer> table2price=null;
	
	public void setCacheThreshold(int threshold){
		this.Cache_Threshold = threshold;
	}
	
	public void setFeeDao(IFeeDao feeDao){
		this.feeDao = feeDao;
	}

	public void setTablePriceDao(TablePriceDao tablePriceDao){
		this.tablePriceDao = tablePriceDao;
	}
	
	public void setOntoService(OntoService ontoService){
		this.ontoService = ontoService;
	}
	

	synchronized public void initialize(){
			table2price = new HashMap<String,Integer>();
			for(TablePrice price:tablePriceDao.getAllTablePrice()){
				if(price.isFree()){
					table2price.put(price.getTableId(),0);
				}else{
					table2price.put(price.getTableId(),price.getPrice());
				}
			}

			onto2price = new HashMap<String,Integer>();
			IDartSemanticRegistry semReg = ontoService.getDartCoreSettings().getDartSemanticRegistry();
			for(DartOntology ontology:ontoService.getBaseOntologies()){
				String ontoId = OntoUriParseUtil.getOntoUri(ontology.getIdentity());
				IRelation2Ontology[] r2os = semReg.getSemRegByOntologyURIs(new String[]{ontoId},true);
				int totalPrice=0;
				for(IRelation2Ontology r2o:r2os){
					String tableId =r2o.getRelation().getRelationSpace().getRelationSpaceName()+"."+r2o.getRelation().getRelationName();
					totalPrice += this.getTableFee(tableId);
				}
				onto2price.put(ontology.getIdentity(),totalPrice==0? 0:totalPrice/r2os.length);
			}
	}

	public int getTableFee(String tableId) {
		// TODO Auto-generated method stub
		if(table2price.containsKey(tableId)){
			return table2price.get(tableId);
		}else{
			return 0;
		}
	}

	public int getOntologyFee(String ontoId) {
		// TODO Auto-generated method stub
		if(onto2price.containsKey(ontoId)){
			return onto2price.get(ontoId);
		}else{
			return 0;
		}
	}
	
	public int getOntologyFee(List<DartOntology> ontology){
		int price = 0;
		for(DartOntology onto:ontology){
			price += getOntologyFee(onto.getIdentity());
		}
		return price;
	}

	public int getTreeNodeFee(TreeNode node) {
		// TODO Auto-generated method stub
		int price=0;
		while(null!=node){
			price += getOntologyFee(node.getOntoIdentity());
			node = node.getParentNode();
		}
		return price;
	}

	synchronized public void log(FeeRecord fee) {
		// TODO Auto-generated method stub
		if(cache.size()>this.Cache_Threshold){
			feeDao.insert(cache);
			cache.clear();
		}
		cache.add(fee);
	}

	public List<FeeRecord> getFeeRecord(int id) {
		// TODO Auto-generated method stub
		return feeDao.getByAccountId(id);
	}

	public List<FeeRecord> getFeeRecord(int id, int type) {
		// TODO Auto-generated method stub
		return feeDao.getByIdType(id,type);
	}

	public List<FeeRecord> getFeeRecord() {
		// TODO Auto-generated method stub
		return feeDao.getAll();
	}

	public void onTableFeeChanged(){
		initialize();
	}
	
	public void destroy(){
		feeDao.insert(cache);
		cache.clear();		
	}
}
