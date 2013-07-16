package edu.zju.tcmsearch.web.controller.query;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.mvc.Controller;

import cn.edu.zju.dart.core.utils.ModelToString;

import edu.zju.tcmsearch.common.service.OntoService;
import edu.zju.tcmsearch.exception.query.TcmRuntimeException;
import edu.zju.tcmsearch.query.domain.DartQuery;
import edu.zju.tcmsearch.query.domain.QueryResult;
import edu.zju.tcmsearch.query.domain.SelectRelationSet;
import edu.zju.tcmsearch.query.service.QueryTemplate;
import edu.zju.tcmsearch.util.web.DartQueryUtil;
import edu.zju.tcmsearch.util.web.TreeNodeUtil;
import edu.zju.tcmsearch.web.form.query.UsedRelationTable;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class SearchResultController implements Controller {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(SearchResultController.class);

	private OntoService ontoService;

	private QueryTemplate queryTemplate;

	private int columnThreshold = 10;// 显示的列数,如果超过这个列数,采用分栏显示,否则采用表格式显示

	private int listPageSize = 30;

	private int columnPageSize = 10;

	/**
	 * @return Returns the columnPageSize.
	 */
	public int getColumnPageSize() {
		return columnPageSize;
	}

	/**
	 * @param columnPageSize
	 *            The columnPageSize to set.
	 */
	public void setColumnPageSize(int columnPageSize) {
		this.columnPageSize = columnPageSize;
	}

	/**
	 * @return Returns the listPageSize.
	 */
	public int getListPageSize() {
		return listPageSize;
	}

	/**
	 * @param listPageSize
	 *            The listPageSize to set.
	 */
	public void setListPageSize(int listPageSize) {
		this.listPageSize = listPageSize;
	}

	public QueryTemplate getQueryTemplate() {
		return queryTemplate;
	}

	public void setQueryTemplate(QueryTemplate queryTemplate) {
		this.queryTemplate = queryTemplate;
	}

	public OntoService getOntoService() {
		return ontoService;
	}

	public void setOntoService(OntoService ontoService) {
		this.ontoService = ontoService;
	}

	@SuppressWarnings("unchecked")
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DartQuery dartQuery = (DartQuery) DartQueryUtil.getFromSession(request
				.getSession());
		if (null == dartQuery) {
			throw new TcmRuntimeException(
					"session中找不到dartQuery,可能是session过期或程序出错!");
		}
		if (logger.isDebugEnabled()) {
			logger.debug(ModelToString.getResultAsString(dartQuery.getQuery(),
					null));
		}
		// getQueryTemplate().setDartQuery(dartQuery);
		Map model = new HashMap();
		String errorInfoMsg = "";
		if (dartQuery.getSelectValueCount() <= 0) {
			errorInfoMsg += "<br/>您没有选择任何显示列! ";
		}

		if (!"".equals(errorInfoMsg)) {
			model.put("errorInfoMsg", errorInfoMsg);
			return new ModelAndView("errorInfo", model);
		}


		/*
		 * 把用户选择数据表传给后台@ming
		
		SelectRelationSet relationSelector = (SelectRelationSet) request
				.getSession().getAttribute("selectRelationSet");
		if (null == relationSelector) {
			throw new TcmRuntimeException("用户选择的表没有保存在session中,程序有错!");
		}
		*/
		try {
			//QueryResult queryResult = getQueryTemplate().queryForResult(dartQuery, relationSelector);
			QueryResult queryResult = getQueryTemplate().queryForResult(dartQuery,null); //取消数据来源选择
			logger.debug("the size of query result is "
					+ queryResult.getResultCount());
			logger.debug("the column num is " + queryResult.getColumnCount());
			// logger.debug("the return RDF is
			// "+ModelToString.getResultAsString(dartQuery.getQuery(),null));
			String style = request.getParameter("style");
			String viewName = setupResultStyle(queryResult, style);
			request.getSession().setAttribute("sessionQueryResult", queryResult);
			//if (queryResult.isMultiPage()) {
				
			//}
			/*
			 * 把数据来源包一层放到session中，用dartcore/dataSource.vm显示
			 */
			UsedRelationTable usedRT=new UsedRelationTable();
			usedRT.setUsedRelation(queryResult.getUsedRelation());
			DartQueryUtil.removeUsedRelation(request.getSession());
			DartQueryUtil.putUsedRelation(request.getSession(),usedRT);
			String treeNode=request.getParameter("nodeIdentity");
			request.getSession().setAttribute("treeNode", treeNode);
			
			/*
			String treeNode=request.getParameter("nodeIdentity");
			request.getSession().setAttribute("treeNode", treeNode);
			model.put("treeNode",TreeNodeUtil.getNode(treeNode));
			model.put("queryResult", queryResult);
			model.put("pageNo", 1);
			
			return new ModelAndView(viewName, model);
			*/
			String st = viewName.equals("dartcore/resultList") ? "list":"column";
			RedirectView redirectView = new RedirectView("searchResultNav.htm?style="+st+"&pageNo=1");
			return new ModelAndView(redirectView);
		} catch (TcmRuntimeException e) {
			return new ModelAndView("errorInfo");
		}
	}

	private String setupResultStyle(QueryResult queryResult, String style) {
		String viewName;
		if ("list".equals(style)) {
			queryResult.setPageSize(getListPageSize());
			viewName = "dartcore/resultList";
			return viewName;
		} else if ("column".equals(style)) {
			queryResult.setPageSize(getColumnPageSize());
			viewName = "dartcore/resultColumn";
			return viewName;
		}
		if (queryResult.getColumnCount() > getColumnThreshold()) {
			queryResult.setPageSize(getColumnPageSize());
			viewName = "dartcore/resultColumn";
		} else {
			queryResult.setPageSize(getListPageSize());
			viewName = "dartcore/resultList";
		}
		return viewName;
	}

	/**
	 * @return Returns the columnThreshold.
	 */
	public int getColumnThreshold() {
		return columnThreshold;
	}

	/**
	 * @param columnThreshold
	 *            The columnThreshold to set.
	 */
	public void setColumnThreshold(int columnThreshold) {
		this.columnThreshold = columnThreshold;
	}

}
