package edu.zju.tcmsearch.dao.impl.secure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlUpdate;

import org.apache.log4j.Logger;

import edu.zju.tcmsearch.secure.domain.access.TableAccessPrivilege;


public class TableAccessPrivDao {
	protected static Logger logger = Logger.getLogger(TableAccessPrivDao.class);
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public List<TableAccessPrivilege> getRoleTableAccessPrivileges(int accountId) {
		RolePrivMappingQuery rolePrivMappingQuery = new RolePrivMappingQuery(
				getDataSource());
		Object[] parameters = new Object[1];
		parameters[0] = new Integer(accountId);
		return rolePrivMappingQuery.execute(parameters);
	}

	protected class RolePrivMappingQuery extends MappingSqlQuery {
		protected RolePrivMappingQuery(DataSource ds) {
			super(ds,
					"select * from tableAccessPriv where AccountId=?");
			declareParameter(new SqlParameter(Types.INTEGER));
			compile();
		}

		protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
			TableAccessPrivilege tableAccessPrivilege = new TableAccessPrivilege();
			tableAccessPrivilege.setAccountId(rs.getInt("AccountId"));
			tableAccessPrivilege.setTableOwner(rs.getString("tableOwner"));
			tableAccessPrivilege.setTableName(rs.getString("tableName"));
			tableAccessPrivilege.setReadPriv(rs.getBoolean("readPriv"));
			tableAccessPrivilege.setWantReadPriv(rs.getBoolean("wantReadPriv"));
			return tableAccessPrivilege;
		}
	}

	public void addRoleTableAccessPrivileges(List<TableAccessPrivilege> privSet){
		   String sqlHead = "insert into tableAccessPriv(AccountId,tableOwner,tableName,readPriv,wantReadPriv) values";
		   for(TableAccessPrivilege priv:privSet){
			 String value = (priv.isReadPriv()?"1":"0")+","+(priv.isWantReadPriv()?"1":"0");
			 String sql = sqlHead+"("+priv.getAccountId()+",'"+priv.getTableOwner()+"','"+priv.getTableName()+"',"+value+")";
			 logger.debug("insert table access privilege :"+sql);	
			 new SqlUpdate(getDataSource(),sql).update();
		   }
	}
	
	
	public void addRoleTableAccessPrivileges(int accountId,String[] tableName,String[] tableOwner){
		   if(null==tableName|| null==tableOwner || tableName.length!=tableOwner.length ||tableName.length==0)
			   return;
		   String sqlHead = "insert into tableAccessPriv(AccountId,tableOwner,tableName,readPriv,wantReadPriv) values";
		   
		   for(int i=0;i<tableName.length;i++){
			   String sql = sqlHead+"("+accountId+",'"+tableOwner[i]+"','"+tableName[i]+"',0,0)";
			   logger.debug("insert table access privilege :"+sql);		
			   new SqlUpdate(getDataSource(),sql).update();
		   }
	}
	
	public void removeRoleTableAccessPrivileges(int accountId,String[] tableName,String[] tableOwner){
		   if(null==tableName|| null==tableOwner || tableName.length!=tableOwner.length ||tableName.length==0)
			   return;
		   String sqlHead = "delete from  tableAccessPriv where ";
		   for(int i=0;i<tableName.length;i++){
			   String sql = sqlHead+"AccountId='"+accountId+"' and tableOwner='"+tableOwner[i]+"' and tableName='"+tableName[i]+"'";
			   logger.debug("remove table access privilege :"+sql);			
			   new SqlUpdate(getDataSource(),sql).update();
		   }
		
	}
	
	public void removeRoleTableAccessPrivileges(List<TableAccessPrivilege> privSet){
		   String sqlHead = "delete from tableAccessPriv where ";
		   for(TableAccessPrivilege priv:privSet){
			 String sql = sqlHead+"AccountId='"+priv.getAccountId()+"' and tableOwner='"+priv.getTableOwner()+"' and tableName='"+priv.getTableName()+"'";
			 logger.debug("remove table access privilege :"+sql);	
			 new SqlUpdate(getDataSource(),sql).update();
		   }
	}
		
	public void updateRoleTableAccessPrivileges(List<TableAccessPrivilege> privSet,int accountId){
		  String sql = "delete from tableAccessPriv where AccountId="+accountId;
		  new SqlUpdate(getDataSource(),sql).update();
		  addRoleTableAccessPrivileges(privSet);
	}
}
