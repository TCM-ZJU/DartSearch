package edu.zju.tcmsearch.secure.domain.account;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.DateFormat;


import edu.zju.tcmsearch.secure.domain.access.InternetAddress;
import net.sf.acegisecurity.GrantedAuthority;
import javax.servlet.http.HttpSession;


public class Account {

	private String loginType = "";
	private String status = "";
	private String username= "";
	private String password = "";
	private String IPAddress = "";
	private String mask = "";
	private String chargeType= "";
	private String startDate = "";
	private int    totalDate = 0;
	private int    totalFlow = 0;
	private int    usedFlow  =0;
	private int    id =0;
	private int    maxConcurrence=0;
	private int    event = 0;
	private String groupId =Account.TEMPAL_GROUP_ID;
	private List<HttpSession> concurrence=new ArrayList<HttpSession>();
	
	private UserInfo userInfo= new UserInfo();
	GrantedAuthority[] grantedAuthority = new GrantedAuthority[0];
	
	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = null==mask ? "":mask;
	}


	public void setLoginType(String type){
		this.loginType = null==type ? "":type;
	}
	
	public void setStatus(String status){
		this.status = null==status ? "":status;
	}
	
	public void setUsername(String username){
		this.username = null==username ? "":username.toUpperCase();
	}
	
	public void setPassword(String password) {
		this.password = null==password ? "":password;
	}
	
	public void setIPAddress(String ip){
		this.IPAddress = null==ip ? "":ip;
	}
	
	public void setChargeType(String chargeType){
		this.chargeType = null==chargeType?"":chargeType;
	}
	
	public void setStartDate(String date) {
		this.startDate = null==date ? "":date;
	}
	
	public void setTotalDate(int date) {
		this.totalDate = date;
	}
	
	public void setTotalFlow(int flow) {
		this.totalFlow = flow;
	}
	
	public void setUsedFlow(int flow) {
		this.usedFlow = flow;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setGrantedAuthority(GrantedAuthority[] grantedAuthority) {
		this.grantedAuthority = grantedAuthority;
	}
	
	public void setGroupId(String gid){
		this.groupId = null==gid || "".equals(gid.trim()) ? Account.TEMPAL_GROUP_ID:gid;
	}
	

	public Account(){
		userInfo = new UserInfo();
	}
	

	public String getGroupId(){
		return this.groupId;
	}
	
	public String getLoginType(){
		return this.loginType;
	}
	
	public String getStatus(){
		return this.status;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getIPAddress(){
		return this.IPAddress;
	}
	
	public String getChargeType(){
		return this.chargeType;
	}
	
	public String getStartDate() {
		return this.startDate;
	}
	
	public int getTotalDate() {
		return this.totalDate;
	}
	
	public int getTotalFlow() {
		return this.totalFlow;
	}
	
	public int getUsedFlow() {
		return this.usedFlow;
	}	
	
	public int getId() {
		return this.id;
	}

	public int getMaxConcurrence(){
		return this.maxConcurrence;
	}
	
	public GrantedAuthority[] getGrantedAuthority() {
		return this.grantedAuthority;
	}	
	
	public String getUniqueName(){
		String name;
		if(loginType.compareTo(TYPE_IP)==0){
			name = TYPE_IP+":"+InternetAddress.getNetAddress(IPAddress,mask);
		}else if(loginType.compareTo(TYPE_USERPASS)==0){
			name = TYPE_USERPASS+":"+username;
		}else
			name = INVAILD_ACCOUNT;
		return name;
	}
	
	public boolean isStopped(){
		return status==null ? false : status.compareTo(ACCOUNT_STATUS_STOPPED)==0;
	}
	
	public boolean isInActviated(){
		return status==null ? false : status.compareTo(ACCOUNT_STATUS_INACTIVATED)==0;
	}
	
	public boolean isIPLogin(){
		return loginType==null ? false : loginType.compareTo(TYPE_IP)==0;
	}
	
	public static final String TYPE_IP   = "LOGIN_TYPE_IP";
	public static final String TYPE_USERPASS = "LOGIN_TYPE_USERPASSWORD";
	public static final String ACCOUNT_STATUS_NORAML = "ACCOUNT_STATUS_NORAML";
	public static final String ACCOUNT_STATUS_STOPPED = "ACCOUNT_STATUS_STOPPED";
	public static final String ACCOUNT_STATUS_INACTIVATED = "ACCOUNT_STATUS_INACTIVATED";
	public static final String CHARGE_TYPE_ONMONTH = "CHARGE_TYPE_ONMONTH";
	public static final String CHARGE_TYPE_ONFLOW = "CHARGE_TYPE_ONFLOW";
	public static final String INVAILD_ACCOUNT = "INVAILD_ACCOUNT";
	
	public static final String GLOBAL_GROUP_ID = "GLOBAL_GROUP_ID";
	public static final String TEMPAL_GROUP_ID = "TEMPAL_GROUP_ID";


	public boolean isInGlobalGroup(){
		return null==groupId ? false:groupId.equals(GLOBAL_GROUP_ID);
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	
	public void useFlow(int fee){
		if(chargeType.compareTo(CHARGE_TYPE_ONFLOW)==0){
			usedFlow+=fee;
			if(usedFlow>=totalFlow)
				this.status = ACCOUNT_STATUS_STOPPED;
		}
	}
	
	public void checkFlow(){
		if(chargeType.equals(CHARGE_TYPE_ONFLOW) && usedFlow>=totalFlow && status.equals(ACCOUNT_STATUS_NORAML))
			this.status = ACCOUNT_STATUS_STOPPED;		
	}
	
	public void checkTimeOut(){
		if(chargeType.equals(CHARGE_TYPE_ONMONTH) && status.equals(ACCOUNT_STATUS_NORAML)){
		try{	
			Date startDate = DateFormat.getDateInstance().parse(this.startDate);
			Date now = new Date();
            if(now.after(startDate) && now.getTime()/( 24*3600*1000) <= startDate.getTime()/( 24*3600*1000) + this.totalDate*30){
            	//It's Ok
            }else{
            	this.status = ACCOUNT_STATUS_STOPPED;
            }
		}catch(java.text.ParseException e){
			this.status = ACCOUNT_STATUS_STOPPED;
	    }
		}
	}
	
	public boolean equals(Object obj){
		if(obj instanceof Account){
			Account account = (Account)obj; 
			return this.getUniqueName().equals(account.getUniqueName());
		}else{
			return false;
		}
	}
	
	public String validate(){
		if(this.loginType.equals(Account.TYPE_IP))
		{
			if(null==InternetAddress.getInternetAddress(this.IPAddress)||
			   null==InternetAddress.getInternetAddress(this.mask))
			{
				return "ACCOUNT_INVALID_IPADDRESS";
			}
		}
		else if(this.loginType.equals(Account.TYPE_USERPASS))
		{
			if(!this.username.matches("[a-zA-Z][\\w_]*"))
			{
				return "ACCOUNT_INVALID_USERNAME";
			}else if(!this.password.matches("[\\w_]*"))
			{
				return "ACCOUNT_INVALID_PASSWORD";
			}
		}		
		else
		{
			return "ACCOUNT_INVALID_LOGINTYPE";
		}
		
		if(this.chargeType.equals(Account.CHARGE_TYPE_ONFLOW)){
			if(this.totalFlow<=0 || this.usedFlow<0){
				return "ACCOUNT_INVALID_FLOW";
			}
		}else if(this.chargeType.equals(Account.CHARGE_TYPE_ONMONTH)){
			try{
				Date date = DateFormat.getDateInstance().parse(this.startDate);
		    }catch(java.text.ParseException e){
			  return "ACCOUNT_INVALID_STARTDATE";
		    }
		    if(this.totalDate<=0){
		    	return "ACCOUNT_INVALID_TOTALDATE";
		    }
		}
		else{
			return "ACCOUNT_INVALID_CHARGETYPE";
		}
		
		return "ACCOUNT_VALID";
	}
	
	public boolean hasAuthority(String authority){
		for(GrantedAuthority GA : grantedAuthority){
			if(GA.getAuthority().equals(authority)){
				return true;
			}
		}
		return false;
	}
	
	public void setMaxConcurrence(int maxCon){
		this.maxConcurrence = maxCon;
	}
	
	synchronized public boolean addConcurrence(HttpSession session){
		if(null!=session && concurrence.size()<maxConcurrence && !concurrence.contains(session)){
			concurrence.add(session);
			return true;
		}else{
			return false;
		}
	}
	
	synchronized public void removeConurrence(HttpSession session){
		concurrence.remove(session);
	}
	
	synchronized public void removeDeadConurrence(){
		while(concurrence.contains(null)){
			concurrence.remove(null);
		}
	}
	
	synchronized public void setConcurrence(List<HttpSession> concurrence){
		this.concurrence = concurrence;
	}
	
	public List<HttpSession> getConcurrence(){
		return this.concurrence;
	}
	
	public int getConcurrenceSize(){
		return concurrence.size();
	}
	
	public void calculateMaxConcurrence(){
		if(this.loginType.equals(Account.TYPE_USERPASS)){
			this.maxConcurrence = 1;
		}else if(this.loginType.equals(Account.TYPE_IP)){
			InternetAddress maskA = InternetAddress.getInternetAddress(this.mask);
			this.maxConcurrence = null==maskA ? 0:maskA.getMaxHostsInSubnet();
		}else{
			this.maxConcurrence = 0;
		}
	}
	
	public static final String ACCOUNT_SAVE_IN_SESSION_1986_KEY ="ACCOUNT_SAVE_IN_SESSION_1986_KEY"; 
	public static final int ACCOUNT_EVENT_NEW_USER     = 0x00000001;
	public static final int ACCOUNT_EVENT_NEW_ONTOLOGY = 0x00000002;
	public static final int ACCOUNT_EVENT_NEW_TABLE    = 0x00000004;
	public static final int ACCOUNT_EVENT_RUNOUT_FEE    = 0x00000008;
	public static final String S_ACCOUNT_EVENT_NEW_USER     = "S_ACCOUNT_EVENT_NEW_USER;";
	public static final String S_ACCOUNT_EVENT_NEW_ONTOLOGY = "S_ACCOUNT_EVENT_NEW_ONTOLOGY;";
	public static final String S_ACCOUNT_EVENT_NEW_TABLE    = "S_ACCOUNT_EVENT_NEW_TABLE;";	
	public static final String S_ACCOUNT_EVENT_RUNOUT_FEE    = "S_ACCOUNT_EVENT_RUNOUT_FEE;"; 
	
	public boolean hasEvent(int eventMask){
		return (this.event & eventMask) !=0 ;
	}
	
	public void setEvent(int eventMask){
		this.event |=  eventMask;
	}
	

	public void unsetEvent(int eventMask){
		this.event = this.event & (~eventMask);
	}
	
	public boolean isNewUser(){
		return hasEvent(ACCOUNT_EVENT_NEW_USER);
	}
	
	public boolean hasNewOntology(){
		return hasEvent(ACCOUNT_EVENT_NEW_ONTOLOGY);
	}
	
	public boolean hasNewTable(){
		return hasEvent(ACCOUNT_EVENT_NEW_TABLE);
	}

	public boolean isToRunOutFee(){
		return hasEvent(ACCOUNT_EVENT_RUNOUT_FEE);
	}
	
	public void setNewUser(){
		 setEvent(ACCOUNT_EVENT_NEW_USER);
	}
	
	public void setNewOntology(){
		 setEvent(ACCOUNT_EVENT_NEW_ONTOLOGY);
	}
	
	public void setNewTable(){
		 setEvent(ACCOUNT_EVENT_NEW_TABLE);
	}	
	
	public void setToRunOutFee(){
		setEvent(ACCOUNT_EVENT_RUNOUT_FEE);
	}
	
	public void unsetNewUser(){
		 unsetEvent(ACCOUNT_EVENT_NEW_USER);
	}
	
	public void unsetNewOntology(){
		 unsetEvent(ACCOUNT_EVENT_NEW_ONTOLOGY);
	}
	
	public void unsetNewTable(){
		 unsetEvent(ACCOUNT_EVENT_NEW_TABLE);
	}
	
	public void unsetToRunOutFee(){
		unsetEvent(ACCOUNT_EVENT_RUNOUT_FEE);
	}
	
	public String getEvent(){
		 return (isNewUser()?      S_ACCOUNT_EVENT_NEW_USER:"")+
		        (hasNewOntology()? S_ACCOUNT_EVENT_NEW_ONTOLOGY:"")+
		        (hasNewTable()?    S_ACCOUNT_EVENT_NEW_TABLE:"")+
		        (isToRunOutFee()?    S_ACCOUNT_EVENT_RUNOUT_FEE:"");
	}
	
	public void setEvent(String s){
		this.event = 0;
		if(null==s)
			return ;
		this.event |= (s.lastIndexOf(S_ACCOUNT_EVENT_NEW_USER)>=0 ?     ACCOUNT_EVENT_NEW_USER:0);
		this.event |= (s.lastIndexOf(S_ACCOUNT_EVENT_NEW_TABLE)>=0 ?    ACCOUNT_EVENT_NEW_TABLE:0);
		this.event |= (s.lastIndexOf(S_ACCOUNT_EVENT_NEW_ONTOLOGY)>=0 ? ACCOUNT_EVENT_NEW_ONTOLOGY:0);
		this.event |= (s.lastIndexOf(S_ACCOUNT_EVENT_RUNOUT_FEE)>=0 ?    ACCOUNT_EVENT_RUNOUT_FEE:0);
	}

	public static int ACCOUNT_RUNOUT_FEE_THRESHOLD = 10;
	public static int ACCOUNT_RUNOUT_DAYS_THRESHOLD = 3;
	
	public boolean checkToFeeRunOut(){
		if(this.chargeType.equals(CHARGE_TYPE_ONFLOW)&&this.totalFlow <= this.usedFlow + Account.ACCOUNT_RUNOUT_FEE_THRESHOLD){
			this.setToRunOutFee();
			return true;
		}else if(this.chargeType.equals(CHARGE_TYPE_ONMONTH)){
			try{	
				Date startDate = DateFormat.getDateInstance().parse(this.startDate);
				Date now = new Date();
	            if(now.after(startDate) 
	               && now.getTime()/( 24*3600*1000) <= startDate.getTime()/( 24*3600*1000) + (this.totalDate-Account.ACCOUNT_RUNOUT_DAYS_THRESHOLD)*30){
	            	return false;
	            }else{
	            	this.setToRunOutFee();
	    			return true;
	            }
			}catch(java.text.ParseException e){
				this.setToRunOutFee();
				return true;
		    }			
		}
		this.unsetToRunOutFee();
		return false;
	}
}
