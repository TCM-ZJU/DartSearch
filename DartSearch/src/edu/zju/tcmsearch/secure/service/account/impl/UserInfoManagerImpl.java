/**
 * 
 */
package edu.zju.tcmsearch.secure.service.account.impl;
import edu.zju.tcmsearch.dao.secure.UserInfoDAO;
import edu.zju.tcmsearch.secure.domain.account.UserInfo;
import edu.zju.tcmsearch.secure.service.account.UserInfoManager;
/**
 * @author 倪亦柯
 *
 */
public class UserInfoManagerImpl implements UserInfoManager{
	
	private UserInfoDAO userInfoDAO;
	
	

	public UserInfoDAO getUserInfoDAO() {
		return userInfoDAO;
	}



	public void setUserInfoDAO(UserInfoDAO userInfoDAO) {
		this.userInfoDAO = userInfoDAO;
	}



	public void userInfoInsert(UserInfo userInfo) {
		// TODO Auto-generated method stub
		
		userInfoDAO.insert(userInfo);
	}



	public UserInfo userInfoLoadById(int id) {
		// TODO Auto-generated method stub
		return userInfoDAO.loadById(id);
	}



	public void delete(UserInfo userInfo) {
		// TODO Auto-generated method stub
		userInfoDAO.delete(userInfo);
	}

}
