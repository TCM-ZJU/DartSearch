package edu.zju.tcmsearch.cnki;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

public class DocumentService {
	public static final Logger logger =Logger.getLogger(DocumentService.class);
	
	public List<Document> getDocumentList(String keyword){
		SmartHttpConnection shcon = new SmartHttpConnection();
		String content;
		try {
			content = shcon.getResultContent(keyword);
			logger.debug(content);
			shcon.close();
			ResultParser parser = new ResultParser(content);
			if(parser.parser()){
				return parser.getDocument();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.EMPTY_LIST;
	}
	
	public DownloadFileConnection getFile(Document doc){
		return new DownloadFileConnection(doc);
	}

}
