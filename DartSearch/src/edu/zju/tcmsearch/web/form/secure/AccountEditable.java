package edu.zju.tcmsearch.web.form.secure;

import edu.zju.tcmsearch.secure.domain.access.GrantedAuthorityConstant;
import edu.zju.tcmsearch.secure.domain.account.Account;
/*
 * getXX() == true : when XX is NOT Editable (Disabled in HTML)
 */
public class AccountEditable {
	private boolean invisable = false;
	private boolean loginType = true;
	private boolean status = true;
	private boolean username= true;
	private boolean password = true;
	private boolean IPAddress = true;
	private boolean mask = true;
	private boolean chargeType= true;
	private boolean startDate = true;
	private boolean totalDate = true;
	private boolean totalFlow = true;
	private boolean usedFlow  = true;
	private boolean id        = true;
	private boolean groupId   = true;
	private boolean maxConcurrence=true;

	private boolean userInfo = true;
	private boolean grantedAuthority = true;
	
	public boolean getGroupId(){
		return groupId;
	}
	
	public void setGroupId(boolean gid){
		this.groupId = gid;
	}
	
	public boolean getInvisable(){
		return invisable;
	}
	
	public void setInvisable(boolean invisable){
		this.invisable = invisable;
	}
	
	public boolean getMask() {
		return mask;
	}

	public void setMask(boolean mask) {
		this.mask = mask;
	}


	public void setLoginType(boolean type){
		this.loginType = type;
	}
	
	public void setStatus(boolean status){
		this.status = status;
	}
	
	public void setUsername(boolean username){
		this.username = username;
	}
	
	public void setPassword(boolean password) {
		this.password = password;
	}
	
	public void setIPAddress(boolean ip){
		this.IPAddress = ip;
	}
	
	public void setChargeType(boolean chargeType){
		this.chargeType = chargeType;
	}
	
	public void setStartDate(boolean date) {
		this.startDate = date;
	}
	
	public void setTotalDate(boolean date) {
		this.totalDate = date;
	}
	
	public void setTotalFlow(boolean flow) {
		this.totalFlow = flow;
	}
	
	public void setUsedFlow(boolean flow) {
		this.usedFlow = flow;
	}

	public void setId(boolean id) {
		this.id = id;
	}
	
	public void setGrantedAuthority(boolean grantedAuthority) {
		this.grantedAuthority = grantedAuthority;
	}
	
	public boolean getLoginType(){
		return this.loginType;
	}
	
	public boolean getStatus(){
		return this.status;
	}
	
	public boolean getUsername(){
		return this.username;
	}
	
	public boolean getPassword() {
		return this.password;
	}
	
	public boolean getIPAddress(){
		return this.IPAddress;
	}
	
	public boolean getChargeType(){
		return this.chargeType;
	}
	
	public boolean getStartDate() {
		return this.startDate;
	}
	
	public boolean getTotalDate() {
		return this.totalDate;
	}
	
	public boolean getTotalFlow() {
		return this.totalFlow;
	}
	
	public boolean getUsedFlow() {
		return this.usedFlow;
	}	
	
	public boolean getId() {
		return this.id;
	}

	public boolean getMaxConcurrence(){
		return this.maxConcurrence;
	}
	
	public boolean getGrantedAuthority() {
		return this.grantedAuthority;
	}	
	

	public boolean getUserInfo() {
		return this.userInfo;
	}

	public void setUserInfo(boolean userInfo) {
		this.userInfo = userInfo;
	}
	
	public void setMaxConcurrence(boolean maxCon){
		this.maxConcurrence = maxCon;
	}
	
	private AccountEditable(){
		
	}
	
	public static AccountEditable Administrator = new AccountEditable();
	
	public static AccountEditable Servent = new AccountEditable();
	
	public static AccountEditable Customer = new AccountEditable();
	
	public static AccountEditable Stranger = new AccountEditable();
	
	static{
		Administrator.setLoginType(false);
		Administrator.setChargeType(false);
		Administrator.setGrantedAuthority(false);
		Administrator.setIPAddress(false);
		Administrator.setMask(false);
		Administrator.setUsername(false);
		Administrator.setPassword(false);
		Administrator.setStartDate(false);
		Administrator.setTotalDate(false);
		Administrator.setTotalFlow(false);
		Administrator.setUsedFlow(false);
		Administrator.setMaxConcurrence(false);
		Administrator.setUserInfo(false);
		Administrator.setStatus(false);
		Administrator.setGroupId(false);
		
		Customer.setUserInfo(false);
		Customer.setPassword(false);
		
		Stranger.setInvisable(true);
	}
	
	public static AccountEditable getInstance(Account account){
		if(account.hasAuthority(GrantedAuthorityConstant.AUTHORITY_ADMINISTRATOR.toString())){
			return AccountEditable.Administrator;
		}else if(account.hasAuthority(GrantedAuthorityConstant.AUTHORITY_CUSTOMER.toString())){
			return AccountEditable.Customer;
		}else{
			return AccountEditable.Servent;
		}
	}
	
	public void copy(Account s,Account t){
		if(loginType == false)
			t.setLoginType(s.getLoginType());
		if(status == false)
			t.setStatus(s.getStatus());
		if(username== false)
			t.setUsername(s.getUsername());
		if(password == false)
			t.setPassword(s.getPassword());
		if(IPAddress == false)
			t.setIPAddress(s.getIPAddress());
		if(mask == false)
			t.setMask(s.getMask());
		if(chargeType == false)
			t.setChargeType(s.getChargeType());
		if(startDate == false)
			t.setStartDate(s.getStartDate());
		if(totalDate == false)
			t.setTotalDate(s.getTotalDate());
		if(totalFlow == false)
			t.setTotalFlow(s.getTotalFlow());
		if(usedFlow  == false)
			t.setUsedFlow(s.getUsedFlow());
		if(id        == false)
			t.setId(s.getId());
		if(maxConcurrence==false)
			t.setMaxConcurrence(s.getMaxConcurrence());
		if(grantedAuthority == false)  
			t.setGrantedAuthority(s.getGrantedAuthority());
		if(userInfo == false){
			s.getUserInfo().setId(t.getUserInfo().getId());
			t.setUserInfo(s.getUserInfo());		
		}
		if(groupId == false){
			t.setGroupId(s.getGroupId());
		}
	}
}
