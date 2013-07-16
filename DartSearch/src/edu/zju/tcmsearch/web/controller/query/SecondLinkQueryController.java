package edu.zju.tcmsearch.web.controller.query;

import java.util.Set;
import java.util.HashSet;
import java.net.URLEncoder;
import java.sql.Types;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.apache.log4j.Logger;

import edu.zju.tcmsearch.exception.query.TcmRuntimeException;
import edu.zju.tcmsearch.query.domain.ColumnInfo;
import edu.zju.tcmsearch.query.domain.DartQuery;
import edu.zju.tcmsearch.query.domain.PageList;
import edu.zju.tcmsearch.query.domain.QueryResult;
import edu.zju.tcmsearch.query.domain.SelectRelationSet;
import edu.zju.tcmsearch.query.domain.ValueOntology;
import edu.zju.tcmsearch.util.dartcore.EncodingUtil;
import edu.zju.tcmsearch.util.web.DartQueryUtil;
import edu.zju.tcmsearch.util.web.TreeNodeUtil;
import edu.zju.tcmsearch.web.form.query.SearchCondition;
import edu.zju.tcmsearch.web.form.query.SearchConditionForm;
import edu.zju.tcmsearch.web.form.query.TreeNode;
import cn.edu.zju.dart.core.DartCoreConstants;
import cn.edu.zju.dart.core.query.sqlplan.result.IResultDataItem;


public class SecondLinkQueryController implements Controller {

	private static final Logger logger = Logger.getLogger(SecondLinkQueryController.class);	
	
	private SearchConditionForm searchConditionForm;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		TreeNode treeNode = getTreeNodeFromRequest(request);
		/*
		 * 当用户在结果表中选中某一行时，把该行作为二次查询的附加条件
		 */
		if(null!=request.getSession().getAttribute("SearchResult_RowNo"))
		   rebuildQuery(request);
		/*
		 * 添加关联的本体 
		 */
	   addQueryCondition(request,treeNode);
		
		
		String redirectName="redirect:searchResult.htm?nodeIdentity="+treeNode.getNodeIdentityEncoded();
		return new ModelAndView(redirectName);		
	}
     
	/*
	 * 在前一次查询的基础上进行关联查询，把前次查旬的结果作为条件添加到新查询中。
	 */ 
    private void rebuildQuery(HttpServletRequest request) throws Exception{
    	QueryResult QResult =(QueryResult) request.getSession().getAttribute("sessionQueryResult");
		String rowNo =(String) request.getSession().getAttribute("SearchResult_RowNo");
    	if(null==QResult){
    		throw new TcmRuntimeException("queryResult没有在内存，无法进行关联查询");
    	}
		if(null==rowNo){
			throw new TcmRuntimeException("session中没有指定SearchResult_RowNo，无法进行关联查询");
		}
		
    	PageList  page=QResult.getPageList();
    	IResultDataItem dataRow=null;
    	if(page.isMultiPage()){
    		String pageNo =(String) request.getSession().getAttribute("SearchResult_PageNo");
    		if(null==pageNo){
    			throw new TcmRuntimeException("session中没有指定SearchResult_PageNo，无法进行关联查询");
    		}
            dataRow = page.getPage(Integer.parseInt(pageNo)).get(Integer.parseInt(rowNo)-1);		
    	}else{
    		dataRow = page.getPage(page.getMinPage()).get(Integer.parseInt(rowNo)-1);
    	}
    	
    	for(ColumnInfo col: QResult.getColumnInfos()){
    		ValueOntology valueOnto=col.getValueOntology();
    		Set<SearchCondition> conditionSet = new HashSet<SearchCondition>();
    		SearchCondition oldCond = valueOnto.getSearchConditions().iterator().next();
    		SearchCondition condition = new SearchCondition(); 
    		condition.setFieldSelect(",true");
     		condition.setOperator(DartCoreConstants.RDF_DART_VALUE_NODE_EQUALTO.toString());
     		if(true==typeFilter(col.getSqlType())){
    		   condition.setFieldValue(dataRow.getData(col.getColumnName()).toString());
     		}
     		else{
     		   condition.setFieldValue("");
     		}
   		    logger.info("--["+col.getValueOntology().getOntoIdentity()+"] --->  "+dataRow.getData(col.getColumnName()));
   		    logger.info("SqlType ="+col.getSqlType()+"  Filter="+typeFilter(col.getSqlType()));
   		    
    		condition.setCurTreeNode(oldCond.getCurTreeNode());
    		condition.setDartQuery(oldCond.getDartQuery());
    		condition.setOntology(oldCond.getOntology());
    		condition.setParentOnto(oldCond.getParentOnto());
    		conditionSet.add(condition);
    		valueOnto.setSearchConditions(conditionSet);
    	}
    	
    	request.getSession().removeAttribute("SearchResult_RowNo");
    	request.getSession().removeAttribute("SearchResult_PageNo");
    	
    }

	private void addQueryCondition(HttpServletRequest request, TreeNode treeNode) {
        /**
         * 把treeNode添加到dartQuery
         * 构造查询,选择本体的所有属性
         */ 
		DartQuery dartQuery=DartQueryUtil.getFromSession(request.getSession());
		if (null==dartQuery ){
			throw new TcmRuntimeException("dartQuery没有在内存,程序有错!");
		}		
        else{
            dartQuery.addInstance(treeNode); 
            addValueProperties(request, treeNode, dartQuery);
        }
	}

	/**
	 * 把关联的本体添加到查询条件中
	 * @param request
	 * @param treeNode
	 * @param dartQuery
	 */
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
		 
		SelectRelationSet selectRelationSet = DartQueryUtil.getSelectRelationSetFromSession(request.getSession()); 
		if (null==selectRelationSet) 
			throw new TcmRuntimeException("用户选择的表没有保存在session中,程序有错!");
		else 
		    selectRelationSet.addRelation(searchConditionForm.getSelectRelation());
		*/
	}

	private TreeNode getTreeNodeFromRequest(HttpServletRequest request) {
		String nodeIdentity=request.getParameter("nodeIdentity");
		TreeNode treeNode=TreeNodeUtil.getNode(nodeIdentity);
		if (null==treeNode) 
			throw new TcmRuntimeException("treeNode,程序有错!");			
		return treeNode;
	}

	/**
	 * 过滤结果表中某些类型的值, DARTCORE不支持这些类型作为查询条件
	 * @param sqltype
	 * @return
	 */
	private boolean typeFilter(int sqltype){
	  switch(sqltype){
		case Types.BIGINT:
		case Types.BIT:
		case Types.BOOLEAN:
		case Types.DECIMAL:
		case Types.NUMERIC:
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.REAL:
		case Types.INTEGER:
		case Types.SMALLINT:
		case Types.TINYINT:
			 return true;
			 
		// Date, Time and Timestamp stypes
		case Types.DATE:
		case Types.TIME:
		case Types.TIMESTAMP:
             return true; 

		// Binary types
		case Types.BINARY:
		case Types.VARBINARY:
		case Types.LONGVARBINARY:
		case Types.BLOB:
             return false;
			
		// String types (to avoid having to deal with a
		// java.sql.Clob object)
		case Types.CHAR:
		case Types.LONGVARCHAR:
		case Types.VARCHAR:
			 return true;
		case Types.CLOB:
			 return false;
			 
		// Unsupported types
		case Types.ARRAY:
		case Types.DISTINCT:
		case Types.JAVA_OBJECT:
		case Types.NULL:
		case Types.OTHER:
		case Types.REF:
		case Types.STRUCT:
			 return false;
		}
	  
	  return false;	
	}
	
}
