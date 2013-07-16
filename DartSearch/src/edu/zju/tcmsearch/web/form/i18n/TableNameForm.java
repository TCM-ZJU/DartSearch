package edu.zju.tcmsearch.web.form.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import edu.zju.tcmsearch.secure.TableNameLocalization;


public class TableNameForm {

	private List<String> key;
	private String[] value;
	private TableNameLocalization map;
	
	private int save_page_id;
	private int save_next_page_id;
	private int save_prior_page_id;
	private int save_page_count;
	
	public void savePageCount(int c){
		this.save_page_count = c;
	}
	
	public void savePageId(int id){
		this.save_page_id = id;
	}
	
	public void saveNextPageId(int id){
		this.save_next_page_id = id;
	}
	
	public void savePriorPageId(int id){
		this.save_prior_page_id = id;
	}
	
	public int getPageCount(){
		return this.save_page_count;
	}
	
	public int getPageId(){
		return this.save_page_id;
	}
	
	public int getNextPageId(){
		return this.save_next_page_id;
	}
	
	public int getPriorPageId(){
		return this.save_prior_page_id;
	}
	
	public List<String> getKey(){
		return this.key;
	}
	
	public String[] getValue(){
		return this.value;
	}
	
	public TableNameLocalization getMap(){
		return this.map;
	}
	
	public List<String> getValueList(){
	   if(null==value)
		   return Collections.EMPTY_LIST;
	   else{
		   List<String> list = new ArrayList(value.length-1);
		   for(int i=0;i<value.length-1;i++)
			   list.add(i,value[i+1]);
		   return list;
	   }
		   
	}
	
	public TableNameForm(TableNameLocalization map,List<String> key){
		this.map = map;
		this.key = key;
		this.value = new String[this.key.size()+1];
	}

}
