package edu.zju.tcmsearch.secure.service.access;
import java.util.List;

import edu.zju.tcmsearch.secure.domain.access.IAccessRecord;

public interface IAccessRecordManager {
	public void addRecord(IAccessRecord rec);
	
	public List<IAccessRecord> getRecordByAccountId(int accId);
	
	public void flushToPersistence();
}
