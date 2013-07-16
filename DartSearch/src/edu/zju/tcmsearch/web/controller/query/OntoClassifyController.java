/*
 * Created on 2005-11-24
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.web.controller.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import edu.zju.tcmsearch.common.TcmConstants;
import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.common.service.OntoCategoryService;
import edu.zju.tcmsearch.common.service.OntoService;
import edu.zju.tcmsearch.dao.meta.MetaCategoryDAO;
import edu.zju.tcmsearch.query.meta.MetaCategoryData;
import edu.zju.tcmsearch.util.web.MultiLinguaUtil;
import edu.zju.tcmsearch.web.form.query.OntoClassifyForm;

public class OntoClassifyController  extends SimpleFormController{
    
    private OntoService ontoService;
    private OntoCategoryService ontoCategoryService;
    private MetaCategoryDAO metaCategoryDAO;
    private String warnView;
    private String welcomeView;



    /**
     * @return Returns the welcomeView.
     */
    public String getWelcomeView() {
        return welcomeView;
    }

    /**
     * @param welcomeView The welcomeView to set.
     */
    public void setWelcomeView(String welcomeView) {
        this.welcomeView = welcomeView;
    }

    /**
     * @return Returns the metaCategoryDAO.
     */
    public MetaCategoryDAO getMetaCategoryDAO() {
        return metaCategoryDAO;
    }

    /**
     * @param metaCategoryDAO
     *            The metaCategoryDAO to set.
     */
    public void setMetaCategoryDAO(MetaCategoryDAO metaCategoryDAO) {
        this.metaCategoryDAO = metaCategoryDAO;
    }

    /**
     * @return Returns the warnView.
     */
    public String getWarnView() {
        return warnView;
    }

    /**
     * @param warnView
     *            The warnView to set.
     */
    public void setWarnView(String warnView) {
        this.warnView = warnView;
    }

    /**
     * @return Returns the ontoCategoryService.
     */
    public OntoCategoryService getOntoCategoryService() {
        return ontoCategoryService;
    }

    /**
     * @param ontoCategoryService
     *            The ontoCategoryService to set.
     */
    public void setOntoCategoryService(OntoCategoryService ontoCategoryService) {
        this.ontoCategoryService = ontoCategoryService;
    }

    /**
     * @return Returns the ontoService.
     */
    public OntoService getOntoService() {
        return ontoService;
    }

    /**
     * @param ontoService
     *            The ontoService to set.
     */
    public void setOntoService(OntoService ontoService) {
        this.ontoService = ontoService;
    }



    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object,
     *      org.springframework.validation.BindException)
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        OntoClassifyForm ontoClassifyForm=(OntoClassifyForm)command;
        Integer level=RequestUtils.getIntParameter(request,"level");
        Integer metaCategoryId=RequestUtils.getIntParameter(request,"metaCategoryId");
        getOntoCategoryService().updateOntoClassify(ontoClassifyForm.getOntoClassifyInfos(),level, metaCategoryId);
        return super.onSubmit(request, response, command, errors);
    }

 



    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Integer level=RequestUtils.getIntParameter(request,"level");
        Integer metaCategoryId=RequestUtils.getIntParameter(request,"metaCategoryId");
        Map model=new HashMap();
        model.put("level",level);
        model.put("metaCategoryId",metaCategoryId);
        if (null!=level && null!=metaCategoryId){
            MetaCategoryData metaCategoryData=metaCategoryDAO.getCategoryById(level,metaCategoryId);
            model.put("metaCategoryData",metaCategoryData);
        }
        return model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Assert.notNull(metaCategoryDAO);
        Assert.notNull(ontoService);
        Assert.notNull(ontoCategoryService);
        Integer level=RequestUtils.getIntParameter(request,"level");
        Integer metaCategoryId=RequestUtils.getIntParameter(request,"metaCategoryId");

        OntoClassifyForm ontoClassifyForm=new OntoClassifyForm();
        if (null==level || null==metaCategoryId || !TcmConstants.isLeafCategory(level)){
            return ontoClassifyForm;
        }
        MetaCategoryData mtData=metaCategoryDAO.getCategoryById(level,metaCategoryId);
        List<DartOntology> dartOntoList=getOntoService().getBaseOntologies();
        
        ontoClassifyForm.setLength(dartOntoList.size());
        int i=0;
        for (DartOntology dartOntology: dartOntoList){
            ontoClassifyForm.setOntology(dartOntology,i);
            if (null!=level && null!=metaCategoryId){
              boolean select=metaCategoryDAO.existClassification(mtData ,dartOntology);
              ontoClassifyForm.setSelect(select,i);
            }
            i++;
        }        
        return ontoClassifyForm;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#showForm(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.validation.BindException, java.util.Map)
     */
    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors, Map controlModel) throws Exception {
        Integer level=RequestUtils.getIntParameter(request,"level");
        Integer metaCategoryId=RequestUtils.getIntParameter(request,"metaCategoryId");
         if (null==level || null==metaCategoryId){
            return new ModelAndView(getWelcomeView());
        }
        else if (!TcmConstants.isLeafCategory(level)){
            //return new ModelAndView(getWarnView());
        	//当选择非叶子节点时进入分类目录修改页面
        	RedirectView rv = new RedirectView("ontoCategoryEdit.cla?level="+level.intValue()+"&metaCategoryId="+metaCategoryId.intValue());
        	return new ModelAndView(rv);
        }
        else{
            return super.showForm(request,response,errors,controlModel);
        }     
    }
    


}
