package edu.zju.tcmsearch.web.controller.i18n;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import edu.zju.tcmsearch.secure.TableNameLocalization;
import edu.zju.tcmsearch.secure.domain.account.Account;
import edu.zju.tcmsearch.util.web.SimplePager;
import edu.zju.tcmsearch.web.form.i18n.TableNameForm;


public class ChineseTableNameController extends SimpleFormController {
	
	protected  ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){
		TableNameForm form = (TableNameForm)command;
        tableNameLocal.add(form.getKey(),form.getValueList());
        tableNameLocal.update();
		return new ModelAndView(new RedirectView(request.getContextPath()+getSuccessView()));
    }
	 
	protected Object formBackingObject(HttpServletRequest request) throws Exception{
		int pageID=0;
		try{
		pageID = Integer.parseInt(request.getParameter("pageID")); 
		}catch(Exception e){};
		
		String tableSpace = request.getParameter("tableSpace");
		SimplePager pager;
		if(null==tableSpace)
			pager = new SimplePager<String>(tableNameLocal.keySet(),this.pageSize);
		else
			pager = new SimplePager<String>(tableNameLocal.keySet(tableSpace),this.pageSize);
		TableNameForm form = new TableNameForm(tableNameLocal,pager.getPage(pageID));
		form.savePageId(pageID);
		form.saveNextPageId(pager.nextPage(pageID));
		form.savePriorPageId(pager.priorPage(pageID));
		form.savePageCount(pager.pageCount());
		return form;
	}
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Account account =(Account) request.getSession().getAttribute(ACCOUNTEDITOR_SAVE_ACCOUNT_KEY);
		int accountId = null==account ? 0:account.getId();
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("isSubmit","false");
		model.put("accountId",accountId);
		return model;
	}
	
	private TableNameLocalization tableNameLocal;
	private int pageSize = 20;
	
	public void setTableNameLocalization(TableNameLocalization tableNameLocal){
		this.tableNameLocal = tableNameLocal;
	}
	public void setPageSize(int size){
		this.pageSize = size;
	}
	
	protected final String ACCOUNTEDITOR_SAVE_ACCOUNT_KEY = "ACCOUNTEDITOR_SAVE_ACCOUNT_KEY";

}
