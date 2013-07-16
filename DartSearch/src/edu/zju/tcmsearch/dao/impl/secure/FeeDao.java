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

import edu.zju.tcmsearch.dao.secure.IFeeDao;
import edu.zju.tcmsearch.secure.domain.fee.FeeRecord;

public class FeeDao implements IFeeDao {
	
	protected static Logger logger = Logger.getLogger(FeeDao.class);
	
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}	

	public void insert(FeeRecord fee) {
		new InsertFee(dataSource).insert(fee);

	}

	public void insert(List<FeeRecord> fees) {
		InsertFee ins=new InsertFee(dataSource);
		for(FeeRecord fee:fees){
			ins.insert(fee);
		}

	}

	public List<FeeRecord> getByAccountId(int id) {
		return get(id,IFeeDao.IGNORE,IFeeDao.IGNORE,IFeeDao.IGNORE);
	}

	public List<FeeRecord> getByIdType(int id, int type) {
		return get(id,type,IFeeDao.IGNORE,IFeeDao.IGNORE);
	}

	public List<FeeRecord> getAll() {
		return get(IFeeDao.IGNORE,IFeeDao.IGNORE,IFeeDao.IGNORE,IFeeDao.IGNORE);
	}

	public List<FeeRecord> get(int id, int type,long start,long end) {
		LoadFeeRecord loader = new LoadFeeRecord(this.dataSource);
		loader.init(id,type,start,end);
		return loader.execute();
	}

	protected class LoadFeeRecord extends MappingSqlQuery{	
		public LoadFeeRecord(DataSource dataSource){
			super(dataSource,"");
		}
		
		public void init(int id, int type,long start,long end){
			String sql = "select * from AccountFee where ";
			int cnt=0;
			if(id!=IFeeDao.IGNORE){
				sql += "accountId = "+id+" and ";
				cnt++;
			}
			if(type!=IFeeDao.IGNORE){
				sql += "type = "+type+" and ";
				cnt++;
			}
			if(start!=IFeeDao.IGNORE){
				sql += "time >= "+start+" and ";
				cnt++;
			}
			if(end!=IFeeDao.IGNORE){
				sql += "time <= "+end+" and ";
				cnt++;
			}
			sql +="1=1  order by IntTime";
			setSql(sql);
			logger.debug("Get FeeRecord SQL="+sql);
			compile();
		}
		protected Object mapRow(ResultSet rs, int rownum) throws SQLException{
			FeeRecord fee = new FeeRecord();
			fee.setAccountId(rs.getInt("AccountId"));
			fee.setFee(rs.getInt("Fee"));
			fee.setIntTime(rs.getInt("intTime"));
			fee.setTime(rs.getString("Time"));
			fee.setType(rs.getInt("Type"));
			fee.setContent(rs.getString("Content"));
			fee.setGoodsIdentity(rs.getString("GoodsId"));
			return fee;
		}
	}
	
	protected class InsertFee extends SqlUpdate{	
		public InsertFee(DataSource dataSource){
			super(dataSource,"insert into AccountFee(AccountId,Fee,intTime,Type,Time,Content,GoodsId) values(?,?,?,?,?,?,?)");
			declareParameter(new SqlParameter(Types.INTEGER));
			declareParameter(new SqlParameter(Types.INTEGER));
			declareParameter(new SqlParameter(Types.BIGINT));
			declareParameter(new SqlParameter(Types.INTEGER));
			declareParameter(new SqlParameter(Types.VARCHAR));
			declareParameter(new SqlParameter(Types.VARCHAR));
			declareParameter(new SqlParameter(Types.VARCHAR));
			compile();
		}
		
		protected void insert(FeeRecord fee){
			Object[] params = new Object[]{fee.getAccoutId(),fee.getFee(),fee.getIntTime(),
					                       fee.getType(),fee.getTime(),fee.getContent(),fee.getGoodsIdentity()};
			update(params);
		}
	}
	
}
