package edu.zju.tcmsearch.lucene.config;

public interface IIndexDirectory {
	public String getIndexDirectory();
	public String getIndexBuildDirectory();
	public void setIndexDirectory(String dir);
}
