package edu.zju.tcmsearch.common.domain;

import java.util.ArrayList;
import java.util.List;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class OntologyTheme {
	private int id;

	private String name;

	private String description;

	private List<String> ontoIdentityList;
	
	/*
	private List<DartOntology> ontologyList;
	
	private OntoService ontoService;
	*/
	
	private List<OntologyTheme> childTheme;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getOntoIdentityList() {
		return ontoIdentityList;
	}

	public void setOntoIdentityList(List<String> ontoIdentityList) {
		this.ontoIdentityList = ontoIdentityList;
	}
	

	/*
	public List<DartOntology> getDartOntoList(){
		if (null!=ontologyList){
			return ontologyList;
		}
		ontologyList=new ArrayList<DartOntology>();
		for (String ontoIdentity:getOntoIdentityList()){
			ontologyList.add(getOntoService().getInstanceOntology(ontoIdentity));
		}
		return ontologyList;
	}

	public OntoService getOntoService() {
		return ontoService;
	}

	public void setOntoService(OntoService ontoService) {
		this.ontoService = ontoService;
	}
	*/
	
	public void addChild(OntologyTheme child) {
		if(null == this.childTheme) {
			this.childTheme =  new ArrayList<OntologyTheme>();
		}
		this.childTheme.add(child);
	}
	
	public List<OntologyTheme> getChild() {
		if(null == this.childTheme) {
			this.childTheme =  new ArrayList<OntologyTheme>();
		}
		return this.childTheme;
	}
}
