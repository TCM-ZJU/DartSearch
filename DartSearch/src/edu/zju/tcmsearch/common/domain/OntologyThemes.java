package edu.zju.tcmsearch.common.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.zju.tcmsearch.util.dartcore.OntoUriParseUtil;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class OntologyThemes {
	List<OntologyTheme> ontoThemeList;
	
	Map<String,OntologyTheme> ontoThemeMap;

	public List<OntologyTheme> getOntoThemeList() {
		return ontoThemeList;
	}

	public void setOntoThemeList(List<OntologyTheme> ontoThemeList) {
		this.ontoThemeList = ontoThemeList;
	}
	
	public List<OntologyTheme> getOntoBelongToThemes(String ontoIdentity){
		List<OntologyTheme> belongToThemes=new ArrayList<OntologyTheme>();
		for (OntologyTheme ontologyTheme:getOntoThemeList()){
			for (String curOntoIdentity:ontologyTheme.getOntoIdentityList()){
				/*
				 * 从数据库中读出的curOntoIdentity形如http://dart.zju.edu.cn/dart#中草药
				 * 而从语义注册读出（可能经过某些转换）的ontoIdentity形如http://dart.zju.edu.cn/dart__中草药
				 * 因为#不能在httprequest里面传，用__代替
				 * 当两者进行比较时,应该把它们转化为相同的形式OntoUriParseUtil.getOntoIdentity(..)
				 */
				if (OntoUriParseUtil.getOntoIdentity(curOntoIdentity).equals(ontoIdentity)){
					belongToThemes.add(ontologyTheme);
				}
			}
		}
		return belongToThemes;
	}
	
	public Map<String,OntologyTheme> getOntoThemeMap(){
		if (null!=ontoThemeMap){
			return ontoThemeMap;
		}
		ontoThemeMap=new HashMap<String,OntologyTheme>();
		for (OntologyTheme ontologyTheme: getOntoThemeList()){
			ontoThemeMap.put(ontologyTheme.getName(),ontologyTheme);
		}
		return ontoThemeMap;
	}

}
