package edu.zju.tcmsearch.web.controller.query;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import edu.zju.tcmsearch.secure.TableNameLocalization;
import edu.zju.tcmsearch.util.GBK2Big5.GB2Big5;
import edu.zju.tcmsearch.util.web.DartQueryUtil;
import edu.zju.tcmsearch.util.web.MultiLinguaUtil;
import edu.zju.tcmsearch.web.form.query.UsedRelationTable;

public class DataSourceController implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		UsedRelationTable usedRT=(UsedRelationTable)DartQueryUtil.getUsedRelation(request.getSession());

		Map model = new HashMap();
		if(null!=usedRT){
		   model.put("relationUsed" , usedRT);
		   model.put("style",request.getParameter("style"));
		   model.put("pageNo",request.getParameter("pageNo"));
		   model.put("GB2Big5",GB2Big5.getInstance());
		   model.put("chineseTableName",tableNameLocal);
		}
		
		String ViewName = "dartcore/datasource";
		return new ModelAndView(ViewName, model);
	}

	private TableNameLocalization tableNameLocal;
	
	public void setTableNameLocalization(TableNameLocalization tableNameLocal){
		this.tableNameLocal = tableNameLocal;
	}
}
