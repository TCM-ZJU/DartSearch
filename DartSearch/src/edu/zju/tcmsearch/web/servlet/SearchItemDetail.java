package edu.zju.tcmsearch.web.servlet;

/**
 * this servlet is used to handle vml graph xmlhttprequest.
 * @author zhm 
 * 
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import edu.zju.tcmsearch.lucene.index.DbContentRetriever;
import edu.zju.tcmsearch.lucene.index.data.DbRecData;
import edu.zju.tcmsearch.lucene.index.data.FieldValue;
import edu.zju.tcmsearch.util.GBK2Big5.GB2Big5;


public class SearchItemDetail extends HttpServlet implements Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8905747467971949880L;
	private static final Logger logger = Logger
			.getLogger(SearchItemDetail.class.getName());

	private DbContentRetriever dbContentRetriever;
	private String successView;

	public SearchItemDetail() {
		super();

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String tableIdentity = req.getParameter("tableIdentity");
		String primaryKey = req.getParameter("primaryKey");
		logger.debug(tableIdentity + " , " + primaryKey);
		DbRecData dbRecData = getDbContentRetriever().getTableContentByKey(
				tableIdentity, primaryKey);

		resp.setContentType("text/xml; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.write(ListToXml(dbRecData.getFieldValueList(), tableIdentity,""));
		out.flush();
		out.close();
	}

	private String ListToXml(List<FieldValue> list, String tableName,String ononlogyName) {

		/*
		 * <P align="center" class="ms-pagetitle">查看表${dbRecData.tableInfo.tableIdentity}数据</P>
		 * 
		 * <table align="center" class="std_Table" width="90%"> #foreach
		 * ($fieldValue in ${dbRecData.fieldValueList})
		 * <tr  class="Table_grayitem"> <td width="20%">${fieldValue.columnInfo.ontologyName}</td>
		 * <td width="80%">${fieldValue.value}</td> </tr> #end </table>
		 */

		List<FieldValue> indexList = list;

		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		root.addAttribute("tableName", tableName);
		root.addAttribute("ontologyName", ononlogyName);
		if (null != indexList) {
			for (int i = 0; i < indexList.size(); i++) {
				FieldValue fieldValue = indexList.get(i);
				Element item = root.addElement("item")
						.addAttribute("propertyName",fieldValue.getColumnInfo().getOntologyName())
						.addAttribute("propertyValue", fieldValue.getValue());
			}
		}
		//logger.debug(document.asXML());
		return document.asXML();
	}

	public DbContentRetriever getDbContentRetriever() {
		return dbContentRetriever;
	}

	public void setDbContentRetriever(DbContentRetriever dbContentRetriever) {
		this.dbContentRetriever = dbContentRetriever;
	}

	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		// TODO Auto-generated method stub
		String tableIdentity = arg0.getParameter("tableIdentity");
		String primaryKey = arg0.getParameter("primaryKey");
		logger.debug(tableIdentity + " , " + primaryKey);
		DbRecData dbRecData = getDbContentRetriever().getTableContentByKey(
				tableIdentity, primaryKey);
		//System.out.println(dbRecData.getOntoIdentityStr());
		//System.out.println(dbRecData.getOntoNameStr());

		arg1.setContentType("text/xml; charset=UTF-8");
		PrintWriter out = arg1.getWriter();
		out.write(ListToXml(dbRecData.getFieldValueList(), tableIdentity,dbRecData.getOntoNameStr()));
		out.flush();
		out.close();
		Map model = new HashMap();
		model.put("dbRecData", dbRecData);
		model.put("GB2Big5", GB2Big5.getInstance());
		return new ModelAndView(getSuccessView(), model);
	}

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

}
