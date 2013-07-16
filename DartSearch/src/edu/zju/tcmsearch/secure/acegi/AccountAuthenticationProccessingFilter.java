package edu.zju.tcmsearch.secure.acegi;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.AuthenticationException;
import net.sf.acegisecurity.ui.AbstractProcessingFilter;

import edu.zju.tcmsearch.exception.secure.InvaildLoginTypeException;
import edu.zju.tcmsearch.secure.acegi.AccountAuthentication;

public class AccountAuthenticationProccessingFilter extends	AbstractProcessingFilter {
	
	private static Logger logger= Logger.getLogger(AccountAuthenticationProccessingFilter.class);
	
    public static final String ACEGI_SECURITY_FORM_USERNAME_KEY = "j_username";
    public static final String ACEGI_SECURITY_FORM_PASSWORD_KEY = "j_password";
    public static final String ACEGI_SECURITY_FORM_LOGINTYPE_KEY = "j_loginType";
    public static final String ACEGI_SECURITY_LAST_USERNAME_KEY = "ACEGI_SECURITY_LAST_USERNAME";

	@Override
	public String getDefaultFilterProcessesUrl() {
		return "/j_acegi_security_check";
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		String loginType = request.getParameter(ACEGI_SECURITY_FORM_LOGINTYPE_KEY);
		String username = request.getParameter(ACEGI_SECURITY_FORM_USERNAME_KEY);
		String password = request.getParameter(ACEGI_SECURITY_FORM_PASSWORD_KEY);
		
		if(null==loginType)
			throw new InvaildLoginTypeException("LoginType Missing");
		
		if(loginType.compareTo("LOGIN_TYPE_IP")==0){
			 String IP = request.getRemoteAddr();
			 request.getSession().setAttribute(ACEGI_SECURITY_LAST_USERNAME_KEY,IP);
			 logger.debug("attemptAuthentication set LOGIN_TYPE_IP , IP = "+IP);
			 AccountAuthentication auth = new AccountAuthentication(IP);
			 auth.setSession(request.getSession());
			 return this.getAuthenticationManager().authenticate(auth); 
		}else if(loginType.compareTo("LOGIN_TYPE_USERPASSWORD")==0){
			 username = username==null ? "":username.toUpperCase();
			 password = password==null ? "":password;
			 request.getSession().setAttribute(ACEGI_SECURITY_LAST_USERNAME_KEY,username);
			 logger.debug("attemptAuthentication set LOGIN_TYPE_USERPASSWORD , username = "+username +" password = "+password);
			 AccountAuthentication auth = new AccountAuthentication(username,password);
			 auth.setSession(request.getSession());
			 return this.getAuthenticationManager().authenticate(auth);
		}else 
			 throw new InvaildLoginTypeException("Invaild Login Type");
	}
}
