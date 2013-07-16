/*
 * Created on 2005-7-25
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.web.controller.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import edu.zju.tcmsearch.exception.query.TcmRuntimeException;
import edu.zju.tcmsearch.query.domain.QueryResult;
import edu.zju.tcmsearch.util.GBK2Big5.GB2Big5;
import edu.zju.tcmsearch.util.web.TreeNodeUtil;

public class SearchResultNavController implements Controller{

    @SuppressWarnings("unchecked")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String strPageNo=request.getParameter("pageNo");
        if (null==strPageNo){
            throw new TcmRuntimeException("取到的pageNo是空的,无法分页显示结果");
        }
       
        int pageNo=Integer.parseInt(strPageNo);
        QueryResult queryResult=(QueryResult)request.getSession().getAttribute("sessionQueryResult");
        if (null==queryResult){
            throw new TcmRuntimeException("HttpSession里找不到查询结果对象，无法分页显示结果");
        }
        String style=request.getParameter("style");
        String viewName="";
        if ("list".equals(style)){
            viewName="dartcore/resultList";
        }
        else if ("column".equals(style)){
            viewName="dartcore/resultColumn";
        }
        Map model=new HashMap();
        //List queryReultList=queryResult.getPageList().getPage(pageNo);
        
        model.put("pageNo",pageNo); 
        model.put("queryResult",queryResult);
        String treeNode=(String)request.getSession().getAttribute("treeNode");
		model.put("treeNode",TreeNodeUtil.getNode(treeNode));
		model.put("GB2Big5",GB2Big5.getInstance());
        return new ModelAndView(viewName,model);
        
    }
}
