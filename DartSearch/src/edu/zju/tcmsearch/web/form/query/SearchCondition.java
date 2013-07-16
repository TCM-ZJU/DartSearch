package edu.zju.tcmsearch.web.form.query;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

import cn.edu.zju.dart.core.DartCoreConstants;
import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.query.domain.DartQuery;
import edu.zju.tcmsearch.query.domain.Operator;
import edu.zju.tcmsearch.query.domain.ValueOntology;
import edu.zju.tcmsearch.util.dartcore.DataConvertor;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class SearchCondition {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(SearchCondition.class);

	private DartOntology parentOnto;
	
	private DartOntology ontology;	

	private String fieldSelect="false";

	private String operator;

	private String fieldValue;
    
    private TreeNode curTreeNode;
    

	
	DartQuery dartQuery;

	public DartQuery getDartQuery() {
		return dartQuery;
	}

	public void setDartQuery(DartQuery dartQuery) {
		this.dartQuery = dartQuery;
	}

	public String getFieldSelect() {
		return fieldSelect;
	}

	public void setFieldSelect(String fieldSelect) {
		this.fieldSelect = fieldSelect;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public DartOntology getOntology() {
		return ontology;
	}

	public void setOntology(DartOntology ontology) {
		this.ontology = ontology;
	}

	public String getOperator() {
		return operator;
	}
	
	public Operator getOperatorObj(){
		return Operator.get(getOperator());
	}
	
	//如果是like,而且输入的值不为空,则fieldValue上加上%
//	private String getHandleValue(){
//		if (this.operator.equals(DartCoreConstants.RDF_DART_VALUE_NODE_LIKE.toString())){
//			if (!isEmptyValue()){
//			  return "%"+getFieldValue()+"%";
//			}
//		}
//		return getFieldValue();
//	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public DartOntology getParentOnto() {
		return parentOnto;
	}

	public void setParentOnto(DartOntology parentOnto) {
		this.parentOnto = parentOnto;
	}
	
	public boolean isSelect(){
		//logger.debug("the field Select is : "+fieldSelect );
        if (null==fieldSelect){
            return false;
        }
		if (",true".equals(fieldSelect.trim())){
			return true;
		}
		return DataConvertor.toBoolean(fieldSelect);
	}
	
	public boolean isEmptyValue(){
		return (null==fieldValue || "".equals(fieldValue));
	}
	
	/**
	 * 如果当前列的查询值为空或未被选中,则该查询条件为空,无效
	 * @return
	 */
	public boolean isEmptyCondition(){
		return isEmptyValue()&&!isSelect();
	}
	/**
	 * 返回处理后的值，like %大黄%
	 */
	public String getHandleValue(){
		String sb = null;
		if (this.operator.equals(DartCoreConstants.RDF_DART_VALUE_NODE_LIKE.toString())) {
			if (!isEmptyValue()) {
				sb="'%"+getFieldValue()+"%'";
			}
		}else {
		    sb="'"+getFieldValue()+"'";
		}
		logger.debug("HandleFieldValue:"+sb+"");
		return sb;
	}
	
    
	public void addValueOntology(){
		if (isEmptyValue() && !isSelect()){
			return;
		}
       
		logger.debug("添加："+this.ontology.getIdentity()+this.fieldSelect+" "+this.fieldValue);
		ValueOntology valueOntology=dartQuery.getValueByOnto(this.ontology.getIdentity());
		//如果原先的本体论已经有了这个ValueOnto,那么只要取出来往里加条件里行了
		if (null==valueOntology){
			valueOntology=new ValueOntology();
			valueOntology.setDartQuery(dartQuery);
			valueOntology.setOntology(ontology);
			dartQuery.putValueByOnto(valueOntology);
			dartQuery.putValue(valueOntology);
            dartQuery.addValue(curTreeNode.getNodeIdentity(),valueOntology);
		}
		valueOntology.addSearchCondition(this);
	}

	@Override
	/**
	 * 如果两次输入的查询条件是一样的，就可以通过它来合并
	 */
	public boolean equals(Object obj) {
		if (null==obj){
			return false;
		}
		if (!(obj instanceof SearchCondition)){
			return false;
		}
		SearchCondition objSearchCondition=(SearchCondition)obj;
		if (objSearchCondition.getFieldValue().equals(this.getFieldValue()) &&
			objSearchCondition.getOntology().equals(this.getOntology()) && 
			objSearchCondition.getOperator().equals(this.getOperator()) &&
			objSearchCondition.isSelect()==this.isSelect()){
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(15, 19).append(getFieldValue()).append(getOntology()).append(getOperator()).toHashCode();
	}

    /**
     * @return Returns the curTreeNode.
     */
    public TreeNode getCurTreeNode() {
        return curTreeNode;
    }

    /**
     * @param curTreeNode The curTreeNode to set.
     */
    public void setCurTreeNode(TreeNode curTreeNode) {
        this.curTreeNode = curTreeNode;
    }

}
