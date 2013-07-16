package edu.zju.tcmsearch.web.controller.query;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import edu.zju.tcmsearch.common.service.OntoService;
import edu.zju.tcmsearch.exception.query.TcmRuntimeException;
import edu.zju.tcmsearch.query.domain.DartQuery;
import edu.zju.tcmsearch.query.domain.Operator;
import edu.zju.tcmsearch.query.domain.SelectRelationSet;
import edu.zju.tcmsearch.util.GBK2Big5.GB2Big5;
import edu.zju.tcmsearch.util.dartcore.EncodingUtil;
import edu.zju.tcmsearch.util.web.DartQueryUtil;
import edu.zju.tcmsearch.util.web.ParameterValueDecoder;
import edu.zju.tcmsearch.util.web.TreeNodeUtil;
import edu.zju.tcmsearch.web.form.query.SearchCondition;
import edu.zju.tcmsearch.web.form.query.SearchConditionForm;
import edu.zju.tcmsearch.web.form.query.TreeNode;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class OntologyInfoController extends SimpleFormController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(OntologyInfoController.class);

	private OntoService ontoService;

	public OntoService getOntoService() {
		return ontoService;
	}

	public void setOntoService(OntoService ontoService) {
		this.ontoService = ontoService;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		SearchConditionForm searchConditionForm = (SearchConditionForm) command;
        logger.debug(searchConditionForm.toString());
		searchConditionForm.addValueOntologies();

		/*
		SelectRelationSet selectRelationSet = DartQueryUtil
				.getSelectRelationSetFromSession(request.getSession());
		if (null == selectRelationSet) {
			throw new TcmRuntimeException("用户选择的表没有保存在session中,程序有错!");
		} else {
			
			 // 只有当本体中有属性被选中查询，才把数据表加入SelectRelationSet @ming
			 
			for (SearchCondition searcond : searchConditionForm
					.getSearchConditions()) {
				if (false == searcond.isEmptyCondition()) {
					selectRelationSet.addRelation(searchConditionForm
							.getSelectRelation());
					break;
				}
			}
		}*/
		/*
		 * 数据源，添加测试。用完移除，不该放这里 @ming

		logger.debug("Relation name size is "
				+ searchConditionForm.getSelectRelation().getRelationName()
						.size());
		for (String strRelation : searchConditionForm.getSelectRelation()
				.getRelationName()) {
			logger.debug("Relation name String is " + strRelation);
		}
		for (String boolRe : searchConditionForm.getSelectRelation()
				.getSelectSet()) {
			logger.debug("Relation bool checked is " + boolRe);
		}
		logger.debug("^^ " + selectRelationSet.toString());
        */
        
		/*
		String childNodeId = request.getParameter("childNodeId");
		String redirectName = null;
		if ("".equals(childNodeId) || null == childNodeId) {
			redirectName = "searchResult.htm?nodeIdentity="
					+ nodeIdentity;
		} else {
			// request里直接传中文参数出错,要修改encoding
			redirectName = "ontologyInfo.htm?nodeIdentity="
					+ childNodeId;
		}
		redirectName=EncodingUtil.getEngStr(redirectName);
		RedirectView rv = new RedirectView(redirectName);
		return new ModelAndView(rv);
		*/
		String childNodeId = request.getParameter("childNodeId");
		String redirectName = null;		
		if ("".equals(childNodeId) || null == childNodeId) {
			redirectName = "searchResult.htm?nodeIdentity="
					+ URLEncoder.encode(nodeIdentity,"UTF-8");
		} else {
			// request里直接传中文参数出错,要修改encoding
			redirectName = "ontologyInfo.htm?nodeIdentity="
					+ URLEncoder.encode(childNodeId,"UTF-8");
		}

		RedirectView rv = new RedirectView(redirectName);
		return new ModelAndView(rv);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		TreeNode treeNode = getTreeNodeFromReq(request);
		Map model = new HashMap();
		// model.put("ontology",treeNode.getOntology());
		model.put("operators", Operator.getMap());
		model.put("treeNode", treeNode);
		model.put("GB2Big5",GB2Big5.getInstance());
		return model;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		TreeNode treeNode = getTreeNodeFromReq(request);
		TreeNodeUtil.putRootOnto(treeNode, request.getSession());
		DartQuery dartQuery = DartQueryUtil
				.getFromSession(request.getSession());
		if (null == dartQuery
				|| dartQuery.getRootInstOntology().getOntology() != treeNode
						.getRootNode().getOntology()) {// 如果两者指向的不是同一个根本体,则需要将原先的查询条件清除
			dartQuery = new DartQuery(getOntoService().getOntologySchema(),
					treeNode);
			DartQueryUtil.putInSession(request.getSession(), dartQuery);
			/*
			 * 数据来源，添加 selectRelationSet到session,selectRelationSet包含用户选择的表 @ming
			 
			DartQueryUtil.removeSelectRelationSetFromSession(request
					.getSession());
			DartQueryUtil.putSelectRelationSetToSession(request.getSession(),
					new SelectRelationSet());
			*/
			
			/**
			 * 在每次新查询之前清除结果，使查询导航栏显示正确的信息 
			 */
			request.getSession().removeAttribute("sessionQueryResult");
		} else {
			dartQuery.addInstance(treeNode);
		}

		// DartOntology ontology=treeNode.getOntology();
		// logger.debug("ontology is "+ontology);

		SearchConditionForm searchConditionForm = new SearchConditionForm(
				treeNode, dartQuery);

		/*
		 * 数据源，添加测试。用完移除，不该放这里 @ming
		 
		logger.debug("Relation name size is "
				+ searchConditionForm.getSelectRelation().getRelationName()
						.size());
		for (String strRelation : searchConditionForm.getSelectRelation()
				.getRelationName()) {
			logger.debug("Relation name String is " + strRelation);
		}
		for (String boolRe : searchConditionForm.getSelectRelation()
				.getSelectSet()) {
			logger.debug("Relation bool checked is " + boolRe);
		}
        */
		
		return searchConditionForm;
	}

	private TreeNode getTreeNodeFromReq(HttpServletRequest request) {
		nodeIdentity = request.getParameter("nodeIdentity");
		logger.debug("nodeIdentity is " + nodeIdentity);
		if (null == nodeIdentity) {
			throw new TcmRuntimeException("传入的nodeIdentity是空值,程序有错!");
		}
		TreeNode treeNode = TreeNodeUtil.getNode(nodeIdentity);
		logger.debug("treeNode is " + treeNode);
		return treeNode;
	}

	private String nodeIdentity;

}
