package edu.zju.tcmsearch.web.controller.cnki;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import edu.zju.tcmsearch.cnki.Document;
import edu.zju.tcmsearch.cnki.DocumentService;
import edu.zju.tcmsearch.cnki.DownloadFileConnection;
import edu.zju.tcmsearch.util.GBK2Big5.GB2Big5;

public class CnkiQueryController extends SimpleFormController {
	public static final String FieldValue="FieldValue";
	public static final String DocIndex="DocIndex";
	public static final String DOCLIST_INSESSION_KEY = "DOCLIST_INSESSION_KEY";
	
	private DocumentService documentService = new DocumentService();
	
	protected Map referenceData(HttpServletRequest request)throws Exception{
	    Map<String,Object> model = new HashMap<String,Object>();
		model.put("GB2Big5",GB2Big5.getInstance());
		return model;
    }
	
	protected Object formBackingObject(HttpServletRequest request)throws Exception{
		String value = request.getParameter(FieldValue);
		DocumentList list=null;
		if(null!=value){
		  list = new DocumentList();
		  list.docs = documentService.getDocumentList(value);
		  putDocumentListInSession(request,list);
		}else{
		  list = getDocumentListInSession(request);
		  if(null==list){
			  list = DocumentList.EMPTY_DOCUMENT_LIST;
		  }
		}
		return list;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response,Object command,BindException errors)throws Exception{
		DocumentList list =(DocumentList) command;
		int index = Integer.parseInt(request.getParameter(DocIndex));
		Document doc = list.get(index);
		DownloadFileConnection dfc = documentService.getFile(doc);
		dfc.writeToConnection(response);
		return null;
	}
	
	public static class DocumentList{
		public List<Document> docs;
		public List<Document> getDocs(){
			return docs;
		}
		public Document get(int index){
			return docs.get(index);
		}
		
		public static DocumentList EMPTY_DOCUMENT_LIST = null;
		static {
			EMPTY_DOCUMENT_LIST = new DocumentList();
			EMPTY_DOCUMENT_LIST.docs = Collections.EMPTY_LIST;
		}
	}
	
	protected DocumentList getDocumentListInSession(HttpServletRequest request){
		Object obj = request.getSession().getAttribute(DOCLIST_INSESSION_KEY);
		return obj instanceof DocumentList ? (DocumentList)obj : null;
	}
	
	protected void putDocumentListInSession(HttpServletRequest request,DocumentList list){
		 request.getSession().setAttribute(DOCLIST_INSESSION_KEY,list);
	}
}
