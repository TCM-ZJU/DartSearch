/*
 * Created on 2005-11-17
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.web.controller.query;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import edu.zju.tcmsearch.dao.meta.MetaCategoryDAO;
import edu.zju.tcmsearch.query.meta.MetaCategoryData;
import edu.zju.tcmsearch.util.dartcore.DataConvertor;
import edu.zju.tcmsearch.util.web.CateNodeUtil;

public class MetaCategoryController implements Controller{
    private String expandImg;
    private String encloseImg;
    private String navPage;
    private String css;
    
    private boolean showAllLink=false;

    
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(MetaCategoryController.class);



    private MetaCategoryDAO metaCategoryDAO;

   


    /**
     * @return Returns the metaCategoryDAO.
     */
    public MetaCategoryDAO getMetaCategoryDAO() {
        return metaCategoryDAO;
    }

    /**
     * @param metaCategoryDAO The metaCategoryDAO to set.
     */
    public void setMetaCategoryDAO(MetaCategoryDAO metaCategoryDAO) {
        this.metaCategoryDAO = metaCategoryDAO;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
        String cid = request.getParameter("cid");
        String strExpanding=request.getParameter("expanding");
        if (null != cid) {
            MetaCategoryData curMetaCategoryData=CateNodeUtil.getNode(cid);
            Assert.notNull(curMetaCategoryData);

            if (null!=strExpanding){              
              CateNodeUtil.setExpanding(curMetaCategoryData,DataConvertor.toBoolean(strExpanding),request.getSession());
            }
        }
        writeTree(request, response);
        return null;
    	
    	
    	
    }

    private void writeTree(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        List<MetaCategoryData> metaCateList=getMetaCategoryDAO().getCategory1();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<div class='"+getCss()+"'>");
        out.println("<br><div class='ms-pagetitle' align='center'>专题分类导航</div><br/>");
        if (isShowAllLink()){
        	//out.println("<div align='center'><a href='"+request.getContextPath()+"/ontoCategoryEdit.cla'>修改专题</a></div>");
            out.println("<br/><div align='center'><a href='"+navPage+"?level=0&metaCategoryId=0'>显示所有概念</a></div><br/>");
        }else{
        	out.println("<img  border='0' src='"+encloseImg+"'/>"+"<a href='"+request.getContextPath()+"/ontoCategoryEdit.cla?level=0&metaCategoryId=0'>修改一级专题</a></div>");
        }
        // logger.debug("list size "+codeList.size());
        for (MetaCategoryData metaCategoryData : metaCateList) {
              writeNode(out, metaCategoryData, request, 0);
        }
        out.println("</div>");
    }
    




    private void writeNode(PrintWriter out, MetaCategoryData metaCategoryData,
            HttpServletRequest request, int level) {
        CateNodeUtil.putNode(metaCategoryData);
        for (int i = 0; i < level; i++) {
            out.println("&nbsp;&nbsp;");
        }
        StringBuilder nodeParamSb = new StringBuilder();
        nodeParamSb.append("cid=" + metaCategoryData.getCid());
        
        StringBuilder txtParamSb=new StringBuilder();
        txtParamSb.append("level=" + metaCategoryData.getLevel());
        txtParamSb.append("&metaCategoryId=" + metaCategoryData.getId());
        
        //paramSb.append("&ontoIdentity=" + treeNode.getOntoIdentity());
        String imgTreeStr;
        StringBuilder paramNodeSb=new StringBuilder(nodeParamSb);
        if (CateNodeUtil.isExpanding(metaCategoryData,request.getSession())) {
            paramNodeSb.append("&expanding=false");
            imgTreeStr = "treeminus.gif";
        } else {
            paramNodeSb.append("&expanding=true");
            imgTreeStr = "treeplus.gif";
        }
        if (!metaCategoryData.isLeaf()) {               
            String imgLink="<a href='"+navPage+"?"+paramNodeSb.toString() + "'><img border='0' src='images/"
            + imgTreeStr + "'></a>";
            //logger.debug("img link is "+imgLink);
            out.println(imgLink);
        }
        String imgNodeStr;
        if (CateNodeUtil.isExpanding(metaCategoryData,request.getSession())){
            imgNodeStr=expandImg;
        }
        else{
            imgNodeStr=encloseImg;
        }
        

        String textLink="<a href='"+navPage+"?"+txtParamSb.toString()+"'><img  border='0' src='"+imgNodeStr+"'/>"+metaCategoryData.getDname()+"</a>";
        //logger.debug("text link is "+textLink);
        out.println(textLink);
        out.println("<br>");
        if (metaCategoryData.isLeaf()) {
            return;
        }
        if (!CateNodeUtil.isExpanding(metaCategoryData,request.getSession())) {
            return;
        }
        for (MetaCategoryData childCategoryData:metaCategoryData.getChildCategoryList()){
            writeNode(out, childCategoryData, request, level + 1);
        }
    }



    /**
     * @return Returns the encloseImg.
     */
    public String getEncloseImg() {
        return encloseImg;
    }

    /**
     * @param encloseImg The encloseImg to set.
     */
    public void setEncloseImg(String encloseImg) {
        this.encloseImg = encloseImg;
    }

    /**
     * @return Returns the expandImg.
     */
    public String getExpandImg() {
        return expandImg;
    }

    /**
     * @param expandImg The expandImg to set.
     */
    public void setExpandImg(String expandImg) {
        this.expandImg = expandImg;
    }

    /**
     * @return Returns the navPage.
     */
    public String getNavPage() {
        return navPage;
    }

    /**
     * @param navPage The navPage to set.
     */
    public void setNavPage(String navPage) {
        this.navPage = navPage;
    }

 

    /**
     * @return Returns the css.
     */
    public String getCss() {
        return css;
    }

    /**
     * @param css The css to set.
     */
    public void setCss(String css) {
        this.css = css;
    }

    /**
     * @return Returns the showAllLink.
     * 
     */
    public boolean isShowAllLink() {
        return showAllLink;
    }

    /**
     * @param showAllLink The showAllLink to set.
     */
    public void setShowAllLink(boolean showAllLink) {
        this.showAllLink = showAllLink;
    }
  
}
