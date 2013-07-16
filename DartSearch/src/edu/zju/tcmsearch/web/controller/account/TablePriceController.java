package edu.zju.tcmsearch.web.controller.account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import edu.zju.tcmsearch.common.service.OntoService;
import edu.zju.tcmsearch.dao.secure.TablePriceDao;
import edu.zju.tcmsearch.web.form.secure.TablePriceForm;

public class TablePriceController extends SimpleFormController {
	
	private OntoService ontoService;
	
	private TablePriceDao dao;
	
	public void setOntoService(OntoService ontoService){
		this.ontoService = ontoService;
	}
	
	public void setTablePriceDao(TablePriceDao dao){
		this.dao = dao;
	}
	
	protected Object formBackingObject(HttpServletRequest request)throws Exception {
		TablePriceForm form = new TablePriceForm(ontoService);
		form.initialize(dao.getAllTablePrice());
		return form;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, 
            BindException errors)throws Exception {
		TablePriceForm form = (TablePriceForm)command;
		dao.update(form.getPriceSet());
		return new ModelAndView(new RedirectView(request.getContextPath()+getSuccessView()));
	}
	
}
