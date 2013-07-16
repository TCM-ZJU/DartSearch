package edu.zju.tcmsearch.common.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.edu.zju.dart.core.ontology.IOntologySchema;
import cn.edu.zju.dart.core.utils.DartCoreSettings;

import com.hp.hpl.jena.rdf.model.Resource;

import edu.zju.tcmsearch.common.OntologyMaps;
import edu.zju.tcmsearch.common.domain.DartOntoCategory;
import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.common.service.OntoService;
import edu.zju.tcmsearch.dao.meta.MetaCategoryDAO;
import edu.zju.tcmsearch.util.web.TreeNodeUtil;
import edu.zju.tcmsearch.web.form.query.TreeNode;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class OntoServiceImpl implements OntoService{
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(OntoServiceImpl.class);

	private IOntologySchema ontologySchema;
	private DartCoreSettings dartCoreSettingInstance;
	private boolean lazyLoad;
    
    private List<String> excludeOntos=new ArrayList<String>();
    
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

    /**
	 * 采用init方法,而不用构造函数,因为只有构造以后才能知道lazyload属性
	 *
	 */
	public void initialize(){
		if (!isLazyLoad()){
			dartCoreSettingInstance=DartCoreSettings.getInstance();
			ontologySchema=dartCoreSettingInstance.getDartOntoSchema();
            initOntoMap();
		}
	}
	
	/**
	 * 因为map有线程安全问题,因此单独用一个ontogies来存放最基本的几个本本体
	 */
	private List<DartOntology> dartOntologies=new ArrayList<DartOntology>();
	
	public DartCoreSettings getDartCoreSettings(){
		return dartCoreSettingInstance;
	}
	public IOntologySchema getOntologySchema() {
		if (isLazyLoad() && null==ontologySchema){
			ontologySchema=DartCoreSettings.getInstance().getDartOntoSchema();
		}
		return ontologySchema;
	}
	
	private void initOntoMap(){
		//ontologySchema=DartCoreSettings.getInstance().getDartOntoSchema();
		Collection<Resource> ontologies = getOntologySchema().getOntoClasses();
		for (Resource ontology:ontologies){
            //logger.debug("onto URI is "+ontology.getURI());
            if (excludeOntos.contains(ontology.getLocalName())){
                continue;
            }
			DartOntology dartOntology=new DartOntology(getOntologySchema(),ontology);
			OntologyMaps.putInstance(dartOntology);
            
            dartOntologies.add(dartOntology);
            initNodeMap();
//            TreeNode treeNode = new TreeNode(dartOntology,null); // 第一层，没有父亲，它的identity也就是对应本体的identity
//            TreeNodeUtil.putNode(treeNode);
		}
	}
	
    public  void initNodeMap() {
        List<DartOntology> baseOntologies = getBaseOntologies();

        for (DartOntology dartOntology : baseOntologies) {
            TreeNode treeNode = TreeNodeUtil.getTreeNode(dartOntology,
                    dartOntology.getIdentity(), null);// 第一层，没有父亲，它的identity也就是对应本体的identity
            TreeNodeUtil.putNode(treeNode);
            navigateNode(treeNode);           
        }
    }

    public static void navigateNode(TreeNode treeNode) {
        for (TreeNode childNode : treeNode.getChildNodes()) {
            TreeNodeUtil.putNode(childNode);
            navigateNode(childNode);            
        }
    }    


    private boolean isEmptyOntology(){
		return OntologyMaps.isEmptyInstance();
	}

	public DartOntology getInstanceOntology(String identify) {
		if (isEmptyOntology()){
			initOntoMap();
		}
		return OntologyMaps.getInstance(identify);
	}

	/**
	 * 碰到Map的线程安全问题,因此把ontology单独copy出来
	 */
	public List<DartOntology> getBaseOntologies() {
//        if (dartOntologies.size()==0){
//            initOntoMap();
//        }
		return dartOntologies;
	}
    

	public Map<String, DartOntology> getOntologyMap() {
		if (isEmptyOntology()){
			initOntoMap();
		}
		return OntologyMaps.getInstanceMap();
	}

	public DartOntology getValueOntology(String identity) {
		if (isEmptyOntology()){
			initOntoMap();
		}
		return OntologyMaps.getValue(identity);
	}

	public boolean isLazyLoad() {
		return lazyLoad;
	}

	public void setLazyLoad(boolean lazyLoad) {
		this.lazyLoad = lazyLoad;
	}



    /**
     * @return Returns the excludeOntos.
     */
    public List<String> getExcludeOntos() {
        return excludeOntos;
    }

    /**
     * @param excludeOntos The excludeOntos to set.
     */
    public void setExcludeOntos(List<String> excludeOntos) {
        this.excludeOntos = excludeOntos;
    }

    public List<DartOntology> getCateOntologies(int level, int cid) {
        List<String> ontoURIs=getMetaCategoryDAO().getClassifiedOntoURI(level,cid);
        Set<DartOntology> cateOntologies=new HashSet<DartOntology>();
        for (String ontoURI:ontoURIs){
            logger.debug("ontoURI is: "+ ontoURI);
            DartOntology curOnto= OntologyMaps.getValue(ontoURI);
            logger.debug("curOnto is "+curOnto);
            if (null!=curOnto){
                cateOntologies.add(curOnto);
            }
        }
       
        //把set的东西放到List里        
        List<DartOntology> cateOntologyList=new ArrayList<DartOntology>();
        cateOntologyList.addAll(cateOntologies);
        Collections.sort(cateOntologyList);
        
        return cateOntologyList;
    }

}
