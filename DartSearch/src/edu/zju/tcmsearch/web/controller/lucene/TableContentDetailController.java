/*
 * Created on 2005-12-20
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.web.controller.lucene;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import edu.zju.tcmsearch.lucene.index.DbContentRetriever;
import edu.zju.tcmsearch.lucene.index.data.DbRecData;
import edu.zju.tcmsearch.util.GBK2Big5.GB2Big5;

public class TableContentDetailController implements Controller{
    private DbContentRetriever dbContentRetriever;
    private String successView;
    
    /**
     * @return Returns the successView.
     */
    public String getSuccessView() {
        return successView;
    }

    /**
     * @param successView The successView to set.
     */
    public void setSuccessView(String successView) {
        this.successView = successView;
    }

    /**
     * @return Returns the dbContentRetriever.
     */
    public DbContentRetriever getDbContentRetriever() {
        return dbContentRetriever;
    }

    /**
     * @param dbContentRetriever The dbContentRetriever to set.
     */
    public void setDbContentRetriever(DbContentRetriever dbContentRetriever) {
        this.dbContentRetriever = dbContentRetriever;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableIdentity=request.getParameter("tableIdentity");
        String primaryKey=request.getParameter("primaryKey");
        DbRecData dbRecData=getDbContentRetriever().getTableContentByKey(tableIdentity,primaryKey);
        Map model=new HashMap();
        model.put("dbRecData",dbRecData);
        model.put("GB2Big5",GB2Big5.getInstance());
        return new ModelAndView(getSuccessView(),model);
    }
    
}
