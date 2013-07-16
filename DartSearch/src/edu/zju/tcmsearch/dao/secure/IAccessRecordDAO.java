package edu.zju.tcmsearch.dao.secure;
import java.util.List;

import edu.zju.tcmsearch.secure.domain.access.IAccessRecord;

public interface IAccessRecordDAO {
	public void storeRecord(IAccessRecord rec);

	public void storeRecord(List<IAccessRecord> recSet);
	
	public List<IAccessRecord> loadByAccountId(int id);
	
}
