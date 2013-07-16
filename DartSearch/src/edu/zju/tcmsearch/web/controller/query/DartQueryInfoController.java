package edu.zju.tcmsearch.web.controller.query;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


import edu.zju.tcmsearch.exception.query.TcmRuntimeException;
import edu.zju.tcmsearch.query.domain.DartQuery;
import edu.zju.tcmsearch.query.domain.InstanceOntology;
import edu.zju.tcmsearch.query.domain.ValueOntology;
import edu.zju.tcmsearch.util.GBK2Big5.GB2Big5;
import edu.zju.tcmsearch.util.web.DartQueryUtil;
import edu.zju.tcmsearch.util.web.GB2Big5Checker;
import edu.zju.tcmsearch.util.web.MultiLinguaUtil;
import edu.zju.tcmsearch.util.web.TreeNodeUtil;
import edu.zju.tcmsearch.web.form.query.SearchCondition;
import edu.zju.tcmsearch.web.form.query.TreeNode;



public class DartQueryInfoController implements Controller{
	
	
	private String instanceImg="images/RDFNode/instancesmall2.gif";
	private String valueImg="images/RDFNode/valuesmall3.gif";
	private String css="ms-vb";
	private String selectImg="images/select.gif";
	private String unselectImg="images/unselect.gif";
	private String NavUrl = "ontologyInfo.htm";

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public String getInstanceImg() {
		return instanceImg;
	}

	public void setInstanceImg(String instanceImg) {
		this.instanceImg = instanceImg;
	}

	public String getValueImg() {
		return valueImg;
	}

	public void setValueImg(String valueImg) {
		this.valueImg = valueImg;
	}
	public void setNavUrl(String url) {
		this.NavUrl=url;
	}

	public String getNavUrl() {
		return this.NavUrl;
	}
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DartQuery dartQuery=DartQueryUtil.getFromSession(request.getSession());
		if (null==dartQuery){
		  String ViewName = "dartcore/emptyDartQueryInfo";
		  Map<String,Object> model = new HashMap<String,Object>();
		  model.put("GB2Big5",GB2Big5.getInstance());
		  return new ModelAndView(ViewName,model);
		}
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		if(null!=request.getSession().getAttribute("sessionQueryResult")){
		   /*
		    * 关联导航时，显示关联本体
		    */	
		   return writeLinkOntology(request);
		} else {
			/*
			 * 构造查询时，显示查询本体树
			 */		
		   return writeOntologyTree(request,dartQuery, out);
		}
	}

	private ModelAndView writeOntologyTree(HttpServletRequest request,DartQuery dartQuery, PrintWriter out)throws Exception {
		out.println("<div class='"+getCss()+"'>");
		out.println("<br/><div class='ms-pagetitle' align='center'>查询条件基本信息</div>");
		/*
		out.println("<br/><div align='center'  class='ms-GRFontSize'><a href='searchResult.htm'>刷新查询结果</a></div>");
        */
		out.println("<br/><div align='center'  class='ms-GRFontSize'><a href='conditionClear.htm'>清除查询条件</a></div><br/>");
        out.println("<script type='text/javascript'>");
        out.println("function setInnerHtml(node,HtmlContent)\n{\n node.innerHTML=HtmlContent;\n}");
        out.println("function unSet(node)\n{\n node.outerHTML=\"<div id=\"+node.getAttribute(\"id\")+\"></div>\";\n}");
        out.println("</script>");
		writeInstance(request,dartQuery.getRootInstOntology(),0,out);
		out.println("</div>");
		return null;
	}
	
	private int autoNameID=0;
	private void writeInstance(HttpServletRequest request,InstanceOntology instanceOnto,int level,PrintWriter out)throws Exception{
		String spaceStr="&nbsp;&nbsp;";

		for (int i = 0; i < level; i++) {
			spaceStr+="&nbsp;&nbsp;";
		}
				
		String instanceImgStr="<img border='0' src='"+instanceImg+"'";
		instanceImgStr += " OnMouseOver=setInnerHtml(EM"+autoNameID+"_"+level+",HM"+autoNameID+"_"+level+")  ";
		instanceImgStr += " OnMouseOut=unSet(EM"+autoNameID+"_"+level+")  />";
		String valueImagStr="<img border='0' src='"+valueImg+"'/>";
		
		/*
		 * if you have any problem , check this href link . nodeIdentity vs ontoIdentity;
		 */
		out.print("<br/>"+spaceStr+instanceImgStr+"&nbsp");
		out.println("<a href='"+NavUrl+"?nodeIdentity="+URLEncoder.encode(instanceOnto.getIdentity(),"UTF-8")+"'>");
		out.println((GB2Big5Checker.check(request) ? GB2Big5.getInstance().get(instanceOnto.getName()):instanceOnto.getName())+"</a>");
		
		out.println("<script type='text/javascript'>");
		out.print("var HM"+autoNameID+"_"+level+" =");
		for (ValueOntology childValueOnto: instanceOnto.getChildValueOntos()){
			out.print("\" <br/>"+spaceStr+"&nbsp;&nbsp;");
			if (childValueOnto.isSelect()){
				out.println("<img border='0' src='"+selectImg+"'/> \" + ");
			}
			else{
				out.println("<img border='0' src='"+unselectImg+"'/>\" + ");
			}
			out.print("\""+valueImagStr);
			out.println((GB2Big5Checker.check(request) ? GB2Big5.getInstance().get(childValueOnto.getName()):childValueOnto.getName())+"\" + ");
			for (SearchCondition searchCondition:childValueOnto.getSearchConditions()){
				if (!searchCondition.isEmptyValue()){
				  int endIndex=	searchCondition.getFieldValue().indexOf('\n');
				  String fieldValue = endIndex >=0 ? searchCondition.getFieldValue().substring(0,endIndex-1):searchCondition.getFieldValue();
				  out.println("\" <br/>"+spaceStr+"&nbsp;&nbsp;"+searchCondition.getOperatorObj().getName()+" "+fieldValue+"\" + ");
				}
			}
		}
		out.println("\" \"");
		out.println("</script>");
		out.println("<div id='EM"+autoNameID+"_"+level+"'>");
		out.println("</div>");
		for (InstanceOntology childInstanceOnto:instanceOnto.getChildInstanceOntos()){
			autoNameID++;
			writeInstance(request,childInstanceOnto,level+1,out);
		}
	}

	public ModelAndView writeLinkOntology(HttpServletRequest request){
		String nodeIdentity=(String)request.getSession().getAttribute("treeNode");
		TreeNode treeNode=TreeNodeUtil.getNode(nodeIdentity);
		if (null==treeNode) 
			throw new TcmRuntimeException("treeNode,程序有错!");			
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("treeNode",treeNode);
		model.put("GB2Big5",GB2Big5.getInstance());
		String ViewName = "dartcore/SecondQueryOntoInfo";
		return new ModelAndView(ViewName,model);
	}
}
