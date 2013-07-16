package edu.zju.tcmsearch.dao.secure;

import java.util.List;

import edu.zju.tcmsearch.secure.domain.fee.FeeRecord;

public interface IFeeDao {
	
	public void insert(FeeRecord fee);
	
	public void insert(List<FeeRecord> fees);
	
	public List<FeeRecord> getByAccountId(int id);
	
	public List<FeeRecord> getByIdType(int id,int type);
	
	public List<FeeRecord> getAll();
	
	public List<FeeRecord> get(int id,int type,long start,long end);
	
	public final int IGNORE = -1;

}