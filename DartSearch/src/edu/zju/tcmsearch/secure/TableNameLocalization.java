package edu.zju.tcmsearch.secure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.zju.dart.core.semanticregistry.IRelation2Ontology;
import cn.edu.zju.dart.core.utils.DartCoreSettings;
import edu.zju.tcmsearch.common.service.OntoService;
import edu.zju.tcmsearch.dao.secure.ITableNameLocalDAO;

public class TableNameLocalization {

	public final String _CHINESE = "CN";
	public final String _ENGLISH = "EN";
	
	private Map<String,String> chineseName = Collections.synchronizedMap(new HashMap<String,String>());
	private String defaultCountry = _CHINESE;
	private ITableNameLocalDAO dao ;
	
	private OntoService ontoService;
	
	private List<String> cache_keyset=null;
	
	public void setOntoService(OntoService ontoService){
		this.ontoService = ontoService;
	}
	public void setTableNameLocalDAO(ITableNameLocalDAO dao){
		this.dao = dao;
	}
	
	public void setCountry(String country){
		defaultCountry =  _ENGLISH.equals(country) ? _ENGLISH : _CHINESE;
	}
	
	public String get(String name){
		return get(name,defaultCountry);
	}
	
	public String get(String name,String country){
		if(_CHINESE.compareTo(country)==0){
			return null==chineseName.get(name) ?  name:chineseName.get(name) ;
		}else{
			return name;
		}
	}
	
	public void add(String name,String chn){
		chineseName.put(name,chn);
	}
	
	public void add(List<String> name,List<String> chn){
		assert name.size() == chn.size();
		for(int i=0;i<name.size();i++){
			chineseName.put(name.get(i),chn.get(i));
		}
	}
	
	public List<String> keySet(){
		if(null==cache_keyset){
			cache_keyset = new ArrayList<String>(chineseName.keySet());
	        Collections.sort(cache_keyset);
		}
	    return cache_keyset;
	}
	
	public List<String> keySet(String tableSpace){
		boolean found = false;
		List<String> keyset = new ArrayList<String>();
		for(String id:this.keySet()){
			if(id.startsWith(tableSpace)){
				keyset.add(id);
				found = true;
			}else if(found){
				break;
			}
		}
	    return keyset;
	}	
	
	public void load(){
		dao.load(chineseName);
	}

	public void update(){
		dao.clear();
		dao.update(chineseName);
	}
	
	public void init(){
		assert this.dao != null : "ITableNameLocalDAO CAN NOT BE null.";
		initMap();
		load();
	}
	
	public void close(){
		update();
	}
	
	protected void initMap() {
		chineseName.clear();
		DartCoreSettings dcSetting = ontoService.getDartCoreSettings();
		IRelation2Ontology[] allSemReg = dcSetting.getDartSemanticRegistry().getAllSemReg();
		for(IRelation2Ontology r2o:allSemReg){
			String tableName = r2o.getRelation().getRelationName();
			String tableSpace = r2o.getRelation().getRelationSpace().getRelationSpaceName();
			chineseName.put(tableSpace+"."+tableName,null);
		}
	}	
}
