package edu.zju.tcmsearch.web.form.lucene;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class AdvanceSearchForm {
	private String queryExperssion;

	private String ontologyName=null;
	
	private String themeName=null;

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	/**
	 * @return Returns the ontologyName.
	 */
	public String getOntologyName() {
		return ontologyName;
	}

	/**
	 * @param ontologyName
	 *            The ontologyName to set.
	 */
	public void setOntologyName(String ontologyName) {
		this.ontologyName = ontologyName;
	}

	/**
	 * @return Returns the queryExperssion.
	 */
	public String getQueryExperssion() {
		return queryExperssion;
	}

	/**
	 * @param queryExperssion
	 *            The queryExperssion to set.
	 */
	public void setQueryExperssion(String queryExperssion) {
		this.queryExperssion = queryExperssion;
	}
}
