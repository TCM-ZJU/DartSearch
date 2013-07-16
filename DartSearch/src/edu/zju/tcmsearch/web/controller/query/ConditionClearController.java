
package edu.zju.tcmsearch.web.controller.query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


import edu.zju.tcmsearch.query.domain.DartQuery;
import edu.zju.tcmsearch.query.domain.SelectRelationSet;
import edu.zju.tcmsearch.util.web.DartQueryUtil;

public class ConditionClearController implements Controller{

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        DartQuery dartQuery=DartQueryUtil.getFromSession(request.getSession());
        if(null!=dartQuery){
           dartQuery.closeQuery();
           DartQueryUtil.remove(request.getSession());
        }
        
        SelectRelationSet selectRelation= DartQueryUtil.getSelectRelationSetFromSession(request.getSession()); 
        if(null!=selectRelation){
        	selectRelation.clear();
        	DartQueryUtil.removeSelectRelationSetFromSession(request.getSession());
        }
        request.getSession().removeAttribute("queryResult");
        return new ModelAndView("redirect:index.htm");
    }
}
