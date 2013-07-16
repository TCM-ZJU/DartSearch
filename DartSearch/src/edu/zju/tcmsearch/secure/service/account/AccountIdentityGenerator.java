package edu.zju.tcmsearch.secure.service.account;
import edu.zju.tcmsearch.dao.secure.IAccountDao;
public class AccountIdentityGenerator {

	private IAccountDao accountDao;
	private Integer nextAccountId=null;
	
	public void setAccountDAO(IAccountDao accountDao){
		this.accountDao = accountDao;
	}
	
	synchronized public int getAccountId(){
		if(null==nextAccountId){
			nextAccountId = new Integer(accountDao.getMaximumAccountId().intValue()+1);
		}
		int id = nextAccountId.intValue();
		nextAccountId = new Integer(nextAccountId.intValue()+1);
		return id;
	}
}
