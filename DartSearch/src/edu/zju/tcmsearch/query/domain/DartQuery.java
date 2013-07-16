package edu.zju.tcmsearch.query.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.zju.dart.core.ontology.IOntologySchema;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import edu.zju.tcmsearch.common.TcmConstants;
import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.exception.query.TcmRuntimeException;
import edu.zju.tcmsearch.web.form.query.TreeNode;
/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */

/**
 * 一个基本的查询对象，它下面首先有一个根的InstanceOntology, 然后延伸出很多子对象
 * 所有的查询构造都是通过它在实现的
 * 每个Session只有一个DartQuery,因此将它存放在HttpSession中
 */
public class DartQuery {
	private String prefix=TcmConstants.VAR_PREFIX;

	private Model query = ModelFactory.createDefaultModel();

	private InstanceOntology rootInstOntology;
	
	private Map<String,InstanceOntology> instanceMap=new HashMap<String,InstanceOntology>();
	
    private Map<String,ValueOntology> valueMap=new HashMap<String,ValueOntology>();
    
    /**
     * 另一个基于value的HashMap,但它的主键是对应本体论的Identity,可用来判断加入的新的本体是否和原来一样
     * 用于一列对应的查询条件有多个的情况
     * 两个Map里保存的内容应该是相同的,只是查询条件不同
     */
    
    private Map<String,ValueOntology> ontoValueMap=new HashMap<String,ValueOntology>();
	
		
	/**
	 * 初次查询时调用它直接生成原始的查询对象
	 * @param ontoSchema
	 * @param rootOnto
	 */
	public DartQuery(IOntologySchema ontoSchema,TreeNode treeNode){
		InstanceOntology instanceOntology=addInstance(treeNode);		
		this.rootInstOntology=instanceOntology;
	}

	/**
	 * 添加InstanceOntology, 根据需要把它的父节点也添加进去，构成一棵树
	 * @param treeNode
	 * @return 返回的是根结点的InstanceOntology
	 */
	public InstanceOntology addInstance(TreeNode treeNode) {
		InstanceOntology instanceOntology=null;
		InstanceOntology origInstanceOnto=null;
		TreeNode curNode=treeNode;
		
		while (null!=curNode){
			instanceOntology=getInstance(curNode.getNodeIdentity());
			if (null==instanceOntology){
				instanceOntology=new InstanceOntology();
				instanceOntology.setDartQuery(this);
				instanceOntology.setOntology(curNode.getOntology());
				instanceOntology.setIdentity(curNode.getNodeIdentity());
				putInstance(instanceOntology);
			}	
			if (null!=origInstanceOnto){
				if (!instanceOntology.containChild(origInstanceOnto)){
				  instanceOntology.addChildInstance(origInstanceOnto);
				}
				origInstanceOnto.setParentOnto(instanceOntology);				  
			}
			origInstanceOnto=instanceOntology;
			curNode=curNode.getParentNode();
		}
		return instanceOntology;
	}
	
	public void addValue(String curNodeIdentity,ValueOntology valueOntology){
		if (null==valueOntology){
			return;
		}
		InstanceOntology curInstanceOnto=getInstance(curNodeIdentity);
		if (null==curInstanceOnto){
			throw new TcmRuntimeException("找不到node "+curNodeIdentity+" 在DartQuery中的对应节点!");
		}
		curInstanceOnto.addValueOnto(valueOntology);
	}
	
	public void addValues(String curNodeIdentity,List<ValueOntology> valueOntoList){
		if (null==valueOntoList || valueOntoList.size()==0){
			return;
		}
		InstanceOntology curInstanceOnto=getInstance(curNodeIdentity);
		if (null==curInstanceOnto){
			throw new TcmRuntimeException("找不到node "+curNodeIdentity+" 在DartQuery中的对应节点!");
		}
		curInstanceOnto.addValueOntos(valueOntoList);
		
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Resource createResource(String string) {
		return this.query.createResource(string);
	}
	
	public Property createProperty(String string) {
		return this.query.createProperty(string);
	}

	public InstanceOntology getRootInstOntology() {
		return rootInstOntology;
	}
	
    public List<DartOntology> getConcernedOntology(){
    	List<DartOntology> ontology =  new ArrayList<DartOntology>();
    	for(InstanceOntology inst:instanceMap.values()){
    		ontology.add(inst.getOntology());
    	}
    	return ontology;
    }

	public Model getQuery() {
		if(null!=query)
			query.close();
		query = ModelFactory.createDefaultModel();
		getQueryResource();
		return query;
	}

	public void setQuery(Model query) {
		this.query = query;
	}
	
	public void putInstance(InstanceOntology instanceOnto){
		this.instanceMap.put(instanceOnto.getIdentity(),instanceOnto);
	}
	
	public InstanceOntology getInstance(String identity){
		return this.instanceMap.get(identity);
	}
	
	public void putValue(ValueOntology valueOnto){
		this.valueMap.put(valueOnto.getIdentity(),valueOnto);
	}
	
	public ValueOntology getValue(String identity){
		return this.valueMap.get(identity);
	}
	
	public void putValueByOnto(ValueOntology valueOnto){
		this.ontoValueMap.put(valueOnto.getOntoIdentity(),valueOnto);
	}
	
	public ValueOntology getValueByOnto(String identity){
		return this.ontoValueMap.get(identity);
	}
	
	
	
	public Resource getQueryResource(){
		return this.rootInstOntology.getQueryResource();
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("dart query prefix is: "+getPrefix());
		sb.append("onto instance is : "+getRootInstOntology());
		return sb.toString();
	}
	
	public int getSelectValueCount(){
		return this.rootInstOntology.getSelectValueCount();
	}
	
	public int getConditionValueCount(){
		return this.rootInstOntology.getConditionValueCount();	
	}
    
    public void closeQuery(){
        this.query.close();
    }



}
