package edu.zju.tcmsearch.dao.secure;

import java.util.Map;

public interface ITableNameLocalDAO {

	/* load from database and add into map*/
	public void load(Map<String,String> map);
	/* update into database */
	public void update(Map<String,String> map);
	public void clear();
}
