/**
 * 
 */
package edu.zju.tcmsearch.web.form.secure;

/**
 * @author 倪亦柯
 *
 */
public class AccountInfoForm {
	
		
	    private String accountId;
	    
	    private String accountName;
		
		private String accountType;
		
		private String chargeType;
		
		private String amount;
		
		private String used;
		
		private String startDate;
		
		private String status;
		
		/**
		 * @return
		 */
		public String getAccountId() {
			return accountId;
		}
		/**
		 * @param accountId
		 */
		public void setAccountId(String accountId) {
			this.accountId = accountId;
		}
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public String getChargeType() {
			return chargeType;
		}
		public void setChargeType(String chargeType) {
			this.chargeType = chargeType;
		}
		
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String getUsed() {
			return used;
		}
		public void setUsed(String used) {
			this.used = used;
		}
		
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getAccountType() {
			return accountType;
		}
		public void setAccountType(String accountType) {
			this.accountType = accountType;
		}
		public String getAccountName() {
			return accountName;
		}
		public void setAccountName(String accountName) {
			this.accountName = accountName;
		}
		
	}



