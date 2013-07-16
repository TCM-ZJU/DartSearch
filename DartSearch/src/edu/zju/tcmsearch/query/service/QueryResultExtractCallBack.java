package edu.zju.tcmsearch.query.service;

import cn.edu.zju.dart.core.query.sqlplan.result.IQueryResult;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public interface QueryResultExtractCallBack {
	 public Object execute(IQueryResult qrs);
}
