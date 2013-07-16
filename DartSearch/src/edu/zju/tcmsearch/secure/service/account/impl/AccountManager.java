package edu.zju.tcmsearch.secure.service.account.impl;
import edu.zju.tcmsearch.dao.Expression;
import edu.zju.tcmsearch.dao.secure.IAccountDao;
import edu.zju.tcmsearch.secure.domain.account.Account;
import edu.zju.tcmsearch.secure.service.account.AccountIdentityGenerator;
import edu.zju.tcmsearch.secure.service.account.IAccountManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import org.apache.log4j.Logger;

public class AccountManager implements IAccountManager {

	static protected Logger logger = Logger.getLogger(AccountManager.class);
	
	private IAccountDao accountDAO;
	
	private Map<String,Account> loginMap = new HashMap<String,Account>();
	
	private Map<HttpSession,Account> sessionMap = new HashMap<HttpSession,Account>();
	
	private AccountIdentityGenerator accountIdentityGenerator;
	
	public AccountManager(){}

	public void setAccountDAO(IAccountDao accountDao) {
		this.accountDAO = accountDao;
	}

	public void setAccountIdentityGenerator(AccountIdentityGenerator accIG){
		this.accountIdentityGenerator = accIG;
	}
	
	public Account createAccountObject(){
		Account account = new Account();
		account.setId(accountIdentityGenerator.getAccountId());
		return account;		
	}
	
	public Account createInDB(Account account) {
		// TODO Auto-generated method stub
		if(account.getLoginType().equals(Account.TYPE_IP) && null==accountDAO.loadByIp(account.getIPAddress())|| 
		   account.getLoginType().equals(Account.TYPE_USERPASS) && null==accountDAO.loadByUserName(account.getUsername())){
			accountDAO.accountInsert(account);
			return account;			
		}else{
			return null;
		}
	}

	public void delete(Account account) {
		// TODO Auto-generated method stub
		accountDAO.accountDelete(account);
	}
	
	public Account getAccountById(int id){
		return accountDAO.accountFindById(id);
	}
	
	public List<Account> getAllAccounts(){
		return accountDAO.loadAuthority(accountDAO.loadAll());
	}
	
	public List<Account> getAccounts(Expression expr,List<String> orderBy){
		return accountDAO.load(expr,orderBy);
	}
	
	public void update(Account account) {
		// TODO Auto-generated method stub
		accountDAO.update(account);
		updateRuntimeAccount(account);
	}
	
	/*
	 * update static infomation on runtimeAccount while preserving runtime information
	 */
	protected void updateRuntimeAccount(Account account){
		Account runtimeAccount = getRuntimeAccount(account);
		if(null!=runtimeAccount){
			account.setConcurrence(runtimeAccount.getConcurrence());
			for(HttpSession session:runtimeAccount.getConcurrence()){
				sessionMap.put(session,account);
			}
			loginMap.put(account.getUniqueName(),account);
		}
	}
	
    public void updateAuthority(Account account)  {
    	accountDAO.updateAuthority(account);
    }

	public Account getAccountByUserName(String UserName,String password){
		return accountDAO.loadByUserName(UserName,password);
	}
	
	public Account getAccountByIp(String inip){
		return accountDAO.loadByIp(inip);
	}
	
	public List<Account> getGroupAccouts(String gid){
		return accountDAO.loadAuthority(accountDAO.loadByGroup(gid));
	}
    /*
     * ONE RuntimeAccount may be shared between MANY Sessions
     * ONE Session at most have ONE RuntimeAccount
     * When a session login twice with different accounts , the former account will be logOff automatically 
     */
	public Account login(Account account,HttpSession session){
		Account runtimeAccount =loginMap.get(account.getUniqueName());
        if((null!=runtimeAccount) && runtimeAccount.addConcurrence(session)){
        	Account former = sessionMap.get(session);
        	if(null!=former){
        		logOff(former,session);
        	}
        	sessionMap.put(session,runtimeAccount);
        	session.setAttribute(Account.ACCOUNT_SAVE_IN_SESSION_1986_KEY,runtimeAccount);
        	logger.debug("login :"+runtimeAccount.getUniqueName()+" @ "+session.getId());
        	return runtimeAccount;
        }else if((null==runtimeAccount) && account.addConcurrence(session)){
        	Account former = sessionMap.get(session);
        	if(null!=former){
        		logOff(former,session);
        	}        	
			sessionMap.put(session,account);
			session.setAttribute(Account.ACCOUNT_SAVE_IN_SESSION_1986_KEY,account);
			loginMap.put(account.getUniqueName(),account);
			logger.debug("login :"+account.getUniqueName()+" @ "+session.getId());
		    return account;
        }else {
        	return null;
        }
    }
	
	public void logOff(Account account,HttpSession session){
		Account runtimeAccount =loginMap.get(account.getUniqueName());
		if(null!=runtimeAccount){
			runtimeAccount.removeConurrence(session);
			if(runtimeAccount.getConcurrenceSize()<=0){
				update(runtimeAccount);
				updateAuthority(runtimeAccount);
				loginMap.remove(account.getUniqueName());				
			}
			session.removeAttribute(Account.ACCOUNT_SAVE_IN_SESSION_1986_KEY);
			//session.invalidate();			
			sessionMap.remove(session);
			logger.debug("logoff :"+runtimeAccount.getUniqueName()+" @ "+session.getId());
		}
	}
	
	public void logOff(Account account){
		Account runtimeAccount =loginMap.get(account.getUniqueName());
		if(null!=runtimeAccount){
			for(HttpSession session:runtimeAccount.getConcurrence()){
				sessionMap.remove(session);
				session.removeAttribute(Account.ACCOUNT_SAVE_IN_SESSION_1986_KEY);
				//session.invalidate();				
			}
			update(runtimeAccount);
			updateAuthority(runtimeAccount);	
			loginMap.remove(account.getUniqueName());	
			logger.debug("logoff on Force :"+runtimeAccount.getUniqueName());
		}
	}
	
	public boolean isLogin(Account account){
		return null!=loginMap.get(account.getUniqueName());
	}
	

	public Account getRuntimeAccount(Account account){
		return loginMap.get(account.getUniqueName());
	}
	
	public void removeDeadAccount(){
		for(Account runtimeAccount:loginMap.values()){
			if(runtimeAccount.getConcurrenceSize()<=0){
			    update(runtimeAccount);
			    loginMap.remove(runtimeAccount);
			}
		}
	}
	
	public void destroy(){
		for(Account runtimeAccount:loginMap.values()){
		    update(runtimeAccount);
		}
		loginMap.clear();
	}
	
	public void sessionCreated(HttpSessionEvent se){
	}
	
	public void sessionDestroyed(HttpSessionEvent se){
		Account runtimeAccount = sessionMap.get(se.getSession());
		if(null!=runtimeAccount){
			logOff(runtimeAccount,se.getSession());
		}
	}
	
	public boolean isExistUser(String username){
		return null!=accountDAO.loadByUserName(username);
	}
	public boolean isExistIP(String ip){
		return null!=accountDAO.loadByIp(ip);
	}	
}
