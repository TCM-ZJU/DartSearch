package edu.zju.tcmsearch.dao.impl.secure;

import java.util.List;

import edu.zju.tcmsearch.dao.secure.IAccessRecordDAO;
import edu.zju.tcmsearch.secure.domain.access.IAccessRecord;
import edu.zju.tcmsearch.secure.domain.access.impl.AccessRecord;
import edu.zju.tcmsearch.secure.domain.account.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlUpdate;

public class AccessRecordDAO implements IAccessRecordDAO {
	
	private DataSource dataSource;

	public void storeRecord(IAccessRecord rec) {
		// TODO Auto-generated method stub
		new InsertRecordSql(dataSource).insert(rec);

	}

	public void storeRecord(List<IAccessRecord> recSet) {
		// TODO Auto-generated method stub
		InsertRecordSql insRecSql = new InsertRecordSql(dataSource);
		for(IAccessRecord rec:recSet)
			insRecSql.insert(rec);
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	protected class InsertRecordSql extends SqlUpdate{
		public InsertRecordSql(DataSource dataSource){
			super(dataSource,"insert into AccessRecord (AccountId,AccessDate,AccessUrl,Content) values(?,?,?,?)");
			declareParameter(new SqlParameter(Types.INTEGER));
			declareParameter(new SqlParameter(Types.DATE));
			declareParameter(new SqlParameter(Types.VARCHAR));
			declareParameter(new SqlParameter(Types.VARCHAR));
		    compile();
	    }

	    protected void insert(IAccessRecord rec){
	    	Object[] params= new Object[]{new Integer(rec.getAccountId()),rec.getAccessDate(),rec.getAccessUrl(),rec.getContent()};
	    	super.update(params);
	    }

	}
	
	public List<IAccessRecord> loadByAccountId(int id){
		LoadRecordById  loadSql = new LoadRecordById(dataSource);
		return loadSql.execute(new Integer(id));
	}
	
	protected class LoadRecordById extends MappingSqlQuery{
		public LoadRecordById(DataSource dataSource){
			super(dataSource,"select AccountId,AccessDate,AccessUrl,Content FROM AccessRecord WHERE AccountId=?");
			declareParameter(new SqlParameter(Types.INTEGER));
		    compile();
	    }

	    
	    protected Object mapRow(ResultSet rs, int rownum) throws SQLException{
	      AccessRecord rec = new AccessRecord();
	      rec.setAccessDate(rs.getDate("AccessDate"));
	      rec.setAccessUrl(rs.getString("AccessUrl"));
	      rec.setAccountId(rs.getInt("AccountId"));
	      rec.setContent(rs.getString("Content"));
	      return rec;
	    }

	}	
}
