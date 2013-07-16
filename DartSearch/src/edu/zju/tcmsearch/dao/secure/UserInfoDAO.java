/**
 * 
 */
package edu.zju.tcmsearch.dao.secure;

import edu.zju.tcmsearch.secure.domain.account.UserInfo;

/**
 * @author 倪亦柯
 *
 */
public interface UserInfoDAO {
	
	public void insert(UserInfo userInfoForm);
	public void delete(UserInfo userInfo);
	public UserInfo loadById(int id);
//	public void update(UserInfo userInfoForm);

}
