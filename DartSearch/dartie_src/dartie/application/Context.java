package dartie.application;
import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;

import dartie.applets.GraphView;

public class Context {
	protected static Logger logger = Logger.getLogger(Context.class);
	
	private static Map<String,String> session2xml= new HashMap<String,String>();
	
	private static String DEFAULT_XML = 
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
	    "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\">"+
	    "<graph edgedefault=\"undirected\">"+
	    "<key id=\"name\" for=\"node\" attr.name=\"name\" attr.type=\"string\"/>"+
	    "<node id=\"root\">"+
	    "<data key=\"name\">empty result</data>"+
	    "</node>"+
		"</graph>"+
		"</graphml>";
	
	public static void put(String sessionId,String xml){
		logger.debug("context put @"+sessionId+" : "+xml);
		session2xml.put(sessionId,xml);
	}
	
	public static void remove(String sessionId){
		logger.debug("context remove @"+sessionId);
		session2xml.remove(sessionId);
	}
	
	public static String getXML(String sessionId){
		logger.debug("context get @"+sessionId);
		if(session2xml.containsKey(sessionId)){
			return session2xml.get(sessionId);
		}else{
			return DEFAULT_XML;
		}
	}
	
	public static String getDefaultXML(){
		return DEFAULT_XML;
	}
}
