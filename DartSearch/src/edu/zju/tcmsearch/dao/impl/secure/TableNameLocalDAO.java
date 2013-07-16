package edu.zju.tcmsearch.dao.impl.secure;

import java.util.Map;

import javax.sql.DataSource;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.object.SqlUpdate;

import edu.zju.tcmsearch.dao.secure.ITableNameLocalDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;


/**
 * Create Table TableNameLocalization
 * ( TableID   VARCHAR(256) not null,
 *   ChineseID VARCHAR(256) 
 * )
 */
public class TableNameLocalDAO implements ITableNameLocalDAO {

	private DataSource dataSource;
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}	
	public void update(Map<String, String> map) {
		String str = "insert into TableNameLocalization(TableID,ChineseID) values(?,?)";
        BatchSqlUpdate sql = new BatchSqlUpdate(dataSource,str);
        sql.declareParameter(new SqlParameter(Types.VARCHAR));
        sql.declareParameter(new SqlParameter(Types.VARCHAR));
        for(String s:map.keySet()){
            Object param[]= new Object[]{s,map.get(s)};
            sql.update(param);
        }
        if(sql.getQueueCount()>0){
           sql.flush();
        }        
	}

	public void load(Map<String, String> map) {
		String s = "Select TableID,ChineseID from TableNameLocalization ";
		MappingSqlQuery sql = new SMappingSqlQuery(dataSource,s,map);
		sql.execute();
	};


	protected class SMappingSqlQuery extends MappingSqlQuery{
		private Map map;
		SMappingSqlQuery(DataSource dataSource,String sql,Map map){
			super(dataSource,sql);
			this.map = map;
		}
		public Object mapRow(ResultSet rs,int rowNum) throws SQLException{
			String tID = rs.getString("TableID");
			String cID = rs.getString("ChineseID");
			map.put(tID,cID);
			return null;
		}
	}


	public void clear() {
		SqlUpdate sql = new SqlUpdate();
		sql.setDataSource(this.dataSource);
		sql.setSql("Delete from TableNameLocalization");
		sql.update();
	}	
	
	public void initDB(){
		String str = "Create Table TableNameLocalization( TableID   VARCHAR(256) not null, ChineseID VARCHAR(256) )";
		SqlUpdate sql = new SqlUpdate();
		sql.setDataSource(this.dataSource);
		sql.setSql(str);
		sql.update();
	}
}
