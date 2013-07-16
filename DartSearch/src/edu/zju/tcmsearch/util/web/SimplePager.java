package edu.zju.tcmsearch.util.web;

import java.util.List;
import java.util.Collections;
public class SimplePager<T> {

	private List<T> list;
	private int pageSize;
	private int firstPage;
	private final int Invaild_Internal_Page = -1;
	
	public SimplePager(List<T> list,int pageSize,int firstPage){
		assert null!=list && pageSize>0;
		this.list = list;
		this.pageSize = pageSize;
		this.firstPage = firstPage;
	}
	
	public SimplePager(List<T> list,int pageSize){
		this(list,pageSize,1);
	}
	
	public int pageCount(){
		return  (list.size()%pageSize == 0) ? list.size()/pageSize : list.size()/pageSize +1;
	}
	
	public int firstPage(){
		return firstPage;
	}
	
	public int lastPage(){
		return firstPage+pageCount()-1;
	}
	
	public void setPageSize(int ps){
		this.pageSize = ps;
	}
	public void setFirstPage(int fp){
		this.firstPage = fp;
	}
	
	public int nextPage(int page){
		int next = page+1 - firstPage;
		next= isEmpty() ? firstPage : next%pageCount() + firstPage;
		return next;
	}
	
	public int priorPage(int page){
		int prior = page - 1 - firstPage;
		prior= isEmpty() ? firstPage : (prior+pageCount())%pageCount() + firstPage;
		return prior;		
	}
	public List<T> getPage(int page){
		int ipage = getInternalPage(page);
		if(isEmpty() || ipage==this.Invaild_Internal_Page){
			return Collections.EMPTY_LIST;
		}else{
			int begin = ipage*pageSize;
			int end = ipage+1== pageCount() ? list.size(): (ipage+1)*pageSize;
			return list.subList(begin,end);
		}
	}
	
	protected int getInternalPage(int page){
		int ipage = page - firstPage;
		return ipage >= 0 && ipage < pageCount() ? ipage : Invaild_Internal_Page;
	}
	
	public boolean isEmpty(){
		return null==list || list.size()==0;
	}
}
