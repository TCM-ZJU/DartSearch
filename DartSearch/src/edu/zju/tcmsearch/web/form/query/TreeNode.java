package edu.zju.tcmsearch.web.form.query;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.util.web.TreeNodeUtil;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */

/**
 * 用来显示本体论的树状结构,由于DartOntology在系统中每个本体只保留一份， 而在树状显示时可能需要多份，因此建了这个数据库结构，用来专门显示树状
 */
public class TreeNode {
	private DartOntology ontology;

	private TreeNode parentNode;

	private List<TreeNode> childNodes;
	
	
	
	private int level;
	
	private boolean root;
	

	
	
	
	public TreeNode(DartOntology ontology,TreeNode parentNode){
		this.ontology=ontology;
		this.parentNode=parentNode;
	}
	
	public int getChildNodeCount(){
		return getChildNodes().size();
	}
	
	public int getValueNodeCount(){
		return getOntology().getValueProperties().size();
	}
	
	public List<TreeNode> getChildNodes() {
		if (null!=childNodes)
		  return childNodes;
		childNodes=new ArrayList<TreeNode>();
		for (DartOntology childOnto:this.ontology.getChildOntologies()){
			if (isLoopOnto(childOnto)){
				continue;
			}
			TreeNode childNode=new TreeNode(childOnto,this);
			TreeNodeUtil.putNode(childNode);
			childNodes.add(childNode);
		}
		return childNodes;
	}
	
	/**
	 * 循环取节点的根节点
	 * @return
	 */
	public TreeNode getRootNode(){
		TreeNode curNode=this;
		while (null!=curNode.getParentNode()){
			curNode=curNode.getParentNode();
		}
		return curNode;
	}
	
	public boolean isLeaf(){
		return getChildNodes().size()==0;
	}
	
	/**
	 * 判断节点有没有循环引用
	 * 如果当前节点或父节点有一个与这个本体相同，则存在循环
	 * @param childOnto
	 * @return
	 */
	private boolean isLoopOnto(DartOntology childOnto){
		TreeNode curNode=this;
		while (null!=curNode){
			if (curNode.getOntology().equals(childOnto)){
				return true;
			}
			curNode=curNode.getParentNode();
		}
		return false;
	}
	
	public String getName(){
		return this.ontology.getName();
	}


	public DartOntology getOntology() {
		return ontology;
	}

	public void setOntology(DartOntology ontology) {
		this.ontology = ontology;
	}

	public TreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(TreeNode parentNode) {
		this.parentNode = parentNode;
	}
	
	public String getOntoIdentity(){
		return this.ontology.getIdentity();
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * 唯一区别的身份只能是将它本身和所有的父结点全连起来
	 * @return
	 */
	public String getNodeIdentity(){
		String strIdentity=this.ontology.getIdentity();
		if (getParentNode()!=null){
			return getParentNode().getNodeIdentity()+"|"+strIdentity;
		}
		else{
			return strIdentity;
		}
	}

	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(" the following is Treenode information: ");
		sb.append(" ontology is: "+this.ontology);
		sb.append(" node identity is: "+getNodeIdentity());
		sb.append(" isRoot: "+isRoot());
		sb.append(" isLeaf: "+isLeaf());
		return sb.toString();
	}
	public String getOntoIdentityEncoded(){
		try{
		return URLEncoder.encode(getOntoIdentity(),"UTF-8");
		}catch(UnsupportedEncodingException e){
			return "ontologyIdentity Encoding Error";
		}
	}	
	public String getNodeIdentityEncoded(){
		try{
		return URLEncoder.encode(getNodeIdentity(),"UTF-8");
		}catch(UnsupportedEncodingException e){
			return "nodeIdentity Encoding Error";
		}
	}
}
