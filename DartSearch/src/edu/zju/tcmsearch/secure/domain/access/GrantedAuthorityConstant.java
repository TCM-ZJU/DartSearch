package edu.zju.tcmsearch.secure.domain.access;
import net.sf.acegisecurity.GrantedAuthority;
import net.sf.acegisecurity.GrantedAuthorityImpl;

public interface GrantedAuthorityConstant {

	public static GrantedAuthority AUTHORITY_ADMINISTRATOR = new GrantedAuthorityImpl("ROLE_TECH_ADMIN");
	
	public static GrantedAuthority AUTHORITY_SERVENT = new GrantedAuthorityImpl("ROLE_CUSTOMER_ADMIN");
	
	public static GrantedAuthority AUTHORITY_CUSTOMER = new GrantedAuthorityImpl("ROLE_USER");
}
