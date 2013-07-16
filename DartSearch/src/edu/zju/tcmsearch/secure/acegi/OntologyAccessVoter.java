package edu.zju.tcmsearch.secure.acegi;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.dao.impl.secure.OntologyAccessPrivDao;
import edu.zju.tcmsearch.query.domain.DartQuery;
import edu.zju.tcmsearch.query.domain.QueryResult;
import edu.zju.tcmsearch.secure.domain.access.OntologyAccessPrivilege;
import edu.zju.tcmsearch.secure.domain.fee.FeeRecord;
import edu.zju.tcmsearch.secure.service.fee.IFeeManager;
import edu.zju.tcmsearch.util.web.DartQueryUtil;
import edu.zju.tcmsearch.util.web.TreeNodeUtil;
import edu.zju.tcmsearch.web.form.query.TreeNode;

import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.ConfigAttribute;
import net.sf.acegisecurity.ConfigAttributeDefinition;
import net.sf.acegisecurity.intercept.web.FilterInvocation;
import net.sf.acegisecurity.vote.AccessDecisionVoter;

	public class OntologyAccessVoter implements AccessDecisionVoter {
		
		private static final Logger logger = Logger.getLogger(OntologyAccessVoter.class);

		private String rolePrefix = "ONTOLOGY_ACCESS_";
		
		private OntologyAccessPrivDao dao;

		private IFeeManager feeManager;	
		
		public void setFeeManager(IFeeManager feeManager){
			this.feeManager = feeManager;
		}			
		
		public void setOntologyAccessPrivDao(OntologyAccessPrivDao dao){
			this.dao = dao;
		}

	    public void setRolePrefix(String role){
	    	this.rolePrefix = role;
	    }
		
		public String getRolePrefix(){
			return this.rolePrefix;
		}
		
		public boolean supports(ConfigAttribute attribute) {
			if ((attribute.getAttribute() != null)&& attribute.getAttribute().startsWith(rolePrefix)) {
				return true;
			} else {
				return false;
			}
		}

		public boolean supports(Class clazz) {
			return FilterInvocation.class.isAssignableFrom(clazz);
		}

		public int vote(Authentication authentication, Object object,ConfigAttributeDefinition config) {

	        FilterInvocation fi = (FilterInvocation) object;
	        String url = ((FilterInvocation) object).getRequestUrl();  
	        
			if(!(authentication instanceof AccountAuthentication)){
				return ACCESS_ABSTAIN;
			}
	        if(!supports(object.getClass())){
	        	return ACCESS_ABSTAIN;
	        }
	        
	      
	        int result = ACCESS_ABSTAIN;
	        Iterator attr = config.getConfigAttributes();
	        while(attr.hasNext()){
	           ConfigAttribute attribute = (ConfigAttribute)attr.next();	
	           if(supports(attribute)){
	                 AccountAuthentication auth = (AccountAuthentication) authentication;
			    	 if(auth.account.isStopped()){
			    		 return ACCESS_DENIED;
			    	 }else if(null==auth.getSession()){
			    		 return ACCESS_DENIED;
			    	 }
	                 
			 		 DartQuery dartQuery = (DartQuery) DartQueryUtil.getFromSession(auth.getSession());
	                 Integer id = new Integer(auth.getDetails().getId());
	                 if(null!=dartQuery){
	                	 List<DartOntology> ontolgy = dartQuery.getConcernedOntology();
	                	 String goodsId = "";
	                	 String content = "";
	                	 for(DartOntology onto:ontolgy){
	                		 goodsId += onto.getIdentity()+"; ";
	                		 content += onto.getName()+" ";
	                	 }
	                	 content += ", page = "+fi.getHttpRequest().getParameter("pageNo");
	                	 
	                	 if(isAllowToAccess(ontolgy,id.intValue())){
	        		    	 int fee = feeManager.getOntologyFee(ontolgy);
	        		    	 Date now = new Date();
	        		    	 FeeRecord rec = new FeeRecord();
	        		    	 rec.setIntTime(now.getTime());
	        		    	 rec.setTime(now.toLocaleString());
	        		    	 rec.setAccountId(auth.getDetails().getId());		    	 
	                         rec.setType(FeeRecord.ONTOLOGY_FEE);
	        		    	 rec.setGoodsIdentity(goodsId);
	        		    	 rec.setContent(content);	
	        		    	 rec.setFee(fee);
	        		    	 feeManager.log(rec);
	        		    	 auth.account.useFlow(fee);
	                		 return ACCESS_GRANTED;
	                	 }else{
	                		 return ACCESS_DENIED;
	                	 }
	                 }
				     return  ACCESS_DENIED;
	           }
	        }
			return result;        
		}
		
		protected boolean isAllowToAccess(List<DartOntology> ontology,int id){
			List<OntologyAccessPrivilege> privs = dao.getPrivilege(id);
			for(DartOntology onto:ontology){
				OntologyAccessPrivilege p = new OntologyAccessPrivilege(id,onto.getIdentity(),true,false);
				if(!privs.contains(p))
					return false;
			}
			return true;
		}
		
	}
