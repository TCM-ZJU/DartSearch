package edu.zju.tcmsearch.web.form.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.query.domain.DartQuery;
import edu.zju.tcmsearch.util.web.TreeNodeUtil;
import cn.edu.zju.dart.core.semanticregistry.IDartSemanticRegistry;
import cn.edu.zju.dart.core.semanticregistry.IRelation2Ontology;
import cn.edu.zju.dart.core.utils.DartCoreSettings;



/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class SearchConditionForm {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(SearchConditionForm.class);
	
	private int arrayBase=1;

	private int length;
	
	private SearchCondition[] searchConditions;
	
	private DartQuery dartQuery;
	
	private DartOntology  ontology;
	
	private List<DartOntology> valueProperitesFiltered;
	
	/**
	 * 添加一个数据来源的列表 @取消数据来源选择
	 */
	@Deprecated
	private SelectRelationTable selectRelation;
	@Deprecated
	public SelectRelationTable getSelectRelation(){
		return selectRelation;
	}
	@Deprecated
	public void setSelectRelation(SelectRelationTable selectRelation){
		this.selectRelation=selectRelation;
	}
	
	
	public SearchCondition[] getSearchConditions() {
		return searchConditions;
	}

	public void setSearchConditions(SearchCondition[] searchConditions) {
		this.searchConditions = searchConditions;
	}

	private void initSearchCondition(TreeNode treeNode){
		/*
        DartOntology ontology=treeNode.getOntology();
		List<DartOntology> propOntoList= ontology.getValueProperties();
		*/
		
		DartOntology ontology=treeNode.getOntology();
		List<DartOntology> propOntoList= this.getValuePropertiesByFilter(this.ontology,"ID");
	
		int i=1;
		for (DartOntology propOnto:propOntoList){
			searchConditions[i].setOntology(propOnto);
			searchConditions[i].setParentOnto(ontology);
            searchConditions[i].setCurTreeNode(treeNode);
			searchConditions[i].setDartQuery(dartQuery);
			i++;
		}
		

		/*
		 * 数据来源，初始化SelectRelationTable
		 * 
		 
		selectRelation=new SelectRelationTable();
		String[] ontoURI=new String[1];
		ontoURI[0]=ontology.getURI();
		IDartSemanticRegistry semreg = DartCoreSettings.getInstance().getDartSemanticRegistry();
		IRelation2Ontology[] IR2O= semreg.getSemRegByOntologyURIs(ontoURI,false);
		selectRelation.setSelectRelation(IR2O);
		*/
	}
	
	public SearchConditionForm(TreeNode treeNode,DartQuery dartQuery){
		logger.debug("SearchCondForm dartQuery"+dartQuery);
		
		this.dartQuery=dartQuery;
		this.ontology = treeNode.getOntology();
		this.valueProperitesFiltered = this.getValuePropertiesByFilter(this.ontology,"ID");	//过滤ID属性，使其不在页面显示
		this.length=this.valueProperitesFiltered.size()+1;//由于页面上velocityCount是从1开始计,目前没有好办法改为0,因此增加一个元素,其中第0位是浪费掉的
		logger.debug("value properties size is "+this.length);
		searchConditions=new SearchCondition[this.length];
		for (int i=0;i<searchConditions.length;i++){
			searchConditions[i]=new SearchCondition();			
		}
		initSearchCondition(treeNode);
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("searchCondition Length is "+this.length+" \n");
		for (int i=arrayBase;i<searchConditions.length;i++){
			sb.append(" condition "+i+" is as follows: ");
			sb.append(" isSelect:"+searchConditions[i].getFieldSelect());
			sb.append(" operator:"+searchConditions[i].getOperator());
			sb.append(" fieldValue:"+searchConditions[i].getFieldValue());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public List<DartOntology> getSelectOntologies(){
		List<DartOntology> selectOntologies=new ArrayList<DartOntology>();
		for (int  i=arrayBase;i<searchConditions.length;i++){
			if (searchConditions[i].isSelect()){
				selectOntologies.add(searchConditions[i].getOntology());
			}
		}
		return selectOntologies;
	}
    
    public void addValueOntologies(){
        for (SearchCondition searchCondition:searchConditions){
            searchCondition.addValueOntology();
        }
    }
	
//	public List<ValueOntology> getValueOntoList(){
//		List<ValueOntology> valueOntologies=new ArrayList<ValueOntology>();
//		for (int  i=arrayBase;i<searchConditions.length;i++){
//            //只有当查询条件为空并且原来该本体论没有加入查询条件时才加入
//			if (!searchConditions[i].isEmptyCondition() && searchConditions[i].getOriginOnto()==null){
//              valueOntologies.add(searchConditions[i].getValueOntology());
//			}
//		}
//		return valueOntologies;
//	}
	
    public static int utilMinusOne(int number){
    	return number-1;
    }

	/**
	 * 获取本体的值属性，过滤名称中包含pattern子串的值属性
	 * @param ontology
	 * @param pattern
	 * @return
	 */
    protected List<DartOntology> getValuePropertiesByFilter(DartOntology ontology,String pattern){
        List<DartOntology> vpset = new ArrayList<DartOntology>();
        for(DartOntology vp:ontology.getValueProperties()){
        	if(vp.getName().indexOf(pattern)==-1){
        		vpset.add(vp);
        	}
        }
        return vpset;
    }

    public List<DartOntology> getValueProperitesFiltered(){
    	return this.valueProperitesFiltered;
    }
}
