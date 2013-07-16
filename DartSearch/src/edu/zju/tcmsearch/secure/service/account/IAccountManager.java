
package edu.zju.tcmsearch.secure.service.account;
import edu.zju.tcmsearch.dao.Expression;
import edu.zju.tcmsearch.secure.domain.account.Account;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionListener;
/*
 * There are two kinds of Account objects:
 * 1)static  Account : 
 *   created outside AccountManager or loaded from DB
 *   it contains full or partial static information of Account
 * 2)runtime Account :
 *   managered by AccountManager and stored in session
 *   it constains full static information AND runtime information
 *   it may vary over time
 */
public interface IAccountManager extends HttpSessionListener{
	//create new Account object with ID assigned
	public Account createAccountObject();
	//write Account into DB , all properties are set
	public Account createInDB(Account account);
	
	//operations on DB
	public void delete(Account account);
	public void update(Account account);
	public void updateAuthority(Account account);
	public Account getAccountById(int id);
	public List<Account> getAllAccounts();	
	public List<Account> getGroupAccouts(String gid);
	
	public List<Account> getAccounts(Expression expr,List<String> orderBy);
	
	//load Account from DB, providing static information
	public Account getAccountByUserName(String UserName,String password);
	public Account getAccountByIp(String inip);

	//login, return a runtime Account  
	public Account login(Account account,HttpSession session);
	public void logOff(Account account,HttpSession session);	
	public void logOff(Account account);
	public boolean isLogin(Account account);
	public Account getRuntimeAccount(Account account);
	
	public void removeDeadAccount();
	
	public void destroy();
	
	public boolean isExistUser(String username);
	public boolean isExistIP(String ip);	
}
