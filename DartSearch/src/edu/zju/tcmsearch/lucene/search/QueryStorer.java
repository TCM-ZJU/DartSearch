/*
 * Created on 2005-12-25
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.search;

import org.apache.lucene.search.Query;

/**
 * 一个保存query的接口,  其自身放到HttpSession中
 * 没有直接使用HttpSession是为了让search应用本身与Web api无关
 *
 * @author xcc
 *
 */
public interface QueryStorer {
  public void storeQuery(Query query);
  public Query getQuery();
  public void storeHighlightQuery(Query query);
  public Query getHighlightQuery();
  
  public void storeQueryExpression(String expr);
  public String  getQueryExpression();
  
  /**
   * 存储查询的原始信息，在对结果集进行处理时可能用到
   * @param name
   * @return
   */
  public Object getQueryInfo(String name);
  public void   storeQueryInfo(String name,Object info);
  
  /**
   * 释放某些一次性信息，这些信息通常与某次具体的查询相关
   */
  public void freeVolatile();
}
