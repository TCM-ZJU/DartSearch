package edu.zju.tcmsearch.util.web;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.lucene.search.DartOntologyInfo;
import edu.zju.tcmsearch.lucene.search.SearchResult;
public class SearchResultUtil {
 	
	public SearchResultUtil(){}
	public static List<DartOntologyInfo> getOntologySet(SearchResult result){
		return  result.getOntologyInfos();
	}
	
	public static List<DartOntology> getLinkOntologySet(SearchResult result){
		List<DartOntology> linkOntoSet = new ArrayList<DartOntology>();
		if(null!=result){
		   for(DartOntologyInfo ontoInfo: result.getOntologyInfos()){
			   for(DartOntology linkOnto:ontoInfo.getChildOntologies())
				   if(!linkOntoSet.contains(linkOnto))
					   linkOntoSet.add(linkOnto);
		   }
		}
		return linkOntoSet;
	}
}
