package edu.zju.tcmsearch.web.controller.lucene;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.zju.tcmsearch.common.domain.OntologyThemesFromDB;
import edu.zju.tcmsearch.lucene.search.QueryStorer;
import edu.zju.tcmsearch.lucene.search.SearchResults;
import edu.zju.tcmsearch.web.form.lucene.AdvanceSearchForm;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class AdvanceSearchController extends AbstractSearchController {
	private OntologyThemesFromDB ontologyThemes;

	public OntologyThemesFromDB getOntologyThemes() {
		return ontologyThemes;
	}

	public void setOntologyThemes(OntologyThemesFromDB ontologyThemes) {
		this.ontologyThemes = ontologyThemes;
	}

	@Override
	protected SearchResults doSearch(HttpServletRequest request, Object command) {
		AdvanceSearchForm advanceSearchForm = (AdvanceSearchForm) command;
		if (null == advanceSearchForm.getQueryExperssion()
				|| "".equals(advanceSearchForm.getQueryExperssion())) {
			return null;
		}
		QueryStorer queryStorer = getQueryStorer(request.getSession());
		SearchResults searchResults = search(advanceSearchForm, queryStorer);
		return searchResults;
	}

	private SearchResults search(AdvanceSearchForm advanceSearchForm,
			QueryStorer queryStorer) {

		String[] searchFields = { "showContent", "clobContent" };
		logger.debug("themeName is " + advanceSearchForm.getThemeName());
		logger.debug("ontoName is" + advanceSearchForm.getOntologyName());

		String[] requiredFields = {"belongToTheme" };
		String[] requiredQuery = {advanceSearchForm.getThemeName()};


		return getSearcher().search(advanceSearchForm.getQueryExperssion(),
				searchFields, requiredQuery, requiredFields, queryStorer);
	}

	@Override
	protected void putRefData(Map model) {
		model.put("ontoOptions", getOntoOptions());
		//model.put("ontologyThemeMap", ontologyThemes.getOntoThemeMap());
		model.put("ontoThemeList",ontologyThemes.getOntoThemeList());
	}

}
