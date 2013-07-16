/**
 * 
 */
package edu.zju.tcmsearch.secure.domain.account;

/**
 * @author 倪亦柯
 *
 */
public class UserInfo {
	
	private int id;
	
    private String contact_name="";
    
    private String sex="";
    
    private String contact="";
    
    
    
	/**
	 * @param contact
	 * @param contact_name
	 * @param id
	 * @param sex
	 */
	public UserInfo(String contact_name, String sex, String contact) {
		// TODO Auto-generated constructor stub
		this.contact = contact;
		this.contact_name = contact_name;
		this.sex = sex;
	}

	public UserInfo(int id,String contact_name, String sex, String contact) {
		// TODO Auto-generated constructor stub
		this.contact = contact;
		this.contact_name = contact_name;
		this.id=id;
		this.sex = sex;
	}
	
	/**
	 * 
	 */
	public UserInfo() {
		
		// TODO Auto-generated constructor stub
	}
	
	

	



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = null==contact?"":contact;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name==null ? "":contact_name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex==null ? "":sex;
	}
}
