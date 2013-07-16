package edu.zju.tcmsearch.exception.secure;

import net.sf.acegisecurity.AuthenticationException;

public class AccountInActviatedException extends AuthenticationException {
	public AccountInActviatedException(String msg){
		super(msg);
	}
	
	public AccountInActviatedException(String msg,Throwable t){
		super(msg,t);
	}
}
