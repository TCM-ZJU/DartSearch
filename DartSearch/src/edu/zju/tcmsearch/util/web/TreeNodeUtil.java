package edu.zju.tcmsearch.util.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import edu.zju.tcmsearch.common.ApplicationContextHolder;
import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.common.service.OntoService;
import edu.zju.tcmsearch.common.service.impl.OntoServiceImpl;
import edu.zju.tcmsearch.exception.query.TcmRuntimeException;
import edu.zju.tcmsearch.web.form.query.TreeNode;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class TreeNodeUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(TreeNodeUtil.class);
	
	private static Map<String,TreeNode> nodeMap=new HashMap<String,TreeNode>();    
 
	
	/**
     * @return Returns the nodeMap.
     */
    public static Map<String, TreeNode> getNodeMap() {
        return nodeMap;
    }

    public static void putNode(TreeNode treeNode){
		//logger.debug("start put treenode: "+treeNode);
		if (null!=nodeMap.get(treeNode.getNodeIdentity())){
			return;
		}		
		nodeMap.put(treeNode.getNodeIdentity(),treeNode);
		//logger.debug("put treenode success! ");
	}
	
	public static TreeNode getNode(String nodeIdentity){
		TreeNode treeNode=nodeMap.get(nodeIdentity);  
        return treeNode;
	}

    


    public static TreeNode getTreeNode(DartOntology dartOntology,String nodeIdentity,TreeNode parentNode){
		String identity;
		if (null==nodeIdentity){
			identity=dartOntology.getIdentity();			
		}
		else{
			identity=nodeIdentity;
		}
		TreeNode treeNode=getNode(identity);
		if (null==treeNode){
			treeNode=new TreeNode(dartOntology,parentNode);
			putNode(treeNode);
		}
		return treeNode;
	}
	
	public static void putRootOnto(TreeNode treeNode, HttpSession session){
		session.setAttribute("rootOnto", treeNode.getRootNode().getOntology());
	}
	
	public static DartOntology getRootOnto(HttpSession session){
		return (DartOntology)session.getAttribute("rootOnto");
	}

	public static void setExpanding(TreeNode treeNode, boolean expanding,
			HttpSession session) {
		if (true == expanding) {
			addExpanding(treeNode, session);
		} else if (false == expanding) {
			removeExpanding(treeNode, session);
		}
	}


	public static void addExpanding(TreeNode treeNode, HttpSession session) {
		List<String> expandingNodes = getExpandingNodesFromSession(session);
		if (null==treeNode){
			return;
		}
		 logger.debug("expanding nodes:"+expandingNodes);
		 logger.debug("treeNode: "+treeNode);
		if (!isExpanding(treeNode, session)) {
			expandingNodes.add(treeNode.getNodeIdentity());
		}
	}

	/**
	 * true表示有东西被remove了
	 * 
	 * @param domainCode
	 * @param session
	 * @return
	 */
	public static boolean removeExpanding(TreeNode treeNode,
			HttpSession session) {
		List<String> expandingNodes = getExpandingNodesFromSession(session);
		for (String nodeIdentity:expandingNodes){
			if (nodeIdentity.equals(treeNode.getNodeIdentity())) {
				expandingNodes.remove(nodeIdentity);
				return true;
			}
		}
		return false;
	}

	public static boolean isExpanding(TreeNode treeNode, HttpSession session) {
		List<String> expandingNodes = getExpandingNodesFromSession(session);
		for (String nodeIdentity:expandingNodes){
			if (nodeIdentity.equals(treeNode.getNodeIdentity())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * expandingNodes中存放哪些node是需要展开的
	 * 这个node中仅存放String类型的ID,从而使HttpSession中的内容减到最少
	 * 
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getExpandingNodesFromSession(HttpSession session) {
		List<String>  expandingNodes = (List<String> ) session.getAttribute("expandingNodes");
		if (null == expandingNodes) {
			expandingNodes = new ArrayList<String> ();
			session.setAttribute("expandingNodes", expandingNodes);
		}
		return expandingNodes;
	}
	
	public static void clearExpanding(HttpSession session){
		getExpandingNodesFromSession(session).clear();
	}


    
}
