package edu.zju.tcmsearch.web.controller.query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import edu.zju.tcmsearch.dao.meta.MetaCategoryDAO;
import edu.zju.tcmsearch.query.meta.MetaCategoryData;
import edu.zju.tcmsearch.util.web.MultiLinguaUtil;
import edu.zju.tcmsearch.util.web.ParameterValueDecoder;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

public class OntoCategoryEditController implements Controller {

	private static Logger log= Logger.getLogger(OntoCategoryEditController.class);
    private MetaCategoryDAO metaCategoryDAO;
    private String warnView;

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



    private void dealCommand(int fatherLevel,int fatherId,String cmd,HttpServletRequest request)throws Exception{
    	int childId;
    	String newName;
    	if(cmd.compareTo("delCategory")==0)
    	{
    	  childId = RequestUtils.getIntParameter(request,"childId").intValue();
    	  this.metaCategoryDAO.removeCategory(fatherLevel+1,childId);
    	}
    	else if(cmd.compareTo("renCategory")==0)
    	{
    	  childId = RequestUtils.getIntParameter(request,"childId").intValue();
    	  newName = request.getParameter("newName");
    	  if(null==newName){
    		  log.debug("null newName from Request Parameter");
    	  }
    	  this.metaCategoryDAO.renameCategory(fatherLevel+1,childId,newName);
    	}
    	else if(cmd.compareTo("newCategory")==0)
    	{
      	  newName = request.getParameter("newName");
    	  if(null==newName){
    		  log.debug("null newName from Request Parameter");
    	  }
    	  this.metaCategoryDAO.addCategory(fatherLevel+1,fatherId,newName);
    	}
    }

	public ModelAndView handleRequest(HttpServletRequest request,HttpServletResponse reponse) throws Exception {
		// TODO Auto-generated method stub
        Assert.notNull(metaCategoryDAO);
        Integer level=RequestUtils.getIntParameter(request,"level");
        Integer metaCategoryId=RequestUtils.getIntParameter(request,"metaCategoryId");
        log.debug("level:"+level+" metaCategoryId"+metaCategoryId);
        if(null==level||null==metaCategoryId)
        	return new ModelAndView(getWarnView());
        String cmdWord = request.getParameter("cmd");
        if(null!=cmdWord)
           dealCommand(level.intValue(),metaCategoryId.intValue(),cmdWord,request);
        Map model = new HashMap();
        //显示一级专题
        if(level.intValue()==0){
            model.put("categoryList",metaCategoryDAO.getCategory1());
            return new ModelAndView("dartcore/OntoCategoryEidtTopLevel",model);
        }else{
            model.put("childCategory",metaCategoryDAO.getCategory2(metaCategoryId.intValue()));
            model.put("fatherCategory",metaCategoryDAO.getCategoryById(1,metaCategoryId.intValue()));
            return  new ModelAndView("dartcore/OntoCategoryEidt",model);        	
        }
        
	}

}
