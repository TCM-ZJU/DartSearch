package edu.zju.tcmsearch.web.controller.query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import dartie.application.Context;


import edu.zju.tcmsearch.query.domain.ColumnInfo;
import edu.zju.tcmsearch.query.domain.InstanceOntology;
import edu.zju.tcmsearch.query.domain.QueryResult;
import edu.zju.tcmsearch.query.domain.ValueOntology;
import edu.zju.tcmsearch.util.web.MultiLinguaUtil;
import cn.edu.zju.dart.core.query.sqlplan.result.IResultDataItem;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class SemanticViewerController implements Controller {

	protected static Logger  logger = Logger.getLogger(SemanticViewerController.class);
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if(null==request.getParameter("sessionId")){
		   return showApplet(request);
		}else if("GETXML".equals(request.getParameter("cmd"))){
		   Map<String,String> model = new HashMap<String,String>();
		   String xml=Context.getXML(request.getParameter("sessionId"));
		   model.put("xml",xml);
		   return new ModelAndView("dartcore/XMLResult",model);
		}else if("REMOVE".equals(request.getParameter("cmd"))){
		   Context.remove(request.getParameter("sessionId"));
		   return null;
		}
		return null;
			
	}

	
	private ModelAndView showApplet(HttpServletRequest request) {
		// TODO Auto-generated method stub
		QueryResult queryResult = (QueryResult) request.getSession().getAttribute("sessionQueryResult");
		String rowStr = (String)request.getSession().getAttribute("SearchResult_RowNo");
		String pageStr = (String)request.getSession().getAttribute("SearchResult_PageNo");
		String xml = "";
		if(null!=queryResult && null!=rowStr && null!=pageStr){
			int page = Integer.parseInt(pageStr);
			int row =  Integer.parseInt(rowStr);
			xml = getResultXML(queryResult,page,row);
			String sessionId = request.getSession().getId();
		    Context.put(sessionId,xml);
		}    
	    
		Map<String,String> model = new HashMap<String,String>();
		model.put("sessionId",request.getSession().getId());
		String codebase = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath(); 
		model.put("path",request.getRequestURL().toString());
		model.put("codebase",codebase);
		
		String ViewName = "dartcore/SematicResult";
	    return new ModelAndView(ViewName,model);
	}
	
	private  String getResultXML(QueryResult queryResult,int page,int row){
		
		IResultDataItem item=(IResultDataItem)queryResult.getPageList().getPage(page).get(row-1);
		Map<String,String> onto2colname = new HashMap<String,String>();
		List<InstanceOntology> insOnto = new ArrayList<InstanceOntology>();
		for(ColumnInfo col:queryResult.getColumnInfos()){
			logger.debug(col.getOntoName()+":"+col.getColumnName());
			onto2colname.put(col.getOntoName(),col.getColumnName());
		}
		int pos =0;
		InstanceOntology cur = queryResult.getDartQuery().getRootInstOntology();
		insOnto.add(cur);
		String xEdgesOnto = "";
		while(pos<insOnto.size()){
			int nodeId = pos+1;
			cur = insOnto.get(pos);
			for(InstanceOntology inso:cur.getChildInstanceOntos()){
				insOnto.add(inso);
				int childId = insOnto.size();
				xEdgesOnto +=  "<edge source=\"onto_"+nodeId+"\" target=\"onto_"+childId+"\"></edge>";
				logger.debug(inso.getName());
			}
			pos++;
		}
		final String xHead = 		
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
	        "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\">"+
	        "<graph edgedefault=\"undirected\">"+
	        "<key id=\"name\" for=\"node\" attr.name=\"name\" attr.type=\"string\"/>";
		final String xEnd = "</graph>"+ "</graphml>";
		String xNodes="";
		String xEdges="";
		int ID_Count=1;
		int ID_Count_Onto = 1;
		for(InstanceOntology inso:insOnto){
			int nodeId = ID_Count_Onto++;
			String node = "<node id=\"onto_"+nodeId+"\"><data key=\"name\">"+inso.getName()+"</data></node>";
			xNodes+=node;
			for(ValueOntology vonto:inso.getChildValueOntos()){
				int childId = ID_Count++;
				String childnode = "<node id=\"val_"+childId+"\"><data key=\"name\">"+vonto.getName()+":"+item.getData(onto2colname.get(vonto.getName()))+"</data></node>";
				xNodes+=childnode;
				String edge = "<edge source=\"onto_"+nodeId+"\" target=\"val_"+childId+"\"></edge>";
				xEdges+=edge;
			}
		}
		return xHead+xNodes+xEdgesOnto+xEdges+xEnd;
	}
	
	private String handleXML(String xml){
		return xml.replace("<","&lt").replace(">","&gt").replace("%","％");
	}
}
