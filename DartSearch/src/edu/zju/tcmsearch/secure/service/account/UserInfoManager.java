/**
 * 
 */
package edu.zju.tcmsearch.secure.service.account;
import edu.zju.tcmsearch.secure.domain.account.UserInfo;
/**
 * @author 倪亦柯
 *
 */
public interface UserInfoManager {
	
	public void userInfoInsert(UserInfo userInfo);
	public UserInfo userInfoLoadById(int id);
	public void delete(UserInfo userInfo);

}
