package edu.zju.tcmsearch.secure.service.access.impl;

import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;


import edu.zju.tcmsearch.dao.secure.IAccessRecordDAO;
import edu.zju.tcmsearch.secure.acegi.TableAccessVoter;
import edu.zju.tcmsearch.secure.domain.access.IAccessRecord;
import edu.zju.tcmsearch.secure.service.access.IAccessRecordManager;

public class RecordManager implements IAccessRecordManager{
	
	private List<IAccessRecord> newRecordSet = new ArrayList<IAccessRecord>();
	private IAccessRecordDAO recordDAO;
	private int threshold = 100;
	private static final Logger logger = Logger.getLogger(RecordManager.class);
	
	public void addRecord(IAccessRecord rec){
	       newRecordSet.add(rec); 
	       if(newRecordSet.size()>threshold)
	    	   flushToPersistence();
	}
	
	public List<IAccessRecord> getRecordByAccountId(int accId){
		   return getRecordByIdFromNew(accId,recordDAO.loadByAccountId(accId));
	}
	
	public void flushToPersistence(){
		   logger.debug("flush access record(s) into DB , count ="+newRecordSet.size()); 
		   recordDAO.storeRecord(newRecordSet);
		   newRecordSet.clear();
	}
	
	public List<IAccessRecord> getRecordByIdFromNew(int id,List<IAccessRecord> list){
		   for(IAccessRecord rec:newRecordSet){
			   if(rec.getAccountId()==id)
				   list.add(rec);
		   }
		   return list;
	}
	public void setRecordDAO(IAccessRecordDAO dao){
		   recordDAO = dao;
	}
	
	public void setThreshold(int value){
		   this.threshold = value;
	}
	
	public void close(){
		   flushToPersistence();
	}
}
