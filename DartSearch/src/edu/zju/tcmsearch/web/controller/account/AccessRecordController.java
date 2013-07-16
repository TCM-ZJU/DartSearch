package edu.zju.tcmsearch.web.controller.account;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import edu.zju.tcmsearch.secure.acegi.AccountAuthenticationProccessingFilter;
import edu.zju.tcmsearch.secure.domain.access.IAccessRecord;
import edu.zju.tcmsearch.secure.domain.fee.FeeRecord;
import edu.zju.tcmsearch.secure.service.access.IAccessRecordManager;
import edu.zju.tcmsearch.secure.service.account.IAccountManager;
import edu.zju.tcmsearch.secure.service.fee.IFeeManager;
import edu.zju.tcmsearch.util.web.MultiLinguaUtil;

public class AccessRecordController implements Controller {

	private static Logger logger= Logger.getLogger(AccessRecordController.class);
	
	private static final String ACEGI_ACCOUNT_ID_KEY = "j_acegi_account_id"; 
	private static final String ACEGI_ACCOUNT_UNIQNAME_KEY = "j_acegi_account_uniqueName";
	private IFeeManager manager ;
	private String successView;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse repsone) throws Exception {
		
		List<FeeRecord> recList;
        String accID = (String)request.getParameter("accountId");
        logger.debug("account id ="+accID);        
        if(null==accID){
           recList = java.util.Collections.EMPTY_LIST;	
        }else {
           recList = manager.getFeeRecord(Integer.parseInt(accID));
        }
        logger.debug("record list size ="+recList.size());
        Map<String,Object> model = new HashMap<String,Object>();
        model.put("recordList",recList);
        model.put("accId",accID);
        if(null==request.getParameter("onself")){
        	model.put("onself","no");
        }else{
        	model.put("onself","yes");
        }
        model.put("accId",accID);
        
        String ViewName = successView;
		return new ModelAndView(ViewName,model);
	}
	
	public void setFeeManager(IFeeManager manager){
		this.manager = manager;
	}
	
	public void setSuccessView(String view){
		this.successView = view;
	}
	
}
