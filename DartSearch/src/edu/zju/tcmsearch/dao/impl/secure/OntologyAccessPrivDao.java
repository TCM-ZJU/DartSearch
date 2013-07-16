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

import edu.zju.tcmsearch.secure.domain.access.OntologyAccessPrivilege;


public class OntologyAccessPrivDao {
	protected static Logger logger = Logger.getLogger(OntologyAccessPrivDao.class);
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public List<OntologyAccessPrivilege> getPrivilege(int accountId){
		OntologyPrivMappingQuery rolePrivMappingQuery = new OntologyPrivMappingQuery(getDataSource());
		Object[] parameters = new Object[1];
		parameters[0] = new Integer(accountId);
		return rolePrivMappingQuery.execute(parameters);		
	}
	
	protected class OntologyPrivMappingQuery extends MappingSqlQuery {
		protected OntologyPrivMappingQuery(DataSource ds) {
			super(ds,"select *  FROM OntologyAccessPriv where AccountId=?");
			declareParameter(new SqlParameter(Types.INTEGER));
			compile();
		}

		protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
			OntologyAccessPrivilege ontoAccessPrivilege = new OntologyAccessPrivilege();
			ontoAccessPrivilege.setAccountId(rs.getInt("AccountId"));
			ontoAccessPrivilege.setReadPriv(rs.getBoolean("readPriv"));
			ontoAccessPrivilege.setCategory(rs.getBoolean("isCategory"));
			ontoAccessPrivilege.setOntoloygURI(rs.getString("OntologyURI"));
			ontoAccessPrivilege.setWantReadPriv(rs.getBoolean("WantReadPriv"));
			return ontoAccessPrivilege;
		}
	}	
	
	public void removePrivilege(int accountId){
		String sql = "delete from OntologyAccessPriv where accountId="+accountId;
		new SqlUpdate(getDataSource(),sql).update();
	}
	
	public void addPrivilege(List<OntologyAccessPrivilege> privSet,int accountId){
		   String sqlHead = "insert into OntologyAccessPriv(AccountId,OntologyURI,isCategory,readPriv,wantReadPriv) values";
		   for(OntologyAccessPrivilege priv:privSet){
			 String value = (priv.isCategory()?"1":"0")+","+(priv.isReadPriv()?"1":"0")+","+(priv.isWantReadPriv()?"1":"0");
			 String sql = sqlHead+"("+priv.getAccountId()+",'"+priv.getOntoloygURI()+"',"+value+")";
			 logger.debug("insert table access privilege :"+sql);	
			 new SqlUpdate(getDataSource(),sql).update();
		   }		
	}
	
	public void updatePrivilege(List<OntologyAccessPrivilege> privSet,int accountId){
		removePrivilege(accountId);
		addPrivilege(privSet,accountId);
	}
}