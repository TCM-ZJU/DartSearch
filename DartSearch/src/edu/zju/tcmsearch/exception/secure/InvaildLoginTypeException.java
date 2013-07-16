package edu.zju.tcmsearch.exception.secure;

import net.sf.acegisecurity.AuthenticationException;

public class InvaildLoginTypeException extends AuthenticationException {

	public InvaildLoginTypeException(String msg){
		super(msg);
	}
	
	public InvaildLoginTypeException(String msg,Throwable t){
		super(msg,t);
	}
}
