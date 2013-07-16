package edu.zju.tcmsearch.query.domain;

import java.util.HashSet;
import java.util.Set;

import cn.edu.zju.dart.core.DartCoreConstants;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.exception.query.TcmRuntimeException;
import edu.zju.tcmsearch.web.form.query.SearchCondition;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */

/**
 * RDF的叶子属性的查询条件,它是最基本查询属性 下面不包含任何子对象
 */
public class ValueOntology {
	private DartQuery dartQuery;

	private DartOntology ontology;


	private Property nodeProperty;

	/**
	 * 一个Value 可以有多个查询条件，因此用Set
	 */
	private Set<SearchCondition> searchConditions = new HashSet<SearchCondition>();

	private String identity;

	public Set<SearchCondition> getSearchConditions() {
		return searchConditions;
	}

	public void setSearchConditions(Set<SearchCondition> searchConditions) {
		this.searchConditions = searchConditions;
	}

	/**
	 * 节点URI的唯一标志
	 * 查询结果取到ValueNode之后,可以直接通过这个identity来找到对应的ValueOntology,从而取得与之相关的本体和其它信息
	 * 
	 * @return
	 */
	public String getIdentity() {
		if (null != identity) {
			return identity;
		}
		identity = dartQuery.getPrefix() + "?var_vn_" + hashCode();
		return identity;
	}

	/**
	 * 相应本体论的标志 每个查询里面只有一个唯一的本体论,这样如果查询条件里已经有了这个本体论,就要取出这个valueNode,再往里添加条件
	 * 当一个列有多个查询条件要用到
	 * 
	 * @return
	 */
	public String getOntoIdentity() {
		return this.ontology.getIdentity();
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



	public Resource getValueNode() {
		if (this.searchConditions.size()==0) {
			throw new TcmRuntimeException("程序有错,查询条件不应该为空！");
		}
		Resource vn = dartQuery.createResource(getIdentity());
		vn.addProperty(DartCoreConstants.RDF_TYPE,
				DartCoreConstants.RDF_DART_VALUE_NODE);
		vn.addProperty(DartCoreConstants.RDF_DART_VALUE_NODE_IS_SELECTED,
				isSelect());
		/**
		 * 循环往里加查询条件
		 */
		for (SearchCondition searchCondition:searchConditions){
			Operator operator=Operator.get(searchCondition.getOperator());
            if (!searchCondition.isEmptyValue()){
			  vn.addProperty(operator.getProp(), searchCondition.getHandleValue());
            }
		}		
		return vn;
	}
	
	/**
	 * 只要有一次该列是被选中的，就设置它为被选中
	 * @return
	 */
	public boolean isSelect(){
		for (SearchCondition searchCondition:searchConditions){
			if (searchCondition.isSelect()){
				return true;
			}
		}
		return false;
	}

	public Property getNodeProperty() {
		if (null != this.nodeProperty) {
			return this.nodeProperty;
		}
		this.nodeProperty = dartQuery.createProperty(this.ontology
				.getOntology().toString());
		return this.nodeProperty;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n value ontology is " + this.ontology);
//		sb.append("\n operatore is " + this.operator);
//		sb.append("\n field value is " + this.getFieldValue());
		sb.append("\n is select is " + this.isSelect());
		return sb.toString();
	}

	public String getName() {
		return this.ontology.getName();
	}


	
	public void addSearchCondition(SearchCondition searchCondition){
		this.searchConditions.add(searchCondition);
	}
	
	public int getNotEmptyConditionValueCount(){
		int count=0;
		for (SearchCondition searchCondition:getSearchConditions()){
			if (!searchCondition.isEmptyValue()){
				count++;
			}
		}
		return count;
	}

}
