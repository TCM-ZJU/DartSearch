package edu.zju.tcmsearch.dao.impl.secure;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlUpdate;

import java.util.Collections;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;

import edu.zju.tcmsearch.dao.Expression;
import edu.zju.tcmsearch.dao.secure.IAccountDao;
import edu.zju.tcmsearch.secure.domain.access.InternetAddress;
import edu.zju.tcmsearch.secure.domain.account.Account;

import net.sf.acegisecurity.GrantedAuthority;
import net.sf.acegisecurity.GrantedAuthorityImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
  
import java.util.List;
  
import javax.sql.DataSource;
/*
create table ACCOUNT
(
  LOGINTYPE      VARCHAR2(50) not null,
  USERNAME       VARCHAR2(50),
  PASSWORD       VARCHAR2(50),
  STATUS         VARCHAR2(50) not null,
  IPADDRESS      VARCHAR2(100),
  MASK           VARCHAR2(100),
  SUBNETADDRESS  VARCHAR2(100),
  CHARGETYPE     VARCHAR2(50),
  STARTDATE      VARCHAR2(50),
  TOTALDATE      INTEGER default 0,
  TOTALFLOW      INTEGER default 0,
  USEDFLOW       INTEGER default 0,
  MAXCONCURRENCE INTEGER default 0,
  ACCID          INTEGER default 1 not null,
  GROUPID        VARCHAR2(50),
  EVENT          VARCHAR2(100),
)
 */
public class AccountDAO implements IAccountDao{

	private DataSource dataSource;
	private List<InternetAddress> mask_cache=null;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void invalidate(){
		mask_cache = null;
	}
	
	public void updateMaskCache(String mask){
		if(null==mask_cache ){
			return;
		}
		
		InternetAddress maskA = InternetAddress.getInternetAddress(mask);
		if(null!=maskA && !mask_cache.contains(maskA)){
			mask_cache.add(maskA);
		}
	}

	public void accountDelete(Account account) {
		AuthorityDelete authDelete = new AuthorityDelete(getDataSource());
		authDelete.delete(new Integer(account.getId()));
		AccountDelete accountDelete=new AccountDelete(getDataSource());
		accountDelete.delete(new Integer(account.getId()));
		
	}
   
	public void update(Account account) {
		AccountUpdate accountUpdate = new AccountUpdate(getDataSource());
		accountUpdate.update(account);
		if(account.getLoginType().equals(Account.TYPE_IP)){
			updateMaskCache(account.getMask());
		}		
	}

	public Account loadByIp(String inip){
		if(null==mask_cache){
			AccountLoadAllMask accountLAM = new AccountLoadAllMask(getDataSource());
			mask_cache = accountLAM.execute();
		}
		InternetAddress inipAddress = InternetAddress.getInternetAddress(inip);
		if(mask_cache.size()>0){
		    return loadByIp(inipAddress,mask_cache);
		}else{
			return null;
		}
	}
	
	public Account loadByIp(InternetAddress inip,List<InternetAddress> mask) {
		AccountLoadByIp accountLoadAll = new AccountLoadByIp(getDataSource(),mask,inip);
		List<Account> result = accountLoadAll.execute();
		if(result.size()!=0){
			  Account account = (Account)result.get(0);
			  AccountLoadAuthorityById loadAuthority = new AccountLoadAuthorityById(getDataSource());
			  List<GrantedAuthority> resGA= loadAuthority.execute(account.getId());
			  GrantedAuthority[] GAs = resGA.toArray(new GrantedAuthority[0]);
			  account.setGrantedAuthority(GAs);
		      return account;
		}
		else 
			return null;
	}

	public Account loadByUserName(String userName, String passWord) {
		AccountLoadByUserName accountLoadByUserName = new AccountLoadByUserName(getDataSource());
		String[] para = {userName,passWord};
		
		List<Account> result = accountLoadByUserName.execute(para);
		if(result.size()!=0){
			  Account account = (Account)result.get(0);
			  AccountLoadAuthorityById loadAuthority = new AccountLoadAuthorityById(getDataSource());
			  List<GrantedAuthority> resGA= loadAuthority.execute(account.getId());
			  GrantedAuthority[] GAs = resGA.toArray(new GrantedAuthority[0]);
			  account.setGrantedAuthority(GAs);
		      return account;
		}
			else return null;
	}

	public Account loadByUserName(String userName) {
		AccountLoadByUserNameOnly accountLoadByUserNameOnly = new AccountLoadByUserNameOnly(getDataSource());
		String[] para = {userName};
		
		List<Account> result = accountLoadByUserNameOnly.execute(para);
		if(result.size()!=0){
			  Account account = (Account)result.get(0);
			  AccountLoadAuthorityById loadAuthority = new AccountLoadAuthorityById(getDataSource());
			  List<GrantedAuthority> resGA= loadAuthority.execute(account.getId());
			  GrantedAuthority[] GAs = resGA.toArray(new GrantedAuthority[0]);
			  account.setGrantedAuthority(GAs);
		      return account;
		}
			else return null;
	}	
	public Account accountFindById(int id) {
		AccountFindById accountFindById=new AccountFindById(getDataSource());
		List<Account> results=accountFindById.execute(new Integer(id));
		if(results.size()!=0){
			  Account account = (Account)results.get(0);
			  AccountLoadAuthorityById loadAuthority = new AccountLoadAuthorityById(getDataSource());
			  List<GrantedAuthority> resGA= loadAuthority.execute(account.getId());
			  GrantedAuthority[] GAs = resGA.toArray(new GrantedAuthority[0]);
			  account.setGrantedAuthority(GAs);
		      return account;
		}
		else
			return null;
	}

	/*
	 * Authority not loaded yet (non-Javadoc)
	 * @see edu.zju.tcmgrid2.secure.account.IAccountDao#loadAll()
	 */
	public List<Account> loadAll() {
		AccountLoadAll accountLoadAll = new AccountLoadAll(getDataSource());
		return accountLoadAll.execute();
	}

	public List<Account> load(Expression expr,List<String> orderBy){
		String where = null==expr? "":" WHERE "+expr.toString();
		String orderby = "";
		if(null!=orderBy&& orderBy.size()>0){
			orderby =" ORDER BY ";
			for(String str:orderBy){
				if(str.startsWith("+")){
					orderby +=str.substring(1)+" asc ,";
				}
				else if(str.startsWith("-")){
					orderby +=str.substring(1)+" desc ,";
				}else{
					orderby +=str+" asc ,";
				}
			}
			orderby +=" (1 = 1)";
		}
		AccountLoadAll accountLoadAll = new AccountLoadAll(getDataSource(),"SELECT * FROM account left outer join userinfo on  account.ACCID = userinfo.ID "+where+orderby);
		return accountLoadAll.execute();
	}
	public void accountInsert(Account account) {
		AccountInsert accountInsert = new AccountInsert(getDataSource());
		accountInsert.insert(account);
		if(account.getLoginType().equals(Account.TYPE_IP)){
			updateMaskCache(account.getMask());
		}
	}

	public Integer getMaximumAccountId(){
		//AccountId 0~99 are reserved 
		Integer accountSize = (Integer) new AccountTableIsEmpty(getDataSource()).execute().get(0);
		if(accountSize.intValue()==0){
			return new Integer(100);
		}else{
			Integer maxId = (Integer)new AccountMaxId(getDataSource()).execute().get(0);
			return maxId.intValue()>=100 ? maxId:new Integer(100); 
		}
	}
	
	protected class AccountMaxId extends MappingSqlQuery{
		  protected AccountMaxId(DataSource ds){
		      super(ds,
			            "SELECT max(accid) as maxID FROM account"
			      );
			      compile();
			    }
		  
		  protected Object mapRow(ResultSet rs, int rownum)
	      throws SQLException{
			  return new Integer(rs.getInt("maxID"));
		  }		
	}
	
	protected class AccountTableIsEmpty extends MappingSqlQuery{
		  protected AccountTableIsEmpty(DataSource ds){
		      super(ds,
			            "SELECT count(*) as AccountSize FROM account"
			      );
			      compile();
			    }
		  
		  protected Object mapRow(ResultSet rs, int rownum)
	      throws SQLException{
			  return new Integer(rs.getInt("AccountSize"));
		  }		
	}
	  
	protected class AccountDelete extends SqlUpdate{
	    protected AccountDelete(DataSource ds){
	      super(ds, "DELETE FROM ACCOUNT WHERE accid= ?");
	      declareParameter(new SqlParameter(Types.INTEGER));
	      compile();
	    }

	    protected void delete(Integer accId){
	      super.update(accId.intValue());
	    }
	  }
	  
	  
	protected class AuthorityDelete extends SqlUpdate{
		    protected AuthorityDelete(DataSource ds){
		      super(ds, "DELETE FROM authorities WHERE authid= ?");
		      declareParameter(new SqlParameter(Types.INTEGER));
		      compile();
		    }

		    protected void delete(Integer accId){
		      super.update(accId.intValue());
		    }
		  }	  

	protected class AuthorityDeleteAll extends SqlUpdate{
	    protected AuthorityDeleteAll(DataSource ds){
	      super(ds, "DELETE FROM authorities");
	      compile();
	    }

	    protected void delete(){
	      super.update();
	    }
	  }		
	  
	protected class AccountInsert extends SqlUpdate{
		  
		    protected AccountInsert(DataSource ds){
		      super(ds, "INSERT INTO ACCOUNT ( logintype,username,password,ipaddress,mask,SubNetAddress,status,chargetype,startdate,totaldate,totalflow,usedflow,accid,maxconcurrence,groupid,event) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.INTEGER));
		      declareParameter(new SqlParameter(Types.INTEGER));
		      declareParameter(new SqlParameter(Types.INTEGER));
		      declareParameter(new SqlParameter(Types.INTEGER));
		      declareParameter(new SqlParameter(Types.INTEGER));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      compile();
		    }

		    protected void insert(Account account){
		      String SubNetAddress = InternetAddress.getNetAddress(account.getIPAddress(),account.getMask());
		      SubNetAddress = null==SubNetAddress ? "":SubNetAddress;
		      Object[] objs=new Object[]{account.getLoginType(),
		    		                     account.getUsername(),account.getPassword(),
		    		                     account.getIPAddress(),account.getMask(),SubNetAddress,
		    		                     account.getStatus(),account.getChargeType(),
		    		                     account.getStartDate(),account.getTotalDate(),
		    		                     account.getTotalFlow(),account.getUsedFlow(),
		    		                     account.getId(),
		    		                     account.getMaxConcurrence(),
		    		                     account.getGroupId(),
		    		                     account.getEvent()
		    		                     };
		      super.update(objs);
		    }
		  }
	 
	  protected class AccountUpdate extends SqlUpdate{
		    protected AccountUpdate(DataSource ds){
		      super(ds,
		            "UPDATE account SET logintype = ?, username = ?, password = ?, ipaddress = ?,mask = ?, SubNetAddress = ? , status = ? , chargetype = ?, startdate = ?, totaldate = ?, totalflow = ?, usedflow = ? ,maxconcurrence = ? , groupid = ?, event = ?  WHERE accid = ?"
		      );
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));		      
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.INTEGER));
		      declareParameter(new SqlParameter(Types.INTEGER));
		      declareParameter(new SqlParameter(Types.INTEGER));
		      declareParameter(new SqlParameter(Types.INTEGER));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.INTEGER));
		      compile();
		    }

		    protected void update(Account account){
			      String SubNetAddress = InternetAddress.getNetAddress(account.getIPAddress(),account.getMask());
			      SubNetAddress = null==SubNetAddress ? "":SubNetAddress;
			      Object[] objs=new Object[]{account.getLoginType(),
			    		                     account.getUsername(),account.getPassword(),
			    		                     account.getIPAddress(),account.getMask(),SubNetAddress,
			    		                     account.getStatus(),account.getChargeType(),
			    		                     account.getStartDate(),account.getTotalDate(),
			    		                     account.getTotalFlow(),account.getUsedFlow(),
			    		                     account.getMaxConcurrence(),
			    		                     account.getGroupId(),
			    		                     account.getEvent(),
			    		                     account.getId()
			    		                     };
		      super.update(objs);
		    }
		  }
	  
	  protected class AccountLoadAllMask extends MappingSqlQuery{
		  protected AccountLoadAllMask(DataSource ds){
		      super(ds,
			            "SELECT mask FROM account where LoginType = '"+Account.TYPE_IP+"' group by mask"
			      );
			      compile();
			    }
		  
		  protected Object mapRow(ResultSet rs, int rownum)
	      throws SQLException{
			  return InternetAddress.getInternetAddress(rs.getString("mask"));
		  }
	  }
	  
	  
	  protected class AccountLoadAll extends MappingSqlQuery{
		    protected AccountLoadAll(DataSource ds){
		      super(ds,
		            "SELECT * FROM account"
		      );
		      compile();
		    }

		    public AccountLoadAll(DataSource dataSource, String sql) {
		    	super(dataSource,sql);
			      compile();
			}

			protected Object mapRow(ResultSet rs, int rownum)
		      throws SQLException{
		      Account account= new Account();
		      account.setLoginType(rs.getString("loginType"));
		      account.setUsername(rs.getString("username"));
		      account.setPassword(rs.getString("password"));
		     
		      account.setStatus(rs.getString("status"));
		      account.setIPAddress(rs.getString("ipaddress"));
		      account.setMask(rs.getString("mask"));
		      
		      account.setChargeType(rs.getString("chargetype"));
		      account.setStartDate(rs.getString("startdate"));
		      account.setTotalDate(new Integer(rs.getInt("totaldate")));
		      account.setTotalFlow(new Integer(rs.getInt("totalflow")));
		      account.setUsedFlow(new Integer(rs.getInt("usedflow")));
		      
		      account.setId(rs.getInt("accid"));
		      account.setMaxConcurrence(rs.getInt("maxconcurrence"));
		      account.setGroupId(rs.getString("groupid"));
		      
		      account.setEvent(rs.getString("event"));
		      return account;
		    }
		  }
	  protected class AccountLoadByIp extends MappingSqlQuery{
		   protected AccountLoadByIp(DataSource ds,List<InternetAddress> masks,InternetAddress ip){
			   super(ds,"");
			   String SubNetConditions = " (";
			   for(InternetAddress mask:masks){
				   SubNetConditions += "SubNetAddress = '"+ip.getNetAddress(mask)+"' OR ";
			   }
			   SubNetConditions += "1=2) ";
			   String Sql = "SELECT * FROM account WHERE"+SubNetConditions+" and LoginType = '"+Account.TYPE_IP+"'";
			   super.setSql(Sql);
			   compile();
		   }
		    
		   protected Object mapRow(ResultSet rs, int rownum)
		      throws SQLException{
		      Account account= new Account();
		      account.setLoginType(rs.getString("loginType"));
		      account.setUsername(rs.getString("username"));
		      account.setPassword(rs.getString("password"));
		     
		      account.setStatus(rs.getString("status"));
		      account.setIPAddress(rs.getString("ipaddress"));
		      account.setMask(rs.getString("mask"));
		      account.setChargeType(rs.getString("chargetype"));
		      account.setStartDate(rs.getString("startdate"));
		     
		      account.setTotalDate(new Integer(rs.getInt("totaldate")));
		      account.setTotalFlow(new Integer(rs.getInt("totalflow")));
		      account.setUsedFlow(new Integer(rs.getInt("usedflow")));
		      
		      account.setId(rs.getInt("accid"));
		      account.setMaxConcurrence(rs.getInt("maxconcurrence"));
              account.setGroupId(rs.getString("groupid"));
              
              account.setEvent(rs.getString("event"));
		      return account;
		    }		   
		   
	  }
	  protected class AccountLoadByUserName extends MappingSqlQuery{
		    protected AccountLoadByUserName(DataSource ds){
		      super(ds,
		            "SELECT * FROM account WHERE username = ? and password = ? and loginType='"+Account.TYPE_USERPASS+"'"
		      );
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      compile();
		    }

		    protected Object mapRow(ResultSet rs, int rownum)
		      throws SQLException{
		      Account account= new Account();
		      account.setLoginType(rs.getString("loginType"));
		      account.setUsername(rs.getString("username"));
		      account.setPassword(rs.getString("password"));
		     
		      account.setStatus(rs.getString("status"));
		      account.setIPAddress(rs.getString("ipaddress"));
		      account.setMask(rs.getString("mask"));
		      account.setChargeType(rs.getString("chargetype"));
		      account.setStartDate(rs.getString("startdate"));
		     
		      account.setTotalDate(new Integer(rs.getInt("totaldate")));
		      account.setTotalFlow(new Integer(rs.getInt("totalflow")));
		      account.setUsedFlow(new Integer(rs.getInt("usedflow")));
		      
		      account.setId(rs.getInt("accid"));
		      account.setMaxConcurrence(rs.getInt("maxconcurrence"));
              account.setGroupId(rs.getString("groupid"));
              
              account.setEvent(rs.getString("event"));
		      return account;
		    }
		  }
	  
	  protected class AccountLoadByUserNameOnly extends MappingSqlQuery{
		    protected AccountLoadByUserNameOnly(DataSource ds){
		      super(ds,
		            "SELECT * FROM account WHERE username = ? and loginType='"+Account.TYPE_USERPASS+"'"
		      );
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      compile();
		    }

		    protected Object mapRow(ResultSet rs, int rownum)
		      throws SQLException{
		      Account account= new Account();
		      account.setLoginType(rs.getString("loginType"));
		      account.setUsername(rs.getString("username"));
		      account.setPassword(rs.getString("password"));
		     
		      account.setStatus(rs.getString("status"));
		      account.setIPAddress(rs.getString("ipaddress"));
		      account.setMask(rs.getString("mask"));
		      account.setChargeType(rs.getString("chargetype"));
		      account.setStartDate(rs.getString("startdate"));
		     
		      account.setTotalDate(new Integer(rs.getInt("totaldate")));
		      account.setTotalFlow(new Integer(rs.getInt("totalflow")));
		      account.setUsedFlow(new Integer(rs.getInt("usedflow")));
		      
		      account.setId(rs.getInt("accid"));
		      account.setMaxConcurrence(rs.getInt("maxconcurrence"));
              account.setGroupId(rs.getString("groupid"));
              
              account.setEvent(rs.getString("event"));
		      return account;
		    }
		  }	  
	  
	  protected class AccountFindById extends MappingSqlQuery{
		    protected AccountFindById(DataSource ds){
		      super(ds,
		            "SELECT * FROM account WHERE accid = ? "
		      );
		      declareParameter(new SqlParameter(Types.INTEGER));
		      compile();
		    }

		    protected Object mapRow(ResultSet rs, int rownum)
		      throws SQLException{
		      Account account= new Account();
		      account.setLoginType(rs.getString("loginType"));
		      account.setUsername(rs.getString("username"));
		      account.setPassword(rs.getString("password"));
		     
		      account.setStatus(rs.getString("status"));
		      account.setIPAddress(rs.getString("ipaddress"));
		      account.setMask(rs.getString("mask"));
		      account.setChargeType(rs.getString("chargetype"));
		      account.setStartDate(rs.getString("startdate"));
		      account.setGroupId(rs.getString("groupid"));
		     
		      account.setTotalDate(new Integer(rs.getInt("totaldate")));
		      account.setTotalFlow(new Integer(rs.getInt("totalflow")));
		      account.setUsedFlow(new Integer(rs.getInt("usedflow")));
		      
		      account.setId(rs.getInt("accid"));
		      account.setMaxConcurrence(rs.getInt("maxconcurrence"));
		      
		      account.setEvent(rs.getString("event"));

		      return account;
		    }
		  }

	 protected class AccountLoadAuthorityById extends MappingSqlQuery{
		    protected AccountLoadAuthorityById(DataSource ds){
			      super(ds,
			            "select * from authorities where authid = ?"
			      );
			      declareParameter(new SqlParameter(Types.INTEGER));
			      compile();
			    }
		    protected Object mapRow(ResultSet rs, int rownum)
		        throws SQLException{
		    	GrantedAuthority authority = new GrantedAuthorityImpl(rs.getString("authority"));
		    	return authority;
		    }
	 }

	 public void updateAuthority(Account account)
	 {
		 new AuthorityDelete(dataSource).delete(new Integer(account.getId()));
		 new AuthorityInsert(dataSource).insert(account);
	 }

	  protected class AuthorityInsert extends SqlUpdate{
		  
		    protected AuthorityInsert(DataSource ds){
		      super(ds, "INSERT INTO authorities ( AuthID,Authority) VALUES (?,?)");
		      declareParameter(new SqlParameter(Types.INTEGER));
		      declareParameter(new SqlParameter(Types.VARCHAR));		      
		      compile();
		    }

		    protected void insert(Account account){
		      Object[] params=new Object[2];
		      params[0]= new Integer(account.getId());
              for(GrantedAuthority authority:account.getGrantedAuthority()){
            	  params[1] = authority.getAuthority();
            	  super.update(params);  
              }
		    }
	  }
	  
	  
	  public void deleteAllAccount(){
		  new AccountDeleteAll(getDataSource()).delete();
		  new AuthorityDeleteAll(getDataSource()).delete();
	  }
	  
		
	  protected class AccountDeleteAll extends SqlUpdate{
		    protected AccountDeleteAll(DataSource ds){
		      super(ds, "DELETE FROM ACCOUNT ");
		      compile();
		    }

		    protected void delete(){
		      super.update();
		    }
	  }
	  
	  public List<Account> loadByGroup(String gid){
		  AccountLoadByGroupId accloader = new AccountLoadByGroupId(this.dataSource);
		  return accloader.execute(gid);
	  }
	  
	  protected class AccountLoadByGroupId extends MappingSqlQuery{
		    protected AccountLoadByGroupId(DataSource ds){
		      super(ds,
		            "SELECT * FROM account WHERE groupid = ? "
		      );
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      compile();
		    }

		    protected Object mapRow(ResultSet rs, int rownum)
		      throws SQLException{
		      Account account= new Account();
		      account.setLoginType(rs.getString("loginType"));
		      account.setUsername(rs.getString("username"));
		      account.setPassword(rs.getString("password"));
		     
		      account.setStatus(rs.getString("status"));
		      account.setIPAddress(rs.getString("ipaddress"));
		      account.setMask(rs.getString("mask"));
		      account.setChargeType(rs.getString("chargetype"));
		      account.setStartDate(rs.getString("startdate"));
		     
		      account.setTotalDate(new Integer(rs.getInt("totaldate")));
		      account.setTotalFlow(new Integer(rs.getInt("totalflow")));
		      account.setUsedFlow(new Integer(rs.getInt("usedflow")));
		      
		      account.setId(rs.getInt("accid"));
		      account.setMaxConcurrence(rs.getInt("maxconcurrence"));
              account.setGroupId(rs.getString("groupid"));
              
              account.setEvent(rs.getString("event"));
		      return account;
		    }
		  }	  
	  
	  public List<Account> loadAuthority(List<Account> accounts){
		  AccountLoadAuthorityById authLoader = new AccountLoadAuthorityById(this.dataSource);
		  for(Account account:accounts){
			  List<GrantedAuthority> auths = authLoader.execute(account.getId());
			  account.setGrantedAuthority(auths.toArray(new GrantedAuthority[0]));
		  }
		  return accounts;
	  }
	  
	  public void updateField(int accountID,String field,String value){
		  String sql = "update account set "+field+"='"+value+"' where accid="+accountID;
		  SqlUpdate enginee = new SqlUpdate(getDataSource(),sql);
		  enginee.update();
	  }
}
