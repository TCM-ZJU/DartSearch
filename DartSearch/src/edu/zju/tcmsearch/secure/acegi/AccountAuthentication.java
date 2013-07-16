package edu.zju.tcmsearch.secure.acegi;

import javax.servlet.http.HttpSession;

import net.sf.acegisecurity.GrantedAuthority;
import net.sf.acegisecurity.Authentication;
import edu.zju.tcmsearch.secure.domain.account.Account;
import edu.zju.tcmsearch.secure.domain.account.AccountDetial;

public class AccountAuthentication implements  Authentication{

	public boolean authenticated = false;
	public Account account;
	
	private boolean authByIP;
	private String IPAddress;
	private String username;
	private String passowrd;
	
	private HttpSession session;
	
	public AccountAuthentication(String IP){
		   this.authenticated = false;
		   this.authByIP = true;
		   this.IPAddress = IP;
	}
	
	public AccountAuthentication(String username, String password){
		   this.authenticated = false;
		   this.authByIP = false;
		   this.username = username;
		   this.passowrd = password;
	}

	public AccountAuthentication(Account account){
		   this.account = account;
		   this.authenticated = true;
	}
	
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
              throw new IllegalArgumentException(
                  "Cannot set this token to trusted - use constructor AccountAuthentication(Account account)");
        }
        this.authenticated = isAuthenticated;
	}

	public boolean isAuthenticated() {
		return this.authenticated;
	}

	public GrantedAuthority[] getAuthorities() {
		if(null!=account){
			return account.getGrantedAuthority();
		}
		return new GrantedAuthority[0];
	}

	public String getCredentials() {
		if(null!=account){
			if(account.getLoginType().compareTo(Account.TYPE_IP)==0)
				return account.getIPAddress();
			else
				return account.getPassword();
		}
		return null;
	}

	public Account getDetails() {
		return account;
	}

	public Integer getPrincipal() {
		if(null!=account)
		   return new Integer(account.getId());
		else
		   return null;
	}

	public String getName() {
		if(null!=account)
		   return account.getUniqueName();
		else
		   return null;
	}
	
	public boolean isAuthenticatedByIP(){
		return this.authByIP;
	}
	
	public String getIPAddress(){
		return this.IPAddress;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public String getPassword(){
		return this.passowrd;
	}
	
	public void setSession(HttpSession session){
		this.session = session;
	}
	
	public HttpSession getSession(){
		return this.session;
	}
}
