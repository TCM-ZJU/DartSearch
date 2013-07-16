package edu.zju.tcmsearch.web.controller.query;

import org.apache.log4j.Logger;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.common.service.OntoService;
import edu.zju.tcmsearch.exception.query.TcmRuntimeException;
import edu.zju.tcmsearch.lucene.index.DbContentRetriever;
import edu.zju.tcmsearch.lucene.index.data.DbRecData;
import edu.zju.tcmsearch.query.domain.DartQuery;
import edu.zju.tcmsearch.query.domain.SelectRelationSet;
import edu.zju.tcmsearch.util.dartcore.EncodingUtil;
import edu.zju.tcmsearch.util.dartcore.OntoUriParseUtil;
import edu.zju.tcmsearch.util.web.DartQueryUtil;
import edu.zju.tcmsearch.util.web.ParameterValueDecoder;
import edu.zju.tcmsearch.util.web.TreeNodeUtil;
import edu.zju.tcmsearch.web.form.query.SearchCondition;
import edu.zju.tcmsearch.web.form.query.SearchConditionForm;
import edu.zju.tcmsearch.web.form.query.TreeNode;

public class SecondQueryController implements Controller {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(SecondQueryController.class);

	private SearchConditionForm searchConditionForm;
	
	private OntoService ontoService;
	
	private DbContentRetriever dbContentRetriever;
	



	public DbContentRetriever getDbContentRetriever() {
		return dbContentRetriever;
	}



	public void setDbContentRetriever(DbContentRetriever dbContentRetriever) {
		this.dbContentRetriever = dbContentRetriever;
	}



	public OntoService getOntoService() {
		return ontoService;
	}



	public void setOntoService(OntoService ontoService) {
		this.ontoService = ontoService;
	}



	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		TreeNode treeNode=getTreeNodeFromRequest(request);
		TreeNode parentNode = getParentNodeFromRequest(request);
		addQueryCondition(request,treeNode,parentNode);	
		
		
		String redirectName="redirect:searchResult.htm?nodeIdentity="+treeNode.getNodeIdentity();
		redirectName=EncodingUtil.getEngStr(redirectName);
		return new ModelAndView(redirectName);		
	}



	private void addQueryCondition(HttpServletRequest request, TreeNode treeNode,TreeNode parentNode) {
        /**
         * 把treeNode添加到dartQuery
         * 构造查询,选择本体的所有属性
         */ 
		DartQuery dartQuery=null;
		if (null!=parentNode ){
			   dartQuery=new DartQuery(getOntoService().getOntologySchema(),parentNode);
			   DartOntology dartOntology=parentNode.getOntology();
			   String primaryKey=request.getParameter("primaryKey");
			   String tableIdentity=request.getParameter("tableIdentity");
			   DbRecData dbRecData=getDbContentRetriever().getTableContentByKey(tableIdentity,primaryKey);	
			   String primaryKeyOntoName=OntoUriParseUtil.getOntoName( dbRecData.getPrimaryKeyColumn().getOntologyURI());
			   DartQueryUtil.putInSession(request.getSession(),dartQuery);
			   SearchCondition searchCondition=new SearchCondition();
			   searchCondition.setDartQuery(dartQuery);
			   searchCondition.setOntology(dartOntology.getValuePropByName(primaryKeyOntoName));
			   searchCondition.setFieldSelect("false");
			   searchCondition.setFieldValue(primaryKey);			   
			   searchCondition.addValueOntology();
			   
			   dartQuery.addInstance(treeNode);
		}		
        else{
        	dartQuery=DartQueryUtil.getFromSession(request.getSession());
            dartQuery.addInstance(treeNode); 
            addValueProperties(request, treeNode, dartQuery);
        }
		/*
		 *数据来源，添加 selectRelationSet到session,selectRelationSet包含用户选择的表 @ming
		 */		   
	   DartQueryUtil.removeSelectRelationSetFromSession(request.getSession());
	   DartQueryUtil.putSelectRelationSetToSession(request.getSession(),new SelectRelationSet());
	}

	private void addValueProperties(HttpServletRequest request, TreeNode treeNode, DartQuery dartQuery) {
		
		searchConditionForm=new SearchConditionForm(treeNode,dartQuery);
		

		if (null==dartQuery ){
			throw new TcmRuntimeException("dartQuery没有在内存,程序有错!");
		}
		
    	/**
    	 * 选择本体的全部属性
    	 * cond下标从1开始,因为conditionForm里面这样处理
    	 */		
		SearchCondition[] cond=searchConditionForm.getSearchConditions();
		for(int i=1;i<cond.length;i++ ){
			cond[i].setFieldSelect(",true");
			cond[i].setFieldValue("");
			cond[i].setOperator("");
		}
		
		searchConditionForm.addValueOntologies();
		
		/**
		 * 添加数据来源
		 */
		SelectRelationSet selectRelationSet = DartQueryUtil.getSelectRelationSetFromSession(request.getSession()); 
		if (null==selectRelationSet) 
			throw new TcmRuntimeException("用户选择的表没有保存在session中,程序有错!");
		else 
		    selectRelationSet.addRelation(searchConditionForm.getSelectRelation());
	}

	private TreeNode getTreeNodeFromRequest(HttpServletRequest request,String nodeIdentityStr) {
		String curNodeIdentity= request.getParameter(nodeIdentityStr);
		logger.debug("nodeIdentity: "+curNodeIdentity);
		TreeNode treeNode=TreeNodeUtil.getNode(curNodeIdentity);
		if (null==treeNode) {
			logger.debug("treeNode is empty with identity "+curNodeIdentity);		
		}
		return treeNode;
	}
	
	private TreeNode getTreeNodeFromRequest(HttpServletRequest request) {				
		return getTreeNodeFromRequest(request,"nodeIdentity");
	}	
	
	private TreeNode getParentNodeFromRequest(HttpServletRequest request) {				
		return getTreeNodeFromRequest(request,"parentNodeIdentity");
	}	

}
