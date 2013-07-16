package edu.zju.tcmsearch.secure.acegi;

import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import edu.zju.tcmsearch.secure.domain.access.impl.AccessRecord;
import edu.zju.tcmsearch.secure.service.access.IAccessRecordManager;
import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.ConfigAttribute;
import net.sf.acegisecurity.ConfigAttributeDefinition;
import net.sf.acegisecurity.intercept.web.FilterInvocation;
import net.sf.acegisecurity.vote.AccessDecisionVoter;

public class AccessRecordVoter implements AccessDecisionVoter {
	private static final Logger logger = Logger.getLogger(AccessRecordVoter.class);

	private String rolePrefix = "ACCESS_RECORD_";

    public void setRolePrefix(String role){
    	this.rolePrefix = role;
    }
	
	public String getRolePrefix(){
		return this.rolePrefix;
	}
	public boolean supports(ConfigAttribute attribute) {
		if ((attribute.getAttribute() != null)&& attribute.getAttribute().startsWith(rolePrefix)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean supports(Class clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	public int vote(Authentication authentication, Object object,ConfigAttributeDefinition config) {

        FilterInvocation fi = (FilterInvocation) object;
        String url = ((FilterInvocation) object).getRequestUrl();  
        
		if(!(authentication instanceof AccountAuthentication)){
			return ACCESS_ABSTAIN;
		}
        if(!supports(object.getClass())){
        	return ACCESS_ABSTAIN;
        }
        
      
        int result = ACCESS_ABSTAIN;
        Iterator attr = config.getConfigAttributes();
        while(attr.hasNext()){
           ConfigAttribute attribute = (ConfigAttribute)attr.next();	
           if(supports(attribute)){
                 AccountAuthentication auth = (AccountAuthentication) authentication;
                 Integer id = new Integer(auth.getDetails().getId());
                 fi.getRequest().setAttribute("j_acegi_account_id",id);
                 fi.getRequest().setAttribute("j_acegi_account_uniqueName",auth.getDetails().getUniqueName()); 
                 logger.debug("set request parameter j_acegi_account_id = "+id);
			     return  ACCESS_GRANTED;
           }
        }
		return result;        
	}

}
