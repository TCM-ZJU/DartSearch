package edu.zju.tcmsearch.util.web;
import java.util.HashMap;
import java.util.Map;

import edu.zju.tcmsearch.secure.domain.account.Account;

public class AccountMessageMapper {

	private static Map<String,String> mapper = new HashMap<String,String>();
	
	public AccountMessageMapper(){}
	
	public String get(String str){
		String value = mapper.get(str);
		return null==value ? "":value;
	}
	
	static {
		mapper.put(Account.ACCOUNT_STATUS_INACTIVATED,"未激活");
		mapper.put(Account.ACCOUNT_STATUS_NORAML,"正常");
		mapper.put(Account.ACCOUNT_STATUS_STOPPED,"暂停");
		mapper.put(Account.CHARGE_TYPE_ONFLOW,"流量计费");
		mapper.put(Account.CHARGE_TYPE_ONMONTH,"按月计费");
		mapper.put(Account.TYPE_IP,"IP帐户");
		mapper.put(Account.TYPE_USERPASS,"密码帐户");
		mapper.put(Account.INVAILD_ACCOUNT,"无效帐户");
	}
}
