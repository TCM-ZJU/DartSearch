package edu.zju.tcmsearch.web.controller.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import edu.zju.tcmsearch.common.service.OntoService;
import edu.zju.tcmsearch.dao.Expression;
import edu.zju.tcmsearch.dao.Operator;
import edu.zju.tcmsearch.dao.impl.secure.TableAccessPrivDao;
import edu.zju.tcmsearch.dao.secure.IAccountDao;
import edu.zju.tcmsearch.secure.TableNameLocalization;
import edu.zju.tcmsearch.secure.domain.account.Account;
import edu.zju.tcmsearch.web.form.secure.TableAccessAssigmentForm;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;


public class TableAccessAssignmentController extends SimpleFormController {

	private OntoService ontoService;
	private TableAccessPrivDao tableAccessPrivDao;
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)throws Exception {
		TableAccessAssigmentForm form = new TableAccessAssigmentForm(ontoService);
		int accountId = Integer.parseInt(request.getParameter("accountId"));
		form.initialize(tableAccessPrivDao.getRoleTableAccessPrivileges(accountId),accountId);
		return form;
	}
	protected  Map referenceData(HttpServletRequest request){
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("chineseTableName",tableNameLocal);
		return model;
	}
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			                        HttpServletResponse response, Object command, 
			                        BindException errors)throws Exception {
		TableAccessAssigmentForm form = (TableAccessAssigmentForm)command;
   	    int accountId = Integer.parseInt(request.getParameter("accountId"));
		tableAccessPrivDao.updateRoleTableAccessPrivileges(form.getTableAccessPrivilege(),accountId);
		List<Account> accs = accountDAO.load(new Expression("accid",Operator.Equal,""+accountId),null);
		if(accs.size()==1){
			Account acc = accs.get(0);
			acc.unsetNewTable();
			accountDAO.updateField(accountId,"event",acc.getEvent());
		}
		return new ModelAndView(new RedirectView(request.getContextPath()+getSuccessView()));
	}
	
	public void setOntoService(OntoService service){
		this.ontoService = service;
	}
	
	public void setTableAccessPrivilegeDao(TableAccessPrivDao dao){
		this.tableAccessPrivDao = dao;
	}
	
	private TableNameLocalization tableNameLocal;
	
	private IAccountDao accountDAO;
	
	public void setTableNameLocalization(TableNameLocalization tableNameLocal){
		this.tableNameLocal = tableNameLocal;
	}	
	
	public void setAccountDAO(IAccountDao accountDao) {
		this.accountDAO = accountDao;
	}
	
	
}
