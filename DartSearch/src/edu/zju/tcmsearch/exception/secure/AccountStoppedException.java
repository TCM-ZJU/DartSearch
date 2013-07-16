package edu.zju.tcmsearch.exception.secure;

import net.sf.acegisecurity.AuthenticationException;

public class AccountStoppedException extends AuthenticationException {
	
	public AccountStoppedException(String msg){
		super(msg);
	}
	
	public AccountStoppedException(String msg,Throwable t){
		super(msg,t);
	}
}
