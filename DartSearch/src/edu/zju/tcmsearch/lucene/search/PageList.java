/*
 * Created on 2005-12-19
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.search;

import java.util.List;

import org.apache.lucene.search.Hits;

public interface PageList {
  public int getPageCount();
  public int getPageSize();
  public List<SearchResult> getPage(int pageId);
  public boolean isLastPage(int pageId);
  public boolean isFirstPage(int pageId);
  public List<Integer> getPageIdList();
}
