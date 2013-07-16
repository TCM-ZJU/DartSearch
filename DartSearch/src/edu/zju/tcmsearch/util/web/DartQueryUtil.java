package edu.zju.tcmsearch.util.web;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import cn.edu.zju.dart.core.exceptions.DartCoreResourceReleaseException;
import cn.edu.zju.dart.core.ontology.IOntologySchema;
import cn.edu.zju.dart.core.query.sqlplan.ISqlPlan;
import cn.edu.zju.dart.core.query.sqlplan.result.IQueryResult;
import cn.edu.zju.dart.core.utils.DartCoreSettings;

import com.hp.hpl.jena.rdf.model.Model;

import edu.zju.tcmsearch.query.domain.DartQuery;
import edu.zju.tcmsearch.query.domain.SelectRelationSet;
import edu.zju.tcmsearch.web.form.query.UsedRelationTable;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class DartQueryUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DartQueryUtil.class);

	private static final ThreadLocal threadQueryResult = new ThreadLocal();

	private static final ThreadLocal threadSqlPlan = new ThreadLocal();

	private static final ThreadLocal threadQuery = new ThreadLocal();

	public static IOntologySchema getOntoSchema() {
		return DartCoreSettings.getInstance().getDartOntoSchema();
	}

	public static void closeResource(IQueryResult queryResult,
			ISqlPlan sqlPlan, Model query) {
		try {
			if (null != queryResult)
				queryResult.close();
//			if (null != sqlPlan)
//				sqlPlan.release();
//			if (null != query)
//				query.close();
		} catch (DartCoreResourceReleaseException e) {
			logger.error("close resource error!");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void putResource(IQueryResult queryResult, ISqlPlan sqlPlan,
			Model query) {
		if (null != queryResult)
			threadQueryResult.set(queryResult);
		if (null != sqlPlan)
			threadSqlPlan.set(sqlPlan);
		if (null != query)
			threadQuery.set(query);
	}

	public static void closeResouceInThread() {
		IQueryResult queryResult = (IQueryResult) threadQueryResult.get();
		ISqlPlan sqlPlan = (ISqlPlan) threadSqlPlan.get();
		Model query = (Model) threadQuery.get();
		closeResource(queryResult, sqlPlan, query);
	}

	public static void putInSession(HttpSession session, DartQuery dartQuery) {
		session.setAttribute("dartQuery", dartQuery);
	}
    
    public static void removeDartQuery(HttpSession session){
        session.removeAttribute("dartQuery");
    }

	public static DartQuery getFromSession(HttpSession session) {
		DartQuery dartQuery = (DartQuery) session.getAttribute("dartQuery");
		return dartQuery;
	}
    
	public static SelectRelationSet getSelectRelationSetFromSession(HttpSession session){
		SelectRelationSet srs=(SelectRelationSet) session.getAttribute("selectRelationSet");
		return srs;
	}

	public static void removeSelectRelationSetFromSession(HttpSession session){
			session.removeAttribute("selectRelationSet");
	}

	public static void putSelectRelationSetToSession(HttpSession session,SelectRelationSet srs){
           session.setAttribute("selectRelationSet",srs);
	}		
	
    public static void remove(HttpSession session){
        session.removeAttribute("dartQuery");
    }
    
    public static UsedRelationTable getUsedRelation(HttpSession session){
    	return (UsedRelationTable)session.getAttribute("relationUsed");
    }

    public static void putUsedRelation(HttpSession session, UsedRelationTable srt){
    	session.setAttribute("relationUsed",srt);  
    } 
    
    public static void removeUsedRelation(HttpSession session){
    	session.removeAttribute("relationUsed");
    } 
        
}
