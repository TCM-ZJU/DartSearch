package edu.zju.tcmsearch.secure.service;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;
import junit.framework.Assert;
import edu.zju.tcmsearch.dao.impl.secure.AccountDAO;
import edu.zju.tcmsearch.secure.domain.access.GrantedAuthorityConstant;
import edu.zju.tcmsearch.secure.domain.account.Account;
import net.sf.acegisecurity.GrantedAuthority;

public class InitDataBase extends TestCase{
	AccountDAO dao;
	ClassPathXmlApplicationContext context;

	public void setUp(){
		context = new ClassPathXmlApplicationContext(new String[]{"accountDBContext-local.xml"});
		Assert.assertNotNull(context);
		dao = (AccountDAO)context.getBean("AccountDAO");
	}
	
	public void testInitDB(){
		Account acc= new Account();
		acc.setId(0);
		dao.accountDelete(acc);
		acc.setGroupId(Account.GLOBAL_GROUP_ID);
		acc.setChargeType(Account.CHARGE_TYPE_ONMONTH);
		acc.setStartDate("2006-7-1");
		acc.setTotalDate(10);
		acc.setLoginType(Account.TYPE_USERPASS);
		acc.setUsername("TCM");
		acc.setPassword("TCM");
		acc.setStatus(Account.ACCOUNT_STATUS_NORAML);
		acc.setGrantedAuthority(new GrantedAuthority[]{GrantedAuthorityConstant.AUTHORITY_ADMINISTRATOR,GrantedAuthorityConstant.AUTHORITY_SERVENT,GrantedAuthorityConstant.AUTHORITY_CUSTOMER});
		acc.calculateMaxConcurrence();
		dao.accountInsert(acc);
		dao.updateAuthority(acc);
		
		acc.setId(1);
		dao.accountDelete(acc);
		acc.setLoginType(Account.TYPE_IP);
		acc.setIPAddress("127.0.0.1");
		acc.setMask("255.255.255.255");
		acc.calculateMaxConcurrence();
		dao.accountInsert(acc);
		dao.updateAuthority(acc);		
	}
	
	public static void main(String[] args){
		InitDataBase init = new InitDataBase();
		init.setUp();
		init.testInitDB();
	}

}
