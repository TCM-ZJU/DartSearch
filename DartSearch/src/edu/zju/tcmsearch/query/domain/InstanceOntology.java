package edu.zju.tcmsearch.query.domain;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.dart.core.DartCoreConstants;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import edu.zju.tcmsearch.common.domain.DartOntology;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */

/**
 * RDF的非叶子节点的查询属性,它下面可以包含一组子instance和一组子value的查询对象
 */
public class InstanceOntology {
	private DartQuery dartQuery;

	private DartOntology ontology;
	
	private InstanceOntology parentOnto;

	private List<InstanceOntology> childInstanceOntos = new ArrayList<InstanceOntology>();

	private List<ValueOntology> childValueOntos=new ArrayList<ValueOntology>();
	
	private String identity;
	

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public List<InstanceOntology> getChildInstanceOntos() {
		return childInstanceOntos;
	}

	public void setChildInstanceOntos(List<InstanceOntology> childInstanceOntos) {
		this.childInstanceOntos = childInstanceOntos;
	}

	public List<ValueOntology> getChildValueOntos() {
		return childValueOntos;
	}

	public void setChildValueOntos(List<ValueOntology> childValueOntos) {
		this.childValueOntos = childValueOntos;
	}

	public DartQuery getDartQuery() {
		return dartQuery;
	}

	public void setDartQuery(DartQuery dartQuery) {
		this.dartQuery = dartQuery;
	}

	public DartOntology getOntology() {
		return ontology;
	}

	public void setOntology(DartOntology ontology) {
		this.ontology = ontology;
	}
	
	/**
	 * 用递归取出本级以下的完整的查询对象
	 * @return
	 */
	public Resource getQueryResource(){
		Resource baseOnto = dartQuery.createResource(dartQuery.getPrefix() + "?var_ins_" + hashCode());
		Resource ontoType = dartQuery.createResource(ontology.getURI());
		baseOnto.addProperty(DartCoreConstants.RDF_TYPE,ontoType);
		for (InstanceOntology childInstanceOnto:childInstanceOntos){
			DartOntology predOntology=this.ontology.getPredOntology(childInstanceOnto.getOntology());
			Property prop = dartQuery.createProperty(predOntology.getURI());//取到谓语的URI作为property
			baseOnto.addProperty(prop,childInstanceOnto.getQueryResource());//加入的属性是宾语的查询对象
		}
		for (ValueOntology childValueOnto:childValueOntos){
			baseOnto.addProperty(childValueOnto.getNodeProperty(),childValueOnto.getValueNode());//加入简单属性的查询
		}
		return baseOnto;
	}

	
	public void addChildInstance(InstanceOntology instanceOntology){
		this.childInstanceOntos.add(instanceOntology);		
	}
	
	public boolean containChild(InstanceOntology instanceOntology){
		return this.childInstanceOntos.contains(instanceOntology);
	}

	public InstanceOntology getParentOnto() {
		return parentOnto;
	}

	public void setParentOnto(InstanceOntology parentOnto) {
		this.parentOnto = parentOnto;
	}
	
	public void addValueOnto(ValueOntology valueOntology){
		this.childValueOntos.add(valueOntology);
	}
	
	public void addValueOntos(List<ValueOntology> valueOntoList){	
		this.childValueOntos.addAll(valueOntoList);
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("\n current Ontology is "+ontology);
		//sb.append("\n parent Ontology is "+parentOnto);
		sb.append("\n the following is ontology value: ");
		sb.append("\n child value size is "+childValueOntos.size());
		sb.append("\n child onto size is "+childInstanceOntos.size());
		for (ValueOntology valueOnto:childValueOntos){
			sb.append(valueOnto.toString());
		}
		sb.append("/n the following is child ontology : ");
		for (InstanceOntology childInstance:childInstanceOntos){
			sb.append(childInstance.toString());
		}

		return sb.toString();
	}
	
	public String getName(){
		return this.ontology.getName();
	}
	
	public int getSelectValueCount(){
		int count=0;
		for (ValueOntology childValueOnto:childValueOntos){
			if (childValueOnto.isSelect()) count++;
		}
		for (InstanceOntology childInstanceOnto:childInstanceOntos){
			count+=childInstanceOnto.getSelectValueCount();
		}
		return count;
	}
	
	public int getConditionValueCount(){
		int count=0;
		for (ValueOntology childValueOnto:childValueOntos){
			int nCount=childValueOnto.getNotEmptyConditionValueCount();
			count=count+nCount;
		}
		for (InstanceOntology childInstanceOnto:childInstanceOntos){
			count+=childInstanceOnto.getConditionValueCount();
		}
		return count;		
	}
	
}
