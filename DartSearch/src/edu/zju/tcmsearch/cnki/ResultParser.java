package edu.zju.tcmsearch.cnki;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class ResultParser {
	static Logger logger=Logger.getLogger(ResultParser.class);
	private String content;
	private List<Document> docs = null;
	public ResultParser(String content){
		this.content = content;
	}
	
	public boolean parser(){
		List<String> list = new ArrayList<String>();
		int[] fieldIndex = new int[]{PatternFactory.INDEX_PRL};
        boolean ok=retrieval(content,PatternFactory.getResultListPattern(),list,fieldIndex);
        if(ok){
        	docs = parseDocument(list.get(0));
        	return true;
        }
        return false;
	}
	
	public List<Document> getDocument(){
		return this.docs;
	}
	
	protected List<Document> parseDocument(String content) {
		int[] fieldIndex = new int[]{1,2,5};
		List<String> field = new ArrayList<String>();
        List<Document> docs = new ArrayList<Document>();
        Scanner scan = new Scanner(content);
        boolean usePatternA = true;
        boolean ok = false;
        int count = 1;
        while(scan.hasNextLine()){
        	StringBuilder segment = new StringBuilder();
        	String line;
        	do{
        	 line = scan.nextLine();
        	}while(line.length()==0&&scan.hasNext());
        	
        	if(!scan.hasNext())
        		break;
        	
        	while(line.length()>0){
        		segment.append(line).append("\n");
        		line = scan.nextLine();
        	}
        	logger.debug("第"+(count++)+"段记录 ======================================================");
        	logger.debug(segment);
        	
        	if(usePatternA){
        		ok=retrieval(segment.toString(),PatternFactory.getDocumentPatternA(),field,fieldIndex);
        		if(!ok){
        			retrieval(segment.toString(),PatternFactory.getDocumentPatternB(),field,fieldIndex);
        		}
        		usePatternA = !ok;
        	}else{
        		ok=retrieval(segment.toString(),PatternFactory.getDocumentPatternB(),field,fieldIndex);
        		if(!ok){
        			retrieval(segment.toString(),PatternFactory.getDocumentPatternA(),field,fieldIndex);
        		}
        		usePatternA = ok;
        	}
        	if(field.size()>0){
        		Document doc = new Document();
        		doc.fileUrl = field.get(0);
        		doc.absUrl  = field.get(1);
        		doc.title   = field.get(2);        		
        		docs.add(doc);
        	}
        }
        return docs;
	}
	
	
	public boolean retrieval(String content,Pattern p,List<String> list,int[] logOut){
		list.clear();
		Matcher m = p.matcher(content);
		if(m.matches()){
			for(int i=0;i<logOut.length;i++){
				if(logOut[i]>m.groupCount())
					continue;
				list.add(m.group(logOut[i]));			
				logger.debug("============================================ 第"+logOut[i]+"值域");
				logger.debug(list.get(i));
			}
			return true;
		}
		return false;
	}	

}
