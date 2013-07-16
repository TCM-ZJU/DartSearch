package edu.zju.tcmsearch.web.controller.query;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.RequestUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.common.service.OntoService;
import edu.zju.tcmsearch.exception.query.TcmRuntimeException;
import edu.zju.tcmsearch.util.GBK2Big5.GB2Big5;
import edu.zju.tcmsearch.util.dartcore.DataConvertor;
import edu.zju.tcmsearch.util.web.GB2Big5Checker;
import edu.zju.tcmsearch.util.web.TreeNodeUtil;
import edu.zju.tcmsearch.web.form.query.TreeNode;

public class NavigatorController implements Controller {
    private String rootImg;

    private String leafImg;

    private String nodeImg;

    private String css;

    private String navPage;

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(NavigatorController.class);

    private OntoService ontoService;

    public OntoService getOntoService() {
        return ontoService;
    }

    public void setOntoService(OntoService ontoService) {
        this.ontoService = ontoService;
    }

    private Integer retrieveValue(HttpServletRequest request,String valueStr) throws ServletRequestBindingException {
        Integer value = null;
        value = RequestUtils.getIntParameter(request, valueStr);
        if (null == value) {
            value = (Integer) request.getSession().getAttribute(valueStr);
        } else {
            request.getSession().setAttribute(valueStr, value);
        }

        return value;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String nodeIdentity = request.getParameter("nodeIdentity");
        String strExpanding = request.getParameter("expanding");
        Integer level = retrieveValue(request, "level");
        Integer metaCategoryId = retrieveValue(request,"metaCategoryId");
        if (null != nodeIdentity) {
            logger.debug("node identity is " + nodeIdentity);
            TreeNode curNode = TreeNodeUtil.getNode(nodeIdentity);
            if (curNode == null) {
                throw new TcmRuntimeException("The node with identity "
                        + nodeIdentity
                        + " is null, and can not be retrieve from hashMap");
            }
            logger.debug("curNode is " + curNode);
            if (null != strExpanding) {
                TreeNodeUtil.setExpanding(curNode, DataConvertor
                        .toBoolean(strExpanding), request.getSession());
            }
        }
        List<DartOntology> listOntologies;

        if ((null == level || null == metaCategoryId) || (level==0 && metaCategoryId==0)) {
            listOntologies = getOntoService().getBaseOntologies();
        } else {
            listOntologies = getOntoService().getCateOntologies(level,
                    metaCategoryId);
        }
        writeTree(request, response, listOntologies);
        return null;
    }

    private void writeTree(HttpServletRequest request,
            HttpServletResponse response, List<DartOntology> listOntologies)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\" src=\""+request.getContextPath()+"/JS/dartie.js\"></script>");        
        out.println("<div class='" + getCss() + "'>");
        out.println("<br><div class='ms-pagetitle' align='center'>概念词关联导航</div><br/>");
        out.println("<div class='ms-pagetitle' align='center'><a href='#' onclick="+"openWin('"+request.getContextPath()+"/ClassViewer.jsp',300,300);>语义浏览</a></div><br/>");
        for (DartOntology dartOntology : listOntologies) {
            TreeNode treeNode = TreeNodeUtil.getTreeNode(dartOntology,
                    dartOntology.getIdentity(), null);// 第一层，没有父亲，它的identity也就是对应本体的identity
            // TreeNodeUtil.putNode(treeNode);
            writeNode(out, treeNode, request, 0);
        }
        out.println("</div>");
    }

    private void writeNode(PrintWriter out, TreeNode treeNode,
            HttpServletRequest request, int level) {
        logger.debug("current node is " + treeNode);
        TreeNodeUtil.putNode(treeNode);
        for (int i = 0; i < level; i++) {
            out.println("&nbsp;&nbsp;");
        }
        StringBuilder paramSb = new StringBuilder();
        try{
            paramSb.append("nodeIdentity=" + URLEncoder.encode(treeNode.getNodeIdentity(),"UTF-8"));
            paramSb.append("&ontoIdentity=" + URLEncoder.encode(treeNode.getOntoIdentity(),"UTF-8"));
            }catch(UnsupportedEncodingException e){
                paramSb.append("nodeIdentity=" + treeNode.getNodeIdentity());
                paramSb.append("&ontoIdentity=" + treeNode.getOntoIdentity());
            }
        String imgTreeStr;
        StringBuilder paramNodeSb = new StringBuilder(paramSb);
        
        /*
         * write expanding tree node(s)
         * 
        if (TreeNodeUtil.isExpanding(treeNode, request.getSession())) {
            paramNodeSb.append("&expanding=false");
            imgTreeStr = "treeminus.gif";
        } else {
            paramNodeSb.append("&expanding=true");
            imgTreeStr = "treeplus.gif";
        }
        if (!treeNode.isLeaf()) {
            String imgLink = "<a href='" + navPage + "?"
                    + paramNodeSb.toString() + "'><img border='0' src='images/"
                    + imgTreeStr + "'></a>";
            // logger.debug("img link is "+imgLink);
            out.println(imgLink);
        }
        String imgNodeStr;
        if (level == 0) {
            imgNodeStr = rootImg;
        }
        if (treeNode.isLeaf()) {
            imgNodeStr = leafImg;
        } else {
            imgNodeStr = nodeImg;
        }
        String textLink = "<a href='" + navPage + "?" + paramSb.toString()
                + "'><img  border='0' src='" + imgNodeStr + "'/>"
                + treeNode.getName() + "</a>";
        // logger.debug("text link is "+textLink);
        out.println(textLink);
        out.println("<br>");
        if (treeNode.isLeaf()) {
            return;
        }
        if (!TreeNodeUtil.isExpanding(treeNode, request.getSession())) {
            return;
        }
        for (TreeNode childNode : treeNode.getChildNodes()) {
            writeNode(out, childNode, request, level + 1);
        }
  
        */
        
        String textLink = "<a href='" + navPage + "?" + paramSb.toString()
                          + "'><img  border='0' src='" + leafImg + "'/>"
                          + (GB2Big5Checker.check(request) ? GB2Big5.getInstance().get(treeNode.getName()):treeNode.getName()) + "</a>";
        out.println(textLink);
        out.println("<br>");
        
    }

    public String getLeafImg() {
        return leafImg;
    }

    public void setLeafImg(String leafImg) {
        this.leafImg = leafImg;
    }

    public String getNodeImg() {
        return nodeImg;
    }

    public void setNodeImg(String nodeImg) {
        this.nodeImg = nodeImg;
    }

    public String getRootImg() {
        return rootImg;
    }

    public void setRootImg(String rootImg) {
        this.rootImg = rootImg;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getNavPage() {
        return navPage;
    }

    public void setNavPage(String navPage) {
        this.navPage = navPage;
    }
}
