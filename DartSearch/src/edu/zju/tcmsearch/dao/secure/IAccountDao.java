
package edu.zju.tcmsearch.dao.secure;
import java.util.List;

import edu.zju.tcmsearch.dao.Expression;
import edu.zju.tcmsearch.secure.domain.account.Account;

public interface IAccountDao {

	
		public Account loadByUserName(String UserName,String password);
		public Account loadByUserName(String UserName);
		public Account loadByIp(String inip);
		public List<Account> loadAll();
		public List<Account> loadByGroup(String gid);
		public List<Account> load(Expression expr,List<String> orderBy);
		public Account accountFindById(int id);
		
		public void accountDelete(Account account);
		public void update(Account account);
		public void accountInsert(Account account);
		
		public void updateAuthority(Account acount);
		public List<Account> loadAuthority(List<Account> accounts);
		
		public Integer getMaximumAccountId();
		
		public void updateField(int accountID,String field,String value);
		

}
