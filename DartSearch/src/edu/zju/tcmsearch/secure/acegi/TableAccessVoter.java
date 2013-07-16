package edu.zju.tcmsearch.secure.acegi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;

import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.ConfigAttribute;
import net.sf.acegisecurity.ConfigAttributeDefinition;
import net.sf.acegisecurity.GrantedAuthority;
import net.sf.acegisecurity.intercept.web.FilterInvocation;
import net.sf.acegisecurity.vote.AccessDecisionVoter;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import edu.zju.tcmsearch.dao.impl.secure.TableAccessPrivDao;
import edu.zju.tcmsearch.secure.acegi.AccountAuthentication;
import edu.zju.tcmsearch.secure.domain.access.TableAccessPrivilege;
import edu.zju.tcmsearch.secure.domain.fee.FeeRecord;
import edu.zju.tcmsearch.secure.service.fee.IFeeManager;

import javax.servlet.http.*;
import javax.servlet.*;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 et ming
 * 2005年
 */
public class TableAccessVoter implements AccessDecisionVoter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TableAccessVoter.class);

	private TableAccessPrivDao tableAccessPrivDao;
	
	private IFeeManager feeManager;

	private String rolePrefix = "TABLE_ACCESS_";

    public void setRolePrefix(String role){
    	this.rolePrefix = role;
    }
	
	public String getRolePrefix(){
		return this.rolePrefix;
	}
	
	public TableAccessPrivDao getTableAccessPrivDao() {
		return tableAccessPrivDao;
	}

	public void setTableAccessPrivDao(TableAccessPrivDao tableAccessPrivDao) {
		this.tableAccessPrivDao = tableAccessPrivDao;
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

	public int vote(Authentication authentication, Object object,
			ConfigAttributeDefinition config) {

         FilterInvocation fi = (FilterInvocation) object;
        String tableIdentity = fi.getRequest().getParameter("tableIdentity");
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
		      if(checkPrivilegeTable(authentication, tableIdentity)) {
		    	 AccountAuthentication AccAuth = (AccountAuthentication) authentication;
		    	 if(AccAuth.account.isStopped()){
		    		 return ACCESS_DENIED;
		    	 }
		    	 
		    	 FeeRecord rec = new FeeRecord();
		    	 int fee = feeManager.getTableFee(tableIdentity);
		    	 String goodsId = "Table="+tableIdentity+",PrimaryKey="+fi.getRequest().getParameter("primaryKey");	
		    	 Date now = new Date();
		    	 rec.setIntTime(now.getTime());
		    	 rec.setTime(now.toLocaleString());
		    	 rec.setAccountId(AccAuth.getDetails().getId());		    	 
                 rec.setType(FeeRecord.TABLE_RECORD_FEE);
		    	 rec.setGoodsIdentity(goodsId);
		    	 rec.setContent(fi.getRequestUrl());	
		    	 rec.setFee(fee);
		    	 feeManager.log(rec);
		    	 AccAuth.account.useFlow(fee);
			     return  ACCESS_GRANTED;
		      }else{
		    	 logger.debug("ACCESS_DENIED");
			     return  ACCESS_DENIED;
		      }
           }
        }
		return result;
	}

	private boolean checkPrivilegeTable(Authentication authentication,
			String tableIdentity) {
		
		Assert.isInstanceOf(AccountAuthentication.class,authentication,"authenticate AccountAuthentication only");
		AccountAuthentication auth = (AccountAuthentication) authentication;
		
		if (null==tableIdentity){
			return true; //没有这一项,跳过检查
		}
		
   	    List<TableAccessPrivilege> privList= getTableAccessPrivDao().getRoleTableAccessPrivileges(auth.getDetails().getId());
		for (TableAccessPrivilege tableAccessPrivilege:privList){
		  if (tableAccessPrivilege.getTableIdentity().equals(tableIdentity)){
			  return tableAccessPrivilege.isReadPriv();
		  }
		}
		return false;
	}
	

	public void setFeeManager(IFeeManager feeManager){
		this.feeManager = feeManager;
	}
}
