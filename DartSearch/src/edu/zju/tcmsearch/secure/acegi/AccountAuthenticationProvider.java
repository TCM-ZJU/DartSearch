package edu.zju.tcmsearch.secure.acegi;

import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.AuthenticationException;
import net.sf.acegisecurity.providers.AuthenticationProvider;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import net.sf.acegisecurity.DisabledException;
import net.sf.acegisecurity.LockedException;
import net.sf.acegisecurity.BadCredentialsException;


import edu.zju.tcmsearch.dao.secure.IAccountDao;
import edu.zju.tcmsearch.exception.secure.AccountInActviatedException;
import edu.zju.tcmsearch.exception.secure.AccountNotFoundException;
import edu.zju.tcmsearch.exception.secure.AccountStoppedException;
import edu.zju.tcmsearch.secure.domain.access.GrantedAuthorityConstant;
import edu.zju.tcmsearch.secure.domain.account.Account;
import edu.zju.tcmsearch.secure.service.account.IAccountManager;


public class AccountAuthenticationProvider implements AuthenticationProvider , InitializingBean{

	private static Logger logger= Logger.getLogger(AccountAuthenticationProvider.class);
	
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		Assert.isInstanceOf(AccountAuthentication.class,authentication,"authenticate AccountAuthentication only");
		AccountAuthentication auth = (AccountAuthentication) authentication;
		Account account=null;
		if(!auth.isAuthenticated()){
			if(auth.isAuthenticatedByIP()){
				account=accountManager.getAccountByIp(auth.getIPAddress());
			}else{
				account=accountManager.getAccountByUserName(auth.getUsername(),auth.getPassword());
			}
			if(null==account){
				//throw new AccountNotFoundException("account not found in database");
				throw new BadCredentialsException("帐户不存在或密码错误");
			}else if(account.hasAuthority(GrantedAuthorityConstant.AUTHORITY_ADMINISTRATOR.toString())){
				//administrator never fails on login
				Account runtimeAccount = accountManager.login(account,auth.getSession());
				if(null!=runtimeAccount){
					AccountAuthentication accAuth = new AccountAuthentication(runtimeAccount);
					accAuth.setSession(auth.getSession());
					return accAuth;
				}else{
					throw new net.sf.acegisecurity.concurrent.ConcurrentLoginException("超过最大登陆用户数");
				}
			}
			
			account.checkTimeOut();
			account.checkFlow();
			account.checkToFeeRunOut();
			
			if(account.isInActviated()){
				//throw new AccountInActviatedException("account is InActivated");
				throw new DisabledException("帐户尚未激活");
			}else if(account.isStopped()){
				//throw new AccountStoppedException("account is stopped");
				throw new LockedException("帐户暂停");
			}else {
				Account runtimeAccount = accountManager.login(account,auth.getSession());
				if(null!=runtimeAccount){
					AccountAuthentication accAuth = new AccountAuthentication(runtimeAccount);
					accAuth.setSession(auth.getSession());
					return accAuth;
				}else{
				   throw new net.sf.acegisecurity.concurrent.ConcurrentLoginException("超过最大登陆用户数");
				}
			}
		}
		return authentication;
	}

	public boolean supports(Class clazz) {
		return AccountAuthentication.class.isAssignableFrom(clazz);
	}

	public void setAccountManager(IAccountManager man){
	    this.accountManager = man;	
	}
	
	public void afterPropertiesSet() throws Exception {
         Assert.notNull(this.accountManager);
    }

	private IAccountManager accountManager;
}
