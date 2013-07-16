/**
 * 
 */
package edu.zju.tcmsearch.dao.impl.secure;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.*;
import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlUpdate;

import edu.zju.tcmsearch.dao.secure.UserInfoDAO;
import edu.zju.tcmsearch.secure.domain.account.UserInfo;
/**
 * @author 倪亦柯
 *
 */
public class UserInfoDAOImpl implements UserInfoDAO{
	
	private DataSource dataSource;
	
	

	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	public UserInfo loadById(int id) {
		// TODO Auto-generated method stub
		UserLoadById userLoadById = new UserLoadById(getDataSource());
		List<UserInfo> userInfo = userLoadById.execute(id);
		if(userInfo.size()!=0)
			return userInfo.get(0);
		else 
			return null;
			               
	}
		
	protected class UserLoadById extends MappingSqlQuery{
	    protected UserLoadById(DataSource ds){
	      super(ds,
	            "SELECT * FROM userinfo WHERE id = ? "
	      );
	      declareParameter(new SqlParameter(Types.INTEGER));
	      compile();
	    }

	    protected Object mapRow(ResultSet rs, int rownum)
	      throws SQLException{
	      UserInfo userInfo = new UserInfo();
	      userInfo.setId(rs.getInt("id"));
          userInfo.setContact_name(rs.getString("contact_name"));
          userInfo.setSex(rs.getString("sex"));
          userInfo.setContact(rs.getString("contact"));

	      return userInfo;
	    }
	  }
	
	
	public void insert(UserInfo userInfoForm) {
		// TODO Auto-generated method stub
		UserInfoInsert userInfoInsert = new UserInfoInsert(getDataSource());
		userInfoInsert.insert(userInfoForm);
	}
	
	 protected class UserInfoInsert extends SqlUpdate{
		    protected UserInfoInsert(DataSource ds){
		      super(ds, "INSERT INTO USERINFO (id,contact_name,sex,contact) VALUES (?,?,?,?)");
		      declareParameter(new SqlParameter(Types.INTEGER));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      declareParameter(new SqlParameter(Types.VARCHAR));
		      compile();
		    }

		    protected void insert(UserInfo userInfoForm){
		      Object[] objs=new Object[]{new Integer(userInfoForm.getId()),userInfoForm.getContact_name(),userInfoForm.getSex(),userInfoForm.getContact()};
		      super.update(objs);
		    }
		  }

	 protected class UserInfoDelete extends SqlUpdate{
		    protected UserInfoDelete(DataSource ds){
		      super(ds, "DELETE FROM userinfo WHERE id= ?");
		      declareParameter(new SqlParameter(Types.INTEGER));
		      compile();
		    }

		    protected void delete(Integer id){
		      super.update(id.intValue());
		    }
		  }

	public void delete(UserInfo userInfo) {
		// TODO Auto-generated method stub
		UserInfoDelete userInfoDelete = new UserInfoDelete(getDataSource());
		userInfoDelete.delete(new Integer(userInfo.getId()));
		
	}
}

	                                                                      