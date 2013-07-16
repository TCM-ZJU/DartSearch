package edu.zju.tcmsearch.exception.secure;

import net.sf.acegisecurity.AuthenticationException;

public class AccountNotFoundException extends AuthenticationException {

	public AccountNotFoundException(String msg){
		super(msg);
	}
	
	public AccountNotFoundException(String msg,Throwable t){
		super(msg,t);
	}
}
