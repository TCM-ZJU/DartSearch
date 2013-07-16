package edu.zju.tcmsearch.lucene.search;

import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import edu.zju.tcmsearch.common.ApplicationContextHolder;
import edu.zju.tcmsearch.common.domain.OntologyTheme;
import edu.zju.tcmsearch.common.domain.OntologyThemesFromDB;
import edu.zju.tcmsearch.util.dartcore.OntoUriParseUtil;

/**
 * AdvanceSearchResult 过滤掉结果集中的某些本体
 * 这些本体不属于用户查询时指定的专题
 * AdvanceSearchResult filters some ontologies from result set,
 * because these ontologies do not belong to the ontologyThemes specified in query   
 * @author dart
 *
 */
public class AdvancedSearchResult extends SearchResult {
	private static final Logger logger = Logger.getLogger(AdvancedSearchResult.class);
	
    public List<String> getOntoIdenties() {
    	OntologyThemesFromDB  ontoThemeDB = getOntologyThemes();
    	List<String> ontoAll = new ArrayList<String>();
    	for(String themeID : getBelongToThemes()){
    		OntologyTheme  ontoTheme = ontoThemeDB.getOntoThemeMap().get(themeID);
    		if(null==ontoTheme)
    			logger.debug("ontology theme does not exsit :"+themeID);
    		else 
    			ontoAll.addAll(ontoTheme.getOntoIdentityList());  
    	}
    	
    	List<String> ontoSet = new ArrayList<String>();
    	for(String ontoID : super.getOntoIdenties()){
    		if(ontoAll.contains(OntoUriParseUtil.getOntoUri(ontoID)))
    			ontoSet.add(ontoID);
    	}
        return ontoSet;
    }
    
    public OntologyThemesFromDB getOntologyThemes() {
		return (OntologyThemesFromDB) ApplicationContextHolder.getContext().getBean("ontologyThemes");
	}    
    
    public String[] getBelongToThemes(){
    	String themeStr = (String)super.getQueryStorer().getQueryInfo("belongToTheme");
    	if(null==themeStr)
    	   return new String[]{};
    	else
    	   return StringUtils.tokenizeToStringArray(themeStr,",");
    }
    
}
