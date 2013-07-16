package edu.zju.tcmsearch.common.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.HashCodeBuilder;

import cn.edu.zju.dart.core.ontology.IOntologySchema;
import cn.edu.zju.dart.core.ontology.model.IOntoClass;

import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import edu.zju.tcmsearch.common.ApplicationContextHolder;
import edu.zju.tcmsearch.common.OntologyMaps;
import edu.zju.tcmsearch.exception.query.TcmRuntimeException;
import edu.zju.tcmsearch.util.dartcore.OntoUriParseUtil;


/**
 * 浙江大学计算机学院CCNT实验室网格组
 * 基于DartGrid2的中医药综合查询系统
 * @author 谢骋超 
 * 2005年7月
 * 
 * 这个类是对本体论类的简单封装，使它更适合于最终用户使用
 * 同时增加这层转换之后,更加适应新的需求的变化。
 * 我可以在这个Domain Object里自由地增加自己需要的方法。
 * 
 * 它本身会有很多子属性，包括instanceProperties,valueProperties
 * 这两个子属性的类型也是DartOntology，因此它是一个类似递归的树状结构
 */

public class DartOntology implements Comparable{
	private Resource ontology;
	private IOntologySchema ontologySchema;
	private Resource ontoType;
	private List<DartOntology> instanceProperties;
	private List<DartOntology> valueProperties;
	private List<DartOntology> childOntologies;
	private Map<DartOntology,DartOntology> childOntoMap;//一个根据宾语可以找到谓语的map,由于查询中要用到谓语,因此必须有一个返到谓语的方法
    private IOntoClass ontoClass;

	private boolean instanceType=true;	



	public boolean isInstanceType() {
		return instanceType;
	}




	/**
     * @return Returns the ontoClass.
     */
    public IOntoClass getOntoClass() {
        return ontoClass;
    }



    public void setInstanceType(boolean instanceType) {
		this.instanceType = instanceType;
	}
    
    public String getCategory(){
        return getOntoClass().getCategory();
    }




	public DartOntology(IOntologySchema ontologySchema,Resource ontology){
		this.ontologySchema=ontologySchema;
		this.ontology=ontology;
        this.ontoClass=this.ontologySchema.getOntoClass(ontology);
	}

/**
 * 直接取到的是关联属性,要根据这个属性取去取对应的谓语，才是我们对应的instance
 * @return
 */
	public List<DartOntology> getChildOntologies() {
		if (null!=childOntologies){
			return childOntologies;
		}
		childOntologies=new ArrayList<DartOntology>();
		childOntoMap=new HashMap<DartOntology,DartOntology>();
		Collection<Resource> instanceProperyResources=this.ontologySchema.getInstancePropsByOnto(retrieveOntoType());
		for (Resource propResource:instanceProperyResources){
			Resource predResource=this.ontologySchema.getObjByInstanceProp(this.ontology,propResource);
			DartOntology dartOntology = createInstanceOnto(predResource);			
			childOntologies.add(dartOntology);
			childOntoMap.put(dartOntology,new DartOntology(this.ontologySchema,predResource));
		}
		return childOntologies;
	}
	
	/**
	 * 根据宾语取谓语的方法
	 * @param childOnto
	 * @return
	 */
	public DartOntology getPredOntology(DartOntology childOnto){
		if (null==this.childOntoMap){
			getChildOntologies();
		}
		return this.childOntoMap.get(childOnto);
	}
	
	public List<DartOntology> getInstanceProperties() {
		if (null!=instanceProperties){
			return instanceProperties;
		}
		instanceProperties=new ArrayList<DartOntology>();
		Collection<Resource> instanceProperyResources=this.ontologySchema.getInstancePropsByOnto(retrieveOntoType());
		for (Resource propResource:instanceProperyResources){
		//	Resource predResource=this.ontologySchema.getObjByInstancePred(this.ontology,propResource);
			DartOntology dartOntology = createInstanceOnto(propResource);			
			instanceProperties.add(dartOntology);
		}
        Collections.sort(instanceProperties);
		return instanceProperties;
	}
	
	public DartOntology getValuePropByOntoUri(String ontoUri){
		for (DartOntology valueProperty:getValueProperties()){
			if (ontoUri.equals(valueProperty.getURI())){
				return valueProperty;
			}
		}
		return null;		
	}
	
	public DartOntology getValuePropByName(String name){
		for (DartOntology valueProperty:getValueProperties()){
			if (name.equals(valueProperty.getName())){
				return valueProperty;
			}
		}
		return null;
	}
	
	public DartOntology getChildOntoByName(String name){
		for (DartOntology childOnto:getChildOntologies()){
			if (name.equals(childOnto.getName())){
				return childOnto;
			}
		}
		return null;
	}
	




	private DartOntology createInstanceOnto(Resource propResource) {
		DartOntology dartOntology=OntologyMaps.getInstance(propResource.getURI());
		if (null==dartOntology){
			dartOntology=new DartOntology(ontologySchema,propResource);
			OntologyMaps.putInstance(dartOntology);
		}
		return dartOntology;
	}
	
	public List<DartOntology> getValueProperties() {
		if (null!=valueProperties){
			return valueProperties;
		}
		valueProperties=new ArrayList<DartOntology>();
		Collection<Resource> valuePropertyResources=this.ontologySchema.getValuePropsByOnto(retrieveOntoType());
		for (Resource propResource:valuePropertyResources){
			DartOntology dartOntology = createValueOnto(propResource);	
			valueProperties.add(dartOntology);
		}		
        Collections.sort(valueProperties);
		return valueProperties;
	}
	
	private DartOntology createValueOnto(Resource propResource) {
		DartOntology dartOntology=OntologyMaps.getValue(propResource.getURI());
		if (null==dartOntology){
			dartOntology=new DartOntology(ontologySchema,propResource);
			OntologyMaps.putValue(dartOntology);
		}
		return dartOntology;
	}	




	private Resource retrieveOntoType() {
		if (null!=this.ontoType){
			return this.ontoType;
		}
		this.ontoType = ModelFactory.createDefaultModel().createResource(this.ontology.getURI());
		return this.ontoType;
	}



	public Resource getOntology() {
		return ontology;
	}


	public IOntologySchema getOntologySchema() {
		return ontologySchema;
	}

	public void setOntologySchema(IOntologySchema ontologySchema) {
		this.ontologySchema = ontologySchema;
	}

	public void setOntology(Resource ontology) {
		this.ontology = ontology;
	}


	public String getURI(){
		return this.ontology.getURI();
	}
	
	public String getName(){
		return this.ontology.getLocalName();
	}
	
	public String getNameSpace(){
		return this.ontology.getNameSpace();
	}
	
	/**
	 * 区分ontology的身份,由于URI虽然能区分本体的唯一性,但带了#号不能在request里传,因此建立这样一个字段来区分ontology
	 * @return
	 */
	public String getIdentity(){
        return OntoUriParseUtil.getOntoIdentity(getURI());
	}


	@Override
	public String toString() {
	   StringBuilder sb=new StringBuilder();
	   sb.append("ontology identity is: "+getIdentity()+";");
	   sb.append(" ontology URI is: "+ getURI()+";");       
       if (isInstanceType()){
    	  sb.append(" ontology is instance type;");
       }
       else{
    	   sb.append(" ontology is value type;");
       }
       return sb.toString();
	}




	@Override
	public boolean equals(Object obj) {
        if (!(obj instanceof DartOntology)){
            throw new TcmRuntimeException("要比较的对象不是DartOntology类型!");
        }
		if (null==obj){
			return false;
		}
		DartOntology compareOnto=(DartOntology)obj;
		return this.getIdentity().equals(compareOnto.getIdentity());
	}


	@Override
	public int hashCode() {
		return new HashCodeBuilder(15, 19).append(getNameSpace()).append(getName()).toHashCode();
	}



/**
 * 使本体按名称排序
 * @param o
 * @return
 */
    public int compareTo(Object o) {
        if (!(o instanceof DartOntology)){
            throw new TcmRuntimeException("要比较的对象不是DartOntology类型!");
        }
        DartOntology compareOnto=(DartOntology)o;
        return this.getName().compareTo(compareOnto.getName());
    }
	
	
}
