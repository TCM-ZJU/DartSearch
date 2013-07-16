package edu.zju.tcmsearch.web.controller.account;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import edu.zju.tcmsearch.common.service.OntoService;
import edu.zju.tcmsearch.dao.Expression;
import edu.zju.tcmsearch.dao.Operator;
import edu.zju.tcmsearch.dao.impl.secure.OntologyAccessPrivDao;
import edu.zju.tcmsearch.dao.secure.IAccountDao;
import edu.zju.tcmsearch.secure.domain.account.Account;
import edu.zju.tcmsearch.web.form.secure.OntologyAccessAssigmentForm;

public class OntologyAccessAssignmentController extends SimpleFormController {
	private OntoService ontoService;
	private OntologyAccessPrivDao ontologyAccessPrivDao;
	private IAccountDao accountDAO;
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)throws Exception {
		OntologyAccessAssigmentForm form = new OntologyAccessAssigmentForm(ontoService);
		int accountId = Integer.parseInt(request.getParameter("accountId"));
		form.initialize(ontologyAccessPrivDao.getPrivilege(accountId),accountId);
		return form;
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			                        HttpServletResponse response, Object command, 
			                        BindException errors)throws Exception {
		OntologyAccessAssigmentForm form = (OntologyAccessAssigmentForm)command;
   	    int accountId = Integer.parseInt(request.getParameter("accountId"));
   	    ontologyAccessPrivDao.updatePrivilege(form.getPrivilege(),accountId);
		List<Account> accs = accountDAO.load(new Expression("accid",Operator.Equal,""+accountId),null);
		if(accs.size()==1){
			Account acc = accs.get(0);
			acc.unsetNewOntology();
			accountDAO.updateField(accountId,"event",acc.getEvent());
		}
   	    
 	    return new ModelAndView(new RedirectView(request.getContextPath()+getSuccessView()));
	}
	
	public void setOntoService(OntoService service){
		this.ontoService = service;
	}
	
	public void setOntologyAccessPrivilegeDao(OntologyAccessPrivDao dao){
		this.ontologyAccessPrivDao = dao;
	}
	
	public void setAccountDAO(IAccountDao accountDao) {
		this.accountDAO = accountDao;
	}	

}
